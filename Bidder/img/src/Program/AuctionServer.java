// Copyright (C) 2017 by LiTeam
package Program;

import java.math.*;
import java.util.*;
import java.security.SecureRandom;

import YaoGC.*;
import Utils.*;

public class AuctionServer extends ProgServer {
	private BigInteger[] sPrices;
	
	private State outputState;
	
	private BigInteger[][] sPriceslps, cPriceslps;
	public static BigInteger secondMaxPrice = BigInteger.ZERO;
	public static BigInteger firstMaxIndex = BigInteger.ZERO;
	
	private static final SecureRandom rnd = new SecureRandom();
	
	static int bitLength(int x) {
    	return BigInteger.valueOf(x).bitLength();
    }
	
	public AuctionServer(BigInteger[] pv, int number) {
	sPrices = pv;
	AuctionCommon.priceVecLen = number;
	}
	
	protected void init() throws Exception {
	AuctionCommon.oos.writeInt(AuctionCommon.priceBitsLen);
	AuctionCommon.oos.flush();
		
	AuctionCommon.initCircuits();
		
	generateLabelPairs();
	super.init();
	}
	
	private void generateLabelPairs() {
	sPriceslps = new BigInteger[AuctionCommon.priceVecLen*(AuctionCommon.priceBitsLen+bitLength(AuctionCommon.priceVecLen))][2];
	cPriceslps = new BigInteger[AuctionCommon.priceVecLen*AuctionCommon.priceBitsLen][2];
		
	for(int i = 0; i < AuctionCommon.priceVecLen*(AuctionCommon.priceBitsLen+bitLength(AuctionCommon.priceVecLen)); i++){
		BigInteger glb0 = new BigInteger(Wire.labelBitLength, rnd);
		BigInteger glb1 = glb0.xor(Wire.R.shiftLeft(1).setBit(0));
		sPriceslps[i][0] = glb0;
		sPriceslps[i][1] = glb1;
	} 
	for(int i = 0; i < AuctionCommon.priceVecLen * AuctionCommon.priceBitsLen; i++){
		BigInteger glb0 = new BigInteger(Wire.labelBitLength, rnd);
		BigInteger glb1 = glb0.xor(Wire.R.shiftLeft(1).setBit(0));
		cPriceslps[i][0] = glb0;
		cPriceslps[i][1] = glb1;
	}
	}
	
	protected void execTransfer() throws Exception {
	BigInteger[][] tempPriceslps = new BigInteger[AuctionCommon.priceBitsLen][2];
	BigInteger[] sIndexs = new BigInteger[AuctionCommon.priceVecLen];
	
	for(int i = 0;  i < AuctionCommon.priceVecLen;  i++) { //send server price bit labels
		for(int j = 0; j < AuctionCommon.priceBitsLen; j++) {
			int idx = sPrices[i].testBit(j) ? 1 : 0;
				
			int bytelength = (Wire.labelBitLength - 1)/8 + 1;
			Utils.writeBigInteger(sPriceslps[j + i*AuctionCommon.priceBitsLen][idx], bytelength, AuctionCommon.oos);
		}
	}
	
	for(int i = 0;  i < AuctionCommon.priceVecLen;  i++) {
		sIndexs[i] = BigInteger.valueOf(i);
	}
	
	for(int i = 0;  i < AuctionCommon.priceVecLen;  i++) { //send index bit labels
		for(int j = 0; j < bitLength(AuctionCommon.priceVecLen); j++) {
			int idx = sIndexs[i].testBit(j) ? 1 : 0;
				
			int bytelength = (Wire.labelBitLength - 1)/8 + 1;
			Utils.writeBigInteger(sPriceslps[j+(AuctionCommon.priceVecLen*AuctionCommon.priceBitsLen)+ i*bitLength(AuctionCommon.priceVecLen)][idx], bytelength, AuctionCommon.oos);
		}
	}
	AuctionCommon.oos.flush();
	StopWatch.taskTimeStamp("sending labels for selfs inputs and index");
	
	for(int i = 0; i < AuctionCommon.priceVecLen; i++) { //send client price bit labels
		for(int j = 0; j < AuctionCommon.priceBitsLen; j++) {
			tempPriceslps[j][0] = cPriceslps[j + i*AuctionCommon.priceBitsLen][0];
			tempPriceslps[j][1] = cPriceslps[j + i*AuctionCommon.priceBitsLen][1];
		}
		snder.execProtocol(tempPriceslps);
	}
	StopWatch.taskTimeStamp("sending labels for peers inputs");
	}
	
	protected void execCircuit() throws Exception {
	BigInteger[] sPriceslbs =  new BigInteger[AuctionCommon.priceVecLen*(AuctionCommon.priceBitsLen+bitLength(AuctionCommon.priceVecLen))];
	BigInteger[] cPriceslbs =  new BigInteger[AuctionCommon.priceVecLen*AuctionCommon.priceBitsLen];
	
	for(int i = 0; i < sPriceslps.length; i++) {
		sPriceslbs[i] = sPriceslps[i][0];
	}
	
	for(int i = 0; i < cPriceslps.length; i++) {
		cPriceslbs[i] = cPriceslps[i][0];
	}
	
	outputState = AuctionCommon.execCircuit(sPriceslbs, cPriceslbs);
	}
	
	protected void interpretResult() throws Exception {
	BigInteger[] outLabels = (BigInteger[]) AuctionCommon.ois.readObject();

	BigInteger output = BigInteger.ZERO;
	BigInteger m = BigInteger.ONE;
	m = m.shiftLeft(AuctionCommon.priceBitsLen - 20);
	System.out.println("m = "+m);
	
	for (int i = 0; i < outLabels.length; i++) {
		if (outputState.wires[i].value != Wire.UNKNOWN_SIG) {
		if (outputState.wires[i].value == 1)
			output = output.setBit(i);
		continue;
		}
		else if (outLabels[i].equals(outputState.wires[i].invd ? 
			outputState.wires[i].lbl :
			outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)))) {
			output = output.setBit(i);
		}
		else if (!outLabels[i].equals(outputState.wires[i].invd ? 
			outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)) :
			outputState.wires[i].lbl)) 
			throw new Exception("Bad label encountered: i = " + i + "\t" +
					outLabels[i] + " != (" + 
					outputState.wires[i].lbl + ", " +
					outputState.wires[i].lbl.xor(Wire.R.shiftLeft(1).setBit(0)) + ")");
		}
		
		StopWatch.taskTimeStamp("output labels received and interpreted");
		System.out.println("output="+output);

		for (int i = 0; i < AuctionCommon.priceBitsLen+1; i++) {
			if(output.testBit(i) == true)
				secondMaxPrice = secondMaxPrice.setBit(i);
		}
		secondMaxPrice = secondMaxPrice.mod(m);
		
		for(int i = AuctionCommon.priceBitsLen+1; i < AuctionCommon.priceBitsLen+1+bitLength(AuctionCommon.priceVecLen); i++){
			if(output.testBit(i)== true)
				firstMaxIndex = firstMaxIndex.setBit(i-(AuctionCommon.priceBitsLen+1));
		}
		System.out.println("firstMaxIndex = " + firstMaxIndex);
		System.out.println("secondMaxPrice = " + secondMaxPrice);
	}
	
	protected void verify_result() throws Exception{
		BigInteger[] sumPrices = new BigInteger[AuctionCommon.priceVecLen];
		int maxPriceIndex;
		BigInteger firstMPrice, secondMPrice;
		
		for(int i = 0; i < sumPrices.length; i++){
			sumPrices[i] = (BigInteger)AuctionCommon.ois.readObject();
			sumPrices[i] = sumPrices[i].add(sPrices[i]);
		}
		
		if(sumPrices[0].compareTo(sumPrices[1]) > 0){
			firstMPrice = sumPrices[0];
			maxPriceIndex = 0;
			secondMPrice = sumPrices[1];
		}
		else{
			firstMPrice = sumPrices[1];
			maxPriceIndex = 1;
			secondMPrice = sumPrices[0];
		}
		
		for(int i = 2; i < sumPrices.length; i++){
			if(sumPrices[i].compareTo(firstMPrice) > 0){
				if(firstMPrice.compareTo(secondMPrice) > 0){
					secondMPrice = firstMPrice;
				}
				firstMPrice = sumPrices[i];
				maxPriceIndex = i; 
			}
			else if(sumPrices[i].compareTo(secondMPrice) > 0){
				secondMPrice = sumPrices[i];
			}
		}
		System.out.println("买家报价中的最高价的下标为"+maxPriceIndex);
		System.out.println("买家报价中的次最高价为:" + secondMPrice);
	}
}
