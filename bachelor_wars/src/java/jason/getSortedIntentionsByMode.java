package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class getSortedIntentionsByMode extends getSortedIntentions {
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		by = MODE;
		return super.execute(ts, un, terms);
	}
}
