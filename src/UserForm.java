import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class UserForm {

	private JFrame frame;
	public JList<String> list = new JList();
	public JList<String> list_1 = new JList();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{try {
					UserForm window = new UserForm();
					window.frame.setVisible(true);
				 } catch (Exception e) {	e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserForm() {initialize();}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1124, 611);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		DefaultListModel<String> model = new DefaultListModel<>();
		DefaultListModel<String> model_1 = new DefaultListModel<>();
		list = new JList<>(model);
		list_1 = new JList<>(model_1);
		
		ArrayList<String> RestIdPool = new ArrayList<String>();
		
		JButton btnNewButton = new JButton("Загрузить");
		btnNewButton.addActionListener(new ActionListener() //BUTT
		{
			public void actionPerformed(ActionEvent arg0) 
			{
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
		btnNewButton.setBounds(10, 538, 135, 23);
		frame.getContentPane().add(btnNewButton);
			
		JButton btnNewButton_1 = new JButton("Открыть коментарии");
		btnNewButton_1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!RestIdPool.isEmpty())
				{
					String Path = "https://gachirest.herokuapp.com/restaurants/";
					int selectedRest = list.getSelectedIndex();
					String RestID = RestIdPool.get(selectedRest);
					if(selectedRest != -1)
					{
						try 
						{
							String ans = SenderGET(Path+RestID+"/");
							System.out.println(Path+RestID+"/");
							System.out.println(ans);
							JsonObject jsonObject = new JsonParser().parse(ans).getAsJsonObject(); //str as jsonObj
							if(jsonObject.isJsonObject() && jsonObject.get("status").getAsString().equals("success")) //if json is correct and status success
							{
								Gson gson = new Gson();
								GETrest2 BigJson = gson.fromJson(ans, GETrest2.class);
								for(int i = 0; i < BigJson.getData().getReviews().size(); i++) //show all rest at ui
									model_1.addElement(BigJson.getData().getReviews().get(i).getName() + " - Рейтинг: " + BigJson.getData().getReviews().get(i).getRating() +
											", Коммернтарий к оценке: " + BigJson.getData().getReviews().get(i).getReview());
							}
						}catch (IOException Ex) {System.out.println("Error: "+ Ex);} 
						
					}
				}
			}
		});
		btnNewButton_1.setBounds(166, 538, 178, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		list.setBounds(10, 28, 1088, 212);
		frame.getContentPane().add(list);
		
		list_1.setBounds(10, 294, 1088, 212);
		frame.getContentPane().add(list_1);
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
}
