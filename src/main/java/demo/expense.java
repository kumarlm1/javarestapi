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
	    String query = " select *,(select name from currency where id = cid) as currency,(select symbol from currency where id = cid) as symbol,(select name from merchant where id = mid) as customer from expense where id = ?;";
		java.sql.PreparedStatement pd= conn.prepareStatement(query);
		pd.setInt(1, id);
		ResultSet rs =pd.executeQuery();
		String query1 = "select id,amount,description,(select category from category where id = caid) as category from expense_item where eid = ?";
		java.sql.PreparedStatement pd1= conn.prepareStatement(query1);
		pd1.setInt(1, id);
		ResultSet rs1 =pd1.executeQuery();
		JSONArray js1;
		js1 =convert(rs,rs1);
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
	       String merchant = obj.getString("customer");
	       String date = obj.getString("date").toString();
	      String refno = obj.getString("refno");
	      float total = obj.getFloat("total");
	     try {
	    	 String  query = "select id from currency where name = ?";
	    java.sql.PreparedStatement  pd = conn.prepareStatement(query);
			  pd.setString(1,currency);
			  ResultSet  rs1 = pd.executeQuery();rs1.next();
			  int cid = rs1.getInt("id");
			  
			  query = "select id from merchant where name = ?";
			  pd = conn.prepareStatement(query);
			  pd.setString(1,merchant);
			  rs1 = pd.executeQuery();rs1.next();
			  int mid = rs1.getInt("id");
			
			  
	      	query = "insert into expense(date,refno,uid,total,cid,mid) values(?,?,?,?,?,?)";
	      pd= conn.prepareStatement(query);
				
				pd.setString(1,date);pd.setString(2, refno);
				pd.setInt(3,uid);pd.setFloat(4,total);
				pd.setInt(5,cid);pd.setInt(6,mid);
			pd.execute();
				
		    String query1 = "select max(id) from expense";
		    java.sql.PreparedStatement pd1= conn.prepareStatement(query1);
		    rs1 = pd1.executeQuery();rs1.next();
		    int id = rs1.getInt(1);
		    
		    JSONArray s= (JSONArray) obj.get("expense_item") ;
		    for (int i=0; i<s.length(); i++) {
		    	 JSONObject dat = s.getJSONObject(i);
		    	 System.out.println(dat.getFloat("amount"));
		    	 try {
		    		 
		    	
		    			  query = "select id from category where category = ?";
		    			  pd = conn.prepareStatement(query);
		    			  pd.setString(1,dat.getString("category"));
		    			  rs1 = pd.executeQuery();rs1.next();
		    			  int caid = rs1.getInt("id");
		    			 
		    		 
		    		 
		    	  query = "insert into expense_item(caid,description,amount,eid) values(?,?,?,?)";
	              pd= conn.prepareStatement(query);
	              pd.setInt(1,caid);
			      pd.setString(2,dat.getString("description"));
			      pd.setFloat(3,dat.getFloat("amount"));
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
	      String refno = obj.getString("refno");
	     float total = obj.getFloat("total");
	     try {
	    	 String query = "select id from currency where name = ?";
	    	  java.sql.PreparedStatement  pd1 = conn.prepareStatement(query);
			  pd1.setString(1,currency);
			  ResultSet rs2  = pd1.executeQuery();rs2.next();
			  int cid = rs2.getInt("id");
			  
			  query = "select id from merchant where name = ?";
			  pd1 = conn.prepareStatement(query);
			  pd1.setString(1,customer);
			  rs2 = pd1.executeQuery();rs2.next();
			  int mid = rs2.getInt("id");
			  
	      	 query = "update expense set date=?,refno=?,total=?,cid=?,mid=? where id = ?";
	       pd1= conn.prepareStatement(query);
				pd1.setString(1,date);pd1.setString(2, refno);
				pd1.setFloat(3,total);
				pd1.setInt(4,cid);
				pd1.setInt(5,mid);pd1.setInt(6,id);
				int rs=pd1.executeUpdate();
	
	       query = "select id from expense_item where eid = ?";
		      pd1= conn.prepareStatement(query);
				pd1.setInt(1,id);
				ArrayList<Integer> childs = new ArrayList(){}; 
			ResultSet rs1 =pd1.executeQuery();
			while(rs1.next()) {
				childs.add(rs1.getInt("id"));
			}
				
	    			
			System.out.println(childs);	
				
		    JSONArray s= (JSONArray) obj.get("expense_item") ;
		    for (int i=0; i<s.length(); i++) {
		    	 JSONObject dat = s.getJSONObject(i);
		    	 System.out.println(dat.getFloat("amount"));
		    	 int ids = dat.getInt("id");
		    	 childs.remove(new Integer(ids));
		    	 try {
		    		 
		    		 query = "select id from category where category = ?";
	    			  pd1 = conn.prepareStatement(query);
	    			  pd1.setString(1,dat.getString("category"));
	    			  rs1 = pd1.executeQuery();rs1.next();
	    			  int caid = rs1.getInt("id");
		    		 
		    		 
		    		 
		    	  query = "update expense_item set caid=?,description=?,amount=? where id = ?";
	              pd1= conn.prepareStatement(query);
	              pd1.setInt(1,caid);
			      pd1.setString(2,dat.getString("description"));
			      pd1.setFloat(3,dat.getFloat("amount"));
		          pd1.setInt(4,ids);
		          pd1.executeUpdate();
		          
		         
		          
		          
		          
		    	 }
		    	 catch(SQLException e) {
		    		 e.printStackTrace();
		    	 }
		      }
		    for(int j=0;j<childs.size();j++) {
		    	try {
			    	  query = "delete from expense_item where id = ?";
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
