import pandas as pd
import lightgbm as lgb
import numpy as np
from sklearn import metrics
from scipy import stats
from sklearn.decomposition import PCA
from sklearn.model_selection import StratifiedKFold


def get_features(train_data):
    train_data['当前费用稳定性'] = train_data['用户账单当月总费用（元）'] / (train_data['用户近6个月平均消费值（元）'] + 1)
    # 当月话费/当月账户余额
    train_data['用户当月费用比例'] = train_data['用户账单当月总费用（元）'] / (train_data['用户当月账户余额（元）'] + train_data['用户账单当月总费用（元）'] + 1)
    train_data['旅游率'] = (train_data['当月旅游资讯类应用使用次数']) / (train_data['当月旅游资讯类应用使用次数'] + train_data['当月飞机类应用使用次数'] + train_data['当月火车类应用使用次数'] + 1)
    train_data.loc[train_data['用户年龄'] == 0, '用户年龄'] = np.nan
    train_data['是否完全缴费'] = train_data['缴费用户最近一次缴费金额（元）'] - train_data['用户账单当月总费用（元）']
    train_data['缴费额是否超过平均消费额'] = train_data['缴费用户最近一次缴费金额（元）'] - train_data['用户近6个月平均消费值（元）']
    train_data['交通类应用使用次数'] = train_data['当月飞机类应用使用次数'] + train_data['当月火车类应用使用次数']
    train_data['6个月平均占比总费用'] = train_data['用户近6个月平均消费值（元）'] / (train_data['用户账单当月总费用（元）'] + 1)
    # data['网购率'] = data['当月网购类应用使用次数'] / (data['当月网购类应用使用次数'] + data['当月物流快递类应用使用次数'] + 1)
    # train_data.drop(['是否大学生客户'], axis=1, inplace=True)
    return train_data


def base_process(train_data):
    app_feature = ['当月网购类应用使用次数', '当月物流快递类应用使用次数', '当月金融理财类应用使用总次数',
                   '当月视频播放类应用使用次数', '当月飞机类应用使用次数', '当月火车类应用使用次数',
                   '当月旅游资讯类应用使用次数']
    total_app = 0
    for fea in app_feature:
        total_app += train_data[fea]
    for col in app_feature:
        train_data[col + '_ratio'] = train_data[col] / (total_app + 1)

    train_data['缴费方式'] = 0
    train_data.loc[(train_data['缴费用户最近一次缴费金额（元）'] % 10 == 0) & (train_data['缴费用户最近一次缴费金额（元）'] != 0), '缴费方式'] = 1
    train_data['当月账单是否超过平均消费额'] = train_data['用户账单当月总费用（元）'] - train_data['用户近6个月平均消费值（元）']
    transform_value_feature = ['用户年龄', '用户网龄（月）', '当月通话交往圈人数',
                               '近三个月月均商场出现次数', '当月网购类应用使用次数',
                               '当月物流快递类应用使用次数', '当月金融理财类应用使用总次数',
                               '当月视频播放类应用使用次数','当月火车类应用使用次数',
                               '当月旅游资讯类应用使用次数', '当月飞机类应用使用次数']
    user_fea = ['用户当月账户余额（元）', '用户近6个月平均消费值（元）',
                '用户账单当月总费用（元）','缴费用户最近一次缴费金额（元）']
    log_features = ['当月网购类应用使用次数', '当月金融理财类应用使用总次数',
                    '当月物流快递类应用使用次数', '当月视频播放类应用使用次数']
    # 处理离散点
    for col in transform_value_feature + user_fea + log_features:
        # 取出最高99.9%值
        ulimit = np.percentile(train_data[col].values, 99.9)
        # 取出最低0.1%值
        llimit = np.percentile(train_data[col].values, 0.1)
        train_data.loc[train_data[col] > ulimit, col] = ulimit
        train_data.loc[train_data[col] < llimit, col] = llimit

    for col in user_fea + log_features:
        train_data[col] = train_data[col].map(lambda x: np.log1p(x))

    return train_data


def online_age_class(age):
    if age <= 160:
        return 0
    elif age <= 220:
        return 1
    else:
        return 2


def six_month_average_cost(cost):
    if cost <= 2.9:
        return 0
    elif cost <= 3.6:
        return 1
    elif cost <= 4:
        return 2
    elif cost <= 5.1:
        return 3
    else:
        return 4


def contact_circle_number(number):
    if number <= 75:
        return 0
    elif number <= 200:
        return 1
    else:
        return 2


def user_account_cost(cost):
    if cost <= 130:
        return 0
    elif cost <= 300:
        return 1
    else:
        return 2


def recent_payment_money(money):
    if money <= 100:
        return 0
    elif money <= 300:
        return 1
    else:
        return 2


def comprehensive_grade(grade):
    if grade <= 1:
        return 0
    elif grade <= 5:
        return 1
    elif grade <= 8:
        return 2
    else:
        return 3


def level_sum(train_data):
    train_data['网龄等级'] = train_data['用户网龄（月）'].map(lambda x: online_age_class(x))
    train_data['6个月平均消费等级'] = train_data['用户近6个月平均消费值（元）'].map(lambda x: six_month_average_cost(x))
    train_data['交往圈人数等级'] = train_data['当月通话交往圈人数'].map(lambda x: contact_circle_number(x))
    # train_data['用户账单总费用等级'] = train_data['用户账单当月总费用（元）'].map(lambda x: user_account_cost(x))
    train_data['用户最近缴费金额等级'] = train_data['缴费用户最近一次缴费金额（元）'].map(lambda x: recent_payment_money(x))
    # train_data['用户账单总费用等级'] +
    train_data['综合等级'] = train_data['网龄等级'] + train_data['6个月平均消费等级'] + train_data['交往圈人数等级'] + \
        train_data['用户最近缴费金额等级']
    train_data['综合等级'] = train_data['综合等级'].map(lambda x: comprehensive_grade(x))
    train_data.drop(['网龄等级', '6个月平均消费等级', '交往圈人数等级', '用户最近缴费金额等级'], axis=1, inplace=True)
    return train_data


def cross_validation_predict(train_data, validate_data, metric='mae'):
    label = train_data['信用分']
    use_fea = list(train_data.columns)
    use_fea.remove('用户编码')
    use_fea.remove('信用分')

    train_data = train_data[use_fea]
    test_data = validate_data[use_fea]

    train_data = base_process(train_data)
    test_data = base_process(test_data)

    train_data = get_features(train_data)
    test_data = get_features(test_data)
    # 部分重要特征等级划分
    train_data = level_sum(train_data)
    test_data = level_sum(test_data)
    # return None
    # 测试集结果
    # 特征重要度
    # feature_importance_df = pd.DataFrame()
    # feature_importance_df['feature'] = np.array(use_fea)
    # 交叉验证分数集合
    if metric == 'mae':
        category = 'l1'
    else:
        category = 'l2'
    fold_score = []
    train_set = np.zeros(train_data.shape[0])
    test_set = np.zeros(test_data.shape[0])
    skf = StratifiedKFold(n_splits=5, shuffle=True, random_state=520)
    params = {
        'learning_rate': 0.02,
        'boosting_type': 'gbdt',
        'objective': 'regression_' + category,
        'metric': 'mae',
        'feature_fraction': 0.6,
        'bagging_fraction': 0.8,
        'bagging_freq': 2,
        'num_leaves': 32,
        'verbose': -1,
        'max_depth': 6,
        'reg_alpha': 2.2,
        'reg_lambda': 2,
        'nthread': 8
    }
    for i, (train_fold, validate) in enumerate(skf.split(train_data, label)):
        print('fold: ', i + 1, ' training.....')
        x_train, x_validate = train_data.iloc[train_fold, :], train_data.iloc[validate, :]
        label_train, label_validate = label[train_fold], label[validate]
        data_train = lgb.Dataset(x_train, label_train)
        data_valid = lgb.Dataset(x_validate, label_validate, reference=data_train)

        model = lgb.train(params, data_train, num_boost_round=10000, valid_sets=[data_train, data_valid],
                          verbose_eval=-1, early_stopping_rounds=80)
        train_set[validate] = model.predict(x_validate, num_iteration=model.best_iteration)
        # 测试集结果累加
        test_set += model.predict(test_data, num_iteration=model.best_iteration)
        # 交叉验证得分
        fold_score.append(model.best_score['valid_1']['l1'])
        # 特征重要性
        # feature_importance_df["fold_" + str(i + 1) + "_importance"] = model.feature_importance(
        #     importance_type='gain', iteration=model.best_iteration)
    score_mean = np.mean(fold_score)
    print('score ：', 1 / (1 + score_mean))
    test_set /= 5
    return train_set, test_set


def main():
    train_data = pd.read_csv('train_dataset.csv')
    validate_data = pd.read_csv('test_dataset.csv')

    train_set1, cv_predict1 = cross_validation_predict(train_data, validate_data, metric='mae')
    # return None
    train_set2, cv_predict2 = cross_validation_predict(train_data, validate_data, metric='mse')
    # cv_predict = (cv_predict1 + cv_predict2) / 2
    # test_predict = pd.Series(np.round(cv_predict), dtype=int)
    # df = pd.concat([validate_data['用户编码'], test_predict], axis=1)
    # 写入文件
    # df.to_csv('result_lgb.csv', encoding='utf-8', header=['id', 'score'], index=False)
    train_set1 = pd.DataFrame(train_set1)
    train_set2 = pd.DataFrame(train_set2)
    cv_predict1 = pd.DataFrame(cv_predict1)
    cv_predict2 = pd.DataFrame(cv_predict2)
    train_set = pd.concat([train_set1, train_set2], axis=1)
    test_set = pd.concat([cv_predict1, cv_predict2], axis=1)
    train_set.to_csv('lgb_train_stacking.csv', header=[0, 1], index=False)
    test_set.to_csv('lgb_test_stacking.csv', header=[0, 1], index=False)


if __name__ == '__main__':
    main()