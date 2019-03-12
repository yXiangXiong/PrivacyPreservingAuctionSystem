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
		socket  = new MulticastSocket(BROADCAST_PORT); //����Ĭ�ϵ�ַ��ָ���˿�
		
		broadcastAddress = InetAddress.getByName(BROADCAST_IP);
		socket.setTimeToLive(1);
		socket.joinGroup(broadcastAddress); //����ָ���Ķ��㲥��ַ
		
		outPacket = new DatagramPacket(new byte[0], 0, broadcastAddress, BROADCAST_PORT); //��ʼ��Ϊ����Ϊ0���ֽ�����
	}

	public void SendBigInteger(BigInteger number) throws IOException {
			String n = String.valueOf(number); //����ת��Ϊ�ַ���
	        byte[] buff =n.getBytes(); //�ַ���ת��Ϊ�ֽ�����
	        outPacket.setData(buff);
	        
	        socket.send(outPacket); //�������ݱ�
	        
	}
	
	public BigInteger ReceiveBigInteger() throws IOException {
		byte[] inBuff = new byte[1024]; //�������ݱ��Ĵ�С���Ϊ1KB
		inPacket = new DatagramPacket(inBuff, inBuff.length);
		
		socket.receive(inPacket); //��ȡ���ݷ���inPacket����װ���ֽ�������
        String n = new String(inBuff, 0, inPacket.getLength());
        BigInteger number = new BigInteger(n);
        
		return number;
	}
	
}