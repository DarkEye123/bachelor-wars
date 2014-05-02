package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

public class SecondYear extends Unit {
	public static final int TYPE = 128;
	
	private static final int COST = 40;
    public static final int ATK = 10;
    public static final int HP = 60;
    public static final int MOV = 4;
    private static final String NAME = "Second Year Student";
    private static final SecondYear prototype = new SecondYear();

	public SecondYear() {
		init();
	}
    
    public SecondYear(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, TYPE);
		init();
		name = name + " " + id;
	}
	
	private void init() {
		image = Toolkit.getDefaultToolkit().getImage("pics/SecondYearStudent.gif");
		cost = COST;
		atk = ATK;
		hp = HP;
		mov = MOV;
		name = NAME;
		type = TYPE;
	}
	
	public static SecondYear getPrototype() {
		return prototype;
	}
}
