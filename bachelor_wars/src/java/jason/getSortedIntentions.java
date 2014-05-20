// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.HashMap;
import java.util.LinkedList;

import mapping.GameSettings;
import mapping.Intention;
import mapping.Node;
import mapping.Wrapper;
import objects.Base;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;

public class getSortedIntentions extends DefaultInternalAction {
	private static final long serialVersionUID = -420405120028143584L;
	protected static final int DISTANCE = 1;
	protected static final int MODE = 2;

	class SemanticIntention {
		GameObject object;
		Intention intention;
		SemanticIntention(GameObject o, Intention i) {
			object = o;
			intention = i;
		}
		@Override
		public String toString() {
			return "[" + object + ", " + intention + "]";
		}
		
	}

	protected int by = -1;
	Unit unit;
	
	private boolean canAdd(Intention intention, boolean canRemoveTemporary, int pathLength) {
		if (canRemoveTemporary) {
			if (intention.type == Intention.Type.TEMPORARY) {
				if (pathLength > unit.getMov()) {
					return false;
				}
			}
		}
		return true;
	}
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitID = (int)((NumberTerm) terms[0]).solve();
    	int mode = (int)((NumberTerm) terms[1]).solve();
    	
    	unit = GameMap.searchUnit(unitID);
    	
    	HashMap<GameObject, Intention> intentions = (HashMap<GameObject, Intention>) (unit.getIntentions()).clone();
    	LinkedList<Wrapper> wrapper = new LinkedList<>();
    	
    	boolean allEmpty = true;
    	String role = unit.base.getRole();
    	boolean canRemoveTemporary = false;
    	boolean isSeizer = false;
    	if (role != null && role.equals("seizer")) {
    		canRemoveTemporary = true;
    		isSeizer = true;
		}
    	
    	for (GameObject obj:intentions.keySet()) { //iterate over all object in intention map
    		LinkedList<Node> path = Node.searchPath(unit.getNode(), obj.getNode(), false); //firt try find a possible ways to the object of given intention
    		if ( canAdd(intentions.get(obj), canRemoveTemporary, path.size()) ) {
    			wrapper.add(new Wrapper(unit, obj, path));
    		} else {
    			path.clear();
    		}
    		if ( allEmpty && !path.isEmpty()) //there is atleast one possible intention (path was found)
    			allEmpty = false;
    	}
    	if (allEmpty) { //no path was found, so we are probably trapped
    		wrapper.clear();
    		for (GameObject obj:intentions.keySet()) { //iterate over all object in intention map
    			LinkedList<Node> path = Node.searchPath(unit.getNode(), obj.getNode(), true); //finds way to intentions but ignore units (they can move next round)
    			if ( canAdd(intentions.get(obj), canRemoveTemporary, path.size()) ) {
        			wrapper.add(new Wrapper(unit, obj, path));
        		}
    		}
    	} else { //some possible paths were found
    		LinkedList<Wrapper> remove = new LinkedList<>(); //we need to remove all empty paths
    		for (Wrapper obj:wrapper) {
    			if (obj.path.isEmpty())
    				remove.add(obj);
    		}
    		wrapper.removeAll(remove);
    	}
    	
    	
    	Wrapper.sort(wrapper);
    	
    	if (getBy() == DISTANCE) {
    		LinkedList<SemanticIntention> ret = new LinkedList<>();
    		boolean wasAlreadyAdded = false;
    		for (Wrapper o:wrapper) {
    			ret.add(new SemanticIntention(o.to, intentions.get(o.to)));
    			
    			if (unit.base.getType() != GameSettings.SIMPLE_AI) {
	    			if (o.to.getClass().equals(Base.class) && o.path.size() <= unit.getMov()*2) {//base is near within 2 rounds
	    				ret.addFirst(ret.removeLast());
	    				wasAlreadyAdded = true;
	    			}
	    			
	    			if (isSeizer && !wasAlreadyAdded) {
	    				if (intentions.get(o.to).intention == Unit.SEIZE && o.path.size() <= unit.getMov()) {//for seizer role, seize intention in range has bigger priority
	    					ret.addFirst(ret.removeLast());
	    					wasAlreadyAdded = true;
	    				}
	    			}
    			}
    		}
    		un.unifies(terms[2], ListTermImpl.parseList(ret.toString()));
    	}
    	
    	if (getBy() == MODE) { //TODO this all need to be rewritten!!!
	    	if (mode == GameSettings.DOMINATION) {
	    		int [] pom = {Unit.SEIZE, Unit.KILL};
	    		un.unifies(terms[2], ListTermImpl.parseList(getSortedInt(unit, wrapper, intentions, pom).toString()));
	    	}
	    	if (mode == GameSettings.ANNIHLIATION) {
	    		int [] pom = {Unit.KILL, Unit.SEIZE};
	    		un.unifies(terms[2], ListTermImpl.parseList(getSortedInt(unit, wrapper, intentions, pom).toString()));
	    	}
	    	if (mode == GameSettings.MADNESS) {
	    		int [] pom = {Unit.KILL, Unit.SEIZE};
	    		un.unifies(terms[2], ListTermImpl.parseList(getSortedInt(unit, wrapper, intentions, pom).toString()));
	    	}
    	}
    	
        return true;
    }
    private int getNextIntention(LinkedList<Wrapper> sortedList, int position, HashMap<GameObject, Intention> intentions, int intention, LinkedList<SemanticIntention> sortedIntentions) {
    	for (int pos = position; pos < sortedList.size(); pos++) {
    		GameObject o = sortedList.get(pos).to;
    		if (intentions.get(o).intention == intention) { //TODO sorting by temporary and persistent intentions
    			sortedIntentions.add(new SemanticIntention(o, intentions.get(o)));
    			return pos + 1;
    		}
    	}
//    	System.out.println("max");
    	return Integer.MAX_VALUE;
    }
    
    private LinkedList<SemanticIntention> getSortedInt(Unit unit, LinkedList<Wrapper> wrapper, HashMap<GameObject, Intention> intentions, int[] list) {
    	int counterSeize = 0;
    	int counterKill = 0;
    	LinkedList<SemanticIntention> sortedIntentions = new LinkedList<>();
    	
    	while (counterSeize != Integer.MAX_VALUE || counterKill != Integer.MAX_VALUE) {
//    		System.out.println(counterSeize + " " + counterKill + " " + counterHeal + " " + counterSupport);
    		for (Integer intention: list) {
    			if (intention == Unit.SEIZE && counterSeize != Integer.MAX_VALUE) {
	    			counterSeize = getNextIntention(wrapper, counterSeize, intentions, intention, sortedIntentions);
	    		}
	    		if (intention == Unit.KILL && counterKill != Integer.MAX_VALUE) {
	    			counterKill = getNextIntention(wrapper, counterKill, intentions, intention, sortedIntentions);
	    		}
    		}
    	}
    	return sortedIntentions;
    }
    
    private int getBy() {
    	return by;
    }
}
