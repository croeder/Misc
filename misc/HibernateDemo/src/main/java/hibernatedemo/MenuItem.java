package hibernatedemo;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class MenuItem {

	String name;
	Float price;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity=Restaurant.class)
	//@JoinColumn(name="Restaurant_id")
	Restaurant restaurant;
	
    public MenuItem() {
		super();
		name="";
		price=0.0F;
	}
    
	public MenuItem(String name, Float price) {
		super();
		this.name = name;
		this.price = price;
	}
	
	@Id  
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
    

    public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	
	public String  toString() {
		String restaurantName = "<none given>";
		// have some bad data in the db with null restaurant IDs
		if (getRestaurant() != null) {
			restaurantName = getRestaurant().getName();
		}
		return "MenuItem: " + id + ", " + name + "......" + price + "  at " + restaurantName;
	}


}
