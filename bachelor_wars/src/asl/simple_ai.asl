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

//General knowledge of map
mode(unknown)[source(percept)].
agentID(unknown)[source(percept)].
availableUnits([])[source(percept)]. //stats: type, id, cost, hp, atk, mov, atkRange, sp -> these are units that can be used for creation, but player doesn't own them
playingUnits([])[source(percept)]. //stats: type, id, cost, hp, atk, mov, atkRange, sp -> enemies units
knowledgeSources([])[source(percept)]. //list of knowledge positions in this format [[x,y],[x,y],..]

actualKnowledge(unknown)[source(percept)].
freeSlots(unknown)[source(percept)].
maximumSlots(unknown)[source(percept)].

ownedUnits([])[source(percept)]. // type, id, cost, hp, atk, mov, atkRange, sp, x, y, owner TODO class
ownedUnusedUnits([])[source(percept)]. // type, id, cost, hp, atk, mov, atkRange, sp, x, y, owner
possibleUnits([])[source(percept)].


enoughSlots :- freeSlots(N)[source(percept)] & N > 0 & .print("free slots: ",N).
dominationMode :- mode(N)[source(percept)] & N == 0. //0 for domination mode
//isKnowledgeInReach(Unit, Knowledge) :- .getUnitKnowledgePairs(Unit, Knowledge) & .print("Knowledge: ", Knowledge).

//test1([]).
//test2([]).

//!start.
//+!start: true <- jason.test([1,2]).
//!start.
//+!start : true <- ?test1(N); ?test2(M); !compare(N,M). 
//+!compare(N,M) : N == M <- .print(N == M).
//+!compare(N,M) : N \== M <- .print(N \== M).
//-!start : true <- !start.

//unit stats
+!getType(Unit,Stat) : true
	<- .nth(0,Unit,Stat).
+!getId(Unit,Stat) : true
	<- .nth(1,Unit,Stat).
+!getCost(Unit,Stat) : true
	<- .nth(2,Unit,Stat).
+!getHp(Unit,Stat) : true
	<- .nth(3,Unit,Stat).
+!getAtk(Unit,Stat) : true
	<- .nth(4,Unit,Stat).
+!getMov(Unit,Stat) : true
	<- .nth(5,Unit,Stat).
+!getRange(Unit,Stat) : true
	<- .nth(6,Unit,Stat).
+!getSp(Unit,Stat) : true
	<- .nth(7,Unit,Stat).
+!getXY(Unit,X,Y) : true
	<- .nth(8,Unit,X); .nth(9,Unit,Y).
+!getOwner(Unit,Stat) : true
	<- .nth(10,Unit,Stat).
	

//+!decomp([H|B]) : true <- .print(H); !decomp(H); !decomp(B).
//+!decomp(X).

+!getRandomUnit(Units,Unit) : true
	<-	X=math.floor(math.random(.length(Units)));
		.nth(X, Units, Unit);.

+!createRandomUnit(ID, Units, Unit) : true 
	<- 	!getRandomUnit(Units,Unit);
		.print("choosing unit: ", Unit);
		!getType(Unit, Id);
		create_unit(ID, Id);
		update_percepts.
			
+!moveUnit(Unit): jason.getNearestFreeKnowledge(Unit,Knowledge) & not jason.hasIntention(Unit)
	<- move(Unit, Knowledge). //move unit in that direction
		
+!moveUnit(Unit, Place) : jason.isEmpty(Place)
	<-	!getId(Unit,Id); 
		move(Id, Place).
		
//+!move_unit(U,X,Y) : i 
//		<-	move_unit()


+can_act <- .print("preparing action"); ?agentID(N); .print(N); update_percepts; !check_action(N).

+!check_action: ownedUnusedUnits[source(percept)]
	<-	!move_units.

+!check_action(ID): enoughSlots & jason.getAffordableUnits(ID, Units) //agentID(N) wtf?
	<- 	!createRandomUnit(ID, Units, Unit);
		!getId(Unit,Id);
		!moveUnit(Id);
		update_percepts;
		!check_action(ID).
		 
-!check_action(ID): true <- mark_done.


		
-can_act <- .print("can_act removed, waiting for next turn").


