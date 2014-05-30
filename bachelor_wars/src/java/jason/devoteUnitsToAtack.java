// Internal action code for project bachelor_wars

package jason;

import java.util.Collections;
import java.util.LinkedList;

import mapping.Node;
import objects.Base;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class devoteUnitsToAtack extends DefaultInternalAction {
	
	class Distance implements Comparable<Distance> {
		
		Unit unit;
		int distance;
		
		Distance(Unit u, int d) {
			unit = u;
			distance = d;
		}
		
		@Override
		public int compareTo(Distance o) {
			return distance - o.distance;
		}
		
	}
	
	private void deleteBaseIntentions(Unit u, Base enemy) {
		LinkedList<GameObject> toRemove = new LinkedList<>();
		for (GameObject obj : u.getIntentions().keySet()) {
			if (obj.getClass().equals(Base.class) && !obj.equals(enemy)) { //find bases except the chosen one
				toRemove.add(obj);
			}
		}
		for (GameObject o : toRemove) { //end delete intentions for them
			u.getIntentions().remove(o);
		}
	}

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int baseID = (int)((NumberTerm) terms[0]).solve();
    	int limit = (int)((NumberTerm) terms[1]).solve();
    	int enemyID = (int)((NumberTerm) terms[2]).solve();
    	Base base = GameMap.searchBase(baseID);
    	Base enemy = GameMap.searchBase(enemyID);
    	
    	Node enemyNode = enemy.getNode();
    	
    	LinkedList<Distance> sorted = new LinkedList<>();
    	for (Unit u:base.getUnitList()) {
    		sorted.add(new Distance(u, u.getNode().distance(enemyNode)));
    	}
    	
    	Collections.sort(sorted);
    	
    	for (int x = 0; x < limit; ++x) {
    		deleteBaseIntentions(sorted.get(x).unit, enemy);
    	}
    	
        return true;
    }
}
