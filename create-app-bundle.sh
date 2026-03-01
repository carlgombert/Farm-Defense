#!/bin/bash

# macOS Application Bundle Creator for Farm Defense
# Creates a standard .app bundle that can be launched from Finder
# Usage: ./create-app-bundle.sh [app-name] [version]

set -e

# Configuration
APP_NAME="${1:-Farm Defense}"
APP_VERSION="${2:-1.0.0}"
BUNDLE_IDENTIFIER="com.carlgombert.farmdefense"

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FARM_DIR="$PROJECT_DIR/farm_defense"
SRC_DIR="$FARM_DIR/src"
BIN_DIR="$FARM_DIR/bin"
RESOURCES_DIR="$FARM_DIR/resources"
APP_BUNDLE="${APP_NAME}.app"
CONTENTS_DIR="$APP_BUNDLE/Contents"
MACOS_DIR="$CONTENTS_DIR/MacOS"
RESOURCES_APP_DIR="$CONTENTS_DIR/Resources"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}================================================${NC}"
echo -e "${BLUE}Farm Defense - macOS App Bundle Creator${NC}"
echo -e "${BLUE}================================================${NC}"
echo ""
echo -e "App Name: ${YELLOW}$APP_NAME${NC}"
echo -e "Version: ${YELLOW}$APP_VERSION${NC}"
echo -e "Bundle ID: ${YELLOW}$BUNDLE_IDENTIFIER${NC}"
echo ""

# Step 1: Build the project
echo -e "${YELLOW}Step 1: Building project...${NC}"
if [ ! -d "$BIN_DIR" ] || [ -z "$(find "$BIN_DIR" -name '*.class' 2>/dev/null)" ]; then
    echo "Compiling Java source files..."
    if ! bash "$PROJECT_DIR/build.sh"; then
        echo -e "${RED}Build failed!${NC}"
        exit 1
    fi
else
    echo "Using existing build"
fi
echo ""

# Step 2: Create app bundle structure
echo -e "${YELLOW}Step 2: Creating app bundle structure...${NC}"
rm -rf "$APP_BUNDLE"
mkdir -p "$MACOS_DIR"
mkdir -p "$RESOURCES_APP_DIR/Java"
mkdir -p "$RESOURCES_APP_DIR/game"
echo "Created: $APP_BUNDLE/"
echo ""

# Step 3: Copy compiled classes
echo -e "${YELLOW}Step 3: Copying compiled classes...${NC}"
cp -r "$BIN_DIR"/* "$RESOURCES_APP_DIR/Java/"
echo "Classes copied to: $RESOURCES_APP_DIR/Java/"
echo ""

# Step 4: Copy game resources
echo -e "${YELLOW}Step 4: Copying game resources...${NC}"
if [ -d "$RESOURCES_DIR" ]; then
    cp -r "$RESOURCES_DIR" "$RESOURCES_APP_DIR/game/resources"
    echo "Resources copied"
else
    echo -e "${YELLOW}Warning: resources directory not found${NC}"
fi
echo ""

# Step 5: Create launcher script
echo -e "${YELLOW}Step 5: Creating launcher script...${NC}"
cat > "$MACOS_DIR/FarmDefense" << 'LAUNCHER'
#!/bin/bash

# Launcher script for Farm Defense macOS application
# Gets the directory where the app bundle is located
APP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
RESOURCES_DIR="$APP_DIR/Contents/Resources"
JAVA_DIR="$RESOURCES_DIR/Java"
GAME_DIR="$RESOURCES_DIR/game"

# Find Java executable
if [ -n "$JAVA_HOME" ]; then
    JAVA_CMD="$JAVA_HOME/bin/java"
elif command -v java &> /dev/null; then
    JAVA_CMD="java"
else
    echo "Error: Java not found. Please install Java 11 or later."
    exit 1
fi

# Change to game directory so resources can be found
cd "$GAME_DIR"

# Launch with appropriate memory settings
"$JAVA_CMD" -Xmx1024m -cp "$JAVA_DIR" controller.Game

exit $?
LAUNCHER

chmod +x "$MACOS_DIR/FarmDefense"
echo "Launcher script created"
echo ""

# Step 6: Create Info.plist
echo -e "${YELLOW}Step 6: Creating Info.plist...${NC}"
cat > "$CONTENTS_DIR/Info.plist" << PLIST
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleDevelopmentRegion</key>
    <string>en</string>
    <key>CFBundleExecutable</key>
    <string>FarmDefense</string>
    <key>CFBundleName</key>
    <string>$APP_NAME</string>
    <key>CFBundleIdentifier</key>
    <string>$BUNDLE_IDENTIFIER</string>
    <key>CFBundleVersion</key>
    <string>$APP_VERSION</string>
    <key>CFBundleShortVersionString</key>
    <string>$APP_VERSION</string>
    <key>CFBundlePackageType</key>
    <string>APPL</string>
    <key>CFBundleSignature</key>
    <string>????</string>
    <key>NSHighResolutionCapable</key>
    <true/>
    <key>NSHumanReadableCopyright</key>
    <string>Copyright © 2024 Carl Gombert. All rights reserved.</string>
    <key>NSRequiredJavaVersion</key>
    <string>11.0</string>
    <key>LSMinimumSystemVersion</key>
    <string>10.12</string>
</dict>
</plist>
PLIST

echo "Info.plist created"
echo ""

# Step 7: Create PkgInfo file
echo -e "${YELLOW}Step 7: Creating PkgInfo...${NC}"
echo -n "APPL????" > "$CONTENTS_DIR/PkgInfo"
echo "PkgInfo created"
echo ""

# Step 8: Display results
echo -e "${GREEN}================================================${NC}"
echo -e "${GREEN}Success! App bundle created:${NC}"
echo -e "${GREEN}================================================${NC}"
echo ""
echo -e "Location: ${YELLOW}$APP_BUNDLE${NC}"
echo ""
echo "To run the application:"
echo -e "  ${YELLOW}open $APP_BUNDLE${NC}"
echo ""
echo "To add to Applications folder:"
echo -e "  ${YELLOW}cp -r $APP_BUNDLE /Applications/${NC}"
echo ""
echo "To create a .dmg installer:"
echo -e "  ${YELLOW}hdiutil create -volname \"$APP_NAME\" -srcfolder $APP_BUNDLE -ov -format UDZO \"$APP_NAME.dmg\"${NC}"
echo ""

# Step 9: Display bundle structure
echo -e "${BLUE}Bundle structure:${NC}"
echo ""
tree -L 3 "$APP_BUNDLE" 2>/dev/null || find "$APP_BUNDLE" -type f | head -20

echo ""
echo -e "${GREEN}Done!${NC}"
