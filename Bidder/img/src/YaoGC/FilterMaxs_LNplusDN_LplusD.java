// Copyright (C) 2017 by LiTeam
package YaoGC;

public class FilterMaxs_LNplusDN_LplusD extends CompositeCircuit {
	
    private final int L;
    private final int N;
    private final int D;
    
    private final static int MAX = 0;
    private final static int MIN = 1;
    
    public FilterMaxs_LNplusDN_LplusD(int n, int l, int d) {
	super(l*n+d*n, l+d, n, "FilterMaxs_" + (l*n+d*n) + "_" + (l+d));
	L = l;
	N = n;
	D = d;
    }

    protected void createSubCircuits() throws Exception {
    subCircuits[MAX]=new MAX_2Lplus2D_LplusD(L, D);
    subCircuits[MIN]=new MIN_2Lplus2D_LplusD(L, D);
    
	for (int i = 2; i < N; i++)
	    subCircuits[i]  = new UPDATE_3Lplus3D_2Lplus2D(L, D);
	super.createSubCircuits();
    }

    protected void connectWires() throws Exception {
    for (int i = 0; i < L; i++) { //connect value wires
		inputWires[i].connectTo(subCircuits[MAX].inputWires, i);
		inputWires[i+L].connectTo(subCircuits[MAX].inputWires, i+L);
		inputWires[i].connectTo(subCircuits[MIN].inputWires, i);
		inputWires[i+L].connectTo(subCircuits[MIN].inputWires, i+L);
		subCircuits[MAX].outputWires[i].connectTo(subCircuits[2].inputWires, i);
		subCircuits[MIN].outputWires[i].connectTo(subCircuits[2].inputWires, i+L);
		inputWires[i+2*L].connectTo(subCircuits[2].inputWires, i+2*L);
    }
    
    for(int i = N*L; i < N*L+D; i++) { //connect index wires
    	inputWires[i].connectTo(subCircuits[MAX].inputWires, i -(N-2)*L);
    	inputWires[i+D].connectTo(subCircuits[MAX].inputWires, i+(D-(N-2)*L));
    	inputWires[i].connectTo(subCircuits[MIN].inputWires, i -(N-2)*L);
    	inputWires[i+D].connectTo(subCircuits[MIN].inputWires, i+(D-(N-2)*L));

    	subCircuits[MAX].outputWires[i-(N-1)*L].connectTo(subCircuits[2].inputWires, i-(N-3)*L);
    	subCircuits[MIN].outputWires[i-(N-1)*L].connectTo(subCircuits[2].inputWires, i+(D-(N-3)*L));
    	inputWires[i+2*D].connectTo(subCircuits[2].inputWires, i+(2*D-(N-3)*L));
    }
    
    for (int j = 1; j < N-2; j++) { //connect value wires
    	for (int i = 0; i < L; i++) {
		subCircuits[j+1].outputWires[i].connectTo(subCircuits[j+2].inputWires, i);
		subCircuits[j+1].outputWires[i+L].connectTo(subCircuits[j+2].inputWires, i+L);
		inputWires[i+(j+2)*L].connectTo(subCircuits[j+2].inputWires, i+2*L);
    	}
    }
    
    for (int j = 1; j < N-2; j++) { //connect index wires 
    	for (int i = 2*L; i < 2*L+D; i++) {
		subCircuits[j+1].outputWires[i].connectTo(subCircuits[j+2].inputWires, i+L);
		subCircuits[j+1].outputWires[i+D].connectTo(subCircuits[j+2].inputWires, i+L+D);
		inputWires[i+(N-2)*L+(j+2)*D].connectTo(subCircuits[j+2].inputWires, i+L+2*D);
    	}
    }
    }

    protected void defineOutputWires() {
	System.arraycopy(subCircuits[N-1].outputWires, L, outputWires, 0, L);
	System.arraycopy(subCircuits[N-1].outputWires, 2*L, outputWires, L, D);
    }

}
