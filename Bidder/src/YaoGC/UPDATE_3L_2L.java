// Copyright (C) 2017 by LiTeam
package YaoGC;

public class UPDATE_3L_2L extends CompositeCircuit{
	
	private final int L;
	
	private final static int MAXA = 0;
	private final static int MIN = 1;
	private final static int MAXB = 2;
	
	public UPDATE_3L_2L(int l){
	super(3*l, 2*l, 3, "UPDATE_"+3*l+"_"+2*l);
	L = l;
	}
	
	protected void createSubCircuits() throws Exception{
	subCircuits[MAXA]  = new MAX_2L_L(L);
	subCircuits[MIN]  = new MIN_2L_L(L);
	subCircuits[MAXB]  = new MAX_2L_L(L);
		 
	super.createSubCircuits();
	}
	 
	protected void connectWires() throws Exception{
	for(int i = 0; i < L; i++){
		inputWires[i].connectTo(subCircuits[MAXA].inputWires, i);
		inputWires[i].connectTo(subCircuits[MIN].inputWires, i);
		inputWires[i + L].connectTo(subCircuits[MAXB].inputWires, i);
		inputWires[i + 2*L].connectTo(subCircuits[MAXA].inputWires, i + L);
		inputWires[i + 2*L].connectTo(subCircuits[MIN].inputWires, i + L);
	}
		 
	for(int i = 0; i < L; i++){
		subCircuits[MIN].outputWires[i].connectTo(subCircuits[MAXB].inputWires, i + L);
	}
	}
	 
	protected void defineOutputWires(){
	System.arraycopy(subCircuits[MAXA].outputWires, 0, outputWires, 0, L);
	System.arraycopy(subCircuits[MAXB].outputWires, 0, outputWires, L, L);
	}
}
