package demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.PreparedStatement;



@Path ("/cr")
public class category {
	String database_conn;
	Connection conn;
	databaseconn con;
	public category() {
	con = new databaseconn();
		conn = con.getconn();
}
   @GET
   @Produces("application/json")
   public Response get(@PathParam("id") int id){
		JSONObject result = new JSONObject();
	   try {
	    String query = "select * from category where id = ?";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		pd.setInt(1, id);
		ResultSet rs =pd.executeQuery();
		
		JSONObject js;
		js =convert(rs);
		if(js != null) {
			result.put("status","success");
			result.put("data",js);
			 return Response.ok()
		               .entity(result.toString())
		             
		            
		               .build();
		}
		else {
			 result.put("status","invalid_user");
			 return Response.ok()
		               .entity(result)
		              
		           
		               .build();
		}
			
	 }catch (Exception e) {
		result.put("status",e.toString());
		 return Response.ok()
	               .entity(result)
	             
	             
	               .build();
	}
	   
   }
   
   @Path("/all")
   @GET
   @Produces("application/json")
   public Response getall(){
		JSONObject result = new JSONObject();
	   try {
	    String query = "select * from category";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		ResultSet rs =pd.executeQuery();
		
		JSONArray js;
		js =convert_array(rs);
		if(js != null) {
			result.put("status","success");
			result.put("data",js);
			 return Response.ok()
		               .entity(result.toString())
		             
		            
		               .build();
		}
		else {
			 result.put("status","invalid_user");
			 return Response.ok()
		               .entity(result)
		              
		           
		               .build();
		}
			
	 }catch (Exception e) {
		result.put("status",e.toString());
		 return Response.ok()
	               .entity(result)
	             
	             
	               .build();
	}
	   
   }
   
   
   
   
   @POST
   @Produces("application/json")
   @Consumes("application/json")
 public Response requestLogin12(String str) {
	   JSONObject json = new JSONObject(str);
	   JSONObject result = new JSONObject();
	   String name = json.get("name").toString();
       String des = json.get("description").toString();
     
      try {
      	 String query = "insert into category(category,description) values(?,?)";
			java.sql.PreparedStatement pd= conn.prepareStatement(query);
			pd.setString(1,name);
			pd.setString(2,des);
		
			pd.execute();
		    result.put("status","success");
		    return Response.ok()
		               .entity(result)
		            
		               
		               .build();
       }
       catch(SQLException e) {
    	   
    	   e.printStackTrace();
    		result.put("status","error_duplicate_keys");
    		 return Response.ok()
		               .entity(result)
		               
		               .build();
       }
	   
   }
   @PUT 
   @Produces("application/json")
   @Consumes("application/json")
 public Response requestLogin1(String str) {
	   JSONObject json = new JSONObject(str);
	   System.out.println(json.toString());
	   JSONObject result = new JSONObject();
	
      try {
	   String query = "select id from category";
	   java.sql.PreparedStatement  pd1= conn.prepareStatement(query);
			
			ArrayList<Integer> childs = new ArrayList(){}; 
		ResultSet rs1 =pd1.executeQuery();
		while(rs1.next()) {
			childs.add(rs1.getInt("id"));
		}
	   
	   
       JSONArray s= (JSONArray) json.get("category_item") ;
	    for (int i=0; i<s.length(); i++) {
	    	 JSONObject dat = s.getJSONObject(i);
	    	 int ids = dat.getInt("id");
	    	 childs.remove(new Integer(ids));
	    	 try {
	    	  query = "update category set description=?,category=? where id = ?";
             pd1= conn.prepareStatement(query);
             pd1.setString(1,dat.getString("description"));
		      pd1.setString(2,dat.getString("name"));
	          pd1.setInt(3,ids);
	          pd1.executeUpdate();
	          }
	    	 catch(SQLException e) {e.printStackTrace();}
	    	
			    
			     
	    	 
	      }
	    for(int j=0;j<childs.size();j++) {
	    	try {
		    	  query = "delete from category where id = ?";
	              pd1= conn.prepareStatement(query);
		          pd1.setInt(1,childs.get(j));
		          pd1.executeUpdate();
		          }
		    	 catch(SQLException e) {
		    		 e.printStackTrace();
		    	 }
          }
	    result.put("status","success");
	      return Response.ok()
	               .entity(result.toString())
	               .header("Access-Control-Allow-Origin", "*")
	               .build();
      }
      catch(SQLException e) {
    	  
    	  e.printStackTrace();
    	  result.put("status","error");
    	  return Response.ok()
	               .entity(result.toString())
	               .header("Access-Control-Allow-Origin", "*")
	               .build();
      }
	   
   }
   
   
   
   @Path("/{id}")
   @DELETE
   @Produces("application/json")
   public Response delete(@PathParam("id") int id){
		JSONObject result = new JSONObject();
	   try {
	    String query = "delete from category where id =?";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		pd.setInt(1, id);
		pd.execute();
		 if(pd.getUpdateCount() > 0)
			 result.put("status","success");
	    else
			result.put("status","error");
		 return Response.ok()
	               .entity(result)
	               .build();
		   
	} catch (SQLException e) {
		result.put("status","You have expense!!");
		 return Response.ok()
	               .entity(result)
	               .build();
	}
   } 
   
   
   public static JSONObject convert(ResultSet resultSet) throws Exception {
	   JSONObject obj = new JSONObject();
	   while (resultSet.next()) {
	 
	        int columns = resultSet.getMetaData().getColumnCount();
	       
	 
	        for (int i = 0; i < columns; i++)
	            obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
	    	
	        return obj;
	    }
	  
	   return null;
	 
	}
   public static JSONArray convert_array(ResultSet resultSet) throws Exception {
	   
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

