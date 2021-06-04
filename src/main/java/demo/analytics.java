package demo;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.simple.JSONObject;

@Path ("/analytics")
public class analytics {
	String database_conn;
	Connection conn;
	databaseconn con;
	public analytics() {
	con = new databaseconn();
		conn = con.getconn();
}
   @GET
   @Produces("application/json")
   public Response get(){
		JSONObject result = new JSONObject();
	   try {
	    String query = " select sum(total) as total,(select code from currency where id = cid) as currency from expense group by cid;";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		ResultSet rs =pd.executeQuery();
		
		JSONArray js;
		js = convert(rs);
		if(js != null) {
			result.put("status","success");
			result.put("data",js);
			 return Response.ok()
		               .entity(result.toString())
		             
		            
		               .build();
		}
		else {
			 result.put("status","error");
			 return Response.ok()
		               .entity(result.toString())
		  
		           
		               .build();
		}
			
	 }catch (Exception e) {
		result.put("status",e.toString());
		 return Response.ok()
	               .entity(result.toString())
	            
	             
	               .build();
	}
	   
   }
   @Path("/{id}")
   @GET
   @Produces("application/json")
   public Response get_by_user(@PathParam("id") int id){
		JSONObject result = new JSONObject();
	   try {
	    String query = "  select sum(total) as total ,(select code from currency where id = cid) as currency from expense where uid = ? group by cid";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		pd.setInt(1, id);
		ResultSet rs =pd.executeQuery();
		
		JSONArray js;
		js = convert(rs);
		if(js != null) {
			result.put("status","success");
			result.put("data",js);
			 return Response.ok()
		               .entity(result.toString())
		             
		            
		               .build();
		}
		else {
			 result.put("status","error");
			 return Response.ok()
		               .entity(result.toString())
		  
		           
		               .build();
		}
			
	 }catch (Exception e) {
		result.put("status",e.toString());
		 return Response.ok()
	               .entity(result.toString())
	            
	             
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