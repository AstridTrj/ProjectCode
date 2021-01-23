#include<stdio.h>
#include<time.h>
#include<string.h>
#include<signal.h>
#include<windows.h>
#include<unistd.h>
#include<conio.h>
#include<malloc.h>

#define DATA_BLOCK 10240 //可根据磁盘进行设置 此为20个512block处
#define DISK_START 0
#define BLOCK_BITMAP 512
#define INODE_BITMAP 1024
#define INODE_TABLE 1536
#define BLOCK_SIZE 512
#define INODE_SIZE 64

//用户信息定义 
struct user{
	//用户名 
	char username[8];
	//密码 
	char password[8];
	//权限 
	int auth;
};

struct group_desc{
	//组名 
    char bg_volume_name[16];
    //保存块位图的起始块号 
    unsigned short bg_block_bitmap;
    //保存索引结点位图的起始块号 
    unsigned short bg_inode_bitmap;
    //索引结点表的起始块号
    unsigned short bg_inode_table;
    //本组空闲块的个数
    unsigned short bg_free_blocks_count;
    //本组空闲索引结点的个数
    unsigned short bg_free_inodes_count;
    //本组目录的个数
    //unsigned short bg_used_dirs_count;
};
struct inode{
	//文件类型，访问权限
    unsigned short i_mode;
    //文件的数据块个数
    unsigned short i_blocks;
    //当前目录下所有文件的目录项大小之和 
    unsigned long i_size;
    //指向数据块的指针，默认有8个数据块 
    unsigned short i_block[8];
};
//目录项结构
struct dir_entry{
	//索引节点号
    unsigned short inode;
    //目录项长度
    unsigned short rec_len;
    //文件名长度
    unsigned short name_len; 
    //文件类型(1: 普通文件， 2: 目录.. )
    char file_type;
    //文件名 
    char name[9];
};

//针对数据块的缓冲区
char Buffer[512];
//临时输入数据存储 
char tempbuf[4097];
//位图缓冲区，包括块位图和索引位图 
unsigned char bitbuf[512];
unsigned short index_buf[256];
//文件打开情况表
short fopen_table[16];
//最近分配的节点号
unsigned short last_alloc_inode;
//最近分配的数据块号
unsigned short last_alloc_block;
//当前目录的节点号
unsigned short current_dir;
//组描述符缓冲区，相当于GDT
struct group_desc super_block[1];
//节点缓冲区
struct inode inode_area[1];
//目录项缓冲区
struct dir_entry dir[32];
//当前路径名方便展示 
char current_path[256];
//当前路径长度 
unsigned short current_dirlen;
//当前用户 
struct user cur_user;
//所有用户 
struct user users[16];
//已注册用户的信息 
int cur_user_num = 1;
int cur_index = 0;
//文件指针，用于模拟文件数据写入读出磁盘 
FILE *fp;

//更新组描述符到 super_block缓冲区，需指定规定的磁盘上的起始位置 
void update_group_desc(){
    fseek(fp, DISK_START, SEEK_SET);
    fwrite(super_block, BLOCK_SIZE, 1, fp);
}
//读入组描述符到 super_block
void reload_group_desc(){
    fseek(fp, DISK_START, SEEK_SET);
    fread(super_block, BLOCK_SIZE, 1, fp);
}
//更新inode位图到bitbuf缓冲区 
void update_inode_bitmap(){
    fseek(fp, INODE_BITMAP, SEEK_SET);
    fwrite(bitbuf, BLOCK_SIZE, 1, fp);
}
//读入inode位图 到 bitbuf
void reload_inode_bitmap(){
    fseek(fp, INODE_BITMAP, SEEK_SET);
    fread(bitbuf, BLOCK_SIZE, 1, fp);
}
//更新block位图到 bitbuf
void update_block_bitmap(){
    fseek(fp, BLOCK_BITMAP, SEEK_SET);
    fwrite(bitbuf, BLOCK_SIZE, 1, fp);
}
//读入block位图到 bitbuf 
void reload_block_bitmap(){
    fseek(fp, BLOCK_BITMAP, SEEK_SET);
    fread(bitbuf, BLOCK_SIZE, 1, fp);
}
//更新第i个inode入口
void update_inode_entry(unsigned short i){
    fseek(fp, INODE_TABLE+(i-1)*INODE_SIZE, SEEK_SET);
    fwrite(inode_area, INODE_SIZE, 1, fp);
}
//读入第i个inode入口
void reload_inode_entry(unsigned short i){
    fseek(fp, INODE_TABLE+(i-1)*INODE_SIZE, SEEK_SET);
    fread(inode_area, INODE_SIZE, 1, fp);
}
//当前需要访问的是目录时，读入含有第i个目录信息的数据块
void reload_dir(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fread(dir, BLOCK_SIZE, 1, fp);
}
//更新第i个目录的数据块 
void update_dir(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fwrite(dir, BLOCK_SIZE, 1, fp);
}
//读入第i个数据块 
void reload_block(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fread(Buffer, BLOCK_SIZE, 1, fp);
}
//更新第i个数据块
void update_block(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fwrite(Buffer, BLOCK_SIZE, 1, fp);
}
//分配一个数据块,返回数据块号; 
int alloc_block(){
	//从上一次分配的位置开始分配，可避免较多的碎片产生 
    unsigned short cur = last_alloc_block;
    //用于计算当前行的哪一块数据 
    unsigned char con = 128;
    int flag = 0;
    if(super_block[0].bg_free_blocks_count == 0){
        printf("There is no block to be alloced!\n");
        return 0;
    }
    reload_block_bitmap();
    cur = cur / 8;
    //当前行已满 
    while(bitbuf[cur] == 255){
    	//超过最大值，循环查找 
        if(cur == 511) cur = 0;
        else cur++;
    }
    //找到空行位置cur时，再确定位于该行何处 
    while(bitbuf[cur]&con){
        con = con/2;
        flag++;
    }
    //更新当前行位图信息 
    bitbuf[cur] = bitbuf[cur] + con;
    //记录当前分配块号 
    last_alloc_block = cur*8 + flag;
    //更新位图信息和组描述符信息 
    update_block_bitmap();
    super_block[0].bg_free_blocks_count--;
    update_group_desc();
    return last_alloc_block;
}
//清除一个block
void remove_block(unsigned short del_num){
    unsigned short tmp;
    tmp = del_num / 8;
    reload_block_bitmap();
    //更改block位图 ，找到对应行对应列位置 
    switch(del_num % 8){
        case 0: bitbuf[tmp] = bitbuf[tmp]&127; break;
        case 1: bitbuf[tmp] = bitbuf[tmp]&191; break;
        case 2: bitbuf[tmp] = bitbuf[tmp]&223; break;
        case 3: bitbuf[tmp] = bitbuf[tmp]&239; break;
        case 4: bitbuf[tmp] = bitbuf[tmp]&247; break;
        case 5: bitbuf[tmp] = bitbuf[tmp]&251; break;
        case 6: bitbuf[tmp] = bitbuf[tmp]&253; break;
        case 7: bitbuf[tmp] = bitbuf[tmp]&254; break;
    }
    //更新位图和组描述符信息 
    update_block_bitmap();
    super_block[0].bg_free_blocks_count++;
    update_group_desc();
}
//分配一个inode,返回序号 
int get_inode(){
    unsigned short cur = last_alloc_inode;
    unsigned char con = 128;
    int flag = 0;
    if(super_block[0].bg_free_inodes_count == 0){
        printf("There is no Inode to be alloced!\n");
        return 0;
    }
    reload_inode_bitmap();
    //由于初始化时cur是从1开始，故需减1 
    cur = (cur-1) / 8;
    //寻找空闲区 
    while(bitbuf[cur] == 255){
        if(cur == 511) cur = 0;
        else cur++;
    }
    while(bitbuf[cur]&con){
        con = con / 2;
        flag++;
    }
    //分配并修改位图和组描述符信息 
    bitbuf[cur] = bitbuf[cur] + con;
    last_alloc_inode = cur*8 + flag+1;
    update_inode_bitmap();
    super_block[0].bg_free_inodes_count--;
    update_group_desc();
    return last_alloc_inode;
}
//清除一个inode 
void remove_inode(unsigned short del_num){
    unsigned short tmp;
    tmp = (del_num-1) / 8;
    reload_inode_bitmap();
    //更改block位图
    switch((del_num-1) % 8){
        case 0: bitbuf[tmp] = bitbuf[tmp]&127; break;
        case 1: bitbuf[tmp] = bitbuf[tmp]&191; break;
        case 2: bitbuf[tmp] = bitbuf[tmp]&223; break;
        case 3: bitbuf[tmp] = bitbuf[tmp]&239; break;
        case 4: bitbuf[tmp] = bitbuf[tmp]&247; break;
        case 5: bitbuf[tmp] = bitbuf[tmp]&251; break;
        case 6: bitbuf[tmp] = bitbuf[tmp]&253; break;
        case 7: bitbuf[tmp] = bitbuf[tmp]&254; break;
    }
    update_inode_bitmap();
    super_block[0].bg_free_inodes_count++;
    update_group_desc();
}

//新目录和文件初始化.and ..
void dir_prepare(unsigned short tmp,unsigned short len,int type){
    reload_inode_entry(tmp);//得到新目录的节点入口地址
    //目录文件 
    if(type == 2){
        inode_area[0].i_size = 32;
        inode_area[0].i_blocks = 1;
        inode_area[0].i_block[0] = alloc_block();
        dir[0].inode = tmp;
        dir[1].inode = current_dir;
        dir[0].name_len = len;
        dir[1].name_len = current_dirlen;
        dir[0].file_type = dir[1].file_type = 2;
        //.和..已经设置，故从2开始 
        for(type = 2; type < 32; type++)
            dir[type].inode = 0;
        strcpy(dir[0].name, ".");
        strcpy(dir[1].name, "..");
        //1 111，drwx目录
        inode_area[0].i_mode = 7;
        //更新到对应数据块 
        update_dir(inode_area[0].i_block[0]);
    }
    else{
        inode_area[0].i_size = 0;
        inode_area[0].i_blocks = 0;
        //0111-rwx文件
        inode_area[0].i_mode = 4;
    }
    update_inode_entry(tmp);
}

//查找文件的inode节点号，所在目录节点的数据块号（0~7）、目录项所在号
unsigned short reserch_file(char tmp[9],int file_type,unsigned short *inode_num,unsigned short *block_num,unsigned short *dir_num){
    unsigned short j, k;
    reload_inode_entry(current_dir);
    j = 0;
    while(j < inode_area[0].i_blocks){
    	//数据块大小为512，此时目录缓冲区最多可放32个文件项，默认每个文件大小16 
        reload_dir(inode_area[0].i_block[j]);
        k = 0;
        while(k < 32){
            if(!dir[k].inode || dir[k].file_type!=file_type || strcmp(dir[k].name,tmp))
				k++;
            else{
                *inode_num = dir[k].inode;
                *block_num = j;
                *dir_num = k;
                return 1;
            }
        }
        j++;
    }
    return 0;
}

//目录移动 
void cd(char tmp[9]){
    unsigned short i, j, k, flag;
    flag = reserch_file(tmp, 2, &i, &j, &k);
    if(flag){
        current_dir = i;
        //根目录的namelen为0,此时不能再继续向上回退,若当前的命令为cd ..
		//则dir[k-1]为目录.，即当前目录，故使用cd ..需保证当前目录长度不为0 
        if(!strcmp(tmp, "..") && dir[k-1].name_len){
            current_path[strlen(current_path)-dir[k-1].name_len-1] = '\0';
            current_dirlen = dir[k].name_len;
        }
        //若cd到当前目录则不作操作 
        else if(!strcmp(tmp,"."));
        //cd到其他路径 
        else if(strcmp(tmp,"..")){
            current_dirlen=strlen(tmp);
            strcat(current_path,tmp);
            strcat(current_path,"/");
        }
    }
    else printf("The directory %s not exists!\n", tmp);
}
//删除文件 
void del(char tmp[9])
{
    unsigned short i, j, k, m, n, flag;
    m = 0;
    //找到要删除文件的inode号 
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
        flag = 0;
        //查看文件是否打开 
        while(fopen_table[flag] != dir[k].inode && flag < 16)
			flag++;
		//若文件打开，则将其清除 
        if(flag < 16)
			fopen_table[flag] = 0;
		//加载需要删除的文件的inode号 
        reload_inode_entry(i);
        //依次清空数据区并初始化其参数 
        while(m < inode_area[0].i_blocks)
			remove_block(inode_area[0].i_block[m++]);
        inode_area[0].i_blocks = 0;
        inode_area[0].i_size = 0;
        remove_inode(i);
        //对文件所在的当前目录进行修改
        reload_inode_entry(current_dir);
        //重置当前目录缓冲区中的信息并更新 
        dir[k].inode = 0;
        update_dir(inode_area[0].i_block[j]);
        //一个文件的默认大小为16，故size减去16 
        inode_area[0].i_size -= 16;
        m = 1;
        //对当前目录项进行修改 
        while(m < inode_area[i].i_blocks){
            flag = n = 0;
            reload_dir(inode_area[0].i_block[m]);
            while(n < 32){
                if(!dir[n].inode)
					flag++;
                n++;
            }
            //当flag为32时，说明该数据块已经全空，则进行删除 
            if(flag == 32){
                remove_block(inode_area[i].i_block[m]);
                inode_area[i].i_blocks--;
                //当几个数据块中间被删除时，将后面的数据块向前移动 
                while(m < inode_area[i].i_blocks)
					inode_area[i].i_block[m] = inode_area[i].i_block[++m];
            }
        }
        update_inode_entry(current_dir);
    }
    else printf("The file %s not exists!\n",tmp);
}
//新建目录文件 
void mkdir(char tmp[9], int type)
{
    unsigned short tmpno, i, j, k, flag;
    //将当前目录的索引节点加载到缓冲区inode_area中
    reload_inode_entry(current_dir);
    if(!reserch_file(tmp, type, &i, &j, &k)){
    	//目录项限制空间已满
        if(inode_area[0].i_size == 4096){
            printf("Directory has no room to be alloced!\n");
            return;
        }
        flag = 1;
        //目录中已有数据块有某些块中32个项未满
        if(inode_area[0].i_size != inode_area[0].i_blocks*512){
            i = 0;
            while(flag && i < inode_area[0].i_blocks){
                reload_dir(inode_area[0].i_block[i]);
                j = 0;
                //找到空闲位置区 
                while(j < 32){
                    if(dir[j].inode == 0){
                        flag = 0;
                        break;
                    }
                    j++;
                }
                i++;
            }
            //此时j所在的位置即新建目录文件的位置
			//对其申请一个inode号，tmpno记录申请的inode号 
            tmpno = dir[j].inode = get_inode();
            dir[j].name_len = strlen(tmp);
            dir[j].file_type = type;
            strcpy(dir[j].name, tmp);
            update_dir(inode_area[0].i_block[i-1]);
        }
        //全满，则重新申请一个数据块 
        else{
        	//分配一个数据块并做相应设置 
            inode_area[0].i_block[inode_area[0].i_blocks] = alloc_block();
            inode_area[0].i_blocks++;
            //对新建的目录文件进行初始化 
            reload_dir(inode_area[0].i_block[inode_area[0].i_blocks-1]);
            tmpno = dir[0].inode = get_inode();
            dir[0].name_len = strlen(tmp);
            dir[0].file_type = type;
            strcpy(dir[0].name, tmp);
            //初始化新块
            for(flag = 1; flag < 32; flag++)
				dir[flag].inode = 0;
            update_dir(inode_area[0].i_block[inode_area[0].i_blocks-1]);
        }
        //新建一个文件后更新size 
        inode_area[0].i_size += 16;
        update_inode_entry(current_dir);
        //初始化目录，即设置.和..目录 
        dir_prepare(tmpno, strlen(tmp), type);
    }
    //已经存在同名文件或目录
    else{
        if(type == 1)
			printf("File has already existed!\n");
        else printf("Directory has already existed!\n");
    }
}
// 删除目录文件 
void rmdir(char tmp[9]){
    unsigned short i, j, k, flag;
    unsigned short m, n;
    //.和..目录不能删除 
    if(!strcmp(tmp, "..") || !strcmp(tmp,".")){
        printf("The directory can not be deleted!\n");
        return;
    }
    flag = reserch_file(tmp, 2, &i, &j, &k);
    if(flag){
    	//找到要删除的目录的节点并加载到缓冲区 
        reload_inode_entry(dir[k].inode);
        //大小为32，则只有.and ..
        if(inode_area[0].i_size == 32){
            inode_area[0].i_size = 0;
            inode_area[0].i_blocks = 0;
            //此时只有一个数据块，即初始化的一个数据块 
            remove_block(inode_area[0].i_block[0]);
            //得到当前目录的inode号并更改当前目录项
            reload_inode_entry(current_dir);
            remove_inode(dir[k].inode);
            dir[k].inode = 0;
            update_dir(inode_area[0].i_block[j]);
            inode_area[0].i_size -= 16;
            flag = 0;
            m = 1;
            //对当前目录项进行修改 
            while(flag < 32 && m < inode_area[0].i_blocks){
                flag = n = 0;
                reload_dir(inode_area[0].i_block[m]);
                while(n < 32){
                	//若n所在目录项的内容为空则flag++ 
                    if(!dir[n].inode)
						flag++;
                    n++;
                }
                //当flag为32时，说明该数据块已经全空，则进行删除 
                if(flag == 32){
                    remove_block(inode_area[0].i_block[m]);
                    inode_area[0].i_blocks--;
                    //删除之后，对已存在的数据块进行合并移动 
                    while(m < inode_area[0].i_blocks)
						inode_area[0].i_block[m] = inode_area[0].i_block[++m];
                }
            }
        update_inode_entry(current_dir);
        }
        else printf("Directory is not null!\n");
    }
    else printf("Directory to be deleted not exists!\n");
}
//列出当前目录内容 
void ls()
{
    printf("items           type           mode            size\n");
    unsigned short i, j, k, tmpno, no;
    i = 0;
    //加载当前目录的inode号 
    reload_inode_entry(current_dir);
    //依次访问当前目录的数据块 
    while(i < inode_area[0].i_blocks){
        k = 0;
        //加载到目录文件缓冲区 
        reload_dir(inode_area[0].i_block[i]);
        while(k < 32){
        	//当前目录文件存在inode号，即存在该文件 
            if(dir[k].inode){
                printf("%s", dir[k].name);
                //若为目录文件 
                if(dir[k].file_type == 2){
                    j = 0;
                    //加载出该文件的inode号，得到其文件信息
                    reload_inode_entry(dir[k].inode);
                    if(!strcmp(dir[k].name, ".."))
						printf("             ");
                    else if(!strcmp(dir[k].name, "."))
						printf("              ");
                    else while(j++ < 15-dir[k].name_len)
							printf(" ");
                    printf("<DIR>          ");
                    //判断当前用户的权限 
                    switch(cur_user.auth&7){
                        case 1: printf("d--x"); break;
                        case 2: printf("d-w-"); break;
                        case 3: printf("d-wx"); break;
                        case 4: printf("dr--"); break;
                        case 5: printf("dr-x"); break;
                        case 6: printf("drw-"); break;
                        case 7: printf("drwx"); break;
                    }
                    printf("            ----");
                }
                //普通文件 
                else if(dir[k].file_type == 1){
                    j = 0;
                    reload_inode_entry(dir[k].inode);
                    while(j++ < 15-dir[k].name_len)
						printf(" ");
                    printf("<FILE>         ");
                    switch(cur_user.auth&7){
                        case 1: printf("---x"); break;
                        case 2: printf("--w-"); break;
                        case 3: printf("--wx"); break;
                        case 4: printf("-r--"); break;
                        case 5: printf("-r-x"); break;
                        case 6: printf("-rw-"); break;
                        case 7: printf("-rwx"); break;
                    }
                    printf("            %d bytes     ", inode_area[0].i_size);
                }
                printf("\n");
            }
            k++;
            reload_inode_entry(current_dir);
        }
        i++;
    }
}

//在打开文件表中查找是否已打开文件
unsigned short search_file(unsigned short Ino)
{
    unsigned short fopen_table_point = 0;
    while(fopen_table_point < 16 && fopen_table[fopen_table_point++] != Ino);
    if(fopen_table_point == 16)
		return 0;
    return 1;
}
//读文件
void read_file(char tmp[9])
{
    unsigned short flag, i, j, k;
    //找到文件目录项的inode号 
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//文件已打开 
        if(search_file(dir[k].inode)){
            reload_inode_entry(dir[k].inode);
            //auth:111b:读,写,执行
            if(!(cur_user.auth&4)){
                printf("The file %s can not be read!\n", tmp);
                return;
            }
            //循环读取已有数据块
            for(flag = 0; flag < inode_area[0].i_blocks; flag++){
                reload_block(inode_area[0].i_block[flag]);
                Buffer[512] = '\0';
                printf("%s", Buffer);
            }
            //flag未加，表示无数据 
            if(flag == 0)
				printf("The file %s is empty!\n", tmp);
            else
				printf("\n");
        }
        else
			printf("The file %s has not been opened!\n", tmp);
    }
    else
		printf("The file %s not exists!\n", tmp);
}

//写文件
void write_file(char tmp[9])
{
    unsigned short flag, i, j, k, size = 0, need_blocks;
    //找到对应文件的目录项信息，flag为1表示找到，否则未找到 
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//判断是否打开 
        if(search_file(dir[k].inode)){
        	//找到索引节点号 
            reload_inode_entry(dir[k].inode);
            //判断权限是否可写 
            if(!(cur_user.auth&2)){
                printf("The file %s can not be writed!\n", tmp);
                return;
            }
            while(1){
                tempbuf[size] = getchar();
                //以#号结束文件输入 
                if(tempbuf[size] == '#'){
                    tempbuf[size] = '\0';
                    break;
                }
                //超过文件最大长度 
                if(size >= 4096){
                    printf("Sorry,the max size of a file is 4KB!\n");
                    tempbuf[size] = '\0';
                    break;
                }
                size++;
            }
            //需要的数据块数量 
            need_blocks = strlen(tempbuf)/512;
            //还有未满512的部分需再申请一块空间 
            if(strlen(tempbuf)%512)
				need_blocks++;
			//最多8块，共4KB 
            if(need_blocks < 9){
            	//分配需要的数据块 
                while(inode_area[0].i_blocks < need_blocks){
                    inode_area[0].i_block[inode_area[0].i_blocks] = alloc_block();
                    inode_area[0].i_blocks++;
                }
                j = 0;
                while(j < need_blocks){
                    if(j != need_blocks-1){
                    	//加载出数据块位图信息 
                        reload_block(inode_area[0].i_block[j]);
                        memcpy(Buffer, tempbuf+j*BLOCK_SIZE, BLOCK_SIZE);
                        //更新位图信息 
                        update_block(inode_area[0].i_block[j]);
                    }
                    //最后一块需要单独判断
                    else{
                        reload_block(inode_area[0].i_block[j]);
                        memcpy(Buffer, tempbuf+j*BLOCK_SIZE, strlen(tempbuf)-j*BLOCK_SIZE);
                        //若有多余部分，需要对size重新设置 
                        if(strlen(tempbuf) > inode_area[0].i_size){
                            Buffer[strlen(tempbuf)-j*BLOCK_SIZE] = '\0';
                            inode_area[0].i_size = strlen(tempbuf);
                        }
                        update_block(inode_area[0].i_block[j]);
                    }
                    j++;
                }
                update_inode_entry(dir[k].inode);
            }
            else printf("Sorry,the max size of a file is 4KB!\n");
        }
        else printf("The file %s has not opened!\n",tmp);
    }
    else printf("The file %s does not exist!\n",tmp);
}
//关闭文件
void close_file(char tmp[9])
{
    unsigned short flag, i, j, k;
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//文件已打开，此时才可执行关闭操作 
        if(search_file(dir[k].inode)){
            flag = 0;
            while(fopen_table[flag] != dir[k].inode)
				flag++;
            fopen_table[flag] = 0;
            printf("File: %s! closed\n", tmp);
        }
        else printf("The file %s has not been opened!\n", tmp);
    }
    else printf("The file %s does not exist!\n", tmp);
}
//打开文件 
void open_file(char tmp[9])
{
    unsigned short flag, i, j, k;
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//判断文件是否已经打开，若已经打开则不再进行操作 
        if(search_file(dir[k].inode))
			printf("The file %s has opened!\n", tmp);
        else {
        	//未打开时，在文件打开表中做相应记录 
            flag = 0;
            while(fopen_table[flag])
				flag++;
            fopen_table[flag] = dir[k].inode;
            printf("File %s! opened\n", tmp);
        }
    }
    else printf("The file %s does not exist!\n", tmp);
}
//初始化磁盘 
void initialize_disk()
{
    int i = 0;
    printf("Creating the ext2 file system\n");
    printf("Please wait ");
    while(i < 1){
        printf("... ");
        sleep(1);
        i++;
    }
    printf("\n");
    cur_user = users[cur_index];
    strcpy(current_path, "[");
    strcat(current_path, cur_user.username);
    strcat(current_path, "@localhost/");
    //初始化上一次分配时的inode和block号 
    last_alloc_inode = 1;
    last_alloc_block = 0;
    //清空文件打开表 
    for(i = 0; i < 16; i++)
		fopen_table[i] = 0;
	// 清空缓冲区，通过缓冲区清空文件，即模拟清空磁盘
    for(i = 0; i < BLOCK_SIZE; i++)
		Buffer[i] = 0;
	//磁盘文件 
    fp = fopen("FS_zqw_zzw.txt", "w+b");
    fseek(fp, DISK_START, SEEK_SET);
    //清空文件，即清空磁盘全部用0填充
    for(i = 0; i < 4096; i++) 
		fwrite(Buffer, BLOCK_SIZE, 1, fp);
	//加载出并初始化组信息，inode以及目录项信息 
    reload_group_desc();
    reload_inode_entry(1);
    reload_dir(0);
    
	//改组名，并初始化组描述符内容
    strcpy(super_block[0].bg_volume_name, "MYSYSTEM");
    //初始化组描述符内容
    super_block[0].bg_block_bitmap = BLOCK_BITMAP;
    super_block[0].bg_inode_bitmap = INODE_BITMAP;
    super_block[0].bg_inode_table = INODE_TABLE;
    super_block[0].bg_free_blocks_count = 4096;
    super_block[0].bg_free_inodes_count = 4096;
    //super_block[0].bg_used_dirs_count = 0;
    //更新组描述符内容
    update_group_desc();
    //载入位图信息 
    reload_block_bitmap();
    reload_inode_bitmap();
    //初始化位图信息 
    inode_area[0].i_mode = 7;
    inode_area[0].i_blocks = 0;
    inode_area[0].i_size = 32;
    inode_area[0].i_block[0] = alloc_block();
    inode_area[0].i_blocks++;
    //申请一个inode号 
    current_dir = get_inode();
    update_inode_entry(current_dir);
    //初始化.和..目录 
    dir[0].inode = dir[1].inode = current_dir;
    dir[0].name_len = 0;
    dir[1].name_len = 0;
    //1:文件;2:目录
    dir[0].file_type = dir[1].file_type = 2; 
    strcpy(dir[0].name, ".");
    strcpy(dir[1].name, "..");
    update_dir(inode_area[0].i_block[0]);
    printf("The ext2 file system has been installed!\n");
}
//初始化内存，进一步清空文件 
void initialize_memory()
{
    cur_user = users[cur_index];
    printf("%s\n", cur_user.username);
    int i = 0;
    last_alloc_inode = 1;
    last_alloc_block = 0;
    for(i = 0; i < 16; i++)
		fopen_table[i] = 0;
		
    strcpy(current_path, "[");
    strcat(current_path, cur_user.username);
    strcat(current_path, "@localhost/");
    
    current_dir = 1;
    fp = fopen("FS_zqw_zzw.txt","r+b");
    if(fp == NULL)
    {
        printf("The File system does not exist!\n");
        initialize_disk();
        return ;
    }
    reload_group_desc();
}
int format()
{
	if(cur_index != 0){
		printf("您无权格式化！");
		return 0;
	}
    initialize_disk();
    initialize_memory();
    return 0;
}
    
void help()
{
    printf("   ***************************************************************************\n");
    printf("   *                            虚拟文件系统帮助                             *\n");
    printf("   *                                                                         *\n");
    printf("   *     1.移动目录   : cd+dir_name          8.新建目录  : mkdir+dir_name    *\n");
    printf("   *     2.新建文件   : mkf+file_name        9.删除目录  : rmdir+dir_name    *\n");
    printf("   *     3.删除文件   : rm+file_name         10.读文件   : read+file_name    *\n");
    printf("   *     4.打开文件   : open+file_name       11.写文件   : write+file_name   *\n");
    printf("   *     5.关闭文件   : close+file_name      12.退出     : quit              *\n");
    printf("   *     6.目录内容   : ls                   13.帮助     : help              *\n");
    printf("   *     7.格式化     : format               14.注销当前用户 ：logout        *\n");
    printf("   ***************************************************************************\n");
}
//用户登录 
int login(){
	struct user cur;
	char ch;
	int i = 0, j = 0;
	printf("用户名：");
	scanf("%s", cur.username);
	printf("密码：");
	while((ch = getch()) != '\r'){
		cur.password[i++] = ch;
	}
	cur.password[i] = '\0';
	printf("\n");
	//查找是否有登录用户的信息，有则登录，没有则无此用户 
	while(j < cur_user_num){
		if(!strcmp(users[j].username, cur.username) && !strcmp(users[j].password, cur.password)){
			cur_index = j;
			cur_user = users[cur_index];
			return 1;
		}
		j++;
	}
	return 0;
}
//用户注册 
void reg(){
	char ch;
	int i = 0;
	printf("新用户名：");
	scanf("%s", cur_user.username);
	printf("新密码：");
	while((ch = getch()) != '\r'){
		cur_user.password[i++] = ch;
	}
	cur_user.password[i] = '\0';
	//默认权限为只读 
	cur_user.auth = 4;
	printf("\n");
	users[cur_user_num++] = cur_user;
}
//用户注销 
void logout(char str[8]){
	struct user u;
	strcpy(u.username, str);
	char ch;
	int i = 0, j;
	printf("密码确认：");
	while((ch = getch()) != '\r'){
		u.password[i++] = ch;
	}
	u.password[i] = '\0';
	printf("\n");
	for(i = 0; i < cur_user_num; i++){
		if(!strcmp(users[i].username, u.username)){
			for(j = i; j < cur_user_num - 1; j++)
				users[j] = users[j+1];
			cur_user_num--;
		}
	}
}

int enter(){
	strcpy(users[0].username, "root");
	strcpy(users[0].password, "root");
	users[0].auth = 7;
	int ck, flag = 0;
	while(1){
		flag = 0;
		printf("\n     ****************************\n");
		printf("     *        1.用户登录        *\n");
		printf("     *        2.用户注册        *\n");
		printf("     *        3.退出            *\n");
		printf("     ****************************\n");
		scanf("%d", &ck);
		switch(ck){
			case 1: flag = login(); break;
			case 2: reg(); break;
			case 3: return 1; 
		}
		if(ck == 1){
			if(flag == 1) break;
			else
				printf("用户名或密码输入错误，请重新输入\n");
		}
	}
	return 0;
} 

/***************************main********************************/
int main()
{
	int flag = 0;
    char command[10],temp[9];
    while(1){
    	system("cls");
	    flag = enter();
	    if(flag == 1)
	    	break;
	    initialize_memory();
	    system("cls");
	    while(1){
	        printf("%s]#", current_path);
	        scanf("%s", command);
	        if(!strcmp(command, "cd")){
	            scanf("%s", temp);
	            cd(temp);
	        }
	        else if(!strcmp(command, "mkdir")){
	            scanf("%s", temp);
	            mkdir(temp, 2);
	        }
	        else if(!strcmp(command, "mkf")){
	            scanf("%s", temp);
	            mkdir(temp, 1);
	        }
	        else if(!strcmp(command, "rmdir")){
	            scanf("%s", temp);
	            rmdir(temp);
	        }
	        else if(!strcmp(command, "rm")){
	            scanf("%s", temp);
	            del(temp);
	        }
	        else if(!strcmp(command, "open")){
	            scanf("%s", temp);
	            open_file(temp);
	        }
	        else if(!strcmp(command, "close")){
	            scanf("%s", temp);
	            close_file(temp);
	        }
	        else if(!strcmp(command, "read")){
	            scanf("%s", temp);
	            read_file(temp);
	        }
	        else if(!strcmp(command, "write")){
	            scanf("%s", temp);
	            write_file(temp);
	        }
	        else if(!strcmp(command, "ls"))
					ls();
	        else if(!strcmp(command, "format")){
	            char tempch;
	            printf("Format will erase all the data in the Disk\n");
	            printf("Are you sure?y/n:\n");
	            scanf(" %c", &tempch);
	            if(tempch == 'Y' || tempch == 'y'){
	                fclose(fp);
	                initialize_disk();
	            }
	            else
	            printf("Format Disk canceled\n");
	        }
	
	        else if(!strcmp(command, "help"))
					help();
	        else if(!strcmp(command, "logout")){
	        	logout(cur_user.username);
				break;
			}
	        else if(!strcmp(command,"quit")) 
					break;
	        else printf("No this Command,Please check!\n");
	    }
	}
    return 0;
}

