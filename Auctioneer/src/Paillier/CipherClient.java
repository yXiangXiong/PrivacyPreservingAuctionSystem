// Copyright (C) 2017 by LiTeam
package Paillier;

import java.io.*;
import java.math.*;
import java.net.*;

import Utils.Utils;

public class CipherClient {
	
 	private Socket       sock         = null;                    // Socket object for communicating
 	
 	private ObjectOutputStream oos        = null;              // socket output stream
    private ObjectInputStream  ois        = null;              // socket input stream
    
 	public CipherClient () {} 
 	
 	public void create_socket_and_connect(String serveripname, int port) throws Exception {
 	sock = new java.net.Socket(serveripname, port);  // create socket and connect
 	oos  = new java.io.ObjectOutputStream(sock.getOutputStream());  
 	ois  = new java.io.ObjectInputStream(sock.getInputStream());
 	}
 	
 	public void SendCiphertxt(BigInteger m) throws Exception {
 	Utils.writeBigInteger(m, oos);
 	oos.flush();
 	}
 	
 	public void SendBuyerIp() throws Exception {
 	oos.writeObject(sock.getLocalAddress());
 	}
 	
 	public void SendProduct(BigInteger[] pro, InetAddress[] ips) throws Exception {
 	for(int i = 0; i < pro.length; i++) {
 		Utils.writeBigInteger(pro[i], oos);
 	}
 	
 	oos.flush();
 	
 	for(int i = 0; i < ips.length; i++) {
 		oos.writeObject(ips[i]);
 	}
 	}
 	
 	public void SendValue(String info) throws Exception {
 	oos.writeObject(info);
 	oos.flush();
 	}
 	
 	public void cleanup() throws Exception {
	oos.close();
	ois.close();
	sock.close();
 	}

}