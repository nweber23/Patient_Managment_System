# 🏥 Patient Management System

A simple Java console application for managing patient queues with priority system.

## 📋 What it does

This program helps manage patients in a waiting room. Emergency patients 🚨 get priority over regular patients 👥. It's a menu-based system where you can add patients, see who's waiting, and call the next patient.

## ✨ Features

- 🚨 Emergency patients go first
- 👥 Regular patients wait in line  
- 📋 View all patients in waiting room
- 📞 Call next patient
- ❌ Remove specific patients from queue
- 🎨 Colorful console interface
- ✅ Input validation for names, ages, and dates

## 📖 How to use

The program shows you a colorful menu with 6 options:

1. **Add Patient** 🟢 - Add new patient (emergency or regular)
2. **Print Waiting Room** 🔵 - See all patients waiting
3. **Print Next Patient** 🔵 - See who's next in line  
4. **Call Next Patient** 🟣 - Remove next patient from queue
5. **Remove Patient** 🟡 - Remove a specific patient by name
6. **Exit** 🔴 - Quit the program

### Adding a patient
You'll need to enter:
- Patient name 📝
- Age (0-150) 🔢
- Birthday (format: yyyy-mm-dd) 📅
- Emergency status (y/n) 🚨

## 🎯 How the queue works

- Emergency patients always go first 🚨
- Within each group (emergency/regular), it's first-come-first-served
- Maximum 100 patients per type

## 📁 Files

- `Main.java` - Starts the program
- `Controller.java` - Handles menus and user input
- `PatientManagement.java` - Manages the patient queues
- `Patient.java` - Base class for patients
- `EmergencyPatient.java` - Emergency patient type
- `RegularPatient.java` - Regular patient type

## 🧪 Additional Easy Implementations
Here are some simple improvements you can add to your program:

🕒 Show Patient Wait Time
Display how long each patient has been waiting (in minutes), based on their check-in time.

📅 Show Today's Patients
Filter and display only the patients who were added today.

🧓 Prioritize Senior Patients
Give patients aged 75+ higher priority than regular patients, just below emergency cases.

💬 Add Notes to Patients
Allow users to add an optional note or comment when adding a new patient (e.g., "severe pain", "accompanied by mother").

📈 View Statistics
Add a menu option to show basic statistics:

Total patients added today

Number of emergency patients

Average age of all patients

🔄 Recall Last Called Patient
If the last called patient didn’t respond, allow re-adding them to the queue.

🧾 Export Patient List to CSV
Export the current queue to a .csv file including name, age, birthday, emergency status, and wait time.

🖼️ Add Icons or ASCII Tags
Add visual tags to patient types in the console output:

🚨 [EMERGENCY]

👴 [Senior]

👥 [Regular]

---

💡 This was one of my early Java projects to practice object-oriented programming and queue management!
