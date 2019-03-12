// Copyright (C) 2017 by LiTeam

package YaoGC;

public class UPDATE_3Lplus3D_2Lplus2D extends CompositeCircuit{

	private final int L;
	private final int D;
	
	private final static int MAXA = 0;
	private final static int MIN = 1;
	private final static int MAXB = 2;
	
	public UPDATE_3Lplus3D_2Lplus2D(int l, int d){
	super(3*l+3*d, 2*l+2*d, 3, "UPDATE_"+(3*l+3*d)+"_"+(2*l+2*d));
	L = l;
	D = d;
	}
	
	protected void createSubCircuits() throws Exception{
	subCircuits[MAXA]  = new MAX_2Lplus2D_LplusD(L, D);
	subCircuits[MIN]  = new MIN_2Lplus2D_LplusD(L, D);
	subCircuits[MAXB]  = new MAX_2Lplus2D_LplusD(L, D);
	
	super.createSubCircuits();
	}
	 
	protected void connectWires() throws Exception{
	for(int i = 0; i < L; i++){
		inputWires[i].connectTo(subCircuits[MAXA].inputWires, i);
		inputWires[i].connectTo(subCircuits[MIN].inputWires, i);
		inputWires[i + L].connectTo(subCircuits[MAXB].inputWires, i);
		inputWires[i + 2*L].connectTo(subCircuits[MAXA].inputWires, i+L);
		inputWires[i + 2*L].connectTo(subCircuits[MIN].inputWires, i+L);
	}
	
	for(int i = 3*L; i < 3*L+D; i++){
		inputWires[i].connectTo(subCircuits[MAXA].inputWires, i-L);
		inputWires[i].connectTo(subCircuits[MIN].inputWires, i-L);
		inputWires[i + D].connectTo(subCircuits[MAXB].inputWires, i-L);
		inputWires[i + 2*D].connectTo(subCircuits[MAXA].inputWires, i-L+D);
		inputWires[i + 2*D].connectTo(subCircuits[MIN].inputWires, i-L+D);
	}
		
	for(int i = 0; i < L; i++)
		subCircuits[MIN].outputWires[i].connectTo(subCircuits[MAXB].inputWires, i+L);
	
	for(int i = L; i < L+D; i++)
		subCircuits[MIN].outputWires[i].connectTo(subCircuits[MAXB].inputWires, i+L+D);
	}
	 
	protected void defineOutputWires(){
	System.arraycopy(subCircuits[MAXA].outputWires, 0, outputWires, 0, L);
	System.arraycopy(subCircuits[MAXB].outputWires, 0, outputWires, L, L);
	System.arraycopy(subCircuits[MAXA].outputWires, L, outputWires, 2*L, D);
	System.arraycopy(subCircuits[MAXB].outputWires, L, outputWires, 2*L+D, D);
	}

}
