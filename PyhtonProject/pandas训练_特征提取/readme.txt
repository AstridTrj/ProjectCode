使用该链接查看数据说明。https://www.kesci.com/home/competition/5ab8c36a8643e33f5138cba4/content/3

从data里的4个文件中提取出以下特征：

1.行为的总次数，每天行为数的平均次数，一天中最多的行为数，
2.每天行为次数的方差、标准差，
3.最大连续有行为的天数，有行为的总天数，
4.最后一次行为的日期，行为数最多的一天的日期，

注：

2）行为表还需提：
	action_type为0 1 2的行为次数，action_type为0且page为1的行为次数，
	action_type的种类个数（比如：某个用户action_type有0 1 2，则此处值为3）
	28 29 30号action_type为0的总数，


最后所得结果存为一个csv文件，格式如sample.csv所示，其中每一行表示一个用户，每一列表示以上所提的某个特征。