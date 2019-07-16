import  all_data as da
import numpy as np
import pandas as pd
import re
import datetime as dt
import matplotlib.pyplot as plt
import pylab as pl

#设置编码以显示中文
pl.mpl.rcParams['font.sans-serif'] = ['SimHei']
pl.mpl.rcParams['axes.unicode_minus'] = False

def chart_bar(month):
    if month == 11:#up--天数
        up = 30
    else:
        up = 31

    #得到处理了签入签到时间后的数据，装入frame
    frame = da.get_new_data(month)
    frame.columns = range(up)
    #统计个时段人数的DataFrame结构
    count_statistics11 = pd.DataFrame(np.zeros((3, up)))

    #对frame中的数据遍历统计人数
    for col in frame.columns:#对每列循环
        for index, row in frame.iterrows():#获取行信息
            if frame[col][index] != 0:
                each = re.findall(r'.{5}', frame[col][index])#拆分成时间格式
                count1 = count2 = count3 = 0#用于计数
                for iterm in each:#浏览并统计每一个时间所在的时段
                    if dt.datetime.strptime('07:00', "%H:%M") <= dt.datetime.strptime(iterm, "%H:%M") \
                        <= dt.datetime.strptime('12:00', "%H:%M"):
                        count1 += 1
                    if dt.datetime.strptime('14:00', "%H:%M") <= dt.datetime.strptime(iterm, "%H:%M") \
                        <= dt.datetime.strptime('19:00', "%H:%M"):
                        count2 += 1
                    if dt.datetime.strptime('19:30', "%H:%M") <= dt.datetime.strptime(iterm, "%H:%M") \
                        <= dt.datetime.strptime('23:00', "%H:%M"):
                        count3 += 1
                if count1 != 0:
                    count_statistics11.loc[0][col] += 1
                if count2 != 0:
                    count_statistics11.loc[1][col] += 1
                if count3 != 0:
                    count_statistics11.loc[2][col] += 1

    count_statistics11 = count_statistics11.T#转置以用于画图
    count_statistics11.index = range(1, up + 1)
    count_statistics11.plot(kind='line', marker = 'o', title='{0}'.format(month) + '月每日各时段到实验室人数折线图')
    plt.xlabel('日期')
    plt.ylabel('人数')
    plt.legend(['上午', '下午', '晚上'])
    plt.show()





if __name__  == '__main__':
    chart_bar(11)
    chart_bar(12)