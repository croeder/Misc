

cad(P) :- mi(P); cabg(P); pci(P).

included(studya, Person) :-
    med(Person, spironolactone),
    condition(Person, hf).

included(studyx, P) :- male(P), cad(P), age(P,X), X<40.
included(studyy, P) :- male(P), pci(P), age(P,X), X<40.
included(studyz, P) :- male(P), lvef(P,L), L<50, age(P,X), X<40.

% it's a smidge too literal. This picks up x and y, but not z!!!
%! clause(included(S,P), (male(P), Q, age(P,X), X<L)).

%! no way it's doing meta rule reasoning like this:
clause(included(S,P), pci(P))
% Wouldn't it be cool if it could see that the rules that accept people with CAD means it accepts people who have what it takes to have CAD!
% Need to do 2 things: 1 really look at what's its reasoning over here in order
% to see why 2, you need to write a system that works in the domain of rules
% rather than people where you have to meta the rules....My hunch is that it isn't quite enough

% When you look to see if a person will be in a study....
% it's actually applying predicates to variables whose values are persons.
% There's a database of predict truths about those people that it can filter to tell you if the included() predicate came up true.
% It actually evaluates the logic at the at level.

% The meta work here is working on the level of the symbols that make up the rules, and the logic about cad and pci is lost in dumb letters.
% clause(included(S,P),  blah, blah blah) is just looking for a rule with the included() head as written that 

%
% YOU'D THINK, but...
% ?- clause(included(S,P), (male(P), Q, age(P,X), X<L)).
% S = studyx,
% Q = cad(P),
% L = 40 .
% SEE THERE? it's actually evaluating things and telling you that L=40!!!

% I guess the reason it stops at saing CAD has to be true, is that's all the question you've posed. There could be a rule that says CAD(P) is true in some other unique case..
% FURTHER, the values it fills are from the rule as stated. It's not about the people instances, so it is about the rules....
