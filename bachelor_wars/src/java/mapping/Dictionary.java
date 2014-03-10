package mapping;

public class Dictionary<K, M> {
	K index;
	M value;
	
	public Dictionary(K index, M value) {
		this.index = index;
		this.value = value;
	}
	
	public K getIndex() {
		return index;
	}
	public M getValue() {
		return value;
	}
}
