from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_absolute_error
from sklearn import linear_model
import pandas as pd
import numpy as np

# 数据读入
path = "E:\\kaifabu\\MathematicalModeling\\jpn\\k1.csv"
data = pd.read_csv(path)


# 测试集和训练集构造
train_X = data.loc[data['年份']!=2017][['年末总人口（万人）', '面积', '密度', '农业人口','非农业人口（万人）']].values
train_y = (data.loc[data['年份']!=2017]['地区生产总值（万元）']/10000000).values
test_X = data.loc[data['年份']==2017][['年末总人口（万人）','面积', '密度', '农业人口','非农业人口（万人）']].values
test_y = (data.loc[data['年份']==2017]['地区生产总值（万元）']/10000000).values


# 数据标准化
scaler = StandardScaler()
train_X = scaler.fit_transform(train_X)
test_X = scaler.transform(test_X)


# 模型训练
def train_model(clf, train_X, test_X, train_y, test_y):
    clf.fit(train_X, train_y)
    pre = clf.predict(test_X)
    mae = mean_absolute_error(test_y, pre)
    print("mae: ", mae)
    print("权重: ", clf.coef_)
    print("偏置： ", clf.intercept_)


# 声明模型
clf = linear_model.LinearRegression()


# 训练模型
train_model(clf, train_X, test_Xs, train_y, test_y)
