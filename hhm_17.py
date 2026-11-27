from dataclasses import dataclass, field
from datetime import date


@dataclass
class WaterSystem:
    tank_capacity_liters: float
    current_liters: float
    daily_usage_liters: float

    def days_remaining(self) -> float:
        if self.daily_usage_liters <= 0:
            return float("inf")
        return self.current_liters / self.daily_usage_liters

    def add_water(self, liters: float, source: str = "rain/vendor") -> None:
        self.current_liters = min(self.tank_capacity_liters, self.current_liters + liters)
        print(
            f"[WATER] +{liters:.0f}L ({source}) -> "
            f"{self.current_liters:.0f}L / {self.tank_capacity_liters:.0f}L"
        )

    def log_daily_use(self, liters: float | None = None) -> None:
        used = self.daily_usage_liters if liters is None else liters
        self.current_liters = max(0.0, self.current_liters - used)
        days = self.days_remaining()
        alert = "LOW - ration or order water!" if days < 5 else "OK"
        print(f"[WATER] -{used:.0f}L -> {self.current_liters:.0f}L left (~{days:.1f} days) [{alert}]")


@dataclass
class HouseholdManager:
    homestead_name: str
    water: WaterSystem
    monthly_budget_kes: float
    expenses_kes: list[tuple[str, str, float]] = field(default_factory=list)
    pantry_kg: dict[str, tuple[float, float]] = field(default_factory=dict)

    def log_expense(self, category: str, note: str, amount_kes: float) -> None:
        self.expenses_kes.append((category, note, amount_kes))
        spent = sum(amount for _, _, amount in self.expenses_kes)
        remaining = self.monthly_budget_kes - spent
        print(
            f"[BUDGET] {category}: KES {amount_kes:,.0f} ({note}) | "
            f"Spent: KES {spent:,.0f} | Left: KES {remaining:,.0f}"
        )

    def use_pantry(self, item: str, kg_used: float) -> None:
        if item not in self.pantry_kg:
            print(f"[PANTRY] '{item}' not tracked.")
            return
        current_kg, min_kg = self.pantry_kg[item]
        new_kg = max(0.0, current_kg - kg_used)
        self.pantry_kg[item] = (new_kg, min_kg)
        alert = "RESTOCK NEEDED!" if new_kg <= min_kg else "OK"
        print(f"[PANTRY] {item}: {new_kg:.1f}kg left (min {min_kg:.1f}kg) [{alert}]")

    def restock_pantry(self, item: str, kg_added: float, min_kg: float | None = None) -> None:
        current_kg, old_min = self.pantry_kg.get(item, (0.0, 5.0))
        target_min = old_min if min_kg is None else min_kg
        self.pantry_kg[item] = (current_kg + kg_added, target_min)
        print(f"[PANTRY] Restocked +{kg_added:.1f}kg {item} -> {current_kg + kg_added:.1f}kg")

    def daily_summary(self) -> None:
        spent = sum(amount for _, _, amount in self.expenses_kes)
        low_items = [item for item, (qty, min_qty) in self.pantry_kg.items() if qty <= min_qty]
        print(f"\n=== {self.homestead_name} ({date.today()}) ===")
        print(
            f"Water: {self.water.current_liters:.0f}L / {self.water.tank_capacity_liters:.0f}L "
            f"(~{self.water.days_remaining():.1f} days)"
        )
        print(f"Budget: KES {spent:,.0f} used of KES {self.monthly_budget_kes:,.0f}")
        print(f"Low Pantry Items: {', '.join(low_items) if low_items else 'None'}")


if __name__ == "__main__":
    tank = WaterSystem(tank_capacity_liters=10000, current_liters=2400, daily_usage_liters=350)
    home = HouseholdManager(
        homestead_name="Kikumbulyu Homestead",
        water=tank,
        monthly_budget_kes=25000,
        pantry_kg={"maize": (45.0, 20.0), "beans": (12.0, 8.0), "flour": (4.0, 5.0)},
    )
    home.water.log_daily_use()
    home.water.add_water(3000, source="roof rain catchment")
    home.log_expense("Energy", "Solar battery check & paraffin", 1800)
    home.log_expense("Food", "Market vegetables & cooking oil", 1250)
    home.use_pantry("flour", 1.5)
    home.daily_summary()
