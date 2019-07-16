import pandas as pd
import numpy as np
from sklearn.linear_model import BayesianRidge
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import RepeatedKFold


def model_train(train_data, test_data, label_data):
    label = label_data['信用分']
    off_stack = np.zeros(train_data.shape[0])
    predict = np.zeros(test_data.shape[0])
    rk = RepeatedKFold(n_splits=5, n_repeats=2, random_state=2019)
    for i, (train_index, test_index) in enumerate(rk.split(train_data, label)):
        print('Fold ', i)
        trn_data, trn_y = train_data.iloc[train_index], label[train_index]
        val_data, val_y = train_data.iloc[test_index], label[test_index]

        model = BayesianRidge()
        model.fit(trn_data, trn_y)
        off_stack[test_index] = model.predict(val_data)
        predict += model.predict(test_data)
    metrics = mean_absolute_error(label.values, off_stack)
    mae = 1 / (1 + metrics)
    print(mae)
    predict = predict / 10
    print(predict)
    test_predict = pd.Series(np.round(predict), dtype=int)
    code_data = pd.read_csv('test_dataset.csv')
    df = pd.concat([code_data['用户编码'], test_predict], axis=1)
    print(df)
    df.to_csv('stacking_ending_324.csv', encoding='utf-8', header=['id', 'score'], index=False)


def main():
    train_data = pd.read_csv('train_dataset.csv')
    train_set1 = pd.read_csv('lgb_train_stacking.csv')
    test_set1 = pd.read_csv('lgb_test_stacking.csv')
    train_set2 = pd.read_csv('xgb_train_stacking.csv')
    test_set2 = pd.read_csv('xgb_test_stacking.csv')
    train_set3 = pd.read_csv('train_03-23-18-49.csv')
    test_set3 = pd.read_csv('lgb_baseline_03-23-20-54.csv')
    test_set3 = test_set3['score']
    train_set = pd.concat([train_set1, train_set2, train_set3], axis=1)
    test_set = pd.concat([test_set1, test_set2, test_set3], axis=1)
    train_set.columns = [0, 1, 2, 3]
    test_set.columns = [0, 1, 2, 3]
    model_train(train_set, test_set, train_data)


if __name__ == '__main__':
    main()