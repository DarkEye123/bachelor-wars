package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class FirstYear extends Unit {
	public static final int TYPE = 64;
	
	private static final int COST = 20;
    public static final int ATK = 5;
    public static final int HP = 40;
    public static final int MOV = 4;
    private static final String NAME = "First Year Student";
//    private static final File IMAGE_PATH = new File("pics/first_class128.png");
    private static final FirstYear prototype = new FirstYear();

	public FirstYear() {
		init();
	}
    
    public FirstYear(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, TYPE);
		init();
		name = name + " " + id;
	}
	
	private void init() {
		image = Toolkit.getDefaultToolkit().getImage("pics/FirstYearStudent.gif");
		cost = COST;
		atk = ATK;
		hp = HP;
		mov = MOV;
		name = NAME;
		type = TYPE;
	}
	
	public static FirstYear getPrototype() {
		return prototype;
	}
}
