import numpy as np
import matplotlib as mpl
import matplotlib.pyplot as plt
from sklearn import datasets
import bisecting_kmeans as bk
from sklearn.cluster import KMeans
from sklearn.cluster import SpectralClustering


mpl.rcParams['font.sans-serif'] = [u'SimHei']
mpl.rcParams['axes.unicode_minus'] = False


def distance(a, b):
    return np.sum(np.power(a - b, 2))


# 每个数据点之间的距离计算，返回一个含距离的矩阵
def distance_matrix(data):
    s_matrix = np.zeros((len(data), len(data)))

    for i in range(len(data)):
        for j in range(i + 1, len(data)):
            s_matrix[i][j] = distance(data[i], data[j])
            s_matrix[j][i] = s_matrix[i][j]
    return s_matrix


# 根据kNN算法原理求解邻接矩阵
def neighbour_matrix(s, k, sigma=1.0):
    n = len(s)
    w_matrix = np.zeros((n, n))

    for i in range(n):
        # 获取距离和下标对
        dis_and_index = zip(s[i], range(n))
        # 对距离下标对排序，以得到前k个最近的距离
        dis_and_index = sorted(dis_and_index, key=lambda x: x[0])
        # 排序后得到前k个距离的下标
        k_index = [dis_and_index[index][1] for index in range(k)]

        # 对选取的k个下标计算相似度,其余则保留为0
        for j in k_index:
            w_matrix[i][j] = np.exp(-s[i][j] / 2 / sigma / sigma)
            w_matrix[j][i] = w_matrix[i][j]
    return w_matrix


# 返回标准化后的拉普拉斯矩阵
def laplacian(w_matrix):
    d_matrix = np.diag(np.sum(w_matrix, axis=1))
    l_matrix = d_matrix - w_matrix
    d1_matrix = np.linalg.inv(d_matrix ** 0.5)
    return np.dot(np.dot(d1_matrix, l_matrix), d1_matrix)

if __name__ == '__main__':
    t = np.arange(0, 2*np.pi, 0.1)
    data1 = np.vstack((np.cos(t), np.sin(t))).T
    data2 = np.vstack((2*np.cos(t), 2*np.sin(t))).T
    data3 = np.vstack((3*np.cos(t), 3*np.sin(t))).T
    data = np.array(np.vstack((data1, data2, data3)))

    s = distance_matrix(data)
    w = neighbour_matrix(s, 3)
    l = laplacian(w)

    l, h = np.linalg.eig(l)
    l_index = zip(l, range(len(l)))
    l_index = sorted(l_index, key=lambda x: x[0])
    h = np.array([h[l_index[i][1]] for i in range(3)]).T
    # 将得到的前k个特征向量进行转化
    for i in range(3):
        for j in range(len(h[:, i])):
            if h[j, i] != 0:
                h[j, i] = i + 1

    k_model = KMeans(n_clusters=3)
    y_hat = k_model.fit(h).labels_
    colors = mpl.colors.ListedColormap(['r', 'b', 'g', 'y', 'm', 'c', '#A0FFA0', '#FFA0A0', '#A0A0FF', '#60FF06'])
    plt.scatter(data[:, 0], data[:, 1], s=35, c=y_hat, marker='o', cmap=colors, edgecolors='k')
    plt.title('谱聚类算法聚类结果')
    plt.show()

    sp_model = SpectralClustering(n_clusters=3, gamma=0.16)
    y_sp = sp_model.fit(data).labels_
    plt.scatter(data[:, 0], data[:, 1], s=35, c=y_sp, marker='o', cmap=colors, edgecolors='k')
    plt.title('sklearn中SpectralClustering包聚类结果')
    plt.show()
