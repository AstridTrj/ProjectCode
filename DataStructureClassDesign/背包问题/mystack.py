import numpy as np


# 栈结点元素
class Node:
    # 一个结点包含一个值value和指向下一个结点的指针域
    def __init__(self, value):
        self.value = value
        self.next = None


class Stack:
    # 初始化头指针为空
    def __init__(self):
        self.top = None

    # 元素入栈，通过指针域连接
    def push(self, value):
        node = Node(value)
        node.next = self.top
        self.top = node

    #  元素出栈，栈空时抛出异常
    def pop(self):
        node = self.top
        if node == None:
            raise  Exception('Empty stack.')
        # 改变指针域，删除栈顶结点，并将弹出的值返回
        self.top = node.next
        return node.value

    # 返回栈顶元素值，不删除栈顶结点
    def peek(self):
        node = self.top
        if node == None:
            raise Exception('Empty stack.')
        return node.value

    # 判断栈是否为空
    def is_empty(self):
        return not self.top

    # 求当前栈的大小
    def size(self):
        node = self.top
        count = 0
        if node == None:
            raise Exception('Empty stack.')
        # 循环计数，求出栈大小
        while node is not None:
            count += 1
            node = node.next
        return count

    def print_stack(self):
        values = []
        node = self.top
        if node == None:
            print('Empty stack.')
        while node is not None:
            values.append(node.value)
            node = node.next
        values = values[::-1]
        print(values)

