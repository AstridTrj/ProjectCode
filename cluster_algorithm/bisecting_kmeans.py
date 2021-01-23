from sklearn.cluster import KMeans
import k_means as km
import numpy as np
import pandas as pd
import matplotlib as mpl
import sklearn.datasets as ds
from matplotlib import pyplot as plt


# 获取簇平均值
def centroid(data):
    return np.mean(data, 0)


# 计算SSE(误差项平方和)误差值
def sse(data):
    u = centroid(data)
    return np.sum(np.linalg.norm(data - u, 2, 1))


# 二分K-Means算法
# 先把所有的数据看成一个簇
# 计算每个簇的SSE值
# 选择SSE值最大的一个簇进行二分
# 重复以上步骤，直到达到终止条件，此处为聚簇数量
def bisecting_k_means(data_source, max_k, min_error=1e-6):
    # 将所有数据看成一个簇放入列表
    C = [data_source]
    # 簇数量计数
    k = 1
    while True:
        # 计算每个簇的SSE误差值
        sse_list = [sse(data) for data in C]
        old_sse = np.sum(sse_list)
        # 选择SSE值最大的一个簇
        data = np.array(C.pop(np.argmax(sse_list)))
        # 将该簇进行二分
        u = km.rand_center(data[:, 0], data[:, 1], 2)
        result = km.my_k_means(data, u, 2, outer=True)
        # 得到二分后的两个簇，加入簇列表
        clusters = result[2]
        clusters1 = [data[i] for i in range(len(clusters)) if clusters[i] == 0]
        clusters2 = [data[i] for i in range(len(clusters)) if clusters[i] == 1]
        C.append(clusters1)
        C.append(clusters2)
        k += 1
        sse_list = [sse(i_data) for i_data in C]
        now_sse = np.sum(sse_list)
        # 可将误差作为终止条件
        error = now_sse - old_sse
        if k == max_k:
            break
    return now_sse, C


# 手写算法可视化
def result_of_mine(data, k):
    sse_error, clusters = bisecting_k_means(data, k)
    clusters = [np.mat(i) for i in clusters]
    u = [centroid(i) for i in clusters]

    colors = ['r', 'b', 'g', 'y', 'm', 'c']
    for i in range(len(clusters)):
        plt.scatter(list(clusters[i][:, 0]), list(clusters[i][:, 1]), marker='o', s=20, c=colors[i])
        plt.scatter(list(u[i][:, 0]), list(u[i][:, 1]), marker='^', s=100, c='#000000')
    plt.title('二分-Kmeans算法聚类结果')
    plt.show()
    print('手写算法SSE误差值: ', sse_error)


# sklearn包可视化
def result_of_sklearn(data, k):
    cm1 = mpl.colors.ListedColormap(['b', 'y', 'm', 'r'])
    # sklearn包中K-Means实现
    kms = KMeans(n_clusters=k)
    kms.fit(data)
    y_hat = kms.predict(data)
    kms_centers = kms.cluster_centers_
    plt.scatter(data[:, 0], data[:, 1], c=y_hat, s=30, cmap=cm1, edgecolors='none')
    plt.scatter(kms_centers[:, 0], kms_centers[:, 1], c='b', marker='^', s=100, edgecolors='k')
    plt.title('sklearn中kmeans包聚类结果')
    plt.show()
    print('sklearn包聚类SSE误差值：', kms.inertia_)


if __name__ == '__main__':
    # 数据集1
    data = pd.read_csv('iris.csv', header=None)
    x = data[0]
    y = data[1]
    data = np.array(list(zip(x, y)))
    # 数据集2
    # N = 1000
    # centers = [[1, 2], [-1, -1], [1, -1], [-1, 1]]
    # data, y = ds.make_blobs(N, n_features=2, centers=centers, cluster_std=[0.5, 0.25, 0.7, 0.5], random_state=0)

    k = 3
    result_of_mine(data, k)
    result_of_sklearn(data, k)