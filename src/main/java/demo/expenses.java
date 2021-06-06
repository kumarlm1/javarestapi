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
import javax.ws.rs.core.Response;

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

	   @Path("/{id}")
	   @GET
	   @Produces("application/json")
	   public Response get(@PathParam("id") int id){
			JSONObject result=new JSONObject();
		   try {
		    String query = " select *,(select name from currency where id = cid) as currency,(select symbol from currency where id = cid) as symbol,(select name from merchant where id = mid)"
		    		+ " as customer from expense where uid = ? order by id";
			java.sql.PreparedStatement pd= conn.prepareStatement(query);
			pd.setInt(1, id);
			ResultSet rs =pd.executeQuery();
			JSONArray js1;
			js1 =convert(rs);
			if(js1 != null) {
				result.put("status","success");
				result.put("data",js1);
				return Response.ok()
			               .entity(result.toString())
			               .header("Access-Control-Allow-Origin", "*")
			               .build();
			}
			else {
				result.put("status","invalid_user");
				return Response.ok()
			               .entity(result.toString())
			               .header("Access-Control-Allow-Origin", "*")
			               .build();}
				}
		   catch (Exception e) {
			   result.put("status",e.toString());
			   return Response.ok()
		               .entity(result.toString())
		               .header("Access-Control-Allow-Origin", "*")
		               .build();
		}
		}
	   
	   
	   public static JSONArray convert(ResultSet resultSet) throws Exception {
		   
		    JSONArray jsonArrays = new JSONArray();
		  
		 
		    while (resultSet.next()) {

		 
		        int columns = resultSet.getMetaData().getColumnCount();
		        JSONObject obj = new JSONObject();
		        
		 
		        for (int i = 0; i < columns; i++) {
		        	 
		             obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
		            
		    }
		        jsonArrays.put(obj);
		        System.out.println(jsonArrays.toString());
		        
		    }
		    
		    return jsonArrays;
		    
		 
		   
		}
	
	
	

}
