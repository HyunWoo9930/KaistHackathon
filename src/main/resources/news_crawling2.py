import sys
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
        print(f"Generated URL: {url}", file=sys.stderr)  # URL 출력
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
        print(f"URL: {url} | Status Code: {original_html.status_code}", file=sys.stderr)  # 응답 상태 코드 출력
        html = BeautifulSoup(original_html.text, "html.parser")
        url_naver = html.select("div.group_news > ul.list_news > li div.news_area > div.news_info > div.info_group > a.info")
        url = news_attrs_crawler(url_naver, 'href')
        print(f"Crawled URLs: {url}", file=sys.stderr)  # 크롤링된 URL 출력
        return url
    except requests.exceptions.RequestException as e:
        print(f"Request failed: {e}", file=sys.stderr)
        return []

def filter_urls_by_sid(urls, sid):
    filtered_urls = []
    for url in urls:
        print(f"Checking URL: {url}", file=sys.stderr)  # URL 체크
        match = re.search(r'sid=(\d+)', url)
        if match:
            found_sid = match.group(1)
            print(f"Found SID: {found_sid}", file=sys.stderr)  # 찾은 SID 출력
            if found_sid == sid:
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
    print(f"Category SID: {sid}", file=sys.stderr)  # SID 확인
    urls = makeUrl(search, date, start_page, end_page)

    news_titles = []
    news_urls = []
    news_contents = []
    news_dates = []

    for url in urls:
        urls_list = articles_crawler(url)
        news_urls.extend(urls_list)

    print(f"News URLs: {news_urls}", file=sys.stderr)  # 필터링 전의 URL 출력
    final_urls = filter_urls_by_sid(news_urls, sid)
    print(f"Filtered URLs: {final_urls}", file=sys.stderr)  # 필터링된 URL 출력

    for url in tqdm(final_urls, file=sys.stderr):
        try:
            news = requests.get(url, headers=headers)
            news.raise_for_status()
            news_html = BeautifulSoup(news.text, "html.parser")

            # 기사 제목 크롤링
            title_elem = news_html.select_one('h2.media_end_head_headline')
            if title_elem is None:
                title_elem = news_html.select_one('h2#articleTitle')
            title = re.sub('<[^>]*>', '', str(title_elem))
            print(f"Crawled title: {title}", file=sys.stderr)  # 크롤링된 제목 출력

            # 기사 내용 크롤링
            content = news_html.select("article#dic_area")
            if not content:
                content = news_html.select("#articeBody")
            content = ''.join(str(content))
            content = re.sub('<[^>]*>', '', content).replace("""[\n\n\n\n\n// flash 오류를 우회하기 위한 함수 추가\nfunction _flash_removeCallback() {}""", '')


        # 기사 날짜 크롤링
            try:
                html_date = news_html.select_one('span.media_end_head_info_datestamp_time')
                news_date = html_date.attrs['data-date-time']
            except AttributeError:
                news_date = news_html.select_one('span.t11')
                news_date = re.sub('<[^>]*>', '', str(news_date))
            print(f"Crawled date: {news_date}", file=sys.stderr)  # 크롤링된 날짜 출력

            news_titles.append(title)
            news_contents.append(content)
            news_dates.append(news_date)
        except requests.exceptions.RequestException as e:
            print(f"Failed to crawl {url}: {e}", file=sys.stderr)

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
