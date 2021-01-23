import  numpy as np
import pandas as pd
import tkinter as tk
import time

# 题目定义的得分矩阵，对此矩阵进行创建以便后续使用
def get_score():
    # 使用pandas中的DataFrame结构存储，设置其columns和index
    score = pd.DataFrame([[5, -1, -2, -1, -3], [-1, 5,  -3, -2, -4],
                          [-2, -3, 5, -2, -2], [-1, -2, -2, 5, -1],
                         [-3, -4, -2, -1, 0]])
    score.index = ['A', 'C', 'G', 'T', '-']
    score.columns = ['A', 'C', 'G', 'T', '-']
    return score

# 字符串匹配算法
'''
原理：利用最长公共子序列中相似的匹配思想，挨个进行字符的匹配，选择最优决策
输入为两个字符串
输出为计算出的得分矩阵，也即是动态规划表
'''
def align(s1, s2):
    # 先将两个字符串转化为列表
    s1 = list(s1)
    s2 = list(s2)
    score = get_score()
    # 定义得分矩阵（动态规划表）
    align_score = np.zeros((len(s1) + 1, len(s2) + 1), dtype=np.int)
    align_score[0, 0] = 0
    # 初始化化对齐得分矩阵（包括第一行和第一列的初始化）
    for i in range(1, len(s2) + 1):
        align_score[0, i] = align_score[0, i-1] + score['-'][s2[i-1]]
    for i in range(1, len(s1) + 1):
        align_score[i, 0] = align_score[i-1, 0] + score[s1[i-1]]['-']
    # 得分矩阵的计算，根据动态规划思想循环计算
    for i in range(1, len(s1) + 1):
        for j in range(1, len(s2) + 1):
            # 先求得三种选择的值
            three = [
                align_score[i-1, j-1] + score[s1[i-1]][s2[j-1]],
                align_score[i-1, j] + score[s1[i-1]]['-'],
                align_score[i, j-1] + score['-'][s2[j-1]],
            ]
            # 求其最大值作为当前位置的值
            align_score[i, j] = max(three)
    print(align_score)
    return align_score

# 路径寻找
'''
原理：根据得分矩阵回溯进行路径的求解
输入：得分矩阵an_score，两个字符串string1和string2
输出：路径和两个匹配序列
'''
def find_path(an_score, string1, string2):
    path = []
    res1 = []
    res2 = []
    score = get_score()
    i, j = an_score.shape
    # 从得分矩阵最后一个位置开始回溯
    i, j = i - 1, j - 1
    while i > 0 and j > 0:
        cur = an_score[i, j] - score[string1[i-1]][string2[j-1]]
        # 依次比较可能的三个选择，若值相等，则说明其在计算时选择了该条路，记录相应的位置以及匹配情况
        # 左上角
        if cur == an_score[i-1, j-1]:
            path.append([(i, j), (i-1, j-1)])
            res1.append(string1[i-1])
            res2.append(string2[j-1])
            i, j = i - 1, j - 1
        # 上方
        elif cur == an_score[i-1, j]:
            path.append([(i, j), (i-1, j)])
            res1.append(string1[i-1])
            res2.append('-')
            i = i - 1
        # 左方
        else:
            path.append([(i, j), (i, j-1)])
            res1.append('-')
            res2.append(string2[j-1])
            j = j - 1
    # 有在向左或者向右回溯的过程中，可能提前达到了边界，而在边界的选择只有一种
    # 不能够继续使用上述过程进行回溯，故需重新计算
    # i不等于0时，在第一列
    while i > 0:
        path.append([path[-1][1], (i-1, 0)])
        res1.append(string1[i-1])
        res2.append('-')
        i = i - 1
    # j不等于0在第一行
    while j > 0:
        path.append([path[-1][1], (0, j-1)])
        res1.append('-')
        res2.append(string2[j-1])
        j = j - 1
    res1.reverse()
    res2.reverse()
    res1 = ''.join(res1)
    res2 = ''.join(res2)
    return path, res1, res2

re_w = 80
re_h = 60
# 绘制分数矩阵
def draw_score():
    # 首先清空画布，再进行绘制
    canvas.delete(tk.ALL)
    score = get_score()
    rc = ['A', 'C', 'G', 'T', '-']
    # 固定的绘制方格
    for i in range(6):
        for j in range(6):
            # 根据下标的不同，绘制不同的颜色以及对应的文本内容
            if i == 0 and j == 0:
                canvas.create_rectangle(i*re_w+10, j*re_h+16, (i+1)*re_w+10,
                                        (j+1)*re_h+16, fill='white')
            elif i == 0 or j == 0:
                pos = max(i, j)
                canvas.create_rectangle(i*re_w+10, j*re_h+16, (i+1)*re_w+10,
                                        (j+1)*re_h+16, fill='gold')
                canvas.create_text(i*re_w+45, j*re_h+45, text=rc[pos-1], font=('', 15))
            else:
                canvas.create_rectangle(i*re_w+10, j*re_h+16, (i+1)*re_w+10,
                                        (j+1)*re_h+16, fill='white')
                canvas.create_text(i*re_w+45, j*re_h+45, text=score[rc[i-1]][rc[j-1]],
                                  font=('', 15))

# 绘制得分矩阵和路径
def draw_penalty():
    # 宽度和高度的设定
    ww, wh = 500, 400
    # 先获得相应的绘制内容
    al_score = align(str1.get(), str2.get())
    s1, s2 = list(str1.get()), list(str2.get())
    path, res1, res2 = find_path(al_score, s1, s2)
    for i in range(2):
        s1.insert(0, '')
        s2.insert(0, '')
    # 计算需要绘制的宽度和高度及数量
    score_w, score_h = len(s2), len(s1)
    ws, hs = ww/score_w, wh/score_h
    # 先绘制行和列的标志
    for i in range(score_w):
        canvas.create_rectangle(i*ws, 0, (i+1)*ws, hs, fill='gold')
        canvas.create_text(i*ws+ws/2, hs/2, text=s2[i], font=('', 15))
    for i in range(score_h):
        canvas.create_rectangle(0, i*hs, ws, (i+1)*hs, fill='gold')
        canvas.create_text(ws/2, i*hs+hs/2, text=s1[i], font=('', 15))
    # 依次绘制方格和文本
    for i in range(1, score_w):
        for j in range(1, score_h):
            canvas.create_rectangle(i*ws, j*hs, (i+1)*ws, (j+1)*hs, fill='white')
            canvas.create_text(i*ws+ws/2, j*hs+hs/2, text=al_score[j-1, i-1],
                               font=('', 15))
    # 绘制路径
    for p in path:
        start, end = p[0], p[1]
        canvas.create_line((start[1]+1)*ws+ws/2, (start[0]+1)*hs+hs/2, (end[1]+1)*ws+ws/2,
                          (end[0] + 1)*hs+hs/2, width=4, fill='blue', arrow='last')
        canvas.update()
        time.sleep(1)
    # 显示匹配序列
    tk.Label(frame, width=15, text='对齐序列如下：', font=('黑体', 11)).place(anchor=tk.W,
                                                                 x=50, y=280)
    tk.Label(frame, width=10, text=res1, font=('黑体', 11)).place(anchor=tk.W,
                                                                 x=60, y=310)
    tk.Label(frame, width=10, text=res2, font=('黑体', 11)).place(anchor=tk.W,
                                                                 x=60, y=340)

def delete_canvas():
    canvas.delete(tk.ALL)

if __name__ == '__main__':
    # 窗口的宽度和高度
    wd_width = 700
    wd_height = 400
    window = tk.Tk()
    window.title('Sequence alignment.')
    window.geometry('{}x{}'.format(wd_width, wd_height))
    # 画布及框架的定义
    canvas = tk.Canvas(window, width=wd_width-200, height=400)
    canvas.grid(row=0, column=0)
    frame = tk.Frame(window, width=200, height=400)
    frame.grid(row=0, column=1)
    # 各组件的定义
    str1 = tk.StringVar()
    str2 = tk.StringVar()
    tk.Label(frame, width=5, text='序列1:', font=('黑体', 11)).place(anchor=tk.W, x=0, y=90)
    tk.Label(frame, width=5, text='序列2:', font=('黑体', 11)).place(anchor=tk.W, x=0, y=130)
    se1 = tk.Entry(frame, width=18, textvariable=str1).place(anchor=tk.W, x=50, y=90)
    se2 = tk.Entry(frame, width=18, textvariable=str2).place(anchor=tk.W, x=50, y=130)
    tk.Button(frame, text='确定', width=12, command=draw_penalty).place(anchor=tk.W,
                                                              x=60, y=170)
    tk.Button(frame, text='分数矩阵定义', width=12, command=draw_score).place(anchor=tk.W,
                                                                      x=60, y=210)
    tk.Button(frame, text='清空画布', width=12, command=delete_canvas).place(anchor=tk.W,
                                                                       x=60, y=250)
    window.mainloop()