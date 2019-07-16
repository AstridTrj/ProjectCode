import pandas as pd
import numpy as np
import lightgbm as lgb
from sklearn import metrics
from sklearn.model_selection import StratifiedKFold
from sklearn.feature_extraction.text import TfidfVectorizer


def handle(train_data):
    pass


def lgb_model(train_data):
    label = train_data.pop(3)
    train_data.drop([0], axis=1, inplace=True)
    print(label)
    print(train_data)

    params = {
        'num_leaves': 32,
        'objective': 'binary',
        'learning_rate': 0.02,
        "boosting": "gbdt",
        # "feature_fraction": 0.8,
        # "bagging_freq": 1,
        # "bagging_fraction": 0.85,
        # "bagging_seed": 23,
        "metric": 'auc',
        "nthread": 6,
        "verbose": -1
    }
    folds = StratifiedKFold(n_splits=5, shuffle=True, random_state=111)
    # lgb_pre = np.zeros(len(label))

    for i, (train_index, test_index) in enumerate(folds.split(train_data.values, label.values)):
        print('Fold ', i+1, ' training......')
        train = lgb.Dataset(train_data.iloc[train_index], label=label.iloc[train_index])
        test = lgb.Dataset(train_data.iloc[test_index], label=label.iloc[test_index])

        model = lgb.train(params, train, num_boost_round=10000, valid_sets=[train, test],
                          verbose_eval=-1, early_stopping_rounds=80)
        print(model.best_score['valid_1']['auc'])
        # lgb_pre += model.predict(test_data, num_iteration=model.best_iteration)
    # print(model.best_score['valid_1']['auc'])
    # print(lgb_pre / 5)


def main():
    pd.set_option('display.max_columns', 200)
    train_data = pd.read_csv('sample.csv', header=None)
    lgb_model(train_data)
    # print(train_data[3])


if __name__ == '__main__':
    main()