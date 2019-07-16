import re
import requests
import time
import json
from bs4 import BeautifulSoup


# URL的headers和cookies
headers = {'User-Agent': 'Spider'}
cookies = {'cookies': 'SINAGLOBAL=9220449677509.182.1525354570111; _'
                      'ga=GA1.2.814940997.1538553604; __gads=ID=f8867745e11c'
                      '4cc7:T=1538553611:S=ALNI_MYK4b0jE7E9vTCU1V0b0gw66ut'
                      'FrQ; SCF=AlfRp0hyl0TJfEqORsC_M0AcHW2puJd3qBw-NXYpC'
                      'LmAxDtDXPz3N4-LBTDIoSy_tUyO3y4RkJqoBRpbBIP2ouk.; SU'
                      'HB=05i6vkgddgWe_7; un=15283106514; SUB=_2AkMs6p-DdcP'
                      'xrABZkP0VyWziaotH-jyfP_Z1An7uJhMyAxh77lYeqSVutBF-XH_'
                      'WemsHdz7e7_y4f6-E0oUs-fJv; SUBP=0033WrSXqPxfM72wW'
                      's9jqgMF55529P9D9W5-ojFsxPl5W1Ub9.TC2FQ35JpVF02feon0e'
                      'K5cSK2X; UOR=www.huiyi8.com,widget.weibo.com,www.baidu.c'
                      'om; login_sid_t=406857bb2600eb3f7ac04af70c1345ab; cross_o'
                      'rigin_proto=SSL; YF-Ugrow-G0=ea90f703b7694b74b62d38420b'
                      '5273df; YF-V5-G0=73b58b9e32dedf309da5103c77c3af4f; _s_'
                      'tentry=www.baidu.com; wb_view_log=1366*7681; Apache=2147'
                      '093233075.8984.1538805122782; ULV=1538805122813:10:8:8:'
                      '2147093233075.8984.1538805122782:1538736939861; YF-Page'
                      '-G0=9a31b867b34a0b4839fa27a4ab6ec79f; WBStorage=e8781eb7dee3fd7f|undefined'}


# 根据URL抓取到其中的内容，并通过json解析
def get_url_text(url1):
    response = requests.get(url=url1, headers=headers, cookies=cookies).text
    response = json.loads(response)
    return response


# 获取微博用户名和微博时间
def get_id_time():
    # 页数计数
    page = 1
    # 爬取微博数量
    count = 0
    while count <= 300:
        time.sleep(0.5)
        # 根据页数合成URL
        url = 'https://weibo.com/a/aj/transform/loadingmoreunlogin?aj' \
              'wvr=6&category=0&page=' + str(page) + '&lefnav=0&cursor=&__rnd=1538821026224'
        response = get_url_text(url)
        # 用BeautifulSoup及lxml解析库解析
        soup = BeautifulSoup(response['data'], 'lxml')
        # 根据class属性找到每页微博
        find = soup.find_all(attrs={'class': ['UG_list_b', 'UG_list_a', 'UG_list_v2']})
        # 循环对每页每条微博进行评论获取
        for i in find:
            # 判断count，如果大于300则退出抓取
            if count > 300:
                break
            # 对单条微博解析
            soup1 = BeautifulSoup(str(i), 'lxml')
            # 根据class属性找到用户名和时间，通过CSS选择器查找， 在写入文件时通过get_text()获取内容
            find_id = soup1.select('.subinfo')
            # 根据class属性找到微博内容
            find_content = soup1.select('.list_title_s')[0].get_text()
            # 逐个写入文件
            with open('spider{}.txt'.format(count + 1), 'a+', encoding='utf-8') as f:
                f.write(find_id[0].get_text() + '  ' + find_id[1].get_text() + '\n' + find_content + '\n')
            # 获取该微博的id，用于拼接评论URL
            mid_c = re.compile('mid="(.*?)"', re.S)
            mid = re.findall(mid_c, str(i))
            # 获取评论
            get_comment(mid[0], count + 1)
            count += 1
            print(count)
        page += 1


def get_comment(mid, num):
    url_p = 'https://weibo.com/aj/v6/comment/big?ajwvr=6&id='
    url_n = '&from=singleWeiBo&page='
    rnd = '&__rnd=1538827894696'

    with open('spider{}.txt'.format(num), 'a+', encoding='utf-8') as f:
        for i in range(1, 11):
            time.sleep(0.5)
            # 根据每个用户的id拼接评论的URL
            url_a = url_p + mid + url_n + str(i) + rnd
            # 抓取评论页面内容
            comment_text = get_url_text(url_a)
            soup = BeautifulSoup(str(comment_text), 'lxml')
            # 获取所有评论
            comment_pattern = soup.select('.list_li')

            for each in range(len(comment_pattern)):
                # 替换掉每条评论的\n和空格
                comment = comment_pattern[each].get_text().replace(r'\n', '').replace(' ', '')
                # 删除多余的信息
                comment = re.findall('(.*?)举', comment)[0]
                # 逐条写入文件
                f.write(comment + '\n')


if __name__ == '__main__':
    get_id_time()
