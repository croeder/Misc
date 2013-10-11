package hibernatedemo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD a table with JDBC
 * @author roederc
 *
 */
public class DerbyDemo {


	private static String embeddedURL 
		= "jdbc:derby:myDB;create=true;user=me;password=mine";
	private static String dbURL 
		= "jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine";
	private static String tableName = "restaurants";
	//private static String tableName2 = "restaurantnoautogen";
	// jdbc Connection
	private static Connection conn = null;
	private static Statement stmt = null;

	public static void main(String[] args)
	{
		try {	
			createConnection();
			dropTables();
			createTables();
			insertRestaurants();
			selectRestaurants();
			//dropTables();
			shutdown();
		}
		catch (SQLException x) {
			System.err.print(x);
			x.printStackTrace();
		}
	}

	static void createConnection()
	{
		try
		{
			//Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			//Get a connection
			conn = DriverManager.getConnection(embeddedURL); 
		}
		catch (Exception except)
		{
			except.printStackTrace();
		}
	}
	
	static void dropTables() {
		try {
			stmt = conn.createStatement();
			stmt.execute("drop table " + tableName);
			stmt.close();
			
		}
		catch (SQLException x) {
			System.err.println(" could not drop the tables, not necessarily a problem");
		}
	
	}
	
	static void createTables() {
		try {
			stmt = conn.createStatement();
			stmt.execute("create table " + tableName + " ( " 
					+ "id integer,"
					+ "restaurant varchar(30),"
					+ "city varchar(20) )");
//			
//			stmt.execute("create table " + tableName2 + " ( " 
//					+ "id integer,"
//					+ "restaurant varchar(30),"
//					+ "city varchar(20) )");
			stmt.close();
			
		}
		catch (SQLException x) {
			x.printStackTrace();
		}
	
	}

	/// JDBC CRUD below
	
	static void insertRestaurants() {
		insertRestaurant(5, "LaVals", "Berkeley");
		insertRestaurant(9, "Los Faralitos", "Aurora");
		insertRestaurant(6, "Solera", "Denver");
	}
	
	static void insertRestaurant(int id, String restName, String cityName)
	{
		try
		{
			stmt = conn.createStatement();
			stmt.execute("insert into " + tableName + " values (" +
					id + ",'" + restName + "','" + cityName +"')");
			stmt.close();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}
	}

	static List<Restaurant> selectRestaurants()  throws SQLException {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		stmt = conn.createStatement();
		ResultSet results = stmt.executeQuery("select * from " + tableName);
		ResultSetMetaData rsmd = results.getMetaData();
		int numberCols = rsmd.getColumnCount();
		for (int i=1; i<=numberCols; i++) {
			//print Column Names
			System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
		}

		while(results.next()) {
			// get columns by number
			int id = results.getInt(1); 
			// or by name
			//  results.getInt("id");
			String restName = results.getString(2);
			String cityName = results.getString(3);
			System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
			Restaurant r = new Restaurant(restName, cityName);
			restaurants.add(r);
		}
		results.close();
		stmt.close();
		return restaurants;
	}

	static void shutdown()
	{
		try
		{
			if (stmt != null)
			{
				stmt.close();
			}
			if (conn != null)
			{
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}           
		}
		catch (SQLException sqlExcept)
		{

		}

	}
}

