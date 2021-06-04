package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import demo.databaseconn;

@Path ("/login")
public class login {
	String database_conn;
	Connection conn;
	databaseconn con;
	public login() {
	con = new databaseconn();
		conn = con.getconn();
}
	
	   @Path("/{email}")
	   @GET
	   @Produces("application/json")
	   public Response requestLogin1(@PathParam("email") String email) {
		   JSONObject result=new JSONObject();
		   try {
			   System.out.println(email);
			    String query = "select count(id),id from user where email = ?";
				java.sql.PreparedStatement pd= conn.prepareStatement(query);
				pd.setString(1,email);
				ResultSet rs =pd.executeQuery();
				rs.next();
				if (rs.getInt(1) == 1) {   
					result.put("status","success");
				    result.put("id", rs.getInt(2));
					}
				else {
					result.put("status","invalid_email");
				   }
				  con.close();  
				 return Response.ok()
			               .entity(result)
			               .header("Access-Control-Allow-Origin", "*")
			               .build();
				   
			} catch (Exception e) {
				database_conn=e.toString();
				result.put("status",database_conn);
				  con.close();  
				  return Response.ok()
			               .entity(result)
			               .header("Access-Control-Allow-Origin", "*")
			               .build();
			}
		   
	 
		  
}
	   
	  
	   
	
}
