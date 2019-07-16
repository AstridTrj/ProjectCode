#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <windows.h>
#include <conio.h>

//设置字体颜色或者背景颜色
void setColor(unsigned short ForeColor=7,unsigned short BackGroundColor=0)
{
	HANDLE handle=GetStdHandle(STD_OUTPUT_HANDLE);//获取当前窗口句柄
	SetConsoleTextAttribute(handle,ForeColor+BackGroundColor*0x10);//设置颜色
}
//随机创建游戏矩阵 
void creatGameArray(int n, int (*pArray)[100])
{
	int i,j;
	srand(time(NULL));
	for(i=0; i<n; i++)
	    for(j=0; j<n; j++)
	        *(*(pArray+i)+j) = rand()%20+10;
}
//界面输出 
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
//系统随机产生给定数字 
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
 //判断输入并清除为0
void delteToZero(int n, int (*array)[100], int num)
 {
    int a,b,c,d;
 	printf("请输入您确定的第一个位置坐标(0开始)：");
	while(1) {
		scanf("%d%d", &a, &b);
		if(a > n - 1 || b > n - 1)
			printf("位置超出限制！请重新输入:");
		else{
			if( (*(*(array+a)+b)) == 0)
				printf("该位置数字已经被清除过，请重新输入:");
			else
		    	break;
		}
	}
	printf("请输入您确定的第二个位置坐标(0开始)：");
	while(1) {
		scanf("%d%d", &c, &d);
		if(c > n - 1 || d > n - 1)
			printf("位置超出限制！请重新输入:");
		else{
			if((*(*(array+c)+d)) == 0)
				printf("该位置数字已经被清除过，请重新输入:");
			else
		    	break;
		}
	}
	if( ((*(*(array+a)+b)) + (*(*(array+c)+d))) == num ){
		(*(*(array+a)+b)) = 0;
		(*(*(array+c)+d)) = 0;
		printf("符合题意，清除成功！");
	}
	else
		printf("清除失败.");
	printf("任意键继续!");
	getch();
  } 
 //给定数字并判断游戏是否结束
void gamePlay(int n, int (*array)[100])
 {
 	int num;
 	while(1){
 		system("cls");
		imput(n, array);
 		num = randNum(n, array);
 		if(num == 0){
 			printf("恭喜您，数字全部清除成功！");
 			break;
		}
		if(num == 2){
			printf("由于数字只剩一个，游戏被迫结束！");
			break;
		}
		printf("\n给定数字:%d\n", num);
		delteToZero(n, array, num);
	}
}

void timeCalculate()
{
	int start, end,i = 3;
	int gameArray[100][100],n;
	printf("请输入游戏矩阵阶数: ");
	scanf("%d", &n);
	while(i > 0){
		system("cls");
		printf("\n\t游戏即将开始！%d\n", i);
		Sleep(1000);
		i--;
	}
	start = clock();
	creatGameArray(n, gameArray);
	gamePlay(n, gameArray);
	end = clock();
	printf("\n  完成游戏时间为 %d s", (end - start)/1000);
}
int main()
{
	timeCalculate();
	return 0;
}











