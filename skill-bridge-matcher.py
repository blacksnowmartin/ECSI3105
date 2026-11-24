from dataclasses import dataclass
from typing import List, Dict


@dataclass
class JobSeeker:
    name: str
    county: str
    skills: set[str]
    has_smartphone: bool
    available_hours_per_week: int


@dataclass
class MicroOpportunity:
    title: str
    county: str
    required_skills: set[str]
    weekly_hours: int
    weekly_pay_kes: int
    offers_micro_stipend_training: bool


def match_opportunities(
    seekers: List[JobSeeker], opportunities: List[MicroOpportunity]
) -> Dict[str, List[tuple[str, int, str]]]:
    """Matches seekers to local gigs or skill-bridge tracks based on location,
    time capacity, and skill overlap (including 1-skill bridge gaps).
    """
    assignments: Dict[str, List[tuple[str, int, str]]] = {s.name: [] for s in seekers}
    remaining_hours = {s.name: s.available_hours_per_week for s in seekers}

    for seeker in seekers:
        # Sort opportunities so high-paying, exact-fit local gigs come first
        ranked_opps = sorted(
            opportunities,
            key=lambda opp: (
                opp.county.lower() == seeker.county.lower(),
                len(opp.required_skills & seeker.skills),
                opp.weekly_pay_kes,
            ),
            reverse=True,
        )

        for opp in ranked_opps:
            if opp.county.lower() != seeker.county.lower():
                continue
            if opp.weekly_hours > remaining_hours[seeker.name]:
                continue

            missing_skills = opp.required_skills - seeker.skills

            # Case 1: Immediate skill match
            if not missing_skills:
                assignments[seeker.name].append(
                    (opp.title, opp.weekly_pay_kes, "Direct Match")
                )
                remaining_hours[seeker.name] -= opp.weekly_hours

            # Case 2: Missing only 1 skill, and opportunity offers micro-training
            elif len(missing_skills) == 1 and opp.offers_micro_stipend_training:
                needed = next(iter(missing_skills))
                assignments[seeker.name].append(
                    (
                        f"{opp.title} [Bridge: learn '{needed}']",
                        int(opp.weekly_pay_kes * 0.7),
                        "Skill-Bridge Track",
                    )
                )
                remaining_hours[seeker.name] -= opp.weekly_hours

    return assignments


if __name__ == "__main__":
    seekers = [
        JobSeeker("Amina", "Nairobi", {"excel", "customer_service"}, True, 30),
        JobSeeker("Brian", "Kisumu", {"plumbing", "basic_math"}, False, 40),
        JobSeeker("Faith", "Mombasa", {"photography", "social_media"}, True, 25),
    ]

    opportunities = [
        MicroOpportunity(
            "SME Inventory & M-Pesa Ledger Audit",
            "Nairobi",
            {"excel", "bookkeeping"},
            15,
            6500,
            offers_micro_stipend_training=True,
        ),
        MicroOpportunity(
            "Shop Helpdesk & Order Follow-up",
            "Nairobi",
            {"customer_service"},
            15,
            5000,
            offers_micro_stipend_training=False,
        ),
        MicroOpportunity(
            "Water Tank & Pipe Maintenance Sprint",
            "Kisumu",
            {"plumbing"},
            20,
            8000,
            offers_micro_stipend_training=False,
        ),
        MicroOpportunity(
            "Local Brand Reels & Product Shoots",
            "Mombasa",
            {"photography", "social_media"},
            20,
            7500,
            offers_micro_stipend_training=False,
        ),
    ]

    results = match_opportunities(seekers, opportunities)
    for person, gigs in results.items():
        total_kes = sum(pay for _, pay, _ in gigs)
        print(f"\n{person} (Est. weekly income: KES {total_kes:,}):")
        if not gigs:
            print("  - No immediate local match; queue for county TVET/digital hub.")
        for title, pay, mode in gigs:
            print(f"  - [{mode}] {title} -> KES {pay:,}/wk")
