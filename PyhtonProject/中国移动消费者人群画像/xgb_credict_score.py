import pandas as pd
import numpy as np
import xgboost as xgb
from sklearn.model_selection import StratifiedKFold
from sklearn.preprocessing import MinMaxScaler


def feature_deal(train_data):

    train_data['大学生黑名单'] = train_data['是否大学生客户'] & train_data['是否黑名单客户']
    # train_data['用户年龄'] = MinMaxScaler().fit_transform(pd.DataFrame(train_data['用户年龄']))

    a = np.zeros(train_data.shape[0])
    a.fill(np.sum(train_data['当月旅游资讯类应用使用次数']) / train_data['当月旅游资讯类应用使用次数'].shape[0])
    b = np.zeros(train_data.shape[0])
    b.fill((np.sum(train_data['当月飞机类应用使用次数']) + np.sum(train_data['当月火车类应用使用次数'] + np.sum(train_data['当月旅游资讯类应用使用次数']))) / train_data.shape[0])
    train_data['当月旅游资讯类应用使用次数'] = (train_data['当月旅游资讯类应用使用次数'] + a) / (train_data['当月旅游资讯类应用使用次数'] + a + train_data['当月飞机类应用使用次数'] + train_data['当月旅游资讯类应用使用次数'] + train_data['当月火车类应用使用次数'] + b)

    log_features = ['当月网购类应用使用次数', '当月金融理财类应用使用总次数',
                    '当月物流快递类应用使用次数', '当月视频播放类应用使用次数']
    for col in log_features:
        train_data[col] = train_data[col].map(lambda x: np.log1p(x))

    train_data.loc[train_data['用户年龄'] == 0, '用户年龄'] = np.nan
    train_data.loc[train_data['用户话费敏感度'] == 0, '用户话费敏感度'] = np.nan
    train_data.loc[train_data['用户账单当月总费用（元）'] < 8, '用户账单当月总费用（元）'] = np.nan
    train_data.loc[train_data['用户近6个月平均消费值（元）'] == 0, '用户近6个月平均消费值（元）'] = np.nan
    # train_data['用户当月费用比例'] = train_data['用户账单当月总费用（元）'] / (train_data['用户当月账户余额（元）'] + train_data['用户账单当月总费用（元）'] + 1)
    train_data['当前费用稳定性'] = train_data['用户账单当月总费用（元）'] / (train_data['用户近6个月平均消费值（元）'] + 1)
    train_data['当月账单是否超过平均消费额'] = train_data['用户账单当月总费用（元）'] - train_data['用户近6个月平均消费值（元）']
    return train_data


def xgb_model(train_data, test_data):
    label = train_data['信用分']
    use_fea = list(train_data.columns)
    use_fea.remove('用户编码')
    use_fea.remove('信用分')

    train_data = train_data[use_fea]
    test_data = test_data[use_fea]

    train_data = feature_deal(train_data)
    test_data = feature_deal(test_data)
    # print(train_data.shape)

    n_fold = 5
    scores = []
    # 用于stacking中训练和预测数据存储
    train_set = np.zeros(train_data.shape[0])
    test_set = np.zeros(test_data.shape[0])
    skf = StratifiedKFold(n_splits=n_fold, shuffle=True, random_state=2019)
    params = {
        'booster': 'gbtree',
        'learning_rate': 0.05,
        'max_depth': 5,
        'subsample': 0.7,
        'colsample_bytree': 0.8,
        'objective': 'reg:linear',
        'n_estimators': 1000,
        'min_child_weight': 3,
        'gamma': 1,
        'random_state': 2019,
        'reg_alpha': 2,
        'reg_lambda': 3,
        'alpha': 1,
        'eval_metric': 'mae',
        'verbose': 1,
        'nthread': 8
    }
    for i, (train_index, valid_index) in enumerate(skf.split(train_data, label)):
        print('fold: ', i + 1, 'training......')
        x_train, label_train = train_data.iloc[train_index, :], label[train_index]
        x_valid, label_valid = train_data.iloc[valid_index, :], label[valid_index]

        model = xgb.XGBRegressor(**params)
        xgb_model = model.fit(x_train, label_train, eval_set=[(x_train, label_train), (x_valid, label_valid)],
                              early_stopping_rounds=200, verbose=500)

        train_set[valid_index] = xgb_model.predict(x_valid, ntree_limit=xgb_model.best_ntree_limit)
        test_set += xgb_model.predict(test_data, ntree_limit=xgb_model.best_ntree_limit)
        scores.append(xgb_model.best_score)
    print('score ：', 1 / (1 + np.mean(scores)))
    test_set /= 5
    return train_set, test_set


def main():
    train_data = pd.read_csv('train_dataset.csv')
    test_data = pd.read_csv('test_dataset.csv')

    train_set, test_set = xgb_model(train_data, test_data)
    # 运用已知模型测试数据
    # test_predict = pd.Series(np.round(test_set), dtype=int)
    # df = pd.concat([test_data['用户编码'], test_predict], axis=1)
    # 写入文件
    # df.to_csv('result1.csv', encoding='utf-8', header=['id', 'score'], index=False)
    train_set = pd.DataFrame(train_set)
    test_set = pd.DataFrame(test_set)
    train_set.to_csv('xgb_train_stacking.csv', index=False)
    test_set.to_csv('xgb_test_stacking.csv', index=False)


if __name__ == '__main__':
    main()