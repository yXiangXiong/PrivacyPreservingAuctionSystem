// Copyright (C) 2017 by LiTeam
package Test;

import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import GUI.ClientWindow;
import Paillier.*;
import Program.*;

public class TestAuctionClient {
	
	static JButton sure;
	
	static TextField ip_text;
	
	static TextArea content;
	static String IP;
	
	static int nPerson = 3;
	static int priceBitsLen = 50;
	
	static BroadCast bClient;
	static CipherServer cServer;
	static CipherClient cClient;
	
	static Paillier paillier;
	static BigInteger g, n;
	static InetAddress[] buyIps;
	static BigInteger[] ePkPrice, ePkRand, Rand, rand, ePkProduct;
	
	static SecureRandom rnd = new SecureRandom();
	public TestAuctionClient() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	private static void initBroadCastClient() throws Exception {
	bClient = new BroadCast();
	bClient.Init();
	}
	
	private static void ReceivePublicKey() throws Exception {
	g = bClient.ReceiveBigInteger();
	
	System.out.println(g);
	n = bClient.ReceiveBigInteger();

	System.out.println(n);
	}
	
	private static void ReceiveEncryptPrice() throws Exception {
	cServer = new CipherServer();
	ePkPrice = new BigInteger[nPerson];
	buyIps = new  InetAddress[nPerson];

	cServer.createSocket(23457);
	cServer.receiveCiphertxt(ePkPrice, buyIps);
	System.out.println("已经收到"+nPerson+"个买家加密后的价格:");
	for(int i = 0; i < nPerson; i++) {
		System.out.println(ePkPrice[i]);
		System.out.println(buyIps[i]);
		
	}
	}
	
	private static void generateEProduct() throws Exception {
	paillier = new Paillier();
		
	Rand = new BigInteger[nPerson];
	ePkRand = new BigInteger[nPerson];
	ePkProduct =  new BigInteger[nPerson];
	
	for(int i = 0; i < nPerson; i++) {
		Rand[i] = new BigInteger(priceBitsLen+20, rnd);
		ePkRand[i] = paillier.Encryption(Rand[i], g, n);
		
		ePkProduct[i] = ePkPrice[i].multiply(ePkRand[i]).mod(n.multiply(n));
		content.append("ePkProduct["+i+"] = " + ePkProduct[i]+"\n");
		System.out.println("ePkProduct["+i+"] = " + ePkProduct[i]);
	}
	}
	
	private static void sendEProduct() throws Exception {
	cClient = new CipherClient();
	
	cClient.create_socket_and_connect(IP, 23458);
	cClient.SendProduct(ePkProduct, buyIps);
	 	
	cClient.cleanup();
	
	}
	
	private static void GarbleCircuits() throws Exception {
	AuctionCommon.priceBitsLen = priceBitsLen + 20;
	Program.iterCount = 1;
	
	rand =  new BigInteger[nPerson];
	for(int i = 0; i < nPerson; i++) {
		rand[i] = Rand[i].negate();
		System.out.println("rand["+i+"] = "+rand[i]);
		
	}
	
	AuctionClient auctionclient = new AuctionClient(rand, nPerson);
	auctionclient.serverIPname = IP;
	auctionclient.run();
	}
	ClientWindow client;
	private void init() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 client = new ClientWindow();
					sure = client.getSure();
			        ip_text = client.getIp_text();
				    content = client.getContent();			    
				    sure.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							
							IP = ip_text.getText();
							ip_text.setEnabled(false);
							
							content.append("获取IP成功");
							
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									

									try {
										initBroadCastClient();
										ReceivePublicKey();
									ReceiveEncryptPrice();
									generateEProduct();
									sendEProduct();
									GarbleCircuits();
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}).start();
							
									
							
							
							
						}
					});
				
			}
		});
		
		
		

	}
	 
	public static void main(String[] args) throws Exception {
//	initBroadCastClient();
//	ReceivePublicKey();
//	ReceiveEncryptPrice();
//	generateEProduct();
//	sendEProduct();
//	GarbleCircuits();
		new TestAuctionClient();
		
	}
	
}