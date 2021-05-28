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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Path ("/expense")
public class expense {
	String database_conn;
	Connection conn;
	public expense() {
		
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
	
	   @Path("/{id}")
	   @GET
	   @Produces("application/json")
	   public String expense_data(@PathParam("id") String id) throws SQLException {
		   try {
		    String query = "select category,amount,description from expense_item where eid ="+id+"";
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
	   
	   
	   
	   
 @POST
@Produces("application/json")
@Consumes("application/json")
	 
public String requestLogin1(JSONObject json) {
	         String currency = json.get("currency").toString();
	         String date = json.get("date").toString();
	         String customer =json.get("customer").toString();
	         String email =json.get("email").toString();
	         int refno = Integer.valueOf(json.get("refno").toString());
	         
	         
	         try {
	        	 String query = "select id from user where email = '"+email+"'";
				java.sql.PreparedStatement pd= conn.prepareStatement(query);
				ResultSet rs =pd.executeQuery();
				int id=rs.getInt(0);
	         }
	         catch(Exception e) {
	        	 return "invalid email";
	         }
	         
		   try {
			    String query = "insert into table expense ";
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
