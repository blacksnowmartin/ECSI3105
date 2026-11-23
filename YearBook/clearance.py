from flask import Flask, request, jsonify

app = Flask(__name__)

# Sample graduand records: reg_no -> details
graduands = {
    "SCT211-0001/2022": {"name": "Amina Kamau", "cleared": True, "checked_in": False},
    "SCT211-0002/2022": {"name": "Brian Ochieng", "cleared": False, "checked_in": False},
}

@app.route("/check-in/<path:reg_no>", methods=["POST"])
def check_in(reg_no):
    student = graduands.get(reg_no)
    if not student:
        return jsonify({"error": "Graduand not found"}), 404
    if not student["cleared"]:
        return jsonify({"error": "Pending graduation clearance", "name": student["name"]}), 400
    
    student["checked_in"] = True
    return jsonify({"status": "Checked in", "name": student["name"]})

if __name__ == "__main__":
    app.run(debug=True)
