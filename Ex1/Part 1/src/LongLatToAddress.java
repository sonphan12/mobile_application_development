import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.json.JSONObject;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;

public class LongLatToAddress {

	private JFrame frame;
	private JTextField txtLat;
	private JTextField txtLong;
	private JTextField txtAddress;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LongLatToAddress window = new LongLatToAddress();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LongLatToAddress() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 176);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblLongtitude = new JLabel("Latitude:");
		
		JLabel lblLongtitude_1 = new JLabel("Longtitude:");
		
		txtLat = new JTextField();
		txtLat.setColumns(10);
		
		txtLong = new JTextField();
		txtLong.setColumns(10);
		
		JButton btnRequestAddress = new JButton("Request Address");
		btnRequestAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String APIKey = "AIzaSyCUEjx5CmgjWahzse9ve_MtzdEEvRMGh74";
				String lat = txtLat.getText();
				String lng = txtLong.getText();
				String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + APIKey;
				try {
					URL url = new URL(urlString);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					con.setRequestProperty("User-Agent", "Mozilla/5.0");
					BufferedReader buffer =  new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));;
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = buffer.readLine()) != null) {
						response.append(inputLine+"\n");
					}
					buffer.close();
					JSONObject jsonAddress = new JSONObject(response.toString());
					if (jsonAddress.getString("status").equals("OK"))
						txtAddress.setText((jsonAddress.getJSONArray("results").getJSONObject(0).getString("formatted_address")));
					else
						txtAddress.setText(jsonAddress.getString("status"));

					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		JLabel lblAddress = new JLabel("Address:");
		
		txtAddress = new JTextField();
		txtAddress.setEditable(false);
		txtAddress.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnRequestAddress)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblLongtitude)
									.addGap(18)
									.addComponent(txtLat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
									.addComponent(lblLongtitude_1)))
							.addGap(18)
							.addComponent(txtLong, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAddress)
							.addGap(18)
							.addComponent(txtAddress, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLongtitude)
						.addComponent(lblLongtitude_1)
						.addComponent(txtLat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtLong, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnRequestAddress)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAddress)
						.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
