#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>


int lock(int f)
{
	if(lockf(f, F_LOCK, 0) == -1)
	{
		perror("Lock file error.");
		exit(1);
	}
	return 0;
}

int unlock(int f)
{
	if(lockf(f, F_ULOCK, 0) == -1)
	{
		perror("Unlock file error.");
		exit(1);
	}
	return 0;
}

int main()
{
	int file;
	int p1, p2, i;
	char str[15];

	system("cat df.txt");
	if((file = open("df.txt", O_RDWR|O_APPEND)) < 0)
	{
		perror("Can not open the file.");
		exit(1);
	}
	while((p1 = fork()) == -1);
	if(p1 == 0)
	{
		lock(file);
		sprintf(str, "child1\n");
		for(i = 0; i < 3; i++)
			write(file, str, strlen(str));
		sleep(1);
		unlock(file);
		exit(0);
	}
	else
	{
		while((p2 = fork()) == -1);
		if(p2 == 0)
		{
			lock(file);
			sprintf(str, "child2\n");
			for(i = 0; i < 3; i++)
				write(file, str, strlen(str));
			sleep(1);
			unlock(file);
			exit(0);
		}
		else
		{
			wait(NULL);
			wait(NULL);	
			lock(file);
			sprintf(str, "parent\n");
			for(i = 0; i < 3; i++)
				write(file, str, strlen(str));
			unlock(file);
		}
	}
	close(file);
	system("cat df.txt");
	return 0;
}
