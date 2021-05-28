package demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import org.json.JSONArray;

import com.mysql.jdbc.PreparedStatement;



@Path ("/user")
public class logins {
	String database_conn;
	Connection conn;
	public logins() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_api","root",""); 
		}
	catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
		 database_conn = "{'error':'database connection error'}";
	}
			 }
	
   @GET
   @Produces("application/json")
   public String requestLogin() throws SQLException {
	   try {
	    String query = "select * from user";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		ResultSet rs =pd.executeQuery();
		JSONArray js;
		try {
			js = convert(rs);
			return js.toJSONString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
		   
	} catch (Exception e) {
		return database_conn;
	}
   }
   
   
   
   
   @Path("/login/{name}")
   @POST
   @Produces("application/json")
   
 
   public String requestLogin1(@PathParam("name") String name) {
	   try {
		    String query = "select category,amount,description from expense_item where eid in "
		    		+ "(select uid from expense where uid in (select id from user where name = '"+name+"'))";
			java.sql.PreparedStatement pd= conn.prepareStatement(query);
			ResultSet rs =pd.executeQuery();
			JSONArray js;
			try {
				js = convert(rs);
				return js.toJSONString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
			   
		} catch (Exception e) {
			return database_conn;
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
