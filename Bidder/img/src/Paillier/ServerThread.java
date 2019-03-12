// Copyright (C) 2017 by LiTeam
package Paillier;

import java.io.*;
import java.math.*;
import java.net.*;

import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.output.CountingOutputStream;

import Utils.Utils;

public class ServerThread extends Thread{
	Socket clientSocket = null; 
	
	CountingOutputStream cos = null;
	CountingInputStream  cis = null;
	
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	
	BigInteger ePrice;
	InetAddress ip;
	
	public ServerThread(Socket socket){
		this.clientSocket = socket;
	}
	
	public void run() {
		try {
			cos = new CountingOutputStream(clientSocket.getOutputStream());
			cis = new CountingInputStream(clientSocket.getInputStream());
			
			oos = new ObjectOutputStream(cos);
			ois = new ObjectInputStream(cis);
			
			ePrice = Utils.readBigInteger(ois);
			ip = (InetAddress)ois.readObject();
			
			oos.close();
			ois.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
