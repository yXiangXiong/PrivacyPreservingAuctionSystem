// Copyright (C) 2017 by LiTeam
package Paillier;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;

public class BroadCast {
	
	private static final String BROADCAST_IP = "230.0.0.1"; 
	public static final int BROADCAST_PORT = 30000;
	
	private MulticastSocket socket = null; 
	private InetAddress broadcastAddress = null;
	
	private DatagramPacket inPacket = null;
	private DatagramPacket outPacket = null;

	public BroadCast(){ }
	
	public void Init() throws IOException{
		socket  = new MulticastSocket(BROADCAST_PORT); //本机默认地址，指定端口
		
		broadcastAddress = InetAddress.getByName(BROADCAST_IP);
		socket.setTimeToLive(1);
		socket.joinGroup(broadcastAddress); //加入指定的多点广播地址
		
		outPacket = new DatagramPacket(new byte[0], 0, broadcastAddress, BROADCAST_PORT); //初始化为长度为0的字节数组
	}

	public void SendBigInteger(BigInteger number) throws IOException {
			String n = String.valueOf(number); //大数转换为字符串
	        byte[] buff =n.getBytes(); //字符串转换为字节数组
	        outPacket.setData(buff);
	        
	        socket.send(outPacket); //发送数据报
	        
	}
	
	public BigInteger ReceiveBigInteger() throws IOException {
		byte[] inBuff = new byte[1024]; //接收数据报的大小最大为1KB
		inPacket = new DatagramPacket(inBuff, inBuff.length);
		
		socket.receive(inPacket); //读取数据放在inPacket所封装的字节数组里
        String n = new String(inBuff, 0, inPacket.getLength());
        BigInteger number = new BigInteger(n);
        
		return number;
	}
	
}