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

	int cost; //how much SP points (from this is damage evaluated)
	int multiplier; //multiplier for damage/heal/buff (for exaple multiplier is x2 so damage for given value of 3 SP = 6 damage
	int maxSP; //maximum cost for power
	int minSP; //minimum cost for power
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
