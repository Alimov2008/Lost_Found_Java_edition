# Building LostFoundApp Executable

## Prerequisites
- JDK 16 or later (with jpackage)
- Maven 3.6+

## Build Steps
1. Run `build-exe.bat` (Windows)
2. Find the EXE in `dist/LostFoundApp.exe`

## Output Files
- `dist/LostFoundApp.exe` - Standalone executable
- `java-runtime/` - Custom JRE (can be deleted after build)
- `target/` - Build artifacts (can be cleaned)

## Distribution
Copy only `LostFoundApp.exe` to users - no Java required!