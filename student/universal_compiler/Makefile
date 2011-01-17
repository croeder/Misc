test_StringSpace: test_StringSpace.o StringSpace.o
	g++  -g -o test_StringSpace StringSpace.o test_StringSpace.o


all: parser analyzer scanner

parser: grammarAnalyzer.h grammarAnalyzer.o test_parser.o scanner.o parser.o functions.o StringSpace.o SymbolTable.o
	g++ -o parser grammarAnalyzer.o test_parser.o scanner.o parser.o functions.o StringSpace.o SymbolTable.o

analyzer: grammarAnalyzer.h grammarAnalyzer.o test_analyzer.o scanner.o parser.o functions.o
	g++ -o analyzer grammarAnalyzer.o test_analyzer.o scanner.o parser.o functions.o

scanner:  test_scanner.o scanner.o 
	g++ -o scanner test_scanner.o scanner.o 

grammarAnalyzer.o: grammarAnalyzer.cpp grammarAnalyzer.h
	g++ -g -c grammarAnalyzer.cpp

test_analyzer.o: test_analyzer.cpp
	g++ -g -c test_analyzer.cpp

test_parser.o: test_parser.cpp
	g++ -g -c test_parser.cpp

test_scanner.o: test_scanner.cpp
	g++ -g -c test_scanner.cpp

scanner.o: scanner.cpp scanner.h transitionTable.h
	g++ -g -c scanner.cpp

parser.o: parser.cpp eop.h semantic_record.h
	g++ -g -c parser.cpp

functions.o: functions.cpp functions.h
	g++ -g -c functions.cpp


test_StringSpace.o: test_StringSpace.cpp StringSpace.h
	g++ -g -c test_StringSpace.cpp

StringSpace.o: StringSpace.cpp StringSpace.h
	g++ -g -c StringSpace.cpp


SymbolTable.o: SymbolTable.cpp SymbolTable.h SymbolAttributes.h
	g++ -g -c SymbolTable.cpp

test_SymbolTable.o: test_SymbolTable.cpp SymbolTable.h SymbolAttributes.h
	g++ -g -c test_SymbolTable.cpp

test_SymbolTable: test_SymbolTable.o SymbolTable.o StringSpace.o
	g++ -g -o test_SymbolTable test_SymbolTable.o SymbolTable.o StringSpace.o


test_SymbolAttributes: test_SymbolAttributes.cpp SymbolAttributes.h StringSpace.o
	g++ -g -o test_SymbolAttributes test_SymbolAttributes.cpp StringSpace.o
