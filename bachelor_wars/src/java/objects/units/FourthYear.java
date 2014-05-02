package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Toolkit;

public class FourthYear extends Unit {
	public static final int TYPE = 512;
	private static final int COST = 100;
    public static final int ATK = 25;
    public static final int HP = 90;
    public static final int MOV = 5;
    private static final String NAME = "Fourth Year Student";
    private static final FourthYear prototype = new FourthYear();

	public FourthYear() {
		init();
	}
    
    public FourthYear(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, TYPE);
		init();
		name = name + " " + id;
	}
	
	private void init() {
		image = Toolkit.getDefaultToolkit().getImage("pics/FourthYearStudent.gif");
		cost = COST;
		atk = ATK;
		hp = HP;
		mov = MOV;
		name = NAME;
		type = TYPE;
	}
	
	public static FourthYear getPrototype() {
		return prototype;
	}
}
