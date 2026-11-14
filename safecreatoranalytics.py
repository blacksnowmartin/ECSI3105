import pandas as pd
from datetime import datetime

class CreatorAnalytics:
    def __init__(self, handle: str):
        self.handle = handle
        self.records = []

    def log_post(self, platform: str, post_id: str, views: int, likes: int, comments: int, timestamp: str):
        engagement_rate = round(((likes + comments) / views) * 100, 2) if views > 0 else 0.0
        self.records.append({
            "platform": platform,
            "post_id": post_id,
            "views": views,
            "likes": likes,
            "comments": comments,
            "engagement_rate_%": engagement_rate,
            "hour": datetime.fromisoformat(timestamp).hour
        })

    def summary(self) -> pd.DataFrame:
        df = pd.DataFrame(self.records)
        if df.empty:
            return df
        return df.groupby("platform").agg(
            total_posts=("post_id", "count"),
            avg_views=("views", "mean"),
            avg_engagement=("engagement_rate_%", "mean"),
            best_hour=("hour", lambda x: x.mode()[0])
        ).reset_index()

# Example Usage
tracker = CreatorAnalytics("@blacksnowmartin")
tracker.log_post("TikTok", "vid_01", 12500, 940, 65, "2026-11-14T19:30:00")
tracker.log_post("Facebook", "post_01", 4300, 310, 42, "2026-11-14T20:15:00")
print(tracker.summary())
