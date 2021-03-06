// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Wrapper;
import ui.GameMap;

public class getUnitKnowledgePairs extends getPairs {
	private static final long serialVersionUID = 4836533164553341855L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		super.execute(ts, un, terms);
		LinkedList<Wrapper> finalList = getShortestPaths(GameMap.getKnowledgeList(), base.getKnowledgeList(), base);
		Wrapper.sort(finalList); //get sorted paths to all knowledge resources (unit:knowledge .. etc) so this are best pairs for given round
		
		LinkedList<LinkedList<Wrapper>> groups = groupByPathLength(finalList); //group objects with same path lengths
		
		finalList = evaluateBestOption(groups); // find best movement variation for actual round for given targets
		
		un.unifies(terms[1], ListTermImpl.parseList(finalList.toString()));
		
		if (finalList.isEmpty())
			return false;
		else 
			return true;
    }
	
	public static void main(String[] args) {
		
	}
}
