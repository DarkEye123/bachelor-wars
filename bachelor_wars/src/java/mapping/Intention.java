package mapping;


public class Intention {
	
	private static int UNUSED = -1;
	
	public enum Type {
		TEMPORARY, //conversion to 0 in jason
		PERSISTENT //conversion to 1 in jason
	}
	
	public Integer intention;
	public Type type;
	public Integer powerIndex = UNUSED; //index of used power from powerlist of given unit
	
	public Intention(Integer intention, Type type) {
		this.intention = intention;
		this.type = type;
	}
	
	public Intention(Integer intention, Type type, Integer powerIndex) {
		this(intention, type);
		this.powerIndex = powerIndex;
	}

	@Override
	public String toString() {
		if (type == Type.TEMPORARY)
			return "[" + intention + ", temporary," + powerIndex + "]";
		else
			return "[" + intention + ", persistent," + powerIndex + "]";
	}
	
}
