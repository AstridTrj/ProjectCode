import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib as mpl
import sklearn.datasets as ds
from sklearn.cluster import DBSCAN


mpl.rcParams['font.sans-serif'] = [u'SimHei']
mpl.rcParams['axes.unicode_minus'] = False


# 访问和未访问的数据记录
class Visit:
    def __init__(self, count=0):
        self.unvisited = [i for i in range(count)]
        self.visited = list()
        self.unvisited_num = count

    def visit(self, index):
        self.visited.append(index)
        self.unvisited.remove(index)
        self.unvisited_num -= 1


# 计算两个数据点之间的欧几里得距离
def distance(a, b):
    return np.sqrt(np.power(a - b, 2).sum())


# DBSCAN算法
# DBSCAN通过检查数据集中每点的eps邻域来搜索簇
# 如果该点的eps邻域包含的点多于MinPts个，则创建一个以该点为核心对象的簇
# 然后迭代地聚集从这些核心对象直接密度可达的对象
# 当没有新的点添加到任何簇时，该过程结束
def dbscan(data, epsilon, min_pts):
    n = data.shape[0]
    # 标记所有点为未访问
    visit_data = Visit(count=n)
    # 初始化簇标记列表c，簇标记为k
    k = -1
    c = [-1 for i in range(n)]
    # 当数据集中的点未访问完时，继续循环
    while visit_data.unvisited_num > 0:
        # 随机选择一个未访问的对象i_data
        i_data = np.random.choice(visit_data.unvisited)
        # 标记为已访问
        visit_data.visit(i_data)
        # 计算距离小于等于eps的数据点
        point_list = [i for i in range(n) if distance(data[i], data[i_data]) <= epsilon]
        # 如果eps领域类的点数目大于等于min_pts则进入if
        if len(point_list) >= min_pts:
            # 创建新簇（k加1），并把i_data添加进去，此处由于c是标记列表，则直接赋值即可
            k += 1
            c[i_data] = k
            # 对于i_data的eps领域内的所有点进行访问
            for point in point_list:
                if point in visit_data.unvisited:
                    visit_data.visit(point)
                    # point的eps领域内的点
                    other_point_list = [i for i in range(n) if distance(data[i], data[point]) <= epsilon]
                    if len(other_point_list) >= min_pts:
                        # 如果other_point_list中的点不在point_list中，则添加进去
                        for i in other_point_list:
                            if i not in point_list:
                                point_list.append(i)
                    # 如果point未被标记为另外的簇，则标记为当前簇
                    if c[point] == -1:
                        c[point] = k
        # 都不是则标记为噪声点
        else:
            c[i_data] = -1
    return c


if __name__ == '__main__':

    #数据集1 (0.3, 3)
    # t = np.arange(0, 2*np.pi, 0.1)
    # data1 = np.vstack((np.cos(t), np.sin(t))).T
    # data2 = np.vstack((2*np.cos(t), 2*np.sin(t))).T
    # data3 = np.vstack((3*np.cos(t), 3*np.sin(t))).T
    # data = np.vstack((data1, data2, data3))

    #数据集2 (0.26,5)
    N = 1000
    centers = [[1, 2], [-1, -1], [1, -1], [-1, 1]]
    data, y = ds.make_blobs(N, n_features=2, centers=centers, cluster_std=[0.5, 0.25, 0.6, 0.5], random_state=0)

    eps = 0.3
    minpts = 3
    # 手写算法可视化
    c = dbscan(data, eps, minpts)
    colors = mpl.colors.ListedColormap(['r', 'b', 'g', 'y', 'm', 'c', '#A0FFA0', '#FFA0A0', '#A0A0FF', '#60FF06'])
    c_num = (1 if -1 in c else 0)
    print('聚类簇数目：', len(set(c)) - c_num)
    plt.scatter(data[:, 0], data[:, 1], s=35, c=c, marker='o', cmap=colors, edgecolors='k')
    plt.title('DBSCAN算法聚类结果\n(ε=0.3，minpts=3)')
    plt.show()
    # sklearn调包实现及可视化
    model = DBSCAN(eps=eps, min_samples=minpts)
    model.fit(data)
    y_hat = model.labels_
    plt.scatter(data[:, 0], data[:, 1], s=35, c=y_hat, marker='o', cmap=colors, edgecolors='k')
    plt.title('skelarn中DBSCAN包聚类结果\n(ε=0.3，minpts=3)')
    plt.show()
