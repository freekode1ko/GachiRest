import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.sound.sampled.ReverbType;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserForm {

	private JFrame frame;
	public JList<String> list = new JList();
	public JList<String> list_1 = new JList();
	private JTextField textField;
	private JTextArea textField_1;
	private JTextField textField_2;

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
				 } catch (Exception e) {e.printStackTrace();}
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
	private void initialize() 
	{
		frame = new JFrame();

		frame.getContentPane().setForeground(Color.GRAY);
		frame.setBounds(100, 100, 1124, 737);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		DefaultListModel<String> model = new DefaultListModel<>();
		DefaultListModel<String> model_1 = new DefaultListModel<>();
		list = new JList<>(model);
		list_1 = new JList<>(model_1);
		
		ArrayList<String> RestIdPool = new ArrayList<String>();
		ArrayList<String> RestNamePool = new ArrayList<String>();
		ArrayList<String> RestPlacePool = new ArrayList<String>();
		ArrayList<String> RestRatePool = new ArrayList<String>();
		
		JButton btnNewButton = new JButton("Загрузить");
		btnNewButton.addActionListener(new ActionListener() //BUTT
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				model.clear();
				RestIdPool.clear();
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
							RestNamePool.add(BigJson.getData().getRestaurants().get(i).getName());
							RestPlacePool.add(BigJson.getData().getRestaurants().get(i).getLocation());
							RestRatePool.add(BigJson.getData().getRestaurants().get(i).getPrice_range().toString());	
						}
					}
				} catch (IOException Ex) {System.out.println("Error: "+ Ex);} 
			}
		});
		btnNewButton.setBounds(10, 538, 135, 23);
		frame.getContentPane().add(btnNewButton);
			
		frame.addWindowListener(new WindowAdapter() //load rests on start program
		{
			@Override
			public void windowOpened(WindowEvent arg0) 
			{btnNewButton.doClick();}
		});
		
		JButton btnNewButton_1 = new JButton("Открыть коментарии"); // butt real rest comments
		btnNewButton_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				model_1.clear();
				if(!RestIdPool.isEmpty())
				{
					String Path = "https://gachirest.herokuapp.com/restaurants/";
					int selectedRest = list.getSelectedIndex(); // get secelted rest
					String RestID = RestIdPool.get(selectedRest); // get id of selected rest
					if(selectedRest != -1)
					{
						try
						{
							String ans = SenderGET(Path+RestID+"/");
							System.out.println(Path+RestID+"/"); //log to who
							System.out.println(ans); //log get json
							JsonObject jsonObject = new JsonParser().parse(ans).getAsJsonObject(); //str as jsonObj
							if(jsonObject.isJsonObject() && jsonObject.get("status").getAsString().equals("success")) //if json is correct and status success
							{
								Gson gson = new Gson();
								GETrest2 BigJson = gson.fromJson(ans, GETrest2.class);
								for(int i = 0; i < BigJson.getData().getReviews().size(); i++) //show all rest at ui
									model_1.addElement(BigJson.getData().getReviews().get(i).getName() + " - Рейтинг: " + BigJson.getData().getReviews().get(i).getRating() +
											", Коммернтарий к оценке: " + BigJson.getData().getReviews().get(i).getReview()); // read comment
							}
						}catch (IOException Ex) {System.out.println("Error: "+ Ex);} 
					}
				}
			}
		});
		btnNewButton_1.setBounds(166, 538, 178, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(10, 572, 334, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextArea();
		textField_1.setBounds(10, 603, 334, 84);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(354, 572, 155, 20);
		frame.getContentPane().add(comboBox);
		
		JButton btnNewButton_2 = new JButton("Оставить отзыв");
		btnNewButton_2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
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
							Review rev = AddComToRest(textField.getText(), RestID, comboBox.getSelectedItem().toString(), textField_1.getText()); // name, restoranID, score and comment
							Gson gson = new Gson();
							String json = gson.toJson(rev);
							System.out.println(json);
							String ans = SenderPOST(Path+RestID+"/addReview/", json);
							System.out.println(ans);
							btnNewButton_1.doClick();
							//todo add shit to list, clear shit etc
						}
						catch(Exception ex) {};
					}
				}
			}
		});
		btnNewButton_2.setBounds(354, 603, 155, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JCheckBox chckbxName = new JCheckBox("Имя");
		chckbxName.setSelected(true);
		chckbxName.setBounds(537, 599, 51, 23);
		frame.getContentPane().add(chckbxName);
		
		JCheckBox chckbxRate = new JCheckBox("Рейтинг");
		chckbxRate.setBounds(537, 625, 74, 23);
		frame.getContentPane().add(chckbxRate);
		
		textField_2 = new JTextField();
		textField_2.setBounds(617, 600, 123, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		comboBox_1.setSelectedIndex(0);
		comboBox_1.setBounds(617, 626, 123, 20);
		frame.getContentPane().add(comboBox_1);
		
		JButton btnNewButton_3 = new JButton("Поиск");
		btnNewButton_3.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(chckbxName.isSelected() || chckbxRate.isSelected())
				{
					model.clear();
					model_1.clear();
				}
				
				if(chckbxName.isSelected())
				{
					ArrayList<String> FoundRestIds = FindByName(textField_2.getText(),RestNamePool,RestIdPool);
					for(int i = 0; i < FoundRestIds.size(); i++)
					{model.addElement(LoadRestByID(FoundRestIds.get(i)));}
				}
				
				if(chckbxRate.isSelected())
				{
					ArrayList<String> FoundRestIds = FindByRate(comboBox_1.getSelectedItem().toString(),RestRatePool,RestIdPool);
					for(int i = 0; i < FoundRestIds.size(); i++)
					{model.addElement(LoadRestByID(FoundRestIds.get(i)));}
				}
			}
		});
		
		btnNewButton_3.setBounds(537, 571, 203, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		list.setBounds(10, 28, 1088, 212);
		frame.getContentPane().add(list);
		
		list_1.setBounds(10, 294, 1088, 212);
		frame.getContentPane().add(list_1);
		
	}
	
	private static ArrayList<String> FindByName(String RestName, ArrayList<String> RestNamePool, ArrayList<String> RestIdPool)
	{
		ArrayList<String> IDs = new ArrayList<String>();
		for(int i = 0; i < RestNamePool.size(); i++)
			if (RestName.toLowerCase().equals(RestNamePool.get(i).toLowerCase()))
				IDs.add(RestIdPool.get(i));
		return IDs;
	}
	
	private static String LoadRestByID(String RestId)
	{
		String out = "";
		String Path = "https://gachirest.herokuapp.com/restaurants/";
		try
			{
				String ans = SenderGET(Path+RestId+"/");
				System.out.println(Path+RestId+"/"); //log to who
				System.out.println(ans); //log get json
				JsonObject jsonObject = new JsonParser().parse(ans).getAsJsonObject(); //str as jsonObj
				if(jsonObject.isJsonObject() && jsonObject.get("status").getAsString().equals("success")) //if json is correct and status success
				{
					Gson gson = new Gson();
					GETrest2 BigJson = gson.fromJson(ans, GETrest2.class);
					out = ("Ресторан "+ BigJson.getData().getRestaurants().getName() + " по адрессу: " + 
							BigJson.getData().getRestaurants().getLocation() +", ценовой рейтинг " +
							BigJson.getData().getRestaurants().getPrice_range()+"/5, рейтинг пользователей: "+ 
							BigJson.getData().getRestaurants().getAverage_rating()+"/5"); // read rest
				}
			}catch (IOException Ex) {System.out.println("Error: "+ Ex);} 
		return out;
	}
	
	private static ArrayList<String> FindByRate(String RestRate, ArrayList<String> RestRatePool, ArrayList<String> RestIdPool)
	{
		ArrayList<String> IDs = new ArrayList<String>();
		for(int i = 0; i < RestRatePool.size(); i++)
			if (RestRate.toLowerCase().equals(RestRatePool.get(i).toLowerCase()))
				IDs.add(RestIdPool.get(i));
		return IDs;
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
	
	private static Review AddComToRest(String Name, String restaurant_id, String rating, String Review) // add ne comment for selected rest
	{
		Review com = new Review();
		com.setRestaurant_id(Integer.parseInt(restaurant_id));
		com.setName(Name);
		com.setRating(Integer.parseInt(rating));
		com.setReview(Review);
		return com;
	}
}