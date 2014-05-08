// Agent spectator in project bachelor_wars

/* Initial beliefs and rules */

/* Initial goals */

currentNumPlayersInTeam([])[team(X)]. //for computing next seizer (list of agents)
currentSeizer(Y)[team(X)]. //curent agent id of seizer

/* Plans */

//!start.

+!appendList([], X, [X]).
	
+!appendList([H|B], X, [H|C]) : true
	<- !appendList(B, X, C).

@addAgent [atomic] +!addAgent(Name, Team) : true
	<-	?currentNumPlayersInTeam(ListOfPlayers)[team(Team)];
		!appendList(ListOfPlayers, Name, NewList);
		-+currentNumPlayersInTeam(NewList)[team(Team)];
		.print("Agent: ", Name, " added into team: ", Team, " -> ", NewList).

+!setCurrentSeizer(Player, Team) : true
	<- -+currentSeizer(Player)[team(Team)].

+!getAgentIndex([Element|_], Element, 0).
+!getAgentIndex([_|Tail], Element, Pos) : true
	<- 	!getAgentIndex(Tail, Element, Pos1);
		Pos = Pos1 + 1.
		
+!getNextSeizer(Team, Player) : true
	<- 	?currentNumPlayersInTeam(List)[team(Team)];
		?currentSeizer(ActualSeizer)[team(Team)];
		!getAgentIndex(List, ActualSeizer, Pos);
		NewPos = (Pos + 1) mod .length(List);
		.nth(NewPos, List, Player).
		
		
		
//+!start : true <- !addAgent(test, killers); !addAgent(tester, killers); !addAgent(test123, fuckers); 
//	!setCurrentSeizer(test123, fuckers); ?currentSeizer(X)[team(fuckers)]; .print(X); 
//	!getNextSeizer(fuckers, Player); .print(Player); 
//	!setCurrentSeizer(Player, fuckers); !getNextSeizer(fuckers, Y); .print(Y).
	

	
