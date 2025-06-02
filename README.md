# 🏥 Patient Management System

A comprehensive Java console application for managing patient queues with a priority-based system.

## 📋 What it does

This program helps manage patients in a medical waiting room with a three-tier priority system. Emergency patients 🚨 get the highest priority, followed by senior patients 👴 (75+ years), and then regular patients 👥. It features a colorful, menu-based interface with comprehensive patient management capabilities.

## ✨ Features

### Queue Management
- 🚨 **Emergency patients** - Highest priority (immediate attention)
- 👴 **Senior patients** - Medium priority (75+ years old get automatic priority)
- 👥 **Regular patients** - Standard priority (first-come-first-served within group)
- 📋 View all patients in waiting room with color-coded display
- 📞 Call next patient based on priority system
- ❌ Remove specific patients from any queue by name

### Patient Information
- 📝 Complete patient details (name, age, birthday)
- 💬 **Optional notes/comments** for each patient
- 📅 Birthday validation with proper date formatting
- ✏️ **Add or update patient notes** after registration

### User Interface
- 🎨 **Colorful console interface** with ANSI color coding
- 🖼️ **Icons and visual indicators** for different patient types
- ✅ **Comprehensive input validation** for all fields
- 🔢 Age validation (0-150 years)
- 📅 Date validation (yyyy-MM-dd format)

### Statistics & Analytics
- 📈 **Comprehensive statistics display**
- 📊 Current queue composition and percentages
- 🧮 Average age calculation for all patients
- 📅 Daily patient tracking (total patients and emergencies processed)
- 🔢 Real-time queue counts for each priority level

## 📖 How to use

The program presents a colorful menu with 8 options:

1. **Add Patient** 🟢 - Register new patient with full details
2. **Print Waiting Room** 🔵 - View all patients in all queues
3. **Print Next Patient** 🔵 - See who's next in line
4. **Call Up Next Patient** 🟣 - Process next patient and remove from queue
5. **Remove Patient** 🟡 - Remove specific patient by name from any queue
6. **View Statistics** 🟠 - Display comprehensive analytics and queue statistics
7. **Add Patient Notes** ⚪ - Add or update notes for existing patients
8. **Exit** 🔴 - Quit the program safely

### Adding a patient
You'll be prompted to enter:
- **Patient name** 📝 (required, non-empty)
- **Age** 🔢 (0-150 years, validated)
- **Birthday** 📅 (yyyy-MM-dd format, e.g., 1990-05-15)
- **Emergency status** 🚨 (y/n - determines priority level)
- **Optional notes** 💬 (press Enter to skip)

### Patient Priority System
The system automatically determines patient priority:
1. **Emergency patients** 🚨 - Always processed first (regardless of age)
2. **Senior patients** 👴 - Ages 75+ get priority over regular patients
3. **Regular patients** 👥 - Standard queue processing

## 🎯 How the queue works

- **Priority-based processing**: Emergency → Senior → Regular
- **FIFO within priority levels**: First-come-first-served within each group
- **Automatic senior detection**: Patients 75+ automatically get senior priority (unless emergency)
- **Maximum capacity**: 100 patients per priority type (300 total)
- **Smart queue management**: Patients are automatically shifted when others are removed

## 📊 Statistics Features

The statistics view provides:
- **Current queue status** with patient counts per priority
- **Today's totals** including emergency count
- **Average age calculation** across all waiting patients
- **Queue composition percentages** for insights
- **Color-coded display** for easy reading

## 📁 Project Structure

- `Main.java` - Application entry point
- `Controller.java` - User interface and input handling with validation
- `PatientManagement.java` - Core queue management and statistics
- `Patient.java` - Abstract base class with common patient functionality
- `EmergencyPatient.java` - Emergency patient implementation
- `SeniorPatient.java` - Senior patient implementation (75+ years)
- `RegularPatient.java` - Regular patient implementation

## 🔧 Technical Features

- **Object-oriented design** with inheritance and polymorphism
- **Robust input validation** with error handling
- **Memory-efficient array management** with dynamic shifting
- **Color-coded console output** using ANSI escape codes
- **Date handling** with LocalDate and proper formatting
- **Exception handling** for graceful error management

## 🚀 Recent Enhancements

This system has been enhanced with several advanced features:
- ✅ **Senior patient priority system** (75+ years get automatic priority)
- ✅ **Patient notes functionality** with add/update capabilities
- ✅ **Comprehensive statistics and analytics**
- ✅ **Enhanced visual interface** with icons and color coding
- ✅ **Robust patient removal** from any queue
- ✅ **Advanced input validation** and error handling

---

💡 This project demonstrates advanced Java concepts including inheritance, polymorphism, array management, input validation, and user interface design. Perfect for learning queue management algorithms and object-oriented programming principles!
