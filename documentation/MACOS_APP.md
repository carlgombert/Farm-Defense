# macOS Application Bundling Guide

## Overview

Farm Defense can be packaged as a standard macOS `.app` bundle for convenient distribution and launching from Finder or Launchpad. This guide explains the bundling process and distribution options.

## Quick Start

### Creating the App Bundle

```bash
# Make script executable (first time only)
chmod +x create-app-bundle.sh

# Create the app bundle
./create-app-bundle.sh

# Run the app from command line
open "Farm Defense.app"

# Or copy to Applications folder
cp -r "Farm Defense.app" /Applications/
```

### Custom App Name and Version

```bash
./create-app-bundle.sh "My Game" "2.0.0"
```

This creates an app bundle named `My Game.app` with version `2.0.0`.

## App Bundle Structure

The created `.app` bundle follows standard macOS conventions:

```
Farm Defense.app/
├── Contents/
│   ├── MacOS/
│   │   └── FarmDefense          # Executable launcher script
│   ├── Resources/
│   │   ├── Java/                # Compiled .class files
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── view/
│   │   │   └── util/
│   │   └── game/
│   │       └── resources/       # Game assets (textures, sounds, etc.)
│   ├── Info.plist              # macOS application metadata
│   └── PkgInfo                 # Package type information
```

## Components

### Info.plist

The `Info.plist` file contains application metadata recognized by macOS:

- **CFBundleExecutable**: Name of launcher script (`FarmDefense`)
- **CFBundleIdentifier**: Unique identifier (`com.carlgombert.farmdefense`)
- **CFBundleVersion**: Build version number
- **CFBundleShortVersionString**: User-visible version
- **NSRequiredJavaVersion**: Minimum Java version (`11.0`)
- **LSMinimumSystemVersion**: Minimum macOS version (`10.12` - Sierra)

### Launcher Script

The `MacOS/FarmDefense` script:
- Detects the Java installation
- Sets classpath to compiled classes
- Configures the working directory for resources
- Launches the game with appropriate Java settings
- Handles error conditions gracefully

### Resource Structure

The `Resources/` directory contains:
- **Java/**: Compiled bytecode (.class files)
- **game/resources/**: Game assets (sprites, sounds, maps, fonts)

## Distribution Options

### 1. Direct App Bundle

Simply distribute the `.app` folder:

```bash
# Users can run directly
open "Farm Defense.app"

# Or copy to Applications
cp -r "Farm Defense.app" ~/Applications/
```

**Advantages**:
- Simple distribution
- Users understand .app files
- Can be dropped into Applications folder

**Disadvantages**:
- Large file size (includes all compiled classes)
- Not professionally packaged

### 2. DMG Installer

Create a professional disk image for distribution:

```bash
# Create DMG from app bundle
hdiutil create -volname "Farm Defense" \
               -srcfolder "Farm Defense.app" \
               -ov -format UDZO \
               "FarmDefense.dmg"
```

The DMG file:
- Compresses the app bundle
- Provides standard installation experience
- Shows an "Applications" folder for drag-and-drop installation
- Can include license and readme files

**Create professional DMG with background image**:

```bash
# Create temporary directory with app and Applications symlink
mkdir -p dmg_temp
cp -r "Farm Defense.app" dmg_temp/
ln -s /Applications dmg_temp/Applications

# Create DMG with custom background
hdiutil create -volname "Farm Defense" \
               -srcfolder dmg_temp \
               -ov -format UDZO \
               "FarmDefense.dmg" \
               -imagekey zlib-level=9

# Clean up
rm -rf dmg_temp
```

### 3. Notarization (Optional - for App Store)

For distribution via App Store or to bypass Gatekeeper warnings:

```bash
# Sign the app
codesign -s - "Farm Defense.app"

# Check signature
codesign -v "Farm Defense.app"

# For App Store submission:
# Follow Apple's app notarization process
```

Note: Notarization requires an Apple Developer account and is primarily needed for App Store distribution.

## System Requirements

The app bundle specifies:
- **Minimum Java**: 11.0
- **Minimum macOS**: 10.12 (Sierra)

Users must have Java 11 or later installed. If Java is not found, the launcher script displays an error message.

## Launcher Script Details

### Environment Variables

The launcher automatically handles:
- **JAVA_HOME**: Uses $JAVA_HOME if set, otherwise searches PATH
- **Working Directory**: Changes to game resources directory for asset loading
- **Memory**: Allocates up to 1GB heap (`-Xmx1024m`)
- **Classpath**: Points to compiled .class files

### Customization

Edit `[app-name].app/Contents/MacOS/FarmDefense` to modify:

```bash
# Change memory allocation
"$JAVA_CMD" -Xmx2048m ... # Increase to 2GB

# Add debug output
"$JAVA_CMD" -verbose:class ... # Show class loading

# Specify Java version
"$JAVA_CMD" -version 17 ... # Force specific Java version
```

## Troubleshooting

### App Won't Launch

**Symptom**: Error when double-clicking app or running `open Farm Defense.app`

**Solutions**:
1. Check Java installation: `java -version`
2. Verify executable permissions: `chmod +x "Farm Defense.app/Contents/MacOS/FarmDefense"`
3. Check Info.plist syntax: `plutil -lint "Farm Defense.app/Contents/Info.plist"`
4. Run from terminal to see errors:
   ```bash
   "Farm Defense.app/Contents/MacOS/FarmDefense"
   ```

### "Java not found" Error

**Symptom**: Launcher script reports Java not found

**Solutions**:
1. Install Java: `brew install openjdk`
2. Set JAVA_HOME:
   ```bash
   export JAVA_HOME=/usr/libexec/java_home -v 11
   ```
3. Or install via Oracle: Visit oracle.com and download JDK

### Resources Not Found

**Symptom**: Game crashes saying resources/maps/map1.txt not found

**Solutions**:
1. Verify resources were copied: Check `Farm Defense.app/Contents/Resources/game/resources/`
2. Recreate bundle: `./create-app-bundle.sh`
3. Check game resources in source: Verify `farm_defense/resources/` exists

### DMG Creation Fails

**Symptom**: `hdiutil: create failed` error

**Solutions**:
1. Ensure target DMG doesn't exist: `rm -f FarmDefense.dmg`
2. Check available disk space
3. Verify app bundle path is correct
4. Use absolute paths in commands

## Advanced: Custom Icons

To add a custom application icon:

### 1. Create Icon File

```bash
# Using ImageMagick
# Convert image to 512x512 PNG
convert input.png -resize 512x512 icon.png

# Or use Figma, Photoshop, or online tools
```

### 2. Create .icns File (icon format)

```bash
# Using sips (built-in macOS tool)
sips -s format icns icon.png -o app_icon.icns

# Or using ImageMagick
convert icon.png -define icon:auto-resize=512,256,128,96,64,48,32,16 app_icon.icns
```

### 3. Add to App Bundle

```bash
mkdir -p "Farm Defense.app/Contents/Resources"
cp app_icon.icns "Farm Defense.app/Contents/Resources/AppIcon.icns"
```

### 4. Update Info.plist

Add to `Info.plist`:
```xml
<key>CFBundleIconFile</key>
<string>AppIcon</string>
```

## Performance Optimization

### Reducing Bundle Size

The app bundle size can be reduced by:

1. **Stripping debug information** (rebuild with debug disabled):
   ```bash
   javac -g:none -d bin ... # Omit debug info
   ```

2. **Compressing JAR** (if using JAR instead of classes):
   ```bash
   # Create JAR instead of copying .class files
   cd farm_defense/bin
   jar -cvf FarmDefense.jar controller/ model/ view/ util/
   ```

3. **Excluding unnecessary files**:
   - Remove old build artifacts
   - Exclude development documentation

### Runtime Performance

The launcher script optimizes performance:
- **-Xmx1024m**: Sufficient for game's memory needs
- **Bytecode compilation**: JIT compilation provides near-native speed
- **Resource caching**: Game loads resources once at startup

## Signing and Distribution

### Code Signing (for Mac App Store or security)

```bash
# Self-sign app
codesign -s - "Farm Defense.app"

# Verify signature
codesign -v "Farm Defense.app"

# Display signature details
codesign -d -v "Farm Defense.app"
```

### Creating Release Package

```bash
#!/bin/bash
APP_NAME="Farm Defense"
VERSION="1.0.0"

# Create app bundle
./create-app-bundle.sh

# Sign it
codesign -s - "$APP_NAME.app"

# Create DMG
hdiutil create -volname "$APP_NAME" \
               -srcfolder "$APP_NAME.app" \
               -ov -format UDZO \
               "$APP_NAME-$VERSION.dmg"

# Calculate checksum for verification
shasum -a 256 "$APP_NAME-$VERSION.dmg" > sha256sums.txt

echo "Release package: $APP_NAME-$VERSION.dmg"
echo "Checksum:"
cat sha256sums.txt
```

## Workflow Summary

Typical workflow for building and distributing:

```bash
# 1. Development and testing
./run.sh          # Run during development

# 2. Create app bundle
./create-app-bundle.sh

# 3. Test the app
open "Farm Defense.app"

# 4. Create DMG for distribution
hdiutil create -volname "Farm Defense" \
               -srcfolder "Farm Defense.app" \
               -ov -format UDZO \
               "FarmDefense.dmg"

# 5. Distribute FarmDefense.dmg to users
```

## Conclusion

The app bundling system provides a professional distribution method for macOS users. The automated script handles all complexities of app bundle creation, and optional DMG packaging enables standard macOS distribution conventions.

