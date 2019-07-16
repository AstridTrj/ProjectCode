import pylab as pl
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import all_data as da

#设置编码以显示中文
pl.mpl.rcParams['font.sans-serif'] = ['SimHei']
pl.mpl.rcParams['axes.unicode_minus'] = False

#输入分析的同学
name = input('请输入需要分析的同学姓名：')
while name not in da.get_day_time(11).index:
    print(name + '同学不存在')
    name = input('请重新输入需要分析的同学姓名：')

#11月，12月画图数据处理，包括各种值，每天在实验室时间
time11 = da.get_day_time(11).ix[name]
time12 = da.get_day_time(12).ix[name]#返回serie对象
time11.index = range(1, 31)
time12.index = range(1, 32)
time11_plot = []
time12_plot = []
time11_plot.append([time11.sum()/60, time11.mean()/60, time11.max()/60, time11.min()/60, time11.std()/60])
time12_plot.append([time12.sum()/60, time12.mean()/60, time12.max()/60, time12.min()/60, time12.std()/60])
data_1112 = {'November': list(time11_plot[0]), 'December': list(time12_plot[0])}#每个series的0列取出应用list
data_plot = pd.DataFrame(data_1112)

#考勤图1设置
data_plot.plot(kind='bar')
plt.title(name + '的考勤数据图1')
plt.ylabel('小时数')
plt.xticks(range(5), ['总计', '平均值', '最大值', '最小值', '标准差'])
pl.xticks(rotation=360)
for x, y in zip(range(5), time11_plot[0]):
    plt.text(x - 0.15, y + 0.2, '%.1f' % y, ha='center', va='bottom')
for x, y in zip(range(5), time12_plot[0]):
    plt.text(x + 0.1, y + 0.2, '%.1f' % y, ha='center', va='bottom')
plt.show()

#考勤图2设置
plt.plot(time11, 'r--', )
plt.plot(time12, 'g')
plt.title(name + '的考勤数据图2')
plt.xlabel('日期')
plt.ylabel('在实验室分钟数')
plt.legend(['11月', '12月'])
plt.show()