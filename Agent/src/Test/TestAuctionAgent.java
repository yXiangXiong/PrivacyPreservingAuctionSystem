// Copyright (C) 2017 by LiTeam
package Test;

import java.awt.TextArea;
import java.awt.TextField;
import java.math.BigInteger;
import java.net.InetAddress;
import GUI.AgentWindow;
import Paillier.*;
import Program.*;

public class TestAuctionAgent {
	
    static TextField ip_text;
	static TextArea content;
	static int nPerson = 3;//number of bidders
	static BroadCast bServer;
	static Paillier paillier;
	static CipherServer cServer;
	static CipherClient cClient;
	static BigInteger[] ePkProduct, ePkSum;
	static InetAddress[] buyIps;
	static int priceBitsLen = 50;
	
	
	public TestAuctionAgent() {
		// TODO Auto-generated constructor stub
		init();
	}
	private static void generatePaillier() {
	paillier = new Paillier(512, 64);
	}
	
	private static void initBroadCastServer() throws Exception {
	bServer = new BroadCast();
	bServer.Init();
	}
	
	private static void sendPublicKey() throws Exception {
	bServer.SendBigInteger(paillier.g);
	System.out.println("paillier.g = "+paillier.g);
	bServer.SendBigInteger(paillier.n);
	System.out.println("paillier.n = "+paillier.n);
	}
	
	private static void receiveEProduct() throws Exception {
	cServer = new CipherServer();
	ePkProduct  = new BigInteger[nPerson];
	buyIps = new  InetAddress[nPerson];
		
	cServer.createSocket(23458);
	cServer.receiveProduct(ePkProduct, buyIps);
	for(int i = 0; i < nPerson; i++) {
		System.out.println("ePkProduct["+i+"] = " + ePkProduct[i]);
	}
	}
	
	private static void DecryptEProduct() throws Exception {
	ePkSum = new BigInteger[nPerson];
		
	for(int i = 0; i < nPerson; i++) {
		ePkSum[i] = paillier.Decryption(ePkProduct[i]);
		System.out.println("ePkSum[" + i +"] = " + ePkSum[i]);
	}
	}
	 
	private static void GarbleCircuits() throws Exception {
	AuctionCommon.priceBitsLen = priceBitsLen + 20;
	
	AuctionAgent auctionserver = new AuctionAgent(ePkSum, nPerson);
	auctionserver.run();
	}
	
	public static void NotifyBuyers() throws Exception {
	for(int i = 0; i < nPerson; i++) {
		cClient = new CipherClient();
		cClient.create_socket_and_connect(buyIps[i].toString().replaceAll("/", ""), 23459);
		if(i != AuctionAgent.firstMaxIndex.intValue()) {
		cClient.SendValue("Sorry, you fail.");
		}
		else {
		cClient.SendValue("恭喜你竞拍成功，成交价为："+AuctionAgent.secondMaxPrice.toString());
		}
		
		cClient.cleanup();
	}
	
	}
	
	AgentWindow server;
	
	private void init() {
		
		// TODO Auto-generated method stub
		 server = new AgentWindow();
		server.setVisible(true);
		    content = server.getContent();		
		
			    new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
				 	
					try {
						generatePaillier();
			    	    initBroadCastServer();
						sendPublicKey();
						receiveEProduct();
			    	    DecryptEProduct();
						GarbleCircuits();
						NotifyBuyers();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
	
					}
				}).start();
	}

	public static void main(String[] args) throws Exception {
	new TestAuctionAgent();
	}
	
}