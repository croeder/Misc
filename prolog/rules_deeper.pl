% Take 'clause' deeper and implement a kind of 'in list' to get away from the 
% literal order of the rule structure and still match

cad(P) :- mi(P); cabg(P); pci(P).

included(studya, Person) :-
    med(Person, spironolactone),
    condition(Person, hf).

included(studyx, P) :- male(P), cad(P), age(P,X), X<40.
included(studyy, P) :- male(P), pci(P), age(P,X), X<40.
included(studyz, P) :- male(P), lvef(P,L), L<50, age(P,X), X<40.

% it's a smidge too literal. This picks up x and y, but not z!!!
%! clause(included(S,P), (male(P), Q, age(P,X), X<L)).


# GOOGLE for clause_tree for a page that discusses a recursive approach.
# ALSO, there are functional functions that let you fold or map over the list of predicates on the right.
# "higher order" predicates, they call them: predicates that work on predicates.
# http://www.swi-prolog.org/pldoc/man?predicate=foldl/4


