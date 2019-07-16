import pandas as pd
import seaborn as sns
import numpy as np
import matplotlib as mpl
import matplotlib.pyplot as plt
from sklearn.mixture import GaussianMixture
from scipy import stats
from mpl_toolkits.mplot3d import Axes3D

mpl.rcParams['font.sans-serif'] = ['SimHei']


def analysis(train_data, test_data):
    feature = '用户账单当月总费用（元）'
    # train_data[feature] = train_data[feature].map(lambda x: np.log1p(x))
    # three_d = Axes3D(plt.figure())
    # three_d.scatter(train_data['用户话费敏感度'], train_data['信用分'], train_data['用户年龄'], edgecolors='k')
    ulimit = np.percentile(train_data[feature].values, 99.9)
    llimit = np.percentile(train_data[feature].values, 0.1)
    train_data.loc[train_data[feature] > ulimit, feature] = ulimit
    train_data.loc[train_data[feature] < llimit, feature] = llimit

    # plt.scatter(train_data[feature], train_data['信用分'], s=10, edgecolors='k')
    sns.distplot(train_data[feature], kde=True)
    print(np.percentile(train_data[feature], 25))
    print(np.percentile(train_data[feature], 50))
    print(np.percentile(train_data[feature], 75))
    print(np.mean(train_data[feature]))
    plt.show()


if __name__ == '__main__':
    train_data = pd.read_csv('train_dataset.csv')
    test_data = pd.read_csv('test_dataset.csv')
    label = train_data['信用分']
    train_data.drop(['用户编码', '信用分'], axis=1)
    test_data.drop(['用户编码'], axis=1)
    gmm = GaussianMixture(n_components=5, covariance_type='full', random_state=1)
    gmm.fit(train_data, label)
    y = gmm.predict(test_data)
    print(y)
    # analysis(train_data, test_data)
