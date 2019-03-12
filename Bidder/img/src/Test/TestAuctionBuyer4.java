package Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import Paillier.*;

public class TestAuctionBuyer4 {
	
	static BroadCast bClient;
	static CipherClient cClient;
	static CipherServer cServer;
	
	static BigInteger price, ePrice; 
	static int priceBitsLen;
	
	static Paillier paillier;
	static BigInteger g, n; //Paillier公钥
	
	static SecureRandom rnd = new SecureRandom();
	
	private static void generatePrice() throws Exception {
	priceBitsLen = 50;
	price = new BigInteger(priceBitsLen, rnd);
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
		
 	cClient.create_socket_and_connect("localhost", 23457);
 	cClient.SendCiphertxt(ePrice);
 	cClient.SendBuyerIp();
 	
 	cClient.cleanup();
	}
	
	private static void GetResult() throws Exception {
	String res = null;
		
	cServer = new CipherServer();
		
	cServer.createSocket(23459);
    res = cServer.ReceiveResult();
	System.out.println("res = "+res);
	}
	
	public static void main(String[] args) throws Exception {
	generatePrice();
	initBroadCastClient();
	ReceivePublicKey();
	encryptPrice();
	SendEncryptPrice();
	GetResult();
	}
	
}