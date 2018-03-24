package mainpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class NetChannel{
	
	Socket socket;
	String hostName;
	String psswrod;
	BufferedWriter writer;
	BufferedReader reader;
	
	NetChannel(Socket soc,String name,BufferedWriter writer,BufferedReader reader){
		this.socket=soc;
		this.hostName=name;
		this.writer=writer;
		this.reader=reader;
	}
	
	public BufferedWriter getWriter() {
		
		if(writer==null){
			 OutputStream outs;
			try {
				outs = socket.getOutputStream();
				writer=new BufferedWriter(new OutputStreamWriter(outs,"UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer;
	}
	
	public BufferedReader getReader()  {
		if(reader==null){
			InputStream in;
			try {
				in = socket.getInputStream();
				reader=new BufferedReader(new InputStreamReader(in,"UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reader;
	}

}
