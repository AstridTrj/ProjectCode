#include<stdio.h>
#include<time.h>
#include<string.h>
#include<signal.h>
#include<windows.h>
#include<unistd.h>
#include<conio.h>
#include<malloc.h>

#define DATA_BLOCK 10240 //�ɸ��ݴ��̽������� ��Ϊ20��512block��
#define DISK_START 0
#define BLOCK_BITMAP 512
#define INODE_BITMAP 1024
#define INODE_TABLE 1536
#define BLOCK_SIZE 512
#define INODE_SIZE 64

//�û���Ϣ���� 
struct user{
	//�û��� 
	char username[8];
	//���� 
	char password[8];
	//Ȩ�� 
	int auth;
};

struct group_desc{
	//���� 
    char bg_volume_name[16];
    //�����λͼ����ʼ��� 
    unsigned short bg_block_bitmap;
    //�����������λͼ����ʼ��� 
    unsigned short bg_inode_bitmap;
    //�����������ʼ���
    unsigned short bg_inode_table;
    //������п�ĸ���
    unsigned short bg_free_blocks_count;
    //��������������ĸ���
    unsigned short bg_free_inodes_count;
    //����Ŀ¼�ĸ���
    //unsigned short bg_used_dirs_count;
};
struct inode{
	//�ļ����ͣ�����Ȩ��
    unsigned short i_mode;
    //�ļ������ݿ����
    unsigned short i_blocks;
    //��ǰĿ¼�������ļ���Ŀ¼���С֮�� 
    unsigned long i_size;
    //ָ�����ݿ��ָ�룬Ĭ����8�����ݿ� 
    unsigned short i_block[8];
};
//Ŀ¼��ṹ
struct dir_entry{
	//�����ڵ��
    unsigned short inode;
    //Ŀ¼���
    unsigned short rec_len;
    //�ļ�������
    unsigned short name_len; 
    //�ļ�����(1: ��ͨ�ļ��� 2: Ŀ¼.. )
    char file_type;
    //�ļ��� 
    char name[9];
};

//������ݿ�Ļ�����
char Buffer[512];
//��ʱ�������ݴ洢 
char tempbuf[4097];
//λͼ��������������λͼ������λͼ 
unsigned char bitbuf[512];
unsigned short index_buf[256];
//�ļ��������
short fopen_table[16];
//�������Ľڵ��
unsigned short last_alloc_inode;
//�����������ݿ��
unsigned short last_alloc_block;
//��ǰĿ¼�Ľڵ��
unsigned short current_dir;
//�����������������൱��GDT
struct group_desc super_block[1];
//�ڵ㻺����
struct inode inode_area[1];
//Ŀ¼�����
struct dir_entry dir[32];
//��ǰ·��������չʾ 
char current_path[256];
//��ǰ·������ 
unsigned short current_dirlen;
//��ǰ�û� 
struct user cur_user;
//�����û� 
struct user users[16];
//��ע���û�����Ϣ 
int cur_user_num = 1;
int cur_index = 0;
//�ļ�ָ�룬����ģ���ļ�����д��������� 
FILE *fp;

//�������������� super_block����������ָ���涨�Ĵ����ϵ���ʼλ�� 
void update_group_desc(){
    fseek(fp, DISK_START, SEEK_SET);
    fwrite(super_block, BLOCK_SIZE, 1, fp);
}
//�������������� super_block
void reload_group_desc(){
    fseek(fp, DISK_START, SEEK_SET);
    fread(super_block, BLOCK_SIZE, 1, fp);
}
//����inodeλͼ��bitbuf������ 
void update_inode_bitmap(){
    fseek(fp, INODE_BITMAP, SEEK_SET);
    fwrite(bitbuf, BLOCK_SIZE, 1, fp);
}
//����inodeλͼ �� bitbuf
void reload_inode_bitmap(){
    fseek(fp, INODE_BITMAP, SEEK_SET);
    fread(bitbuf, BLOCK_SIZE, 1, fp);
}
//����blockλͼ�� bitbuf
void update_block_bitmap(){
    fseek(fp, BLOCK_BITMAP, SEEK_SET);
    fwrite(bitbuf, BLOCK_SIZE, 1, fp);
}
//����blockλͼ�� bitbuf 
void reload_block_bitmap(){
    fseek(fp, BLOCK_BITMAP, SEEK_SET);
    fread(bitbuf, BLOCK_SIZE, 1, fp);
}
//���µ�i��inode���
void update_inode_entry(unsigned short i){
    fseek(fp, INODE_TABLE+(i-1)*INODE_SIZE, SEEK_SET);
    fwrite(inode_area, INODE_SIZE, 1, fp);
}
//�����i��inode���
void reload_inode_entry(unsigned short i){
    fseek(fp, INODE_TABLE+(i-1)*INODE_SIZE, SEEK_SET);
    fread(inode_area, INODE_SIZE, 1, fp);
}
//��ǰ��Ҫ���ʵ���Ŀ¼ʱ�����뺬�е�i��Ŀ¼��Ϣ�����ݿ�
void reload_dir(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fread(dir, BLOCK_SIZE, 1, fp);
}
//���µ�i��Ŀ¼�����ݿ� 
void update_dir(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fwrite(dir, BLOCK_SIZE, 1, fp);
}
//�����i�����ݿ� 
void reload_block(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fread(Buffer, BLOCK_SIZE, 1, fp);
}
//���µ�i�����ݿ�
void update_block(unsigned short i){
    fseek(fp, DATA_BLOCK+i*BLOCK_SIZE, SEEK_SET);
    fwrite(Buffer, BLOCK_SIZE, 1, fp);
}
//����һ�����ݿ�,�������ݿ��; 
int alloc_block(){
	//����һ�η����λ�ÿ�ʼ���䣬�ɱ���϶����Ƭ���� 
    unsigned short cur = last_alloc_block;
    //���ڼ��㵱ǰ�е���һ������ 
    unsigned char con = 128;
    int flag = 0;
    if(super_block[0].bg_free_blocks_count == 0){
        printf("There is no block to be alloced!\n");
        return 0;
    }
    reload_block_bitmap();
    cur = cur / 8;
    //��ǰ������ 
    while(bitbuf[cur] == 255){
    	//�������ֵ��ѭ������ 
        if(cur == 511) cur = 0;
        else cur++;
    }
    //�ҵ�����λ��curʱ����ȷ��λ�ڸ��кδ� 
    while(bitbuf[cur]&con){
        con = con/2;
        flag++;
    }
    //���µ�ǰ��λͼ��Ϣ 
    bitbuf[cur] = bitbuf[cur] + con;
    //��¼��ǰ������ 
    last_alloc_block = cur*8 + flag;
    //����λͼ��Ϣ������������Ϣ 
    update_block_bitmap();
    super_block[0].bg_free_blocks_count--;
    update_group_desc();
    return last_alloc_block;
}
//���һ��block
void remove_block(unsigned short del_num){
    unsigned short tmp;
    tmp = del_num / 8;
    reload_block_bitmap();
    //����blockλͼ ���ҵ���Ӧ�ж�Ӧ��λ�� 
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
    //����λͼ������������Ϣ 
    update_block_bitmap();
    super_block[0].bg_free_blocks_count++;
    update_group_desc();
}
//����һ��inode,������� 
int get_inode(){
    unsigned short cur = last_alloc_inode;
    unsigned char con = 128;
    int flag = 0;
    if(super_block[0].bg_free_inodes_count == 0){
        printf("There is no Inode to be alloced!\n");
        return 0;
    }
    reload_inode_bitmap();
    //���ڳ�ʼ��ʱcur�Ǵ�1��ʼ�������1 
    cur = (cur-1) / 8;
    //Ѱ�ҿ����� 
    while(bitbuf[cur] == 255){
        if(cur == 511) cur = 0;
        else cur++;
    }
    while(bitbuf[cur]&con){
        con = con / 2;
        flag++;
    }
    //���䲢�޸�λͼ������������Ϣ 
    bitbuf[cur] = bitbuf[cur] + con;
    last_alloc_inode = cur*8 + flag+1;
    update_inode_bitmap();
    super_block[0].bg_free_inodes_count--;
    update_group_desc();
    return last_alloc_inode;
}
//���һ��inode 
void remove_inode(unsigned short del_num){
    unsigned short tmp;
    tmp = (del_num-1) / 8;
    reload_inode_bitmap();
    //����blockλͼ
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

//��Ŀ¼���ļ���ʼ��.and ..
void dir_prepare(unsigned short tmp,unsigned short len,int type){
    reload_inode_entry(tmp);//�õ���Ŀ¼�Ľڵ���ڵ�ַ
    //Ŀ¼�ļ� 
    if(type == 2){
        inode_area[0].i_size = 32;
        inode_area[0].i_blocks = 1;
        inode_area[0].i_block[0] = alloc_block();
        dir[0].inode = tmp;
        dir[1].inode = current_dir;
        dir[0].name_len = len;
        dir[1].name_len = current_dirlen;
        dir[0].file_type = dir[1].file_type = 2;
        //.��..�Ѿ����ã��ʴ�2��ʼ 
        for(type = 2; type < 32; type++)
            dir[type].inode = 0;
        strcpy(dir[0].name, ".");
        strcpy(dir[1].name, "..");
        //1 111��drwxĿ¼
        inode_area[0].i_mode = 7;
        //���µ���Ӧ���ݿ� 
        update_dir(inode_area[0].i_block[0]);
    }
    else{
        inode_area[0].i_size = 0;
        inode_area[0].i_blocks = 0;
        //0111-rwx�ļ�
        inode_area[0].i_mode = 4;
    }
    update_inode_entry(tmp);
}

//�����ļ���inode�ڵ�ţ�����Ŀ¼�ڵ�����ݿ�ţ�0~7����Ŀ¼�����ں�
unsigned short reserch_file(char tmp[9],int file_type,unsigned short *inode_num,unsigned short *block_num,unsigned short *dir_num){
    unsigned short j, k;
    reload_inode_entry(current_dir);
    j = 0;
    while(j < inode_area[0].i_blocks){
    	//���ݿ��СΪ512����ʱĿ¼���������ɷ�32���ļ��Ĭ��ÿ���ļ���С16 
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

//Ŀ¼�ƶ� 
void cd(char tmp[9]){
    unsigned short i, j, k, flag;
    flag = reserch_file(tmp, 2, &i, &j, &k);
    if(flag){
        current_dir = i;
        //��Ŀ¼��namelenΪ0,��ʱ�����ټ������ϻ���,����ǰ������Ϊcd ..
		//��dir[k-1]ΪĿ¼.������ǰĿ¼����ʹ��cd ..�豣֤��ǰĿ¼���Ȳ�Ϊ0 
        if(!strcmp(tmp, "..") && dir[k-1].name_len){
            current_path[strlen(current_path)-dir[k-1].name_len-1] = '\0';
            current_dirlen = dir[k].name_len;
        }
        //��cd����ǰĿ¼�������� 
        else if(!strcmp(tmp,"."));
        //cd������·�� 
        else if(strcmp(tmp,"..")){
            current_dirlen=strlen(tmp);
            strcat(current_path,tmp);
            strcat(current_path,"/");
        }
    }
    else printf("The directory %s not exists!\n", tmp);
}
//ɾ���ļ� 
void del(char tmp[9])
{
    unsigned short i, j, k, m, n, flag;
    m = 0;
    //�ҵ�Ҫɾ���ļ���inode�� 
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
        flag = 0;
        //�鿴�ļ��Ƿ�� 
        while(fopen_table[flag] != dir[k].inode && flag < 16)
			flag++;
		//���ļ��򿪣�������� 
        if(flag < 16)
			fopen_table[flag] = 0;
		//������Ҫɾ�����ļ���inode�� 
        reload_inode_entry(i);
        //�����������������ʼ������� 
        while(m < inode_area[0].i_blocks)
			remove_block(inode_area[0].i_block[m++]);
        inode_area[0].i_blocks = 0;
        inode_area[0].i_size = 0;
        remove_inode(i);
        //���ļ����ڵĵ�ǰĿ¼�����޸�
        reload_inode_entry(current_dir);
        //���õ�ǰĿ¼�������е���Ϣ������ 
        dir[k].inode = 0;
        update_dir(inode_area[0].i_block[j]);
        //һ���ļ���Ĭ�ϴ�СΪ16����size��ȥ16 
        inode_area[0].i_size -= 16;
        m = 1;
        //�Ե�ǰĿ¼������޸� 
        while(m < inode_area[i].i_blocks){
            flag = n = 0;
            reload_dir(inode_area[0].i_block[m]);
            while(n < 32){
                if(!dir[n].inode)
					flag++;
                n++;
            }
            //��flagΪ32ʱ��˵�������ݿ��Ѿ�ȫ�գ������ɾ�� 
            if(flag == 32){
                remove_block(inode_area[i].i_block[m]);
                inode_area[i].i_blocks--;
                //���������ݿ��м䱻ɾ��ʱ������������ݿ���ǰ�ƶ� 
                while(m < inode_area[i].i_blocks)
					inode_area[i].i_block[m] = inode_area[i].i_block[++m];
            }
        }
        update_inode_entry(current_dir);
    }
    else printf("The file %s not exists!\n",tmp);
}
//�½�Ŀ¼�ļ� 
void mkdir(char tmp[9], int type)
{
    unsigned short tmpno, i, j, k, flag;
    //����ǰĿ¼�������ڵ���ص�������inode_area��
    reload_inode_entry(current_dir);
    if(!reserch_file(tmp, type, &i, &j, &k)){
    	//Ŀ¼�����ƿռ�����
        if(inode_area[0].i_size == 4096){
            printf("Directory has no room to be alloced!\n");
            return;
        }
        flag = 1;
        //Ŀ¼���������ݿ���ĳЩ����32����δ��
        if(inode_area[0].i_size != inode_area[0].i_blocks*512){
            i = 0;
            while(flag && i < inode_area[0].i_blocks){
                reload_dir(inode_area[0].i_block[i]);
                j = 0;
                //�ҵ�����λ���� 
                while(j < 32){
                    if(dir[j].inode == 0){
                        flag = 0;
                        break;
                    }
                    j++;
                }
                i++;
            }
            //��ʱj���ڵ�λ�ü��½�Ŀ¼�ļ���λ��
			//��������һ��inode�ţ�tmpno��¼�����inode�� 
            tmpno = dir[j].inode = get_inode();
            dir[j].name_len = strlen(tmp);
            dir[j].file_type = type;
            strcpy(dir[j].name, tmp);
            update_dir(inode_area[0].i_block[i-1]);
        }
        //ȫ��������������һ�����ݿ� 
        else{
        	//����һ�����ݿ鲢����Ӧ���� 
            inode_area[0].i_block[inode_area[0].i_blocks] = alloc_block();
            inode_area[0].i_blocks++;
            //���½���Ŀ¼�ļ����г�ʼ�� 
            reload_dir(inode_area[0].i_block[inode_area[0].i_blocks-1]);
            tmpno = dir[0].inode = get_inode();
            dir[0].name_len = strlen(tmp);
            dir[0].file_type = type;
            strcpy(dir[0].name, tmp);
            //��ʼ���¿�
            for(flag = 1; flag < 32; flag++)
				dir[flag].inode = 0;
            update_dir(inode_area[0].i_block[inode_area[0].i_blocks-1]);
        }
        //�½�һ���ļ������size 
        inode_area[0].i_size += 16;
        update_inode_entry(current_dir);
        //��ʼ��Ŀ¼��������.��..Ŀ¼ 
        dir_prepare(tmpno, strlen(tmp), type);
    }
    //�Ѿ�����ͬ���ļ���Ŀ¼
    else{
        if(type == 1)
			printf("File has already existed!\n");
        else printf("Directory has already existed!\n");
    }
}
// ɾ��Ŀ¼�ļ� 
void rmdir(char tmp[9]){
    unsigned short i, j, k, flag;
    unsigned short m, n;
    //.��..Ŀ¼����ɾ�� 
    if(!strcmp(tmp, "..") || !strcmp(tmp,".")){
        printf("The directory can not be deleted!\n");
        return;
    }
    flag = reserch_file(tmp, 2, &i, &j, &k);
    if(flag){
    	//�ҵ�Ҫɾ����Ŀ¼�Ľڵ㲢���ص������� 
        reload_inode_entry(dir[k].inode);
        //��СΪ32����ֻ��.and ..
        if(inode_area[0].i_size == 32){
            inode_area[0].i_size = 0;
            inode_area[0].i_blocks = 0;
            //��ʱֻ��һ�����ݿ飬����ʼ����һ�����ݿ� 
            remove_block(inode_area[0].i_block[0]);
            //�õ���ǰĿ¼��inode�Ų����ĵ�ǰĿ¼��
            reload_inode_entry(current_dir);
            remove_inode(dir[k].inode);
            dir[k].inode = 0;
            update_dir(inode_area[0].i_block[j]);
            inode_area[0].i_size -= 16;
            flag = 0;
            m = 1;
            //�Ե�ǰĿ¼������޸� 
            while(flag < 32 && m < inode_area[0].i_blocks){
                flag = n = 0;
                reload_dir(inode_area[0].i_block[m]);
                while(n < 32){
                	//��n����Ŀ¼�������Ϊ����flag++ 
                    if(!dir[n].inode)
						flag++;
                    n++;
                }
                //��flagΪ32ʱ��˵�������ݿ��Ѿ�ȫ�գ������ɾ�� 
                if(flag == 32){
                    remove_block(inode_area[0].i_block[m]);
                    inode_area[0].i_blocks--;
                    //ɾ��֮�󣬶��Ѵ��ڵ����ݿ���кϲ��ƶ� 
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
//�г���ǰĿ¼���� 
void ls()
{
    printf("items           type           mode            size\n");
    unsigned short i, j, k, tmpno, no;
    i = 0;
    //���ص�ǰĿ¼��inode�� 
    reload_inode_entry(current_dir);
    //���η��ʵ�ǰĿ¼�����ݿ� 
    while(i < inode_area[0].i_blocks){
        k = 0;
        //���ص�Ŀ¼�ļ������� 
        reload_dir(inode_area[0].i_block[i]);
        while(k < 32){
        	//��ǰĿ¼�ļ�����inode�ţ������ڸ��ļ� 
            if(dir[k].inode){
                printf("%s", dir[k].name);
                //��ΪĿ¼�ļ� 
                if(dir[k].file_type == 2){
                    j = 0;
                    //���س����ļ���inode�ţ��õ����ļ���Ϣ
                    reload_inode_entry(dir[k].inode);
                    if(!strcmp(dir[k].name, ".."))
						printf("             ");
                    else if(!strcmp(dir[k].name, "."))
						printf("              ");
                    else while(j++ < 15-dir[k].name_len)
							printf(" ");
                    printf("<DIR>          ");
                    //�жϵ�ǰ�û���Ȩ�� 
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
                //��ͨ�ļ� 
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

//�ڴ��ļ����в����Ƿ��Ѵ��ļ�
unsigned short search_file(unsigned short Ino)
{
    unsigned short fopen_table_point = 0;
    while(fopen_table_point < 16 && fopen_table[fopen_table_point++] != Ino);
    if(fopen_table_point == 16)
		return 0;
    return 1;
}
//���ļ�
void read_file(char tmp[9])
{
    unsigned short flag, i, j, k;
    //�ҵ��ļ�Ŀ¼���inode�� 
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//�ļ��Ѵ� 
        if(search_file(dir[k].inode)){
            reload_inode_entry(dir[k].inode);
            //auth:111b:��,д,ִ��
            if(!(cur_user.auth&4)){
                printf("The file %s can not be read!\n", tmp);
                return;
            }
            //ѭ����ȡ�������ݿ�
            for(flag = 0; flag < inode_area[0].i_blocks; flag++){
                reload_block(inode_area[0].i_block[flag]);
                Buffer[512] = '\0';
                printf("%s", Buffer);
            }
            //flagδ�ӣ���ʾ������ 
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

//д�ļ�
void write_file(char tmp[9])
{
    unsigned short flag, i, j, k, size = 0, need_blocks;
    //�ҵ���Ӧ�ļ���Ŀ¼����Ϣ��flagΪ1��ʾ�ҵ�������δ�ҵ� 
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//�ж��Ƿ�� 
        if(search_file(dir[k].inode)){
        	//�ҵ������ڵ�� 
            reload_inode_entry(dir[k].inode);
            //�ж�Ȩ���Ƿ��д 
            if(!(cur_user.auth&2)){
                printf("The file %s can not be writed!\n", tmp);
                return;
            }
            while(1){
                tempbuf[size] = getchar();
                //��#�Ž����ļ����� 
                if(tempbuf[size] == '#'){
                    tempbuf[size] = '\0';
                    break;
                }
                //�����ļ���󳤶� 
                if(size >= 4096){
                    printf("Sorry,the max size of a file is 4KB!\n");
                    tempbuf[size] = '\0';
                    break;
                }
                size++;
            }
            //��Ҫ�����ݿ����� 
            need_blocks = strlen(tempbuf)/512;
            //����δ��512�Ĳ�����������һ��ռ� 
            if(strlen(tempbuf)%512)
				need_blocks++;
			//���8�飬��4KB 
            if(need_blocks < 9){
            	//������Ҫ�����ݿ� 
                while(inode_area[0].i_blocks < need_blocks){
                    inode_area[0].i_block[inode_area[0].i_blocks] = alloc_block();
                    inode_area[0].i_blocks++;
                }
                j = 0;
                while(j < need_blocks){
                    if(j != need_blocks-1){
                    	//���س����ݿ�λͼ��Ϣ 
                        reload_block(inode_area[0].i_block[j]);
                        memcpy(Buffer, tempbuf+j*BLOCK_SIZE, BLOCK_SIZE);
                        //����λͼ��Ϣ 
                        update_block(inode_area[0].i_block[j]);
                    }
                    //���һ����Ҫ�����ж�
                    else{
                        reload_block(inode_area[0].i_block[j]);
                        memcpy(Buffer, tempbuf+j*BLOCK_SIZE, strlen(tempbuf)-j*BLOCK_SIZE);
                        //���ж��ಿ�֣���Ҫ��size�������� 
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
//�ر��ļ�
void close_file(char tmp[9])
{
    unsigned short flag, i, j, k;
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//�ļ��Ѵ򿪣���ʱ�ſ�ִ�йرղ��� 
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
//���ļ� 
void open_file(char tmp[9])
{
    unsigned short flag, i, j, k;
    flag = reserch_file(tmp, 1, &i, &j, &k);
    if(flag){
    	//�ж��ļ��Ƿ��Ѿ��򿪣����Ѿ������ٽ��в��� 
        if(search_file(dir[k].inode))
			printf("The file %s has opened!\n", tmp);
        else {
        	//δ��ʱ�����ļ��򿪱�������Ӧ��¼ 
            flag = 0;
            while(fopen_table[flag])
				flag++;
            fopen_table[flag] = dir[k].inode;
            printf("File %s! opened\n", tmp);
        }
    }
    else printf("The file %s does not exist!\n", tmp);
}
//��ʼ������ 
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
    //��ʼ����һ�η���ʱ��inode��block�� 
    last_alloc_inode = 1;
    last_alloc_block = 0;
    //����ļ��򿪱� 
    for(i = 0; i < 16; i++)
		fopen_table[i] = 0;
	// ��ջ�������ͨ������������ļ�����ģ����մ���
    for(i = 0; i < BLOCK_SIZE; i++)
		Buffer[i] = 0;
	//�����ļ� 
    fp = fopen("FS_zqw_zzw.txt", "w+b");
    fseek(fp, DISK_START, SEEK_SET);
    //����ļ�������մ���ȫ����0���
    for(i = 0; i < 4096; i++) 
		fwrite(Buffer, BLOCK_SIZE, 1, fp);
	//���س�����ʼ������Ϣ��inode�Լ�Ŀ¼����Ϣ 
    reload_group_desc();
    reload_inode_entry(1);
    reload_dir(0);
    
	//������������ʼ��������������
    strcpy(super_block[0].bg_volume_name, "MYSYSTEM");
    //��ʼ��������������
    super_block[0].bg_block_bitmap = BLOCK_BITMAP;
    super_block[0].bg_inode_bitmap = INODE_BITMAP;
    super_block[0].bg_inode_table = INODE_TABLE;
    super_block[0].bg_free_blocks_count = 4096;
    super_block[0].bg_free_inodes_count = 4096;
    //super_block[0].bg_used_dirs_count = 0;
    //����������������
    update_group_desc();
    //����λͼ��Ϣ 
    reload_block_bitmap();
    reload_inode_bitmap();
    //��ʼ��λͼ��Ϣ 
    inode_area[0].i_mode = 7;
    inode_area[0].i_blocks = 0;
    inode_area[0].i_size = 32;
    inode_area[0].i_block[0] = alloc_block();
    inode_area[0].i_blocks++;
    //����һ��inode�� 
    current_dir = get_inode();
    update_inode_entry(current_dir);
    //��ʼ��.��..Ŀ¼ 
    dir[0].inode = dir[1].inode = current_dir;
    dir[0].name_len = 0;
    dir[1].name_len = 0;
    //1:�ļ�;2:Ŀ¼
    dir[0].file_type = dir[1].file_type = 2; 
    strcpy(dir[0].name, ".");
    strcpy(dir[1].name, "..");
    update_dir(inode_area[0].i_block[0]);
    printf("The ext2 file system has been installed!\n");
}
//��ʼ���ڴ棬��һ������ļ� 
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
		printf("����Ȩ��ʽ����");
		return 0;
	}
    initialize_disk();
    initialize_memory();
    return 0;
}
    
void help()
{
    printf("   ***************************************************************************\n");
    printf("   *                            �����ļ�ϵͳ����                             *\n");
    printf("   *                                                                         *\n");
    printf("   *     1.�ƶ�Ŀ¼   : cd+dir_name          8.�½�Ŀ¼  : mkdir+dir_name    *\n");
    printf("   *     2.�½��ļ�   : mkf+file_name        9.ɾ��Ŀ¼  : rmdir+dir_name    *\n");
    printf("   *     3.ɾ���ļ�   : rm+file_name         10.���ļ�   : read+file_name    *\n");
    printf("   *     4.���ļ�   : open+file_name       11.д�ļ�   : write+file_name   *\n");
    printf("   *     5.�ر��ļ�   : close+file_name      12.�˳�     : quit              *\n");
    printf("   *     6.Ŀ¼����   : ls                   13.����     : help              *\n");
    printf("   *     7.��ʽ��     : format               14.ע����ǰ�û� ��logout        *\n");
    printf("   ***************************************************************************\n");
}
//�û���¼ 
int login(){
	struct user cur;
	char ch;
	int i = 0, j = 0;
	printf("�û�����");
	scanf("%s", cur.username);
	printf("���룺");
	while((ch = getch()) != '\r'){
		cur.password[i++] = ch;
	}
	cur.password[i] = '\0';
	printf("\n");
	//�����Ƿ��е�¼�û�����Ϣ�������¼��û�����޴��û� 
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
//�û�ע�� 
void reg(){
	char ch;
	int i = 0;
	printf("���û�����");
	scanf("%s", cur_user.username);
	printf("�����룺");
	while((ch = getch()) != '\r'){
		cur_user.password[i++] = ch;
	}
	cur_user.password[i] = '\0';
	//Ĭ��Ȩ��Ϊֻ�� 
	cur_user.auth = 4;
	printf("\n");
	users[cur_user_num++] = cur_user;
}
//�û�ע�� 
void logout(char str[8]){
	struct user u;
	strcpy(u.username, str);
	char ch;
	int i = 0, j;
	printf("����ȷ�ϣ�");
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
		printf("     *        1.�û���¼        *\n");
		printf("     *        2.�û�ע��        *\n");
		printf("     *        3.�˳�            *\n");
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
				printf("�û��������������������������\n");
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

