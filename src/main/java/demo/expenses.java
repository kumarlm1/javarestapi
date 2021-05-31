package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONObject;
import org.json.JSONArray;

//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;


@Path ("/expenses")
public class expenses {
	String database_conn;
	Connection conn;
	databaseconn con;
	public expenses() {
	con = new databaseconn();
		conn = con.getconn();
}

	@Path("/{uid}")
	 @PUT 
	   @Produces("application/json")
	   @Consumes("application/json")
	 public JSONObject requestLogin(String str) {
	JSONObject obj = new JSONObject(str);
	JSONArray s= (JSONArray) obj.get("expense_item") ;

	 
	 System.out.println(s.get(0));
	return obj;
	
	
		  
//	     try {
//	      	 String query = "update user set name = ?,role =? where id = ?";
//				java.sql.PreparedStatement pd= conn.prepareStatement(query);
//				pd.setString(1,name);
//			    pd.setString(2,role);
//			    pd.setInt(3,id);
//			    pd.execute();
//			    
//			    if(pd.getUpdateCount() > 0)
//			    result.put("status","success");
//			    else
//			    result.put("status","error");
//				return result;
//	       }
//	       catch(SQLException e) {
//	    	   e.printStackTrace();
//	    	   result.put("status",e.toString());
//	      	 return result;
//	       }
		   
	   }
	      

	
	
	

}
