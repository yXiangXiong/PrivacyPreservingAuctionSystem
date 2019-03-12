// Copyright (C) 2017 by LiTeam
package Program;

import java.math.*;
import java.security.SecureRandom;

import YaoGC.*;
import Utils.*;

public class Auctioneer extends ProgClient {
	private BigInteger[] cPrices;
	private BigInteger[] sPriceslbs, cPriceslbs;
	
	private State outputState;
	
	static int bitLength(int x) {
    	return BigInteger.valueOf(x).bitLength();
    }
	
	public Auctioneer(BigInteger[] pv, int number,String IP) {
		cPrices = pv;
		AuctionCommon.priceVecLen = number;
		this.serverIPname = IP;
	}
	
	protected void init() throws Exception {
	AuctionCommon.priceBitsLen = AuctionCommon.ois.readInt();
	AuctionCommon.oos.flush();
		
	AuctionCommon.initCircuits();
		
	otNumOfPairs = AuctionCommon.priceBitsLen;
	
	super.init();
	}
	
	protected void execTransfer() throws Exception {
	sPriceslbs = new BigInteger[AuctionCommon.priceVecLen*(AuctionCommon.priceBitsLen+bitLength(AuctionCommon.priceVecLen))];
	BigInteger[] tempPriceslbs = new BigInteger[AuctionCommon.priceBitsLen];
		
	for(int i = 0;  i < AuctionCommon.priceVecLen*AuctionCommon.priceBitsLen;  i++) {
		int bytelength = (Wire.labelBitLength - 1)/8 + 1;
		sPriceslbs[i] = Utils.readBigInteger(bytelength, AuctionCommon.ois);
	}

	for(int i = AuctionCommon.priceVecLen * AuctionCommon.priceBitsLen; i < AuctionCommon.priceVecLen*(AuctionCommon.priceBitsLen+bitLength(AuctionCommon.priceVecLen)); i++){
		int bytelength = (Wire.labelBitLength - 1)/8 + 1;
		sPriceslbs[i] = Utils.readBigInteger(bytelength, AuctionCommon.ois);
	}
	
	StopWatch.taskTimeStamp("receiving labels for peer's inputs and index");
		
	cPriceslbs = new BigInteger[AuctionCommon.priceVecLen * AuctionCommon.priceBitsLen];
	for(int i = 0; i < AuctionCommon.priceVecLen; i++) {
		rcver.execProtocol(cPrices[i]);
		tempPriceslbs = rcver.getData();
		for(int j = 0; j < AuctionCommon.priceBitsLen; j++) {
			cPriceslbs[j + i*AuctionCommon.priceBitsLen] = tempPriceslbs[j];
		}
	}
	StopWatch.taskTimeStamp("receiving labels for self's inputs");
	}
	
	protected void execCircuit() throws Exception {
	outputState = AuctionCommon.execCircuit(sPriceslbs, cPriceslbs);
	}
	
	protected void interpretResult() throws Exception {
	AuctionCommon.oos.writeObject(outputState.toLabels());
	AuctionCommon.oos.flush();
	}
	/*This code is just for testing result.It's can not appear in real application*/
	/*
	protected void verify_result() throws Exception {
	for(int i = 0; i < AuctionCommon.priceVecLen; i++) {
		AuctionCommon.oos.writeObject(cPrices[i]);
	}
	AuctionCommon.oos.flush();
	}*/
}
