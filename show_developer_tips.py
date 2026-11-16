def show_developer_tips():
    tips = {
        "1. Code on Paper and Study Books": [
            "Write code by hand: Draft functions and algorithms on paper to test syntax recall.",
            "Read programming books: Study core concepts and system design page by page."
        ],
        "2. Use a Smartphone or Tablet": [
            "Use mobile coding apps: Practice syntax with apps like Sololearn or Mimo.",
            "Set up a mobile terminal: Use Termux on Android with a Bluetooth keyboard.",
            "Access cloud IDEs: Use browser-based workspaces like GitHub Codespaces."
        ],
        "3. Study System Design and Architecture": [
            "Diagram systems: Draw database schemas and API flows on paper.",
            "Read engineering blogs: Learn how major companies handle scale."
        ],
        "4. Leverage Public and Shared Spaces": [
            "Visit local libraries or labs: Use shared computers during open hours.",
            "Network and find mentors: Attend local meetups or watch tech talks."
        ],
        "5. Build Logic and Analytical Thinking Offline": [
            "Play logic games: Solve puzzles or play chess to boost reasoning.",
            "Explain concepts out loud: Teach topics like recursion in plain words."
        ]
    }

    print("=== HOW TO IMPROVE AS A DEVELOPER WITHOUT A LAPTOP ===\n")
    
    for category, advice_list in tips.items():
        print(category)
        for tip in advice_list:
            print(f"  * {tip}")
        print()  # Blank line for readability

if __name__ == "__main__":
    show_developer_tips()
