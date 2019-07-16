import mystack
import time


# 递归求解背包可行方案
def package_recursion(volumes, t, start, stack):
    # 如果背包容量t为负，则退出
    if t < 0:
        return
    # 若背包刚好装满，则输出方案并返回
    elif t == 0:
        stack.print_stack()
        return
    # 对当前start后的物品依次尝试
    for i in range(start, len(volumes)):
        # 在i大于start的条件下，如果两个背包体积相等，则继续
        if i > start and volumes[i] == volumes[i - 1]:
            continue
        # 当前体积入栈
        stack.push(volumes[i])
        # 递归调用，转换为下一个的装入问题
        package_recursion(volumes, t - volumes[i], i + 1, stack)
        # 若当前返回后，则栈顶退栈，继续搜索其他方案
        stack.pop()


# 动态规划求解背包可行方案
def package_iteration(volumes, t):
    # 用set()以去重，比如（1,2）和（2,1）
    dp = [set() for i in range(t + 1)]
    # 第一个初始为空
    dp[0].add(())
    for vol in volumes:
        # 根据每一个物品体积，对背包从后往前搜索
        for i in range(t, vol - 1, -1):
            for j in dp[i - vol]:
                # 添加当前体积的可行方案
                dp[i].add(j + (vol,))
    for i in dp[-1]:
        print(i)


def package_problem(volumes, t, n):
    # 创建一个栈，用于存储可行方案
    stack = mystack.Stack()
    # 对物品体积排序，以达到过程中去重目的
    volumes.sort()
    print('递归算法:')
    start = time.clock()
    package_recursion(volumes, t, 0, stack)
    elapsed = (time.clock() - start)
    print("递归使用时间:", elapsed)

    print('动态规划:')
    start = time.clock()
    package_iteration(volumes, t)
    elapsed = (time.clock() - start)
    print("动态规划使用时间:", elapsed)


def main():
    vol = []
    t = int(input("请输入背包容量："))
    n = int(input("请输入物品个数："))
    x = input('请输入每个物品的体积:')
    x = x.split(' ')
    vol_sum = 0
    for i in x:
        vol.append(int(i))
        vol_sum += int(i)
    if vol_sum < t:
        print('所有物品体积太少，不能装满背包.')
        return
    print('满足条件的解如下：')
    package_problem(vol, t, n)


if __name__ == '__main__':
    main()
