package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.soap.Text;

import Utils.MyPanel;

public class ByerWindow extends JFrame{
	
	JLabel IP_label;
	JLabel price_label;
	JLabel result;
	JLabel img;
	JLabel logo;
	
	JLabel name_label;
	JLabel meterial_label;
	JLabel quality_label;
	JLabel wight_label;
	JLabel color_label;
	JLabel size_label;
	
	
	JButton sure;
	
	TextField ip_text;
	TextField price_text;
	
	TextArea intro;
	
	JPanel panel_top;
	JPanel panel_img;
	JPanel panel_intro;
	JPanel panel_result;
	JPanel panel_logo;
	
	public ByerWindow() {
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



	public TextField getPrice_text() {
		return price_text;
	}



	public void setPrice_text(TextField price_text) {
		this.price_text = price_text;
	}



	//初始化组件
	public void init(){
		
		this.setSize(800, 900);
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("安全拍卖客户端");
		Container container = this.getContentPane();
		container.setBackground(new Color(250, 218, 218));
		
	    IP_label = new JLabel("请输入ServerIP");
		price_label = new JLabel("请输入竞拍价格");
		result = new JLabel("欢迎登陆");
		result.setFont(new Font("微软雅黑",1,25));
		result.setOpaque(false);
		img = new JLabel(new ImageIcon("./img/icon.png"));	
		ip_text = new TextField();
		ip_text.setFont(new Font("微软雅黑",1,15));
		price_text = new TextField();
		price_text.setFont(new Font("微软雅黑",1,15));
		 name_label = new JLabel("品名：  韩雅轩宝玉器-314");
		 meterial_label = new JLabel("材质：    和田玉");
		 quality_label = new JLabel("质地：  细润");
		 wight_label = new JLabel("重量：   75.471克");
		 color_label = new JLabel("颜色：   白");
		 size_label = new JLabel("尺寸：   55*55*9mm");
		 
		 logo = new JLabel(new ImageIcon("./img/logo.png"));
		
		
		sure = new JButton("确认提交");
		
		panel_top = new JPanel(new GridLayout(3, 2));
		panel_top.setSize(350,100);
		panel_top.setLocation(150, 50);
		panel_top.add(IP_label);
		panel_top.add(ip_text);
		panel_top.add(price_label);
		panel_top.add(price_text);
		panel_top.add(new JLabel());
		panel_top.add(sure);
		this.add(panel_top);
		
		panel_img = new JPanel();
		panel_img.setSize(350,310);
		panel_img.setLocation(45, 200);
		panel_img.add(img);
		this.add(panel_img);
	
		
		
		panel_intro = new MyPanel(new ImageIcon("./img/1.png"));
		panel_intro.setLayout(new GridLayout(6, 1));
		panel_intro.setSize(350,310);
		panel_intro.setLocation(400, 200);
		panel_intro.add(name_label);
		panel_intro.add(meterial_label);
		panel_intro.add(quality_label);
		panel_intro.add(wight_label);
		panel_intro.add(color_label);
		panel_intro.add(size_label);
		this.add(panel_intro);
		
		panel_result = new JPanel();
		panel_result.setOpaque(false);
		panel_result.setSize(800, 100);
		panel_result.setLocation(100,600);
		panel_result.add(result);
		this.add(panel_result);
		 panel_top.setOpaque(false);
		 panel_img.setOpaque(false);
		 panel_intro.setOpaque(false);
		 
		 
		 panel_logo = new JPanel();
		 panel_logo.setSize(200, 80);
		 panel_logo.setOpaque(false);
		 panel_logo.setLocation(50, 700);
		 panel_logo.add(logo);
		 this.add(panel_logo);
		 
		 
	sure.setBackground(Color.red);
	sure.setFont(new Font("微软雅黑",1,15));
		
		this.setVisible(true);
		
		
		
	}
	public static void main(String[] args) {
		new ByerWindow();
	}
	//添加组件
	

	}
	
	
	


