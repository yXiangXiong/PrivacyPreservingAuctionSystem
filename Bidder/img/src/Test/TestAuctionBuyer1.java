package Test;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import GUI.ByerWindow;
import Paillier.*;

public class TestAuctionBuyer1 {
	static String IP;
	static String getPrice;
	
	TextField ip_text;
	TextField price_text;
	JLabel result;
	JButton sure;
	
	static BroadCast bClient;
	static CipherClient cClient;
	static CipherServer cServer;
	
	static BigInteger price, ePrice; 
	static int priceBitsLen;
	
	static Paillier paillier;
	static BigInteger g, n; //Paillier公钥
	
	static SecureRandom rnd = new SecureRandom();
	
	public TestAuctionBuyer1() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	ByerWindow window;
	private void init() {
		// TODO Auto-generated method stub
		window = new ByerWindow();
		 ip_text = window.getIp_text();
		 price_text = window.getPrice_text();
		 result = window.getResult();
		 sure = window.getSure();
		 
		 sure.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				IP = ip_text.getText();
				getPrice = price_text.getText();
				ip_text.setEnabled(false);
				
				result.setText("参与竞拍成功，价格已发送，请耐心等待。");
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							generatePrice();
							initBroadCastClient();
							ReceivePublicKey();
							encryptPrice();
							SendEncryptPrice();
							GetResult();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}).start();
			
						
						
					
				
				
				
			
			}
		});

	}
	
	private static void generatePrice() throws Exception {
	priceBitsLen = 50;
	price = new BigInteger(getPrice);
	System.out.println("price = "+price);
	}
	
	private static void initBroadCastClient() throws Exception {
	bClient = new BroadCast();
	bClient.Init();
	}
	
	private static void ReceivePublicKey() throws Exception {
	g = bClient.ReceiveBigInteger();
	System.out.println("g = "+g);
	n = bClient.ReceiveBigInteger();
	System.out.println("n = "+n);
	}
	
	private static void encryptPrice() throws Exception { //加密报价
	paillier = new Paillier();
	
	ePrice = paillier.Encryption(price, g, n);
	System.out.println("ePrice = "+ePrice);
	}
	
	private static void SendEncryptPrice() throws Exception {
	cClient = new CipherClient();
		
 	cClient.create_socket_and_connect(IP, 23457);
 	cClient.SendCiphertxt(ePrice);
 	cClient.SendBuyerIp();
 	
 	cClient.cleanup();
	}
	
	private  void GetResult() throws Exception {
	String res = null;
		
	cServer = new CipherServer();
		
	cServer.createSocket(23459);
	res = cServer.ReceiveResult();
	
	result.setText(res);
	System.out.println("res = " + res);
	}
	
	public static void main(String[] args) throws Exception {
//	generatePrice();
//	initBroadCastClient();
//	ReceivePublicKey();
//	encryptPrice();
//	SendEncryptPrice();
//	GetResult();
		new TestAuctionBuyer1();
	}
	
}