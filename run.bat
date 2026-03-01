@echo off

REM Run script for Farm Defense (Windows)
REM Usage: run.bat [build]

setlocal enabledelayedexpansion

set "PROJECT_DIR=%~dp0"
set "FARM_DIR=%PROJECT_DIR%farm_defense"
set "BIN_DIR=%FARM_DIR%\bin"

echo.
echo ========================================
echo Farm Defense Run Script
echo ========================================
echo.

REM Build first if requested or if bin directory doesn't exist
if "%1"=="build" goto BuildFirst
if not exist "%BIN_DIR%" goto BuildFirst
goto CheckBinary

:BuildFirst
echo Running build...
call "%PROJECT_DIR%build.bat"
if errorlevel 1 (
    echo Build failed. Aborting run.
    exit /b 1
)
echo.

:CheckBinary
REM Check if bin directory exists
if not exist "%BIN_DIR%" (
    echo Error: Compiled classes not found in %BIN_DIR%
    echo Please run: build.bat
    exit /b 1
)

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: java not found. Please install JDK 11 or later.
    exit /b 1
)

echo Starting Farm Defense...
echo.

REM Run the game
cd /d "%FARM_DIR%"
java -cp "%BIN_DIR%" controller.Game

set EXIT_CODE=!errorlevel!
echo.
if %EXIT_CODE% equ 0 (
    echo Game closed successfully.
) else (
    echo Game exited with code %EXIT_CODE%
)

exit /b %EXIT_CODE%
