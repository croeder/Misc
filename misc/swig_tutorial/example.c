/* File : example.c */
 
 #include <time.h>
#include <unistd.h>
 double My_variable = 3.0;
 
 int fact(int n) {
	printf("fact\n");
     if (n <= 1) return 1;
     else return n*fact(n-1);
 }
 
 int my_mod(int x, int y) {
	printf("my_mod\n");
     return (x%y);
 }
 	
 char *get_time() {
	printf("get_time\n");
     time_t ltime;
     time(&ltime);
     return ctime(&ltime);
 }
