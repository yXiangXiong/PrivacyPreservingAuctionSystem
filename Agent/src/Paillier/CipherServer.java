// Copyright (C) 2017 by LiTeam
package Paillier;

import java.net.*;
import java.io.*;
import java.math.*;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.io.input.CountingInputStream;

import Utils.*;

public class CipherServer {
	
    private ServerSocket       sock         = null;              // original server socket
    private Socket             clientSocket = null;              // socket created by accept
    
    public CipherServer() {
    	
    }
    
    public void createSocket(int port) throws Exception {
    sock = new ServerSocket(port); // create socket and bind to port
    }
    
	public void receiveCiphertxt(BigInteger[] eprices, InetAddress[] buyips) throws Exception {
	int count = 0;
	
	while(true) {
		clientSocket = sock.accept();                   // wait for client to connect
		System.out.println(clientSocket.getPort());
		ServerThread serverthread = new ServerThread(clientSocket);
		serverthread.start();
		Thread.sleep(100); //sleep 100 ms
		
		eprices[count] = serverthread.ePrice;
		buyips[count] = serverthread.ip;
		
		count++;
		System.out.println("第"+count+"台客户端连接");
		if(count == eprices.length) {
			sock.close();
			break;
		}
	}
	}
	
	public void receiveProduct(BigInteger[] pro, InetAddress[] ips) throws Exception {
	clientSocket = sock.accept();
	
	CountingOutputStream cos = new CountingOutputStream(clientSocket.getOutputStream()); 
	CountingInputStream  cis = new CountingInputStream(clientSocket.getInputStream()); 
	
    ObjectOutputStream oos = new ObjectOutputStream(cos);
	ObjectInputStream ois = new ObjectInputStream(cis);
	
	for(int i = 0; i < pro.length; i++) {
		pro[i] = Utils.readBigInteger(ois);
	}
	
	for(int i = 0; i < ips.length; i++) {
		ips[i] = (InetAddress)ois.readObject();
	}
	
	oos.close();
	ois.close();
	sock.close();
	clientSocket.close();
	}
	
	public  String ReceiveResult() throws Exception {
	String info;
	
	clientSocket = sock.accept();
			
	CountingOutputStream cos = new CountingOutputStream(clientSocket.getOutputStream()); 
	CountingInputStream  cis = new CountingInputStream(clientSocket.getInputStream()); 
			
	ObjectOutputStream oos = new ObjectOutputStream(cos);
	ObjectInputStream ois = new ObjectInputStream(cis);
		
	info  = (String)ois.readObject();
		
	oos.close();
	ois.close();
	sock.close();
	clientSocket.close();
	
	return info;
	}
}
