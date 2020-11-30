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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class AdminForm {

	private JFrame frame;
	public JList<String> list = new JList();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnNewButton_3;

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
				 } catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminForm() {initialize();}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 595, 505);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		list = new JList<>(model);
		
		textField = new JTextField();
		textField.setBounds(10, 339, 180, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(10, 370, 180, 20);
		frame.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(10, 401, 180, 20);
		frame.getContentPane().add(textField_2);
		
		ArrayList<String> RestIdPool = new ArrayList<String>();
		
		JButton btnNewButton_1 = new JButton("Загрузить список");
		btnNewButton_1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				model.clear();
				String Path = "https://gachirest.herokuapp.com/restaurants/"; //url with json to steal
				try {
					String ans = SenderGET(Path);
					System.out.println(ans);
					
					JsonObject jsonObject = new JsonParser().parse(ans).getAsJsonObject(); //str as jsonObj
					if(jsonObject.isJsonObject() && jsonObject.get("status").getAsString().equals("success")) //if json is correct and status success
					{
						Gson gson = new Gson();
						GETrest BigJson = gson.fromJson(ans, GETrest.class);
						for(int i = 0; i < BigJson.getData().getRestaurants().size(); i++) //show all rest at ui
						{
							model.addElement("Ресторан "+ BigJson.getData().getRestaurants().get(i).getName() + " по адрессу: " + 
									BigJson.getData().getRestaurants().get(i).getLocation() +", ценовой рейтинг " +
									BigJson.getData().getRestaurants().get(i).getPrice_range()+"/5, рейтинг пользователей: "+ 
									BigJson.getData().getRestaurants().get(i).getAverage_rating()+"/5");
							
							RestIdPool.add(BigJson.getData().getRestaurants().get(i).getId()); // add id into list for load comments later
						
						}
					}
				} catch (IOException Ex) {System.out.println("Error: "+ Ex);} 
			}
		});
		btnNewButton_1.setBounds(200, 338, 180, 23);
		frame.getContentPane().add(btnNewButton_1);
		
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
		        	btnNewButton_1.doClick();
		        }catch(IOException Ex) {System.out.println("Error: "+ Ex);}
			}
		});
		btnNewButton.setBounds(200, 369, 180, 23);
		frame.getContentPane().add(btnNewButton);
		
		
		JButton btnNewButton_2 = new JButton("Удалить ресторан");
		btnNewButton_2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(!RestIdPool.isEmpty())
				{
					int selectedRest = list.getSelectedIndex(); // get secelted rest
					String RestID = RestIdPool.get(selectedRest); // get id of selected rest
					String Path = "https://gachirest.herokuapp.com/restaurants/";
					if(selectedRest != -1) 
					{
						try 
						{
							String ans = SenderDEL(Path+RestID);
							System.out.println(ans);
							btnNewButton_1.doClick();
						}catch(IOException Ex) {System.out.println("Error: "+ Ex);} 
					}
				}
			}
		});
		
		btnNewButton_2.setBounds(200, 400, 180, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("Изменить ресторан");
		btnNewButton_3.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(!RestIdPool.isEmpty())
				{
					int selectedRest = list.getSelectedIndex(); // get secelted rest
					String RestID = RestIdPool.get(selectedRest); // get id of selected rest
					String Path = "https://gachirest.herokuapp.com/restaurants/";
					if(selectedRest != -1) 
					{
				    	Restaurant rest = CreateNewRest(textField.getText(),textField_1.getText(),textField_2.getText());
				    	Gson gson = new Gson();
				        String json = gson.toJson(rest);
				        System.out.println(json);
				        
						try 
						{
							String ans = SenderPUT(Path+RestID, json);
				        	System.out.println(ans);
				        	btnNewButton_1.doClick();
						}catch(IOException Ex) {System.out.println("Error: "+ Ex);} 
					}
				}
			}
		});
		btnNewButton_3.setBounds(200, 434, 180, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		list.setBounds(10, 11, 559, 317);
		frame.getContentPane().add(list);
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
	
	private static String SenderGET(String PATH) throws IOException //nudis GET
	{
		URL url = new URL (PATH);
	     HttpURLConnection con = (HttpURLConnection)url.openConnection();
	     con.setRequestMethod("GET");
	     con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"); //as user-agent to get permission
	     con.setRequestProperty("Content-Type", "application/json; utf-8");
	     con.setRequestProperty("Accept", "application/json");
	     con.setDoOutput(true);
	     
	     try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) //build ans 
	     {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) 
		    	{response.append(responseLine.trim());}
		    
		    return response.toString();
	     }
	}
	private static String SenderDEL(String PATH) throws IOException //nudis DEL
	{
		URL url = new URL (PATH);
	     HttpURLConnection con = (HttpURLConnection)url.openConnection();
	     con.setRequestMethod("DELETE");
	     con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"); //as user-agent to get permission
	     con.setRequestProperty("Content-Type", "application/json; utf-8");
	     con.setRequestProperty("Accept", "application/json");
	     con.setDoOutput(true);
	     
	     try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) //build ans 
	     {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) 
		    	{response.append(responseLine.trim());}
		    
		    return response.toString();
	     }
	}
	
	private static String SenderPUT (String PATH, String json) throws IOException // send PUT request
	{
		URL url = new URL(PATH);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(String.format(json));
        osw.flush();
        osw.close();
        System.err.println(connection.getResponseCode());
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) //build ans 
	     {
		    StringBuilder response = new StringBuilder();
		    String responseLine = null;
		    while ((responseLine = br.readLine()) != null) 
		    	{response.append(responseLine.trim());}
		    
		    return response.toString();
	     }
	}
}
