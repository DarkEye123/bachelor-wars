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

//-----------------------------------------------------------------General knowledge of map---------------------------------------------------
mode(unknown)[source(percept)].
agentID(unknown)[source(percept)].

actualKnowledge(unknown)[source(percept)].
freeSlots(unknown)[source(percept)].
maximumSlots(unknown)[source(percept)].

//-----------------------------------------------------------------------Intentions-----------------------------------------------------------
killIntention(0). //damage with atk
healIntention(1).
seizeIntention(2).
buffIntention(3). //use a power with given intention TODO try to find out if necessary
supportIntention(4). //Support copy intentions of friendly target unit. Do not forget to remove this intention after that

//-----------------------------------------------------------------------Rules----------------------------------------------------------------
enoughSlots :- freeSlots(N)[source(percept)] & N > 0 & .print("free slots: ",N).
dominationMode :- mode(N)[source(percept)] & N == 0. //0 for domination mode
isKillingIntention(Type) :- killIntention(N) & N == Type.
isHealingIntention(Type) :- healIntention(N) & N == Type.
isSeizeIntention(Type) :- seizeIntention(N) & N == Type.

//isKnowledgeInReach(Unit, Knowledge) :- .getUnitKnowledgePairs(Unit, Knowledge) & .print("Knowledge: ", Knowledge).

//test1([]).
//test2([]).


//!start.
//+!test(List, Place) : true <- .nth(1,List,X); .nth(2,List,Y); Place=[X,Y].
//+!start: true <- !test([1,2,3],Place); .print(Place).
//+!start: jason.test(K) <- .print("preslo ", K).
//-!start: true <- .print("nepreslo").
//!start.
//+!start : true <- ?test1(N); ?test2(M); !compare(N,M). 
//+!compare(N,M) : N == M <- .print(N == M).
//+!compare(N,M) : N \== M <- .print(N \== M).
//-!start : true <- !start.

//------------------------------------------------------------------------unit-stats------------------------------------------------------------
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
+!getPostion(Unit,Place) : true
	<- .nth(8,Unit,X); .nth(9,Unit,Y); Place=[X,Y].
+!getOwner(Unit,Stat) : true
	<- .nth(10,Unit,Stat).


//------------------------------------------------------------------------knowledge-stats------------------------------------------------------------	
+!getKnowledgePosition([H|B], P) : true
	<- P=B.
+!getKnowledgeId(Knowledge, Stat) : true
	<- .nth(0,Knowledge,Stat).
	
//------------------------------------------------------------------------intentions-stats------------------------------------------------------------
+!getTypeOfIntention(Intention, Type): true 
	<-	.nth(1,Intention, Type).

//+!decomp([H|B]) : true <- .print(H); !decomp(H); !decomp(B).
//+!decomp(X).

+!getRandomUnit(Units,Unit) : true
	<-	X=math.floor(math.random(.length(Units)));
		.nth(X, Units, Unit);.

+!createRandomUnit(ID, Units, UnitID) : true 
	<- 	!getRandomUnit(Units, U);
		.print("choosing unit: ", U, "from ", Units);
		!getType(U, Type);
		create_unit(ID, Type);
		?created_unit(UnitID); //here is unit id created
		.print("created unit id: ", UnitID).
			
+!moveUnit(Unit): not jason.hasIntention(Unit) & jason.getNearestFreeKnowledge(Unit,Knowledge) & .print("Moving unit: ", Unit, " to knowledge: ", Knowledge, " adding intention")
	<-	!getKnowledgePosition(Knowledge,Pos); 
		move(Unit, Pos); //move unit in that direction
		!getKnowledgeId(Knowledge, TargetObject);
		do_intention_if_possible(Unit, TargetObject).
		
+!moveUnit(Unit): not jason.hasIntention(Unit) & jason.getNearestFreeEnemy(Unit,Enemy) & .print("Moving unit: ", Unit, " to free enemy: ", Knowledge, " adding intention")
	<-	!getPostion(Enemy,Place);
		move(Unit, Place); //move unit in that direction
		!getId(Enemy,TargetObject);
		do_intention_if_possible(Unit, TargetObject).
		
+!moveUnit(Unit): not jason.hasIntention(Unit) & jason.getNearestEnemy(Unit,Enemy) & .print("Moving unit: ", Unit, " to enemy: ", Knowledge, " adding intention")
	<-	!getPostion(Enemy,Place);
		move(Unit, Place); //move unit in that direction
		!getId(Enemy,TargetObject);
		do_intention_if_possible(Unit, TargetObject).

+!moveUnit(Unit): jason.hasIntention(Unit)
	<-	?mode(Mode);
		jason.getSortedIntentionsByDistance(Unit, Mode, Intentions); //if mode is domination then most priority in actual round have seize intentions, next attack and then support
		.nth(0, Intentions, Intention);
		.print("Unit : ", Unit, " chooses intention: ", Intention);
		!getTypeOfIntention(Intention, Type);
		!moveUnit(Unit, Intention, Type).

+!moveUnit(Unit, Intention, Type): isKillingIntention(Type)
	<- 	.nth(0, Intention, Enemy);
		!getPostion(Enemy, Place);
		move(Unit, Place); //move unit in that direction
		!getId(Enemy,TargetObject);
		do_intention_if_possible(Unit, TargetObject).
		
+!moveUnit(Unit, Intention, Type): isHealingIntention(Type)
	<- 	.nth(0, Intention, FriendlyUnit);
		!getPostion(FriendlyUnit, Place);
		move(Unit, Place); //move unit in that direction
		!getId(FriendlyUnit,TargetObject);
		do_intention_if_possible(Unit, TargetObject).
		
+!moveUnit(Unit, Intention, Type): isSeizeIntention(Type) //TODO add difference between knowledge and base
	<- 	.nth(0, Intention, Object);
		!getKnowledgePosition(Object, Place);
		move(Unit, Place); //move unit in that direction
		!getKnowledgeId(Object, TargetObject);
		do_intention_if_possible(Unit, TargetObject).

+!listUnits([Unit|Units]): true
	<- 	!getId(Unit, UnitId); 
		update_percepts;
		!moveUnit(UnitId); 
		!listUnits(Units).
		
+!listUnits([]).
		
+!check_action(ID): jason.getUsableUnits(ID, Units)
	<-	!listUnits(Units);
		update_percepts;
		!check_action(ID).

+!check_action(ID): enoughSlots & jason.getAffordableUnits(ID, Units) //agentID(N) wtf?
	<- 	!createRandomUnit(ID, Units, UnitID); //here is unit created from a template and actual created unit is given
		!moveUnit(UnitID);
		update_percepts;
		!check_action(ID).
		 
-!check_action(ID): true <- update_percepts; mark_done.


+can_act <- .print("preparing action"); ?agentID(N); update_percepts; !check_action(N).
		
-can_act <- .print("can_act removed, waiting for next turn").


