#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<conio.h>
#include<windows.h>
#include"common.h"

//设置光标位置
void SetPos(int x,int y)
{
    COORD pos;
	HANDLE handle;
    pos.X=x;
    pos.Y=y;
    handle=GetStdHandle(STD_OUTPUT_HANDLE);
    SetConsoleCursorPosition(handle,pos);
}
//读取文件中数据个数
void readStudentsFileRow()
{
	FILE *fp;
	char c;
	if( (fp = fopen("allStudents.txt", "r")) == NULL )
	{
		printf("暂无学生信息.");
		exit(0);
	}
	while(!feof(fp))
	{
		c = fgetc(fp);
		if(c == '\n')
			nn++;
	}
}
//读取文件中数据个数
void readDormFileRow()
{
	FILE *fp;
	char c;
	if( (fp = fopen("dorm.txt", "r")) == NULL )
	{
		printf("暂无学生信息.");
		exit(0);
	}
	while(!feof(fp))
	{
		c = fgetc(fp);
		if(c == '\n')
			boyN++;
	}
}
//注册用户 
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
//修改密码 
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
						printf("密码超限！请输入14位以内密码"); 
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
							printf("密码超限！请输入14位以内密码"); 
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
					printf("密码错误!请重新输入");
					SetPos(30, 9);
					printf("               ");
					SetPos(30, 9);
				}
			}
		}
	}
	SetPos(22, 12);
	printf("用户名输入错误，请重新输入:");
	SetPos(30, 10);
	printf("               ");
	SetPos(30, 10);
	Sleep(1000);
	fclose(fp);
	return 1;
}
//用户登录 
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
						printf("密码超限！请输入14位以内密码"); 
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
					printf("密码错误!请重新输入");
					SetPos(30, 10);
					printf("               ");
					SetPos(30, 10);
				}
			}		
		}
	}
	SetPos(22, 12);
	printf("用户名输入错误，请重新输入:");
	SetPos(30, 10);
	printf("               ");
	SetPos(30, 10);
	Sleep(1000); 
	fclose(fp);
	return 0;
}
//插入节点到链表(宿舍)
void insertDormNode(struct dorm *node, struct dorm *dormHead)
{
	struct dorm *p = dormHead;
	while(p->next != NULL)
		p = p->next;
	p->next = node;
	node->next = NULL;
}
//插入节点到链表(学生)
void insertStudentNode(struct student *node)
{
	struct student *p = studentHead;
	while(p->next != NULL)
		p = p->next;
	p->next = node;
	node->next = NULL;
}
//读取宿舍信息
void readDormInformation()
{
	FILE *fp;
	int i;
	struct dorm *p = NULL;
	dormHead1 = (struct dorm *)malloc(sizeof(struct dorm));
	dormHead1->next = NULL;
	
	if( (fp = fopen("dorm.txt", "r")) == NULL )
	{
		printf("暂无宿舍信息,等待学生入住.");
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
//读取学生信息
void readStudentInformation()
{
	FILE *fp;
	int i;
	struct student *p = NULL;
	studentHead = (struct student *)malloc(sizeof(struct student));
	studentHead->next = NULL;
	if( (fp = fopen("allStudents.txt", "r")) == NULL ){
		printf("暂无学生信息.");
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
//入住宿舍 
void addDormInformation()
{
	struct dorm *p = NULL;
	struct student *aStu = (struct student *)malloc(sizeof(struct student));
	aStu->next = NULL;
	system("cls"); 
	printf("-------------------------------------------------\n");
	printf("          学生宿舍管理系统--入住宿舍             \n");
	printf("-------------------------------------------------\n");
	fflush(stdin);
	printf("\t请输入学生姓名：");
	gets(aStu->name);
	aStu->name[strlen(aStu->name)] = '\0';
	fflush(stdin);
	printf("\t请输入学生性别：");
	gets(aStu->sex);
	aStu->sex[strlen(aStu->sex)] = '\0';
	fflush(stdin);
	printf("\t请输入学生年龄：");
	scanf("%d", &aStu->age);
	fflush(stdin);
	printf("\t请输入学生学号：");
	gets(aStu->number);
	aStu->number[strlen(aStu->number)] = '\0';
	fflush(stdin);
	printf("\t请输入学生专业：");
	gets(aStu->major);
	aStu->major[strlen(aStu->major)] = '\0';
	fflush(stdin);
	printf("\t请输入学生入住时间：");
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
//按照宿舍性质查找床位数
void findBedNumberOfSex()
{
	int i, j; 
	int boyBedNum = 0, girlBedNum = 0, n = 0, havingNum = 0;
	int boyFloorEmpty[10][20] = {0}, girlFloorEmpty[10][50] = {0};
	char in[3];
	struct dorm *p = dormHead1->next; 
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                 按宿舍性质查找                  \n");
	printf("-------------------------------------------------\n");
	printf("请输入宿舍性质：");
	fflush(stdin);
	gets(in);
	if( (strcmp(in, "男") != 0) && strcmp(in, "女") != 0)
	{
		printf("输入非法，请重新查询.\n\t");
		return;
	}
		 
	printf("\n\n\t\t正在查询......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                 按宿舍性质查找                  \n");
	printf("-------------------------------------------------\n");
	if(strcmp(in, "男") == 0)
	{
		for(p; p != NULL; p = p->next)
		{
			if(strcmp(p->dormSex, "男") == 0)
			{
				boyBedNum += p->emptyBed;
				havingNum += p->haveNum;
				boyFloorEmpty[p->building][p->floorNum] += p->emptyBed;
			}
		}
		printf("男生宿舍：总的空床位数为：%d , 已占床位数为%d\n", boyBedNum, havingNum);
		for(i = 0; i < 10; i++)
			for(j = 0; j < 20; j++)
			{
				if(boyFloorEmpty[i][j])
					printf("\t%d栋%d楼空床位数为：%d\n", i, j, boyFloorEmpty[i][j]);
			}
	}
	else
	{
		for(p; p != NULL; p = p->next)
		{
			if(strcmp(p->dormSex, "女") == 0)
			{
				girlBedNum += p->emptyBed;
				havingNum += p->haveNum;
				girlFloorEmpty[p->building][p->floorNum] += p->emptyBed;
			}
		}
		printf("女生宿舍：总的空床位数为：%d , 已占床位数为%d\n", girlBedNum, havingNum);
		for(i = 0; i < 10; i++)
			for(j = 0; j < 20; j++)
			{
				if(girlFloorEmpty[i][j])
					printf("\t%d栋%d楼空床位数为：%d\n", i, j, girlFloorEmpty[i][j]);
			}
	}
	Sleep(1000);
}
//按照楼层号查找空宿舍
void findBedNumberOfFloor()
{
	int i = 0, j, empty[30] = {0};
	int emptyHouseNumber = 0, build = 0, floor = 0;
	struct dorm *p = dormHead1->next;
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                 楼层空宿舍查找                  \n");
	printf("-------------------------------------------------\n");
	printf("\t请输入栋号：");
	scanf("%d", &build);
	printf("\t请输入楼号：");
	scanf("%d", &floor);
	printf("\n\n\t\t正在查询......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                 楼层空宿舍查找                  \n");
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
	printf("%d栋%d楼的空宿舍数为：%d\n", build, floor, emptyHouseNumber);
	printf("空宿舍房间号分别为：\n");
	for(j = 0; j < i; j++)
	{
		printf("%d ", empty[j]);
		if(j % 5 == 0 && j != 0)
			printf("\n");
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
}
//按照房号查找空床位数
void findInformationOfHouse()
{
	int build, floor, house, flag = 0;
	struct dorm *p = dormHead1->next;
	struct student *p1 = studentHead->next;
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                 房间床位数查找                  \n");
	printf("-------------------------------------------------\n");
	printf("\t请输入栋号：");
	scanf("%d", &build);
	printf("\t请输入楼号：");
	scanf("%d", &floor);
	printf("\t请输入房号：");
	scanf("%d", &house);
	printf("\n\n\t\t正在查询......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                 房间床位数查找                  \n");
	printf("-------------------------------------------------\n");
	for(p; p != NULL; p = p->next)
	{
		if(p->building == build)
			if(p->floorNum == floor)
				if(p->houseNum == house)
				{
					printf("%d栋%d楼%d号房空床位数为：%d", build, floor, house, p->emptyBed);
					flag = 1;
					if(p->haveNum != 0)
					{
						printf("\n该房间已住学生床位信息为：\n");
						for(p1; p1 != NULL; p1 = p1->next)
						{
							if(p1->building == build)
								if(p1->floorNum == floor)
									if(p1->houseNum == house)
									{
										printf("\t姓名：%s\n\t性别：%s\n\t年龄：%d\n", p1->name, p1->sex, p1->age);
										printf("\t宿舍地址：%d栋%d楼%d号房\n", p1->building, p1->floorNum, p1->houseNum);
										printf("\t学号：%s\n\t专业：%s\n\t入住时间：%s\n\n", p1->number, p1->major, p1->times);	
									}
						}
					}
					break;
				}
	}
	if(!flag)
		printf("\t暂无此房间信息，请重新查找.");
	Sleep(1000);
	printf("\n\t任意键继续......");
}
//查找学生信息 
void findInformationOfStudent()
{
	int flag = 0;
	char name[10];
	struct student *p =  studentHead->next;
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生信息查找                   \n");
	printf("-------------------------------------------------\n");
	printf("请输入学生姓名：");
	fflush(stdin);
	gets(name);
	printf("\n\n\t\t正在查询......");
	Sleep(2000);
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--查找信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生信息查找                   \n");
	printf("-------------------------------------------------\n");
	for(p; p != NULL; p = p->next)
	{
		if(strcmp(p->name, name) == 0)
		{
			printf("\t姓名：%s\n\t性别：%s\n\t年龄：%d\n", p->name, p->sex, p->age);
			printf("\t宿舍地址：%d栋%d楼%d号房\n", p->building, p->floorNum, p->houseNum);
			printf("\t学号：%s\n\t专业：%s\n\t入住时间：%s\n", p->number, p->major, p->times);
			flag = 1; 
		}
	}
	if(flag == 0)
		printf("无此学生信息，请输入正确的姓名查找。");
	Sleep(1000);
	printf("\n\t任意键继续......");
}
//查找信息
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
				printf("任意键继续......");
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
//浏览学生信息
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
		printf("学生信息浏览");
		SetPos(40,16);
		printf("任意键下一个");
		SetPos(44,17);
		printf("ESC 退出");
		SetPos(27,7);
		printf("姓名：%s", p->name);
		SetPos(27,8);
		printf("性别：%s", p->sex);
		SetPos(27,9);
		printf("年龄：%d", p->age);
		SetPos(27,10);
		printf("学号：%s", p->number);
		SetPos(27,11);
		printf("专业：%s", p->major);
		SetPos(27,12);
		printf("宿舍地址：%d栋%d楼%d号房", p->building, p->floorNum, p->houseNum);
		ch = getch();
		if(ch == 27)
			break; 
	}
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--浏览信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生信息浏览                   \n");
	printf("-------------------------------------------------\n");
	if(ch != 27)
		printf("\t所有学生信息已经浏览完毕.");
	Sleep(1000);
	printf("\n\t任意键继续......");
}
//宿舍信息浏览
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
		printf("宿舍信息浏览");
		SetPos(40,16);
		printf("任意键下一个");
		SetPos(44,17);
		printf("ESC 退出");
		SetPos(27,7);
		printf("%s生宿舍", p->dormSex);
		SetPos(27,8);
		printf("%d栋%d楼%d号房", p->building, p->floorNum, p->houseNum);
		SetPos(27,9);
		printf("已住%d人，空床位数为%d", p->haveNum, p->emptyBed);
		ch = getch();
		if(ch == 27)
			break; 
	}
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--浏览信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  宿舍信息浏览                   \n");
	printf("-------------------------------------------------\n");
	if(ch != 27)
		printf("\t所有宿舍信息已经浏览完毕.");
	Sleep(1000);
	printf("\n\t任意键继续......");
}
//浏览信息
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
//修改学生姓名
void changeStudentName()
{
	struct student *p;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--修改信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生姓名修改                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t请输入原姓名：");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				printf("\t请输入新姓名：");
				gets(p->name);
				p->name[strlen(p->name)] = '\0';
				Sleep(1000);
				printf("\t修改成功！");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t无此姓名，请重新输入:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
	writeStudentToFile();
}
//修改学生学号
void changeStudentNumber()
{
	struct student *p;
	int flag = 0;
	char number[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--修改信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生学号修改                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t请输入原学号：");
		fflush(stdin);
		gets(number);
		number[strlen(number)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->number, number) == 0)
			{
				printf("\t请输入新学号：");
				gets(p->number);
				p->number[strlen(p->number)] = '\0';
				Sleep(1000);
				printf("\t修改成功！");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t无此学号，请重新输入:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
	writeStudentToFile();
}
//修改学生年龄
void changeStudentAge()
{
	struct student *p;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--修改信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生年龄修改                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t请输入修改学生姓名：");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				printf("\t请输入新的年龄：");
				scanf("%d", &p->age);
				Sleep(1000);
				printf("\t修改成功！");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t无此学生，请重新输入:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
	writeStudentToFile();
}
//修改学生专业
void changeStudentMajor()
{
	struct student *p;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--修改信息            \n");
	printf("-------------------------------------------------\n");
	printf("                  学生专业修改                   \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t请输入修改学生姓名：");
		fflush(stdin);
		gets(name);
		name[strlen(name)] = '\0';
		for(p = studentHead->next; p !=NULL; p = p->next)
		{
			if(strcmp(p->name, name) == 0)
			{
				printf("\t请输入新的专业：");
				gets(p->major);
				p->major[strlen(p->major)] = '\0';
				Sleep(1000);
				printf("\t修改成功！");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t无此姓名，请重新输入:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
	writeStudentToFile();
}
//修改学生住宿信息
void changeStudentDorm()
{
	struct student *p;
	struct dorm *p1;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--修改信息            \n");
	printf("-------------------------------------------------\n");
	printf("               学生住宿信息修改                  \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n\t请输入修改学生姓名：");
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
				printf("\t请输入新的宿舍地址（栋,楼,房）：");
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
				printf("\t修改成功！");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("\t无此姓名，请重新输入:");
		else
			break;
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
	writeStudentToFile();
	writeDormToFile();
}
//修改信息
void modifyInformation()
{
	char ch;
	while(1)
	{
		system("cls");
		printf("-------------------------------------------------\n");
		printf("           学生宿舍管理系统--修改信息            \n");
		printf("-------------------------------------------------\n");
		printf("                1.学生姓名修改                   \n");
		printf("                2.学生学号修改                   \n");
		printf("                3.学生年龄修改                   \n");
		printf("                4.学生专业修改                   \n");
		printf("                5.学生住宿修改                   \n");
		printf("                    0.退出                       \n");
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
//退出宿舍
void exitDorm()
{
	struct student *p;
	struct student *temp = NULL;
	struct dorm *p1;
	int flag = 0;
	char name[15];
	system("cls");
	printf("-------------------------------------------------\n");
	printf("           学生宿舍管理系统--退出宿舍            \n");
	printf("-------------------------------------------------\n");
	while(1)
	{
		printf("\n请输入搬离学生姓名：");
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
				printf("\n\t修改成功！");
				flag = 1;
			}
			if(flag)
				break;
		}
		if(!flag)
			printf("无此姓名，请重新输入.");
		else
		    break;
	}
	Sleep(1000);
	printf("\n\t任意键继续......");
	writeStudentToFile();
	writeDormToFile();
}
//将学生信息写入文件 
void writeStudentToFile()
{
	FILE *fp;
	struct student *p = studentHead->next;
	if( (fp = fopen("allStudents.txt", "w")) == NULL )
	{
		printf("暂无学生信息.");
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
//将宿舍信息写入文件 
void writeDormToFile()
{
	FILE *fp;
	struct dorm *p = dormHead1->next;
	boyN = 0;
	if( (fp = fopen("dorm.txt", "w")) == NULL )
	{
		printf("暂无学生信息.");
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
//系统功能页面
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
//释放宿舍信息链表空间 
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
//释放学生信息链表空间 
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
			printf("\n\t感谢使用！");
			break;
		}
	}
	writeStudentToFile();
	writeDormToFile();
	dischargeDorm();
	dischargeStudent();
	return 0;
}





