# Farm Defense Build System

## Overview

Farm Defense uses a multi-method build system to accommodate various development environments and workflows. The project provides automated scripts for rapid development, IDE integration for interactive development, and a Makefile for Unix-like systems.

## Build Methods

### 1. Automated Scripts (Recommended)

The project includes platform-specific build and run scripts that handle compilation, error checking, and Java version validation.

#### macOS/Linux

**Initial Setup**:
```bash
chmod +x build.sh run.sh
```

**Build only**:
```bash
./build.sh
```

**Build and run**:
```bash
./run.sh
```

**Clean rebuild**:
```bash
./build.sh clean
./run.sh
```

**Features**:
- Automatic Java version checking (requires JDK 11+)
- Colored output for success/failure
- Recursive Java file discovery
- Compilation error reporting
- Directory creation and cleanup

#### Windows

**Build only**:
```cmd
build.bat
```

**Build and run**:
```cmd
run.bat
```

**Clean rebuild**:
```cmd
build.bat && run.bat
```

**Features**:
- Automatic Java version checking
- Error codes for build failures
- Recursive Java file discovery
- Integration with Windows batch environment

### 2. Makefile (Unix-like systems)

The Makefile provides standard Make targets for developers familiar with Unix development tools.

**Available targets**:
```bash
make build      # Compile source code
make clean      # Remove compiled classes (bin/ directory)
make run        # Execute game (requires prior build)
make run-build  # Clean, build, and run the game
make help       # Display available targets
```

**Examples**:
```bash
# Development workflow
make clean
make build
make run

# Single command build and run
make run-build

# Just run (assumes already built)
make run
```

**Features**:
- Colored output for visibility
- Error handling and reporting
- Phony targets to support proper rebuilding
- Concurrent build capability

### 3. IDE Integration

#### Eclipse

**Method 1: Quick Build and Run**
1. Right-click project → Build Project
2. Right-click project → Run As → Java Application
3. Select `Game` from dialog

**Method 2: Using Built-in Build**
1. Project → Build Project (or Ctrl+B)
2. Project → Clean (for clean rebuild)
3. Run via green play button

**Advantages**:
- Real-time error highlighting
- Incremental compilation
- Visual project structure
- Integrated debugging

#### IntelliJ IDEA

**Quick Run**:
1. Open [farm_defense/src/controller/Game.java](farm_defense/src/controller/Game.java)
2. Click green play icon next to `public static void main(String[] args)`
3. Or: Ctrl+Shift+R (macOS) or Shift+F10 (Windows/Linux)

**Building**:
1. Build → Build Project (Ctrl+F9)
2. Build → Clean Project (clears previous build)
3. Build → Rebuild Project (clean then build)

**Advantages**:
- Advanced code inspection
- Refactoring tools
- Performance profiling
- Built-in version control integration

#### Visual Studio Code

**Build using tasks**:
1. Terminal → Run Build Task (Ctrl+Shift+B)
2. Select "build" from dropdown

**Run using scripts**:
1. Terminal → New Terminal
2. Execute: `./run.sh` (macOS/Linux) or `run.bat` (Windows)

**Advantages**:
- Lightweight editor
- Extensive extension ecosystem
- Customizable build tasks
- Git integration

### 4. Manual Command-Line

For environments without scripts or Make:

**macOS/Linux**:
```bash
# Navigate to farm_defense directory
cd farm_defense

# Create bin directory
mkdir -p bin

# Compile all Java files
javac -d bin -sourcepath src $(find src -name "*.java")

# Run the game
java -cp bin controller.Game
```

**Windows (Command Prompt)**:
```cmd
cd farm_defense
mkdir bin
javac -d bin -sourcepath src (list all .java files recursively)
java -cp bin controller.Game
```

**Windows (PowerShell)**:
```powershell
cd farm_defense
javac -d bin -sourcepath src $(Get-ChildItem -Recurse -Filter "*.java" | ForEach-Object { """$($_.FullName)""" })
java -cp bin controller.Game
```

## Build Configuration

### Compiler Options

The build system uses standard javac options:

```
-d bin              # Output directory for .class files
-sourcepath src     # Source path for dependency resolution
```

No special compiler flags are required. The build targets Java 11+ with standard platform features (AWT, Swing, Sound API).

### Directory Structure After Build

```
farm_defense/
├── src/            # Original source files (unchanged)
│   ├── controller/
│   ├── model/
│   ├── view/
│   └── util/
├── bin/            # Compiled bytecode (regenerated each build)
│   ├── controller/
│   ├── model/
│   ├── view/
│   └── util/
├── resources/      # Game assets (unchanged)
└── saves/          # Save files (generated at runtime)
```

## Build Troubleshooting

### Common Issues and Solutions

**"javac: command not found"**
- Cause: Java Development Kit not installed
- Solution: Install JDK 11 or later
- Verify: Run `java -version` and `javac -version`

**"cannot find symbol: class X"**
- Cause: Missing import or classpath issue
- Solution: 
  - Check import statements for typos
  - Verify sourcepath includes all source directories
  - For IDE: Clean and rebuild project

**"incompatible types" compilation error**
- Cause: Type mismatch in assignments or method calls
- Solution:
  - Verify casting is correct
  - Check method return types
  - Ensure generic types match

**Build succeeds but game won't start**
- Cause: Missing resources or incorrect classpath
- Solution:
  - Verify `farm_defense/resources/` directory exists
  - Check file permissions on bin/ directory
  - Run from farm_defense/ directory for game to find resources

**"Out of memory" during compilation**
- Cause: Large project with insufficient heap
- Solution:
  - Increase JVM heap: `javac -J-Xmx512m ...`
  - Use IDE with configurable memory settings

**Incremental build doesn't detect changes**
- Cause: Editor didn't save file or IDE needs refresh
- Solution:
  - Save file (Ctrl+S)
  - Clean and rebuild project
  - Restart IDE if issue persists

## Performance Notes

### Compilation Speed

- **First build**: ~5-10 seconds (all files compiled)
- **Incremental**: <1 second (individual files)
- **Clean build**: ~5-10 seconds (full recompilation)

### Runtime Performance

- **Startup**: ~2-3 seconds (class loading, resource initialization)
- **Frame rate**: 60 FPS target (may vary by system)
- **Memory usage**: ~150-200 MB steady state

## CI/CD Integration

For automated builds in CI/CD pipelines:

```bash
#!/bin/bash
# Example CI build script

cd farm_defense

# Compile
javac -d bin -sourcepath src $(find src -name "*.java")
if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi

# Optional: Run tests
# java -cp bin:lib/junit.jar org.junit.runner.JUnitCore...

echo "Build successful"
exit 0
```

## Development Workflow

### Quick Development Loop

1. **Make changes** to source files
2. **Build**: `./build.sh` or `make build` or IDE build
3. **Run**: `./run.sh` or `make run` or IDE run
4. **Test** in-game
5. **Debug** if needed (see debugging tools)
6. **Commit** changes to version control

### Clean Development Setup

For a fresh environment:

```bash
# Clean old build
./build.sh clean

# Build from scratch
./build.sh

# Run the game
./run.sh
```

### Adding New Source Files

No special build configuration required. New `.java` files are automatically discovered and compiled by:
- All build scripts (use `find` to locate files)
- Makefile (uses recursive find)
- IDEs (auto-detect and compile)

Place new files in appropriate package directories under `src/` and they'll be included in next build.

## Advanced Build Customization

### Environment Variables

Set before building for custom behavior:

```bash
# Example: Use specific Java compiler
export JAVAC=/path/to/javac
./build.sh

# Example: Custom output directory
# (requires modifying scripts)
export BIN_DIR=custom_output
```

### macOS Application Bundling

Create a standard macOS `.app` bundle for convenient distribution:

```bash
# Create app bundle
./create-app-bundle.sh

# Run the app
open "Farm Defense.app"

# Create DMG installer
hdiutil create -volname "Farm Defense" \
               -srcfolder "Farm Defense.app" \
               -ov -format UDZO \
               "FarmDefense.dmg"
```

See [MACOS_APP.md](MACOS_APP.md) for complete app bundling documentation, including:
- Professional DMG creation
- Custom icons
- Code signing
- Distribution options
- Troubleshooting

### Modifying Build Scripts

Edit `build.sh`, `build.bat`, or `Makefile` to:
- Change output directory
- Add compiler flags
- Include additional source paths
- Modify resource handling

### Creating Custom Build Targets

Add to Makefile:
```make
test:
	@echo "Running tests..."
	java -cp bin:lib/junit.jar org.junit.runner.JUnitCore ...

debug:
	@echo "Running with debug output..."
	java -cp bin -Ddebug=true controller.Game
```

Then run: `make test` or `make debug`

## Conclusion

The Farm Defense build system supports multiple development workflows:
- **Scripts**: Fastest for quick builds and runs
- **Make**: Familiar Unix development experience
- **IDE**: Best for interactive development and debugging
- **Manual**: Explicit control for unusual environments

Choose the method that best fits your development process and environment.

