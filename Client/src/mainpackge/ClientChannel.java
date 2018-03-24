package mainpackge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ClientChannel extends Thread{
	
	static HashMap<String,String> webroot=new HashMap();
	
	static boolean isserving=false;
	//	域名凭证
	static String pssword="null";
	
	//  自定义域名
	static String verName="ckj";
	
	//	本地服务器地址（无协议头）:端口
	static String realName="127.0.0.1:8080";
	
	//	真实服务器地址(无端口，无协议头)
//	static String serverAddress="39.106.34.156";
	static String serverAddress="127.0.0.1";
	//	真实服务器端口
	static String serverPort="8080";
	
	
	//	真实服务器项目名
	static String webname="Server";
	
	Socket socket;
	
	@Override
	public void run() {
		try {
			socket=new Socket(serverAddress,8088);
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
			JSONObject login=new JSONObject();
			login.put("serHost",verName);
			login.put("setPass",pssword);
			writer.write(login.toString());
			writer.flush();
			writer.write("\r\n");
			writer.flush();
			writer.write("end");
			writer.flush();
			writer.write("\r\n");
			writer.flush();
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			String result=null;
			StringBuilder builer=new StringBuilder();
			while(((result=reader.readLine())!=null)&&(!result.equals("end"))){
				builer.append(result);
			}
			JSONObject re=JSONObject.fromObject(builer.toString().trim());
			if(re.optBoolean("result")){
				System.out.println("通道建立成功");
				ClientView.ts.setText("启动成功");
				ClientView.maintext.insert("刀锋启动成功\n",0);
				isserving=true;
			}else{
				ClientView.ts.setText("启动失败");
				System.out.println("通道建立失败，失败原因："+re.optString("reason"));
				ClientView.maintext.insert("通道建立失败，失败原因："+re.optString("reason")+"\n", 0);
				return;
			}
			while(true){
				try{
					String line=null;
					builer=new StringBuilder();
					while((line=reader.readLine())!=null&&!line.equals("end")){
						builer.append(line);
					}
					JSONObject protocal=JSONObject.fromObject(builer.toString().trim());
					String method=protocal.optString("method");
					
					
					String verpath=protocal.optString("serAdress");
					String realpart=verpath.substring(verName.length());

					URL url=new URL("http://"+realName+realpart);
					System.out.println(realpart);
					try{
						HttpURLConnection conn=(HttpURLConnection)url.openConnection();
						conn.setRequestMethod(method);
						conn.setRequestProperty("host",realName);
						Iterator<String> set=protocal.keys();
						while(set.hasNext()){
							String key=set.next();
							JSONArray array=protocal.optJSONArray(key);
							if(array!=null){
								for(int i=0;i<array.size();i++){
									System.out.println(key+":"+array.getString(i));
									if(!key.equals("method")&&!key.equals("serAdress")&&!key.equals("host")){
										conn.setRequestProperty(key,array.getString(i));
									}
								}
							}
						}
						
						System.out.println();
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.setUseCaches(false);
						conn.connect();
						OutputStream os=conn.getOutputStream(); 
						BufferedWriter connwriter=new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
						while((line=reader.readLine())!=null&&!line.equals("end")){
							connwriter.write(line);
							System.out.println(line);
							connwriter.flush();
						}
						InputStream is=conn.getInputStream();  
				        BufferedReader connreader=new BufferedReader(new InputStreamReader(is,"utf-8"));  
				        line=null;
				        while((line=connreader.readLine())!=null){  
				        	writer.write(line);
				        	writer.flush();
				        	
				        }  
				        writer.write("\r\nend\r\n");
				        writer.flush();
					}catch(FileNotFoundException e){
						writer.write("您访问的资源不存在。。。。");
						writer.write("\r\nend\r\n");
				        writer.flush();
				        e.printStackTrace();
					}catch(Exception e){
						writer.write(e.getMessage());
						writer.write("\r\nend\r\n");
				        writer.flush();
					}
					
				}catch(SocketException e){
					isserving=false;
					ClientView.ts.setText("服务已停止");
					ClientView.maintext.insert("服务已停止，原因："+e.getMessage()+"\n", 0);
					break;
				}catch(Exception e){
				}
			}
		} catch (Exception e) {
			isserving=false;
			ClientView.ts.setText("服务已停止");
			ClientView.maintext.insert("服务启动失败，原因："+e.getMessage()+"\n", 0);
		}
	}
}
