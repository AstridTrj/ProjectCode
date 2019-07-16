#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <windows.h>
#include <conio.h>

//����������ɫ���߱�����ɫ
void setColor(unsigned short ForeColor=7,unsigned short BackGroundColor=0)
{
	HANDLE handle=GetStdHandle(STD_OUTPUT_HANDLE);//��ȡ��ǰ���ھ��
	SetConsoleTextAttribute(handle,ForeColor+BackGroundColor*0x10);//������ɫ
}
//���������Ϸ���� 
void creatGameArray(int n, int (*pArray)[100])
{
	int i,j;
	srand(time(NULL));
	for(i=0; i<n; i++)
	    for(j=0; j<n; j++)
	        *(*(pArray+i)+j) = rand()%20+10;
}
//������� 
void imput(int n, int (*array)[100])
{
	setColor(10, 0);
	int i, j;
	printf("\n\t");
	for(i=0; i<5*n+1; i++) {
		printf("-");
	}
	printf("\n");
	for(i=0; i<n; i++)
	{
		printf("\t|");
		for(j=0; j<n; j++) {
			if(*(*(array+i)+j) == 0){
				setColor(12, 0);
				printf(" %d  ", *(*(array+i)+j));
				setColor(10, 0);
				printf("|");
			}
			else
				printf(" %d |", *(*(array+i)+j));
		}
		printf("\n\t");
		for(j=0; j< 5*n+1; j++)
		    printf("-");
	    printf("\n");
	}
	setColor(7, 0);
}
//ϵͳ��������������� 
int randNum(int n, int (*gameArray)[100])
{
	int k,sum = 0,num = 0;
	int a,b,c,d,i,j;
	srand(time(NULL));
	for(i=0; i<n; i++)
	    for(j=0; j<n; j++)
	        if((*(*(gameArray+i)+j)) == 0)
	            sum++;
	if(sum == n*n)
	    return 0;
	if(sum == (n*n - 1))
	    return 2;
	for(k=0; k<1; k++)
	{
		a = rand()%n;
		b = rand()%n;
		if( (*(*(gameArray+a)+b)) != 0)
		    num += (*(*(gameArray+a)+b));
		else
		    k--;
	}
	for(k=0; k<1; k++)
	{
		c = rand()%n;
		d = rand()%n;
		if( (c == a) && (d == b) )
			k--;
		else{
			if( (*(*(gameArray + c) + d)) != 0)
		    	num += (*(*(gameArray + c) + d));
		    else
		        k--;
		}
	}
	return num;
 }
 //�ж����벢���Ϊ0
void delteToZero(int n, int (*array)[100], int num)
 {
    int a,b,c,d;
 	printf("��������ȷ���ĵ�һ��λ������(0��ʼ)��");
	while(1) {
		scanf("%d%d", &a, &b);
		if(a > n - 1 || b > n - 1)
			printf("λ�ó������ƣ�����������:");
		else{
			if( (*(*(array+a)+b)) == 0)
				printf("��λ�������Ѿ��������������������:");
			else
		    	break;
		}
	}
	printf("��������ȷ���ĵڶ���λ������(0��ʼ)��");
	while(1) {
		scanf("%d%d", &c, &d);
		if(c > n - 1 || d > n - 1)
			printf("λ�ó������ƣ�����������:");
		else{
			if((*(*(array+c)+d)) == 0)
				printf("��λ�������Ѿ��������������������:");
			else
		    	break;
		}
	}
	if( ((*(*(array+a)+b)) + (*(*(array+c)+d))) == num ){
		(*(*(array+a)+b)) = 0;
		(*(*(array+c)+d)) = 0;
		printf("�������⣬����ɹ���");
	}
	else
		printf("���ʧ��.");
	printf("���������!");
	getch();
  } 
 //�������ֲ��ж���Ϸ�Ƿ����
void gamePlay(int n, int (*array)[100])
 {
 	int num;
 	while(1){
 		system("cls");
		imput(n, array);
 		num = randNum(n, array);
 		if(num == 0){
 			printf("��ϲ��������ȫ������ɹ���");
 			break;
		}
		if(num == 2){
			printf("��������ֻʣһ������Ϸ���Ƚ�����");
			break;
		}
		printf("\n��������:%d\n", num);
		delteToZero(n, array, num);
	}
}

void timeCalculate()
{
	int start, end,i = 3;
	int gameArray[100][100],n;
	printf("��������Ϸ�������: ");
	scanf("%d", &n);
	while(i > 0){
		system("cls");
		printf("\n\t��Ϸ������ʼ��%d\n", i);
		Sleep(1000);
		i--;
	}
	start = clock();
	creatGameArray(n, gameArray);
	gamePlay(n, gameArray);
	end = clock();
	printf("\n  �����Ϸʱ��Ϊ %d s", (end - start)/1000);
}
int main()
{
	timeCalculate();
	return 0;
}











