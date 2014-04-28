#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#define MAXPENDING 5    /* Max connection requests */
#define BUFFSIZE  256


void Die(char *mess) { perror(mess); exit(1); }

char * readFile(char *path) {

	FILE *fr;            /* declare the file pointer */
 	int n;
 	long elapsed_seconds;
	char *file = malloc(10 * 256 * sizeof(char));
	if (file == NULL) { printf("malloc error\n"); exit(1); }
	*file='\0';
	char *line = malloc(256 * sizeof(char));
	if (line == NULL) { printf("malloc error\n"); exit(1); }
	*line='\0';
	//fr = fopen (path, "r");  /* open the file for reading */
	fr = fopen ("htdocs/foo/bar.html", "r");  /* open the file for reading */
	if (fr == NULL) { printf("file open error\n"); exit(1); }
	char *retval=NULL;
	while((retval = (char *) fgets(line, 255, fr)) != NULL) {
		if (retval) {
			strcat(file, line);
		}
	}
   	fclose(fr);  /* close the file prior to exiting the routine */
	return file;
} 

void handleGet(int sock, char *buffer) ;
void HandleClient(int sock) {
	char buffer[BUFFSIZE];
    int received = -1;

    /* Receive message */
    while ((received = recv(sock, buffer, BUFFSIZE, 0)) >=0) {

	/* Parse request */
    if (received > 0) {
		/* "GET <url>" */
		/* Accept-Language: en */
		/* emtpy line  */
		/* message */

		if (strstr(buffer, "GET")) {
			handleGet(sock, buffer);
		}
	}
	}
}

void handleGet(int sock, char *buffer) {
	printf("handling GET:\n");
	char *s = strtok(buffer, " \n");
	char *u = strtok(NULL, " \n");
	char *path = malloc(270 * sizeof(char));
	*path='\0';
	strcat(path, "htdocs");
	strcat(path, u);
	printf("handling path:%s\n", path);
	char *contents;
	contents = readFile(u);
	char *responseString = malloc (256 * 14 * sizeof (char));
	*responseString = '\0';
	/* put a header on the content */
	strcat(responseString, "HTTP/1.1 200 OK\n");
	strcat(responseString, "Content-Type: text/html\n");
	strcat(responseString, "\n");
	strcat(responseString, contents);
	printf("sending response:\"%s\"\n", responseString);
	int sent=-1;
    if (send(sock, responseString, sent, 0) != strlen(responseString)) {
       	Die("Failed to send bytes to client");
    }
	else {
		printf("response sent successfully");
	}
}

int main(int argc, char *argv[]) {
	int serversock, clientsock;
    struct sockaddr_in echoserver, echoclient;

    if (argc != 2) {
    	fprintf(stderr, "USAGE: echoserver <port>\n");
        exit(1);
    }
    /* Create the TCP socket */
    if ((serversock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
    	Die("Failed to create socket");
    }
    /* Construct the server sockaddr_in structure */
    memset(&echoserver, 0, sizeof(echoserver));       /* Clear struct */
    echoserver.sin_family = AF_INET;                  /* Internet/IP */
    echoserver.sin_addr.s_addr = htonl(INADDR_ANY);   /* Incoming addr */
    echoserver.sin_port = htons(atoi(argv[1]));       /* server port */

    /* Bind the server socket */
    if (bind(serversock, (struct sockaddr *) &echoserver,
    	sizeof(echoserver)) < 0) {
        Die("Failed to bind the server socket");
    }
    /* Listen on the server socket */
    if (listen(serversock, MAXPENDING) < 0) {
    	Die("Failed to listen on server socket");
    }

  	/* Run until cancelled */
    while (1) {
    	unsigned int clientlen = sizeof(echoclient);
        /* Wait for client connection */
        if ((clientsock =
        	accept(serversock, (struct sockaddr *) &echoclient,
                          &clientlen)) < 0) {
            Die("Failed to accept client connection");
        }
        fprintf(stdout, "Client connected: %s\n",
                              inet_ntoa(echoclient.sin_addr));
        HandleClient(clientsock);
    }
}
          
