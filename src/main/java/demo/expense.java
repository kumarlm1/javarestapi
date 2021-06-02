package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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


@Path ("/expense")
public class expense {
	String database_conn;
	Connection conn;
	databaseconn con;
	public expense() {
	con = new databaseconn();
		conn = con.getconn();
}

   @Path("/{id}")
   @GET
   @Produces("application/json")
   public Response get(@PathParam("id") int id){
		JSONObject result=new JSONObject();
	   try {
	    String query = "select * from expense_item where eid = ?";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		pd.setInt(1, id);
		ResultSet rs =pd.executeQuery();
		String query1 = "select * from expense where id = ?";
		java.sql.PreparedStatement pd1= conn.prepareStatement(query1);
		pd1.setInt(1, id);
		ResultSet rs1 =pd1.executeQuery();
		JSONArray js1;
		js1 =convert(rs1,rs);
		if(js1 != null) {
			result.put("status","success");
			result.put("data",js1);
			return Response.ok()
		               .entity(result.toString())
		               .header("Access-Control-Allow-Origin", "*")
		               .build();
		}
		else {
			result.put("status","invalid_item");
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
   
   
   
	   @Path("/{uid}")
	   @POST
	   @Produces("application/json")
	   @Consumes("application/json")
	 public Response requestLogin1(@PathParam("uid") int uid,String str) {
		   System.out.println(str);
		   JSONObject result = new JSONObject();
		
		   JSONObject obj = new JSONObject(str);
	      String currency = obj.getString("currency");
	       String customer = obj.getString("customer");
	       String date = obj.getString("date").toString();
	      int refno = obj.getInt("refno");
	      int total = obj.getInt("total");
	     try {
	      	String query = "insert into expense(currency,customer,date,refno,uid,total) values(?,?,?,?,?,?)";
	        java.sql.PreparedStatement pd= conn.prepareStatement(query);
				pd.setString(1,currency);pd.setString(2,customer);
				pd.setString(3,date);pd.setInt(4, refno);
				pd.setInt(5,uid);pd.setInt(6,total);
				int rs=pd.executeUpdate();
		    String query1 = "select max(id) from expense";
		    java.sql.PreparedStatement pd1= conn.prepareStatement(query1);
		    ResultSet rs1 = pd1.executeQuery();rs1.next();
		    int id = rs1.getInt(1);
		    
		    JSONArray s= (JSONArray) obj.get("expense_item") ;
		    for (int i=0; i<s.length(); i++) {
		    	 JSONObject dat = s.getJSONObject(i);
		    	 System.out.println(dat.getInt("amount"));
		    	 try {
		    	  query = "insert into expense_item(category,description,amount,eid) values(?,?,?,?)";
	              pd= conn.prepareStatement(query);
	              pd.setString(1,dat.getString("category"));
			      pd.setString(2,dat.getString("description"));
			      pd.setInt(3,dat.getInt("amount"));
		          pd.setInt(4,id);
		          pd.executeUpdate();
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
	    		result.put("status",e.toString());
	    		return Response.ok()
			               .entity(result.toString())
			               .header("Access-Control-Allow-Origin", "*")
			               .build();
	      	 }
	 }
	
	   
	   
	   @Path("/{id}")
	   @PUT
	   @Produces("application/json")
	   @Consumes("application/json")
	 public Response requestLogin(@PathParam("id") int id,String str) {
		   JSONObject result = new JSONObject();
		
		   JSONObject obj = new JSONObject(str);
	      String currency = obj.getString("currency");
	       String customer = obj.getString("customer");
	       String date = obj.getString("date").toString();
	      int refno = obj.getInt("refno");
	      int total = obj.getInt("total");
	     try {
	      	String query = "update expense set currency=?,customer=?,date=?,refno=?,total=? where id = ?";
	        java.sql.PreparedStatement pd= conn.prepareStatement(query);
				pd.setString(1,currency);pd.setString(2,customer);
				pd.setString(3,date);pd.setInt(4, refno);
				pd.setInt(5,total);
				pd.setInt(6,id);
				int rs=pd.executeUpdate();
				
				query = "select id from expense_item where eid = ?";
		      pd= conn.prepareStatement(query);
				pd.setInt(1,id);
				ArrayList<Integer> childs = new ArrayList(){}; 
			ResultSet rs1 =pd.executeQuery();
			while(rs1.next()) {
				childs.add(rs1.getInt("id"));
			}
				
	    			
			System.out.println(childs);	
				
		    JSONArray s= (JSONArray) obj.get("expense_item") ;
		    for (int i=0; i<s.length(); i++) {
		    	 JSONObject dat = s.getJSONObject(i);
		    	 System.out.println(dat.getInt("amount"));
		    	 int ids = dat.getInt("id");
		    	 childs.remove(new Integer(ids));
		    	 try {
		    	  query = "update expense_item set category=?,description=?,amount=? where id = ?";
	              pd= conn.prepareStatement(query);
	              pd.setString(1,dat.getString("category"));
			      pd.setString(2,dat.getString("description"));
			      pd.setInt(3,dat.getInt("amount"));
		          pd.setInt(4,ids);
		          pd.executeUpdate();
		          
		         
		          
		          
		          
		    	 }
		    	 catch(SQLException e) {
		    		 e.printStackTrace();
		    	 }
		      }
		    for(int j=0;j<childs.size();j++) {
		    	try {
			    	  query = "delete from expense_item where id = ?";
		              pd= conn.prepareStatement(query);
			          pd.setInt(1,childs.get(j));
			          pd.executeUpdate();
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
	    		result.put("status",e.toString());
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
		    String query = "delete from expense where id =?";
			java.sql.PreparedStatement pd= conn.prepareStatement(query);
			pd.setInt(1, id);
			pd.execute();
			
			result.put("status","success");
		   System.out.println(result.get("status"));

			 return Response.ok()
		               .entity(result.toString())
		               .build();
			   
		} catch (Exception e) {
			result.put("status","invalid_user");
			 return Response.ok()
		               .entity(result.toString())
		               .build();
		}
	   }       
	   
 public static JSONArray convert(ResultSet resultSet,ResultSet resultSet1) throws Exception {
		   
		    JSONArray jsonArrays = new JSONArray();
		    JSONArray jsonArrays1 = new JSONArray();
		 
		    while (resultSet.next()) {
		    	while(resultSet1.next()) {
		    		int columns1 = resultSet1.getMetaData().getColumnCount();
			        JSONObject obj1 = new JSONObject();
			        for (int j = 0; j < columns1; j++) 
			    obj1.put(resultSet1.getMetaData().getColumnLabel(j + 1).toLowerCase(), resultSet1.getObject(j + 1));
			           
			        jsonArrays1.put(obj1);
		    	}
		 
		        int columns = resultSet.getMetaData().getColumnCount();
		        JSONObject obj = new JSONObject();
		        
		 
		        for (int i = 0; i < columns; i++) {
		        	 
		             obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
		            obj.put("expense_item", jsonArrays1);
		    }
		        jsonArrays.put(obj);
		        System.out.println(jsonArrays.toString());
		        return jsonArrays;
		    }
		    
		    
		  return null;
		   
		}
	
	
	

}
