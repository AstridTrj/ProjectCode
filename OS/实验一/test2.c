#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/wait.h>
#include<sys/types.h>

int main()
{
	int pid;
	char command[10];
	while(1)
	{
		printf("Input the command: ");
		scanf("%s", command);
		if(strcmp(command, "exit") == 0)
		{
			printf("Exit\n");
			return 0;
		}

		pid = fork();
		switch(pid)
		{
			case -1: printf("Built error."); exit(1);
			case 0: system(command); exit(0); break;
			default: wait(NULL); break;
		}
	}
	return 0; 
}
