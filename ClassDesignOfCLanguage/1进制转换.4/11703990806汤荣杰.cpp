#include<stdio.h>
#include<string.h> 
#include<math.h>
#include<stdlib.h>
#include<time.h>
#include<conio.h>
#include<windows.h>



//字体颜色+背景颜色
void setColor(unsigned short ForeColor=7,unsigned short BackGroundColor=0)
{
	HANDLE handle=GetStdHandle(STD_OUTPUT_HANDLE);//获取当前窗口句柄
	SetConsoleTextAttribute(handle,ForeColor+BackGroundColor*0x10);//设置颜色
}

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
//中途转换为十进制 
int changeToTen(char a[], int m)
{
	unsigned int i;
	int sum=0;
	for(i=0;i<strlen(a);i++)
	{
		if(a[i]>='0'&&a[i]<='9')
		    sum+=(int)((a[i]-48)*pow(m,strlen(a)-1-i));
		else
		    sum+=(int)((a[i]-'A'+10)*pow(m,strlen(a)-1-i));
	    
	}
	return sum;
}
//转二进制
void toTwo()
{
	system("cls");
	int n,i=0,x,y=0,m,j=0,num;
	char a[100];
	printf("\n      请输入数字及进制(回车确定)：");
	while (1)
	{
		num = 0;
		n = 0;
		j = 0;
		scanf("%s%d",a,&m);
		while(a[j] != '\0')
		{
			if(n < a[j] )
				n = (int)a[j];
			if( (a[j] >= 'A' && a[j] <= 'Z') || (a[j] >= '0' && a[j] <= '9') );
			else
				num++;
			j++;
		}
		if(num != 0)
			printf("      输入非法，请重新输入:");
		else{
			if( (n - 48) >= m)
				printf("      输入数字与进制矛盾，请重新输入:");
			else
				break;
		}
	}
	n=changeToTen(a,m);
	getchar();
	printf("      对应的二进制数为：");
	a[0]=0;
	x=n;
    while(x!=0)
    {
    	a[++i]=x%2;
    	x=x/2;
	}
	while(i)
	{
		if(a[i]>=10)
		    printf("%c",'A'+(a[i--]-10));
		else
		    printf("%c",'0'+a[i--]);
	}
	Sleep(1000);
	printf("\n      任意键继续");
	getch(); 
} 
//转八进制 
void toEight()
{
	system("cls");
	int n,i=0,x,y=0,m, j, num;
	char a[100];
	printf("\n      请输入数字及进制(回车确定)：");
	while (1)
	{
		num = 0;
		n = 0;
		j = 0;
		scanf("%s%d",a,&m);
		while(a[j] != '\0')
		{
			if(n < a[j] )
				n = (int)a[j];
			if( (a[j] >= 'A' && a[j] <= 'Z') || (a[j] >= '0' && a[j] <= '9') );
			else
				num++;
			j++;
		}
		if(num != 0)
			printf("      输入非法，请重新输入:");
		else{
			if( (n - 48) >= m)
				printf("      输入数字与进制矛盾，请重新输入:");
			else
				break;
		}
	}
	n=changeToTen(a,m);
	getchar();
	printf("      对应的八进制数为：");
	a[0]=0; 
	x=n;
    while(x!=0)
    {
    	a[++i]=x%8;
    	x=x/8; 
	}
	while(i)
	{
		if(a[i]>=10)
		    printf("%c",'A'+(a[i--]-10));
		else
		    printf("%c",'0'+a[i--]);
	}
	Sleep(1000);
	printf("\n      任意键继续");
	getch();
}
//转十进制
void toTen()
{
	system("cls");
	unsigned int i;
	int m,sum=0, j, num, n;
	char a[100];
	printf("\n      请输入数字及进制(回车确定)：");
	while (1)
	{
		num = 0;
		n = 0;
		j = 0;
		scanf("%s%d",a,&m);
		while(a[j] != '\0')
		{
			if(n < a[j] )
				n = (int)a[j];
			if( (a[j] >= 'A' && a[j] <= 'Z') || (a[j] >= '0' && a[j] <= '9') );
			else
				num++;
			j++;
		}
		if(num != 0)
			printf("      输入非法，请重新输入:");
		else{
			if( (n - 48) >= m)
				printf("      输入数字与进制矛盾，请重新输入:");
			else
				break;
		}
	}
	getchar();
	printf("      对应的十进制数为：");
	for(i=0;i<strlen(a);i++)
	{
		if(a[i]>='0'&&a[i]<='9')
		    sum+=(int)((a[i]-48)*pow(m,strlen(a)-1-i));
		else
		    sum+=(int)((a[i]-'A'+10)*pow(m,strlen(a)-1-i));
	    
	}
	printf("%d ",sum);
	Sleep(1000);
	printf("\n      任意键继续");
	getch();
}
//转十六进制 
void toSixteen()
{
	system("cls"); 
	int n,i=0,x,y=0,m,j, num;
	char a[100];
	printf("\n      请输入数字及进制(回车确定)：");
	while (1)
	{
		num = 0;
		n = 0;
		j = 0;
		scanf("%s%d",a,&m);
		while(a[j] != '\0')
		{
			if(n < a[j] )
				n = (int)a[j];
			if( (a[j] >= 'A' && a[j] <= 'Z') || (a[j] >= '0' && a[j] <= '9') );
			else
				num++;
			j++;
		}
		if(num != 0)
			printf("      输入非法，请重新输入:");
		else{
			if( (n - 48) >= m)
				printf("      输入数字与进制矛盾，请重新输入:");
			else
				break;
		}
	}
	n=changeToTen(a,m);
	getchar();
	printf("      对应的十六进制数为：");
	a[0]=0; 
	x=n;
    while(x!=0)
    {
    	a[++i]=x%16;
    	x=x/16; 
	}
	while(i)
	{
		if(a[i]>=10)
		    printf("%c",'A'+(a[i--]-10));
		else
		    printf("%c",'0'+a[i--]);
	}
	Sleep(1000);
	printf("\n      任意键继续");
	getch();
}
//转换为其他进制 
void toOther()
{
	system("cls"); 
	int n,i=0,x,y=0,m,other,j,num;
	char a[100];
	printf("\n      请输入数字,进制及要转化为的进制(回车确定)：");
	while (1)
	{
		num = 0;
		n = 0;
		j = 0;
		scanf("%s%d%d",a,&m,&other);
		while(a[j] != '\0')
		{
			if(n < a[j] )
				n = (int)a[j];
			if( (a[j] >= 'A' && a[j] <= 'Z') || (a[j] >= '0' && a[j] <= '9') );
			else
				num++;
			j++;
		}
		if(num != 0)
			printf("      输入非法，请重新输入:");
		else{
			if( (n - 48) >= m)
				printf("      输入数字与进制矛盾，请重新输入:");
			else
				break;
		}
	}
	n=changeToTen(a,m);
	getchar();
	printf("      对应的%d进制数为：", other);
	a[0]=0; 
	x=n;
    while(x!=0)
    {
    	a[++i]=x%other;
    	x=x/other; 
	}
	while(i)
	{
		if(a[i]>=10)
		    printf("%c",'A'+(a[i--]-10));
		else
		    printf("%c",'0'+a[i--]);
	}
	Sleep(1000);
	printf("\n      任意键继续");
	getch();
}

//改变背景颜色 
void changeColor(const char c[50],int m,int n) {
	if(m==n)
	    setColor(7,12);
	else
		setColor(10,0);
    printf("%s\n", c);
    setColor(7,0);
}
//进制转换菜单 
void changeMenu()
{
	system("cls");
	int i=0,pos=0,m;
	char ch;
	const char *name[5] = {
		"◎.转化为二进制",
		"◎.转化为八进制",
		"◎.转化为十进制",
		"◎.转化为十六进制",
		"◎.其他进制?"
	    };
	while(1) {
		i=0;
		system("cls");
		setColor(10,0);
		printf("********************************\n");	
		while (i < 5) {
			SetPos(8,i+2);
	        changeColor(name[i],i,pos);
	        i++;
	    }
	    setColor(14,0);
	    printf("\n                   -------------\n");
	    printf("                   | ESC--退出 |\n");
	    printf("                   -------------\n");
	    setColor(10,0);
	    printf("********************************\n");
	    setColor(7,0);
	    ch=getch();
	    if(ch==80)//向下 
	        pos++;
	    if(ch==72)//向上
		    pos--;
		if(pos==5)
		    pos=0;
		if(pos==-1)
		    pos=4;
	    if(ch==27)
	        break;
	    if(ch=='\r') {
	    	m=pos;
	    	switch(m) {
	    		case 0: toTwo(); break;
	    		case 1: toEight(); break;
	    		case 2: toTen(); break;
	    		case 3: toSixteen(); break;
	    		case 4: toOther(); break;
			}
		}
	}
} 

//退出操作 
int judgeExit(char str) {
	if(str=='5') {
		system("cls");
		printf("\n  退出成功，感谢使用！\n\n");
		return 1;
	}
	    
	if(str=='0')
		changeMenu();
	return 0;
} 
//主菜单 
int main() {
	int a;
	char ch; 
	while(1) {
		system("cls");
	    setColor(10,0);
		printf("********************************");
		SetPos(11,1);
		printf("进制转换\n");
		printf("--------------------------------");
		SetPos(9,4);
		printf("0.进入菜单");
		SetPos(9,5);
		printf("5.退出操作\n\n");
		printf("********************************\n");
		setColor(7,0);
		fflush(stdin);
		ch=getch();
		a=judgeExit(ch);
		if(a==1)
		    break;
	} 
	return 0;
}






