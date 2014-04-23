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

//----------------------------------------------------------------------Available-Modes-------------------------------------------------------
domination(0).
anihilation(1).
madness(2).

//----------------------------------------------------------------------Available-Classes-----------------------------------------------------
tank(1).
healer(2).
damageDealer(4).
supporter(8).

//-----------------------------------------------------------------------Rules----------------------------------------------------------------
enoughSlots :- freeSlots(N)[source(percept)] & N > 0 & .print("free slots: ",N).
isKillingIntention(Type) :- killIntention(N) & N == Type.
isHealingIntention(Type) :- healIntention(N) & N == Type.
isSeizeIntention(Type) :- seizeIntention(N) & N == Type.

isDominationMode :- mode(N) & domination(M) & N == M.
isAnihilationMode :- mode(N) & anihilation(M) & N == M.
isMadnessMode :- mode(N) & madness(M) & N == M.

isTankClass(Type) :- tank(M) & Type == M.
isHealerClass(Type) :- healer(M) & Type == M.
isDamageDealerClass(Type) :- damageDealer(M) & Type == M.
isSupporterClass(Type) :- supporter(M) & Type == M.

//---------------------------------------------------------------------Rule-Variants-Of-Goals-For-Unit-Stats----------------------------------
getType(Unit,Stat) :- 
	.nth(0,Unit,Stat).
getID(Unit,Stat) :-
	.nth(1,Unit,Stat).
getCost(Unit,Stat) :-
	.nth(2,Unit,Stat).
getHp(Unit,Stat) :-
	.nth(3,Unit,Stat).
getAtk(Unit,Stat) :-
	.nth(4,Unit,Stat).
getMov(Unit,Stat) :-
	.nth(5,Unit,Stat).
getRange(Unit,Stat) :-
	.nth(6,Unit,Stat).
getSp(Unit,Stat) :-
	.nth(7,Unit,Stat).
getPostion(Unit,Place) :-
	.nth(8,Unit,X) & .nth(9,Unit,Y) & Place=[X,Y].
getOwner(Unit,Stat) :-
	.nth(10,Unit,Stat).
getClass(Unit,Stat) :-
	.nth(11,Unit,Stat).

//test(Val) :- .nth(1,[1,2,3],X) & .nth(2,[1,2,3],Y) & Val=[X,Y].
//
//!start.
//+!start : test(Val) <- .print(Val). 

//------------------------------------------------------------------------unit-stats------------------------------------------------------------
+!getType(Unit,Stat) : true
	<- .nth(0,Unit,Stat).
+!getID(Unit,Stat) : true
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
+!getClass(Unit,Stat) : true
	<- .nth(11,Unit,Stat).
	
//------------------------------------------------------------------------knowledge-stats------------------------------------------------------------	
+!getKnowledgePosition([H|B], P) : true
	<- P=B.
+!getKnowledgeId(Knowledge, Stat) : true
	<- .nth(0,Knowledge,Stat).
	
//------------------------------------------------------------------------intentions-stats------------------------------------------------------------
+!getTypeOfIntention(Intention, Type): true 
	<-	.nth(1,Intention, Pom);
		.nth(0,Pom, Type).

+!getRandomUnit(Units,Unit) : true
	<-	X=math.floor(math.random(.length(Units)));
		.nth(X, Units, Unit);.

+!createRandomUnit(ID, Units, Unit) : true 
	<- 	!getRandomUnit(Units, U);
		.print("choosing unit: ", U, "from ", Units);
		!getType(U, Type);
		create_unit(ID, Type);
		?created_unit(Unit); //here is unit id created
		!getID(Unit, UnitID);
		.print("created unit id: ", UnitID).

//-----------------------------------------------------------------Class-Based-Intentions--------------------------------------------------------------------------		
//TODO - add power use decisions -> it's part of intention
+!addClassBasedIntention(Unit, Class, _) : isHealerClass(Class) & 
										jason.getFriendlyUnitInReach(Unit, FriendlyUnit) & 
										jason.shouldHeal(FriendlyUnit)
	<-	!getID(FriendlyUnit, TargetObject);
		jason.addIntention(Unit, TargetObject, 0, "friendly").
	
+!addClassBasedIntention(Unit, _, _) : jason.getEnemyUnitInReach(Unit, EnemyUnit) //default action for every unit if none of previous is possible
	<-	!getID(EnemyUnit, TargetObject);
		jason.addIntention(Unit, TargetObject, 0, "enemy").

+!addClassBasedIntention(Unit, _, Source) : Source \== "knowledge" & jason.getFreeKnowledgeInReach(Unit, Knowledge) //default action for every unit if none of previous is possible
	<-	!getKnowledgeId(Knowledge, TargetObject);
		jason.addIntention(Unit, TargetObject, 0, "knowledge").

+!addClassBasedIntention(_, _, _). //end point if there is nothing in reach

//--------------------------------------------------------------------Without-Intentions---------------------------------------------------------------------------			
//####################################################################--MODE-DOMINATION--##########################################################################
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isDominationMode & jason.getNearestFreeKnowledge(UnitID,Knowledge) 
	& .print("Moving unit: ", UnitID, " to knowledge: ", Knowledge, " adding intention")
	<-	!getKnowledgeId(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!getClass(Unit,Class);
		!addClassBasedIntention(UnitID, Class, "knowledge");
		!moveUnit(Unit).
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isDominationMode & jason.getNearestFreeEnemy(UnitID,Enemy) 
	& .print("Moving unit: ", UnitID, " to free enemy: ", Enemy, " adding intention")
	<-	!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!getClass(Unit,Class);
		!addClassBasedIntention(UnitID, Class, "enemy");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isDominationMode & jason.getNearestEnemy(UnitID,Enemy) 
	& .print("Moving unit: ", UnitID, " to enemy: ", Enemy, " adding intention")
	<-	!getPostion(Enemy,Place);
		!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		move(UnitID, Place); //move unit in that direction
		do_intention_if_possible(UnitID, TargetObject).
//##################################################################################################################################################################
//===================================================================================================================================================================

//--------------------------------------------------------------------With-Intentions--------------------------------------------------------------------------------
+!moveUnit(Unit): getID(Unit, UnitID) & jason.hasIntention(UnitID)
	<-	?mode(Mode);
		jason.getSortedIntentionsByDistance(UnitID, Mode, Intentions); //if mode is domination then most priority in actual round have seize intentions, next attack and then support
		.nth(0, Intentions, Intention);
		.print("Unit : ", UnitID, " chooses intention: ", Intention, " from: ", Intentions);
		!getTypeOfIntention(Intention, Type);
		!moveUnit(Unit, Intention, Type).

+!moveUnit(Unit, Intention, Type): getID(Unit, UnitID) & isKillingIntention(Type)
	<- 	.nth(0, Intention, Enemy);
		!getPostion(Enemy, Place);
		move(UnitID, Place); //move unit in that direction
		!getID(Enemy,TargetObject);
		do_intention_if_possible(UnitID, TargetObject).
		
+!moveUnit(Unit, Intention, Type): getID(Unit, UnitID) & isHealingIntention(Type)
	<- 	.nth(0, Intention, FriendlyUnit);
		!getPostion(FriendlyUnit, Place);
		move(UnitID, Place); //move unit in that direction
		!getID(FriendlyUnit,TargetObject);
		do_intention_if_possible(UnitID, TargetObject).
		
+!moveUnit(Unit, Intention, Type): getID(Unit, UnitID) & isSeizeIntention(Type) //TODO add difference between knowledge and base
	<- 	.nth(0, Intention, Object);
		!getKnowledgePosition(Object, Place);
		move(UnitID, Place); //move unit in that direction
		!getKnowledgeId(Object, TargetObject);
		do_intention_if_possible(UnitID, TargetObject).
//===================================================================================================================================================================

+!listUnits([Unit|Units]): true
	<- 	update_percepts;
		!moveUnit(Unit); 
		!listUnits(Units).
		
+!listUnits([]).
		
+!check_action(ID): jason.getUsableUnits(ID, Units)
	<-	!listUnits(Units);
		update_percepts;
		!check_action(ID).

+!check_action(ID): enoughSlots & jason.getAffordableUnits(ID, Units) //agentID(N) wtf?
	<- 	!createRandomUnit(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		 
-!check_action(ID): true <- update_percepts; mark_done.


+can_act <- .print("preparing action"); ?agentID(N); update_percepts; !check_action(N).
		
-can_act <- .print("can_act removed, waiting for next turn").


