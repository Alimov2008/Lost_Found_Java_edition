import random
import sqlite3
import time
from datetime import datetime


def generate_random_lostfound_data(num_entries=1000, batch_size=100, clear_tables=True):
    """
    Stress test the lostfound.db database by inserting random data.

    Args:
        num_entries: Total number of random entries to insert (per table)
        batch_size: Number of entries to insert in each batch transaction
        clear_tables: If True, clears both tables before inserting new data
    """

    # Sample data for random generation
    item_categories = {
        "Electronics": {
            "items": [
                "iPhone",
                "Samsung Phone",
                "Android Phone",
                "iPad",
                "Tablet",
                "Laptop",
                "MacBook",
                "ChromeBook",
                "Headphones",
                "Earbuds",
                "Smart Watch",
                "Digital Camera",
                "Power Bank",
                "Charger",
                "USB Cable",
                "Bluetooth Speaker",
                "Calculator",
                "Kindle",
            ],
            "models": [
                "Pro",
                "Air",
                "Galaxy",
                "Pixel",
                "X",
                "SE",
                "Ultra",
                "Plus",
                "Standard",
                "Lite",
                "Max",
                "Mini",
                "2023",
                "2024",
            ],
            "colors": [
                "Black",
                "White",
                "Silver",
                "Space Gray",
                "Midnight",
                "Starlight",
                "Blue",
                "Red",
                "Green",
                "Purple",
                "Gold",
                "Rose Gold",
                "Graphite",
            ],
            "brands": [
                "Apple",
                "Samsung",
                "Google",
                "Sony",
                "Bose",
                "JBL",
                "Dell",
                "HP",
                "Lenovo",
                "Microsoft",
                "Canon",
                "Nikon",
            ],
            "conditions": [
                "New",
                "Like new",
                "Good condition",
                "Some scratches",
                "Cracked screen",
                "Water damaged",
                "Works perfectly",
            ],
        },
        "Personal Items": {
            "items": [
                "Wallet",
                "Keys",
                "Glasses",
                "Sunglasses",
                "Watch",
                "Ring",
                "Necklace",
                "Bracelet",
                "Earrings",
                "Passport",
                "ID Card",
                "Credit Card",
                "Driver's License",
                "Student ID",
            ],
            "materials": [
                "Leather",
                "Metal",
                "Plastic",
                "Gold",
                "Silver",
                "Titanium",
                "Stainless Steel",
                "Fabric",
                "Wood",
                "Ceramic",
            ],
            "colors": [
                "Brown",
                "Black",
                "Blue",
                "Red",
                "Green",
                "Gold",
                "Silver",
                "Multi-color",
                "Transparent",
                "Patterned",
            ],
            "conditions": [
                "Brand new",
                "Well-maintained",
                "Worn",
                "Scratched",
                "Vintage",
                "Personalized",
            ],
        },
        "Bags & Accessories": {
            "items": [
                "Backpack",
                "Handbag",
                "Purse",
                "Wallet",
                "Lunchbox",
                "Water Bottle",
                "Umbrella",
                "Sunglasses Case",
                "Glasses Case",
            ],
            "brands": [
                "North Face",
                "Jansport",
                "Herschel",
                "Patagonia",
                "Lululemon",
                "Nike",
                "Adidas",
                "Hydro Flask",
                "Camelbak",
                "Swiss Army",
            ],
            "colors": [
                "Black",
                "Navy",
                "Gray",
                "Red",
                "Blue",
                "Green",
                "Patterned",
                "Camouflage",
                "Pink",
                "Yellow",
            ],
            "features": [
                "Water resistant",
                "Multiple compartments",
                "Padded laptop sleeve",
                "Insulated",
                "With strap",
                "Rolling wheels",
                "Leather trim",
            ],
        },
        "Clothing": {
            "items": [
                "Jacket",
                "Coat",
                "Hoodie",
                "Sweater",
                "Hat",
                "Scarf",
                "Gloves",
                "Shoes",
                "Sneakers",
                "Boots",
                "Raincoat",
                "Sunglasses",
            ],
            "sizes": ["XS", "S", "M", "L", "XL", "XXL", "Size 8", "Size 10", "Size 12"],
            "colors": [
                "Black",
                "Navy",
                "Gray",
                "White",
                "Red",
                "Blue",
                "Green",
                "Purple",
            ],
            "brands": [
                "Nike",
                "Adidas",
                "Patagonia",
                "Columbia",
                "The North Face",
                "Carhartt",
            ],
            "materials": ["Cotton", "Polyester", "Wool", "Leather", "Denim", "Fleece"],
        },
        "Books & Documents": {
            "items": [
                "Textbook",
                "Notebook",
                "Binder",
                "Folder",
                "Planner",
                "Journal",
                "Novel",
                "Library Book",
                "Research Paper",
                "Assignment",
            ],
            "subjects": [
                "Math",
                "Science",
                "History",
                "English",
                "Computer Science",
                "Biology",
                "Chemistry",
                "Physics",
                "Economics",
                "Psychology",
            ],
            "conditions": [
                "New",
                "Used",
                "Highlighted",
                "Annotated",
                "Damaged cover",
                "Missing pages",
                "Good condition",
            ],
        },
        "Miscellaneous": {
            "items": [
                "Toy",
                "Plushie",
                "Sports Equipment",
                "Gym Bag",
                "Yoga Mat",
                "Water Bottle",
                "Lunch Container",
                "Thermos",
                "Notebook",
                "Pen",
            ],
            "descriptions": [
                "Personalized with name",
                "With stickers",
                "Limited edition",
                "Vintage",
                "Collector's item",
                "Handmade",
                "Gift item",
            ],
        },
    }

    locations = [
        "Main Library (2nd floor)",
        "Science Library",
        "Cafeteria (north side)",
        "Engineering Building Room 201",
        "Student Center Lounge",
        "Gym Locker Room",
        "Parking Lot B",
        "Auditorium Seats",
        "Computer Lab 305",
        "Administration Office",
        "Dormitory Common Room",
        "Sports Field Bleachers",
        "Bus Stop #5",
        "Coffee Shop counter",
        "Bookstore entrance",
        "Elevator lobby",
    ]

    # Month as numbers 1-12
    months = list(range(1, 13))

    contacts = [
        "student.name@university.edu",
        "faculty.member@university.edu",
        "john.smith@email.com",
        "jane.doe@email.com",
        "admin@campus.edu",
        "security@university.edu",
        "lostfound@campus.edu",
        "(555) 123-4567",
        "(555) 987-6543",
        "roommate@dorm.edu",
    ]

    def generate_electronics_description(category, item):
        """Generate detailed description for electronics"""
        model = random.choice(category["models"])
        color = random.choice(category["colors"])
        brand = random.choice(category["brands"])
        condition = random.choice(category["conditions"])

        details = [
            f"{brand} {item} {model} in {color}",
            f"{color} {brand} {item} {model}",
            f"{brand} {model} model, color: {color}",
        ]

        extra_features = []
        if "Phone" in item:
            extra_features.extend(
                [
                    "with case",
                    "screen protector installed",
                    "specific wallpaper",
                    "in a specific case",
                ]
            )
        elif "Laptop" in item or "MacBook" in item:
            extra_features.extend(
                ["with stickers on lid", "specific decal", "with charger", "in sleeve"]
            )
        elif "Headphones" in item or "Earbuds" in item:
            extra_features.extend(
                ["wireless", "with charging case", "noise-cancelling"]
            )

        description = random.choice(details)
        if random.random() > 0.3 and extra_features:
            description += f", {random.choice(extra_features)}"

        description += f". Condition: {condition}."

        if random.random() > 0.7:
            identifiers = [
                "Serial number partially visible",
                "Personal engraving on back",
                "Unique scratch pattern",
                "Specific case with design",
                "Custom wallpaper visible",
            ]
            description += f" {random.choice(identifiers)}."

        return description

    def generate_personal_item_description(category, item):
        """Generate detailed description for personal items"""
        material = (
            random.choice(category["materials"]) if "materials" in category else ""
        )
        color = random.choice(category["colors"])
        condition = random.choice(category["conditions"])

        if "Wallet" in item:
            types = ["bifold", "trifold", "card holder", "money clip", "travel wallet"]
            wallet_type = random.choice(types)
            description = f"{color} {material} {wallet_type} wallet"

            contents = random.sample(
                [
                    "credit cards",
                    "ID cards",
                    "cash",
                    "photos",
                    "receipts",
                    "membership cards",
                ],
                random.randint(1, 3),
            )
            description += f" containing {', '.join(contents)}"

        elif "Keys" in item:
            key_types = [
                "car key with fob",
                "house keys",
                "office keys",
                "locker key",
                "keychain with",
                "mailbox key",
            ]
            key_type = random.choice(key_types)
            charms = ["no charm", "a small charm", "multiple charms", "a leather tag"]
            description = f"Set of {color} {key_type}"
            if random.random() > 0.5:
                description += f" with {random.choice(charms)}"

        elif "Glasses" in item or "Sunglasses" in item:
            frame_types = [
                "rectangular frames",
                "round frames",
                "aviator style",
                "cat-eye frames",
                "sport frames",
            ]
            lens_types = [
                "prescription lenses",
                "sunglass lenses",
                "transition lenses",
                "blue light blocking",
            ]
            description = (
                f"{color} {material} glasses with {random.choice(frame_types)}"
            )
            if random.random() > 0.3:
                description += f", {random.choice(lens_types)}"

        elif "Watch" in item or "Jewelry" in item:
            description = f"{color} {material} {item.lower()}"
            if random.random() > 0.4:
                engravings = [
                    "with engraving",
                    "personalized",
                    "with initials",
                    "dated",
                    "special inscription",
                ]
                description += f" {random.choice(engravings)}"

        else:
            description = f"{color} {material} {item.lower()}"

        description += f". Condition: {condition}."

        if random.random() > 0.6:
            identifiers = [
                "Personal markings inside",
                "Distinctive scratch pattern",
                "Worn in specific areas",
                "Custom modifications",
                "Unique wear pattern",
            ]
            description += f" {random.choice(identifiers)}."

        return description

    def generate_clothing_description(category, item):
        """Generate detailed description for clothing"""
        size = random.choice(category["sizes"])
        color = random.choice(category["colors"])
        brand = random.choice(category["brands"]) if "brands" in category else ""
        material = (
            random.choice(category["materials"]) if "materials" in category else ""
        )

        description = f"{color} {material} {item.lower()}"
        if brand:
            description = f"{brand} {description}"

        description += f", size {size}"

        if random.random() > 0.4:
            features = [
                "with hood",
                "zippered",
                "button-up",
                "with pockets",
                "with brand logo",
                "striped pattern",
                "solid color",
            ]
            description += f", {random.choice(features)}"

        condition = random.choice(
            ["New", "Lightly worn", "Well-worn", "Excellent condition"]
        )
        description += f". Condition: {condition}."

        if random.random() > 0.5:
            identifiers = [
                "Small stain on sleeve",
                "Torn pocket lining",
                "Missing button",
                "Personalized embroidery",
                "Distinctive patch",
            ]
            description += f" {random.choice(identifiers)}."

        return description

    def generate_book_description(category, item):
        """Generate detailed description for books and documents"""
        if "Textbook" in item:
            subject = random.choice(category["subjects"])
            editions = [
                "1st edition",
                "2nd edition",
                "3rd edition",
                "International edition",
            ]
            description = f"{subject} textbook, {random.choice(editions)}"

            if random.random() > 0.3:
                authors = [
                    "by multiple authors",
                    "author name on cover",
                    "specific professor's book",
                ]
                description += f" {random.choice(authors)}"

        elif "Notebook" in item or "Journal" in item:
            cover_types = [
                "spiral bound",
                "hardcover",
                "softcover",
                "composition style",
            ]
            description = f"{random.choice(cover_types)} notebook"

            if random.random() > 0.4:
                contents = [
                    "with class notes",
                    "with meeting notes",
                    "mostly blank",
                    "filled halfway",
                    "with sketches",
                ]
                description += f", {random.choice(contents)}"

        else:
            description = f"{item.lower()}"

        condition = random.choice(category["conditions"])
        description += f". Condition: {condition}."

        if random.random() > 0.5:
            identifiers = [
                "Name written inside cover",
                "Specific doodles on pages",
                "Bookmark left inside",
                "Highlighter marks throughout",
                "Coffee stain on cover",
            ]
            description += f" {random.choice(identifiers)}."

        return description

    def create_random_entry(table_type="lost"):
        """Generate a single random database entry with detailed description"""
        # Randomly select a category
        category_name = random.choice(list(item_categories.keys()))
        category = item_categories[category_name]

        # Select specific item from category
        item = random.choice(category["items"])

        # Generate detailed description based on category
        if category_name == "Electronics":
            description = generate_electronics_description(category, item)
            name = item  # Electronics already have specific names like "iPhone"

        elif category_name == "Personal Items":
            description = generate_personal_item_description(category, item)
            name = item

        elif category_name == "Clothing":
            description = generate_clothing_description(category, item)
            name = item

        elif category_name == "Books & Documents":
            description = generate_book_description(category, item)
            name = item

        elif category_name == "Bags & Accessories":
            brand = random.choice(category["brands"]) if "brands" in category else ""
            color = random.choice(category["colors"])
            features = (
                random.choice(category["features"]) if "features" in category else ""
            )
            name = f"{item}"
            description = f"{color} {brand} {item.lower()}"
            if features:
                description += f", {features}"
            description += (
                f". Condition: {random.choice(['New', 'Used', 'Good condition'])}."
            )

        else:  # Miscellaneous
            name = item
            desc = random.choice(category["descriptions"])
            description = f"{item}, {desc.lower()}. Condition varies."

        # Generate random date (within last 2 years)
        current_year = datetime.now().year
        year = random.randint(current_year - 2, current_year)
        month = random.choice(months)

        # Get days in month
        if month in [1, 3, 5, 7, 8, 10, 12]:
            max_day = 31
        elif month == 2:
            max_day = 28  # Simplified, ignoring leap years
        else:
            max_day = 30

        day = random.randint(1, max_day)

        # Add location-specific details to description
        location = random.choice(locations)
        if random.random() > 0.7:
            location_details = [
                f"Found on seat",
                f"Left on table",
                f"Found near entrance",
                f"Left in locker",
                f"Found on floor near",
            ]
            description += f" {random.choice(location_details)} {location}."

        return {
            "name": name,
            "description": description,
            "year": year,
            "month": month,
            "day": day,
            "location": location,
            "contact": random.choice(contacts),
            "category": category_name,
        }

    try:
        # Connect to database
        conn = sqlite3.connect("lostfound.db")
        cursor = conn.cursor()

        # CLEAR TABLES BEFORE INSERTION
        if clear_tables:
            print("Clearing existing data from tables...")
            cursor.execute("DELETE FROM lost_items;")
            cursor.execute("DELETE FROM found_items;")

            # Reset autoincrement counters (optional but good for clean state)
            cursor.execute(
                "DELETE FROM sqlite_sequence WHERE name IN ('lost_items', 'found_items');"
            )

            conn.commit()
            print("Tables cleared successfully!")
            print("-" * 50)

        print(
            f"Generating {num_entries * 2} total entries ({num_entries} per table)..."
        )

        # Insert into lost_items table
        print("\nInserting into 'lost_items' table...")
        start_time = time.time()

        for i in range(0, num_entries, batch_size):
            batch = []
            categories_used = {"lost": {}, "found": {}}

            for _ in range(min(batch_size, num_entries - i)):
                entry = create_random_entry("lost")
                batch.append(
                    (
                        entry["name"],
                        entry["description"],
                        entry["year"],
                        entry["month"],
                        entry["day"],
                        entry["location"],
                        entry["contact"],
                    )
                )

                # Track category usage
                if entry["category"] not in categories_used["lost"]:
                    categories_used["lost"][entry["category"]] = 0
                categories_used["lost"][entry["category"]] += 1

            cursor.executemany(
                """
                INSERT INTO lost_items (name, description, year, month, day, location, contact)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
                batch,
            )

            conn.commit()
            print(f"  Batch inserted: {i + len(batch)}/{num_entries}")

        lost_time = time.time() - start_time
        print(f"Lost items insertion complete: {lost_time:.2f} seconds")

        # Insert into found_items table
        print("\nInserting into 'found_items' table...")
        start_time = time.time()

        for i in range(0, num_entries, batch_size):
            batch = []
            for _ in range(min(batch_size, num_entries - i)):
                entry = create_random_entry("found")
                batch.append(
                    (
                        entry["name"],
                        entry["description"],
                        entry["year"],
                        entry["month"],
                        entry["day"],
                        entry["location"],
                        entry["contact"],
                    )
                )

                # Track category usage
                if entry["category"] not in categories_used["found"]:
                    categories_used["found"][entry["category"]] = 0
                categories_used["found"][entry["category"]] += 1

            cursor.executemany(
                """
                INSERT INTO found_items (name, description, year, month, day, location, contact)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
                batch,
            )

            conn.commit()
            print(f"  Batch inserted: {i + len(batch)}/{num_entries}")

        found_time = time.time() - start_time
        print(f"Found items insertion complete: {found_time:.2f} seconds")

        # Print category distribution
        print("\nCategory Distribution:")
        for table_type in ["lost", "found"]:
            print(f"\n  {table_type.capitalize()} items:")
            for category, count in categories_used[table_type].items():
                percentage = (count / num_entries) * 100
                print(f"    {category}: {count} items ({percentage:.1f}%)")

        # Verify insertion
        cursor.execute("SELECT COUNT(*) FROM lost_items;")
        lost_count = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM found_items;")
        found_count = cursor.fetchone()[0]

        print(f"\nVerification:")
        print(f"  Lost items count: {lost_count}")
        print(f"  Found items count: {found_count}")
        print(f"  Total time: {lost_time + found_time:.2f} seconds")

        # Show sample data with detailed descriptions
        print("\n=== Sample Lost Items (with detailed descriptions) ===")
        cursor.execute(
            "SELECT name, description, location FROM lost_items ORDER BY RANDOM() LIMIT 3;"
        )
        for name, description, location in cursor.fetchall():
            print(f"\nName: {name}")
            print(f"Location: {location}")
            print(f"Description: {description}")
            print("-" * 50)

        print("\n=== Sample Found Items (with detailed descriptions) ===")
        cursor.execute(
            "SELECT name, description, location FROM found_items ORDER BY RANDOM() LIMIT 3;"
        )
        for name, description, location in cursor.fetchall():
            print(f"\nName: {name}")
            print(f"Location: {location}")
            print(f"Description: {description}")
            print("-" * 50)

        # Show most common items
        print("\n=== Most Common Lost Items ===")
        cursor.execute("""
            SELECT name, COUNT(*) as count 
            FROM lost_items 
            GROUP BY name 
            ORDER BY count DESC 
            LIMIT 5
        """)
        for name, count in cursor.fetchall():
            print(f"  {name}: {count} times")

    except sqlite3.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"General error: {e}")
    finally:
        if conn:
            conn.close()
            print("\nDatabase connection closed.")


def clear_database_tables():
    """Clear all data from lost_items and found_items tables"""
    try:
        conn = sqlite3.connect("lostfound.db")
        cursor = conn.cursor()

        print("Clearing all data from database tables...")

        # Get current counts
        cursor.execute("SELECT COUNT(*) FROM lost_items;")
        lost_before = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM found_items;")
        found_before = cursor.fetchone()[0]

        print(
            f"Before clearing: Lost items = {lost_before}, Found items = {found_before}"
        )

        # Clear tables
        cursor.execute("DELETE FROM lost_items;")
        cursor.execute("DELETE FROM found_items;")

        # Reset autoincrement counters
        cursor.execute(
            "DELETE FROM sqlite_sequence WHERE name IN ('lost_items', 'found_items');"
        )

        conn.commit()

        # Verify clearing
        cursor.execute("SELECT COUNT(*) FROM lost_items;")
        lost_after = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM found_items;")
        found_after = cursor.fetchone()[0]

        print(f"After clearing: Lost items = {lost_after}, Found items = {found_after}")
        print("✓ Database cleared successfully!")

    except sqlite3.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"General error: {e}")
    finally:
        if conn:
            conn.close()


# Additional functions for more specific testing
def test_descriptive_queries():
    """Test queries that use the detailed descriptions"""
    conn = sqlite3.connect("lostfound.db")
    cursor = conn.cursor()

    print("Testing descriptive queries...")

    # First check if there's data
    cursor.execute("SELECT COUNT(*) FROM lost_items;")
    lost_count = cursor.fetchone()[0]

    if lost_count == 0:
        print("No data found in tables. Please run data generation first.")
        conn.close()
        return

    # Test 1: Find electronics by brand
    print("\n1. Finding Apple products:")
    cursor.execute("""
        SELECT name, description, location 
        FROM lost_items 
        WHERE description LIKE '%Apple%' 
        LIMIT 3
    """)
    results = cursor.fetchall()
    if results:
        for name, desc, location in results:
            print(f"  {name} at {location}")
            print(f"    Description excerpt: {desc[:80]}...")
    else:
        print("  No Apple products found")

    # Test 2: Find items by color
    print("\n2. Finding black items:")
    cursor.execute("""
        SELECT name, description 
        FROM found_items 
        WHERE description LIKE '%Black%' 
        LIMIT 3
    """)
    results = cursor.fetchall()
    if results:
        for name, desc in results:
            print(f"  {name}: {desc[:60]}...")
    else:
        print("  No black items found")

    # Test 3: Find textbooks
    print("\n3. Finding textbooks:")
    cursor.execute("""
        SELECT name, description, location 
        FROM lost_items 
        WHERE description LIKE '%textbook%' 
        LIMIT 3
    """)
    results = cursor.fetchall()
    if results:
        for name, desc, location in results:
            print(f"  {name} at {location}")
    else:
        print("  No textbooks found")

    # Test 4: Search for specific conditions
    print("\n4. Items in 'new' condition:")
    cursor.execute("""
        SELECT COUNT(*) 
        FROM lost_items 
        WHERE description LIKE '%Condition: New%' 
           OR description LIKE '%Brand new%'
    """)
    count = cursor.fetchone()[0]
    print(f"  Found {count} items in new condition")

    # Test 5: Complex search for electronics with specific features
    print("\n5. Phones with cases:")
    cursor.execute("""
        SELECT name, description 
        FROM lost_items 
        WHERE (description LIKE '%iPhone%' OR description LIKE '%Phone%')
          AND description LIKE '%with case%'
        LIMIT 3
    """)
    results = cursor.fetchall()
    if results:
        for name, desc in results:
            print(f"  {name}: {desc[:70]}...")
    else:
        print("  No phones with cases found")

    conn.close()


# Quick test function with clearing option
def quick_descriptive_test(num_entries=200, clear_first=True):
    """Quick test with descriptive data"""
    conn = sqlite3.connect("lostfound.db")
    cursor = conn.cursor()

    # Clear tables if requested
    if clear_first:
        print("Clearing tables before quick test...")
        cursor.execute("DELETE FROM lost_items;")
        cursor.execute("DELETE FROM found_items;")
        cursor.execute(
            "DELETE FROM sqlite_sequence WHERE name IN ('lost_items', 'found_items');"
        )
        conn.commit()

    print(f"Generating {num_entries} descriptive entries...")

    # Sample electronics description generator
    brands = ["Apple", "Samsung", "Google", "Sony"]
    models = ["Pro", "Air", "Galaxy S23", "Pixel 8", "iPhone 15"]
    colors = ["Black", "Silver", "Blue", "Midnight", "Starlight"]

    for i in range(num_entries):
        brand = random.choice(brands)
        model = random.choice(models)
        color = random.choice(colors)
        condition = random.choice(
            ["New", "Like new", "Good condition", "Some scratches"]
        )

        description = f"{brand} {model} in {color}. Condition: {condition}. "

        # Add random details
        details = [
            f"Case: {random.choice(['black silicone', 'clear', 'leather', 'no case'])}",
            f"Screen protector: {random.choice(['installed', 'none', 'tempered glass'])}",
            f"Accessories: {random.choice(['with charger', 'no accessories', 'with cables'])}",
            f"Personal: {random.choice(['engraved initials', 'custom wallpaper', 'distinctive case'])}",
        ]
        description += random.choice(details)

        cursor.execute(
            """
            INSERT INTO lost_items (name, description, year, month, day, location, contact)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """,
            (
                f"{brand} {model.split()[0]}",
                description,
                2024,
                random.randint(1, 12),
                random.randint(1, 28),
                random.choice(["Library", "Cafeteria", "Classroom"]),
                f"test{i}@email.com",
            ),
        )

    conn.commit()

    # Show some examples
    print("\nSample generated descriptions:")
    cursor.execute("SELECT description FROM lost_items LIMIT 3")
    for i, (desc,) in enumerate(cursor.fetchall(), 1):
        print(f"\nExample {i}:")
        print(f"  {desc}")

    cursor.execute("SELECT COUNT(*) FROM lost_items")
    print(f"\nTotal entries: {cursor.fetchone()[0]}")

    conn.close()


# Standalone function to just clear the database
def clear_only():
    """Only clear the database without inserting new data"""
    clear_database_tables()


# Usage examples
if __name__ == "__main__":
    print("=== Descriptive Data Stress Test with Auto-Clearing ===")
    print("Options:")
    print("1. Clear tables and generate new data")
    print("2. Just clear tables (no new data)")
    print("3. Generate new data without clearing")

    choice = input("\nEnter choice (1, 2, or 3): ").strip()

    if choice == "1":
        # Clear and generate new data
        print("\n" + "=" * 60)
        clear_database_tables()
        print("\n" + "=" * 60)
        print("Generating items with detailed descriptions including:")
        print("- Electronics: Brands, models, colors, conditions")
        print("- Personal items: Materials, contents, conditions")
        print("- Clothing: Sizes, brands, materials, features")
        print("- Books: Subjects, editions, conditions")
        print("- Bags: Brands, colors, features\n")

        # Generate 2000 entries per table
        generate_random_lostfound_data(
            num_entries=2000, batch_size=100, clear_tables=False
        )

        # Test the descriptive queries
        test_descriptive_queries()

    elif choice == "2":
        # Just clear the database
        clear_only()

    elif choice == "3":
        # Generate new data without clearing (will append to existing)
        print("\n" + "=" * 60)
        print("WARNING: This will append new data to existing tables!")
        confirm = input("Continue? (y/n): ").strip().lower()

        if confirm == "y":
            print("Generating items with detailed descriptions...")
            generate_random_lostfound_data(
                num_entries=1000, batch_size=100, clear_tables=False
            )
            test_descriptive_queries()
        else:
            print("Operation cancelled.")

    else:
        # Default: Clear and generate 1000 entries each
        print(
            "\nRunning default configuration (clear and generate 1000 entries each)..."
        )
        clear_database_tables()
        print("\n" + "=" * 60)
        generate_random_lostfound_data(
            num_entries=1000, batch_size=100, clear_tables=False
        )
        test_descriptive_queries()
