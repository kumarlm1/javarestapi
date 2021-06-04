package demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
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
import org.json.simple.JSONObject;
import org.json.JSONArray;

import com.mysql.jdbc.PreparedStatement;



@Path ("/user")
public class user {
	String database_conn;
	Connection conn;
	databaseconn con;
	public user() {
	con = new databaseconn();
		conn = con.getconn();
}
	@Path("/{id}")
   @GET
   @Produces("application/json")
   public Response get(@PathParam("id") int id){
		JSONObject result = new JSONObject();
	   try {
	    String query = "select *,if(STRCMP(role,'Admin'),false,true) as isadmin from user where id = ?";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		pd.setInt(1, id);
		ResultSet rs =pd.executeQuery();
		ResultSet rs1 = rs;
		rs1.next();
	    int isadmin = rs1.getInt("isadmin");
	    rs1.beforeFirst();
	    System.out.println(isadmin);
		JSONObject js;
		js =convert(rs);
		
	    JSONArray users = new JSONArray();	 
		if(isadmin == 1) {
			query = "select name,role,phone from user where id != ?";
	        pd= conn.prepareStatement(query);
			pd.setInt(1, id);
			ResultSet rs2 =pd.executeQuery();
			 
			users = convert_array(rs2);
			
		}	
		
		
		if(js != null) {
			result.put("status","success");
			result.put("data",js);
			result.put("users", users);
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
 public Response requestLogin1(JSONObject json) {
	   System.out.println(json.toJSONString());
	   JSONObject result = new JSONObject();
	   String name = json.get("name").toString();
       String role = json.get("role").toString();
       String email =json.get("email").toString();
       String phone= json.get("phone").toString(); 
      try {
      	 String query = "insert into user(name,email,role,phone) values(?,?,?,?)";
			java.sql.PreparedStatement pd= conn.prepareStatement(query);
			pd.setString(1,name);
			pd.setString(2,email);
			pd.setString(3,role);
			pd.setString(4, phone);
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
   @Path("/{id}")
   @PUT 
   @Produces("application/json")
   @Consumes("application/json")
 public Response requestLogin(@PathParam("id") int id,JSONObject json) {
	   System.out.println(json.toJSONString());
	   JSONObject result = new JSONObject();
	   String name = json.get("name").toString();
       String role = json.get("role").toString();
       String phone= json.get("phone").toString(); 
       
     try {
      	 String query = "update user set name = ?,role =?,phone=? where id = ?";
			java.sql.PreparedStatement pd= conn.prepareStatement(query);
			pd.setString(1,name);
		    pd.setString(2,role);
		    pd.setString(3,phone);
		    pd.setInt(4,id);
		    pd.execute();
		    
		    if(pd.getUpdateCount() > 0)
		    result.put("status","success");
		    else
		    result.put("status","error");
		    return Response.ok()
		               .entity(result)
		               
		               .build();
       }
       catch(SQLException e) {
    	   e.printStackTrace();
    	   result.put("status",e.toString());
    	   return Response.ok()
	               .entity(result)
	               
	               .build();
       }
	   
   }
   
   
   
   @Path("/{id}")
   @DELETE
   @Produces("application/json")
   public Response delete(@PathParam("id") int id){
		JSONObject result = new JSONObject();
	   try {
	    String query = "delete from user where id =?";
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
	    	
	        System.out.println(obj.toString());
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
