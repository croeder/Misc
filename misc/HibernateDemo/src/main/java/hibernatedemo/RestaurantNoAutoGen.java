package hibernatedemo;
import javax.persistence.Id;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;




@Entity
public class RestaurantNoAutoGen {
	@Id
	int id;
	String restaurant;
	String city;
	
       
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public RestaurantNoAutoGen(int id, String restaurant, String city) {
		super();
		this.id = id;
		this.restaurant = restaurant;
		this.city = city;
	}
	public RestaurantNoAutoGen() {
		super();
		this.id = 0;
		this.restaurant = "";
		this.city = "";
	}
	
	
	public String toString() {
		return "Restaurant id: " + id + ", name:" + restaurant + ", " + city;
	}

}
