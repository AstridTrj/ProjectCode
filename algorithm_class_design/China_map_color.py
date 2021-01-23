# coding=utf-8
import numpy as np
from pyecharts.charts import Map
import pyecharts.options as opts

# 中国地图各省市的邻接关系
link = {
    '新疆': ['西藏', '青海', '甘肃'],
    '西藏': ['新疆', '青海', '四川', '云南'],
    '青海': ['新疆', '西藏', '甘肃', '四川'],
    '甘肃': ['新疆', '青海', '内蒙古', '宁夏', '四川', '陕西'],
    '内蒙古': ['甘肃', '宁夏', '陕西', '山西', '河北', '黑龙江', '吉林', '辽宁'],
    '宁夏': ['甘肃', '内蒙古', '陕西'],
    '四川': ['西藏', '青海', '甘肃', '云南', '陕西', '重庆', '贵州'],
    '云南': ['西藏', '四川', '贵州', '广西'],
    '陕西': ['甘肃', '内蒙古', '宁夏', '四川', '重庆', '山西', '河南', '湖北'],
    '重庆': ['四川', '陕西', '贵州', '湖北', '湖南'],
    '贵州': ['四川', '云南', '重庆', '广西', '湖南'],
    '广西': ['云南', '贵州', '湖南', '广东'],
    '山西': ['内蒙古', '陕西', '河南', '河北'],
    '河南': ['陕西', '山西', '湖北', '河北', '山东', '安徽'],
    '湖北': ['陕西', '重庆', '河南', '湖南', '安徽', '江西'],
    '湖南': ['重庆', '贵州', '广西', '湖北', '广东', '江西'],
    '广东': ['广西', '湖南', '澳门', '香港', '江西', '福建'],
    '海南': [],
    '澳门': ['广东'],
    '香港': ['广东'],
    '河北': ['内蒙古', '山西', '河南', '北京', '天津', '山东', '辽宁'],
    '北京': ['河北', '天津'],
    '天津': ['河北', '北京'],
    '山东': ['河南', '河北', '安徽', '江苏'],
    '安徽': ['河南', '湖北', '山东', '江西', '江苏', '浙江'],
    '江西': ['湖北', '湖南', '广东', '安徽', '浙江', '福建'],
    '江苏': ['山东', '安徽', '上海', '浙江'],
    '上海': ['江苏', '浙江'],
    '浙江': ['广东', '安徽', '江西', '江苏', '上海'],
    '福建': ['广东', '江西', '浙江'],
    '台湾': [],
    '黑龙江': ['内蒙古', '吉林'],
    '吉林': ['内蒙古', '黑龙江', '辽宁'],
    '辽宁': ['内蒙古', '河北', '吉林']
}

citys = ['新疆', '西藏', '青海', '甘肃', '内蒙古', '宁夏', '四川', '云南', '陕西',
        '重庆', '贵州', '广西', '山西', '河南', '湖北', '湖南', '广东', '海南', '澳门',
        '香港', '河北', '北京', '天津', '山东', '安徽', '江西', '江苏', '上海', '浙江',
        '福建', '台湾', '黑龙江', '吉林', '辽宁']
# 各省的度数，与citys中的城市顺序对应
degree = [3, 4, 4, 6, 8, 3, 7, 4, 8, 5, 5, 4, 4, 6, 6, 6, 6, 0, 1, 1, 7, 2, 2, 4, 6, 6, 4, 2, 5, 3, 0, 2, 3, 3]
n = 34
# 每个省市着色值，初始为0及不着色
colors = [0] * n
# 各省市邻接矩阵
map_graph = []

# 根据度数降序排序
def degree_sort():
    global link, citys, degree, n
    for i in range(n):
        for j in range(n-i-1):
            if degree[j] < degree[j+1]:
                degree[j], degree[j+1] = degree[j+1], degree[j]
                citys[j], citys[j+1] = citys[j+1], citys[j]

# 利用邻接关系计算各省市邻接矩阵
def get_map():
    global map_graph, link, n, citys
    map_graph = np.zeros((n, n))
    for i in range(34):
        for j in link[citys[i]]:
            map_graph[i, citys.index(j)] = 1

# 判断index使用color是否会冲突
def is_conflict(index, color):
    global map_graph, colors
    # index对应的省市使用color是否会与index之前与index邻接的省市发生冲突
    for k in range(index):
        if map_graph[index, k] and colors[k] == color:
            return True
    return False

# 对地图着色
def map_color():
    global n, map_graph, colors
    # 颜色数，当不能满足着色条件时则加1
    color = 1
    # 对于n个省市依次着色
    for i in range(n):
        # 着色值从1开始到color
        for j in range(1, color+1):
            # 如果着色j值与i邻接的省市不冲突则省市i着色为j
            if not is_conflict(i, j):
                colors[i] = j
                break
        # 当遍历了所有颜色都不满足时，则增加颜色数
        if colors[i] == 0:
            color = color + 1
            colors[i] = color
    # 着色信息输出
    print('最少需要的颜色数为: ', color)
    print('每个省市对应着色号为：', colors)
    print('对应省市为：', end='')
    print(citys)

# 地图绘制
def map_test():
    global citys, colors
    # 每个省市对应的颜色值（需增大各颜色差值，否则无法区分）
    areas_values = []
    color_list = [1, 80, 160, 240]
    for i in range(34):
        areas_values.append((citys[i], color_list[colors[i] - 1])) # colors[i] * 50 + 30
    # 初始化地图大小
    test_map = Map(init_opts=opts.InitOpts(width='1200px', height='600px'))
    # 添加参数china, 绘制为中国地图
    test_map.add('', areas_values, 'china').set_global_opts(
            title_opts=opts.TitleOpts(title="中国地图着色"),
            visualmap_opts=opts.VisualMapOpts(max_=250),
        )
    # 生成html文件
    test_map.render('map_color.html')


def main():
    degree_sort()
    get_map()
    map_color()
    map_test()


if __name__ == '__main__':
    main()

