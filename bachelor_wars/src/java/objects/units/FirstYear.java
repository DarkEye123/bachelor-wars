package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FirstYear extends Unit {
	public static final int TYPE = 64;
	
	private static final int COST = 20;
    public static final int ATK = 5;
    public static final int HP = 40;
    public static final int SP = 0;
    public static final int MOV = 4;
    private static final String NAME = "First Year Student";
    private static final File IMAGE_PATH = new File("pics/first_class128.png");
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
		try {
			image = ImageIO.read(IMAGE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cost = COST;
		atk = ATK;
		hp = HP;
		sp = SP;
		mov = MOV;
		name = NAME;
		type = TYPE;
		uClass = Unit.SUPPORT;
	}
	
	public static FirstYear getPrototype() {
		return prototype;
	}
}
