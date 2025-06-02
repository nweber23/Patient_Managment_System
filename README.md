# 🏥 Patient Management System

A comprehensive Java console application for managing patient queues with a priority-based system and colorful interface.

## 📋 Overview

This program manages patients in a medical waiting room using a three-tier priority system: Emergency patients 🚨 get highest priority, followed by senior patients 👴 (75+ years), then regular patients 👥.

## ✨ Current Features

### Queue Management
- 🚨 **Emergency patients** - Highest priority (immediate attention)
- 👴 **Senior patients** - Medium priority (75+ years automatic priority)
- 👥 **Regular patients** - Standard priority (FIFO within group)
- 📋 View all patients with color-coded display
- 📞 Call next patient based on priority system
- ❌ Remove patients by name from any queue

### Patient Information & Notes
- 📝 Complete patient details (name, age, birthday)
- 💬 **Add notes** to existing patients with timestamps
- 📚 **View complete note history** for any patient
- 🔄 **Change patient type** with age validation (Senior requires 75+)

### Search & Navigation
- 🔍 **Search patients by name** (partial name matching)
- 📊 **Real-time statistics** with queue composition
- 🎨 **Colorful console interface** with visual indicators

## 📖 Menu Options

1. **Add Patient** 🔵 - Register new patient
2. **Print Waiting Room** 🔵 - View all patients
3. **Print Next Patient** 🔵 - See who's next
4. **Call Up Next Patient** 🟢 - Process next patient
5. **Remove Patient** 🔴 - Remove by name
6. **View Statistics** 🔵 - Display analytics
7. **Add Patient Notes** 🔵 - Add timestamped notes
8. **Search Patient by Name** 🔵 - Find patients quickly
9. **Change Patient Type** 🟡 - Switch priority levels
10. **View Patient Note History** 🔵 - Complete note timeline
11. **Exit** 🔴 - Quit safely

## 🎯 Priority System

1. **Emergency** 🚨 - Always first (any age)
2. **Senior** 👴 - Ages 75+ get priority
3. **Regular** 👥 - Standard queue

**Age Validation**: Only patients 75+ can be changed to Senior type.

## 📊 Statistics Include
- Current queue counts per priority
- Total patients processed today
- Average age of waiting patients
- Queue composition percentages

## 🔧 Technical Features
- Object-oriented design with inheritance
- Comprehensive input validation
- ANSI color-coded interface
- Date handling with LocalDate
- Exception handling and error recovery

## 🚀 Upcoming Features

- 📝 **Note preservation** when changing patient types
- ⏰ **Waiting time display** for each patient
- 📋 **Patient count limit** with queue full notification
- 🔤 **Sort patients alphabetically** within priority groups
- ✏️ **Edit patient information** (name, age, birthday)
- 🗑️ **Clear all patients** from specific queue types
- 📄 **Print patient summary** with basic details only

---

💡 Perfect for learning queue management, object-oriented programming, and user interface design in Java!
