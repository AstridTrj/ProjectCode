import numpy as np


# flag用于记录每一点的最大滑雪长度
flag = np.zeros((6, 6))
# 记录每一点的高度
height = np.zeros((6, 6))
# 数据行数和列数
row = 0
column = 0
# 记录最大滑雪路线
route = []


# 动态规划记忆化搜索解决滑雪场问题
def dp(x, y):
    # 使用全局变量
    global row, column
    # m记录当前点的最大滑雪长度
    m = 0
    # 如果该点已经被记录过则直接返回
    if flag[x, y] > 0:
        return flag[x, y]
    # 该点未被记录过时，依次搜索四个方向上的四点的最大长度值。
    # 如果当前的高度比周围高，则求得周围最大值加1作为当前的最大滑雪长度
    # 左
    if y - 1 >= 0:
        # 如果当前的高度比左边高，则记录左边的最大滑雪长度
        if height[x, y] > height[x, y - 1]:
            m = max(m, dp(x, y - 1))
    # 右
    if y + 1 < column:
        if height[x, y] > height[x, y + 1]:
            m = max(m, dp(x, y + 1))
    # 上
    if x - 1 >= 0:
        if height[x, y] > height[x - 1, y]:
            m = max(m, dp(x - 1, y))
    # 下
    if x + 1 < row:
        if height[x, y] > height[x + 1, y]:
            m = max(m, dp(x + 1, y))
    # 如果四周的高度都比当前高，则当前为初始值0加1
    # 若有比当前点高度低的，则当前点最大滑雪长度为周围低的点中最大滑雪长度的最大值加1
    flag[x, y] = m + 1
    return flag[x, y]


# 找出最大滑雪长度的路线
def long_route(x, y):
    # 全局变量使用
    global row, column
    # 如果当前的最大滑雪长度为1，则表示达到终点，直接返回
    if flag[x, y] == 1:
        return
    # 依次判断四个方向，找出周围的最大滑雪长度为当前点滑雪长度减1的点
    if x - 1 >= 0:
        # 如果满足为当前点的最大滑雪长度减1
        if flag[x - 1, y] == flag[x, y] - 1:
            # 将下一点添加入路线列表，同时进入下一点的搜索
            route.append(height[x - 1, y])
            long_route(x - 1, y)
            # 完成搜索时，则返回
            return
    if x + 1 < row:
        if flag[x + 1, y] == flag[x, y] - 1:
            route.append(height[x + 1, y])
            long_route(x + 1, y)
            return
    if y - 1 >= 0:
        if flag[x, y - 1] == flag[x, y] - 1:
            route.append(height[x, y - 1])
            long_route(x, y - 1)
            return
    if y + 1 < column:
        if flag[x, y + 1] == flag[x, y] - 1:
            route.append(height[x, y + 1])
            long_route(x, y + 1)
            return


def main():
    global row, column
    row = int(input("请输入区域行数："))
    column = int(input("请输入区域列数："))
    print('请输入区域各点高度：')
    # 输入信息
    for i in range(row):
        line = input()
        line = line.split(' ')
        for j in range(column):
            height[i, j] = int(line[j])
    # 对每一个点搜索，记录最大滑雪长度
    for i in range(row):
        for j in range(column):
            dp(i, j)
    # flag的大小
    r, c = flag.shape
    # 过去最大值的位置
    pos = flag.argmax()
    # 得到最大值对的坐标
    x, y = divmod(pos, c)
    route.append(height[x, y])
    # 求解路线
    long_route(x, y)
    print('滑雪场最长滑道的长度为：', int(flag.max()))
    print('滑雪路线如下：', list(map(int, route)))


if __name__ == '__main__':
    main()