package mainpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerStartListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		URL url=this.getClass().getResource("");
		
		try {
			String path = URLDecoder.decode(url.getPath(),"UTF-8");
			File f1=new File(path,"../../../config.txt");
			FileReader reader=new FileReader(f1);
			BufferedReader	Breader=new BufferedReader(reader);
			String line=null;
			while((line=Breader.readLine())!=null){
				if(!line.equals("")){
					line=line.trim();
					if(line.startsWith("dburl=")){
						tomysql.url=line.substring(line.indexOf("dburl=")+6);
						System.out.println(tomysql.url);
					}else if(line.startsWith("dbadmin=")){
						tomysql.admine=line.substring(line.indexOf("dbadmin=")+8);
						System.out.println(tomysql.admine);
					}else if(line.startsWith("dbpass=")){
						tomysql.root=line.substring(line.indexOf("dbpass=")+7);
						System.out.println(tomysql.root);
					}
				}
			}
			Breader.close();
			reader.close();
			NetExchanger netExchanger=new NetExchanger();
			netExchanger.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	
		
	}

}
