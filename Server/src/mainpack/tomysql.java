package mainpack;


import java.sql.*;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class tomysql {
	
	//	1、数据库路径，为jdbc:mysql://localhost:3306/mydata，其中mydata为数据库名称
	static String url="jdbc:mysql://localhost:3306/blade?useSSL=false";
	//	2、数据库账号
	static String admine="root";
	//	3、数据库密码
	static String root="123";
    //  4、jdbc的数据库驱动名称	
	static String driver="com.mysql.jdbc.Driver";
	
	
	public static JSONObject updatesql(String sql){
		JSONObject re=new JSONObject();
		re.put("result",false);
		String qqdata=null;
		Connection co=null;
		int at=-1;
		try{
			Class.forName(driver);
			co=(Connection) DriverManager.getConnection(url, admine,root);
			PreparedStatement sta=(PreparedStatement) co.prepareStatement(sql);
			at=sta.executeUpdate();
			if(at>0){
				re.put("result",true);
			}
		}catch(Exception e){
			e.printStackTrace();
			re.put("result",false);
			re.put("reason",e.getMessage());
		}finally{
			try {
				co.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return re;
	}
	
	
//	查询数据库方法封装
	public static ResultSet querysql(String sql){
		
		String qqdata=null;
		Connection co=null;
		ResultSet at=null;
		try{
			Class.forName(driver);
			co= DriverManager.getConnection(url, admine,root);
			PreparedStatement sta =co.prepareStatement(sql);
			at=sta.executeQuery();
			return at;
		}catch(Exception e){
			e.printStackTrace();
			return at;
		}
	}
	
	//	获取数据库连接
	public static Connection getConnection() throws Exception{
		Class.forName(driver);
		Connection con= DriverManager.getConnection(url, admine,root);
		return con;
	}
	
	

}
