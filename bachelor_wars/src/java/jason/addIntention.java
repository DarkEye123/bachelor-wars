package jason;

import mapping.Intention;
import mapping.Intention.Type;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import jason.asSyntax.NumberTerm;

public class addIntention extends DefaultInternalAction{
	private static final long serialVersionUID = -3447388669731589527L;
	
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int unitID = (int)( (NumberTerm) terms[0]).solve();
		int targetID = (int)( (NumberTerm) terms[1]).solve();
		int type = (int)( (NumberTerm) terms[2]).solve();
		String sorter = ((StringTerm)terms[3]).getString();
		
		Unit unit = GameMap.searchUnit(unitID);
		GameObject target = null;
		
		Intention.Type iType;
		
		if (type == 0) {
			iType =Type.TEMPORARY;
		} else {
			iType =Type.PERSISTENT;
		}
		
		if (sorter.equals("knowledge")) { //TODO if support add it here probably
			target = GameMap.searchKnowledge(targetID);
			unit.addIntention(target, new Intention(Unit.SEIZE, iType));
		} else if (sorter.equals("enemy")) {
			target = GameMap.searchUnit(targetID);
			unit.addIntention(target, new Intention(Unit.KILL, iType));
		} else if (sorter.equals("base")) {
			target = GameMap.searchBase(targetID);
			unit.addIntention(target, new Intention(Unit.SEIZE, iType));
		}
		
		return true;
	}

}
