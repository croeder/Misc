package hibernatedemo;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

//import hibernatedemo.DerbyDemo;
import hibernatedemo.Restaurant;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.ArrayList;


public class JpaDemoTest {
	EntityManagerFactory entityManagerFactory = null;
	
	@Before 
	public void setUp() throws Exception {
		 entityManagerFactory 
			= Persistence.createEntityManagerFactory(
				"edu.ucdenver.ccp.uimatracking.jpademo");
	}
	
	
	@Test
	public void testInsert() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist(new Restaurant("The Attic_jpa", "Boulder"));
		/* one weakness with this implementation using hibernate is that the
		 * combination of fields name and city are not forced to be unique.
		 * it's not a weakness of Hibernate, but of my schema.
		 * They are not an index. So as this test is run repeatedly, you will
		 * get multiple restaurants with the same name, location combination.
		 * ...probably better to validate that the resto. doesn't exist before
		 * you persist it again.
		 */
		
		System.err.println("\n\n\n------------------------\n\n\n");
		
		Query q2 = entityManager.createQuery("from Restaurant"); // where name='The Attic_hibernate'");
		List<Restaurant> result2 = q2.getResultList();
		
		for (Restaurant r : result2) {
			System.out.println(r.toString());
		}
		
		
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	@Test 
	public void testQuery() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		
		Query q2 = entityManager.createQuery("from Restaurant ");//where name='Has Menu'");
		List<Restaurant> result2 = q2.getResultList();
		
		for (Restaurant r : result2) {
			System.out.println("..." + r.toString());
			for (MenuItem mi : r.getMenu()) {
				System.out.println("...    " + mi.toString());
			}
		}
		
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	@Test
	public void testMenu() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		
		// INSERT
		{
			Restaurant r = new Restaurant("Has Menu", "Denver");
			MenuItem m1 = new MenuItem("burger", 1.0F);
			m1.setRestaurant(r);
			MenuItem m2 = new MenuItem("fries", 0.5F);
			m2.setRestaurant(r);
			List<MenuItem> menu = new ArrayList<MenuItem>();
			menu.add(m1);
			menu.add(m2);
			r.setMenu(menu);
			entityManager.persist(m1);
			entityManager.persist(m2);
			entityManager.persist(r);
		}
		
		// QUERY menu items
		{
			Query q2 = entityManager.createQuery("from MenuItem");// where name='The Attic_hibernate'");
			List<MenuItem> result2 = q2.getResultList();
			
			for (MenuItem m : result2) {
				System.out.println("---------" + m.toString());
			}
			
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

}
