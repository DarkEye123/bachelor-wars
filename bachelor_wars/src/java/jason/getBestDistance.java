// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Node;
import objects.Base;
import objects.Knowledge;
import ui.GameMap;

public class getBestDistance extends DefaultInternalAction {
	private static final long serialVersionUID = -7527603402622537945L;
	
	Base base;
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentID = (int)((NumberTerm) terms[0]).solve();
    	base = GameMap.searchBase(agentID);
    	String source = ((StringTermImpl) terms[1]).getString();
    	String target = ((StringTermImpl) terms[2]).getString();
		
		if (source.equals("base")) {
			if (target.equals("knowledge")) {
				int ret = evaluateBaseKnowledgeDistance();
				un.unifies(terms[3], ASSyntax.createNumber(ret));
			}
		}
		
        return true;
    }
    
    private int evaluateBaseKnowledgeDistance() {
    	LinkedList<Knowledge> knowledge = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
//    	knowledge.removeAll(base.getKnowledgeList());
    	int distance = 0;
    	for (Knowledge k:knowledge) {
    		distance += base.getNode().distance(k.getNode()); //need to be celan path without
    	}
//    	System.out.println(distance + " " + base.getName());
    	return distance;
    }
}
