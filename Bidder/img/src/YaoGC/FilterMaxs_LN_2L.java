// Copyright (C) 2017 by LiTeam

package YaoGC;

public class FilterMaxs_LN_2L extends CompositeCircuit {
	
    private final int L;
    private final int N;
    
    private final static int MAX = 0;
    private final static int MIN = 1;
    
    public FilterMaxs_LN_2L(int n, int l) {
	super(l*n, 2*l, n, "FilterMaxs_" + (n*l) + "_" + (2*l));
	L = l;
	N = n;
    }

    protected void createSubCircuits() throws Exception {
    subCircuits[MAX]=new MAX_2L_L(L);
    subCircuits[MIN]=new MIN_2L_L(L);
    
	for (int i = 2; i < N; i++)
	    subCircuits[i]  = new UPDATE_3L_2L(L);
	super.createSubCircuits();
    }

    protected void connectWires() throws Exception {
    for (int i = 0; i < L; i++) {
		inputWires[i].connectTo(subCircuits[MAX].inputWires, i);
		inputWires[i+L].connectTo(subCircuits[MAX].inputWires, i+L);
		inputWires[i].connectTo(subCircuits[MIN].inputWires, i);
		inputWires[i+L].connectTo(subCircuits[MIN].inputWires, i+L);
		subCircuits[MAX].outputWires[i].connectTo(subCircuits[2].inputWires, i);
		subCircuits[MIN].outputWires[i].connectTo(subCircuits[2].inputWires, i+L);
		inputWires[i+2*L].connectTo(subCircuits[2].inputWires, i+2*L);
    }
    
    for (int j = 1; j < N-2; j++) {
    	for (int i = 0; i < L; i++) {
		subCircuits[j+1].outputWires[i].connectTo(subCircuits[j+2].inputWires, i);
		subCircuits[j+1].outputWires[i+L].connectTo(subCircuits[j+2].inputWires, i+L);
		inputWires[i+(j+2)*L].connectTo(subCircuits[j+2].inputWires, i+2*L);
    	}
    }
    }

    protected void defineOutputWires() {
	System.arraycopy(subCircuits[N-1].outputWires, 0, outputWires, 0, 2*L);
    }
}