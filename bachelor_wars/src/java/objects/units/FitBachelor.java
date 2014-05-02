package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Toolkit;

public class FitBachelor extends Unit {
	public static final int TYPE = 2048;
	private static final int COST = 140;
    public static final int ATK = 40;
    public static final int HP = 120;
    public static final int MOV = 6;
    private static final String NAME = "Bachelor from FIT";
    private static final FitBachelor prototype = new FitBachelor();

	public FitBachelor() {
		init();
	}
    
    public FitBachelor(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, TYPE);
		init();
		name = name + " " + id;
	}
	
	private void init() {
		image = Toolkit.getDefaultToolkit().getImage("pics/FitBachelor.gif");
		cost = COST;
		atk = ATK;
		hp = HP;
		mov = MOV;
		name = NAME;
		type = TYPE;
	}
	
	public static FitBachelor getPrototype() {
		return prototype;
	}
}
