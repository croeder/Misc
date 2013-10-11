package hibernatedemo;

import org.dbunit.DBTestCase;
import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.Column;
import org.dbunit.Assertion;
import org.dbunit.dataset.csv.CsvURLDataSet;



import hibernatedemo.DerbyDemo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DerbyDemoTest extends DBTestCase {
	
    public DerbyDemoTest(String name)
    {
        super( name );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.apache.derby.jdbc.EmbeddedDriver" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:derby:myDB;create=true;"); //user=me;password=mine" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "me" );
        System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "mine" );
	// System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "" );
    }

    protected IDataSet getDataSet() throws Exception
    {
        return new CsvDataFileLoader().load("");
        //().build(new FileInputStream("dataset.xml"));
    }

    /**
     * use JDBC and Derby to select a table and compare it to a version stored in XML.
     * @throws Exception
     */
    public void testMe() throws Exception
    {
		DerbyDemo.createConnection();
    	DerbyDemo.dropTables();
    	DerbyDemo.createTables();
    	DerbyDemo.insertRestaurants();
    	
        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("restaurants");
        


        // Load expected data from an CSV dataset (broken)
        // works:
        java.net.URL url = CsvDataFileLoader.class.getClassLoader().getResource("restaurants.xml");
        if (url == null) { 
        	System.err.println("null url, add the resources dir to the classpath."); 
        }

        
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();

        IDataSet expectedDataSet = builder.build(url);
        ITable expectedTable = expectedDataSet.getTable("restaurants");

        // Assert actual database table match expected table
        Assertion.assertEquals(expectedTable, actualTable);
    }
    
    /**
     *  DbUnit is broken. The call to CsvDataFileLoader() calls getResource()
     *	on the class, not the class loader. This restricts (I believe) the
     *	area searched for the file to the jar that class is in, not the
     * 	whole classpath.
     */


    public void printDataSet(ITable actualTable) throws DataSetException {
    	ITableMetaData metaData = actualTable.getTableMetaData();
    	Column[] columns = metaData.getColumns();
        for (int rowNum = 0; rowNum < actualTable.getRowCount(); rowNum++) {
        	for (int colNum = 0; colNum < columns.length; colNum++ ) {
        		String colName = columns[colNum].getColumnName();
        		String s = actualTable.getValue(rowNum, colName).toString();
        		System.out.println("row: " + rowNum + ", " + colName + " "  + s );
        	}
        	System.out.println("---");
        }
    }

}
