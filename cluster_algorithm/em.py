import numpy as np
import pandas as pd
import matplotlib as mpl
import matplotlib.pyplot as plt
from scipy.stats import multivariate_normal as mvn
from sklearn.mixture import GaussianMixture
from sklearn.metrics import pairwise_distances_argmin

mpl.rcParams['font.sans-serif'] = [u'SimHei']
mpl.rcParams['axes.unicode_minus'] = False


# 扩大函数，方便画图展示
def expand(a, b, rate=0.05):
    d = (b - a) * rate
    return a-d, b+d


# em算法
def gmm_em(data, pis, mus, sigmas, error=0.01, iter_num=200):
    n, p = data.shape
    k = len(pis)

    lld_last = 0
    for i in range(iter_num):
        # E-step
        gama = np.zeros((k, n))
        for j in range((len(mus))):
            for m in range(n):
                gama[j, m] = pis[j] * mvn(mus[j], sigmas[j]).pdf(data[m])
        gama /= gama.sum(0)
        # M-step：求解模型参数
        pis = np.zeros(k)
        for j in range(len(mus)):
            for m in range(n):
                pis[j] += gama[j, m]
        pis /= n

        mus = np.zeros((k, p))
        for j in range(k):
            for m in range(n):
                mus[j] += gama[j, m] * data[m]
            mus[j] /= gama[j, :].sum()

        sigmas = np.zeros((k, p, p))
        for j in range(k):
            for m in range(n):
                ys = np.reshape(data[m] - mus[j], (p, 1))
                sigmas[j] += gama[j, m] * np.dot(ys, ys.T)
            sigmas[j] /= gama[j, :].sum()

        lld_next = 0
        for m in range(n):
            ga = 0
            for j in range(k):
                ga += pis[j] * mvn(mus[j], sigmas[j]).pdf(data[m])
            lld_next += np.log(ga)
        # if np.abs(lld_next - lld_last) < error:
        #     break
        lld_last = lld_next

    return pis, mus, sigmas


# iris数据集测试
def iris():
    iris_feature = u'花萼长度', u'花萼宽度', u'花瓣长度', u'花瓣宽度'
    feature_pairs = [[0, 1], [0, 2], [0, 3], [1, 2], [1, 3], [2, 3]]
    iris_data = pd.read_csv('iris.csv', header=None)
    color = iris_data[4]
    for k, pairs in feature_pairs:
        # 得到分类后的标记数字，用于画图
        color = pd.Categorical(color).codes
        print(iris_feature[k], '+', iris_feature[pairs])
        i_data = np.array(iris_data[[k, pairs]])
        pi = np.random.random(3, )
        pi /= pi.sum()
        mu = np.random.random((3, 2))
        sigma = np.array([np.eye(2)] * 3)
        pi1, mu1, sigma1 = gmm_em(i_data, pi, mu, sigma)
        print('类别概率(手写): ', pi1)

        x1_min, x2_min = i_data.min(axis=0)
        x1_max, x2_max = i_data.max(axis=0)
        x1_min, x1_max = expand(x1_min, x1_max)
        x2_min, x2_max = expand(x2_min, x2_max)
        t1 = np.linspace(x1_min, x1_max, 500)
        t2 = np.linspace(x2_min, x2_max, 500)
        x1, x2 = np.meshgrid(t1, t2)
        x_data = np.stack((x1.flat, x2.flat), axis=1)

        gmm = GaussianMixture(n_components=3, covariance_type='full', random_state=0)
        gmm.fit(i_data)
        print('类别概率(调包): ', gmm.weights_)
        print('BIC: ', gmm.bic(i_data))
        y_data = gmm.predict(x_data)
        y_hat = gmm.predict(i_data)

        index = feature_pairs.index([k, pairs])
        plt.subplot(3, 2, index+1)
        cm_light = mpl.colors.ListedColormap(['#A0FFA0', '#FFA0A0', '#A0A0FF'])
        cm_dark = mpl .colors.ListedColormap(['r', 'g', '#6060FF'])
        plt.pcolormesh(x1, x2, y_data.reshape(x1.shape), cmap=cm_light)
        plt.scatter(i_data[:, 0], i_data[:, 1], s=15, c=color, marker='o', cmap=cm_dark, edgecolors='k')
        plt.xlabel(iris_feature[k], fontsize=10)
        plt.ylabel(iris_feature[pairs], fontsize=10)

        m = np.array([np.mean(i_data[color == i], axis=0) for i in range(3)])
        order = pairwise_distances_argmin(m, gmm.means_, axis=1, metric='euclidean')
        # change创建一个随机元素的数组，此处元素为bool类型
        change = np.empty((3, 150), dtype=np.bool)
        # 改变类别顺序，使其标记顺序为0,1,2
        for i in range(3):
            change[i] = y_hat == order[i]
        for i in range(3):
            y_hat[change[i]] = i
        print('准确率: %.2f%%' % (100 * np.mean(y_hat == color)))

    plt.tight_layout(5)
    plt.suptitle(u'EM算法鸢尾花数据聚类', fontsize=14)
    plt.show()


# 随机数据集
def self_data():
    np.random.seed(0)
    cov1 = np.diag((1, 2))
    n1 = 400
    n2 = 200
    x1 = np.random.multivariate_normal(mean=(3, 2), cov=cov1, size=n1)
    m = np.array(((1, 1), (1, 3)))
    x1 = x1.dot(m)
    x2 = np.random.multivariate_normal(mean=(-1, 10), cov=cov1, size=n2)
    x = np.vstack((x1, x2))

    y = np.array([0] * n1 + [1] * n2)
    color = pd.Categorical(y).codes
    pi = np.random.random(2)
    pi /= pi.sum()
    mu = np.random.random((2, 2))
    sigma = np.array([np.eye(2)] * 2)
    pi1, mu1, sigma1 = gmm_em(x, pi, mu, sigma)
    print('手写算法类别概率pi: ', pi1)

    x1_min, x2_min = x.min(axis=0)
    x1_max, x2_max = x.max(axis=0)
    x1_min, x1_max = expand(x1_min, x1_max)
    x2_min, x2_max = expand(x2_min, x2_max)
    t1 = np.linspace(x1_min, x1_max, 700)
    t2 = np.linspace(x2_min, x2_max, 700)
    x1, x2 = np.meshgrid(t1, t2)
    x_data = np.stack((x1.flat, x2.flat), axis=1)

    gmm = GaussianMixture(n_components=2, covariance_type='full', random_state=0)
    gmm.fit(x)
    y_data = gmm.predict(x_data)
    print('调包类别概率: ', gmm.weights_)

    cm_light = mpl.colors.ListedColormap(['#A0FFA0', '#A0A0FF'])
    cm_dark = mpl.colors.ListedColormap(['r', 'y', '#6060FF'])
    plt.subplot(121)
    plt.scatter(x[:, 0], x[:, 1], s=15, marker='o', edgecolors='k')
    plt.title('原始数据', fontsize=14)
    plt.subplot(122)
    plt.pcolormesh(x1, x2, y_data.reshape(x1.shape), cmap=cm_light)
    plt.scatter(x[:, 0], x[:, 1], s=15, c=color, marker='o', cmap=cm_dark, edgecolors='k')
    plt.title('随机数据EM聚类结果', fontsize=14)
    plt.show()
    norm1 = mvn(mu1[0], sigma1[0])
    norm2 = mvn(mu1[1], sigma1[1])
    tau1 = norm1.pdf(x)
    tau2 = norm2.pdf(x)
    order = pairwise_distances_argmin([mu[0], mu[1]], [mu1[0], mu1[1]], metric='euclidean')
    if order[0] == 0:
        c1 = tau1 > tau2
    else:
        c1 = tau1 < tau2
    acc = np.mean(y == c1)
    print('准确率：%.2f%%' % (100 * acc))


if __name__ == '__main__':
    iris()