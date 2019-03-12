// Copyright (C) 2017 by LiTeam

package YaoGC;

public class MIN_2Lplus2D_LplusD extends CompositeCircuit{

    private final int L;
    private final int D;

    public final static int  GT = 0;
    public final static int MUXA = 1;
    public final static int MUXB = 2;

    public MIN_2Lplus2D_LplusD(int l, int d) {
	super(2*l+2*d, l+d, 3, "MIN_" + (2*l+2*d)+ "_" + (l+d));
	L = l;
	D = d;
    }

    /*
     * Connect xWires[xStartPos...xStartPos+L] to the wires representing bits of X;
     * yWires[yStartPos...yStartPos+L] to the wires representing bits of Y;
     */
    public void connectWiresToXY(Wire[] xWires, int xStartPos, Wire[] yWires, int yStartPos) throws Exception {
	if (xStartPos + L > xWires.length || yStartPos + L > yWires.length)
	    throw new Exception("Unmatched number of wires.");
	
	for (int i = 0; i < L; i++) {
	    xWires[xStartPos+i].connectTo(inputWires, X(i));
	    yWires[yStartPos+i].connectTo(inputWires, Y(i));
	}
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

	    inputWires[i+L].connectTo(subCircuits[MUXA].inputWires, MUX_2Lplus1_L.X(i));
	    inputWires[i  ].connectTo(subCircuits[MUXA].inputWires, MUX_2Lplus1_L.Y(i));
	}

	for(int i = 2*L; i < 2*L+D; i++){
		inputWires[i+D].connectTo(subCircuits[MUXB].inputWires, MUX_2Lplus1_L.X(i-2*L));
		inputWires[i].connectTo(subCircuits[MUXB].inputWires, MUX_2Lplus1_L.Y(i-2*L));
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

    private int X(int i) {
	return i+L;
    }

    private int Y(int i) {
	return i;
    }

}
