// Copyright (C) 2017 by LiTeam
package YaoGC;

public class Auction_2LNplusDN_Lplus1plusD extends CompositeCircuit {

	private final int N;
	private final int L; 
	private final int D;
	
	public Auction_2LNplusDN_Lplus1plusD(int n, int l, int d) {
	super(2*l*n+d*n, l+1+d, n+1, "Auction_"+(2*l*n+d*n)+"_"+(l+1+d));
	
	N = n;
	L = l;
	D = d;
	}
	
	protected void createSubCircuits() throws Exception {
	for(int i = 0; i < N; i++)
		subCircuits[i] = new ADD_2L_Lplus1(L);
		
	subCircuits[N] = new FilterMaxs_LNplusDN_LplusD(N, L+1, D);
	super.createSubCircuits();
	}
	
	protected void connectWires() throws Exception {
	for(int i = 0; i < subCircuits.length - 1; i++) {
		((ADD_2L_Lplus1) subCircuits[i]).connectWiresToXY(inputWires, i*L, inputWires, (i*L+L*N));
		
		for(int j = 0; j < L+1; j++)
			subCircuits[i].outputWires[j].connectTo(subCircuits[N].inputWires, j+i*(L+1));
	}
	
	for(int i = 2*L*N; i < (2*L*N+D*N); i++){
		inputWires[i].connectTo(subCircuits[N].inputWires, i-(L-1)*N);
	}
	}
	
	protected void defineOutputWires() {
	System.arraycopy(subCircuits[N].outputWires, 0, outputWires, 0, L+1+D);
	}

}
