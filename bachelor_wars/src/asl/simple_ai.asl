// Agent simple_ai in project bachelor_wars

//Some stuff to know

//Goals has the same syntax as beliefs, but are prefixed by
//! (achievement goal) or
//? (test goal)

//Achievement goal: goal to do
//Test goal: goal to know

//Negation
//Better solution is to distinguish:
//what the agent believes to be true,
//what the agent believes to be false, and
//what the agent believes nothing about
//~ operator (strong negation): trie if agent has an explicit belief that its argument is false
//not operator(negation as failure)  - is true if the interpreter fails to derive its argument

//if added achievement ot test goal g fails, the interpreter generates a -!g event


/* Initial beliefs and rules */

//HERE IS VERY IMPORTANT NOTE!!!! if you have freeSlots(unknown) for example and you update it for some value like 10 there is a note as [source(percept)] 
//so you need to use it that way

actualKnowledge(unknown)[source(percept)].
freeSlots(unknown)[source(percept)].
maximumSlots(unknown)[source(percept)].

//TODO here it fails
can_create_unit :- freeSlots(N)[source(percept)] & N > 0 & .print(N).

/* Initial goals */

//!start.

/* Plans */

//+!start : true <- !wait.
//
//@can_act_True
//+!wait: can_act <- .print("Can create").
//
//@can_act_False_or_unknown
//+!wait: not can_act <- !wait.
//
//+!create_unit: true <- .print("creating"); create_unit.
//
//+can_act <- .print("pridane"); create_unit.
//-can_act <- .print("odobrane").

//+!start : true <- !wait.
//+!wait: not can_act <- !wait.

+can_act <- .print("preparing action"); update_percepts; !check_action.

//is possible buy even the cheapest unit
+!check_action: can_create_unit & enough_knowledge <- create_unit; move_units; mark_done.
-!check_action: true <- mark_done.
		
-can_act <- .print("can_act removed, waiting for next turn").

