package com.example.user.ex2;

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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private Spinner spSrc, spDst;
    private EditText txtSrc;
    private TextView txtDst;
    private String arrItem[];
    private double arrToUSD[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }
    private void addControls() {
        spSrc = findViewById(R.id.spSrc);
        spDst = findViewById(R.id.spDst);
        txtSrc = findViewById(R.id.txtSrc);
        txtDst = findViewById(R.id.txtDst);

        arrItem = new String[]{"USD", "VND", "AUD", "BRL", "CNY", "EUR", "INR", "JPY", "KRW"};
        arrToUSD = new double[]{1.0, 0.000044, 0.77, 0.30 , 0.16, 1.23, 0.015, 0.0094, 0.00094};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrItem);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spSrc.setAdapter(adapter);
        spDst.setAdapter(adapter);
        spSrc.setOnItemSelectedListener(new ItemSelectedListener());
        spDst.setOnItemSelectedListener(new ItemSelectedListener());
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

    private class ItemSelectedListener implements Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            cnvProc();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    private void cnvProc(){
        if (txtSrc.getText().toString().isEmpty() || !txtSrc.getText().toString().matches("-?\\d+(\\.\\d+)?")
                || Double.parseDouble(txtSrc.getText().toString()) < 0) {
            txtDst.setText("");
            return;
        }
        double vSrc = Double.parseDouble(txtSrc.getText().toString());
        double vDst = Math.round(vSrc * arrToUSD[spSrc.getSelectedItemPosition()] /
                arrToUSD[spDst.getSelectedItemPosition()] * 100.0)/100.0;
        txtDst.setText(String.format("%.2f", vDst));
    }
}
