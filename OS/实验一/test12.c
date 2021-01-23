#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>

int main()
{
	int pid;
	printf("Before fork...\n");

	system("ps");
	pid = fork();
	if(pid == -1)
		printf("Built process error.\n");
	else if(pid == 0)
	{
		printf("I'm child1,pid=%d,ppid=%d.\n", getpid(), getppid());
		system("ps");
		exit(0);
	}
	else
		printf("I'm parent,pid=%d,ppid=%d.\n", getpid(), getppid());

	sleep(5);
	
	system("ps");	
	pid = fork();
	if(pid == -1)
		printf("Built process error.\n");
	else if(pid == 0)
	{
		printf("I'm child2,pid=%d,ppid=%d.\n", getpid(), getppid());
		system("ps");
		exit(0);
}
	else
		printf("I'm parent,pid=%d,ppid=%d.\n", getpid(), getppid());
	sleep(5);
	return 0; 
}
