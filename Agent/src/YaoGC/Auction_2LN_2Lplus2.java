// Copyright (C) 2017 by LiTeam
package YaoGC;

public class Auction_2LN_2Lplus2 extends CompositeCircuit {
	
	private final int N;
	private final int L;
	
	public Auction_2LN_2Lplus2(int n, int l){
	super(2*l*n, 2*(l+1), n+1, "Auction_"+(2*l*n)+"_"+(2*l+2));
		
	N = n;
	L = l;
	}
	
	protected void createSubCircuits() throws Exception {
	for(int i = 0; i < N; i++)
		subCircuits[i] = new ADD_2L_Lplus1(L);
		
	subCircuits[N] = new FilterMaxs_LN_2L(N, L+1);
	super.createSubCircuits();
	}
	
	protected void connectWires() throws Exception {
	for(int i = 0; i < subCircuits.length - 1; i++){
		((ADD_2L_Lplus1) subCircuits[i]).connectWiresToXY(inputWires, i*L, inputWires, (i*L+L*N));
		
	for(int j = 0; j < L+1; j++)
		subCircuits[i].outputWires[j].connectTo(subCircuits[N].inputWires, j+i*(L+1));
	}
	}
	
	protected void defineOutputWires(){
	System.arraycopy(subCircuits[N].outputWires, 0, outputWires, 0, 2*L+2);
	}
}
