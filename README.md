# 🏥 Patient Management System

A comprehensive Java console application for managing patient queues with a priority-based system, colorful interface, and advanced patient tracking capabilities.

## 📋 Overview

This program efficiently manages patients in a medical waiting room using a three-tier priority system with automatic queue sorting. Emergency patients 🚨 receive immediate attention, followed by senior patients 👴 (75+ years), then regular patients 👥. The system maintains chronological order within each priority level.

## ✨ Core Features

### 🎯 Priority Queue Management
- **🚨 Emergency Queue** - Highest priority with immediate processing
- **👴 Senior Queue** - Medium priority for patients 75+ years old
- **👥 Regular Queue** - Standard FIFO processing for all other patients
- **⚡ Automatic Queue Sorting** - Patients are automatically ordered by priority and arrival time
- **📞 Smart Patient Calling** - Next patient is determined by priority system
- **⚖️ Queue Capacity Management** - Maximum limits for each queue type

### 📝 Patient Information & Documentation
- **Complete Patient Profiles** - Name, age, birthday, and patient type
- **📚 Timestamped Notes System** - Add unlimited notes with automatic timestamps
- **📖 Complete Note History** - View full chronological note timeline for any patient
- **🔄 Dynamic Type Changes** - Change patient priority with automatic validation
- **⏰ Arrival Time Tracking** - Automatic timestamp when patients join the queue
- **✏️ Patient Information Editing** - Modify existing patient details

### 🔍 Search & Analytics
- **🔎 Fuzzy Name Search** - Find patients with partial name matching
- **📊 Real-time Statistics Dashboard** - Live queue analytics and composition
- **📈 Daily Metrics** - Track total patients and emergencies processed
- **🎨 Color-coded Interface** - Visual indicators for different patient types and actions

### 🛠️ Advanced Management
- **🗑️ Bulk Operations** - Clear entire queues or patient types
- **📊 Queue Health Check** - System status and capacity monitoring
- **⚡ Emergency Override** - Override capacity limits for emergencies
- **🔄 Queue Optimization** - Analyze and optimize queue distribution

## 🎮 Interactive Menu

| Option | Feature | Description |
|--------|---------|-------------|
| **1** | 🔵 Add Patient | Register new patient with full details |
| **2** | 🔵 Print Waiting Room | Display all patients by priority queue |
| **3** | 🔵 Print Next Patient | Preview who will be called next |
| **4** | 🟢 Call Up Next Patient | Process highest priority patient |
| **5** | 🔴 Remove Patient | Remove patient by exact name match |
| **6** | 🔵 View Statistics | Comprehensive analytics dashboard |
| **7** | 🔵 Add Patient Notes | Append timestamped notes to existing patients |
| **8** | 🔵 Search Patient by Name | Find patients with partial matching |
| **9** | 🟡 Change Patient Type | Modify priority level with validation |
| **10** | 🔵 View Patient Note History | Complete chronological note timeline |
| **11** | 🔵 Edit Patient Information | Modify existing patient details |
| **12** | 🔴 Bulk Operations | Manage multiple patients at once |
| **13** | 🟡 Queue Management | Advanced queue control features |
| **14** | 🔵 System Status | View detailed system information |
| **15** | 🔴 Exit | Safe application shutdown |

## ⚙️ Priority System Logic

### 🏆 Queue Hierarchy
1. **🚨 Emergency** (Priority 1) - Any age, immediate attention
2. **👴 Senior** (Priority 2) - Ages 75+, expedited service  
3. **👥 Regular** (Priority 3) - Standard processing order

### 📋 Business Rules
- **Age-based Auto-assignment**: Patients 75+ automatically assigned Senior status (unless Emergency)
- **Type Change Validation**: Only patients 75+ can be changed to Senior type
- **Emergency Override**: Emergency status available for any age
- **FIFO Within Priority**: Patients of same priority processed by arrival time

## 📊 Analytics Dashboard

### Current Queue Status
- Real-time patient counts per priority level
- Total patients currently waiting
- Next patient preview with type indicator

### Daily Performance Metrics  
- Total patients processed today
- Emergency cases handled
- Average age of current patients
- Queue composition percentages

## 🛠️ Technical Architecture

### Object-Oriented Design
- **Patient Class** - Core patient data with note management
- **PatientType Enum** - Type-safe priority and display management  
- **PatientManagement Class** - Queue operations and business logic
- **Controller Class** - User interface and input validation
- **Colors Class** - ANSI color constants for visual feedback

### Advanced Features
- **PriorityQueue Implementation** - Efficient automatic sorting
- **Stream API Usage** - Modern Java functional programming
- **Input Validation Framework** - Generic validation with custom predicates
- **Exception Handling** - Comprehensive error recovery
- **Date/Time Management** - LocalDate and LocalDateTime integration

## 🎨 Visual Design

The application features a rich color-coded interface:
- **🔴 Red** - Emergency patients and critical actions
- **🟠 Orange** - Senior patients  
- **🔵 Blue** - Regular patients and informational displays
- **🟢 Green** - Success messages and confirmations
- **🟡 Yellow** - Warnings and input prompts
- **🔵 Cyan** - Headers and system information

---

**Perfect for learning:** Queue data structures, priority systems, object-oriented design, user interface development, and modern Java programming practices! 
