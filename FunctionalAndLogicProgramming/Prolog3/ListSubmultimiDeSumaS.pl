% candidat(list, integer)
% candidat(i, o) - nedeterminist

candidat([E|_],E).
candidat([_|T],E):-candidat(T,E).

% solutie_aux(list,integer,list,list,integer)
% solutie_aux(i,i,o,i,i) nedeterminist

solutie_aux(_, N, Rez, Rez, N):-!.
solutie_aux(L, N, Rez, [H | Col], S) :-
candidat(L, E),
E < H,
S1 is S+E,
S1 =< N,
solutie_aux(L, N, Rez, [E | [H | Col]], S1).

% solutie(list,integer,list)
% solutie(i,i,o) nedeterminist
solutie(L, N, Rez) :-
candidat(L, E),
E =< N,
solutie_aux(L, N, Rez, [E], E).


solutieSuma(L, N, LRez) :- findall(Rez, solutie(L, N, Rez), LRez).
