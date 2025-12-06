# Lost & Found System

A portable Java application for managing lost and found items with a console-based interface.

## Features

- Report lost items
- Report found items  
- View all items (lost/found)
- Search items by name
- Delete items
- Persistent SQLite database
- Portable - runs anywhere with Java

## Quick Start

### Prerequisites
- **Java 17 or later** must be installed on your system

## Database Location

The application automatically creates and manages its database:
- **Location**: `./LostFoundData/lostfound.db` 


## How to Use

### Main Menu
```
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
- Select item you want to delete from the table 
- press Delete button to delete
- Confirmation provided

## Technical Details

- **Built with**: Java 17, Maven
- **Database**: SQLite (embedded)
- **Dependencies**: SQLite JDBC driver, Java JDK 24, JavaFX library 17.0.6
- **Platform**: Cross-platform (Windows, Linux, Mac)

Or run from your home directory

## Data Persistence

- All data is saved automatically
- Database file persists between application runs
- Safe to close and reopen and data will be there

---

**Enjoy using the Lost and Found System** 
