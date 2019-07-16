import numpy as np
import pandas as pd
import re
import datetime as dt

#从文件中读取并简单处理数据，得到每位同学的初始签到时间
def get_data(month):
    data = pd.read_excel('307实验室{0}月考勤记录.xls'.format(month), '考勤记录')
    pd.set_option('display.max_rows', 500)
    pd.set_option('display.max_columns', 500)
    pd.set_option('display.width', 1000)
    time_data = data.ix[6]

    i = 8
    while i <= data.shape[0]:
        time_data = pd.concat([time_data, data.ix[i]], axis=1, ignore_index=True)
        i += 2

    time_data.index=range(1, data.shape[1] + 1)
    time_data = time_data.T
    time_data = time_data.fillna(0)#NaN值填充为0
    return time_data

#获取名字信息，以用于作为下表使用
def get_name(month):
    name = pd.read_excel('307实验室{0}月考勤记录.xls'.format(month), '考勤汇总')
    name = name[4:]
    name = pd.concat([name['Unnamed: 1'], name['Unnamed: 10']], axis=1, ignore_index=True)
    name.columns = ['name', 'times']
    name.index = name['name']
    name.drop(['name'], axis=1, inplace=True)
    return name

#进一步处理签到时间
def get_new_data(month):
    time = get_data(month)
    time.index = get_name(month).index
    for index, row in time.iterrows():#返回行列元组，index下标，row为某一行的值
        for col_name in time.columns:
            if row[col_name] != 0:
                each = re.findall(r'.{5}', row[col_name])

                i = 0
                while i + 1 < len(each):#去掉重复签到时间
                    if dt.datetime.strptime(each[i + 1], "%H:%M") - dt.datetime.strptime(each[i], "%H:%M") \
                            <= dt.datetime.strptime('0:3', "%H:%M") - dt.datetime.strptime('0:0', "%H:%M"):
                        each.pop(i)#删除掉重复的
                        i -= 1
                    i += 1

                #对只有签入或迁出的时间合理添加一个签入签到时间
                if len(each) % 2 == 1:
                    if dt.datetime.strptime('07:00', "%H:%M") <= dt.datetime.strptime(each[len(each) - 1], "%H:%M") \
                            <= dt.datetime.strptime('12:00', "%H:%M"):
                        each.append('12:00')
                    if dt.datetime.strptime('12:01', "%H:%M") <= dt.datetime.strptime(each[len(each) - 1], "%H:%M") \
                            <= dt.datetime.strptime('14:00', "%H:%M"):
                        each.append('14:00')
                    if dt.datetime.strptime('14:01', "%H:%M") <= dt.datetime.strptime(each[len(each) - 1], "%H:%M") \
                            <= dt.datetime.strptime('18:00', "%H:%M"):
                        each.append('18:00')
                    if dt.datetime.strptime('18:01', "%H:%M") <= dt.datetime.strptime(each[len(each) - 1], "%H:%M") \
                            <= dt.datetime.strptime('19:40', "%H:%M"):
                        each.append('19:40')
                    if dt.datetime.strptime('19:41', "%H:%M") <= dt.datetime.strptime(each[len(each) - 1], "%H:%M") \
                            <= dt.datetime.strptime('23:00', "%H:%M"):
                        each.append('23:00')

                time.loc[index, col_name] = ''.join(each)
    return time

#计算每天每个同学在实验室的时间
def get_day_time(month):
    time = get_new_data(month)
    all_stu = pd.DataFrame()#用于装处理后的签到时间信息

    for index, row in time.iterrows():
        day_time = pd.Series()#装入每个同学签到时间信息的Series结构，再逐渐合并到all_stu
        for col_name in time.columns:
            if row[col_name] != 0:
                each = re.findall(r'.{5}', row[col_name])

                i = 0
                subs = dt.datetime.strptime('00:00', "%H:%M")#计算每人每天在实验室时间
                #循环计数，后一个时间减去前一个时间
                while i + 1 < len(each):
                    subs += dt.datetime.strptime(each[i + 1], "%H:%M") -\
                        dt.datetime.strptime(each[i], "%H:%M")
                    i += 2

                day_time = day_time.append(pd.Series(subs.minute + subs.hour * 60), ignore_index=True)
            else:
                day_time = day_time.append(pd.Series(0), ignore_index=True)
        # 合并到all_stu
        all_stu = pd.concat([all_stu, day_time], ignore_index=True, axis=1)
    all_stu = all_stu.T
    all_stu.index = get_name(month).index#index设置为姓名
    return all_stu


if __name__ == '__main__':
    print(get_day_time(11))

