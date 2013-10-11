package hibernatedemo;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;


import java.util.List;


@Entity
public class Restaurant {
	
	//@OneToMany(mappedBy="MenuItem")
	@OneToMany
	List<MenuItem> menu;
	
	
	@Basic
	String name;
	@Basic
	String city;
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
    
    public List<MenuItem> getMenu() {
		return menu;
	}
	public void setMenu(List<MenuItem> menu) {
		this.menu = menu;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public Restaurant( String restaurantName, String city) {
		super();
		this.name = restaurantName;
		this.city = city;
	}
	public Restaurant() {
		super();
		this.id = 0;
		this.name = "";
		this.city = "";
	}
	
	
	public String toString() {
		return "Restaurant id: " + id + ", name:" + name + ", " + city;
	}

}
