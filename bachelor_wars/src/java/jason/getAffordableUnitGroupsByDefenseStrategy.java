// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import objects.Base;
import objects.units.CommonBachelor;
import objects.units.FirstYear;
import objects.units.FitBachelor;
import objects.units.FourthYear;
import objects.units.SecondYear;
import objects.units.ThirdYear;
import objects.units.Unit;

public class getAffordableUnitGroupsByDefenseStrategy extends jason.getAffordableUnitGroups {
	private static final long serialVersionUID = 3092366345621802016L;

	private int computeDefensePower(List<Unit> combination) {
		int sum = 0;
		for (Unit u:combination) {
			sum += u.getHp();
		}
		return sum;
	}
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	canUnify = false;
    	if ((boolean)super.execute(ts, un, terms) == false) {
    		return false;
    	}
    	
    	
    	LinkedList<Group> groups = new LinkedList<>();
    	for (List<Unit> combination:combinations) {
    		groups.add(new Group(computeDefensePower(combination), combination));
    	}
    	
    	Collections.sort(groups);
    	Collections.reverse(groups);
    	ListTerm list = ListTermImpl.parseList(groups.toString());
    	un.unifies(terms[1], list);
    	
        return true;
    }
    
    public static void main(String[] args) {
    	getAffordableUnitGroupsByDefenseStrategy test = new getAffordableUnitGroupsByDefenseStrategy();
		test.available = new LinkedList<>();
		test.base = new Base();
//		test.base.addKnowledge(500);
//		test.base.setFreeSlots(3);
		test.available.add(FirstYear.getPrototype());
		test.available.add(SecondYear.getPrototype());
		test.available.add(ThirdYear.getPrototype());
		test.available.add(FourthYear.getPrototype());
		test.available.add(CommonBachelor.getPrototype());
		test.available.add(FitBachelor.getPrototype());
		LinkedList<Group> groups = new LinkedList<>();
		test.combinations = test.findCombinations();
		
    	for (List<Unit> combination:test.combinations) {
    		jason.getAffordableUnitGroups.Group g = test.new Group(test.computeDefensePower(combination), combination);
    		groups.add(g);
    	}
    	
    	Collections.sort(groups);
    	Collections.reverse(groups);
    	
    	for (Group g : groups) {
    		System.out.println(g);
    	}
	}
}
