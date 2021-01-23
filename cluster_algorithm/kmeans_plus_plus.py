import copy
import numpy as np
import pandas as pd
import sklearn.datasets as ds
from sklearn.cluster import KMeans
import k_means as km
import matplotlib as mpl
from matplotlib import pyplot as plt


# kmeans++算法
# 初始任意寻找一个均值向量
# 记录其它数据点到最近均值向量的距离
# 选择记录距离最大的点作为下一个均值向量
# 重复上述步骤，直至选择到目标均值向量个数
class KMeansPlusPlus:

    # 初始化数据，包括均值向量数目、数据集数量、聚类个数、数据列表
    # 然后调用函数随机选择一个均值向量以及寻找剩下的均值向量
    def __init__(self, data, k):
        self.centroid_count = 0
        self.data_count = len(data)
        self.cluster_num = k
        self.data = list(data)
        self.centroid_list = []
        self.random_first_centroid()
        self.find_other_centroids()

    # 任意寻找一个均值向量作为第一个均值向量
    def random_first_centroid(self):
        first_index = np.random.randint(0, len(self.data) - 1)
        self.centroid_list.append(self.remove_data(first_index))
        self.centroid_count = 1

    # 删除数据集中对应index的数据，表示此数据不能作为下一个均值向量
    # 返回一个删除了index对应数据的数据列表
    def remove_data(self, index):
        new_centroid = self.data[index]
        del self.data[index]
        return new_centroid

    # 在剩下的数据中找到另外的k-1个均值向量
    def find_other_centroids(self):
        while not self.if_end():
            distances = self.calculate_small_distance()
            index = self.choose_by_weighted(distances)
            self.centroid_list.append(self.remove_data(index))
            self.centroid_count += 1

    # 记录每个数据点到最近均值向量的距离，返回一个含有 剩下数据点 个数的距离列表
    def calculate_small_distance(self):
        distance_list = []
        for i_data in self.data:
            distance_list.append(self.distance_of_nearest_centroid(i_data))

        return distance_list

    # 计算剩下一个数据点到最近均值向量的距离
    def distance_of_nearest_centroid(self, i_data):
        min_distance = np.inf

        for c in self.centroid_list:
            dis = self.distance(c, i_data)
            if min_distance > dis:
                min_distance = dis
        return min_distance

    # 根据每个距离的权重选择下一个均值向量，距离越远，概率越大
    def choose_by_weighted(self, distance):
        distance_list = [x ** 2 for x in distance]
        weighted_list = self.weight_values(distance_list)
        # 通过选择下标来选择对应的数据
        indexs = [i for i in range(len(distance_list))]
        return np.random.choice(indexs, p=weighted_list)

    # 计算两个数据之间的二范式距离
    def distance(self, x1, x2):
        x1 = np.asarray(x1)
        x2 = np.asarray(x2)
        return np.linalg.norm(x2 - x1)

    # 根据距离生成的权重，范围[0,1]
    def weight_values(self, dis_list):
        dis_sum = np.sum(dis_list)
        return [x / dis_sum for x in dis_list]

    # 是否结束的判断
    def if_end(self):
        end = False
        if self.centroid_count == self.cluster_num:
            end = True

        return end

    # 返回最后寻找到的均值向量
    def final_centroids(self):
        return self.centroid_list


if __name__ == '__main__':
    # 数据1
    # N = 1000
    # centers = [[1, 2], [-1, -1], [1, -1], [-1, 1]]
    # data, y = ds.make_blobs(N, n_features=2, centers=centers, cluster_std=[0.5, 0.25, 0.7, 0.5], random_state=0)
    # 数据2
    data = pd.read_csv('iris.csv', header=None)
    x = data[0]
    y = data[1]
    data = list(zip(x, y))

    # 使用kmeans算法找到k个初始均值向量，并用标准kmeans算法聚类
    k = 3
    kmp = KMeansPlusPlus(data, k)
    centroids = np.array(kmp.final_centroids())
    result = km.my_k_means(np.array(data), np.array(centroids), k, outer=True)
    data = np.array(data)

    # 画图，展示聚类结果
    cm_light = mpl.colors.ListedColormap(['#A0FFA0', '#FFA0A0', '#A0A0FF'])
    cm_dark = mpl.colors.ListedColormap(['r', 'g', '#6060FF', 'y'])
    plt.scatter(data[:, 0], data[:, 1], s=20, c=result[2], marker='o', cmap=cm_dark, edgecolors='k')
    plt.scatter(result[1][:, 0], result[1][:, 1], s=100, marker='D', c='b')
    plt.title('K-means++算法聚类结果')
    plt.show()
    # DBI和DI指数计算
    print('手写算法性能评价：', end='')
    km.dbi_and_di(data, result[1], result[2])

    # sklearn中kmeans包聚类结果
    kms = KMeans(n_clusters=k)
    kms.fit(data)
    y_hat = kms.predict(data)
    kms_centers = kms.cluster_centers_
    plt.scatter(data[:, 0], data[:, 1], c=y_hat, s=10, cmap=cm_dark, edgecolors='none')
    plt.scatter(kms_centers[:, 0], kms_centers[:, 1], c=range(k), marker='^', s=100, edgecolors='none')
    plt.title('sklearn包中Kmeans聚类结果')
    plt.show()
    print('sklearn调包性能评价：', end='')
    km.dbi_and_di(data, kms_centers, y_hat)