package mainpackge;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;

import net.sf.json.JSONObject;


public class ClientView extends JFrame implements ActionListener{
	
	static String address="";
	JLabel youraddress=new JLabel("刀锋穿透提示您，您的访问地址",JLabel.CENTER);
	JLabel space=new JLabel();
	JTextField youraddresst=new JTextField(40);
	
	JLabel pzyyzh=new JLabel("配置已有域名");
	JLabel zcxym=new JLabel("免费注册新域名");
	
	JButton startup=new JButton("启动服务");
	JButton stopdown=new JButton("停止服务");
	JButton save=new JButton("保存配置");
	JButton register=new JButton("限免注册新域名");
	JButton highconfig=new JButton("细节配置");
	
	JPanel panelup=new JPanel();
	JPanel paneldown=new JPanel();
	JPanel panelleft=new JPanel();
	JPanel panelright=new JPanel();
	JPanel panelcenter=new JPanel();
	
	static JTextArea maintext=new JTextArea("刀锋(内网穿透)客户端\n\n\n\n\n\n无需公网IP、无需设置路由器、\n可穿透内网为全球1600多万用户提供服务\n的动态域名解析软件");
	JLabel newpassword=new JLabel("设定密码");
	JLabel newverserhost=new JLabel("设定域名(无协议头)");
	JPasswordField newtextpassword=new JPasswordField(10);
	JTextField newtextverserhost=new JTextField(10);
	static JTextArea ts=new JTextArea(1,20);
	
	JLabel lochost=new JLabel("本机地址(无协议头)+端口");
	JLabel verserhost=new JLabel("映射域名(无协议头)");
	JLabel password=new JLabel("域名密码");
	JTextField textlochost=new JTextField(10);
	JPasswordField textpassword=new JPasswordField(10);
	JTextField textverserhost=new JTextField(10);
	ClientChannel channel;
	ClientView(String s){
		super(s);
		Container c=this.getContentPane();
		
		panelup.setBorder(new EmptyBorder(10,10,10,10));
		paneldown.setBorder(new EmptyBorder(10,10,10,10));
		panelleft.setBorder(new EmptyBorder(10,10,10,10));
		panelright.setBorder(new EmptyBorder(10,10,10,10));
		this.setLayout(new BorderLayout());
		
		address=ClientChannel.serverAddress+":"+ClientChannel.serverPort+"/"+ClientChannel.webname+"/s/"+ClientChannel.verName;
		youraddresst.setText(address);
		maintext.setColumns(15);
		
		panelup.setLayout(new GridLayout(1,3));

		panelup.add(youraddress);
		panelup.add(youraddresst);
		panelup.add(space);
		
		panelleft.setLayout(new GridLayout(10,1));
		
		
		panelleft.setSize(30, 300);
		lochost.setSize(lochost.getWidth(), 5);
		textlochost.setSize(textlochost.getWidth(), 5);
		
		panelleft.add(pzyyzh);
		panelleft.add(lochost);
		panelleft.add(textlochost);
		panelleft.add(verserhost);
		panelleft.add(textverserhost);
		panelleft.add(password);
		panelleft.add(textpassword);
		panelleft.add(save);
		
		
		panelright.setLayout(new GridLayout(10,1));
		panelright.setSize(30, 300);
		panelright.add(zcxym);
		panelright.add(newverserhost);
		panelright.add(newtextverserhost);
		panelright.add(newpassword);
		panelright.add(newtextpassword);
		panelright.add(register);
		panelright.add(highconfig);
		
		
		paneldown.add(startup);
		paneldown.add(ts);
		paneldown.add(stopdown);
		
//		panelcenter.add(maintext);
		
		textlochost.setText(ClientChannel.realName);
		textverserhost.setText(ClientChannel.verName);
		textpassword.setText(ClientChannel.pssword);
		c.add(panelup,BorderLayout.NORTH);
		c.add(maintext,BorderLayout.CENTER);
//		c.add(panelcenter,BorderLayout.CENTER);
		c.add(paneldown,BorderLayout.SOUTH);
		c.add(panelleft,BorderLayout.WEST);
		c.add(panelright,BorderLayout.EAST);
		this.setSize(600,400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		stopdown.addActionListener(this);
		startup.addActionListener(this);
		save.addActionListener(this);
		register.addActionListener(this);
		highconfig.addActionListener(this);
		this.addWindowListener(new MyWindownListener());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String v=e.getActionCommand();
		if(v.equals("启动服务")){
			if(!ClientChannel.isserving){
				channel=new ClientChannel();
				channel.start();
				ts.setText("启动中。。。。");
			}else{
				ts.setText("已启动该服务");
			}
			address=ClientChannel.serverAddress+":"+ClientChannel.serverPort+"/"+ClientChannel.webname+"/s/"+ClientChannel.verName;
			youraddresst.setText(address);
		}
		if(v.equals("限免注册新域名")){
			System.out.println("申请中。。。。");
			String newhost=newtextverserhost.getText();
			String password=new String(newtextpassword.getPassword());
			System.out.println(newhost+":"+password);
			try {
				URL url=new URL("http://"+ClientChannel.serverAddress+":"+ClientChannel.serverPort+"/"+ClientChannel.webname+"/Register");
				System.out.println("http://"+ClientChannel.serverAddress+":"+ClientChannel.serverPort+"/"+ClientChannel.webname+"/Register");
				HttpURLConnection conn=(HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("serHost",newhost);
				conn.setRequestProperty("setPass",password);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.connect();
				
				InputStream is=conn.getInputStream();  
		        BufferedReader connreader=new BufferedReader(new InputStreamReader(is,"utf-8"));  
		        String line=null;
		        StringBuilder b=new StringBuilder();
		        while((line=connreader.readLine())!=null){  
		        	b.append(line);
		     
		        }  
				JSONObject re=JSONObject.fromObject(b.toString());
				if(re.optBoolean("result")){
					maintext.insert("免费申请成功\n",0);
				}else{
					maintext.insert("申请失败,原因："+re.optString("reason")+"\n",0);
				}
			} catch (Exception e1) {
				maintext.insert("申请失败,原因："+e1.getMessage()+"\n",0);
				e1.printStackTrace();
			}
		}
		if(v.equals("停止服务")){
			try {
				channel.socket.close();
				ClientChannel.isserving=false;
				channel.interrupt();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}if(v.equals("细节配置")){
			ConfigView config=new ConfigView("更多设置");
		}
		if(v.equals("保存配置")){
			ClientChannel.realName=textlochost.getText();
			ClientChannel.verName=textverserhost.getText();
			ClientChannel.pssword=new String(textpassword.getPassword());
			address=ClientChannel.serverAddress+":"+ClientChannel.serverPort+"/"+ClientChannel.webname+"/s/"+ClientChannel.verName;
			youraddresst.setText(address);
			ts.setText("保存成功");
		}
	}

	public class MyWindownListener  extends WindowAdapter  implements WindowListener {
		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}

	}

}
