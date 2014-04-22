package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class addIntention extends DefaultInternalAction{
	private static final long serialVersionUID = -3447388669731589527L;
	
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		
		return true;
	}

}
