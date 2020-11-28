import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.awt.event.ActionEvent;

public class AdminForm {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{public void run() 
			{try {
					AdminForm window = new AdminForm();
					window.frame.setVisible(true);
				 } catch (Exception e) {	e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 244, 237);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(26, 28, 180, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(26, 71, 180, 20);
		frame.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(26, 114, 180, 20);
		frame.getContentPane().add(textField_2);
		
		JButton btnNewButton = new JButton("Добавить ресторан");
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				String Path = "https://gachirest.herokuapp.com/restaurants"; //to who url
		    	Restaurant rest = CreateNewRest(textField.getText(),textField_1.getText(),textField_2.getText());
		    	Gson gson = new Gson();
		        String json = gson.toJson(rest);
		        System.out.println(json);
		        try 
		        {
		        	String ans = SenderPOST(Path, json);
		        	System.out.println(ans);
		        }catch(IOException Ex) {System.out.println("Error: "+ Ex);}
			}
		});
		btnNewButton.setBounds(26, 159, 180, 23);
		frame.getContentPane().add(btnNewButton);
		
	}
	
	private static String SenderPOST(String PATH, String json) throws IOException //nudis sender(POST) (must be more agile - later) and return serv ans
	{
        HttpPost post = new HttpPost(PATH);
        post.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
        post.setHeader("Content-type", "application/json");
        
        StringEntity body = new StringEntity(json);
        post.setEntity(body);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        // use org.apache.http.util.EntityUtils to read json as string
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
	}
	
	private static Restaurant CreateNewRest(String Name, String Location, String Price_Range)
	{
		Restaurant rest = new Restaurant();
		rest.setName(Name);
		rest.setLocation(Location);
		rest.setPrice_range(Integer.parseInt(Price_Range));
		return rest;
	}
}
