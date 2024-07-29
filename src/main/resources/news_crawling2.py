import sys
import datetime
import re
import requests
from bs4 import BeautifulSoup
import pandas as pd
import json
from tqdm import tqdm

def makePgNum(num):
    return num + 9 * (num - 1)

def makeUrl(search, date, start_pg, end_pg):
    urls = []
    for i in range(start_pg, end_pg + 1):
        page = makePgNum(i)
        url = f"https://search.naver.com/search.naver?where=news&query={search}&sm=tab_opt&sort=1&photo=0&field=0&pd=3&ds={date}&de={date}&docid=&related=0&mynews=0&office_type=0&office_section_code=0&news_office_checked=&nso=so%3Ar%2Cp%3Afrom{date.replace('.', '')}to{date.replace('.', '')}%2Ca%3Aall&start={page}"
        urls.append(url)
    return urls

def news_attrs_crawler(articles, attrs):
    attrs_content = []
    for i in articles:
        attrs_content.append(i.attrs[attrs])
    return attrs_content

headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/98.0.4758.102"}

def articles_crawler(url):
    try:
        original_html = requests.get(url, headers=headers)
        original_html.raise_for_status()
        html = BeautifulSoup(original_html.text, "html.parser")
        url_naver = html.select("div.group_news > ul.list_news > li div.news_area > div.news_info > div.info_group > a.info")
        url = news_attrs_crawler(url_naver, 'href')
        return url
    except requests.exceptions.RequestException as e:
        print(f"Request failed: {e}")
        return []

def filter_urls_by_sid(urls, sid):
    filtered_urls = []
    for url in urls:
        match = re.search(r'sid=(\d+)', url)
        if match and match.group(1) == sid:
            filtered_urls.append(url)
    return filtered_urls

def get_sid_from_category(category):
    category_sid_mapping = {
        "정치": "100",
        "경제": "101",
        "사회": "102",
        "문화": "103",
        "과학": "105",
        "세계": "104",
    }
    return category_sid_mapping.get(category, "all")

def crawl_news(search, date, start_page, end_page):
    sid = get_sid_from_category(search)
    urls = makeUrl(search, date, start_page, end_page)

    news_titles = []
    news_urls = []
    news_contents = []
    news_dates = []

    for url in urls:
        urls_list = articles_crawler(url)
        news_urls.extend(urls_list)

    final_urls = filter_urls_by_sid(news_urls, sid)

    for url in tqdm(final_urls):
        try:
            news = requests.get(url, headers=headers)
            news.raise_for_status()
            news_html = BeautifulSoup(news.text, "html.parser")

            title = news_html.select_one("#ct > div.media_end_head.go_trans > div.media_end_head_title > h2")
            if title is None:
                title = news_html.select_one("#content > div.end_ct > div > h2")
            title = re.sub('<[^>]*>', '', str(title))

            content = news_html.select("article#dic_area")
            if not content:
                content = news_html.select("#articeBody")
            content = ''.join(str(content))
            content = re.sub('<[^>]*>', '', content).replace("""[\n\n\n\n\n// flash 오류를 우회하기 위한 함수 추가\nfunction _flash_removeCallback() {}""", '')

            try:
                html_date = news_html.select_one("div#ct > div.media_end_head.go_trans > div.media_end_head_info.nv_notrans > div.media_end_head_info_datestamp > div > span")
                news_date = html_date.attrs['data-date-time']
            except AttributeError:
                news_date = news_html.select_one("#content > div.end_ct > div > div.article_info > span > em")
                news_date = re.sub('<[^>]*>', '', str(news_date))

            news_titles.append(title)
            news_contents.append(content)
            news_dates.append(news_date)
        except requests.exceptions.RequestException as e:
            print(f"Failed to crawl {url}: {e}")

    news_df = pd.DataFrame({'date': news_dates, 'title': news_titles, 'link': final_urls, 'content': news_contents})
    news_df = news_df.drop_duplicates(keep='first', ignore_index=True)
    return news_df.to_dict('records')

if __name__ == "__main__":
    search = sys.argv[1]
    date = sys.argv[2]
    start_page = int(sys.argv[3])
    end_page = int(sys.argv[4])

    news_data = crawl_news(search, date, start_page, end_page)
    print(json.dumps(news_data, ensure_ascii=False))
