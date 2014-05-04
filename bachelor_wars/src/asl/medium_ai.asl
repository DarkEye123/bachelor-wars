// Agent medium_ai in project bachelor_wars

/* Initial beliefs and rules */

//-----------------------------------------------------------------General knowledge of map---------------------------------------------------
mode(unknown)[source(percept)].
agentID(unknown)[source(percept)].
actualKnowledge(unknown)[source(percept)].
freeSlots(unknown)[source(percept)].
maximumSlots(unknown)[source(percept)].
fightingPower(unknown)[source(percept)].
movingPower(unknown)[source(percept)].
type(unknown)[source(Ag)]. //type can be attacker(enemy diversion till seizer has money for better units), seizer (economy later attack)

//-----------------------------------------------------------------------Intentions-----------------------------------------------------------
killIntention(0). //damage with atk
seizeIntention(2).
//----------------------------------------------------------------------Available-Modes-------------------------------------------------------
domination(0).
anihilation(1).
madness(2).

//-----------------------------------------------------------------------Rules----------------------------------------------------------------
enoughSlots :- freeSlots(N)[source(percept)] & N > 0 & .print("free slots: ",N).
isKillingIntention(Type) :- killIntention(N) & N == Type.
isSeizeIntention(Type) :- seizeIntention(N) & N == Type.

isDominationMode :- mode(N) & domination(M) & N == M.
isAnihilationMode :- mode(N) & anihilation(M) & N == M.
isMadnessMode :- mode(N) & madness(M) & N == M.

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
getPostion(Unit,Place) :-
	.nth(7,Unit,X) & .nth(8,Unit,Y) & Place=[X,Y].
getOwner(Unit,Stat) :-
	.nth(9,Unit,Stat).
	
//---------------------------------------------------------------------Rule-Variants-Of-Goals-For-Intention-Stats----------------------------------
getTypeOfIntention(Intention, Type) :- 
	.nth(1,Intention, Pom) & .nth(0,Pom, Type).

getKnowledgeId(Knowledge, Stat) :-
	.nth(0,Knowledge,Stat).
	
//------------------------------------------------------------------------unit-stats------------------------------------------------------------

//!start.

+!getUnitByMovement([], []).

+!getUnitByMovement([H|B], [X|C]) : true
	<- 	!drawPositionInList(H, 5, X); 
		!test(B, C).

+!drawPositionInList(InputList, PositionToDraw, FinalList) : true 
	<- 	!getType(InputList, Type);
		!getID(InputList, ID);
		.nth(PositionToDraw, InputList, Ret);
		FinalList = [Ret, Type, ID].
		
+!drawPositionInList([], _, []).


//+!makeList([], X, [X]).
//	
//+!makeList([H|B], X, [H|C]) : true
//	<- !test(B, X, C).


//+!test([], X, X).
//	
//+!test([H|B], X, [H|C]) : true
//	<- !test(B, X, C).
	
	
//+!start : true <- .max([[1, "test1"],[3, "test2"],[0, "test4"],[2, "test3"]], X); .print(X).
//+!start :true <- !test([1,2,4], 3, Y); .print(Y).
//+!start :true <- !test([1,2,4], [[3]], Y); .print(Y).
//+!start :true <- !test([], 3, Y); .print(Y); !test(Y, 1, X); .println(X).
//+!start :true <- !test([ [1, 2, 3], [5, 6, 7] ], 1, Y); .print(Y).

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
+!getPostion(Unit,Place) : true
	<- .nth(7,Unit,X); .nth(8,Unit,Y); Place=[X,Y].
+!getOwner(Unit,Stat) : true
	<- .nth(9,Unit,Stat).
	
//------------------------------------------------------------------------knowledge-stats------------------------------------------------------------	
+!getKnowledgePosition([H|B], P) : true
	<- P=B.
+!getKnowledgeId(Knowledge, Stat) : true
	<- .nth(0,Knowledge,Stat).
	
//------------------------------------------------------------------------intentions-stats------------------------------------------------------------
+!getTypeOfIntention(Intention, Type): true 
	<-	.nth(1,Intention, Pom);
		.nth(0,Pom, Type).
		
//------------------------------------------------------------------------base-stats------------------------------------------------------------------
+!getBaseId(Base, Stat) : true
	<- .nth(0,Base,Stat).
	
+!getBasePosition(Base,Stat) : true
	<- .nth(1,Base,X); .nth(2,Base,Y); Stat=[X,Y].

+!getBaseKnowledge(Base, Stat) : true
	<- .nth(3,Base,Stat).

+!getBaseFightingPower(Base, Stat): true
	<- .nth(4,Base,Stat).
	
+!getBaseMovingPower(Base, Stat): true
	<- .nth(5,Base,Stat).
	
//====================================================================================================================================================



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
//####################################################################--MODE-DOMINATION--##########################################################################

+!addPossibleIntention(UnitID, Type) : 	isDominationMode & 
											Type \== "knowledge" & 
											jason.getKnowledgeInReach(UnitID, Knowledge) & //desired action for domination mode - higher priority
											getKnowledgeId(Knowledge, TargetObject) & 
											TargetObject \== Type & //check if this intention isn't the same as
											.print("Unit: ", UnitID, " adding possible based intention(knowledge): ", Knowledge)
	<-	jason.addIntention(UnitID, TargetObject, 0, "knowledge").
		
+!addPossibleIntention(UnitID, Type) : 	isDominationMode &
											Type \== "enemy" &
											jason.getEnemyUnitInReach(UnitID, EnemyUnit) &
											getID(EnemyUnit, TargetObject) &
											TargetObject \== Type &//default action for every unit if none of previous is possible
											.print("Unit: ", UnitID, " adding possible based intention(enemy): ", EnemyUnit)
	<-	jason.addIntention(UnitID, TargetObject, 0, "enemy").
//#################################################################################################################################################################

//####################################################################--MODE-ANIHILATION--##########################################################################

+!addPossibleIntention(UnitID, Type) : 	isAnihilationMode & 
										Type \== "knowledge" & 
										jason.getKnowledgeInReach(UnitID, Knowledge) & //desired action for domination mode - higher priority
										getKnowledgeId(Knowledge, TargetObject) & 
										TargetObject \== Type & //check if this intention isn't the same as
										.print("Unit: ", UnitID, " adding possible based intention(knowledge): ", Knowledge)
	<-	jason.addIntention(UnitID, TargetObject, 0, "knowledge").
		
+!addPossibleIntention(UnitID, Type) : 	isAnihilationMode &
										Type \== "enemy" &
										jason.getEnemyUnitInReach(UnitID, EnemyUnit) &
										getID(EnemyUnit, TargetObject) &
										TargetObject \== Type &//default action for every unit if none of previous is possible
										.print("Unit: ", UnitID, " adding possible based intention(enemy): ", EnemyUnit)
	<-	jason.addIntention(UnitID, TargetObject, 0, "enemy").
//#################################################################################################################################################################

//####################################################################--MODE-MADNESS--##########################################################################

+!addPossibleIntention(UnitID, Type) : 	isMadnessMode &
											Type \== "enemy" &
											jason.getEnemyUnitInReach(UnitID, EnemyUnit) &
											getID(EnemyUnit, TargetObject) &
											TargetObject \== Type &//default action for every unit if none of previous is possible
											.print("Unit: ", UnitID, " adding possible based intention(enemy): ", EnemyUnit)
	<-	jason.addIntention(UnitID, TargetObject, 0, "enemy").
	
+!addPossibleIntention(UnitID, Type) : 	isMadnessMode & 
											Type \== "knowledge" & 
											jason.getKnowledgeInReach(UnitID, Knowledge) & //desired action for domination mode - higher priority
											getKnowledgeId(Knowledge, TargetObject) & 
//											.print("IM HEEEEEEEEEEEEEEEEEEEEEEEEEEEEERE ", TargetObject, " ", Type) &
											TargetObject \== Type & //check if this intention isn't the same as
											.print("Unit: ", UnitID, " adding possible based intention(knowledge): ", Knowledge)
	<-	jason.addIntention(UnitID, TargetObject, 0, "knowledge").
		
//##################################################################################################################################################################

+!addPossibleIntention(_, _). //end point if there is nothing in reach
//==================================================================================================================================================================

+!addEnemyBases(UnitID) : true
	<- 	?agentID(ID);
		jason.getEnemyBases(ID, Bases);
		!addEnemyBases(UnitID, Bases). //list

+!addEnemyBases(UnitID, [H|B]): true
	<-	.nth(0, H, N);
		jason.addIntention(UnitID, N, 1, "base");
		.print("Adding enemy base: ", N);
		!addEnemyBases(UnitID, B).

+!addEnemyBases(_, []).

//--------------------------------------------------------------------Without-Intentions---------------------------------------------------------------------------			
//####################################################################--MODE-DOMINATION--##########################################################################
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isDominationMode & jason.getNearestFreeKnowledge(UnitID,Knowledge) 
	& .print("Unit: ", UnitID, " to knowledge: ", Knowledge, " adding intention")
	<-	!getKnowledgeId(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "knowledge");
		.print("HEREEEEEEE");
		!moveUnit(Unit).
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isDominationMode & jason.getNearestFreeEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to free enemy: ", Enemy, " adding intention")
	<-	!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "enemy");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isDominationMode & jason.getNearestEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to enemy: ", Enemy, " adding intention")
	<-	!getPostion(Enemy,Place);
		!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!moveUnit(Unit).
//##################################################################################################################################################################

//####################################################################--MODE-ANIHILIATION--##########################################################################
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isAnihilationMode & jason.getNearestFreeEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to free enemy: ", Enemy, " adding intention")
	<-	!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "enemy");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isAnihilationMode & jason.getNearestFreeKnowledge(UnitID,Knowledge) 
	& .print("Unit: ", UnitID, " to knowledge: ", Knowledge, " adding intention")
	<-	!getKnowledgeId(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "knowledge");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isAnihilationMode & jason.getNearestEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to enemy: ", Enemy, " adding intention")
	<-	!getPostion(Enemy,Place);
		!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!moveUnit(Unit).

//##################################################################################################################################################################

//####################################################################--MODE-MADNESS--##########################################################################
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isMadnessMode & jason.getNearestFreeEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to free enemy: ", Enemy, " adding intention")
	<-	!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "enemy");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isMadnessMode & jason.getNearestEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to enemy: ", Enemy, " adding intention")
	<-	!getPostion(Enemy,Place);
		!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!moveUnit(Unit).

+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isMadnessMode & jason.getNearestFreeKnowledge(UnitID,Knowledge) 
	& .print("Unit: ", UnitID, " to knowledge: ", Knowledge, " adding intention")
	<-	!getKnowledgeId(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "knowledge");
		!moveUnit(Unit).
//##################################################################################################################################################################

//===================================================================================================================================================================


//--------------------------------------------------------------------Intention-Checks------------------------------------------------------------------------------
+!checkSurrounding(Unit, Intention) : getTypeOfIntention(Intention, Type) & isSeizeIntention(Type) //TODO base seize
	<- 	.nth(0, Intention, Knowledge); //get id of actual intention
		!getKnowledgeId(Knowledge,TargetObject);
		!getID(Unit, UnitID);
		!addPossibleIntention(UnitID, TargetObject). //check for new temporary intention
		
+!checkSurrounding(Unit, Intention) : getTypeOfIntention(Intention, Type) //here are only units
	<- 	.nth(0, Intention, Enemy);
		!getID(Enemy,TargetObject);
		!getID(Unit, UnitID);
		!addPossibleIntention(UnitID, TargetObject).
		

//--------------------------------------------------------------------With-Intentions--------------------------------------------------------------------------------
+!moveUnit(Unit): getID(Unit, UnitID) & jason.hasIntention(UnitID)
	<-	?mode(Mode);
		jason.getSortedIntentionsByDistance(UnitID, Mode, Intentions); //if mode is domination then most priority in actual round have seize intentions, next attack and then support
		.nth(0, Intentions, Intention);
		! checkSurrounding (Unit, Intention); //new intention can be closer that the older one
		jason.getSortedIntentionsByDistance(UnitID, Mode, NewIntentions);
		.nth(0, NewIntentions, FinalIntention);
		!getTypeOfIntention(FinalIntention, FinalType);
		.print("Unit : ", UnitID, " chooses intention: ", FinalIntention, " from: ", NewIntentions);
		!moveUnit(Unit, FinalIntention, FinalType).

+!moveUnit(Unit, Intention, Type): getID(Unit, UnitID) & isKillingIntention(Type)
	<- 	.nth(0, Intention, Enemy);
		!getPostion(Enemy, Place);
		move(UnitID, Place); //move unit in that direction
		!getID(Enemy,TargetObject);
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


+can_act <- 
	.print("preparing actions"); 
	update_percepts; 
	mark_start; 
	?agentID(N);
	!check_action(N).
		
