%1.b

%adauga_unu(L:list, Rez:list)
%adauga_unu(i, o) - determinist
adauga_unu([],[]).
adauga_unu([H|T], [H, 1 | Rez]) :- H mod 2 =:= 0,
                    !,
		    adauga_unu(T, Rez).
adauga_unu([H|T], [H | Rez]) :- adauga_unu(T, Rez).


%1.a

%apare(L:list, E:element)
%apare(i,i) - determinist
apare([], _) :- 0 =:= 1.
apare([H|_], E) :- H =:= E,
		    !.
apare([_|T], E) :- apare(T, E).

%diferenta(P:list, L:list, Rez:list)
%diferenta(i,i,o) - determinist
diferenta([], _, []).
diferenta([H|T], L, Rez) :- apare(L, H),
	                !,
			diferenta(T, L, Rez).
diferenta([H|T], L, [H | Rez]) :- diferenta(T, L, Rez).



