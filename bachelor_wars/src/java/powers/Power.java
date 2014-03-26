package powers;
import ui.ImagePanel;


/**
 * This abstract class represent basic power - from which other types of powers can inherit
 *@author Matej Leško <xlesko04@stud.fit.vutbr.cz>
 *
 */
public abstract class Power {
	public enum PowerType { //type of power
		HEALING, DAMAGE, DEFENSE
	}

	int cost; //how much SP points
	String info; //text information about given power
	ImagePanel image; //panel with picture of this skill
	PowerType type;
	
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public ImagePanel getImage() {
		return image;
	}
	public void setImage(ImagePanel image) {
		this.image = image;
	}
	public PowerType getType() {
		return type;
	}
	public void setType(PowerType type) {
		this.type = type;
	}
	
}
