import datetime
import random
import string

class MbukoniTicketingSystem:
    def __init__(self):
        self.sacco_name = "MBUKONI SERVICES LTD"
        self.paybill = "4098210"  # Simulated SACCO Paybill
        
        # Realistic Ukambani / Mombasa Road routes & base fares (KES)
        self.routes = {
            "NRB-EML": {"origin": "Nairobi (Machakos Country Bus)", "dest": "Emali", "fare": 500},
            "NRB-MKD": {"origin": "Nairobi (Machakos Country Bus)", "dest": "Makindu", "fare": 600},
            "NRB-KBW": {"origin": "Nairobi (Machakos Country Bus)", "dest": "Kibwezi", "fare": 700},
            "NRB-MTM": {"origin": "Nairobi (Machakos Country Bus)", "dest": "Mutomo", "fare": 800},
            "NRB-KTU": {"origin": "Nairobi (Machakos Country Bus)", "dest": "Kitui", "fare": 750},
            "EML-MKD": {"origin": "Emali", "dest": "Makindu", "fare": 150},
        }

        # Active fleet vehicles (14-seater shuttles)
        self.vehicles = {
            "KDN 412T": {"driver": "Mutua Musyoka", "route": "NRB-KBW", "departure": "11:30 AM", "booked_seats": [1, 2, 5]},
            "KDP 889M": {"driver": "Johnstone Mwanzia", "route": "NRB-MTM", "departure": "12:15 PM", "booked_seats": [1, 4, 7, 10]},
            "KDJ 104W": {"driver": "Peter Nzomo", "route": "NRB-EML", "departure": "11:45 AM", "booked_seats": [2, 3]},
        }

        # Customer loyalty database (1 point per KES 100 spent)
        self.loyalty_db = {
            "0712345678": {"name": "Faith Kamene", "points": 240},
            "0722987654": {"name": "Brian Kilonzo", "points": 55},
        }

    def get_loyalty_tier(self, points):
        if points >= 1000:
            return "Platinum"
        elif points >= 500:
            return "Gold"
        elif points >= 200:
            return "Silver"
        return "Bronze"

    def show_seat_map(self, reg_no):
        vehicle = self.vehicles[reg_no]
        booked = vehicle["booked_seats"]
        print(f"\n--- SEAT MAP: {reg_no} ({vehicle['route']} | Dep: {vehicle['departure']}) ---")
        print("[DRV]       [ 1 ] [ 2 ]")  # Front passenger seats
        
        seat_num = 3
        for row in range(4):  # 4 rows of 3 seats = 12 back seats (Total 14)
            row_display = []
            for col in range(3):
                if seat_num in booked:
                    row_display.append("[ X ]")
                else:
                    row_display.append(f"[{seat_num:2d} ]")
                seat_num += 1
            print(" ".join(row_display))
        print("([ X ] = Booked)\n")

    def generate_mpesa_code(self):
        # Realistic 10-character M-Pesa receipt format (e.g., SK94H2L8P1)
        prefix = "SK"
        body = ''.join(random.choices(string.ascii_uppercase + string.digits, k=8))
        return prefix + body

    def book_ticket(self, passenger_name, phone, reg_no, seat_no, payment_method="M-Pesa"):
        if reg_no not in self.vehicles:
            return "Error: Vehicle not found in active fleet."
        
        vehicle = self.vehicles[reg_no]
        if seat_no < 1 or seat_no > 14:
            return "Error: Invalid seat number (Choose 1–14)."
        if seat_no in vehicle["booked_seats"]:
            return f"Error: Seat {seat_no} is already booked on {reg_no}."

        # Reserve seat
        vehicle["booked_seats"].append(seat_no)
        
        # Calculate fare & loyalty points
        route_info = self.routes[vehicle["route"]]
        fare = route_info["fare"]
        earned_points = fare // 100

        if phone not in self.loyalty_db:
            self.loyalty_db[phone] = {"name": passenger_name, "points": 0}
        self.loyalty_db[phone]["points"] += earned_points
        total_points = self.loyalty_db[phone]["points"]
        tier = self.get_loyalty_tier(total_points)

        ticket_id = f"MBK-{random.randint(100000, 999999)}"
        mpesa_ref = self.generate_mpesa_code() if payment_method == "M-Pesa" else "CASH-RCPT"
        timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        receipt = f"""
========================================
        {self.sacco_name}
      Tel: 0700 123 456 | Paybill: {self.paybill}
========================================
TICKET NO   : {ticket_id}
DATE/TIME   : {timestamp}
PASSENGER   : {passenger_name.upper()} ({phone})
----------------------------------------
ROUTE       : {route_info['origin']} -> {route_info['dest']}
VEHICLE     : {reg_no}
SEAT NO     : {seat_no}
DEPARTURE   : {vehicle['departure']}
----------------------------------------
FARE PAID   : KES {fare}.00
PAYMENT     : {payment_method} ({mpesa_ref})
LOYALTY     : +{earned_points} pts (Total: {total_points} | {tier})
========================================
   Safe Journey! Keep receipt for inspection.
========================================
"""
        return receipt


# --- Example Usage ---
if __name__ == "__main__":
    system = MbukoniTicketingSystem()

    # 1. Check available seats on KDN 412T (Nairobi -> Kibwezi)
    system.show_seat_map("KDN 412T")

    # 2. Issue a ticket for Seat 6
    ticket = system.book_ticket(
        passenger_name="Faith Kamene",
        phone="0712345678",
        reg_no="KDN 412T",
        seat_no=6,
        payment_method="M-Pesa"
    )
    print(ticket)

    # 3. View updated seat map showing Seat 6 as [ X ]
    system.show_seat_map("KDN 412T")
