package mainpackge;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class ConfigView extends JFrame implements ActionListener{
	
	JLabel ts=new JLabel("一般无需配置此界面(请勿随意配置)");
	
	JLabel pzycfwq=new JLabel("配置纯地址");
	JPasswordField pzycfwqt=new JPasswordField(20);
	JLabel pzycdc=new JLabel("配置纯端口");
	JPasswordField pzycdct=new JPasswordField(20);
	JLabel pzfum=new JLabel("配置纯服务名");
	JPasswordField pzfumt=new JPasswordField(20);
	JButton save=new JButton("保存配置");
	
	ConfigView(String name){
		super(name);
		Container c=this.getContentPane();
		this.setLayout(new FlowLayout());
		ts.setSize(280, ts.getHeight());
		c.add(ts);
		c.add(pzycfwq);
		c.add(pzycfwqt);
		c.add(pzycdc);
		c.add(pzycdct);
		c.add(pzfum);
		c.add(pzfumt);
		c.add(save);
		this.setSize(280, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		save.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("保存配置")){
			String host=new String(pzycfwqt.getPassword());
			String port=new String(pzycdct.getPassword());
			String webname=new String(pzfumt.getPassword());
			ClientChannel.serverAddress=host;
			ClientChannel.serverPort=port;
			ClientChannel.webname=webname;
			
			JOptionPane.showMessageDialog(ConfigView.this, "保存成功，请重启服务(请勿关闭本程序)");
			this.setVisible(false);
			
		}
		
	}

}
