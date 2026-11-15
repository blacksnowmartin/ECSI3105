import csv
import datetime
import urllib.request
import xml.etree.ElementTree as ET
import os

# Replace with your actual YouTube Channel ID
CHANNEL_ID = "YOUR_YOUTUBE_CHANNEL_ID"
RSS_URL = f"https://youtube.com{CHANNEL_ID}"
LOG_FILE = "release_tracker.csv"

def fetch_latest_uploads(rss_url):
    response = urllib.request.urlopen(rss_url)
    xml_data = response.read()
    root = ET.fromstring(xml_data)
    
    # YouTube Atom XML namespaces
    ns = {
        'atom': 'http://w3.org',
        'yt': 'http://youtube.com'
    }
    
    videos = []
    for entry in root.findall('atom:entry', ns):
        video_id = entry.find('yt:videoId', ns).text
        title = entry.find('atom:title', ns).text
        published = entry.find('atom:published', ns).text
        link = f"https://youtube.com{video_id}"
        videos.append({
            "id": video_id,
            "title": title,
            "published": published,
            "link": link
        })
    return videos

def log_and_generate_promo(videos):
    logged_ids = set()
    file_exists = os.path.isfile(LOG_FILE)
    
    if file_exists:
        with open(LOG_FILE, mode='r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                logged_ids.add(row['video_id'])
                
    with open(LOG_FILE, mode='a', newline='', encoding='utf-8') as f:
        fieldnames = ['date_logged', 'video_id', 'title', 'published', 'link']
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        if not file_exists:
            writer.writeheader()
            
        for video in videos:
            if video['id'] not in logged_ids:
                writer.writerow({
                    'date_logged': datetime.date.today().isoformat(),
                    'video_id': video['id'],
                    'title': video['title'],
                    'published': video['published'],
                    'link': video['link']
                })
                print(f"\n[NEW RELEASE LOGGED]: {video['title']}")
                print("--- Copy-Paste Promo Caption ---")
                print(f"New remix out now! 🎶🔥 {video['title']}")
                print(f"Listen full track here: {video['link']}")
                print("#KambaBenga #BengaRemix #BlacksnowMartin #NewMusic")
                print("--------------------------------\n")

if __name__ == "__main__":
    print("Checking for new uploads...")
    try:
        latest_videos = fetch_latest_uploads(RSS_URL)
        log_and_generate_promo(latest_videos)
    except Exception as e:
        print(f"Error fetching feed: {e}")
