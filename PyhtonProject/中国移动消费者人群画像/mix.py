import numpy as np
import pandas as pd


if __name__ == '__main__':
    a = 640.45 / (640.45 + 639.38)
    b = 639.38 / (640.45 + 639.38)
    lgb = pd.read_csv('mixture640_399_64045.csv')
    xgb = pd.read_csv('concat_baseline_03-22-23-44.csv')
    lgb['score'] = 0.6 * lgb['score'] + 0.4 * xgb['score']
    lgb['score'] = lgb['score'].map(lambda x: int(np.round(x)))
    lgb.to_csv('mixture64045_xie_46.csv', encoding='utf-8', header=['id', 'score'], index=False)

