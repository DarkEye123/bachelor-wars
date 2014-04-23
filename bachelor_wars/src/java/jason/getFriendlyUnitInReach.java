// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

public class getFriendlyUnitInReach extends jason.getNearestFriendlyUnit {
	private static final long serialVersionUID = 5216835098741564049L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		if (!(boolean)super.execute(ts, un, terms)) //empty
			return false;
		
		if (closest.path.size() > unit.getMov()) //not in reach
			return false;
		else  {
			un.unifies(terms[1], ListTermImpl.parseList(closest.to.toString()));
			return true;
		}
    }
}
