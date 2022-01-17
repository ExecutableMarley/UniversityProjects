%% I Dont Always use Recursion, but when i do i use Recursion

%%Creates a list in the form of: (int)[N,N+1,...,M-1,M]
initliste(M,N,[M|Ns]) :- M < N, M1 is M+1, initliste(M1,N,Ns).
initliste(N,N,[N]).

%%Generates Permuations (Z|Zs) of a give list (Xs)
permutation(Xs,[Z|Zs]) :- select(Z,Xs,Ys), permutation(Ys,Zs).
permutation([],[]).

%%Terminate Empty List
angreifen(_,[],_).

%%Process List
angreifen(X, [Head|T], Counter) :-
    %%abs(Head - X) =\= Counter,
	Head - X =\= Counter,
	X - Head =\= Counter,
	Counter2 is Counter + 1,
	angreifen(X, T, Counter2).

%%angreifen entry point
angreifen(X, Xs) :- 
    angreifen(X, Xs, 1).

%%Terminate Empty List
sicher([]).
%%Process List
sicher([Q|Qs]) :-
	sicher(Qs),
	angreifen(Q,Qs,1).

%%Generate Solutions, N specifies the size of the board. Qs holds the generated results
damen(N, Qs) :-
    initliste(1, N, List),
    permutation(List, Qs),
    sicher(Qs),
    write(Qs).
