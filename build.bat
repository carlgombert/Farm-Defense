@echo off

REM Build script for Farm Defense (Windows)
REM Usage: build.bat [clean]

setlocal enabledelayedexpansion

set "PROJECT_DIR=%~dp0"
set "FARM_DIR=%PROJECT_DIR%farm_defense"
set "SRC_DIR=%FARM_DIR%\src"
set "BIN_DIR=%FARM_DIR%\bin"

echo.
echo ========================================
echo Farm Defense Build Script
echo ========================================
echo Project: %PROJECT_DIR%
echo.

REM Check if javac is available
javac -version >nul 2>&1
if errorlevel 1 (
    echo Error: javac not found. Please install JDK 11 or later.
    exit /b 1
)

REM Get Java version
for /f "tokens=2" %%i in ('javac -version 2^>^&1') do set JAVA_VERSION=%%i
echo Java version: %JAVA_VERSION%
echo.

REM Clean if requested
if "%1"=="clean" (
    echo Cleaning build directory...
    if exist "%BIN_DIR%" (
        rmdir /s /q "%BIN_DIR%"
    )
    echo.
)

REM Create bin directory
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

echo Compiling Java source files...
echo.

REM Get all Java files and compile
setlocal enabledelayedexpansion
set "FILES="
for /r "%SRC_DIR%" %%F in (*.java) do (
    set "FILES=!FILES! "%%F""
)

javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" !FILES!

if errorlevel 1 (
    echo.
    echo Build FAILED with compilation errors.
    exit /b 1
) else (
    echo.
    echo Build SUCCESSFUL!
    echo Output: %BIN_DIR%
    exit /b 0
)
