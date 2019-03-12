// Copyright (C) 2017 by LiTeam
package Program;

import java.math.*;

import Utils.*;
import YaoGC.*;

public class AuctionCommon extends ProgCommon {
	static int priceVecLen;
	public static int priceBitsLen;
	
	static int bitLength(int x) {
    	return BigInteger.valueOf(x).bitLength();
    }
	
	 protected static void initCircuits() {
	 ccs = new Circuit[1];
	 ccs[0] = new Auction_2LNplusDN_Lplus1plusD(priceVecLen, priceBitsLen, bitLength(priceVecLen));
	 }
	 
	public static State execCircuit(BigInteger[] splbs, BigInteger[] cplbs) throws Exception {
	BigInteger[] lbs = new BigInteger[2*priceVecLen*priceBitsLen + priceVecLen*bitLength(priceVecLen)];
	
	System.arraycopy(splbs, 0, lbs, 0, priceVecLen*priceBitsLen);
	System.arraycopy(cplbs, 0, lbs, priceVecLen*priceBitsLen, priceVecLen*priceBitsLen);
	System.arraycopy(splbs, priceVecLen*priceBitsLen, lbs, 2*priceVecLen*priceBitsLen, priceVecLen*bitLength(priceVecLen));
		
	State in = State.fromLabels(lbs);
	State out = ccs[0].startExecuting(in);
		
	StopWatch.taskTimeStamp("circuit garbling");
	
	return out;
	}
}
