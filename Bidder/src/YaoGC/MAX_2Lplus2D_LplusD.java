// Copyright (C) 2017 by LiTeam

package YaoGC;

public class MAX_2Lplus2D_LplusD extends CompositeCircuit{

    private final int L;
    private final int D;

    public final static int  GT = 0;
    public final static int MUXA = 1;
    public final static int MUXB = 2;

    public MAX_2Lplus2D_LplusD(int l, int d) {
	super(2*l+2*d, l+d, 3, "MAX_" + (2*l+2*d)+ "_" + (l+d));
	L = l;
	D = d;
    }

    protected void createSubCircuits() throws Exception {
	subCircuits[GT]  = new GT_2L_1(L);
	subCircuits[MUXA] = new MUX_2Lplus1_L(L);
	subCircuits[MUXB] = new MUX_2Lplus1_L(D);
	
	super.createSubCircuits();
    }

    protected void connectWires() throws Exception {
	for (int i = 0; i < L; i++) {
	    inputWires[i+L].connectTo(subCircuits[GT].inputWires, GT_2L_1.X(i));
	    inputWires[i  ].connectTo(subCircuits[GT].inputWires, GT_2L_1.Y(i));

	    inputWires[i  ].connectTo(subCircuits[MUXA].inputWires, MUX_2Lplus1_L.X(i));
	    inputWires[i+L].connectTo(subCircuits[MUXA].inputWires, MUX_2Lplus1_L.Y(i));
	}
	
	for(int i = 2*L; i < 2*L+D; i++) {
		inputWires[i].connectTo(subCircuits[MUXB].inputWires, MUX_2Lplus1_L.X(i-2*L));
		inputWires[i+D].connectTo(subCircuits[MUXB].inputWires, MUX_2Lplus1_L.Y(i-2*L));
	}

	subCircuits[GT].outputWires[0].connectTo(subCircuits[MUXA].inputWires, 2*L);
	subCircuits[GT].outputWires[0].connectTo(subCircuits[MUXB].inputWires, 2*D);
    }

    protected void defineOutputWires() {
	for (int i = 0; i < L; i++)
	    outputWires[i] = subCircuits[MUXA].outputWires[i];
	
	for(int i = L; i < L+D; i++){
		outputWires[i] = subCircuits[MUXB].outputWires[i -L];
	}
    }
    
}
