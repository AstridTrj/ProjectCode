import pandas as pd
import numpy as np
import lightgbm as lgb
from sklearn import metrics
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import StratifiedKFold
from sklearn.feature_extraction.text import TfidfVectorizer


def lgb_model(train_data, test_data):
    null_index = train_data[train_data['label'].isnull()].index
    train_data.drop(null_index, axis=0, inplace=True)
    train_data.reset_index(drop=True, inplace=True)
    train_data['label'] = train_data['label'].map({'Positive': 1, 'Negative': 0})
    label = train_data['label']
    id = test_data['ID']

    data_all = pd.concat([train_data, test_data], ignore_index=True, sort=False)
    len_train = len(train_data)

    print('TF-IDF计算......')
    tfidf = TfidfVectorizer(max_df=0.8, min_df=2, token_pattern=u'(?u)\\b[^\\d\\W]\\w+\\b')
    data_matrix = tfidf.fit_transform(data_all['review']).toarray()
    data = pd.DataFrame(data_matrix, columns=tfidf.get_feature_names())
    print(data.shape)

    train_data = data[:len_train]
    test_data = data[len_train:]

    folds = StratifiedKFold(n_splits=5, shuffle=True, random_state=123)
    lgb_pre = np.zeros(len(test_data))

    for i, (train_index, test_index) in enumerate(folds.split(train_data.values, label.values)):
        print('Fold ', i+1, ' training......')
        lr_model = LogisticRegression(C=5)
        lr_model.fit(train_data.iloc[train_index], label.iloc[train_index])
        y_pre = lr_model.predict_proba(train_data.iloc[test_index])[:, 1]
        print(y_pre)
        auc = metrics.roc_auc_score(label.iloc[test_index].tolist(), y_pre)
        print(auc)
        lgb_pre += lr_model.predict_proba(test_data)[:, 1]

    lgb_pre = pd.DataFrame(lgb_pre / 5, columns=['Pred'])
    results = pd.concat([id, lgb_pre], axis=1)
    print(results)
    results.to_csv('result.csv', index=False)


def main():
    train_data = pd.read_csv('train.csv')
    test_data = pd.read_csv('20190506_test.csv')
    lgb_model(train_data, test_data)
    # print(test_data)


if __name__ == '__main__':
    main()