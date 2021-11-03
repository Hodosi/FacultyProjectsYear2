%1.)

%lungimeSecventaPara(L:list, Ln:number).
%lungimeSecventaPara(i, o) - determinist

lungimeSecventaPara([], 0).
lungimeSecventaPara([H|T], Ln) :- H mod 2 =:= 0,
				     !,
				     lungimeSecventaPara(T, LnSec),
                                     Ln is LnSec + 1.
lungimeSecventaPara([H|_], 0) :- H mod 2 =:= 1.

%lungimiSecventePare(L:list, R:list)
%lungimiSecventePare(i, o) - determinist

lungimiSecventePare([], []).
lungimiSecventePare([H|T],[LSec|LRest]) :- lungimeSecventaPara([H|T], LSec),
				 lungimiSecventePare(T, LRest).

%maxim(L:list, M:number)
%maxim(i, o) - determinist

maxim([A], A).
maxim([H|T], M) :- maxim(T, M),
                   H < M, !.
maxim([H|_], H).

%lungimeMaximaSecventaPara(L:list, Ln:number)
%lungimeMaximaSecventaPara(i, o) - determinist

lungimeMaximaSecventaPara([], 0).
lungimeMaximaSecventaPara(L, Ln) :- lungimiSecventePare(L, LRez),
				    maxim(LRez, Ln).


%secventaPara(L:list, S:list, Ln:nunmber)
%secventaPara(i, o, o) - determinist

secventaPara([],[], 0) :- 1=:=1.

secventaPara([H|_], [], 0) :- H mod 2 =:= 1,
	                            !.

secventaPara([H|T], [H|Sec], Ln) :- H mod 2 =:= 0,
				     !,
				     secventaPara(T, Sec, LnSec),
                                     Ln is LnSec + 1.


%secventaMaxima(L:list, S:list, Ln:number)
%secventaMaxima(i, o, i) - determinist

secventaMaxima([], _, _) :- 1=:=1.

secventaMaxima([H|T], LRez, Ln) :- secventaPara([H|T], LRez, LnSec),
	                           LnSec =:= Ln,
				   !.

secventaMaxima([_|T], LRez, Ln) :- secventaMaxima(T, LRez, Ln).

%secventaMaximaMain(L:list, S:list)
%secventaMaximaMain(i,o) - determinist

secventaMaximaMain([], []).
secventaMaximaMain(L, LRez) :- lungimeMaximaSecventaPara(L, Ln),
			       secventaMaxima(L, LRez, Ln).

%inlocuire(L:list, S:list)
%inlocuire(i, o) - determinist

inlocuire([], []).
inlocuire([H|T],[H | LRez]):-number(H),
			!,
			inlocuire(T, LRez).
inlocuire([H|T],[Sec | LRez]) :- is_list(H),
	                         !,
				 secventaMaximaMain(H, Sec),
				 inlocuire(T, LRez).





