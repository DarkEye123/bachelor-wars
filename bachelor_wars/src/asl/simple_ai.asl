// Agent simple_ai in project bachelor_wars

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- !can_act(True).

+!can_act(p): can_act(p) <- .print("Can create"); !create_unit.
+!can_act(p): not can_act(p) <- !can_act(p).

+!create_unit: true <- .print("creating"); create_unit(random,random).
