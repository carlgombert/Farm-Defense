#!/bin/bash

# Run script for Farm Defense (macOS/Linux)
# Usage: ./run.sh [build]

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FARM_DIR="$PROJECT_DIR/farm_defense"
BIN_DIR="$FARM_DIR/bin"
JAVA="java"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Farm Defense Run Script${NC}"
echo ""

# Build first if requested or if bin directory doesn't exist
if [ "$1" = "build" ] || [ ! -d "$BIN_DIR" ]; then
    echo -e "${YELLOW}Running build...${NC}"
    if ! bash "$PROJECT_DIR/build.sh"; then
        echo -e "${RED}Build failed. Aborting run.${NC}"
        exit 1
    fi
    echo ""
fi

# Check if bin directory exists
if [ ! -d "$BIN_DIR" ]; then
    echo -e "${RED}Error: Compiled classes not found in $BIN_DIR${NC}"
    echo "Please run: ./build.sh"
    exit 1
fi

# Check if Java is installed
if ! command -v $JAVA &> /dev/null; then
    echo -e "${RED}Error: java not found. Please install JDK 11 or later.${NC}"
    exit 1
fi

echo -e "${YELLOW}Starting Farm Defense...${NC}"
echo ""

# Run the game
cd "$FARM_DIR"
$JAVA -cp "$BIN_DIR" controller.Game

EXIT_CODE=$?
echo ""
if [ $EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}Game closed successfully.${NC}"
else
    echo -e "${RED}Game exited with code $EXIT_CODE${NC}"
fi

exit $EXIT_CODE
