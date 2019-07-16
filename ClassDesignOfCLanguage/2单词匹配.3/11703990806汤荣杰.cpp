#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <conio.h>
#include <windows.h>


//�������� 
struct words
{
	char perWord[255];
	int position;
	int perLength;
	struct words *next;
};



//���뵥�ʵ���ͷ���ĵ�������
struct words * insertNode(struct words *head, struct words *node)
{
	static int i=1;
	struct words *p=head;
	while(p->next!=NULL)
	    p=p->next;
	p->next=node;
	node->next=NULL;
	node->position=i++;
	node->perLength = strlen(node->perWord);
	return head;
}

//���ļ���ȡ���ʵ�����
struct words *fileRead()
{
	FILE *fp=NULL;
	struct words *p=NULL;
	struct words *head=(struct words *)malloc(sizeof(struct words));
	memset(head,0,sizeof(struct words));
	
	if( (fp = fopen("EnglishDictionary.txt" , "r") ) == NULL)
	{
		printf("Can't open the file");
		exit(0);
	}
	while( !feof(fp) )
	{
		p=(struct words *)malloc(sizeof(struct words));
		if(p==NULL)
		{
			printf("�ڴ治�㣬�������ڴ�����³��ԡ�");
			exit(0);
		}
		memset(p,0,sizeof(struct words));
		fgets(p->perWord,sizeof(p->perWord),fp);
		p->perWord[strlen(p->perWord)-1]='\0';
		head=insertNode(head,p);
	}
	fclose(fp);
	return head;
}

//����Ŀ�굥��
int findThisWord(struct words *head, char str[255])
{
	struct words *p=head->next;
	int flag=0;
	
	while(p!=NULL)
	{
		if( strcmp(p->perWord, str) == 0)
		{
			printf("1.����%s��λ��Ϊ%d.\n",p->perWord,p->position);
			flag=2;
		}	
		p=p->next;
		if(flag == 2)
	       return 1;
	    flag=1;
	}
	if(flag == 1)
		printf("1.�ʵ���û������ĵ���.\n");
	return 0;
	    
}
//Ѱ��������ص��� 
void findOtherWord(struct words *head, char str[255])
{
	struct words *p1=head->next;
	struct words *p2=head->next;
	struct words *p3=head->next;
	char *s1=NULL, *s2=NULL;
	char *finds[100];
	int n=0,a=0,b=0,c=0,pos;
	
	while(p1!=NULL)
	{
		n=0;
		if( (p1->perLength) == (signed)(strlen(str)) )
		{
			s2=str;
			s1=p1->perWord;
			for( ; *s2!='\0'; s2++, s1++ )
			   if( *s2 == *s1 )
			       n++;
		}
		if( n == (signed)(strlen(str)-1) )
		{
			if(a == 0)
				pos = p1->position;
			finds[a]=p1->perWord;
			a++;
		}
		p1=p1->next;
	}
	if(a == 0)
	    printf("2.û���ҵ�������ĵ���ֻ��һ���ַ���ƥ��ĵ���\n");
	else    if(a == 1)
			{
				printf("2.(1).�ֵ��к�����ĵ���ֻ��һ���ַ���ƥ��ĵ�����%s", finds[0]);
				printf("\n  (2).������ʵ�λ����%d\n", pos);
			}
			else
			{
				printf("2.(1).�ֵ��к�����ĵ���ֻ��һ���ַ���ƥ��ĵ�����:");
				for(n=0; n<a; n++)
					printf("%s ", finds[n]);
				printf("\n  (2).�ҵ��ĵ�һ�����ʵ�λ����%d\n", pos);
			}
	
	while(p2 != NULL)
	{
		if( (p2->perLength == (signed)strlen(str)-1) && (strncmp(p2->perWord, str, strlen(str)-1) == 0) )
		{
			printf("3.������ĵ�����һ���ַ��ĵ���(�����ַ���ƥ��)��%s����λ����%d\n", p2->perWord, p2->position);
			break; 
		}
		b++; 
		p2=p2->next; 
	}
	if(b == 100)
	    printf("3.û���ҵ�������ĵ�����һ���ַ�(�����ַ���ƥ��)�ĵ���\n");
	
	while(p3 != NULL)
	{
		if( (strncmp(p3->perWord, str, strlen(str)) == 0) && (p3->perLength == (signed)strlen(str)+1) )
		{
			if(c == 0)
				pos = p3->position;
			finds[c]=p3->perWord;
			c++;
		}
		p3=p3->next;
	}
	if(c == 0)
	    printf("4.û���ҵ�������ĵ��ʶ�һ���ַ�(�����ַ���ƥ��)�ĵ���\n");
	else    if(c == 1)
			{
				printf("4.(1).�ֵ��б�����ĵ��ʶ�һ���ַ�(�����ַ���ƥ��)�ĵ�����: %s", finds[0]);
				printf("\n  (2).������ʵ�λ����%d\nw", pos);
			}
	            
	        else
			{
				printf("4.(1).�ֵ��б�����ĵ��ʶ�һ���ַ�(�����ַ���ƥ��)�ĵ�����: ");
				for(n=0; n<c; n++)
					printf("%s ", finds[n]);
				printf("\n  (2).�ҵ��ĵ�һ�����ʵ�λ����%d\n", pos);
			}
}

//�ͷ��ڴ�
void discharge(struct words *head)
{
	struct words *p=head;
	struct words *temp=NULL;
	while(p!=NULL)
	{
		temp=p->next;
		free(p);
		p=temp;
	}
}


int main()
{
	struct words *head=NULL;
	int n;
	char str[255];
	char flag;
	
	head=fileRead();
	while(1)
	{
	    system("cls");
		printf("������Ҫ���ҵĵ���: ");
		gets(str);
		n=findThisWord(head, str);
		if(n == 1)
			findOtherWord(head, str);
		Sleep(700);
		printf("\n�Ƿ�������ң�(�����������'n'����)\n");
		flag=getch();
		if(flag == 'n')
		    break;

	}
	discharge(head);
	system("cls");
	printf("\n\t�˳��ɹ���\n"); 
	return 0;
}














