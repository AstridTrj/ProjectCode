import os
import pandas as pd
import matplotlib.pyplot as plt
import datetime as dt


# 设置面板中可现实中文字符
plt.rcParams['font.sans-serif']=['SimHei']


# 运行内存百分比图
def ram_image(phone_data):
	# 需要显示的运存序列
	ram_count = {'2GB':0, '4GB':0, '6GB':0, '8GB':0, '10GB':0, '12GB':0, 'other': 0}

	all_count = len(phone_data)
	# 对含有多种运存的手机进行分开计数，分别计入上述的运存序列
	def rams_count(x):
	    for key in ram_count.keys():
	        if key in x:
	            ram_count[key] = ram_count[key] + 1
	# 调用函数进行处理
	phone_data['RAM容量'].map(lambda x: rams_count(x))
	# 其他类型的计数
	ram_count['other'] = all_count - ram_count['2GB'] - ram_count['4GB'] - ram_count['6GB'] - ram_count['8GB'] - ram_count['10GB'] - ram_count['12GB']
	# 计算占比
	for key in ram_count.keys():
	    ram_count[key] = ram_count[key] / all_count
	# 绘制饼图，explode为该扇区距离饼图中心的距离，autopct为数字显示的位数
	plt.pie(x=ram_count.values(), explode=[0, 0, 0, 0, 0.1, 0.3, 0], labels=ram_count.keys(), autopct='%1.1lf%%')
	# 绘制圆形饼图。而不是椭圆形
	plt.axis('equal')
	# 添加标题
	plt.title('手机运行内存占比')
	# 添加图例
	plt.legend()
	plt.show()


# 手机存储内存占比图，部分操作同运行内存占比图
def rom_image(phone_data):
	rom_count = {'32GB':0, '64GB':0, '128GB':0, '256GB':0, '512GB':0, 'other': 0}
	all_count = len(phone_data)

	def roms_count(x):
	    for key in rom_count.keys():
	        if key in x:
	            rom_count[key] = rom_count[key] + 1
	phone_data['ROM容量'].map(lambda x: roms_count(x))
	rom_count['other'] = all_count - rom_count['32GB'] - rom_count['64GB'] - rom_count['128GB'] - rom_count['256GB'] - rom_count['512GB']
	for key in rom_count.keys():
	    rom_count[key] = rom_count[key] / all_count
	    
	plt.pie(x=rom_count.values(), labels=rom_count.keys(), autopct='%1.2lf%%')
	plt.axis('equal')
	plt.title('手机存储内存占比')
	plt.legend()
	plt.show()


# 手机屏幕类型占比图，部分操作同运行内存占比图
def screen_type_image(phone_data):
	screen_type = {'非全面屏':0, '刘海屏':0, '水滴屏':0, '极点屏':0, '常规全面屏':0}
	all_count = len(phone_data)

	def screen_type_count(x):
	    for key in screen_type.keys():
	        if key in str(x):
	            screen_type[key] = screen_type[key] + 1
	phone_data['屏幕类型'].map(lambda x: screen_type_count(x))
	screen_type['非全面屏'] = all_count - screen_type['刘海屏'] - screen_type['水滴屏'] - screen_type['极点屏'] - screen_type['常规全面屏']
	for key in screen_type.keys():
	    screen_type[key] = screen_type[key] / all_count
	print(screen_type)
	plt.pie(x=screen_type.values(), explode=[0.1, 0.25, 0.35, 0.45, 0.55], labels=screen_type.keys(), autopct='%1.2lf%%')
	plt.axis('equal')
	plt.title('屏幕类型占比')
	plt.legend()
	plt.show()


# 手机解锁方式占比图，部分操作同运存内存占比图
def unlock_methods_image(phone_data):
	unlock_way = {'面部识别':0, '虹膜识别':0, '屏幕指纹':0, '前置指纹':0, '后置指纹':0, '其它解锁方式':0}
	all_count = len(phone_data)

	def unlock_way_count(x):
	    for key in unlock_way.keys():
	        if key in str(x):
	            unlock_way[key] = unlock_way[key] + 1
	phone_data['解锁方式'].map(lambda x: unlock_way_count(x))
	unlock_way['其它解锁方式'] = all_count - unlock_way['面部识别'] - unlock_way['虹膜识别'] - unlock_way['屏幕指纹'] - unlock_way['前置指纹'] - unlock_way['后置指纹']
	for key in unlock_way.keys():
	    unlock_way[key] = unlock_way[key] / all_count
	print(unlock_way)
	plt.pie(x=unlock_way.values(), explode=[0, 0, 0, 0, 0, 0], labels=unlock_way.keys(), autopct='%1.2lf%%')
	plt.axis('equal')
	plt.title('解锁方式占比')
	plt.legend(bbox_to_anchor=[1.2, 1.2])
	plt.show()


# 手机主屏分辨率占比图，部分操作同运行内存占比图
def screen_resolution_image(phone_data):
	screen_resolution_rate = {'4K':0, '2K':0, '1080P':0, '720P':0, '其它分辨率':0}
	all_count = len(phone_data)

	def screen_resolution_rate_count(x):
	    for key in screen_resolution_rate.keys():
	        if key in str(x):
	            screen_resolution_rate[key] = screen_resolution_rate[key] + 1
	phone_data['主屏分辨率'].map(lambda x: screen_resolution_rate_count(x))
	screen_resolution_rate['其它分辨率'] = all_count - screen_resolution_rate['4K'] - screen_resolution_rate['2K'] - screen_resolution_rate['1080P'] - screen_resolution_rate['720P']
	for key in screen_resolution_rate.keys():
	    screen_resolution_rate[key] = screen_resolution_rate[key] / all_count
	print(screen_resolution_rate, all_count)
	plt.pie(x=screen_resolution_rate.values(), labels=screen_resolution_rate.keys(), autopct='%1.2lf%%')
	plt.axis('equal')
	plt.title('主屏分辨率占比')
	# bbox_to_anchor设置图例的位置坐标
	plt.legend(bbox_to_anchor=[1.2, 1.2])
	plt.show()


# 价格分布直方图
def price_distribution_image(phone_data):
	# 对字符串进行转化，变为int类型
	phone_data['电商报价'] = phone_data['电商报价'].astype(int)
	# 取出不为-1（-1为爬取异常的数据）的数据行
	price_data = phone_data.loc[phone_data['电商报价'] != -1, '电商报价']
	# 价格区间序列
	price_count = {'<500':0, '500~1000':0, '1000~1500':0, '1500~2000':0, '2000~3000':0, '3000~4000':0, '4000~5000':0, '>5000':0}
	# 对价格进行计数
	for price in price_data:
	    if price < 500: price_count['<500'] = price_count['<500'] + 1
	    elif price < 1000: price_count['500~1000'] = price_count['500~1000'] + 1
	    elif price < 1500: price_count['1000~1500'] = price_count['1000~1500'] + 1
	    elif price < 2000: price_count['1500~2000'] = price_count['1500~2000'] + 1
	    elif price < 3000: price_count['2000~3000'] = price_count['2000~3000'] + 1
	    elif price < 4000: price_count['3000~4000'] = price_count['3000~4000'] + 1
	    elif price < 5000: price_count['4000~5000'] = price_count['4000~5000'] + 1
	    else: price_count['>5000'] = price_count['>5000'] + 1
	# 直方图绘制，alpha为颜色深度，color为颜色，可通过设置颜色值进行修改（6位）
	plt.bar(price_count.keys(), price_count.values(), alpha=0.7, color='#123456')
	plt.title('手机价格分布直方图', fontsize=18)
	# x轴标签，rotation即坐标的旋转，逆时针
	plt.xticks([i for i in price_count.keys()], rotation=330)
	# x轴标记，fontsize设置字体大小，labelpad设置距离轴的远近
	plt.xlabel('价格区间（单位:元）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机主屏尺寸分布直方图
def screen_size_distribution_image(phone_data):
	# 截取英之前的数字
	phone_data['主屏尺寸'] = phone_data['主屏尺寸'].map(lambda x: str(x)[:str(x).find('英')])
	# 对空值和‘-’的值进行处理
	phone_data['主屏尺寸'] = phone_data['主屏尺寸'].map(lambda x: -1 if x == '' or x == '-' else x)
	# 转化为float类型
	phone_data['主屏尺寸'] = phone_data['主屏尺寸'].astype(float)
	screen_size = phone_data.loc[phone_data['主屏尺寸'] != -1, '主屏尺寸']
	size_count = {'<=4.4':0, '4.5~4.9':0, '5.0':0, '5.1~5.4':0, '5.5':0, '5.6~6.0':0, '6.1~6.4':0, '>=6.5':0}
	for size in screen_size:
	    if size <= 4.4: size_count['<=4.4'] = size_count['<=4.4'] + 1
	    if size >= 4.5 and size <= 4.9: size_count['4.5~4.9'] = size_count['4.5~4.9'] + 1
	    if size == 5.0: size_count['5.0'] = size_count['5.0'] + 1
	    if size >= 5.1 and size <= 5.4: size_count['5.1~5.4'] = size_count['5.1~5.4'] + 1
	    if size == 5.5: size_count['5.5'] = size_count['5.5'] + 1
	    if size >= 5.6 and size <= 6.0: size_count['5.6~6.0'] = size_count['5.6~6.0'] + 1
	    if size >= 6.1 and size <= 6.4: size_count['6.1~6.4'] = size_count['6.1~6.4'] + 1
	    if size >= 6.5: size_count['>=6.5'] = size_count['>=6.5'] + 1
	print(size_count)
	plt.bar(size_count.keys(), size_count.values(), alpha=0.7, color='#555555')
	plt.title('手机屏幕尺寸分布直方图', fontsize=18)
	plt.xticks([i for i in size_count.keys()], rotation=330)
	plt.xlabel('尺寸区间（单位:英寸）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机屏占比分布直方图
def screen_occupation_image(phone_data):
	# 取出字符串中需要使用的数字（%以前）
	phone_data['屏幕占比'] = phone_data['屏幕占比'].map(lambda x: str(x)[:str(x).find('%')])
	phone_data['屏幕占比'] = phone_data['屏幕占比'].map(lambda x: -1 if x == '-' else x)
	phone_data['屏幕占比'] = phone_data['屏幕占比'].astype(float)
	occupation_data = phone_data.loc[phone_data['屏幕占比'] != -1, '屏幕占比']
	# 此处，由于未对区间要求，可通过绘制柱状图（自通过设置柱状的数目）进行区间的大概估计
	# plt.hist(occupation_data, bins=24)
	occupation_rate = {'<50%':0, '50%~70%':0, '70%~75%':0, '75%~80%':0, '80%~90%':0, '>=90%':0}
	for ocp in occupation_data:
	    if ocp < 50: occupation_rate['<50%'] = occupation_rate['<50%'] + 1
	    if ocp >= 50 and ocp < 70: occupation_rate['50%~70%'] = occupation_rate['50%~70%'] + 1
	    if ocp >= 70 and ocp < 75: occupation_rate['70%~75%'] = occupation_rate['70%~75%'] + 1
	    if ocp >= 75 and ocp < 80: occupation_rate['75%~80%'] = occupation_rate['75%~80%'] + 1
	    if ocp >= 80 and ocp < 90: occupation_rate['80%~90%'] = occupation_rate['80%~90%'] + 1
	    if ocp >= 90: occupation_rate['>=90%'] = occupation_rate['>=90%'] + 1
	print(occupation_rate)
	plt.bar(occupation_rate.keys(), occupation_rate.values(), alpha=0.7, color='#265412')
	plt.title('手机屏幕占比分布直方图', fontsize=18)
	plt.xticks([i for i in occupation_rate.keys()], rotation=330, fontsize=12)
	plt.xlabel('占比区间', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机重量分布直方图
def phone_weight_image(phone_data):
	# 截取数字
	phone_data['手机重量'] = phone_data['手机重量'].map(lambda x: str(x)[:str(x).find('g')])
	# 可根据需要添加处理操作，用-1替换异常数据
	def weight_deal(x):
	    if '-' in x: return -1
	    if '精钢拉丝外壳，时尚超大' in x: return -1
	    if '约' in x: return x.replace('约', '')
	    return x
	phone_data['手机重量'] = phone_data['手机重量'].map(lambda x: weight_deal(x))
	phone_data['手机重量'] = phone_data['手机重量'].astype(float)
	phone_weight = phone_data.loc[phone_data['手机重量'] != -1, '手机重量']
	weights = {'<110':0, '110~140':0, '140~150':0, '150~160':0, '160~180':0, '180~200':0, '>=200':0}
	for wegt in phone_weight:
	    if wegt < 110: weights['<110'] = weights['<110'] + 1
	    if wegt >= 110 and wegt < 140: weights['110~140'] = weights['110~140'] + 1
	    if wegt >= 140 and wegt < 150: weights['140~150'] = weights['140~150'] + 1
	    if wegt >= 150 and wegt < 160: weights['150~160'] = weights['150~160'] + 1
	    if wegt >= 160 and wegt < 180: weights['160~180'] = weights['160~180'] + 1
	    if wegt >= 180 and wegt < 200: weights['180~200'] = weights['180~200'] + 1
	    if wegt >= 200: weights['>=200'] = weights['>=200'] + 1
	print(weights)
	plt.bar(weights.keys(), weights.values(), alpha=0.7, color='#122544')
	plt.title('手机重量分布直方图', fontsize=18)
	plt.xticks([i for i in weights.keys()], rotation=330, fontsize=12)
	plt.xlabel('手机重量区间（单位：g）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机电池容量分布直方图
def phone_battery_image(phone_data):
	# 截取和异常值处理
	phone_data['电池容量'] = phone_data['电池容量'].map(lambda x: str(x)[:str(x).find('m')])
	phone_data['电池容量'] = phone_data['电池容量'].map(lambda x: -1 if x == '-' else x)
	phone_data['电池容量'] = phone_data['电池容量'].astype(int)
	battery_data = phone_data.loc[phone_data['电池容量'] != -1, '电池容量']
	battery_count = {'<1500':0, '1500~2000':0, '2000~2500':0, '2500~3000':0, '3000~3500':0, '3500~4000':0, '4000~4500':0, '4500~5000':0, '>=5000':0}
	for bary in battery_data:
	    if bary < 1500: battery_count['<1500'] = battery_count['<1500'] + 1
	    if bary >= 1500 and bary < 2000: battery_count['1500~2000'] = battery_count['1500~2000'] + 1
	    if bary >= 2000 and bary < 2500: battery_count['2000~2500'] = battery_count['2000~2500'] + 1
	    if bary >= 2500 and bary < 3000: battery_count['2500~3000'] = battery_count['2500~3000'] + 1
	    if bary >= 3000 and bary < 3500: battery_count['3000~3500'] = battery_count['3000~3500'] + 1
	    if bary >= 3500 and bary < 4000: battery_count['3500~4000'] = battery_count['3500~4000'] + 1
	    if bary >= 4000 and bary < 4500: battery_count['4000~4500'] = battery_count['4000~4500'] + 1
	    if bary >= 4500 and bary < 5000: battery_count['4500~5000'] = battery_count['4500~5000'] + 1
	    if bary >= 5000: battery_count['>=5000'] = battery_count['>=5000'] + 1
	print(battery_count)
	plt.bar(battery_count.keys(), battery_count.values(), alpha=0.7, color='#a12388')
	plt.title('手机电池容量分布直方图', fontsize=18)
	plt.xticks([i for i in battery_count.keys()], rotation=330, fontsize=12)
	plt.xlabel('手机电池容量区间（单位：mAh）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机前置摄像头像素分布直方图
def front_camera_image(phone_data):
	# 截取数字字段
	phone_data['前置摄像头'] = phone_data['前置摄像头'].map(lambda x: str(x)[:str(x).find('万')])
	# 对值为‘-’和不为数字字符的字符串进行处理
	def judge_isdigit(x):
	    if x == '-': return -1
	    if not str(x).isdigit(): return -1
	    return x
	phone_data['前置摄像头'] = phone_data['前置摄像头'].map(lambda x: judge_isdigit(x))
	phone_data['前置摄像头'] = phone_data['前置摄像头'].astype(int)
	front_camera = phone_data.loc[phone_data['前置摄像头'] != -1, '前置摄像头']
	front_count = {'<=400':0, '500':0, '800':0, '1300':0, '1600':0, '>=2000':0}
	for front in front_camera:
	    if front <= 400: front_count['<=400'] = front_count['<=400'] + 1
	    if front == 500: front_count['500'] = front_count['500'] + 1
	    if front == 800: front_count['800'] = front_count['800'] + 1
	    if front == 1300: front_count['1300'] = front_count['1300'] + 1
	    if front == 1600: front_count['1600'] = front_count['1600'] + 1
	    if front >= 2000: front_count['>=2000'] = front_count['>=2000'] + 1
	print(front_count)
	plt.bar(front_count.keys(), front_count.values(), alpha=0.7, color='#b02c36')
	plt.title('手机前置摄像头像素分布直方图', fontsize=18)
	plt.xticks([i for i in front_count.keys()], rotation=330, fontsize=12)
	plt.xlabel('手机像素区间（万）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机后置摄像头像素分布直方图
def rear_camera_image(phone_data):
	# 对爬取的数据进行基本处理，截取数字部分
	def rear_deal(x):
	    if x == '-' or x == -1:
	        return -1
	    if '双' in x:
	        return  str(x)[str(x).find('双') + 1:str(x).find('万')]
	    return str(x)[:str(x).find('万')]
	phone_data['后置摄像头'] = phone_data['后置摄像头'].map(lambda x: rear_deal(x))
	# 对不为数字的数据进行处理
	phone_data['后置摄像头'] = phone_data['后置摄像头'].map(lambda x: -1 if not str(x).isdigit() else x)
	phone_data['后置摄像头'] = phone_data['后置摄像头'].astype(int)
	rear_camera = phone_data.loc[phone_data['后置摄像头'] != -1, '后置摄像头']
	rear_count = {'<=700':0, '800':0, '1200':0, '1300':0, '1600':0, '>=2000':0}
	for rear in rear_camera:
	    if rear <= 700: rear_count['<=700'] = rear_count['<=700'] + 1
	    if rear == 800: rear_count['800'] = rear_count['800'] + 1
	    if rear == 1200: rear_count['1200'] = rear_count['1200'] + 1
	    if rear == 1300: rear_count['1300'] = rear_count['1300'] + 1
	    if rear == 1600: rear_count['1600'] = rear_count['1600'] + 1
	    if rear >= 2000: rear_count['>=2000'] = rear_count['>=2000'] + 1
	print(rear_count)
	plt.bar(rear_count.keys(), rear_count.values(), alpha=0.7, color='#d32cd6')
	plt.title('手机后置摄像头像素分布直方图', fontsize=18)
	plt.xticks([i for i in rear_count.keys()], rotation=330, fontsize=12)
	plt.xlabel('手机像素区间（万）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机屏幕像素密度分布直方图
def screen_ppi_image(phone_data):
	# 截取数字并转换为int类型
	phone_data['屏幕像素密度'] = phone_data['屏幕像素密度'].map(lambda x: str(x)[:str(x).find('p')])
	phone_data['屏幕像素密度'] = phone_data['屏幕像素密度'].map(lambda x: -1 if x == '-' else x)
	phone_data['屏幕像素密度'] = phone_data['屏幕像素密度'].astype(int)
	ppi_data = phone_data.loc[phone_data['屏幕像素密度'] != -1, '屏幕像素密度']
	ppi_count = {'<300':0, '300-349':0, '350-399':0, '400-500':0, '>=500':0}
	for ppi in ppi_data:
	    if ppi < 300: ppi_count['<300'] = ppi_count['<300'] + 1
	    if ppi >= 300 and ppi < 350: ppi_count['300-349'] = ppi_count['300-349'] + 1
	    if ppi >= 350 and ppi < 400: ppi_count['350-399'] = ppi_count['350-399'] + 1
	    if ppi >= 400 and ppi < 500: ppi_count['400-500'] = ppi_count['400-500'] + 1
	    if ppi >= 500: ppi_count['>=500'] = ppi_count['>=500'] + 1
	print(ppi_count)
	plt.bar(ppi_count.keys(), ppi_count.values(), alpha=0.7, color='#762c09')
	plt.title('手机屏幕像素密度分布直方图', fontsize=18)
	plt.xticks([i for i in ppi_count.keys()], rotation=330, fontsize=12)
	plt.xlabel('手机屏幕像素密度区间（单位:ppi）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 手机窄边框分布直方图
def phone_frame_image(phone_data):
	phone_data['窄边框'] = phone_data['窄边框'].map(lambda x: str(x)[:str(x).find('m')])
	phone_data['窄边框'] = phone_data['窄边框'].map(lambda x: -1 if x == '-' else x)
	phone_data['窄边框'] = phone_data['窄边框'].astype(float)
	frame_data = phone_data.loc[phone_data['窄边框'] != -1, '窄边框']
	frame_count = {'2-3':0, '3-4':0, '4-5':0, '5-6':0, '>=6':0}
	for frame in frame_data:
	    if frame >= 2 and frame < 3: frame_count['2-3'] = frame_count['2-3'] + 1
	    if frame >= 3 and frame < 4: frame_count['3-4'] = frame_count['3-4'] + 1
	    if frame >= 4 and frame < 5: frame_count['4-5'] = frame_count['4-5'] + 1
	    if frame >= 5 and frame < 6: frame_count['5-6'] = frame_count['5-6'] + 1
	    if frame >= 6: frame_count['>=6'] = frame_count['>=6'] + 1
	print(frame_count)
	plt.bar(frame_count.keys(), frame_count.values(), alpha=0.7, color='#513416')
	plt.title('手机窄边框分布直方图', fontsize=18)
	plt.xticks([i for i in frame_count.keys()], rotation=330, fontsize=12)
	plt.xlabel('手机窄边框区间（单位:mm）', fontsize=15, labelpad=10)
	plt.ylabel('频数', fontsize=15, labelpad=10)
	plt.show()


# 上市日期处理，以方便做以时间为自变量的折线图
def day_time_deal(phone_data):
	# 此处因为个部分的手机数较小，只取月部分
	phone_data['上市日期'] = phone_data['上市日期'].map(lambda x: str(x)[:str(x).find('月') + 1])
	# 对月份为个位数时，在其前面添加一个0
	def deal_month(x):
	    if x == '': return -1
	    a = str(x)[5:str(x).find('月')]
	    if len(a) == 1:
	        x = list(x)
	        x.insert(5, '0')
	        x = ''.join(x)
	    return x
	phone_data['上市日期'] = phone_data['上市日期'].map(lambda x: deal_month(x))
	# 用‘-’替换年和月
	phone_data['上市日期'] = phone_data['上市日期'].map(lambda x: str(x).replace('月', '').replace('年', '-'))
	# 分类标记，以半年为单位，对时间进行分类，对半年中的时间进行同样的标记，得到每一年类的手机信息。时间进行比较时，需要转化为时间变量
	def classify(x):
	    if x == '-1': return -1
	    x = dt.datetime.strptime(x, '%Y-%m')
	    if x >= dt.datetime.strptime('2016-01', '%Y-%m') and x <= dt.datetime.strptime('2016-06', '%Y-%m'):
	        return 1
	    if x >= dt.datetime.strptime('2016-07', '%Y-%m') and x <= dt.datetime.strptime('2016-12', '%Y-%m'):
	        return 2
	    if x >= dt.datetime.strptime('2017-01', '%Y-%m') and x <= dt.datetime.strptime('2017-06', '%Y-%m'):
	        return 3
	    if x >= dt.datetime.strptime('2017-07', '%Y-%m') and x <= dt.datetime.strptime('2017-12', '%Y-%m'):
	        return 4
	    if x >= dt.datetime.strptime('2018-01', '%Y-%m') and x <= dt.datetime.strptime('2018-06', '%Y-%m'):
	        return 5
	    if x >= dt.datetime.strptime('2018-07', '%Y-%m') and x <= dt.datetime.strptime('2018-12', '%Y-%m'):
	        return 6
	    if x >= dt.datetime.strptime('2019-01', '%Y-%m') and x <= dt.datetime.strptime('2019-06', '%Y-%m'):
	        return 7
	# 填充空值
	phone_data['上市日期'] = phone_data['上市日期'].map(lambda x: classify(x)).fillna(-1).astype(int)
	return phone_data


# 一段时间内手机运行内存占比折线图
def ram_broken_line_image(phone_data):
	# 使用时间对运存进行分组
	group_ram = phone_data.groupby(phone_data['上市日期'])['RAM容量']
	# 通过unstack（）得到每半年各种运存类型的手机数量
	group_ram_data = group_ram.value_counts().unstack()
	# 将空值填充为0
	group_ram_data = group_ram_data.fillna(0)
	# 对有多种类型的手机数，将其添加到对应的类型
	for i in group_ram_data.columns:
	    if '2GB' in i: group_ram_data['2GB'] = group_ram_data['2GB'] + group_ram_data[i]
	    if '4GB' in i: group_ram_data['4GB'] = group_ram_data['4GB'] + group_ram_data[i]
	    if '6GB' in i: group_ram_data['6GB'] = group_ram_data['6GB'] + group_ram_data[i]
	    if '8GB' in i: group_ram_data['8GB'] = group_ram_data['8GB'] + group_ram_data[i]
	    if '10GB' in i: group_ram_data['10GB'] = group_ram_data['10GB'] + group_ram_data[i]
	    if '12GB' in i: group_ram_data['12GB'] = group_ram_data['12GB'] + group_ram_data[i]
	# 求出总数，以求占比
	group_ram_data['all'] = group_ram_data.sum(axis=1)
	# 各类型手机占比计算
	for gb in ['2GB', '4GB', '6GB', '6GB', '8GB', '10GB', '12GB']:
	    group_ram_data[gb] = group_ram_data[gb] / group_ram_data['all']
	gb_rate_data = group_ram_data[['2GB', '4GB', '6GB', '8GB', '10GB', '12GB']]
	gb_rate_data = gb_rate_data.drop([-1])

	xticks = ['2016年1月-6月', '2016年7月-12月', '2017年1月-6月', '2017年7月-12月',
	         '2018年1月-6月', '2018年7月-12月', '2019年1月-6月']
	gb_rate_data.index = xticks
	# 折线图绘制
	plt.plot(gb_rate_data, marker='o')
	plt.title('2016-2019各半年不同运行内存占比走势')
	plt.legend(gb_rate_data.columns, bbox_to_anchor=[1, 1])
	plt.xticks(rotation=30)
	# 不显示网格
	plt.grid(b=False)
	plt.show()


# 一段时间内手机存储内存占比折线图
def rom_broken_line_image(phone_data):
	# 此图操作同运存
	group_rom = phone_data.groupby(phone_data['上市日期'])['ROM容量']
	group_rom_data = group_rom.value_counts().unstack()
	group_rom_data = group_rom_data.fillna(0)

	for i in group_rom_data.columns:
	    if '32GB' in i: group_rom_data['32GB'] = group_rom_data['32GB'] + group_rom_data[i]
	    if '64GB' in i: group_rom_data['64GB'] = group_rom_data['64GB'] + group_rom_data[i]
	    if '128GB' in i: group_rom_data['128GB'] = group_rom_data['128GB'] + group_rom_data[i]
	    if '256GB' in i: group_rom_data['256GB'] = group_rom_data['256GB'] + group_rom_data[i]
	    if '512GB' in i: group_rom_data['512GB'] = group_rom_data['512GB'] + group_rom_data[i]
	group_rom_data['all'] = group_rom_data.sum(axis=1)
	for gb in ['32GB', '64GB', '128GB', '256GB', '512GB']:
	    group_rom_data[gb] = group_rom_data[gb] / group_rom_data['all']
	rom_rate_data = group_rom_data[['32GB', '64GB', '128GB', '256GB', '512GB']]
	rom_rate_data = rom_rate_data.drop([-1])

	xticks = ['2016年1月-6月', '2016年7月-12月', '2017年1月-6月', '2017年7月-12月',
	         '2018年1月-6月', '2018年7月-12月', '2019年1月-6月']
	rom_rate_data.index = xticks
	plt.plot(rom_rate_data, marker='o')
	plt.title('2016-2019各半年不同存储内存占比走势')
	plt.legend(rom_rate_data.columns, bbox_to_anchor=[1, 1])
	plt.xticks(rotation=30)
	plt.grid(b=False)
	plt.show()


# 手机屏幕类型占比随时间变化折线图
def screen_type_broken_line_image(phone_data):
	# 操作同运存图
	group_screen = phone_data.groupby(phone_data['上市日期'])['屏幕类型']
	group_screen = group_screen.value_counts().unstack()
	group_screen = group_screen.fillna(0)
	for i in ['非全面屏', '刘海屏', '水滴屏', '极点屏', '常规全面屏']:
	    group_screen[i] = 0
	for i in group_screen.columns:
	    i = str(i)
	    if '刘海屏' in i: group_screen['刘海屏'] = group_screen['刘海屏'] + group_screen[i]
	    if '水滴屏' in i: group_screen['水滴屏'] = group_screen['水滴屏'] + group_screen[i]
	    if '极点屏' in i: group_screen['极点屏'] = group_screen['极点屏'] + group_screen[i]
	    if '常规全面屏' in i: group_screen['常规全面屏'] = group_screen['常规全面屏'] + group_screen[i]

	group_screen['all'] = group_screen.sum(axis=1)
	group_screen['非全面屏'] = group_screen['all'] - group_screen['刘海屏'] - group_screen['水滴屏'] - group_screen['极点屏'] - group_screen['常规全面屏']
	for i in ['非全面屏', '刘海屏', '水滴屏', '极点屏', '常规全面屏']:
	    group_screen[i] = group_screen[i] / group_screen['all']
	screen_rate_data = group_screen[['非全面屏', '刘海屏', '水滴屏', '极点屏', '常规全面屏']]
	screen_rate_data = screen_rate_data.drop([-1])

	xticks = ['2016年1月-6月', '2016年7月-12月', '2017年1月-6月', '2017年7月-12月',
	         '2018年1月-6月', '2018年7月-12月', '2019年1月-6月']
	screen_rate_data.index = xticks
	plt.plot(screen_rate_data, marker='o')
	plt.title('2016-2019各半年不同屏幕类型占比走势')
	plt.legend(screen_rate_data.columns, bbox_to_anchor=[1, 1])
	plt.xticks(rotation=30)
	plt.grid(b=False)
	plt.show()


# 手机解锁方式占比随时间变化折线图，操作同运存图
def unlock_type_broken_line(phone_data):
	group_unlock = phone_data.groupby(phone_data['上市日期'])['解锁方式']
	group_unlock = group_unlock.value_counts().unstack()
	group_unlock = group_unlock.fillna(0)
	for i in ['面部识别', '虹膜识别', '屏幕指纹', '前置指纹', '后置指纹']:
	    group_unlock[i] = 0
	for i in group_unlock.columns:
	    i = str(i)
	    if '面部识别' in i: group_unlock['面部识别'] = group_unlock['面部识别'] + group_unlock[i]
	    if '虹膜识别' in i: group_unlock['虹膜识别'] = group_unlock['虹膜识别'] + group_unlock[i]
	    if '屏幕指纹' in i: group_unlock['屏幕指纹'] = group_unlock['屏幕指纹'] + group_unlock[i]
	    if '前置指纹' in i: group_unlock['前置指纹'] = group_unlock['前置指纹'] + group_unlock[i]
	    if '后置指纹' in i: group_unlock['后置指纹'] = group_unlock['后置指纹'] + group_unlock[i]

	group_unlock['all'] = group_unlock.sum(axis=1)
	for i in ['面部识别', '虹膜识别', '屏幕指纹', '前置指纹', '后置指纹']:
	    group_unlock[i] = group_unlock[i] / group_unlock['all']
	unlock_rate_data = group_unlock[['面部识别', '虹膜识别', '屏幕指纹', '前置指纹', '后置指纹']]
	unlock_rate_data = unlock_rate_data.drop([-1])

	xticks = ['2016年1月-6月', '2016年7月-12月', '2017年1月-6月', '2017年7月-12月',
	         '2018年1月-6月', '2018年7月-12月', '2019年1月-6月']
	unlock_rate_data.index = xticks
	plt.plot(unlock_rate_data, marker='o')
	plt.title('2016-2019各半年不同解锁方式占比走势')
	plt.legend(unlock_rate_data.columns, bbox_to_anchor=[1, 1])
	plt.xticks(rotation=30)
	plt.grid(b=False)
	plt.show()


# 各函数的调用
def main(phone_data):
	ram_image(phone_data)
	rom_image(phone_data)
	screen_type_image(phone_data)
	unlock_methods_image(phone_data)
	screen_resolution_image(phone_data)
	price_distribution_image(phone_data)
	screen_size_distribution_image(phone_data)
	screen_occupation_image(phone_data)
	phone_weight_image(phone_data)
	phone_battery_image(phone_data)
	front_camera_image(phone_data)
	rear_camera_image(phone_data)
	screen_ppi_image(phone_data)
	phone_frame_image(phone_data)
	phone_data = day_time_deal(phone_data)
	ram_broken_line_image(phone_data)
	rom_broken_line_image(phone_data)
	screen_type_broken_line_image(phone_data)
	unlock_type_broken_line(phone_data)


if __name__ == '__main__':
	# 设置数据文件路径
	path = 'E:\\what-study\\project\\all_data.csv'
	phone_data = pd.read_csv(path)
	main(phone_data)