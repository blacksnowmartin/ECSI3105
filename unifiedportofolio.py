import json
from dataclasses import dataclass, asdict
from typing import List

@dataclass
class ProfileManifest:
    handle: str
    name: str
    location: str
    roles: List[str]
    education: str
    links: dict

    def export_json(self, filepath: str = "blacksnow_profile.json"):
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(asdict(self), f, indent=4)
        print(f"Exported profile manifest to {filepath}")

profile = ProfileManifest(
    handle="@blacksnowmartin",
    name="Martin Mwendwa",
    location="Nairobi, Kenya",
    roles=["Quantitative Research Consultant", "Software Developer", "Digital Creator"],
    education="Technical University of Kenya",
    links={
        "github": "https://github.com/blacksnowmartin",
        "facebook": "https://www.facebook.com/martin.mwendwa.3110/"
    }
)
profile.export_json()
