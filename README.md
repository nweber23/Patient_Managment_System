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

## 🧪 Additional easy features to add

Some more simple features you could implement:

- 🕒 **Show how long each patient has been waiting**  
  Calculate and display wait time in minutes using check-in time.

- 📅 **List only patients who arrived today**  
  Filter the waiting list by today’s date.

- 🧓 **Give priority to senior patients (e.g. age 75+)**  
  Add a new priority level between emergency and regular patients.

- 💬 **Add optional notes/comments to each patient**  
  Allow adding short notes like “severe pain” or “needs assistance”.

- 📈 **Show statistics**  
  Display total patients today, number of emergencies, and average age.

- 🔄 **Recall last called patient if not present**  
  Let users re-call the most recently removed patient.

- 🧾 **Export patient list to a `.csv` file**  
  Save current queue data to a file for external viewing.

- 🖼️ **Add icons/tags for emergency, senior, and regular patients**  
  Example: `🚨 [EMERGENCY]`, `👴 [Senior]`, `👥 [Regular]`

---

💡 This was one of my early Java projects to practice object-oriented programming and queue management!
