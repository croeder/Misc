#include <string>
#include "string.h"
#include <iostream>
using namespace std;

#include "StringSpace.h"

int main(int argc, char **argv) {
	StringSpace ss;
	char buffer[100];

	string tests[7];
	tests[0] = "Ford";
	tests[1] = "Carter";
	tests[2] = "Reagan";
	tests[3] = "Bush";
	tests[4] = "Clinton";
	tests[5] = "Bush";
	tests[6] = "Nixon";

	for (int i=0; i<7; i++) {
		ss.enter(tests[i].c_str());		
		cout << "inserting: " << tests[i] << endl;
		//	ss.dump2();
		cout << endl;
	}

	cout << "----------------------------------" << endl;
	cout << endl;

	for (int i=0; i<7; i++) {
	  //ss.dump();
		int l;
		char *s = ss.get(i, l);
		strncpy(buffer, s,l);
		buffer[l] = '\0';
		cout << buffer << endl;	 
		cout << endl;
	}

	ss.dump2();
	cout << "---- delete back to 3 ---" << endl;
	ss.delete_back_to(3);
	ss.dump2();
}
