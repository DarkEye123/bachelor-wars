package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

public class ThirdYear extends Unit {
	public static final int TYPE = 256;
	
	private static final int COST = 80;
    public static final int ATK = 20;
    public static final int HP = 80;
    public static final int MOV = 5;
    private static final String NAME = "Third Year Student";
    private static final ThirdYear prototype = new ThirdYear();

	public ThirdYear() {
		init();
	}
    
    public ThirdYear(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, TYPE);
		init();
		name = name + " " + id;
	}
	
	private void init() {
		image = Toolkit.getDefaultToolkit().getImage("pics/ThirdYearStudent.gif");
		cost = COST;
		atk = ATK;
		hp = HP;
		mov = MOV;
		name = NAME;
		type = TYPE;
	}
	
	public static ThirdYear getPrototype() {
		return prototype;
	}
}
