#!/bin/bash

set -e

# Build script for Farm Defense (macOS/Linux)
# Usage: ./build.sh [clean]

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FARM_DIR="$PROJECT_DIR/farm_defense"
SRC_DIR="$FARM_DIR/src"
BIN_DIR="$FARM_DIR/bin"
JAVAC="javac"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Farm Defense Build Script${NC}"
echo "Project: $PROJECT_DIR"
echo ""

# Check if Java is installed
if ! command -v $JAVAC &> /dev/null; then
    echo -e "${RED}Error: javac not found. Please install JDK 11 or later.${NC}"
    exit 1
fi

# Get Java version
JAVA_VERSION=$($JAVAC -version 2>&1 | grep -oP '(?<=javac )\d+' | head -1)
echo "Java version: $JAVA_VERSION"

if [ $JAVA_VERSION -lt 11 ]; then
    echo -e "${RED}Error: JDK 11 or later is required (found $JAVA_VERSION).${NC}"
    exit 1
fi

# Clean if requested
if [ "$1" = "clean" ]; then
    echo -e "${YELLOW}Cleaning build directory...${NC}"
    rm -rf "$BIN_DIR"
fi

# Create bin directory
mkdir -p "$BIN_DIR"

echo -e "${YELLOW}Compiling Java source files...${NC}"

# Compile all Java files
if $JAVAC -d "$BIN_DIR" \
         -sourcepath "$SRC_DIR" \
         $(find "$SRC_DIR" -name "*.java" -type f); then
    echo -e "${GREEN}Build successful!${NC}"
    echo "Output: $BIN_DIR"
    exit 0
else
    echo -e "${RED}Build failed with compilation errors.${NC}"
    exit 1
fi
