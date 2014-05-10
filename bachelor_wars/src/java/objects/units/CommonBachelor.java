package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Toolkit;

public class CommonBachelor extends Unit {
	public static final int TYPE = 1024;
	private static final int COST = 80;
    public static final int ATK = 15;
    public static final int HP = 50;
    public static final int MOV = 7;
    private static final String NAME = "Common Bachelor";
    private static final CommonBachelor prototype = new CommonBachelor();

	public CommonBachelor() {
		init();
	}
    
    public CommonBachelor(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, TYPE);
		init();
		name = name + " " + id;
	}
	
	private void init() {
		image = Toolkit.getDefaultToolkit().getImage("pics/CommonBachelor.gif");
		cost = COST;
		atk = ATK;
		hp = HP;
		mov = MOV;
		name = NAME;
		type = TYPE;
	}
	
	public static CommonBachelor getPrototype() {
		return prototype;
	}
}
