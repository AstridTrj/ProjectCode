#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<conio.h>
#include<windows.h>
#include"common.h"

//���ù��λ��
void SetPos(int x,int y)
{
    COORD pos;
	HANDLE handle;
    pos.X=x;
    pos.Y=y;
    handle=GetStdHandle(STD_OUTPUT_HANDLE);
    SetConsoleCursorPosition(handle,pos);
}
//��ȡ�ļ������ݸ���
void readStudentsFileRow()
{
	FILE *fp;
	char c;
	if( (fp = fopen("allStudents.txt", "r")) == NULL )
	{
		printf("����ѧ����Ϣ.");
		exit(0);
	}
	while(!feof(fp))
	{
		c = fgetc(fp);
		if(c == '\n')
			nn++;
	}
}
//��ȡ�ļ������ݸ���
void readDormFileRow()
{
	FILE *fp;
	char c;
	if( (fp = fopen("dorm.txt", "r")) == NULL )
	{
		printf("����ѧ����Ϣ.");
		exit(0);
	}
	while(!feof(fp))
	{
		c = fgetc(fp);
		if(c == '\n')
			boyN++;
	}
}
//ע���û� 
void forRegister()
{
	FILE *fp;
	int i = 0;
	char name[16], paw[16];
	char ch;
	for(i = 0; i < 19; i++)
	{
		printf("\t\t");
		puts(logMenu[i]);
	}
	if( (fp = fopen("user.txt", "a+")) == NULL ){
		printf("Can not open the file");
		exit(0);
	}
	SetPos(30, 9);
	gets(name);
	SetPos(30, 10);
	i = 0;
	while((ch = getch()) != '\r')
	{
		if(ch == 27)
			return ; 
		if(ch == '\b')
			printf("\b \b");
		else
		{
			paw[i++] = ch;
			printf("*");
		}
	}
	paw[i] = '\0';
	fprintf(fp, "\n%s %s", name, paw);
}
//�޸����� 
int changeCode()
{
	FILE *fp;
	int i, j;
	char s[16], name[16], password[16];
	char ch;
	for(i = 0; i < 19; i++)
	{
		printf("\t\t");
		puts(changeCodeMenu[i]);
	}
	if( (fp = fopen("user.txt", "r+")) == NULL){
		printf("Can not open the file");
		exit(0);
	}
	SetPos(30, 8);
	fflush(stdin);
	gets(s);
	i = 0;
	while(!feof(fp))
	{
		fscanf(fp, "%s", name);
		if(strcmp(s, name) == 0)
		{
			fscanf(fp, "%s", password);
			SetPos(30, 9);
			while(1)
			{
				i = 0;
				fflush(stdin);
				while((ch = getch()) != '\r')
				{
					if(ch == '\b'){
						i--;
						printf("\b \b");
					}
					else
					{
						s[i++] = ch;
						printf("*");
					}
					if(i > 14)
					{
						SetPos(22, 12);
						printf("���볬�ޣ�������14λ��������"); 
						SetPos(30, 9);
						printf("               ");
						SetPos(30, 9);
						break; 
					}
				}
				s[i] = '\0';
				j = i;
				if(strcmp(s, password) == 0)
				{
					SetPos(22, 12);
					printf("                            ");
					i = 0;
					SetPos(30, 10);
					while((ch = getch()) != '\r')
					{
						if(ch == '\b')
							printf("\b \b");
						else
						{
							password[i++] = ch;
							printf("*");
						}
						if(i > 14)
						{
							SetPos(22, 12);
							printf("���볬�ޣ�������14λ��������"); 
							SetPos(30, 10);
							printf("               ");
							SetPos(30, 10);
							i = 0;
						}
					}
					password[i] = '\0';
					fseek(fp, -j-1, SEEK_CUR);
					if(j > i)
						fprintf(fp, "              ");
					fseek(fp, -14, SEEK_CUR);
					fprintf(fp, " %s", password);
					return 0;
				}
				else
				if(i < 15)
				{
					SetPos(22, 12);
					printf("                            ");
					SetPos(22, 12);
					printf("�������!����������");
					SetPos(30, 9);
					printf("               ");
					SetPos(30, 9);
				}
			}
		}
	}
	SetPos(22, 12);
	printf("�û��������������������:");
	SetPos(30, 10);
	printf("               ");
	SetPos(30, 10);
	Sleep(1000);
	fclose(fp);
	return 1;
}
//�û���¼ 
int logIn()
{
	FILE *fp;
	int i = 0;
	char ch;
	char s[16], name[16], password[16];
	for(i = 0; i < 19; i++)
	{
		printf("\t\t");
		puts(logMenu[i]);
	}
	if( (fp = fopen("user.txt", "r")) == NULL ){
		printf("Can not open the file");
		exit(0);
	}
	SetPos(30, 9);
	gets(s);
	while(!feof(fp))
	{
		fscanf(fp, "%s", name);
		if(strcmp(s, name) == 0)
		{
			fscanf(fp, "%s", password);
			SetPos(30, 10);
			while(1)
			{
				i = 0;
				fflush(stdin);
				while((ch = getch()) != '\r')
				{
					if(ch == '\b'){
						i--;
						printf("\b \b");
					}
					else
					{
						s[i++] = ch;
						printf("*");
					}
					if(i > 14)
					{
						SetPos(22, 12);
						printf("���볬�ޣ�������14λ��������"); 
						SetPos(30, 10);
						printf("               ");
						SetPos(30, 10);
						break; 
					}
				}
				s[i] = '\0';
				if(strcmp(s, password) == 0)
			    	return 1;
				else
				if(i < 15){
					SetPos(22, 12);
					printf("                            ");
					SetPos(22, 12);
					printf("�������!����������");
					SetPos(30, 10);
					printf("               ");
					SetPos(30, 10);
				}
			}		
		}
	}
	SetPos(22, 12);
	printf("�û��������������������:");
	SetPos(30, 10);
	printf("               ");
	SetPos(30, 10);
	Sleep(1000); 
	fclose(fp);
	return 0;
}
//����ڵ㵽����(����)
void insertDormNode(struct dorm *node, struct dorm *dormHead)
{
	struct dorm *p = dormHead;
	while(p->next != NULL)
		p = p->next;
	p->next = node;
	node->next = NULL;
}
//����ڵ㵽����(ѧ��)
void insertStudentNode(struct student *node)
{
	struct student *p = studentHead;
	while(p->next != NULL)
		p = p->next;
	p->next = node;
	node->next = NULL;
}
//��ȡ������Ϣ
void readDormInformation()
{
	FILE *fp;
	int i;
	struct dorm *p = NULL;
	dormHead1 = (struct dorm *)malloc(sizeof(struct dorm));
	dormHead1->next = NULL;
	
	if( (fp = fopen("dorm.txt", "r")) == NULL )
	{
		printf("����������Ϣ,�ȴ�ѧ����ס.");
		exit(0);
	}
	for(i = 0; i < boyN; i++)
	{
		p = (struct dorm *)malloc(sizeof(struct dorm));
		memset(p, 0, sizeof(struct dorm));
		fscanf(fp, "%d", &p->building);
		fscanf(fp, "%d", &p->floorNum);
		fscanf(fp, "%d", &p->houseNum);
		fscanf(fp, "%d", &p->haveNum);
		fscanf(fp, "%d", &p->emptyBed);
		fscanf(fp, "%s", p->dormSex);
		p->dormSex[strlen(p->dormSex)] = '\0';
		insertDormNode(p, dormHead1);
	}
	fclose(fp);
}
//��ȡѧ����Ϣ
void readStudentInformation()
{
	FILE *fp;
	int i;
	struct student *p = NULL;
	studentHead = (struct student *)malloc(sizeof(struct student));
	studentHead->next = NULL;
	if( (fp = fopen("allStudents.txt", "r")) == NULL ){
		printf("����ѧ����Ϣ.");
		exit(0);
	}
	for(i = 0; i < nn; i++)
	{
		p = (struct student *)malloc(sizeof(struct student));
		memset(p,0,sizeof(struct student));
		fscanf(fp, "%d", &p->building);
		fscanf(fp, "%d", &p->floorNum);
		fscanf(fp, "%d", &p->houseNum);
		fscanf(fp, "%s", p->name);
		p->name[strlen(p->name)] = '\0';
		fscanf(fp, "%s", p->sex);
		p->sex[strlen(p->sex)] = '\0';
		fscanf(fp, "%s\n", p->number);
		p->number[strlen(p->number)] = '\0';
		fscanf(fp, "%d", &p->age);
		fscanf(fp, "%s", p->major);
		p->major[strlen(p->major)] = '\0';
		fscanf(fp, "%s", p->times);
		p->times[strlen(p->times)] = '\0';
		insertStudentNode(p);
	}
	fclose(fp);
}
//��ס���� 
void addDormInformation()
{
	struct dorm *p = NULL;
	struct student *aStu = (struct student *)malloc(sizeof(struct student));
	aStu->next = NULL;
	system("cls"); 
	printf("-------------------------------------------------\n");
	printf("          ѧ���������ϵͳ--��ס����             \n");
	printf("-------------------------------------------------\n");
	fflush(stdin);
	printf("\t������ѧ��������");
	gets(aStu->name);
	aStu->name[strlen(aStu->name)] = '\0';
	fflush(stdin);
	printf("\t������ѧ���Ա�");
	gets(aStu->sex);
	aStu->sex[strlen(aStu->sex)] = '\0';
	fflush(stdin);
	printf("\t������ѧ�����䣺");
	scanf("%d", &aStu->age);
	fflush(stdin);
	printf("\t������ѧ��ѧ�ţ�");
	gets(aStu->number);
	aStu->number[strlen(aStu->number)] = '\0';
	fflush(stdin);
	printf("\t������ѧ��רҵ��");
	gets(aStu->major);
	aStu->major[strlen(aStu->major)] = '\0';
	fflush(stdin);
	printf("\t������ѧ����סʱ�䣺");
	gets(aStu->times);
	aStu->times[strlen(aStu->times)] = '\0';
	p = dormHead1->next;
	for(; p != NULL; p = p->next)
	{
		if(strcmp(aStu->sex, p->dormSex) == 0) 
			if(p->haveNum < 4)
			{
				aStu->building = p->building;
				aStu->floorNum = p->floorNum;
				aStu->houseNum = p->houseNum;
				p->haveNum++;
				p->emptyBed--;
				break;
			}
	}
	insertStudentNode(aStu);
	writeStudentToFile();
	writeDormToFile();
}
//�����������ʲ��Ҵ�λ��
void findBedNumberOfSex()
{
	int i, j; 
	int boyBedNum = 0, girlBedNum = 0, n = 0, havingNum = 0;
	int boyFloorEmpty[10][20] = {0}, girlFloorEmpty[10][50] = {0};
	char in[3];
	struct dorm *p = dormHead1->next; 
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                 ���������ʲ���                  \n");
	printf("-------------------------------------------------\n");
	printf("�������������ʣ�");
	fflush(stdin);
	gets(in);
	if( (strcmp(in, "��") != 0) && strcmp(in, "Ů") != 0)
	{
		printf("����Ƿ��������²�ѯ.\n\t");
		return;
	}
		 
	printf("\n\n\t\t���ڲ�ѯ......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                 ���������ʲ���                  \n");
	printf("-------------------------------------------------\n");
	if(strcmp(in, "��") == 0)
	{
		for(p; p != NULL; p = p->next)
		{
			if(strcmp(p->dormSex, "��") == 0)
			{
				boyBedNum += p->emptyBed;
				havingNum += p->haveNum;
				boyFloorEmpty[p->building][p->floorNum] += p->emptyBed;
			}
		}
		printf("�������᣺�ܵĿմ�λ��Ϊ��%d , ��ռ��λ��Ϊ%d\n", boyBedNum, havingNum);
		for(i = 0; i < 10; i++)
			for(j = 0; j < 20; j++)
			{
				if(boyFloorEmpty[i][j])
					printf("\t%d��%d¥�մ�λ��Ϊ��%d\n", i, j, boyFloorEmpty[i][j]);
			}
	}
	else
	{
		for(p; p != NULL; p = p->next)
		{
			if(strcmp(p->dormSex, "Ů") == 0)
			{
				girlBedNum += p->emptyBed;
				havingNum += p->haveNum;
				girlFloorEmpty[p->building][p->floorNum] += p->emptyBed;
			}
		}
		printf("Ů�����᣺�ܵĿմ�λ��Ϊ��%d , ��ռ��λ��Ϊ%d\n", girlBedNum, havingNum);
		for(i = 0; i < 10; i++)
			for(j = 0; j < 20; j++)
			{
				if(girlFloorEmpty[i][j])
					printf("\t%d��%d¥�մ�λ��Ϊ��%d\n", i, j, girlFloorEmpty[i][j]);
			}
	}
	Sleep(1000);
}
//����¥��Ų��ҿ�����
void findBedNumberOfFloor()
{
	int i = 0, j, empty[30] = {0};
	int emptyHouseNumber = 0, build = 0, floor = 0;
	struct dorm *p = dormHead1->next;
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                 ¥����������                  \n");
	printf("-------------------------------------------------\n");
	printf("\t�����붰�ţ�");
	scanf("%d", &build);
	printf("\t������¥�ţ�");
	scanf("%d", &floor);
	printf("\n\n\t\t���ڲ�ѯ......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                 ¥����������                  \n");
	printf("-------------------------------------------------\n");
	for(p; p != NULL; p = p->next)
	{
		if(p->building == build)
			if(p->floorNum == floor) 
				if(p->emptyBed == 4)
				{
					emptyHouseNumber++;
					empty[i++] = p->houseNum;
				}
	}
	printf("%d��%d¥�Ŀ�������Ϊ��%d\n", build, floor, emptyHouseNumber);
	printf("�����᷿��ŷֱ�Ϊ��\n");
	for(j = 0; j < i; j++)
	{
		printf("%d ", empty[j]);
		if(j % 5 == 0 && j != 0)
			printf("\n");
	}
	Sleep(1000);
	printf("\n\t���������......");
}
//���շ��Ų��ҿմ�λ��
void findInformationOfHouse()
{
	int build, floor, house, flag = 0;
	struct dorm *p = dormHead1->next;
	struct student *p1 = studentHead->next;
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                 ���䴲λ������                  \n");
	printf("-------------------------------------------------\n");
	printf("\t�����붰�ţ�");
	scanf("%d", &build);
	printf("\t������¥�ţ�");
	scanf("%d", &floor);
	printf("\t�����뷿�ţ�");
	scanf("%d", &house);
	printf("\n\n\t\t���ڲ�ѯ......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                 ���䴲λ������                  \n");
	printf("-------------------------------------------------\n");
	for(p; p != NULL; p = p->next)
	{
		if(p->building == build)
			if(p->floorNum == floor)
				if(p->houseNum == house)
				{
					printf("%d��%d¥%d�ŷ��մ�λ��Ϊ��%d", build, floor, house, p->emptyBed);
					flag = 1;
					if(p->haveNum != 0)
					{
						printf("\n�÷�����סѧ����λ��ϢΪ��\n");
						for(p1; p1 != NULL; p1 = p1->next)
						{
							if(p1->building == build)
								if(p1->floorNum == floor)
									if(p1->houseNum == house)
									{
										printf("\t������%s\n\t�Ա�%s\n\t���䣺%d\n", p1->name, p1->sex, p1->age);
										printf("\t�����ַ��%d��%d¥%d�ŷ�\n", p1->building, p1->floorNum, p1->houseNum);
										printf("\tѧ�ţ�%s\n\tרҵ��%s\n\t��סʱ�䣺%s\n\n", p1->number, p1->major, p1->times);	
									}
						}
					}
					break;
				}
	}
	if(!flag)
		printf("\t���޴˷�����Ϣ�������²���.");
	Sleep(1000);
	printf("\n\t���������......");
}
//����ѧ����Ϣ 
void findInformationOfStudent()
{
	int flag = 0;
	char name[10];
	struct student *p =  studentHead->next;
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ����Ϣ����                   \n");
	printf("-------------------------------------------------\n");
	printf("������ѧ��������");
	fflush(stdin);
	gets(name);
	printf("\n\n\t\t���ڲ�ѯ......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--������Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ����Ϣ����                   \n");
	printf("-------------------------------------------------\n");
	for(p; p != NULL; p = p->next)
	{
		if(strcmp(p->name, name) == 0)
		{
			printf("\t������%s\n\t�Ա�%s\n\t���䣺%d\n", p->name, p->sex, p->age);
			printf("\t�����ַ��%d��%d¥%d�ŷ�\n", p->building, p->floorNum, p->houseNum);
			printf("\tѧ�ţ�%s\n\tרҵ��%s\n\t��סʱ�䣺%s\n", p->number, p->major, p->times);
			flag = 1; 
		}
	}
	if(flag == 0)
		printf("�޴�ѧ����Ϣ����������ȷ���������ҡ�");
	Sleep(1000);
	printf("\n\t���������......");
}
//������Ϣ
void findInformationMenu()
{
	int i;
	char ch;
	system("cls");
	while(1)
	{
		system("cls");
		for(i = 0; i < 19; i++)
		{
			printf("\t\t");
			puts(findMenu[i]);
		}
		ch = getch();
		switch(ch)
		{
			case '1':
				system("cls");
				findBedNumberOfSex();
				printf("���������......");
				getch();
				break;
			case '2':
				system("cls");
				findBedNumberOfFloor();
				getch();
				break;
			case '3':
				system("cls");
				findInformationOfHouse();
				getch();
				break;
			case '4':
				system("cls");
				findInformationOfStudent();
				getch(); 
				break;
		}
		if(ch == '0')
			break;
	}
}
//���ѧ����Ϣ
void lookInformationOfStudent()
{
	struct student *p = studentHead->next;
	int i = 0;
	char ch;
	for(p; p != NULL; p = p->next)
	{
		system("cls");
		for(i = 0; i < 19; i++)
		{
			printf("\t\t");
			puts(emptyMenu[i]);
		}
		SetPos(29,3);
		printf("ѧ����Ϣ���");
		SetPos(40,16);
		printf("�������һ��");
		SetPos(44,17);
		printf("ESC �˳�");
		SetPos(27,7);
		printf("������%s", p->name);
		SetPos(27,8);
		printf("�Ա�%s", p->sex);
		SetPos(27,9);
		printf("���䣺%d", p->age);
		SetPos(27,10);
		printf("ѧ�ţ�%s", p->number);
		SetPos(27,11);
		printf("רҵ��%s", p->major);
		SetPos(27,12);
		printf("�����ַ��%d��%d¥%d�ŷ�", p->building, p->floorNum, p->houseNum);
		ch = getch();
		if(ch == 27)
			break; 
	}
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�����Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ����Ϣ���                   \n");
	printf("-------------------------------------------------\n");
	if(ch != 27)
		printf("\t����ѧ����Ϣ�Ѿ�������.");
	Sleep(1000);
	printf("\n\t���������......");
}
//������Ϣ���
void lookInformationOfDorm()
{
	struct dorm *p = dormHead1->next;
	int i = 0;
	char ch;
	for(p; p != NULL; p = p->next)
	{
		system("cls");
		for(i = 0; i < 19; i++)
		{
			printf("\t\t");
			puts(emptyMenu[i]);
		}
		SetPos(29,3);
		printf("������Ϣ���");
		SetPos(40,16);
		printf("�������һ��");
		SetPos(44,17);
		printf("ESC �˳�");
		SetPos(27,7);
		printf("%s������", p->dormSex);
		SetPos(27,8);
		printf("%d��%d¥%d�ŷ�", p->building, p->floorNum, p->houseNum);
		SetPos(27,9);
		printf("��ס%d�ˣ��մ�λ��Ϊ%d", p->haveNum, p->emptyBed);
		ch = getch();
		if(ch == 27)
			break; 
	}
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�����Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ������Ϣ���                   \n");
	printf("-------------------------------------------------\n");
	if(ch != 27)
		printf("\t����������Ϣ�Ѿ�������.");
	Sleep(1000);
	printf("\n\t���������......");
}
//�����Ϣ
void lookInformation()
{
	int i;
	char ch;
	system("cls");
	while(1)
	{
		system("cls");
		for(i = 0; i < 19; i++)
		{
			printf("\t\t");
			puts(lookMenu[i]);
		}
		ch = getch();
		switch(ch)
		{
			case '1':
				system("cls");
				lookInformationOfStudent();
				getch();
				break;
			case '2':
				system("cls");
				lookInformationOfDorm();
				getch();
				break;
		}
		if(ch == '0')
			break;
	}
}
//�޸�ѧ������
void changeStudentName()
{
	struct student *p;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�޸���Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ�������޸�                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t������ԭ������");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				printf("\t��������������");
				gets(p->name);
				p->name[strlen(p->name)] = '\0';
				Sleep(1000);
				printf("\t�޸ĳɹ���");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t�޴�����������������:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t���������......");
	writeStudentToFile();
}
//�޸�ѧ��ѧ��
void changeStudentNumber()
{
	struct student *p;
	int flag = 0;
	char number[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�޸���Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ��ѧ���޸�                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t������ԭѧ�ţ�");
		fflush(stdin);
		gets(number);
		number[strlen(number)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->number, number) == 0)
			{
				printf("\t��������ѧ�ţ�");
				gets(p->number);
				p->number[strlen(p->number)] = '\0';
				Sleep(1000);
				printf("\t�޸ĳɹ���");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t�޴�ѧ�ţ�����������:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t���������......");
	writeStudentToFile();
}
//�޸�ѧ������
void changeStudentAge()
{
	struct student *p;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�޸���Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ�������޸�                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t�������޸�ѧ��������");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				printf("\t�������µ����䣺");
				scanf("%d", &p->age);
				Sleep(1000);
				printf("\t�޸ĳɹ���");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t�޴�ѧ��������������:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t���������......");
	writeStudentToFile();
}
//�޸�ѧ��רҵ
void changeStudentMajor()
{
	struct student *p;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�޸���Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("                  ѧ��רҵ�޸�                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t�������޸�ѧ��������");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				printf("\t�������µ�רҵ��");
				gets(p->major);
				p->major[strlen(p->major)] = '\0';
				Sleep(1000);
				printf("\t�޸ĳɹ���");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t�޴�����������������:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t���������......");
	writeStudentToFile();
}
//�޸�ѧ��ס����Ϣ
void changeStudentDorm()
{
	struct student *p;
	struct dorm *p1;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�޸���Ϣ            \n");
	printf("-------------------------------------------------\n");
	printf("               ѧ��ס����Ϣ�޸�                  \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t�������޸�ѧ��������");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				for(p1 = dormHead1->next; p1 != NULL; p1 = p1->next)
				{
					if(p1->building == p->building)
						if(p1->floorNum == p->floorNum)
							if(p1->houseNum == p->houseNum)
							{
								p1->emptyBed++;
								p1->haveNum--;
							}
				}
				printf("\t�������µ������ַ����,¥,������");
				scanf("%d%d%d", &p->building, &p->floorNum, &p->houseNum);
				for(p1 = dormHead1->next; p1 != NULL; p1 = p1->next)
				{
					if(p1->building == p->building)
						if(p1->floorNum == p->floorNum)
							if(p1->houseNum == p->houseNum)
							{
								p1->emptyBed--;
								p1->haveNum++;
							}
				}
				Sleep(1000);
				printf("\t�޸ĳɹ���");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t�޴�����������������:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t���������......");
	writeStudentToFile();
	writeDormToFile();
}
//�޸���Ϣ
void modifyInformation()
{
	char ch;
	while(1)
	{
		system("cls");
		printf("-------------------------------------------------\n");
		printf("           ѧ���������ϵͳ--�޸���Ϣ            \n");
		printf("-------------------------------------------------\n");
		printf("                1.ѧ�������޸�                   \n");
		printf("                2.ѧ��ѧ���޸�                   \n");
		printf("                3.ѧ�������޸�                   \n");
		printf("                4.ѧ��רҵ�޸�                   \n");
		printf("                5.ѧ��ס���޸�                   \n");
		printf("                    0.�˳�                       \n");
		ch = getch();
		switch(ch)
		{
			case '1':
				changeStudentName();
				getch();
				break;
			case '2':
				changeStudentNumber();
				getch(); 
				break;
			case '3':
				changeStudentAge();
				getch();
				break;
			case '4':
				changeStudentMajor();
				getch();
				break;
			case '5':
				changeStudentDorm();
				getch();
				break;
		}
		if(ch == '0')
			break;
	}
}
//�˳�����
void exitDorm()
{
	struct student *p;
	struct student *temp = NULL;
	struct dorm *p1;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           ѧ���������ϵͳ--�˳�����            \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n���������ѧ��������");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p != NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				for(p1 = dormHead1->next; p1 != NULL; p1 = p1->next)
					if(p->building == p1->building)
						if(p->floorNum == p1->floorNum)
							if(p->houseNum == p1->houseNum)
							{
								p1->haveNum--;
								p1->emptyBed++;
								flag = 1;
								break;
							}
			}
			if(flag)
				break;
		}
		flag = 0;
		for(p = studentHead; p->next != NULL; p = p->next)
		{
			if(strcmp(p->next->name, name) == 0)
			{
				temp = p->next;
				p->next = p->next->next;
				free(temp);
				Sleep(1000);
				printf("\n\t�޸ĳɹ���");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("�޴�����������������.");
		else
		    break;
	}
	Sleep(1000);
	printf("\n\t���������......");
	writeStudentToFile();
	writeDormToFile();
}
//��ѧ����Ϣд���ļ� 
void writeStudentToFile()
{
	FILE *fp;
	struct student *p = studentHead->next;
	if( (fp = fopen("allStudents.txt", "w")) == NULL )
	{
		printf("����ѧ����Ϣ.");
		exit(0);
	}
	while(p != NULL)
	{
		fprintf(fp, "%d ", p->building);
		fprintf(fp, "%d ", p->floorNum);
		fprintf(fp, "%d ", p->houseNum);
		fprintf(fp, "%s ", p->name);
		fprintf(fp, "%s ", p->sex);
		fprintf(fp, "%s ", p->number);
		fprintf(fp, "%d ", p->age);
		fprintf(fp, "%s ", p->major);
		fprintf(fp, "%s\n", p->times);
		p = p->next;
	}
}
//��������Ϣд���ļ� 
void writeDormToFile()
{
	FILE *fp;
	struct dorm *p = dormHead1->next;
	boyN = 0;
	if( (fp = fopen("dorm.txt", "w")) == NULL )
	{
		printf("����ѧ����Ϣ.");
		exit(0);
	}
	while(p != NULL)
	{
		fprintf(fp, "%d ", p->building);
		fprintf(fp, "%d ", p->floorNum);
		fprintf(fp, "%d ", p->houseNum);
		fprintf(fp, "%d ", p->haveNum);
		fprintf(fp, "%d ", p->emptyBed);
		fprintf(fp, "%s\n", p->dormSex);
		p = p->next;
		boyN++;
	}
}
//ϵͳ����ҳ��
void systemStartPage()
{
	int i;
	char ch;
	while(1)
	{
		system("cls");
		for(i = 0; i < 19; i++)
		{
			printf("\t\t");
			puts(dormtMenu[i]);
		}
		ch = getch();
		switch(ch)
		{
			case '1':addDormInformation(); break;
			case '2':findInformationMenu(); break;
			case '3':lookInformation(); break;
			case '4':modifyInformation(); break;
			case '5':exitDorm(); break;
			
		}
		if(ch == '0')
		{
			system("cls");
			break;
		}
	}
	
}
//�ͷ�������Ϣ����ռ� 
void dischargeDorm()
{
	struct dorm *p = dormHead1;
	struct dorm *temp = NULL;
	while(p->next != NULL)
	{
		temp = p->next;
		free(p);
		p = temp;
	}
}
//�ͷ�ѧ����Ϣ����ռ� 
void dischargeStudent()
{
	struct student *p = studentHead;
	struct student *temp = NULL;
	while(p->next != NULL)
	{
		temp = p->next;
		free(p);
		p = temp;
	}
}

int main()
{
	int i, pos = 0, flag = 0;
	char ch;
	readStudentsFileRow();
	readDormFileRow();
	readDormInformation();
	readStudentInformation();
	while(1)
	{
		flag = 0;
		system("cls");
		for(i = 0; i < 19; i++)
		{
			printf("\t\t");
			puts(startMenu[i]);
		}
		ch = getch();
		switch(ch)
		{
			case '1':
				system("cls");
				while(!flag)
				{
					system("cls");
					flag = logIn();
				}
				if(flag == 1)
					systemStartPage();
				break;
			case '2':
				system("cls");
				while(1)
				{
					flag = changeCode();
					if(flag == 0)
						break;
					system("cls");
				}
				break;
			case '3':
				system("cls");
				forRegister();
				break;
		}
		if(ch == '0')
		{
			system("cls");
			printf("\n\t��лʹ�ã�");
			break;
		}
	}
	writeStudentToFile();
	writeDormToFile();
	dischargeDorm();
	dischargeStudent();
	return 0;
}





