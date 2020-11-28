public class Restaurant 
{
	private String id;
	private String name;
	private String location;
	private Integer price_range;
	private String restaurant_id;
	private String count;
	private String average_rating;
	
	public String getId()
	{return id;}
	
	public String getName()
	{return name;}
	
	public void setName(String name)
	{this.name = name;}
	
	public String getLocation()
	{return location;}
	
	public void setLocation(String location)
	{this.location = location;}
	
	public Integer getPrice_range()
	{return price_range;}
	
	public void setPrice_range(Integer price_range)
	{this.price_range = price_range;}
	
	public String getRestaurant_id()
	{return restaurant_id;}
	
	public String getCount()
	{return count;}
	
	public String getAverage_rating()
	{return average_rating;}
}
