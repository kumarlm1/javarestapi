package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class databaseconn {

		String database_conn;
		Connection conn;
		String url="jdbc:mysql://localhost:3306/expense_api";
		public databaseconn() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			
			//	conn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306?useSSL=false","d9tGwmyQPo","96CSFAmu2a"); 
			this.conn = DriverManager.getConnection(url,"root","");
			}
		catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 database_conn = "{'error':'database connection error'}";
			 }
			}
		
		public Connection getconn() {
			return this.conn;
			}
		
		public void close() {
			try {
				this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public static JSONArray convert(ResultSet resultSet) throws Exception {
			   
		    JSONArray jsonArrays = new JSONArray();
		 
		    while (resultSet.next()) {
		 
		        int columns = resultSet.getMetaData().getColumnCount();
		        JSONObject obj = new JSONObject();
		 
		        for (int i = 0; i < columns; i++)
		            obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
		 
		        jsonArrays.add(obj);
		    }
		    return jsonArrays;
		}

}
