import random
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import tkinter as tk
import time

# 棋盘定义
board = np.zeros((1, 1))
# 颜色列表
colors = ['red', 'orange', 'gray', 'green', 'blue', '#726dd1']
# 缺失坐标定义
holex = holey = 0

# 图形绘制
def draw_board(canvas):
    # 计算窗口长度和宽度
    width = (len(board) + 2) * 30
    height = width
    # 配置画布canvas的长宽
    canvas.config(width=width, height=height)

    global holex, holey
    # 首先绘制出缺失方块
    canvas.create_rectangle(30 * (holey + 1), 30 * (holex + 1), 30 * (holey + 2), 30 * (holex + 2), fill='black',
                            outline='')
    # 更新操作和睡眠操作，以实现动画效果
    canvas.update()
    # time.sleep(0.01)
    # 根据棋盘每个点的值选择对应的颜色进行绘制
    for i in range(len(board)):
        for j in range(len(board)):
            if board[i, j] != 0:
                # 取余选择颜色
                color = colors[int(board[i, j] % len(colors))]
                canvas.create_rectangle(30 * (j + 1), 30 * (i + 1), 30 * (j + 2), 30 * (i + 2), fill=color, outline='')
    # 睡眠过渡并更新画布
    time.sleep(0.05)
    canvas.update()


# Trimino算法实现
# 算法原理
# 1.每次都从棋盘中心开始，将棋盘划分为四等份
# 2.在三个没有缺失方块的区域放置L行方块
# 3.对四等份递归执行步骤1,2，直到只剩下一个方块
def tromino(hi, hj, start_x, start_y, end_x, end_y, can):
    '''
    hi，hj：缺失坐标
    start_x, start_y：棋盘起点（左上角）
    end_x, end_y：棋盘终点（右下角）
    can：画布
    '''
    # 如果只剩一个点时，结束算法
    if start_x == end_x and start_y == end_y:
        return

    global board
    # 绘制当前的画布
    draw_board(can)
    # 每次递归随机选择颜色值
    color = random.randint(1, 8)

    # 寻找中心点（偏向于左上角一个点）
    cx, cy = (start_x + end_x) // 2, (start_y + end_y) // 2
    # 缺失方块在第一象限
    if hi <= cx and hj > cy:
        # 设置L型方块的放置区域
        board[cx, cy] = board[cx+1, cy] = board[cx+1, cy+1] = color
        # 确定好不同等份的缺失位置及起点和终点，并对四等份递归执行Tromino算法
        tromino(hi, hj, start_x, cy+1, cx, end_y, can)
        tromino(cx, cy, start_x, start_y, cx, cy, can)
        tromino(cx+1, cy, cx+1, start_y, end_x, cy, can)
        tromino(cx+1, cy+1, cx+1, cy+1, end_x, end_y, can)
    # 缺失方块在第二象限
    elif hi <= cx and hj <= cy:
        board[cx, cy+1] = board[cx+1, cy] = board[cx+1, cy+1] = color
        tromino(cx, cy+1, start_x, cy+1, cx, end_y, can)
        tromino(hi, hj, start_x, start_y, cx, cy, can)
        tromino(cx+1, cy, cx+1, start_y, end_x, cy, can)
        tromino(cx+1, cy+1, cx+1, cy+1, end_x, end_y, can)
    # 缺失方块在第三象限
    elif hi > cx and hj <= cy:
        board[cx, cy] = board[cx, cy+1] = board[cx+1, cy+1] = color
        tromino(cx, cy+1, start_x, cy+1, cx, end_y, can)
        tromino(cx, cy, start_x, start_y, cx, cy, can)
        tromino(hi, hj, cx+1, start_y, end_x, cy, can)
        tromino(cx+1, cy+1, cx+1, cy+1, end_x, end_y, can)
    # 缺失方块在第四象限
    elif hi > cx and hj > cy:
        board[cx, cy] = board[cx, cy+1] = board[cx+1, cy] = color
        tromino(cx, cy+1, start_x, cy+1, cx, end_y, can)
        tromino(cx, cy, start_x, start_y, cx, cy, can)
        tromino(cx+1, cy, cx+1, start_y, end_x, cy, can)
        tromino(hi, hj, cx+1, cy+1, end_x, end_y, can)


def main():
    global board, holex, holey, window, canvas
    # 输入棋盘规模
    n = int(input('输入棋盘规模：'))
    n = 2**n
    # 随机产生缺失方块位置
    holex, holey = random.randint(1, n-1), random.randint(1, n-1)
    board = np.zeros((n, n))
    # 窗口的定义
    window = tk.Tk()
    window.title("Tromino谜题")
    # 画布定义，放置到窗口上
    canvas = tk.Canvas(window, bg="white")
    canvas.pack()
    # 初始化画布
    canvas.create_rectangle(30 * (holey + 1), 30 * (holex + 1), 30 * (holey + 2), 30 * (holex + 2), fill='black',
                            outline='')
    canvas.update()
    time.sleep(0.05)
    # 执行算法并不断更新画布
    tromino(holex, holey, 0, 0, n-1, n-1, canvas)
    draw_board(canvas)
    print(board)
    window.mainloop()


if __name__ == '__main__':
    main()