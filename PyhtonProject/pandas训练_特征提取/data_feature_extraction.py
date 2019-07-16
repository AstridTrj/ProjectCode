import numpy as np
import pandas as pd


# 连续天数的计算
def continuous_day(x):
    x.sort()
    max_count = 1
    count = 1
    for i in range(len(x)-1):
        if x[i + 1] - x[i] == 1:
            count += 1
            max_count = count if (count > max_count) else max_count
        else:
            count = 1
    return max_count


def get_activity_count():
    # 统计行为的总次数
    user_activity_log = pd.read_csv('practicedata/user_activity_log.csv') # 读取文件
    activity_total_count = user_activity_log['user_id'].value_counts() # 统计总次数
    # 平均行为数的计算
    activity_day_count = user_activity_log['day'].groupby(user_activity_log['user_id']) # 对天根据id分组
    end_day = activity_day_count.apply(lambda x: x.unique().max()) # 有行为的最后一天
    # 最大连续天数
    con_day = activity_day_count.apply(lambda x: continuous_day(list(x.unique())))
    # 每天的行为数
    activity_day_count = activity_day_count.apply(lambda x: x.unique().size)
    activity_count = pd.concat([activity_total_count, activity_day_count], axis=1) # 先合并两个表方便计算平均行为数
    activity_count.columns = ['total_count', 'day_count'] # 修改列名
    activity_count['average_count'] = activity_count.total_count / activity_count.day_count # 每天平均行为数计算
    activity_count = activity_count.drop('day_count', axis=1) # 去除不需要的天数一列
    # 一天中最多行为数
    day_max_group = user_activity_log['page'].groupby([user_activity_log['user_id'], user_activity_log['day']])
    day_max_count = day_max_group.apply(lambda x: x.size).unstack().fillna(0)
    day_variance = day_max_count.var(axis=1) # 方差计算
    day_std = day_max_count.std(axis=1) # 标准差计算

    # action_type为012的行为数，type为0page为1的行为数
    action_type_group = user_activity_log['page'].groupby([user_activity_log['user_id'], \
                                                           user_activity_log['action_type']])
    action_type_012 = action_type_group.apply(lambda x: x.size).unstack().drop([3, 4, 5], axis=1).fillna(0)
    type0_page1 = action_type_group.apply(lambda x: list(x).count(1)).unstack().drop([3, 4, 5], axis=1).fillna(0)
    # type种类数
    action_type_count = user_activity_log['action_type'].groupby(user_activity_log['user_id']).apply(lambda x:\
                                                x.unique().size)
    type0 = user_activity_log[user_activity_log['action_type'] == 0] # 筛选action_type为0的行为
    # 28,29,30号且type为0的行为数
    day_type_group = type0['action_type'].groupby([type0['user_id'], type0['day']])
    day282930_type0 = day_type_group.apply(lambda x: x.size).unstack().fillna(0)
    # 合成DataFrame结构以写入CSV文件
    activity_count = pd.concat([activity_count, day_max_count.max(axis=1), day_variance, day_std,
                                con_day, activity_day_count, end_day, day_max_count.idxmax(axis=1), action_type_012,
                                type0_page1[[0]], action_type_count, day282930_type0[[28, 29, 30]]], axis=1)
    return activity_count


if __name__ == '__main__':
    ac = get_activity_count()
    ac.to_csv('activity_count.csv', header=['total_count', 'average_count', 'day_max_count',  'day_variance',
                                            'day_std', 'continuous_day','activity_total_day', 'end_day',
                                            'max_activity_day', 'action_type_0', 'action_type_1', 'action_type_2',
                                            'type0_page1', 'action_type_count', 'day28_type0', 'day29_type0',
                                            'day30_type0'])