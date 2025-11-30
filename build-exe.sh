#!/bin/bash
echo "Building LostFoundApp Native Executable..."
echo

# Set error handling
set -e

echo "=== Step 1: Checking Prerequisites ==="
echo "Java version:"
java -version
echo

echo "JDK version:"
javac -version
echo

echo "Checking jpackage:"
jpackage --version || echo "jpackage not found, but we'll try anyway"
echo

echo "=== Step 2: Building JAR file ==="
if [ -f "mvnw" ]; then
    echo "Using Maven Wrapper..."
    ./mvnw clean package
else
    echo "Maven Wrapper not found, using system Maven..."
    mvn clean package
fi

echo "Checking if JAR was created..."
if [ ! -f "target/Lost_Found-1.0-SNAPSHOT.jar" ]; then
    echo "ERROR: JAR file not found! Building manually..."

    # Manual compilation as fallback
    mkdir -p target/classes
    find src/main/java -name "*.java" > sources.txt
    javac -d target/classes @sources.txt
    jar cf target/Lost_Found-1.0-SNAPSHOT.jar -C target/classes .

    if [ ! -f "target/Lost_Found-1.0-SNAPSHOT.jar" ]; then
        echo "ERROR: Manual JAR build also failed!"
        exit 1
    fi
    echo "Manual JAR build successful!"
fi

echo "=== Step 3: Creating Java Runtime ==="
jlink \
  --add-modules java.base,java.sql,java.desktop,java.logging,java.xml \
  --strip-debug \
  --no-man-pages \
  --no-header-files \
  --compress=2 \
  --output java-runtime

echo "=== Step 4: Creating Application with jpackage ==="

# Try different jpackage approaches
echo "Attempt 1: Creating app image..."
jpackage \
  --name "LostFoundApp" \
  --input target/ \
  --main-jar Lost_Found-1.0-SNAPSHOT.jar \
  --main-class app.ConsoleApp \
  --type app-image \
  --dest dist/ \
  --runtime-image java-runtime \
  --vendor "YourCompany" \
  --app-version "1.0.0" \
  --description "Lost and Found Item Management System" \
  --verbose

if [ $? -eq 0 ]; then
    echo "App image created successfully!"
else
    echo "App image failed, trying simple package..."

    # Simpler approach
    jpackage \
      --name "LostFoundApp" \
      --input target/ \
      --main-jar Lost_Found-1.0-SNAPSHOT.jar \
      --type app-image \
      --dest dist/ \
      --verbose
fi

echo
echo "=== BUILD COMPLETE ==="
echo

# Show what was created
if [ -d "dist/LostFoundApp" ]; then
    echo "Application created in: dist/LostFoundApp/"
    echo "Contents:"
    ls -la "dist/LostFoundApp/"
    echo
    echo "To run: ./dist/LostFoundApp/bin/LostFoundApp"
elif [ -f "dist/LostFoundApp.exe" ]; then
    echo "Windows EXE created: dist/LostFoundApp.exe"
else
    echo "Checking dist folder:"
    ls -la dist/ 2>/dev/null || echo "dist folder not created"
fi

echo
echo "If the application was created, test it on a machine without Java!"
echo