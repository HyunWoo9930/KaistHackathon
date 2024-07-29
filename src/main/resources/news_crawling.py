import requests
from bs4 import BeautifulSoup
from tqdm import tqdm
import pandas as pd

# 각 카테고리 URL 설정
category_urls = {
    "정치": "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100",
    "경제": "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101",
    "사회": "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102",
    "생활/문화": "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103",
    "세계": "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104",
    "IT/과학": "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105"
}

# 기사 링크 수집 함수


def get_links(url):
    html = requests.get(url, headers={"User-Agent": "Mozilla/5.0"})
    soup = BeautifulSoup(html.text, "lxml")
    links = [a['href'] for a in soup.select(
        'a[href^="https://n.news.naver.com/mnews/article/"]')]
    return links

# 기사 정보 수집 함수


def get_article_info(url):
    html = requests.get(url, headers={"User-Agent": "Mozilla/5.0"})
    soup = BeautifulSoup(html.text, "lxml")

    title = soup.select_one("h2.media_end_head_headline").text.strip(
    ) if soup.select_one("h2.media_end_head_headline") else None
    date = soup.select_one("span.media_end_head_info_datestamp_time").text.strip(
    ) if soup.select_one("span.media_end_head_info_datestamp_time") else None
    # main = soup.select_one("div#dic_area").text.strip(
    # ) if soup.select_one("div#dic_area") else None
    main = soup.select_one("article#dic_area").text.strip(
    ) if soup.select_one("article#dic_area") else None

    return {"title": title, "date": date, "main": main, "url": url}


all_articles = []

# 각 카테고리별로 크롤링
for category, url in category_urls.items():
    print(f"Collecting articles for category: {category}")
    links = get_links(url)
    for link in tqdm(links):
        article = get_article_info(link)
        article["category"] = category
        all_articles.append(article)
        print(article)  # 크롤링된 기사 데이터 출력

# 데이터프레임 생성 및 저장
df = pd.DataFrame(all_articles)
print(df.head())  # 데이터프레임 상위 5개 행 출력
df.to_csv("/Users/hyunwoo/Desktop/coding_projects/news_crolling/naver_articles.csv", index=False)
