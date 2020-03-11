
cad(P) :- mi(P); cabg(P); pci(P).

included(studyx, P) :- male(P), cad(P), age(P,X), X<40.
included(studyy, P) :- male(P), pci(P), age(P,X), X<40.
included(studyz, P) :- male(P), lvef(P,L), L<50, age(P,X), X<40.

%! clause(included(S,P), (male(P), Q, age(P,X), X<L)).

