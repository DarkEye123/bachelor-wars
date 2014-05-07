package mapping;


public class Intention {
	
	private static int UNUSED = -1;
	
	public enum Type {
		TEMPORARY, //conversion to 0 in jason
		PERSISTENT //conversion to 1 in jason
	}
	
	public Integer intention;
	public Type type;
	
	public Intention(Integer intention, Type type) {
		this.intention = intention;
		this.type = type;
	}
	
	@Override
	public String toString() {
		if (type == Type.TEMPORARY)
			return "[" + intention + ", temporary ]";
		else
			return "[" + intention + ", persistent ]";
	}
	
}
