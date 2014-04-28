package mapping;

import jason.NoValueException;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;

import objects.Base;
import objects.GameObject;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;

public class Node {
	
	private static Node[][] nodeGrid;
	private static int rows, columns;
	
	LinkedList<Node> neighbours = new LinkedList<>();
	private Node predecessor = null;
	int gScore;
	int fScore;
	private int x;
	private int y;
	private Location location;
	List<GameObject> gameObjects = new LinkedList<GameObject>(); //list of objects that are in actual node
	
	public boolean equals(Node node) {
		if (getX() == node.getX() && getY() == node.getY())
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
//			System.out.println(((Unit)object).base);
			getKnowledge().STATE = GameMap.ROUND;
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
				nodeGrid[y][x].neighbours.add(getNode(x, y - 1));
				nodeGrid[y][x].neighbours.add(getNode(x, y + 1));
				nodeGrid[y][x].neighbours.add(getNode(x - 1, y));
				nodeGrid[y][x].neighbours.add(getNode(x + 1, y));
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
		this.setX(x);
		this.setY(y);
		this.location = new Location(x, y);
	}
	
	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	public void setGameObjects(List<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	/** calculates the Manhattan distance between two nodes */
	public int distance(Node n) {
        return Math.abs(getX() - n.getX()) + Math.abs(getY() - n.getY());
    }
	
	public static void removePredecessors() {
		for (int y = 0; y < rows; ++y) {
			for (int x = 0; x < columns; ++x) {
				nodeGrid[y][x].setPredecessor(null);
				nodeGrid[y][x].fScore = 0;
				nodeGrid[y][x].gScore = 0;
			}
		}
	}
	
	private static Node findLowestF(LinkedList<Node> list) {
		int minF = Integer.MAX_VALUE;
		Node ret = null;
		for (Node node:list) {
			if (node.fScore < minF) {
				minF = node.fScore;
				ret = node;
			}
		}
		return ret;
	}
	
	public static LinkedList<Node> searchPath(Node from, Node to, boolean ignoreUnits) {
		Node.removePredecessors();
		LinkedList<Node> ret = new LinkedList<Node>();
		LinkedList<Node> closedSet = new LinkedList<Node>();
		LinkedList<Node> openSet = new LinkedList<Node>();
		from.gScore = 0; // Cost from start along best known path
		from.fScore = from.gScore + from.distance(to);
		openSet.add(from);
		
		boolean dontCloseLastNode = false;
		
		if (from.getUnit() != null && to.getUnit() != null) {
			dontCloseLastNode = true;
//			System.out.println("setting DONT_CLOSE: " + dontCloseLastNode);
		}
		
//		if (ignoreUnits)
//			System.out.println("IGNORIIIIIIIIIIIIIIING for: " + from.getUnit().getId());
		
		while (!openSet.isEmpty()) {
			Node current = findLowestF(openSet);
			if (current.equals(to))
				break;
			openSet.remove(current);
			if (!closedSet.contains(current))
				closedSet.add(current);
			
			for (Node neighbour:current.neighbours) {
				if (neighbour != null && !closedSet.contains(neighbour)) {
					if ( (!neighbour.containUnit() || (neighbour.equals(to) && dontCloseLastNode)) || ignoreUnits) {
						int tentativeGScore = current.gScore + current.distance(to);
						
						if (!openSet.contains(neighbour) || tentativeGScore < neighbour.gScore) {
							neighbour.setPredecessor(current);
							neighbour.gScore = tentativeGScore;
							neighbour.fScore = neighbour.gScore + neighbour.distance(to);
							if (!openSet.contains(neighbour))
								openSet.add(neighbour);
						}
					} else {
						closedSet.add(neighbour);
					}
				}
			}
		}
		Node act = to;
		while (act != null) {
			if (!ignoreUnits) {
				if (act.getPredecessor() == null && !act.equals(from)) //if last node of path != node from where we are trying to go, there is no path
					return ret;
			}
			ret.addFirst(act);
			act = act.getPredecessor();
		}
		if (ignoreUnits) {
			LinkedList<Node> temp = new LinkedList<Node>();
			for (Node node:ret) {
				if (!node.containUnit() || node.equals(from)) {
//					System.out.println("adding node: " + node);
					temp.add(node);
				} else {
					return temp;
				}
			}
		}
		if (ret.getLast().containUnit()) {
			ret.removeLast();
		}
		return ret;
	}
	

	@Override
	public String toString() {
		return "[" + getX() + ", " + getY() + "]";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Node getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}
	
	public static Node getNode(Term[] terms) throws NoValueException {
		int x = 0,y = 0, placeId = -1;
		
		if (terms.length == 2) {
			if (!terms[1].isList()) {
				placeId = (int)((NumberTerm) terms[1]).solve();
				LinkedList<GameObject> objects = (LinkedList<GameObject>) GameMap.getUnitList().clone();
				objects.addAll(GameMap.getKnowledgeList());
				for (GameObject o:objects) {
					if (o.getId() == placeId) {
						x = o.getX();
						y = o.getY();
						break;
					}
				}
			} else {
				List<Term> l = ((ListTermImpl) terms[1]).getAsList();
				x = (int)((NumberTerm)l.get(0)).solve();
				y = (int)((NumberTerm)l.get(1)).solve();
//				System.out.println(x + "  " + y);
			}
		}
		else {
			x = (int)((NumberTerm)(terms[1])).solve();
			y = (int)((NumberTerm)(terms[2])).solve();
		}
		return Node.getNode(x, y);
	}
	
	public Location getLocation() {
		return location;
	}

	//For testing purpose only
	public static void main(String[] args) {
		Node.generateGrid(5, 5);
		Node node = Node.getNode(1, 1);
		
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
}
