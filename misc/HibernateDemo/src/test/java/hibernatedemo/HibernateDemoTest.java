package hibernatedemo;

import hibernatedemo.Restaurant;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;


import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static  org.junit.Assert.assertTrue;
import static  org.junit.Assert.assertEquals;

public class HibernateDemoTest {
	
	// run the DerbyDemo beforehand as a way to create the table used here
	
	SessionFactory sessionFactory=null;
	
	/**
	 * configure Hibernate for using the annotation in the code.
	 * @throws Exception
	 */
	@Before 
	public void setUpbyAnnotation() throws Exception {
		AnnotationConfiguration cfg =  new AnnotationConfiguration();
		cfg.configure();
		sessionFactory =cfg.buildSessionFactory();
	}
	
	//@Before
	public void setup() throws Exception {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	@After
	public void tearDown() throws Exception {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
	
	/**
	 * test insert and select
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		// create and save a new entity
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Restaurant r = new Restaurant( "The Attic_hibernate", "Boulder");
		System.out.println("inserting: " + r.toString());
		session.save(r);
		session.getTransaction().commit();
		session.close();
		
		// select everything from the table and show it.
		session = sessionFactory.openSession();
		session.beginTransaction();
        @SuppressWarnings("unchecked")
		List<Restaurant> result = (List<Restaurant>) session.createQuery( "from Restaurant" ).list();
       //// assertEquals(3, result.size());
        for ( Restaurant event : (List<Restaurant>) result ) {
            System.out.println( "Resto: " + event.getId() + ", " + event.getCity() + ", " + event.getName() );
        }
		session.getTransaction().commit();
		session.close();
	}
	
	/** 
	 * test insert and select with where clause
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		// create and save a new entity, select with "where" clause
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Restaurant r = new Restaurant( "The Attic_hibernate", "Boulder");
		System.out.println("inserting: " + r.toString());
		session.save(r);
		session.getTransaction().commit();
		session.close();
		// select the attic from the table specifically
		session = sessionFactory.openSession();
		session.beginTransaction();
        List result2 = session.createQuery( "from Restaurant where name='The Attic_hibernate'" ).list();
        for ( Restaurant event : (List<Restaurant>) result2 ) {
            System.out.println( "Resto: " + event.getId() + ", " + event.getCity() + ", " + event.getName() );
        }
		session.getTransaction().commit();
		session.close();
	}


}
