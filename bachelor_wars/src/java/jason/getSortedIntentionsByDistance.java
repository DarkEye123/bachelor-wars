// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;


public class getSortedIntentionsByDistance extends getSortedIntentions {
	private static final long serialVersionUID = 8589375725026739855L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		by = DISTANCE;
		return super.execute(ts, un, terms);
	}
}
