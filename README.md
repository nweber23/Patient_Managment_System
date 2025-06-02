# 🏥 Patient Management System

A simple Java console application for managing patient queues with priority system.

## 📋 What it does

This program helps manage patients in a waiting room. Emergency patients 🚨 get priority over regular patients 👥. It's a menu-based system where you can add patients, see who's waiting, and call the next patient.

## ✨ Features

- 🚨 Emergency patients go first
- 👥 Regular patients wait in line  
- 📋 View all patients in waiting room
- 📞 Call next patient
- ✅ Input validation for names, ages, and dates

## 📖 How to use

The program shows you a menu with 5 options:

1. **Add Patient** ➕ - Add new patient (emergency or regular)
2. **Print Waiting Room** 📋 - See all patients waiting
3. **Print Next Patient** 👁️ - See who's next in line  
4. **Call Next Patient** 📞 - Remove next patient from queue
5. **Exit** 🚪 - Quit the program

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

## 🔧 Possible improvements

Some easy features that could be added:
- 🔍 Search for a patient by name
- ⏰ Show how long each patient has been waiting
- 📊 Display total patients served today
- 💾 Save patient data to a text file

---

💡 This was one of my early Java projects to practice object-oriented programming and queue management!
