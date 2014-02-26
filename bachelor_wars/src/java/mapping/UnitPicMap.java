package mapping;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UnitPicMap {

	int type;
	BufferedImage picture;
	public UnitPicMap(int unit, File file) {
		this.type = unit;
		setPic(file);
	}
	public void setPic(File file) {
		try {
			picture = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int getType() {
		return type;
	}
	public void setType(int unit) {
		this.type = unit;
	}
	public BufferedImage getPicture() {
		return picture;
	}
	public void setPicture(BufferedImage picture) {
		this.picture = picture;
	}
	
}
