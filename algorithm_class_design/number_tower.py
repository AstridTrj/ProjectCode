# coding=utf-8
import numpy as np
import tkinter as tk
import time

s_path = []

# 数塔的绘制
def draw_tower(num, path):
    n = len(num)
    # 创建窗口
    window = tk.Tk()
    window.title('最大总和问题-数塔DP')
    # 设置窗口大小
    window.geometry(str(90 * (n + 2)) + 'x' + str(50 * (n + 1)))
    # 创建画布
    canvas = tk.Canvas(window, height=50 * (n + 1), width=90 * (n + 2), bg='white')
    canvas.pack()
    # 绘制每个数所在的方块
    for i in range(n - 1):
        for j in range(i + 1):
            canvas.create_rectangle(50 * (n - i + 2 * j), 50 * (i + 1), 50 * (n - i + 2 + 2 * j), 50 * (i + 2), fill='yellow',
                                outline='black')
            canvas.create_text(50 * (n - i + 2 * j) + 40, 50 * (i + 1) + 25, text=num[i, j])
            canvas.update()
            time.sleep(1)
    global s_path
    for p in s_path:
        start, end, c = p[0], p[1], p[2]
        if c == 'w':
            canvas.create_line(50 * (n - end[0] + 2 * end[1]) + 50, 50 * (end[0] + 1) + 25,
                               50 * (n - start[0] + 2 + 2 * start[1]) - 50, 50 * (start[0] + 2) - 25,
                               fill='blue', width=2, arrow='first')
        else:
            canvas.create_line(50 * (n - start[0] + 2 * start[1]) + 50, 50 * (start[0] + 1) + 25,
                               50 * (n - end[0] + 2 + 2 * end[1]) - 50, 50 * (end[0] + 2) - 25,
                               fill='black', width=2, arrow='last')
        canvas.update()
        time.sleep(1)
    # 绘制最大和路径
    k = 0
    while k < n - 2:
        start, end = path[k], path[k+1]
        canvas.create_line(50 * (n - start[0] + 2 * start[1]) + 50, 50 * (start[0] + 1) + 25, 50 * (n - end[0] + 2 + 2 * end[1]) - 50,
                           50 * (end[0] + 2) - 25, width=5, fill='red', arrow='last')
        canvas.update()
        time.sleep(1)
        k = k + 1
    window.mainloop()


# 数塔算法
# 原理：利用动态规划的思想
# 算法从最底层开始执行，计算向上走时的最大和，直到计算到顶部
def num_tower(num):
    global s_path
    n = len(num)
    # 动态规划表，记录每一个点对应的最大总和
    dp = np.zeros((n, n), dtype=np.int)
    # 从底部开始执行
    for i in range(n-2, -1, -1):
        for j in range(0, i + 1):
            # 每次记录可选路径中的最大值
            dp[i, j] = max(dp[i+1, j], dp[i+1, j+1]) + num[i, j]
            if i != n - 2:
                if dp[i+1, j] > dp[i+1, j+1]:
                    s_path.append([(i+1, j), (i, j), 'b'])
                    s_path.append([(i+1, j+1), (i, j), 'w'])
                elif dp[i+1, j] < dp[i+1, j+1]:
                    s_path.append([(i+1, j), (i, j), 'w'])
                    s_path.append([(i+1, j+1), (i, j), 'b'])
                else:
                    s_path.append([(i+1, j), (i, j), 'w'])
                    s_path.append([(i+1, j+1), (i, j), 'w'])
    print(dp)
    return dp


# 路径寻找，由上往下，找到每次选择的最大值所在的坐标
def find_path(dp, num):
    path = []
    n = len(dp) - 1
    max_a = dp[0, 0]
    i = j = 0
    path.append((0, 0))
    # 从第一层开始，直到找到最后一层，执行完毕
    while i < n - 1:
        max_a = max_a - num[i, j]
        # 如果当前的最大值是由i+1, j而来，则选择该点
        if max_a == dp[i+1, j]:
            path.append((i+1, j))
            i = i + 1
        # 最大值由i+1，j+1而来
        else:
            path.append((i+1, j+1))
            i = i + 1
            j = j + 1
    return path


def main():
    # 输入数据并做相应的处理
    n = int(input('输入数塔层数：'))
    num = np.zeros((n + 1, n + 1), dtype=np.int)
    for i in range(n):
        line = input().split(' ')
        for j in range(len(line)):
            num[i, j] = int(line[j])
    # 执行算法
    dp = num_tower(num)
    path = find_path(dp, num)
    draw_tower(num, path)
    # 输出路径经过的点
    print('最大总和为：', dp[0, 0], '\n路径点为（从上往下）：')
    for i in range(len(path)):
        print(num[path[i][0], path[i][1]], end='')
        if i != len(path) - 1:
            print('->', end='')

if __name__ == '__main__':
    main()


'''
测试数据
9
12 15
10 6 8
2 18 9 5
19 7 10 4 16
7
3 8
8 1 0
2 7 4 4
4 5 2 6 5
6
3 6
2 4 6
8 5 4 1
2 5 8 4 1
2 3 6 1 8 9
2 1 4 5 6 8 9
0 1 4 5 1 2 1 4
'''