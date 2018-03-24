package mainpack;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class Register extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String serHost=req.getHeader("serHost");
		String setPass=req.getHeader("setPass");
		System.out.println(serHost+":"+setPass);
		String sql="insert into users (serhost,serpass) values ('"+serHost+"','"+setPass+"')";
		JSONObject re=tomysql.updatesql(sql);
		resp.getWriter().write(re.toString());
	}

}
