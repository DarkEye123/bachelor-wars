// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;
import java.util.TreeMap;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;
import objects.Base;
import objects.Knowledge;
import ui.GameMap;

public class setRole extends DefaultInternalAction {
	private static final long serialVersionUID = 7916747150499005371L;

	private static final float SEIZER_PERCENTAGE = 80.0f;

	Base base;

	public LinkedList<Knowledge> get80() {
		LinkedList<Knowledge> output = new LinkedList<>();
		TreeMap<Integer, Integer> source = new TreeMap<>();
		int treshold = (int) (((float)GameMap.getKnowledgeList().size()/100.0f) * SEIZER_PERCENTAGE);
		for (Knowledge k:GameMap.getKnowledgeList()) {
			source.put(k.getId(), base.getNode().distance(k.getNode()));
		}

		int num = GameMap.getKnowledgeList().size();
		while (num > treshold) {
			--num;
			int min = Integer.MAX_VALUE;
			int keyToRemove = 0;
			for (Integer key:source.keySet()) {
				if (source.get(key) < min) {
					min = source.get(key);
					keyToRemove = key;
				}
			}
			source.remove(keyToRemove);
		}
		for (Knowledge k:GameMap.getKnowledgeList()) {
			if ( source.keySet().contains(k.getId()) )
				output.add(k);
		}
		return output;
	}

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int agentID = (int)((NumberTerm) terms[0]).solve();
		String role = ((StringTermImpl) terms[1]).getString();
		base  = GameMap.searchBase(agentID);
		
		if (role.equals("seizer")) {
			base.setAvailableKnowledge(get80());
		} else if (role.equals("attacker")){
			LinkedList<Knowledge> list = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
			list.removeAll(base.findSeizerBase().getAvailableKnowledge());
			base.setAvailableKnowledge(list);
		}
		
		base.setRole(role);
		return true;
	}
}
