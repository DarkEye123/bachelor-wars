package mapping;

import java.util.LinkedList;

import objects.GameObject;
import objects.units.Unit;

public class Wrapper {
	public Unit from;
	public GameObject to;
	public LinkedList<Node> path;
	
	public Wrapper(Unit from, GameObject to, LinkedList<Node> path) {
		this.from = from;
		this.to = to;
		this.path = path;
	}
	
	@Override
	public String toString() {
		String nodes = "";
		for (int x = 0; x < path.size(); ++x) {
			if (x + 1 == path.size()) //the last node
				nodes = nodes + ", "+ path.get(x) + "]";
			else if (x ==0) 
				nodes = "[" + path.get(x);
			else
				nodes =  nodes + ", " + path.get(x);
		}
//		return "[" + from.getId() + ", " + to.getId() + ", " + nodes + "]";
		return "[" + from.getId() + ", " + to.getId() + ", " + path.size() + "]";
	}

	public static void sort(LinkedList<Wrapper> objects) {
		quickSort(objects, 0, objects.size() - 1);
	}
	
	private static int getNumRounds(int pathLenght, int movementRange) {
		float rounds = pathLenght / movementRange;
		if (rounds - (int) rounds > 0)
			return (int)rounds+1;
		else
			return (int)rounds;
	}
	
	private static void exchange(LinkedList<Wrapper> objects, int i, int j) {
		Wrapper temp = objects.get(i);
		objects.set(i, objects.get(j));
		objects.set(j, temp);
	}
	
	private static void quickSort(LinkedList<Wrapper> objects, int low, int high) {
		int i = low, j = high;
		int movementRange = objects.get(low + (high-low)/2).from.getMov();
		int pivot = getNumRounds(objects.get(low + (high-low)/2).path.size(), movementRange); //pivot is number of rounds that takes a unit to move to the location.
		
		while (i <= j) {
			while (getNumRounds(objects.get(i).path.size(), objects.get(i).from.getMov()) < pivot) {
				++i;
			}
			while (getNumRounds(objects.get(j).path.size(), objects.get(j).from.getMov()) > pivot) {
				--j;
			}
			if (i <= j) {
				exchange(objects, i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			quickSort(objects, low, j);
		if (i < high)
			quickSort(objects, i, high);
	}
}
