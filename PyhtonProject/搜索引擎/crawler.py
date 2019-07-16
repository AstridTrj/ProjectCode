import my_queue
import random
import time
import requests
import chardet
import jieba
import re
import math
import pandas as pd
from lxml import etree
from bs4 import BeautifulSoup
from collections import Counter

header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit'
                        '/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.3'
                        '6 Core/1.63.6776.400 QQBrowser/10.3.2601.400'}
cookie1 = {'cookies': 'BIDUPSID=8C2E49CB424320C70B84A6F00AEB3864; PSTM=151'
                      '6694923; BAIDUID=78F888F3482A3EA109103047DD9855A1:FG=1; BK_'
                      'SEARCHLOG=%7B%22key%22%3A%5B%22cosplay%22%2C%22coldplay%'
                      '22%5D%7D; bdshare_firstime=1540728930298; BDORZ=FFFB88E99905'
                      '5A3F8A630C64834BD6D0; Hm_lvt_55b574651fcae74b0a9f1cf9c8d7c93a'
                      '=1540796888,1540991118,1540991308,1542462024; pgv_pvi=752898867'
                      '2; pgv_si=s648710144; Hm_lpvt_55b574651fcae74b0a9f1cf9c8d7c93a=1'
                      '542462271; BDRCVFR[S_ukKV6dOkf]=mk3SLVN4HKm; delPer=0; PSINO='
                      '7; H_PS_PSSID=1462_25810_21084_27244_27508'}
cookie2 = {'cookies': 'BIDUPSID=8C2E49CB424320C70B84A6F00AEB3864; PSTM=1516'
                      '694923; BAIDUID=78F888F3482A3EA109103047DD9855A1:FG=1; BK_SE'
                      'ARCHLOG=%7B%22key%22%3A%5B%22cosplay%22%2C%22coldplay%22%'
                      '5D%7D; bdshare_firstime=1540728930298; BDORZ=FFFB88E999055A3F8'
                      'A630C64834BD6D0; Hm_lvt_55b574651fcae74b0a9f1cf9c8d7c93a=15407'
                      '96888,1540991118,1540991308,1542462024; pgv_pvi=7528988672; pgv_s'
                      'i=s648710144; Hm_lpvt_55b574651fcae74b0a9f1cf9c8d7c93a=154246227'
                      '1; BDRCVFR[S_ukKV6dOkf]=mk3SLVN4HKm; delPer=0; PSINO=7; H_PS_P'
                      'SSID=1462_25810_21084_27244_27508'}
cookie3 = {'cookies': 'BIDUPSID=8C2E49CB424320C70B84A6F00AEB3864; PSTM=15166'
                      '94923; BAIDUID=78F888F3482A3EA109103047DD9855A1:FG=1; BDORZ='
                      'FFFB88E999055A3F8A630C64834BD6D0; pgv_pvi=7528988672; pgv_si=s64'
                      '8710144; BDRCVFR[S_ukKV6dOkf]=mk3SLVN4HKm; delPer=0; PSINO=7; '
                      'H_PS_PSSID=1462_25810_21084_27244_27508'}


# 抓取网页
url_que = my_queue.UrlQueue()
url_que.add_unvisited_url('https://baike.baidu.com')


def crawling():
    deep = 1
    while deep <= 20:
        links = []
        while not url_que.is_unvisited_url_empty():
            current_url = url_que.pop_unvisited_url()

            if current_url is None or current_url == '':
                continue
            if '.css' in current_url:
                continue
            # 获取该网页中的链接
            data, link = get_link(current_url)
            if data == '':
                continue
            # 写入文件调用添加出
            links.extend(link)
            # content = get_content(data)

            url_que.add_visited_url(current_url)
            print(len(url_que.get_visited_url()))
            if len(url_que.get_visited_url()) == 1000:
                deep = -1
                break
        if deep == -1:
            break

        for i in links:
            url_que.add_unvisited_url(i)
        deep = deep + 1
    return url_que.get_visited_url()


def get_link(current_url):
    data = get_page_text(current_url)
    if data == '':
        return '', ''
    html = etree.HTML(data)
    link_star = html.xpath('//a[@target="_blank"]/@href')
    length = len(link_star)
    links = []
    for i in range(length):
        if '#' in link_star[i]:
            continue
        if 'http' not in link_star[i]:
            links.append(current_url + link_star[i])
        else:
            links.append(link_star[i])

    return data, links


def get_page_text(the_url):
    time.sleep(0.3)
    ran = random.randint(0, 3)
    if ran == 0:
        ran_cookies = cookie1
    elif ran == 1:
        ran_cookies = cookie2
    else:
        ran_cookies = cookie3
    try:
        response = requests.get(url=the_url, headers=header, cookies=ran_cookies, timeout=3)
        encode = chardet.detect(response.content)
        encode = encode['encoding']
        response.encoding = encode
        return response.text
    except Exception:
        print('Error or time out.') ##########################
        return ''


def get_content(data):
    soup = BeautifulSoup(data, 'lxml')
    find = soup.select('a')
    s = ''
    for i in find:
        s += i.get_text()
    s = s.replace('\n', '').replace('\t', '').replace('\r', '').replace(' ', '')
    content = cut(s)
    return content


def cut(s):
    text = jieba.cut(s)
    text = '/'.join(text).split('/')
    with open('stopwords.txt', 'r', encoding='utf-8') as f:
        stopwords = f.read()
    for i in text:
        if i == "":
            text.remove(i)
            continue
        if i in stopwords:
            text.remove(i)
    return text


def write_file(url_list):
    number = 1
    for a_url in url_list:
        with open('urls//url_and_text{}.txt'.format(number), 'w', encoding='utf-8') as f:
            f.write(a_url + '\n')
            content = get_page_text(a_url)
            f.write(content)
            number += 1


def read_file():
    number = 1
    url_list = []
    doc_dict = {}
    while True:
        try:
            with open('urls//url_and_text{}.txt'.format(number), 'r', encoding='utf-8') as f:
                the_url = f.readline().strip()
                text = f.read()
                text = get_content(text)
                text = Counter(text)
                doc_dict[number] = text
                # print(the_url, doc_dict)
                number += 1
        except FileNotFoundError:
            break
    return doc_dict


def inverted_index_tf_idf(doc_dict):
    words = set()
    word_doc = {}
    for key in doc_dict.keys():
        for value in doc_dict[key].keys():
            words.add(value)

    # print(doc_dict[10]['百科'])
    for word in words:
        for k in doc_dict.keys():
            if word in doc_dict[k].keys():
                frequency = doc_dict[k][word] / len(doc_dict[k].keys())
                if word in word_doc.keys():
                    word_doc[word].append([k, doc_dict[k][word], frequency])
                else:
                    word_doc[word] = list()
                    word_doc[word].append([k, doc_dict[k][word], frequency])

    n = len(doc_dict.keys())
    for doc_i in word_doc:
        number = len(word_doc[doc_i])
        for i in word_doc[doc_i]:
            i[2] = i[2] * math.log2(n / number)
    return word_doc, n
    # with open('doc_dict.txt', 'w', encoding='utf-8') as f:
    #     for key in word_doc:
    #         f.write(key + ': ' + str(word_doc[key]) + '\n')


def turn_to_frame(word_doc, n):
    columns = list(word_doc.keys())
    index = [i + 1 for i in range(n)]
    doc_dict_frame = pd.DataFrame(index=index, columns=columns)
    for i in word_doc.keys():
        for each in word_doc[i]:
            doc_dict_frame[i][each[0]] = each[2]
    doc_dict_frame = doc_dict_frame.fillna(0)
    return doc_dict_frame


def query(doc_dict_frame):
    query_input = input("Search：")
    query_cut = list(set(cut(query_input)))
    q = pd.DataFrame(columns=doc_dict_frame.columns, index=[0])
    for i in query_cut:
        if i in q.columns:
            q[i][0] = query_input.count(i)
    q = q.fillna(0)
    similarity_degree = pd.DataFrame(index=doc_dict_frame.index, columns=['degree'])
    for index in doc_dict_frame.index:
        s = doc_dict_frame.ix[index] * q.ix[0]
        similarity_degree['degree'][index] = (s * doc_dict_frame.ix[index]).sum()
    similarity_degree.sort_values(by=['degree'], inplace=True, ascending=False)
    u_list = []
    for index in similarity_degree.index:
        if len(u_list) >= 15:
            break
        with open('urls/url_and_text{}.txt'.format(index), 'r', encoding='utf-8') as f:
            u_list.append(f.readline())
    for url in u_list:
        chinese = re.compile(u'[\u4e00-\u9fa5]')
        if not chinese.search(url):
            print('>>' + url, end='')


def main():
    # url_list = crawling()
    # write_file(url_list)
    doc_dict = read_file()
    word_doc, n = inverted_index_tf_idf(doc_dict)
    doc_dict_frame = turn_to_frame(word_doc, n)
    query(doc_dict_frame)


if __name__ == '__main__':
    main()
