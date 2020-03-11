lvef(bob, 50).
lvef(alan, 40).
lvef(chris, 60).

age(bob, 34).
age(chris, 53).
age(alan, 29).

mi(alan).
mi(chris).

cabg(bob).
cabg(chris).

pci(chris).

cad(P) :- mi(P); cabg(P); pci(P).


male(alan).
male(bob).

med(chris, enalapril).
med(chris, amiodarone).
med(chris, spironolactone).
med(alan, spironolactone).
med(bob, spironolactone).

condition(chris, mental).
condition(chris, hf).

female(chris).

included(studya, Person) :-
    med(Person, spironolactone),
    condition(Person, hf).

included(studyb, Person) :-
    age(Person, X), X < 40.

included(studyc, Person) :-
    lvef(Person, X), X < 51.

included(studyd, Person) :-
    male(Person).

included(studye, Person) :-
    lvef(Person, 60).

included(studyf, Person) :-
    lvef(Person, 60), male(Person) .

included(studyg, Person):- 
    cad(Person).

included(studyh, Person):- 
    cad(Person),
    male(Person).


included(studyx, P) :- male(P), cad(P), age(P,X), X<40.
included(studyy, P) :- male(P), pci(P), age(P,X), X<40.
included(studyz, P) :- male(P), lvef(P,L), L<50, age(P,X), X<40.

%! clause(included(S, P), lvef(P, 60)).
%! clause(included(S, P), lvef(P, 60)).
%! clause(included(S, P), cad(P)).
%! clause(included(S, P), (cad(P), male(P)).

%! clause(included(S,P), (age(Person, X), x <40))

% it's a smidge too literal. This picks up x and y, but not z!!!
%! clause(included(S,P), (male(P), Q, age(P,X), X<L)).


%! How to find studies that require a particulary med?
