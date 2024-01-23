/**I Dont Always use Recursion, but when i do i use Recursion
 * Github: github.com/ExecutableMarley 
 */ 

% Creates a list in descending order: [M, M-1, ..., N+1, N]
init_list(M, N, [M | Ns]) :- M < N, M1 is M + 1, init_list(M1, N, Ns).
init_list(N, N, [N]).

% Generates permutations (Z|Zs) of a given list (Xs)
permutation(Xs, [Z | Zs]) :- select(Z, Xs, Ys), permutation(Ys, Zs).
permutation([], []).

% Terminate when the list is empty
not_angreifen(_, [], _).

% Process the list
not_angreifen(X, [Head|T], Counter) :-
	Head - X =\= Counter,
	X - Head =\= Counter,
	Counter2 is Counter + 1,
	not_angreifen(X, T, Counter2).

% Entry point for not_angreifen
not_angreifen(X, Xs) :- 
    not_angreifen(X, Xs, 1).

% Evaluate to the opposite of no_angreifen
angreifen(X, Xs) :-
    not(not_angreifen(X,Xs)).

angreifen(X, Xs, C) :-
    not(not_angreifen(X,Xs, C)).

% Terminate when the list is empty
sicher([]).
% Process the list
sicher([Q | Qs]) :-
	sicher(Qs),
	not_angreifen(Q, Qs, 1).

%%Generate Solutions, N specifies the size of the board. Qs holds the generated results
damen(N, Qs) :-
    initliste(1, N, List),
    permutation(List, Qs),
    sicher(Qs),
    write(Qs).
