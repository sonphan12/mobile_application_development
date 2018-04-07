package com.example.user.ex2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private Spinner spSrc, spDst;
    private EditText txtSrc;
    private TextView txtDst;
    private String arrItem[];
    private double arrToEUR[];
    private String concurrencyRespond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            addControls();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /***********************************************************************************************
     *Function: addControls                                                                        *
     *Purpose: Add all the controls of our app                                                     *
     *Parameters: None                                                                             *
     *Created by: SonPhan 2018/04/07                                                               *
     *Update:                                                                                      *
     *Comment:                                                                                     *
     **********************************************************************************************/
    private void addControls() throws JSONException {
        spSrc = findViewById(R.id.spSrc);
        spDst = findViewById(R.id.spDst);
        txtSrc = findViewById(R.id.txtSrc);
        txtDst = findViewById(R.id.txtDst);
        arrItem = new String[]{"USD", "VND", "AUD", "BRL", "CNY", "EUR", "INR", "JPY", "KRW"};
        concurrencyRespond = null;
        arrToEUR = new double[]{1.23, 28043.65, 1.60, 4.14, 7.75, 1, 79.84, 131.50, 1316.82}; //Default concurrency rate

        SharedPreferences sharedPreferences = getSharedPreferences("concurrency_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(isNetworkConnected()){ //Oh we have network connected, how convenient!
            requestConcurrencyInfo(); //Request rate information form fixer.io web service
            for (int i = 0; i < arrToEUR.length; i++){ //After that, save them in sharedpreferences in case of unconnected network
                editor.putFloat(arrItem[i], (float) arrToEUR[i]);
            }
            editor.apply();
        }
        else{//Network unavailable, get the latest offline data of concurrency rates
            for (int i = 0; i < arrToEUR.length; i++){
                arrToEUR[i] = sharedPreferences.getFloat(arrItem[i], (float) arrToEUR[i]);
            }

            Toast.makeText(this, "Unable to updated latest concurrency data due to network unconnected. You might see old data.", Toast.LENGTH_LONG).show();
        }

        //Init spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrItem);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spSrc.setAdapter(adapter);
        spDst.setAdapter(adapter);
        spSrc.setOnItemSelectedListener(new ItemSelectedListener());
        spDst.setOnItemSelectedListener(new ItemSelectedListener());

        //Data updates instantly right after user press a key
        txtSrc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cnvProc();
            }
        });


    }

    //Data updates instantly right after user change item selected on spinner
    private class ItemSelectedListener implements Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            cnvProc();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    /***********************************************************************************************
    *Function: cnvProc                                                                             *
    *Purpose: Process of concurrency converting                                                    *
    *Parameters: None                                                                              *
    *Created by: SonPhan 2018/04/07                                                                *
    *Update:                                                                                       *
    *Comment:                                                                                      *
    ***********************************************************************************************/
    private void cnvProc(){
        //Check if the input is a properly number
        if (txtSrc.getText().toString().isEmpty() || !txtSrc.getText().toString().matches("-?\\d+(\\.\\d+)?")
                || Double.parseDouble(txtSrc.getText().toString()) < 0) {
            txtDst.setText("");
            return;
        }
        double vSrc = Double.parseDouble(txtSrc.getText().toString());
        double vDst = Math.round(vSrc / arrToEUR[spSrc.getSelectedItemPosition()] *
                arrToEUR[spDst.getSelectedItemPosition()] * 100.0)/100.0;
        txtDst.setText(String.format("%.2f", vDst));
    }

    /***********************************************************************************************
     *Function: isNetworkConnected                                                                 *
     *Purpose: Check if network is connected                                                       *
     *Parameters: None                                                                             *
     *Created by: SonPhan 2018/04/07                                                               *
     *Update:                                                                                      *
     *Comment:                                                                                     *
     **********************************************************************************************/
    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /***********************************************************************************************
     *Function: requestConcurrencyInfo                                                             *
     *Purpose: Request concurrency rates data from fixer.io web service                            *
     *Parameters: None                                                                             *
     *Created by: SonPhan 2018/04/07                                                               *
     *Update:                                                                                      *
     *Comment:                                                                                     *
     **********************************************************************************************/
    private void requestConcurrencyInfo() throws JSONException {
        String url = "http://data.fixer.io/api/latest?access_key=ac70d76a03d1fe2cd3ca83d7e3fb9df4&symbols=USD,VND,AUD,BRL,CNY,EUR,INR,JPY,KRW&format=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                concurrencyRespond = response;
                JSONObject root = null;
                JSONObject rates = null;
                try {
                    root = new JSONObject(concurrencyRespond);
                    rates = root.getJSONObject("rates");
                    for (int i = 0; i < arrItem.length; i++){
                        Double rateValue = rates.getDouble(arrItem[i]);
                        arrToEUR[i] = rateValue;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Updated latest concurrency data", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
