package mainpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServerExchanger extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		String strDirPath = req.getSession().getServletContext().getRealPath("/");
		System.out.println("strDirPath:"+strDirPath);
		System.out.println("req.getContextPath():"+req.getContextPath());
		String url=req.getRequestURI();
		System.out.println(url);
		String webname=this.getServletContext().getContextPath();
	    String hostAndPath=url.substring(webname.length()+3);
	    System.out.println(hostAndPath);
	    String host=hostAndPath;
	    int first=hostAndPath.indexOf("/");
	    if(first>0){
	    	host=host.substring(0,first);
	    }
		JSONObject protocal=new JSONObject();
		protocal.put("method", req.getMethod());
		String urlparams=req.getQueryString();
		if(urlparams!=null){
			hostAndPath+="?"+urlparams;
		}
		protocal.put("serAdress",hostAndPath);
		Enumeration<String> hms=req.getHeaderNames();
		while(hms.hasMoreElements()){
			String hm=hms.nextElement();
			Enumeration<String> hds=req.getHeaders(hm);
			JSONArray hs=new JSONArray();
			while(hds.hasMoreElements()){
				String hd=hds.nextElement();
				hs.add(hd);
			}
			protocal.put(hm,hs);
		}
		NetChannel server=NetChannelMangaer.serverMap.get(host);
		BufferedReader reqreader =new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));
		if(server!=null){
			try{
				server.writer.write(protocal.toString()+"\r\nend\r\n");
				server.writer.flush();
				String line=null;
				while((line=reqreader.readLine())!=null){
					server.writer.write(line);
					server.writer.flush();
				}
				server.writer.write("\r\nend\r\n");
				server.writer.flush();
				PrintWriter respwriter= resp.getWriter();
				if(respwriter!=null){
					BufferedReader reader=server.getReader();
					line=null;
					while((line=reader.readLine())!=null&&!line.equals("end")){
						respwriter.write(line);
						respwriter.flush();
					}	
				}
			}catch(SocketException e){
				NetChannelMangaer.serverMap.remove(host);
			}
		}else{
			PrintWriter respwriter= resp.getWriter();
			respwriter.write("找不到服务器："+host);
			respwriter.flush();
			respwriter.close();
		}
	}

}
