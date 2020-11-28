
public class Review 
{
	private String id;
	private Integer restaurant_id;
	private String name;
	private String review;
	private Integer rating;

	public String getId() 
	{return id;}
	
	public Integer getRestaurant_id() 
	{return restaurant_id;}
	
	public void setRestaurant_id(Integer restaurant_id) 
	{this.restaurant_id = restaurant_id;}
	
	public String getName() 
	{return name;}
	
	public void setName(String name) 
	{this.name = name;}
	
	public String getReview() 
	{return review;}
	
	public void setReview(String review) 
	{this.review = review;}
	
	public Integer getRating() 
	{return rating;}
	
	public void setRating(Integer rating) 
	{this.rating = rating;}

}
