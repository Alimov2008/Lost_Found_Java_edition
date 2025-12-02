# Lost & Found System

A portable Java application for managing lost and found items with a console-based interface.

## 📋 Features

- ✅ Report lost items
- ✅ Report found items  
- ✅ View all items (lost/found)
- ✅ Search items by name
- ✅ Delete items
- ✅ Persistent SQLite database
- ✅ Portable - runs anywhere with Java

## 🚀 Quick Start

### Prerequisites
- **Java 17 or later** must be installed on your system

### How to Check Java Version
```bash
java -version
```

If you don't have Java, download it from:
- [Oracle Java](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [OpenJDK](https://openjdk.org/projects/jdk/17/)

### Running the Application

#### Option 1: Direct JAR Execution
```bash
java -jar Lost_Found-1.0-SNAPSHOT.jar
```

#### Option 2: Using Provided Scripts

**Windows:**
- Double-click `run.bat`
- Or run in Command Prompt: `run.bat`

**Linux/Mac:**
- Make script executable: `chmod +x run.sh`
- Run: `./run.sh`

### Creating Jar file  from pom.xml
```bash
mvn clean compile
mvn clean package
```

## 🗃️ Database Location

The application automatically creates and manages its database:
- **Location**: `./LostFoundData/lostfound.db` (in the same folder as the JAR)
- **No setup required** - everything works out of the box

## 🎮 How to Use

### Main Menu
```
=== MAIN MENU ===
1. Report Item
2. View Items
3. Search Items
4. Delete Items
5. Exit
```

### Reporting Items
1. Choose "Report Item"
2. Select "Lost Item" or "Found Item"
3. Enter item details:
   - Name
   - Description
   - Date (with validation)
   - Location
   - Contact information

### Viewing Items
- View all lost items
- View all found items
- Items displayed in a clean table format with dates as DD-MM-YYYY

### Searching Items
- Search by item name
- Works for both lost and found items
- Case-insensitive search

### Deleting Items
- View items first to see their IDs
- Delete by entering the item ID
- Confirmation provided

## 🔧 Technical Details

- **Built with**: Java 17, Maven
- **Database**: SQLite (embedded)
- **Dependencies**: SQLite JDBC driver only
- **Platform**: Cross-platform (Windows, Linux, Mac)

## ❓ Troubleshooting

### "Java not found" error
- Install Java 17 or later
- Ensure Java is in your system PATH

### "No main manifest attribute" error
- The JAR file is corrupted - download again

### Database issues
- The application creates the database automatically
- If problems occur, delete the `LostFoundData` folder and restart

### Permission errors (Linux/Mac)
- Make sure you have write permissions in the current directory
- Or run from your home directory

## 📝 Data Persistence

- All data is saved automatically
- Database file persists between application runs
- Safe to close and reopen - your data will be there

## 🆘 Getting Help

If you encounter issues:
1. Check that Java 17+ is installed
2. Ensure you have write permissions in the current directory
3. The application will show the database location on startup

---

//initial branch commit