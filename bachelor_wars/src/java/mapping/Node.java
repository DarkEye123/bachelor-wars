package mapping;

import java.util.LinkedList;
import java.util.List;

import objects.Base;
import objects.GameObject;
import objects.Knowledge;
import objects.units.FirstYear;
import objects.units.Unit;

public class Node {
	
//	class EmptyGrid extends Exception {
//		private static final long serialVersionUID = 2941646389703077018L;
//		private static final String message = "Grid is not created, it can't be filled with nodes then.";
//		public EmptyGrid() {
//			super(message);
//		}
//	}
//	
//	class IllegalPosition extends Exception {
//		private static final long serialVersionUID = 2941646389703077019L;
//		private static final String message = "You are trying initialize node";
//		public IllegalPosition() {
//			super(message);
//		}
//	}
	
	private static Node[][] nodeGrid;
	private static int rows, columns;
	
	Node up;
	Node down;
	Node left;
	Node right;
	int x,y;
	List<GameObject> gameObjects = new LinkedList<GameObject>(); //list of objects that are in actual node
	
	public boolean equals(Node node) {
		if (x == node.x && y == node.y)
			return true;
		else
			return false;
	}
	
	public void remove(GameObject o) {
		gameObjects.remove(o);
	}
	
	/**
	 * Adds GameObject into the node. There can be more objects in one node, but there are exceptions too - like in the same node can't coexists two units at same time.
	 * @param object - game object to add to the node
	 * @return - true if object was successfully added or false if action failed - like as it is redundant object like another unit on the same field etc.
	 */
	public boolean add(GameObject object) { 
		if (Unit.class.isInstance(object) && containUnit()) 
			return false; //can't add two units into same node
		
		//TODO if it is base - it seizes more nodes, it should be added to all of them
		if (Base.class.isInstance(object) && containBase()) 
			return false; //can't add two bases into same node
		
		
		if (Unit.class.isInstance(object) && containKnowledge()) {
			System.out.println(((Unit)object).base);
			getKnowledge().setBase(((Unit)object).base);
		}
		
		Node.getNode(object.getX(), object.getY()).remove(object); //remove from previous node
		gameObjects.add(object);
		return true;
	}
	
	public Unit getUnit() {
		for (GameObject object:gameObjects) {
			if (Unit.class.isInstance(object))
				return (Unit) object;
		}
		return null;
	}
	
	public Base getBase() {
		return (Base) getSpecificObject(Base.class);
	}
	
	public Knowledge getKnowledge() {
		return (Knowledge) getSpecificObject(Knowledge.class);
	}
	
	public boolean containUnit() {
		if (getUnit() != null)
			return true;
		else
			return false;
	}
	
	public boolean containBase() {
		if (getSpecificObject(Base.class) != null)
			return true;
		else
			return false;
	}
	
	public boolean containKnowledge() {
		if (getSpecificObject(Knowledge.class) != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Finds specified object of class "o".
	 * @param o - should be a class reference. Like Something.class
	 * @return - instance of given class if present, null otherwise
	 */
	public GameObject getSpecificObject(Object o) {
		for (GameObject object:gameObjects) {
			if (object.getClass().equals(o))
				return (GameObject) object;
		}
		return null;
	}
	
	/**
	 * This checks if concrete object exists in list
	 * @param o - should be a class reference. Like Something.class
	 * @return true if given type of object is present
	 */
	public boolean containSpecificObect(Object o) {
		if (getSpecificObject(o) != null) 
			return true;
		else
			return false;
	}
	
	/**
	 * Generates a node representation of game map (grid). Every node is connected to each other in X,Y axis
	 * @param columns - number of columns 
	 * @param rows - number of rows
	 */
	public static void generateGrid(int columns, int rows) {
		Node.rows = rows;
		Node.columns = columns;
		nodeGrid = new Node[rows][columns];
		for (int y = 0; y < rows; ++y) {
			for (int x = 0; x < columns; ++x) {
				nodeGrid[y][x] = new Node(x, y);
			}
		}
		for (int y = 0; y < rows; ++y) {
			for (int x = 0; x < columns; ++x) {
				nodeGrid[y][x].down = getNode(x, y - 1);
				nodeGrid[y][x].up = getNode(x, y + 1);
				nodeGrid[y][x].left = getNode(x - 1, y);
				nodeGrid[y][x].right = getNode(x + 1, y);
			}
		}
	}
	
	public static Node getNode(int x, int y) {
		if (! isValidPosition(x, y))
			return null;
		else {
			return nodeGrid[y][x];
		}
	}
	
	private static boolean isValidPosition(int x, int y) {
		if (nodeGrid == null) {
			return false;
		}
		if (y < 0 || y >= rows || x < 0 || x >= columns) {
			return false;
		}
		return true;
	}
	
	private Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	public void setGameObjects(List<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	//For testing purpose only
	public static void main(String[] args) {
		Node.generateGrid(5, 5);
		Node node = Node.getNode(1, 1);
		
		System.out.println(node.left);
		System.out.println(node.right);
		System.out.println(node.up);
		System.out.println(node.down);
		
//		node.gameObjects.add(new FirstYear());
//		System.out.println(node.containBase());
//		System.out.println(node.containUnit());
//		System.out.println(node.containSpecificObect(Unit.class));
//		System.out.println(node.containSpecificObect(FirstYear.class));
//		
//		System.out.println(Base.class.isInstance(node.gameObjects.get(0)));
//		for (Object c:FirstYear.class.getClasses())
//			System.out.println(c.toString());
	}

	@Override
	public String toString() {
		return "x: " + x + " y " + y;
	}
}
