package mainpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class NetExchanger extends Thread{
	
	ServerSocket serSocket;
	
	@Override
	public void run() {
		startListen();
	}
	public void startListen(){
		try {
			serSocket=new ServerSocket(8088);
			while(true){
				try{
					Socket socket=serSocket.accept();
					BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
					String line=null;
					StringBuilder buider=new StringBuilder();
					while((line=reader.readLine())!=null&&!line.equals("end")){
						buider.append(line);
					}
					JSONObject re=new JSONObject();
					JSONObject ob=JSONObject.fromObject(buider.toString());
					String host=ob.optString("serHost");
					String pass=ob.optString("setPass");
					String sql="select * from users where serhost ='"+host+"' and serpass ='"+pass+"'";
					ResultSet set=tomysql.querysql(sql);
					BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
					if(set.next()){
						NetChannel server=new NetChannel(socket,host,writer,reader);
						NetChannelMangaer.serverMap.put(host, server);
						re.put("result",true);
					}else{
						re.put("result",false);
						re.put("reason","域名或者密码错误");
					}
					writer.write(re.toString());
					writer.flush();
					writer.write("\r\n");
					writer.flush();
					writer.write("end");
					writer.flush();
					writer.write("\r\n");
					writer.flush();
					set.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
