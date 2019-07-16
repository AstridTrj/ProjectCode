import numpy as np
import time


# 木棒结构定义，包括其长度的重量
class Stick:

    def __init__(self, length, weight):
        # 通过给定长度和重量初始化木棒
        self.length = length
        self.weight = weight


# 木棒排序，优先根据长度排序，其次根据重量排序
def sort_stick(sticks):
    # 获取木棒的长度n
    n = len(sticks)
    # 冒泡排序
    for i in range(n - 1):
        for j in range(n - i - 1):
            # 将较大者往后移动
            if sticks[j].length > sticks[j + 1].length:
                sticks[j], sticks[j + 1] = sticks[j + 1], sticks[j]
            # 当长度相同时，比较重量
            if sticks[j].length == sticks[j + 1].length and sticks[j].weight > sticks[j + 1].weight:
                sticks[j], sticks[j + 1] = sticks[j + 1], sticks[j]
    return sticks


# 贪心实现木棒加工问题
def greed(sticks):
    # time_count用于统计加工所需分钟数
    time_count = 0
    # order_stick存储确定加工顺序的木棒
    order_stick = []
    # flag为木棒标记，判断当前木棒是否已被确定顺序
    flag = np.zeros(len(sticks))
    # 对每一根木棒访问
    for i in range(len(sticks)):
        # 若当前木棒未被访问，改变其flag
        if flag[i] == 0:
            flag[i] == 1
            # 将当前木棒加入顺序列表中
            order_stick.append(sticks[i])

            # 得到当前木棒的重量，与其后面的木棒比较
            temp = sticks[i].weight
            for j in range(i + 1, len(sticks)):
                # 当后一根比前一根的重量大时，说明可进行顺序加工
                if sticks[j].weight >= temp and flag[j] == 0:
                    order_stick.append(sticks[j])
                    temp = sticks[j].weight
                    flag[j] = 1
            time_count += 1
    return time_count, order_stick


# 动态规划实现木棒加工问题
def dynamic_program(sticks):
    # order_stick存储顺序加工的木棒
    order_stick = []
    # flag为标记数组，记录每根木棒所属子序列的序号
    flag = np.zeros(len(sticks))
    for i in range(len(sticks)):
        flag[i] = 1
        for j in range(i):
            # 如果i之前有比i重的木棒且未处理过，则将i序号加1
            if sticks[j].weight > sticks[i].weight and flag[j] + 1 > flag[i]:
                flag[i] = flag[j] + 1
    # flag数组的最大值即加工所需的最大时间
    time_count = int(flag.max())
    # 通过转换存储到加工列表中
    for i in range(time_count):
        for j in range(len(flag)):
            if flag[j] == i + 1:
                order_stick.append(sticks[j])
    return time_count, order_stick


def main():
    sticks = []
    n = int(input("请输入需要加工的木棒数："))
    lw = input('请输入每根木棒的长度和重量：')
    lw = lw.split(' ')
    for i in range(0, n * 2, 2):
        stick = Stick(int(lw[i]), int(lw[i + 1]))
        sticks.append(stick)

    sticks = sort_stick(sticks)

    start = time.clock()
    count, sticks = greed(sticks)
    elapsed = (time.clock() - start)

    print('动态规划算法：')
    print("Time used:", elapsed)
    print('加工木棒所需准备时间为', count, '分钟。')
    print('木棒加工顺序为: ', end='')
    for i in sticks:
        print('(', i.length, i.weight, ') ', end='')


if __name__ == '__main__':
    main()