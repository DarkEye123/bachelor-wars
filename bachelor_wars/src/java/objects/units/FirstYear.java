package objects.units;

import jason.environment.grid.Location;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapping.GameSettings;

public class FirstYear extends Unit {
	
    private static final int COST = 20;
    private static final int ATK = 5;
    private static final int HP = 40;
    private static final int MOV = 4;
    private static final String NAME = "First Year Student";
    private static final File IMAGE_PATH = new File("pics/first_class128.png");
    private static final FirstYear prototype = new FirstYear();

	public FirstYear() {
		init();
	}
    
    public FirstYear(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize, GameSettings.FIRST_YEAR_STUDENT);
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
		mov = MOV;
		name = NAME;
	}
	
	public static FirstYear getPrototype() {
		return prototype;
	}
}
