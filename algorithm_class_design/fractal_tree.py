# coding=utf-8
import turtle as tl
import numpy as np
import random


# 绘制分形树
# 原理：利用分治思想先绘制中间树干，再绘制右分支，最后绘制左分支
def draw_smalltree(tree_length,tree_angle):
    '''
    :param tree_length: 当前树干长度
    :param tree_angle: 当前树枝绘制角度
    '''
    # 每次绘制减小画笔粗细，也即减小树干直径
    tl.pensize(tree_length/10)
    # 每次左右树枝的绘制起点设置
    tree_length = tree_length // 3
    # 当树干的长度小于6时不继续绘制
    if tree_length >= 6:
        # 先绘制中间树干
        tl.forward(3 * tree_length)
        # 回退找到右枝绘制起点
        tl.backward(2 * tree_length)
        # 转换角度
        tl.right(tree_angle)
        # 绘制右枝
        draw_smalltree(2 * tree_length, tree_angle)
        # 当绘制完右枝时，转换方向并移动找到左枝绘制起点
        tl.left(tree_angle)
        tl.forward(tree_length)

        tl.left(tree_angle)
        # 绘制左枝
        draw_smalltree(tree_length, tree_angle)
        # 当树干长度小于9时，改变画笔颜色以绘制树叶
        if tree_length <= 9:
            tl.pencolor('green')
        else:
            tl.pencolor('brown')
        if tree_length <= 9:
            # 右叶子绘制
            tl.left(45)
            # 叶子的填充颜色为绿色
            tl.fillcolor("green")
            # 开始绘制并填充
            tl.begin_fill()
            # 叶子的边缘为圆弧，所以根据圆绘制
            tl.circle(-25, 90)
            tl.right(90)
            tl.circle(-25, 90)
            tl.end_fill()
            tl.left(45 + 180)
            # 左叶子绘制
            tl.right(45)
            tl.fillcolor("green")
            tl.begin_fill()
            tl.circle(-25, 90)
            tl.right(90)
            tl.circle(-25, 90)
            tl.end_fill()
            tl.right(45)
        # 每次绘制完当前时都回退，以保证下一次的绘制
        tl.right(tree_angle)
        tl.backward(2 * tree_length)


def main():
    # 提起画笔，但不绘制
    tl.penup()
    # 设置画笔颜色
    tl.pencolor('brown')
    # 设置画笔初始大小
    tl.pensize(20)
    # 设置画笔速度
    tl.speed('fastest')
    # 调整画笔朝向为向上（默认向右）
    tl.left(90)
    # 向下回退100个像素
    tl.backward(100)
    # 开始绘制
    tl.pendown()
    # 初始树干和树枝角度
    tree_length = 300
    tree_angle = 45
    # 绘制分形树
    draw_smalltree(tree_length,tree_angle)
    # 等待退出
    tl.exitonclick()


if __name__ == '__main__':
    main()