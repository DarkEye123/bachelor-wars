package mapping;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import objects.Base;
import objects.GameObject;
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
	
	public boolean containUnit() {
		for (GameObject object:gameObjects) {
			if (Unit.class.isInstance(object))
				return true;
		}
		return false;
	}
	
	public boolean containBase() {
		for (GameObject object:gameObjects) {
			if (object.getClass().equals(Base.class))
				return true;
		}
		return false;
	}
	
	/**
	 * This checks if concrete object exists in list
	 * @param o - should be a class reference. Like Something.class
	 * @return true if given type of object is present
	 */
	public boolean containSpecificObect(Object o) {
		for (GameObject object:gameObjects) {
			if (object.getClass().equals(o))
				return true;
		}
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
				nodeGrid[y][x].down = getNode(y -1, x);
				nodeGrid[y][x].up = getNode(y +1, x);
				nodeGrid[y][x].left = getNode(y, x - 1);
				nodeGrid[y][x].right = getNode(y, x + 1);
			}
		}
	}
	
	public static Node getNode(int x, int y) {
		if (! isValidPosition(x, y))
			return null;
		else
			return nodeGrid[y][x];
	}
	
	private static boolean isValidPosition(int x, int y) {
		if (nodeGrid == null) {
			return false;
		}
		if (x < 0 || x >= rows || y < 0 || y >= columns) {
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
		Node node = Node.getNode(0, 0);
		node.gameObjects.add(new FirstYear());
		System.out.println(node.containBase());
		System.out.println(node.containUnit());
		System.out.println(node.containSpecificObect(Unit.class));
		System.out.println(node.containSpecificObect(FirstYear.class));
		
		System.out.println(Base.class.isInstance(node.gameObjects.get(0)));
		for (Object c:FirstYear.class.getClasses())
			System.out.println(c.toString());
	}
}
