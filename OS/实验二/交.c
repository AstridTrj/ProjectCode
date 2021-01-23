#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>

//������ 
int fatal(const char *info)
{
	perror(info);
	exit(1);
}
//�ļ����� 
int lock(int f)
{
	//��λ���ļ���ͷ 
	lseek(f, 0, SEEK_SET);
	//��������ʱ������ʾ 
	if(lockf(f, F_LOCK, 0) == -1)
		fatal("lockf()");
	return 0;
}
//�ļ����� 
int unlock(int f)
{
	lseek(f, 0, SEEK_SET);
	if(lockf(f, F_ULOCK, 0) == -1)
		fatal("unlockf()");
	return 0;
}

int main()
{
	int file, pid;
	char str[50];
	
	//���ļ��򿪴��������ʾ��Ϣ 
	if((file = open("df.txt", O_RDWR|O_CREAT|O_TRUNC, 0744)) < 0)
		fatal("open");
	//�����ӽ��� 
	while((pid = fork()) == -1);
	//���Ӹ����̷ֱ�����ͬ���� 
	switch(pid)
	{
		//����ʱ��ʾ 
		case -1: fatal("Fork error!"); break;
		//�ӽ��̶�ȡ�ļ����� 
		case 0:
			//���ȼ�����ռ����Դ 
			lock(file);
			//��ȡ�ļ����ݣ�����Ϊstr�����е��ֽ��� 
			read(file, str, sizeof(str));
			//�����ļ� 
			unlock(file);
			//�����ȡ������ 
			printf("\nI'm child and I read the file.\nThe content: %s\n", str);
			exit(0);
		//�����̶�ȡ���벢д���ļ� 
		default:
			lock(file);
			printf("I'm parent, input the string: ");
			//�������� 
			scanf("%s", str);
			//д���ļ� 
			write(file, str, sizeof(str));
			unlock(file);
			//�ر��ļ� 
			close(file);
			//�ȴ��ӽ�����ɶ�ȡ 
			wait(NULL);
	}

	return 0;
}
