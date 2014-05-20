// Agent advanced_ai in project bachelor_wars

/* Initial beliefs and rules */

//-----------------------------------------------------------------General knowledge of map---------------------------------------------------
mode(unknown)[source(percept)].
agentID(unknown)[source(percept)].
actualKnowledge(unknown)[source(percept)].
freeSlots(unknown)[source(percept)].
maximumSlots(unknown)[source(percept)].
fightingPower(unknown)[source(percept)].
movingCapability(unknown)[source(percept)].
allies([])[source(percept)].
role. //type can be attacker(enemy diversion till seizer has money for better units), seizer (economy later attack)
//agreementCounter(1)[source(Ag)].
round(unknown)[source(percept)].
understandSimple("yes").
compatibleAllies([]).
knowledgeDistances([]).
movingCapabilities([]).

//-----------------------------------------------------------------------Intentions-----------------------------------------------------------
killIntention(0). //damage with atk
seizeIntention(2).
//----------------------------------------------------------------------Available-Modes-------------------------------------------------------
domination(0).
anihilation(1).
madness(2).

//----------------------------------------------------------------------Available-Roles-------------------------------------------------------
seizer(seizer).
attacker(attacker).
unknown(unknown).

//-----------------------------------------------------------------------Rules----------------------------------------------------------------
enoughSlots :- 
	freeSlots(N)[source(percept)] & 
	N > 0 & 
	.print("free slots: ",N).
	
isWithoutUnits :- 
	freeSlots(N)[source(percept)] & 
	maximumSlots(M)[source(percept)] & 
	N == M.

isSeizerRole :- 
	role(X) &
	seizer(Y) & 
	X == Y.
	
isAttackerRole :- 
	role(X) &
	attacker(Y) & 
	X == Y.
	
isUnknownRole :- 
	role(X) &
	unknown(Y) & 
	X == Y.
	
isKillingIntention(Type) :- 
	killIntention(N) & 
	N == Type.
	
isSeizeIntention(Type) :- 
	seizeIntention(N) & 
	N == Type.

isDominationMode :- 
	mode(N) & 
	domination(M) & 
	N == M.
	
isAnnihilationMode :- 
	mode(N) & 
	anihilation(M) & 
	N == M.
	
isMadnessMode :- 
	mode(N) & 
	madness(M) & 
	N == M.

canAsk :- 
	round(N) & 
	N mod 5 == 1. //there is need some time for given role to play
//	N mod 2 == 1. //there is need some time for given role to play
//	N mod 1 == 0. //there is need some time for given role to play

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

getSeizedObjectID(Knowledge, Stat) :-
	.nth(0,Knowledge,Stat).
	
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
+!getPostion(Unit,Place) : true
	<- .nth(7,Unit,X); .nth(8,Unit,Y); Place=[X,Y].
+!getOwner(Unit,Stat) : true
	<- .nth(9,Unit,Stat).
	
//------------------------------------------------------------------------knowledge-stats------------------------------------------------------------	
+!getKnowledgePosition([H|B], P) : true
	<- P=B.
+!getSeizedObjectID(Knowledge, Stat) : true
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
+!getUnitByMovement([], []). //this chooses a unit with the best moving ability (creation)

+!getUnitByMovement([H|B], [X|C]) : true
	<- 	!drawPositionInList(H, 5, X); 
		!getUnitByMovement(B, C).
		
+!getUnitByAttack([], []). //this chooses a unit with the best attack ability (creation)

+!getUnitByAttack([H|B], [X|C]) : true
	<- 	!drawPositionInList(H, 4, X); 
		!getUnitByAttack(B, C).

+!drawPositionInList(InputList, PositionToDraw, FinalList) : true 
	<- 	!getType(InputList, Type);
		!getID(InputList, ID);
		.nth(PositionToDraw, InputList, Ret);
		FinalList = [Ret, Type, ID].
		
+!drawPositionInList([], _, []).

//-----------------------------------------------------------------------------------------SEIZER-ROLE----------------------------------
+!getMostMovableUnit(Units,Unit) : true
	<-	!getUnitByMovement(Units, ListByMov);
		.max(ListByMov, Best);
		.nth(1, Best, Unit). //set type

+!createUnitAbilityMov(ID, Units, Unit) : true  //here is changed functionality, its drawing from the fastest combination, so chose the fastest as first, to get futher
	<- 	!getMostMovableUnit(Units, U);
		.print("(mov ability) choosing unit: ", U, "from ", Units);
		create_unit(ID, U);
		?created_unit(Unit); //here is unit id created
		!getID(Unit, UnitID);
		.print("(mov ability) created unit id: ", UnitID).
		
//-----------------------------------------------------------------------------------------ATTACKER-ROLE----------------------------------
+!getMostAtkUnit(Units,Unit) : true
	<-	!getUnitByAttack(Units, ListByMov);
		.max(ListByMov, Best);
		.nth(1,Best,Unit).

+!createUnitAbilityAtk(ID, Units, Unit) : true 
	<- 	!getMostAtkUnit(Units, U);
		.print("(atk ability) choosing unit: ", U, "from ", Units);
		create_unit(ID, U);
		?created_unit(Unit); //here is unit id created
		!getID(Unit, UnitID);
		.print("(atk ability) created unit id: ", UnitID).

//-----------------------------------------------------------------------------------------NO-ROLE----------------------------------

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

//------------------------------------------------------------------------------------------BY-STRATEGY----------------------------------



//+!createByMovStrategy(ID, Units, Unit) : round(Round) & Round
//	<-	!getUnitByMovement(Units, ListByMov);
//		.max(ListByMov, Best);
//		.nth(0,Best,Mov);

//-----------------------------------------------------------------Possible-Intentions--------------------------------------------------------------------------		
//####################################################################--MODE-DOMINATION--##########################################################################

+!addPossibleIntention(UnitID, Type) : 	isDominationMode & 
											Type \== "knowledge" & 
											jason.getKnowledgeInReach(UnitID, Knowledge) & //desired action for domination mode - higher priority
											getSeizedObjectID(Knowledge, TargetObject) & 
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

+!addPossibleIntention(UnitID, Type) : 	isAnnihilationMode & 
										Type \== "knowledge" & 
										jason.getKnowledgeInReach(UnitID, Knowledge) & //desired action for domination mode - higher priority
										getSeizedObjectID(Knowledge, TargetObject) & 
										TargetObject \== Type & //check if this intention isn't the same as
										.print("Unit: ", UnitID, " adding possible based intention(knowledge): ", Knowledge)
	<-	jason.addIntention(UnitID, TargetObject, 0, "knowledge").
		
+!addPossibleIntention(UnitID, Type) : 	isAnnihilationMode &
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
											getSeizedObjectID(Knowledge, TargetObject) & 
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
	<-	!getSeizedObjectID(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "knowledge");
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
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isAnnihilationMode & jason.getNearestFreeEnemy(UnitID,Enemy) 
	& .print("Unit: ", UnitID, " to free enemy: ", Enemy, " adding intention")
	<-	!getID(Enemy,TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "enemy");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "enemy");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isAnnihilationMode & jason.getNearestFreeKnowledge(UnitID,Knowledge) 
	& .print("Unit: ", UnitID, " to knowledge: ", Knowledge, " adding intention")
	<-	!getSeizedObjectID(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "knowledge");
		!moveUnit(Unit).
		
		
+!moveUnit(Unit): getID(Unit, UnitID) & not jason.hasIntention(UnitID) & isAnnihilationMode & jason.getNearestEnemy(UnitID,Enemy) 
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
	<-	!getSeizedObjectID(Knowledge, TargetObject);
		jason.addIntention(UnitID, TargetObject, 1, "knowledge");
		!addEnemyBases(UnitID);
		!addPossibleIntention(UnitID, "knowledge");
		!moveUnit(Unit).
//##################################################################################################################################################################

//===================================================================================================================================================================


//--------------------------------------------------------------------Intention-Checks------------------------------------------------------------------------------
+!checkSurrounding(Unit, Intention) : getTypeOfIntention(Intention, Type) & isSeizeIntention(Type) //TODO base seize
	<- 	.nth(0, Intention, Knowledge); //get id of actual intention
		!getSeizedObjectID(Knowledge,TargetObject);
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
		!getSeizedObjectID(Object, TargetObject);
		do_intention_if_possible(UnitID, TargetObject).

//===================================================================================================================================================================

+!listUnits([Unit|Units]): true
	<- 	update_percepts;
		!moveUnit(Unit); 
		!listUnits(Units).
		
+!listUnits([]).

//+!listQuandrantUnits(ID, [Unit|Units]): true
//	<- 	
//		jason.addIntention(UnitID, TargetObject, 0, "enemy");
//		update_percepts;
//		!moveUnit(Unit); 
//		!listUnits(Units).
//		
//+!listUnits(_,[]).
//
//+!check_action(ID): jason.isAmbushed(ID) & jason.getUnitsInQuadrant(ID, Units) //agent found out, that he is probably ambushed
//	<-	!listQuandrantUnits(ID, Units);
//		update_percepts;
//		!check_action(ID).

+!check_action(ID): jason.getUsableUnits(ID, Units)
	<-	!listUnits(Units);
		update_percepts;
		!check_action(ID).
//--------------------------------------------------------------------------SEIZER-ROLE------------------------------------------------------------------------------

+!check_action(ID): enoughSlots & isSeizerRole & jason.getAffordableUnitGroupsByMovStrategy(ID, UnitsGroups) 
	<- 	.nth(0, UnitsGroups, Group);
		.print("Choosing group: (seizer role mode)", UnitsGroups, " from: ", Groups );
		.nth(1, Group, Units);
		!createUnitAbilityMov(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		
//--------------------------------------------------------------------------ATTACKER-ROLE------------------------------------------------------------------------------

+!check_action(ID): enoughSlots & isAttackerRole & jason.getAffordableUnitGroupsByAtkStrategy(ID, UnitsGroups)
	<- 	.nth(0, UnitsGroups, Group);
		.print("Choosing group: (seizer role mode)", UnitsGroups, " from: ", Groups );
		.nth(1, Group, Units);
		!createUnitAbilityAtk(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		 
//--------------------------------------------------------------------------NO-ROLE----------------------------------------------------------------------------------
		

+!check_action(ID): enoughSlots & isDominationMode & jason.getAffordableUnitGroupsByMovStrategy(ID, Groups)
	<- 	
		.nth(0, Groups, Group);
		.print("Choosing group: (no role and domination mode)", Group, " from: ", Groups );
		.nth(1, Group, Units);
		!createUnitAbilityMov(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		
+!check_action(ID): enoughSlots & isAnnihilationMode & jason.getAffordableUnitGroupsByAtkStrategy(ID, Groups)
	<- 	
		.nth(0, Groups, Group);
		.print("Choosing group: (no role and annihilation mode)", Group, " from: ", Groups );
		.nth(1, Group, Units);
		!createUnitAbilityAtk(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		
+!check_action(ID): enoughSlots & isMadnessMode & jason.getAffordableUnits(ID, Units)
	<- 	!createUnitAbilityAtk(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		 
-!check_action(ID): true <- update_percepts; mark_done.

//=====================================================================================================================================================================

+!knowledge_distance_evaluation(N): true
	<- 	?agentID(ID);
		jason.getKnowledgeDistance(ID, N).
		
+!knowledge_distance_evaluation [source(self)].
+!knowledge_distance_evaluation [source(Ag)] : true
	<- 	?agentID(ID);
		jason.getKnowledgeDistance(ID, N);
		.send(Ag, tell, evaluated_knowledge_distance(N)).
//============================================================================Asks====================================================================================
+!ask_moving_capability([H|B]) : true
	<- 	.send(H, askOne, movingCapability(N));
		!ask_moving_capability(B, G).

+!ask_moving_capability(_, []).

+!ask_knowledge_distances([]).

+!ask_knowledge_distances([H|B]) : true
	<- 	.send(H, achieve, knowledge_distance_evaluation);
		!ask_knowledge_distances(B).
		
//========================================================================EVENTS========================================================================================

+movingCapability(_)[source(self)].
+movingCapability(_)[source(percept)].

+movingCapability(X)[source(Ag)] : true
	<-	?movingCapabilities(Val);
		!appendList(Val, [X, Ag], NewVal);
		.print("Moving capability new val: ", X, " ", Ag, " ", NewVal);
		-+movingCapabilities(NewVal);
		.length(NewVal, N);
		?compatibleAllies(Compare);
		.length(Compare, M);
		if (N == M) { // here is possible set who is seizer and who is not
			!checkBestMovingCapability(NewVal, Ret); //TODO find out usability
		}.
	
+evaluated_knowledge_distance(_)[source(self)].

@plan1 [atomic] +evaluated_knowledge_distance(X)[source(Ag)] : true
	<- 	?knowledgeDistances(Val);
		!appendList(Val, [X, Ag], NewVal);
		.print("knowledge distance new val: ", X, " ", Ag, " ", NewVal);
		-+knowledgeDistances(NewVal);
		.length(NewVal, N);
		?compatibleAllies(Compare);
		.length(Compare, M);
		-evaluated_knowledge_distance(X)[source(Ag)];
		if (N == M) { // here is possible set who is seizer and who is not
			!checkSeizerRole(NewVal);
		}.
//=====================================================================Roles==========================================================================================		

+role(seizer) : true
	<-	mark_start; 
		?agentID(N);
		!check_action(N).
		
+role(attacker) : true
	<-	mark_start; 
		?agentID(N);
		!check_action(N).
		
+role(unknown) : true //act as normal agent
	<-	.print("Setting role to UNKNOWN");
		mark_start; 
		?agentID(N);
		!check_action(N).

//====================================================================================================================================================================
+!checkBestMovingCapability(MovingCapabilities, Ret) : true 
	<-	.my_name(Name);
		?movingCapability(X)[source(percept)];
		!appendList(MovingCapabilities, [X, Name], NewVal);
		.max(NewVal, Ret).

//something like first round -> this is used when game starts and there is need for ideal seizer
+!checkSeizerRole(KnowledgeDistances) : true
	<-	.my_name(Name);
		?agentID(ID);
		.print("Seizer role parameters: ",KnowledgeDistances);
		!knowledge_distance_evaluation(ThisDistance);
		!appendList(KnowledgeDistances, [ThisDistance, Name], NewDistances);
		.min(NewDistances, Min);
		.print("evaluated minimum knowledge distance for this agent: ", ThisDistance);
		.print("Actual choosen evaluated minimum knowledge distance: ", Min);
		.nth(1, Min, Seizer);
		-+knowledgeDistances([]);
		if (Seizer == Name) {
			.print("Setting seizer role");
			?canSwitch;
			-+role(seizer);
		} else {
			.send(Seizer, askOne, canSwitch[source(Name)], U); //ask setted seizer agent that he knows, that he was choosen to be first
			.print("Setting attacker role");
			jason.setRole(ID, "attacker");
			-+role(attacker);
		}.
		
+?preparedIfSeizer: 	canAsk & 
					allies(Allies) & Allies \== [] & 
					compatibleAllies(Compatible) & Compatible \== [] & 
					imSeizer(Pos, Mark) & round(ActualMark) & 
					Mark \== ActualMark & not isSeizerRole
	<-
		?agentID(ID);
		jason.setRole(ID, "seizer"); //set role and everything connected with resources within this role
		.print("prepareIfSeizer: Preparing myself into seizer role - only setting internal tag for seizer ").
		
+?preparedIfSeizer.

+!agree_with_allies : canAsk & allies(Allies) & Allies \== [] & round(Round) & Round == 1
	<- 	.my_name(Name);
		.print("-------------------------------------------------------------------AGREEMENT--------------------------------------------------");
		!getCompatible;
		?compatibleAllies(Compatible);
		?agentID(ID);
		.print("Compatible: ",Compatible);
		if (Compatible \== []) {
			!ask_knowledge_distances(Compatible);
		} else {
			jason.setRole(ID, "unknown");
			-+role(unknown);
		}.

//This is actual seizer for this actual agreement - sets his successor	
+!agree_with_allies : 	canAsk & 
						allies(Allies) & Allies \== [] & 
						compatibleAllies(Compatible) & Compatible \== [] & 
						imSeizer(Pos, Mark) & round(ActualMark) & 
						Mark \== ActualMark & not isSeizerRole 
	<-	
		.print("-------------------------------------------------------------------AGREEMENT--------------------------------------------------");
		.my_name(Name);
		?agentID(ID);
		.nth(Pos, Compatible, NewSeizer);
		NewPos = math.floor(math.random(.length(Compatible)));
		.send(NewSeizer, tell, imSeizer(NewPos, ActualMark));
		-imSeizer(Pos, Mark)[source(Ag)];
		-canSwitch[source(Ag)];
		?role(Role);
		.print("Setting seizer role from: ", Role, " with mark: ", Mark, " and actual mark: ", ActualMark);
		jason.setRole(ID, "seizer"); //set role and everything connected with resources within this role
		-+role(seizer).
		
//This is last seizer (turning into attacker) for this actual agreement	
+!agree_with_allies : 	canAsk & 
						allies(Allies) & Allies \== [] & 
						compatibleAllies(Compatible) & Compatible \== [] & 
						imSeizer(Pos, Mark) & round(ActualMark) & 
						Mark \== ActualMark
	<-	
		-imSeizer(Pos, Mark)[source(Ag)];
		-canSwitch[source(Ag)];
		for (.member(Ally,Compatible)) {
			.send(Ally, askOne, preparedIfSeizer, U); //this will wait till seizer is internally set
		};
		.print("-------------------------------------------------------------------AGREEMENT--------------------------------------------------");
		.my_name(Name);
		?role(Role);
		?agentID(ID);
		.print("Setting attacker role from: ", Role);
		jason.setRole(ID, "attacker");
		-+role(attacker).
		
		
+!agree_with_allies : 	canAsk & 
						allies(Allies) & Allies \== [] & 
						compatibleAllies(Compatible) & Compatible \== []
	<-	
		.print("-------------------------------------------------------------------AGREEMENT--------------------------------------------------");
		for (.member(Ally,Compatible)) {
			.send(Ally, askOne, preparedIfSeizer, U); //this will wait till seizer is internally set
		};
		.my_name(Name);
		?role(Role);
		?agentID(ID);
		.print("Setting attacker role from: ", Role);
		jason.setRole(ID, "attacker");
		-+role(attacker).

/*
 * Checks for compatible allies -> alies that can understand common API for cooperation
 */
+!getCompatible : allies(Allies) & Allies \== []
	<- 	.my_name(Name);
		for (.member(Ally, Allies)) {
			?compatibleAllies(Compatible);
			!askUnderstandSimple(Ally, Answer);
			if (Answer == "yes") {
				!appendList(Compatible, Ally, N);
				-+compatibleAllies(N);
			};
		}. 

/*
 * Checks for compatible allies -> alies that can understand common API for cooperation
 * Delete agent, which actually waits for response from this agent, to prevent deadlock, we have to skip asking him
 */		
+!getCompatible(Ag) : allies(Allies) & Allies \== []
	<- 	.my_name(Name);
		for (.member(Ally, Allies)) {
			?compatibleAllies(Compatible);
			if (Ag \== Ally) {
				!askUnderstandSimple(Ally, Answer);
				if (Answer == "yes") {
					!appendList(Compatible, Ally, N);
					-+compatibleAllies(N);
				};
			} else {
				!appendList(Compatible, Ag, N);
				-+compatibleAllies(N);
			}
		}. 

+?canSwitch : compatibleAllies(Compatible) & Compatible \== [] & not canSwitch
	<-	//here it is tricky, because next seizer is tagged here (successor is tagged but he is playing his role when next agreement shows up)
		+canSwitch;
		?agentID(ID);
		jason.setRole(ID, "seizer"); //set role and everything connected with resources within this role
		.my_name(Name);
		?round(Mark);
		.nth(0, Compatible, NewSeizer); //get new seizer
		NewPos = math.floor(math.random(.length(Compatible)));
		.send(NewSeizer, tell, imSeizer(NewPos, Mark)); //mark this seizer, so he cant play this role till new agreement
		.print("canSwitch: Preparing myself into seizer role ").
		
+?canSwitch[source(Ag)] : not canSwitch 
	<-	
		+canSwitch;
		?agentID(ID);
		jason.setRole(ID, "seizer"); //set role and everything connected with resources within this role
		!getCompatible(Ag); 
	 	?compatibleAllies(Compatible);
		.my_name(Name);
		?round(Mark);
		.nth(0, Compatible, NewSeizer); //get new seizer
		NewPos = math.floor(math.random(.length(Compatible)));
		.send(NewSeizer, tell, imSeizer(NewPos, Mark)); //mark this seizer, so he cant play this role till new agreement
		.print("canSwitch: Preparing myself into seizer role ");
		-+compatibleAllies([]). //empty, because this agent hasn't been on the move yet

+?canSwitch.

+!agree_with_allies: allies(Allies) & Allies == [] & not isUnknownRole
	<- 
		?agentID(ID);
		jason.setRole(ID, "unknown");
		+role(unknown).
		
+!agree_with_allies: true
	<-	?role(X);
		-+role(X).
		
-!agree_with_allies: true //there no role set
	<- 	?agentID(ID);
		jason.setRole(ID, "unknown");
		+role(unknown).

+!askUnderstandSimple(H, Answer) : true
	<-	.send(H, askOne, understandSimple(Answer), understandSimple(Answer)[source(H)]);
//		understandSimple(Answer)[source(H)] = U;
		.print("Asked agent: ", H, " understands simple communication API (answer): ", Answer).

+can_act <- 
	.print("preparing actions"); 
	update_percepts;
	!agree_with_allies. 
	
+!appendList([], X, [X]).
	
+!appendList([H|B], X, [H|C]) : true
	<- !appendList(B, X, C).
		
