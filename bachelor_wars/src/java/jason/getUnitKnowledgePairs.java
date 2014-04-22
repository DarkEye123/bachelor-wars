// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import mapping.Node;
import mapping.Wrapper;
import objects.Base;
import objects.GameObject;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;

public class getUnitKnowledgePairs extends DefaultInternalAction {
	private static final long serialVersionUID = 4836533164553341855L;
	protected static final int PENALTY = 100;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int agentID = (int)((NumberTerm) terms[0]).solve();
		Base base = GameMap.searchBase(agentID);
		LinkedList<Wrapper> finalList = getShortestPaths(GameMap.getKnowledgeList(), base.getKnowledgeList(), base);
		Wrapper.sort(finalList); //get sorted paths to all knowledge resources (unit:knowledge .. etc) so this are best pairs for given round
		
		LinkedList<LinkedList<Wrapper>> sizes = new LinkedList<>(); //List of lists of objects, where one list contains all objects with same length of path
		int lastSize = -1;
		int actSize;
		for (Wrapper o:finalList) { //goes from the shortest paths to the longest
			actSize = o.path.size();
			if (lastSize != actSize) {
				lastSize = actSize;
				sizes.add(new LinkedList<Wrapper>());
			}
			sizes.getLast().add(o);
		}
		
		
		un.unifies(terms[1], ListTermImpl.parseList(objects.toString()));
		
		if (objects.isEmpty())
			return false;
		else 
			return true;
    }
	
	/**
	 * Makes shallow copy of list that extends GameObject class
	 * @param input - list that we want to copy (we copy only pointers)
	 * @return
	 */
	private LinkedList<GameObject> makeShallowCopy(LinkedList<? extends GameObject> input) {
		LinkedList<GameObject> list = new LinkedList<>();
		for (GameObject o:input) {
			list.add(o);
		}
		return list;
	}
	
	/**
	 * Finds pairs of GameObjects - like Unit-Knowledge for example - with the shortest paths between them
	 * @param input - list of GameObjects which are targets for units
	 * @param remove - basically it's list of objects that base already own (like friendly units, knowledge sources etc)
	 * @param base - agents base
	 * @return - list that contains pairs of gameObjects and paths between them
	 */
	private LinkedList<Wrapper> getShortestPaths(LinkedList<? extends GameObject> input, LinkedList<? extends GameObject> remove, Base base) {
		LinkedList<Wrapper> objects = new LinkedList<>();
		LinkedList<Wrapper> toRemove = new LinkedList<>();
		LinkedList<Wrapper> finalList = new LinkedList<>();
		LinkedList<GameObject> listOfInterest = makeShallowCopy(input); //get all knowledge
		listOfInterest.removeAll(remove); //remove that sources, that are already own
		for (Unit unit:base.getUnitList()) { //for every unit agent poses
			for (GameObject object:listOfInterest) { //for every target on the map
				LinkedList<Node> path = Node.searchPath(unit.getNode(), object.getNode(), false); //search path between them
				if (path.isEmpty()) //if no path exists(it's blocked temporary)
					path = Node.searchPath(unit.getNode(), object.getNode(), true); //find closest path to the object in that direction (ignore units)
				objects.add(new Wrapper(unit, object, path));
			}
			for (Wrapper w:objects) { //remove empty paths (there is no path at all, unit is totally blocked) TODO find out if it is real deal here
				if (w.path.isEmpty()) {
					toRemove.add(w);
				}
			}
			objects.removeAll(toRemove);
			Wrapper.sort(objects); //get sorted paths to all knowledge resources for given unit (so it finds shortest paths for actual unit to every knowledge source)
			Wrapper first = objects.getFirst(); //get the shortest path
			//iterate till find all paths with are equal with the shorthest (like there are two nodes that cost to move there is same)
			//so there couldn't be "the shortest" path
			for (Wrapper o:objects) { 
				if (o.path.size() == first.path.size())
					finalList.add(o);
				else
					break;
			}
			objects.clear();//erase others
			toRemove.clear();
		}
		return finalList;
	}
	
	/**
	 * Recursively makes lists of objects. Basically it's combining every possible move with every unit to maximize moving potential.
	 * @param act - actual position in list "L" of lists of "O" objects with the same path length
	 * @param max - it's the length of "L"
	 * @param actObj - actual object chosen from "O" to make new branch to explore
	 * @param input - list "L" of lists of "O" objects
	 * @param actualPath - actual branch of objects
	 * @param variants - list of all branches
	 */
	private void doStep(int act, int max, Wrapper actObj, LinkedList<LinkedList<Wrapper>> input, LinkedList<Wrapper> actualPath, LinkedList<LinkedList<Wrapper>> variants) {
		if (act < max) {
			if (actObj != null)
				actualPath.add(actObj);
			for (Wrapper w: input.get(act)) {
				if (canProceed(actualPath, w))
					doStep(act + 1, max, w, input, (LinkedList<Wrapper>) actualPath.clone(), variants);
			}
		} 
		else { //there is still last Wrapper object
			if (actObj != null)
				actualPath.add(actObj);
			variants.add(actualPath);
		}
	}
	
	/**
	 * Says if "obj" GameObject can be used for next evaluation
	 * @param actualPath - actual branch
	 * @param obj - object to test
	 * @return true if can be used for next evaluation
	 */
	private boolean canProceed(LinkedList<Wrapper> actualPath, Wrapper obj) {
		for (Wrapper actObj:actualPath) {
			if (actObj.to.equals(obj.to) || actObj.from.equals(obj.from)) //skip those variants, that we logically can't use (same target, or source)
				return false; 
		}
		return true;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	private LinkedList<Wrapper> evaluateBestOption(LinkedList<LinkedList<Wrapper>> input) {
		LinkedList<LinkedList<Wrapper>> variants = new LinkedList<>();
		int min = Integer.MAX_VALUE;
		int act = 0;
		int fullPotential = getFullPotential(input);
		LinkedList<Wrapper> bestComposition = null;
		
		doStep(0, input.size(), null, input, new LinkedList<Wrapper>(), variants);
		
		for (LinkedList<Wrapper> variant:variants) {
			act = 0;
			for (Wrapper object:variant) {
				act += object.path.size();
				act += evaluatePenalty(fullPotential, variant);
			}
			if (min > act) {
				min = act;
				bestComposition = variant;
			}
		}
		return bestComposition;
	}
	
	private int getFullPotential(LinkedList<LinkedList<Wrapper>> input) {
		int ret = 0;
		for (LinkedList<Wrapper> variant:input) {
			ret += getNumUniqueObjects(variant);
		}
		return ret;
	}

	private int evaluatePenalty(int fullPotential,
			LinkedList<Wrapper> variant) {
		return (fullPotential - getNumUniqueObjects(variant)) * PENALTY;
	}
	
	private int getNumUniqueObjects(LinkedList<Wrapper> input) {
		HashSet<GameObject> set = new HashSet<>();
		for (Wrapper o:input) {
			set.add(o.to);
		}
		return set.size();
	}
	
	public static void main(String[] args) {
		
	}
}
