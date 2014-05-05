// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.HashSet;
import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.Base;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;

public abstract class getPairs extends DefaultInternalAction {
	private static final long serialVersionUID = 8589098864103700057L;
	protected static final int PENALTY = 100;
	
	Base base = null;
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentID = (int)((NumberTerm) terms[0]).solve();
		base = GameMap.searchBase(agentID);
		return true;
    }
    
    /**
	 * Groups Wrapper objects with the same path length into lists
	 * @param input - list of Wrapper objects
	 * @return List of lists of Wrapper objects
	 */
	public LinkedList<LinkedList<Wrapper>> groupByPathLength(LinkedList<Wrapper> input) {
		LinkedList<LinkedList<Wrapper>> ret = new LinkedList<>(); //List of lists of objects, where one list contains all objects with same length of path
		int lastSize = -1;
		int actSize;
		for (Wrapper o:input) { //goes from the shortest paths to the longest
			actSize = o.path.size();
			if (lastSize != actSize) {
				lastSize = actSize;
				ret.add(new LinkedList<Wrapper>());
			}
			ret.getLast().add(o);
		}
		return ret;
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
	public LinkedList<Wrapper> getShortestPaths(LinkedList<? extends GameObject> input, LinkedList<? extends GameObject> remove, Base base) {
		LinkedList<Wrapper> finalList = new LinkedList<>();
		LinkedList<Wrapper> toRemove = new LinkedList<>();
		LinkedList<GameObject> listOfInterest = makeShallowCopy(input); //get all knowledge
		listOfInterest.removeAll(remove); //remove that sources, that are already own
		for (Unit unit:base.getUnitList()) { //for every unit agent poses
			for (GameObject object:listOfInterest) { //for every target on the map
				LinkedList<Node> path = Node.searchPath(unit.getNode(), object.getNode(), false); //search path between them
				if (path.isEmpty()) //if no path exists(it's blocked temporary)
					path = Node.searchPath(unit.getNode(), object.getNode(), true); //find closest path to the object in that direction (ignore units)
				finalList.add(new Wrapper(unit, object, path));
			}
			Wrapper.sort(finalList); //get sorted paths to all knowledge resources for given unit (so it finds shortest paths for actual unit to every knowledge source)
		}
		for (Wrapper w:finalList) { //remove empty paths (there is no path at all, unit is totally blocked) TODO find out if it is real deal here
			if (w.path.isEmpty()) {
				toRemove.add(w);
			}
		}
		finalList.removeAll(toRemove);
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
			for (Wrapper w: input.get(act)) { //for every Wrapped object from actual group
				if (canProceed(actualPath, w)) //if is possible to proceed with next Wrapped object, do it
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
	 * From all possible compositions this finds the best possible target assignment for given round
	 * @param input List of Lists of Wrapper objects grouped by path length
	 * @return best possible variation
	 */
	public LinkedList<Wrapper> evaluateBestOption(LinkedList<LinkedList<Wrapper>> input) {
		LinkedList<LinkedList<Wrapper>> variants = new LinkedList<>();
		int min = Integer.MAX_VALUE;
		int act = 0;
		int fullPotential = base.getUnitList().size();
		LinkedList<Wrapper> bestComposition = null;
		
		doStep(0, input.size(), null, input, new LinkedList<Wrapper>(), variants);
		
		for (LinkedList<Wrapper> variant:variants) {
			act = 0;
			for (Wrapper object:variant) {
				act += object.path.size(); //evaluate a cost of this path
			}
			act += evaluatePenalty(fullPotential, variant); //for every unused unit there is penalty
			if (min > act) {
				min = act;
				bestComposition = variant;
			}
		}
		return bestComposition;
	}
	
	private int evaluatePenalty(int fullPotential,
			LinkedList<Wrapper> variant) {
		return (fullPotential - getNumUniqueObjects(variant)) * PENALTY;
	}
	
	/**
	 * Evaluate how many unique units is present in list
	 * @param input
	 * @return
	 */
	private int getNumUniqueObjects(LinkedList<Wrapper> input) {
		HashSet<GameObject> set = new HashSet<>();
		for (Wrapper o:input) {
			set.add(o.from);
		}
		return set.size();
	}
}
