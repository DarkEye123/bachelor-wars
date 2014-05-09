// Internal action code for project bachelor_wars

package jason;

import ui.GameMap;
import mapping.GameSettings;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class getKnowledgeInReach extends jason.getNearestFreeKnowledge {
	private static final long serialVersionUID = 1287083926924478575L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		canRemoveWithIntention = false;
		ignoreFriendlySeized = true;
		calledByChildClass = true;
		int unitId = (int)((NumberTerm) terms[0]).solve();
    	unit = GameMap.searchUnit(unitId);
		if (unit.base.getType() != GameSettings.SIMPLE_AI)
			ignoreFriendlySeized = true;
		if (!(boolean)super.execute(ts, un, terms)) //empty
			return false;

//		System.out.println(closest.path.size() + " " + unit.getMov());
		
		if (closest.path.size() > unit.getMov()) //not in reach
			return false;
		else  {
			un.unifies(terms[1], ListTermImpl.parseList(closest.to.toString()));
			return true;
		}
    }
}
