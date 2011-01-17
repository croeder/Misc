#include <iostream>
#include <sstream>
#include <string>
using namespace std;

	int main(int argc, char **argv) {
		string t = "34 43 45";
		int left;
		istringstream ss(t);
		ss >> left;
		int left2;
		ss >> left2;
		int left3;
		ss >> left3;

cout << left << endl;
cout << left2 << endl;
cout << left3 << endl;
	}

