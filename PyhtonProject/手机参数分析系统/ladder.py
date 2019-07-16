import os
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# 设置图中标签可中文显示
plt.rcParams['font.sans-serif']=['SimHei']


# 手机CPU性能得分图（天梯图）
def ladder_iamge():
	# 路径输入
	path = 'E:\\what-study\\project\\ladder\\ladder.csv'
	# 读取数据
	ladder = pd.read_csv(path)

	# 获取图像面板信息，ax为轴相关信息，可通过设置figsize来设置面板大小
	f, ax = plt.subplots(figsize=(8, 27))
	# 利用seaborn根据得分排序绘制条形图
	sns.barplot(x="score", y="name", data=ladder, color="#555555", ci=0)
	plt.title('手机CPU型号性能得分图')
	plt.show()


# 热门手机排行，上升最快手机排行以及按品牌的排行榜（OPPO、VIVO、华为、三星、苹果、荣耀、小米、魅族、诺基亚）
# 根据热度排序
def phone_all_type_iamge():
	# 需要作图的手机序列
	main_type = {'OPPO手机', 'vivo手机', '一加手机', '三星手机', '上升最快的手机', '努比亚手机', 
            '华为手机', '小米手机', '热门手机', '苹果手机', '荣耀手机', '魅族手机'}
	for i in main_type:
		# 根据序列读取文件
	    f = open('E:\\what-study\\project\\rank\\' + i + '排行榜.csv', encoding='utf-8')
	    hot_phone = pd.read_csv(f)
	    # 对热度和手机名称处理。取出热度中的数字，去掉手机型号字符串中括号里的内容
	    hot_phone['热度'] = hot_phone['热度'].map(lambda x: x[:x.find('%')])
	    hot_phone['型号'] = hot_phone['型号'].map(lambda x: x[:x.find('（')])
	    hot_phone['热度'] = hot_phone['热度'].astype(float)
	    f, ax = plt.subplots(figsize=(8, 15))
	    sns.barplot(x=hot_phone['热度'], y=hot_phone['型号'], data=hot_phone, color="#612563", ci=0)
	    plt.title(i + '排行榜')
	    plt.xlabel('热度(%)')
	    plt.show()


def main():
	ladder_iamge()
	phone_all_type_iamge()


if __name__ == '__main__':
	main()