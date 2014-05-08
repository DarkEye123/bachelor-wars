// Agent medium_ai in project bachelor_wars

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
test(1).

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
	
isAnihilationMode :- 
	mode(N) & 
	anihilation(M) & 
	N == M.
	
isMadnessMode :- 
	mode(N) & 
	madness(M) & 
	N == M.

canAsk :- 
	round(N) & 
	N mod 10 == 1. //there is need some time for given role to play
//	N mod 2 == 1. //there is need some time for given role to play

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
//rrr[source(test)].

//!start.
//+!start : true <- ?rrr; -rrr[source(X)]; .print("ok"); !start.
//+!start :true <- .max([ [1, 2, 3], [5, 6, 7] , [3, 10, 7] ], Y); .print(Y).


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
		.nth(0,Best,Mov);
		if (Mov >= 4) { //there is need for units with higher movement ability
			.nth(1, Best, Unit); //set type
		} else {
			Unit = "none";
		}.

+!createUnitAbilityMov(ID, Units, Unit) : true 
	<- 	!getMostMovableUnit(Units, U);
		if (U \== "none") {
			.print("(mov ability) choosing unit: ", U, "from ", Units);
			create_unit(ID, U);
			?created_unit(Unit); //here is unit id created
			!getID(Unit, UnitID);
			.print("(mov ability) created unit id: ", UnitID);
		} else {
			Unit = U;
			.print("(mov ability) saving for another unit");
		}.
		
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


//-----------------------------------------------------------------Possible-Intentions--------------------------------------------------------------------------		
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
//--------------------------------------------------------------------------SEIZER-ROLE------------------------------------------------------------------------------

+!check_action(ID): enoughSlots & jason.getAffordableUnits(ID, Units) & isSeizerRole
	<- 	!createUnitAbilityMov(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		if (Unit \== "none") {
			!moveUnit(Unit);
			update_percepts;
			!check_action(ID);
		} else {
			update_percepts; 
			mark_done;
		}.
		
//--------------------------------------------------------------------------ATTACKER-ROLE------------------------------------------------------------------------------

+!check_action(ID): enoughSlots & jason.getAffordableUnits(ID, Units) & isAttackerRole
	<- 	!createUnitAbilityAtk(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		 
//--------------------------------------------------------------------------NO-ROLE----------------------------------------------------------------------------------
		

+!check_action(ID): enoughSlots & jason.getAffordableUnits(ID, Units)
	<- 	!createRandomUnit(ID, Units, Unit); //here is unit created from a template and actual created unit is given
		update_percepts;
		!moveUnit(Unit);
		update_percepts;
		!check_action(ID).
		 
-!check_action(ID): true <- update_percepts; mark_done.

//=====================================================================================================================================================================

+!knowledge_distance_evaluation(N): true
	<- 	?agentID(ID);
		jason.getBestDistance(ID, "base", "knowledge", N).
		
+!knowledge_distance_evaluation [source(self)].
+!knowledge_distance_evaluation [source(Ag)] : true
	<- 	?agentID(ID);
		jason.getBestDistance(ID, "base", "knowledge", N);
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

+!checkSeizerRole(KnowledgeDistances) : true
	<-	.my_name(Name);
		?team(Team);
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
			jason.setRole(N, "seizer");
			.send(spectator, achieve, setCurrentSeizer(Name, Team));
			-+role(seizer);
		} else {
			.print("Setting attacker role");
			jason.setRole(N, "attacker");
			-+role(attacker);
		}.

+!agree_with_allies : canAsk & allies(Allies) & Allies \== []
	<- 	?round(Round);
		.my_name(Name);
		?team(Team);
		.print("-------------------------------------------------------------------AGREEMENT--------------------------------------------------");
		if (Round == 1) {
			for (.member(Ally, Allies)) {
				?compatibleAllies(Compatible);
				!askUnderstandSimple(Ally, Answer);
				if (Answer == "yes") {
					!appendList(Compatible, Ally, N);
					-+compatibleAllies(N);
				};
			}; 
			?compatibleAllies(Compatible);
			.print("Compatible: ",Compatible);
			.send(spectator, achieve, addAgent(Name, Team)); //add agent into the team
			if (Compatible \== []) {
				!ask_knowledge_distances(Compatible);
			} else {
				jason.setRole(N, "unknown");
				-+role(unknown);
			};
		} else {
//			if (Round \== 1) { //at the first round, nobody have moving capabilities to count on
//				!ask_moving_capability(Compatible, MovingCapabilities); .print(MovingCapabilities);
//			};
			?compatibleAllies(Compatible);
			if (.length(Compatible) == 1) {
				?role(X);
				if (X == seizer) {
					.print("Setting attacker role from seizer");
					jason.setRole(N, "attacker");
					-+role(attacker);
				} else {
					.print("Setting seizer role from attacker");
					jason.setRole(N, "seizer");
					-+role(seizer);
				}
			} else {
				?role(X);
				if (X == seizer) {
					Pos=math.floor(math.random(.length(Compatible)));
					.nth(Pos, Compatible, NewSeizer);
					.send(NewSeizer, tell, canSwitch);
//					.print("NEW SEIZER SHOULD BE ", NewSeizer);
					.print("Setting attacker role from seizer");
					jason.setRole(N, "attacker");
					-+role(attacker);
				} else {
					?canSwitch;
					-canSwitch[source(X)];
					.print("Setting seizer role from attacker");
					jason.setRole(N, "seizer");
					-+role(seizer);
				}
			}
		}.


//+canSwitch[source(X)] : true
//	<-	.print("canSwitch: Preparing ")

+!agree_with_allies: canSwitch[source(X)] //variant, where actual seizer sets seizer agent, which already take its turn.
	<- 	-canSwitch[source(X)];
		.print("Setting seizer role from attacker");
		jason.setRole(N, "seizer");
		-+role(seizer).
		
+!agree_with_allies: true
	<-	?role(X);
		-+role(X).
		
-!agree_with_allies : canAsk & allies(Allies) & Allies \== []
	<-	.print("Setting attacker role from attacker");
		jason.setRole(N, "attacker");
	 	-+role(attacker).
		
-!agree_with_allies: true //there no role set
	<- 	jason.setRole(N, "unknown");
		+role(unknown).

+!askUnderstandSimple(H, Answer) : true
	<-	.send(H, askOne, understandSimple(Answer), understandSimple(Answer)[source(H)]);
//		understandSimple(Answer)[source(H)] = U;
		.print("Asked agent: ", H, " understands simple communication API (answer): ", Answer).

+can_act <- 
	.print("preparing actions"); 
	update_percepts;
	!agree_with_allies. 
//	mark_start; 
//	?agentID(N);
//	mark_done.
//	!check_action(N).
	
+!appendList([], X, [X]).
	
+!appendList([H|B], X, [H|C]) : true
	<- !appendList(B, X, C).
		
