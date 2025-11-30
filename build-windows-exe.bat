@echo off
chcp 65001 > nul
echo Building LostFoundApp Windows EXE...
echo.

echo Step 1: Building JAR file...
call mvnw.cmd clean package

if errorlevel 1 (
    echo Error: Failed to build JAR file
    echo Make sure Maven and Java are installed
    pause
    exit /b 1
)

echo Step 2: Creating custom Java runtime...
jlink ^
  --add-modules java.base,java.sql,java.desktop,java.logging ^
  --strip-debug ^
  --no-man-pages ^
  --no-header-files ^
  --compress=2 ^
  --output java-runtime

if errorlevel 1 (
    echo Error: Failed to create Java runtime
    echo Make sure you have JDK (not just JRE)
    pause
    exit /b 1
)

echo Step 3: Creating Windows EXE...
jpackage ^
  --name "LostFoundApp" ^
  --input target/ ^
  --main-jar Lost_Found-1.0-SNAPSHOT.jar ^
  --main-class app.ConsoleApp ^
  --type exe ^
  --dest dist/ ^
  --runtime-image java-runtime ^
  --vendor "YourCompany" ^
  --app-version "1.0.0" ^
  --description "Lost and Found Item Management System" ^
  --win-console ^
  --win-shortcut ^
  --win-menu ^
  --copyright "Copyright 2024" ^
  --icon app-icon.ico

if errorlevel 1 (
    echo Error: Failed to create EXE
    echo.
    echo Troubleshooting:
    echo - Make sure jpackage is available (JDK 14+)
    echo - Check Java version: java -version
    echo - Try running as Administrator
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.
echo Windows EXE created: dist\LostFoundApp.exe
echo.
echo File size:
for /f "tokens=3" %%i in ('dir /-c dist\LostFoundApp.exe ^| find "LostFoundApp.exe"') do echo %%i bytes
echo.
echo The EXE is completely standalone - no Java required!
echo You can distribute this single file to Windows users.
echo.
pause