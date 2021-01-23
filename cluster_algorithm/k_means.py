import numpy as np
import pandas as pd
import copy
import matplotlib as mpl
from matplotlib import pyplot as plt
import sklearn.datasets as ds
from sklearn.cluster import KMeans

mpl.rcParams['font.sans-serif'] = [u'SimHei']
mpl.rcParams['axes.unicode_minus'] = False


# 距离计算，二范式求法
def distance(a, b, ax=1):
    return np.linalg.norm(a - b, axis=ax)


# 距离计算，两个数据直接计算时使用
def dis1(a, b):
    return np.sqrt(np.sum(np.power(a - b, 2)))


def rand_center(data_x, data_y, k):
    # 初始均值向量生成，从数据中随机选择k个
    center_index = np.random.randint(len(data_x), size=k)
    center_x =[]
    center_y = []
    for i in center_index:
        center_x.append(data_x[i])
        center_y.append(data_y[i])
    # 将质心合成矩阵形式
    center = np.array(list(zip(center_x, center_y)))
    return center


# k—means聚类
# 参数data_source：数据集，center：均值向量，k：需要聚类的簇数目
def my_k_means(data_source, center, k, outer=False):
    # 记录上一个均值向量，用于与当前均值向量比较，是否继续迭代
    last_center = np.zeros(center.shape)
    # 数据集每个数据的分类情况记录，数据对应的类别为对应均值向量的下标
    clusters = np.zeros(len(data_source))
    # 误差变量，即通过距离计算当前和上一个均值向量的误差，以判断是否继续迭代
    error = distance(center, last_center, None)
    while error != 0:
        # 计算每个数据到所有均值向量的距离，用cluster记录最短距离均值向量的下标
        # 将该数据的簇类别记录为cluster
        for i in range(len(data_source)):
            dis = distance(data_source[i], center)
            cluster = np.argmin(dis)
            clusters[i] = cluster
        # 复制上一个均值向量
        last_center = copy.deepcopy(center)
        # 根据每个簇的数据点更新当前均值向量，即通过取平均得到
        for i in range(k):
            points = [data_source[j] for j in range(len(data_source)) if clusters[j] == i]
            center[i] = np.mean(points, axis=0)
        error = distance(center, last_center, None)

    if not outer:
        colors = ['r', 'g', 'b', 'y', 'c', 'm']
        fig, ax = plt.subplots()
        for i in range(k):
            points = np.array([data_source[j] for j in range(len(data_source)) if clusters[j] == i])
            ax.scatter(points[:, 0], points[:, 1], s=30, c=colors[i], edgecolors='k')
        ax.scatter(center[:, 0], center[:, 1], marker='*', s=200, c='#050505')
        plt.title('K-Means聚类结果')
        plt.show()
    return data_source, center, clusters


# 性能度量DBI（DB指数）和DI
def dbi_and_di(data_source, center, clusters):
    # 获取簇的个数
    k = len(center)
    # average用于存储簇内平均值，diam存储簇内最大距离
    average = []
    diam = []
    # points存储聚类后得到的簇
    points = []
    # 以下方法即将标记后的数据点分开到各个簇中
    for c in range(k):
        points.append([data_source[j] for j in range(len(data_source)) if clusters[j] == c])
    # 计算average和diam
    for point in points:
        dis = 0
        max_dis = 0
        p_num = len(point)
        for i in range(0, p_num - 1):
            for j in range(i + 1, p_num):
                each_dis = dis1(point[i], point[j])
                if each_dis > max_dis:
                    max_dis = each_dis
                    dis += each_dis
        ave = float(2)/(p_num * (p_num - 1)) * dis
        average.append(ave)
        diam.append(max_dis)
    # 计算簇间最小样本的距离，存储在dis_min中
    dis_min = []
    for i in range(0, k - 1):
        for j in range(i + 1, k):
            c_min = np.inf
            for m in points[i]:
                for n in points[j]:
                    dis = dis1(m, n)
                    if dis < c_min:
                        c_min = dis
            dis_min.append(c_min)
    # 计算dbi和di
    dbi = 0
    for i in range(k):
        the_max = 0
        for j in range(k):
            if i != j:
                each_dbi = (average[i] + average[j]) / dis1(center[i], center[j])
                if each_dbi > the_max:
                    the_max = each_dbi
        dbi += the_max
    dbi = dbi / k
    di = min(dis_min) / max(diam)
    print('DBI:', dbi, ',    DI:', di)


if __name__ == '__main__':
    # 数据1
    # data = pd.read_csv('iris.csv', header=None)
    # 数据2
    N = 1000
    centers = [[1, 2], [-1, -1], [1, -1], [-1, 1]]
    data, y = ds.make_blobs(N, n_features=2, centers=centers, cluster_std=[0.5, 0.25, 0.7, 0.5], random_state=0)
    # 处理数据，转化为矩阵形式
    x = data[:, 0]
    y = data[:, 1]
    data = np.array(list(zip(x, y)))

    k = 4
    centers = rand_center(x, y, k)
    # 调用实现算法
    data, centers, cluster = my_k_means(data, centers, k)
    print('手写算法性能评价：', end='')
    dbi_and_di(data, centers, cluster)

    cm1 = mpl.colors.ListedColormap(['b', 'y', 'm', 'r'])
    cm_dark = mpl.colors.ListedColormap(['r', 'g', '#6060FF'])
    # sklearn包中K-Means实现
    kms = KMeans(n_clusters=k)
    kms.fit(data)
    y_hat = kms.predict(data)
    kms_centers = kms.cluster_centers_
    plt.scatter(data[:, 0], data[:, 1], c=y_hat, s=30, cmap=cm1, edgecolors='none')
    plt.scatter(kms_centers[:, 0], kms_centers[:, 1], c='b', marker='^', s=100, edgecolors='k')
    plt.title('sklearn中kmeans包聚类结果')
    plt.show()
    print('sklearn包性能评价：', end='')
    dbi_and_di(data, kms_centers, y_hat)

