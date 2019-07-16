import numpy as np
import sys
sys.setrecursionlimit(1300)


if_win = np.zeros((102, 13, 32))
if_win = np.full(if_win.shape, -1)
days = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]


def is_leap_year(y):
    if (y + 1900) % 400 == 0 or ((y + 1900) % 100 != 0 and (y + 1900) % 4 == 0):
        return True
    return False


def dfs(year, month, day):
    # 当日期超过最大日期，直接结束
    if (year > 101) or (year == 101 and (month > 11 or (month == 11 and day > 4))):
        return 1
    # 刚好是最后日期，则可胜利
    if year == 101 and month == 11 and day == 4:
        return 0

    win = 0
    if if_win[year, month, day] == -1:
        # 优先对月份进行搜索
        if month != 12:
            # 下个月有同一天（特殊情况是今年为闰年且当前为1月29，那么2月份29号可用）
            if day <= days[month + 1] or (day == 29 and month == 1 and is_leap_year(year)):
                # 如果搜索到最后日期或者变过头，则本次胜利
                if dfs(year, month + 1, day) == 0:
                    win = 1
        # 如果当前为12月，则搜索下一年
        else:
            if dfs(year + 1, 1, day) == 0:
                win = 1

        # 当搜索月份不能获胜时，搜索天数
        if win == 0:
            if day < days[month]:
                win = 1 - dfs(year, month, day + 1)
            elif month != 12:
                win = 1 - dfs(year, month + 1, 1)
            else:
                win = 1 - dfs(year + 1, 1, 1)
        if_win[year, month, day] = win
    return if_win[year, month, day]


def main(year, month, day):
    if year > 2001 or (year == 2001 and (month > 11 or (month == 11 and day > 4))):
        return 'NO'
    else:
        if dfs(year - 1900, month, day) == 1:
            return 'YES'
        else:
            return 'NO'


if __name__ == '__main__':
    year = int(input("请输入年份："))
    month = int(input("请输入月份："))
    day = int(input("请输入日期："))
    result = main(year, month, day)
    print(result)