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

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

public class getAffordableUnitGroups extends jason.getAffordableUnits {
	private static final long serialVersionUID = 4986115738917608744L;
	
	LinkedList<List<Unit>> combinations;
	
	/**
	 * This class represents a combination group connected with its computed value (value can be f.e. atk, hp etc ...)
	 * @author darkeye
	 *
	 */
	public class Group implements Comparable<Group>{
		int value;
		List<Unit> combination;
		
		public Group(int value, List<Unit> combination) {
			this.value = value;
			this.combination = combination;
		}
		
		@Override
		public int compareTo(Group o) {
			if (value > o.value)
				return 1;
			else if (value < o.value)
				return - 1;
			else {
				int size1 = combination.size();
				int size2 = o.combination.size();
				if (size1 > size2)
					return 1;
				else if (size1 < size2)
					return - 1;
				else {
					return computePrice(o.combination) - computePrice(combination); //here it is inverted, if we have same value and size, cheaper combination wins
				}
			}
		}

		@Override
		public String toString() {
			return "[ " + value + ", " + combination  + " ]";
		}
		
	}
	
	private int computePrice(List<Unit> input) {
		int sum = 0;
		for (Unit u:input) {
			sum += u.getCost();
		}
		return sum;
	}
	
	private int computePrice(ICombinatoricsVector<Unit> input) {
		return this.computePrice(input.getVector());
	}


	/**
	 * Generates combinations of units according available knowledge, these combinations are later used for choosing 
	 * right combination prior agent request (combinations with best defense ability, attack ability etc ..)
	 * @return
	 */
	protected LinkedList<List<Unit>> findCombinations() {
    	LinkedList<List<Unit>> variants = new LinkedList<>();
    	
		LinkedList<Unit> toRemove = new LinkedList<Unit>();
		LinkedList<Unit> pom = new LinkedList<Unit>();
		for (Unit u: available) {
			if (u.getCost() >= base.getKnowledge()) { //remove units, that cost so much, that it is impossible to combine them with others
				toRemove.add(u);
				if (u.getCost() == base.getKnowledge()) {
					pom.clear();
					pom.add(u);
					variants.add((LinkedList<Unit>) pom.clone()); //add them to final list, its a combination too
				}
			}
		}
		available.removeAll(toRemove);
	
		// Create the initial vector of available units
		ICombinatoricsVector<Unit> initialVector = Factory.createVector(available);

		// Create a multi-combination generator to generate 3-combinations of
		// the initial vector
		LinkedList<Generator<Unit>> combinations = new LinkedList<>();
	   	for (int x = 0; x < base.getFreeSlots(); ++x) {
	   		Generator<Unit> gen = Factory.createMultiCombinationGenerator(initialVector, x+1);
	   		combinations.add(gen);
	   	}

	   	// Print all possible combinations
	   	for (Generator<Unit> gen: combinations) {
	   		for (ICombinatoricsVector<Unit> combination : gen) {
	   			if (computePrice(combination) <= base.getKnowledge()) {
    	    	  variants.add(combination.getVector());
	   			}
	   		}
	   	}
	   
	   	for (List<Unit> u : variants) {
	   		Collections.sort(u);
	   	}
	   	return variants;
	}

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	if ((boolean)super.execute(ts, un, terms) == false) {
    		return false;
    	} else {
    		combinations = findCombinations();
    		
    		if (combinations.isEmpty())
    			return false;
    		
    		ListTerm list = ListTermImpl.parseList(combinations.toString());
        	un.unifies(terms[1], list);
    		return true;
    	}
    }
    
    public static void main(String[] args) {
    	getAffordableUnitGroups test = new getAffordableUnitGroups();
		test.available = new LinkedList<>();
		test.base = new Base();
//		test.base.addKnowledge(100);
		test.available.add(FirstYear.getPrototype());
		test.available.add(SecondYear.getPrototype());
		test.available.add(ThirdYear.getPrototype());
		test.available.add(FourthYear.getPrototype());
		test.available.add(CommonBachelor.getPrototype());
		test.available.add(FitBachelor.getPrototype());
		Collections.sort(test.available);
		LinkedList<List<Unit>> variants = test.findCombinations();
		for (List<Unit> u : variants) {
	   		System.out.println(u + " price: " + test.computePrice(u));
	   	}
//		
	}
}
