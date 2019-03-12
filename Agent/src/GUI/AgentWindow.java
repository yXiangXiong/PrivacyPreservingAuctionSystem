// Copyright (C) 2017 by LiTeam
package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AgentWindow extends JFrame{
	JLabel IP_label;
	JLabel result;
	
	
	
	JButton sure;
	
	TextField ip_text;
	
	TextArea content;


	
	JPanel panel_top;
	JPanel panel_result;
	JPanel panel_content ;
	
	public AgentWindow() {
		// TODO Auto-generated constructor stub
		
				init();
		
		
	}
	
	
	
	public JLabel getResult() {
		return result;
	}



	public void setResult(JLabel result) {
		this.result = result;
	}



	public JButton getSure() {
		return sure;
	}



	public void setSure(JButton sure) {
		this.sure = sure;
	}



	public TextField getIp_text() {
		return ip_text;
	}



	public void setIp_text(TextField ip_text) {
		this.ip_text = ip_text;
	}



	public TextArea getContent() {
		return content;
	}



	public void setContent(TextArea content) {
		this.content = content;
	}



	//³õÊ¼»¯×é¼þ
	public void init(){
		
		this.setSize(800, 900);
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Agent");
		String ip;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();

			ip_text = new TextField(ip);
			ip_text.setFont(new Font("Î¢ÈíÑÅºÚ",1,15));
			ip_text.setEnabled(false);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Container container = this.getContentPane();
		container.setBackground(new Color(250, 218, 218));
		
	    IP_label = new JLabel("±¾»úIP");

		result = new JLabel(new ImageIcon("./img/logo.png"));
		result.setFont(new Font("Î¢ÈíÑÅºÚ",1,25));
		result.setOpaque(false);
	
		sure = new JButton("È·ÈÏIP");
		
		content = new TextArea("»¶Ó­µÇÂ½\n",200,100,TextArea.SCROLLBARS_BOTH);
		content.setEditable(false);
		
		panel_top = new JPanel(new GridLayout(1, 2));
		panel_top.setSize(400,30);
		panel_top.setLocation(150, 50);
		panel_top.add(IP_label);
		panel_top.add(ip_text);
		//panel_top.add(sure);
		this.add(panel_top);
		
		
		panel_content = new JPanel();
		panel_content.setOpaque(false);
		panel_content.setSize(800,600);
		panel_content.setLocation(0,100);
		panel_content.add(content);
		this.add(panel_content);
		panel_top.setOpaque(false);
		
		
		panel_result = new JPanel();
		panel_result.setOpaque(false);
		panel_result.setSize(200, 100);
		panel_result.setLocation(250,700);
		panel_result.add(result);
		this.add(panel_result); 
	sure.setBackground(Color.red);
	sure.setFont(new Font("Î¢ÈíÑÅºÚ",1,15));

	//	this.setVisible(true);
	
		
	
		
		
		
	}
	public static void main(String[] args) {
SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		new AgentWindow().setVisible(true);
			}
		});
		
	}
	//Ìí¼Ó×é¼þ

}
