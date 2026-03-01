# Farm Defense Development Guide

## Table of Contents

1. [Development Environment Setup](#development-environment-setup)
2. [Building the Project](#building-the-project)
3. [Running the Project](#running-the-project)
4. [Project Organization](#project-organization)
5. [Development Workflow](#development-workflow)
6. [Adding New Features](#adding-new-features)
7. [Debugging and Testing](#debugging-and-testing)
8. [Code Style and Conventions](#code-style-and-conventions)
9. [Common Tasks and Solutions](#common-tasks-and-solutions)
10. [Troubleshooting](#troubleshooting)

---

## Development Environment Setup

### System Requirements

- **Java Runtime**: Java Development Kit (JDK) 11 or later
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 1 GB RAM
- **IDE**: Eclipse, IntelliJ IDEA, NetBeans, or Visual Studio Code

### Installing Java Development Kit

#### macOS (using Homebrew)

```bash
brew install openjdk@17
```

Then verify installation:
```bash
java -version
javac -version
```

#### Windows (Manual Installation)

1. Download JDK 17 or later from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
2. Run installer and follow prompts
3. Add Java to system PATH:
   - Open Environment Variables
   - Add `C:\Program Files\Java\jdk-17\bin` to PATH
4. Verify in Command Prompt:
   ```
   java -version
   ```

#### Linux (Debian/Ubuntu)

```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
java -version
```

### Setting Up Eclipse IDE

1. Download Eclipse IDE for Java Developers from [eclipse.org](https://www.eclipse.org/downloads/)
2. Extract and launch Eclipse
3. Create workspace or use existing
4. Import project:
   - File → Import
   - General → Existing Projects into Workspace
   - Browse to `/Users/carlgombert/git/Farm-Defense/farm_defense`
   - Click Finish

### Setting Up IntelliJ IDEA

1. Download IntelliJ IDEA Community Edition from [jetbrains.com](https://www.jetbrains.com/idea/)
2. Install and launch
3. Open project:
   - File → Open
   - Browse to `/Users/carlgombert/git/Farm-Defense`
   - Click Open
4. IntelliJ automatically detects Java configuration

### Setting Up Visual Studio Code

1. Install Visual Studio Code
2. Install extensions:
   - Extension Pack for Java (Microsoft)
   - Code Runner (optional)
3. Open workspace:
   - File → Open Folder
   - Select `/Users/carlgombert/git/Farm-Defense`
4. VS Code auto-detects Java projects

---

## Building the Project

### Quick Start with Build Scripts

The project includes automated build scripts for both Unix-like and Windows systems. These are the recommended build methods.

**macOS/Linux**:

```bash
# Make scripts executable (first time only)
chmod +x build.sh run.sh

# Build the project
./build.sh

# Run the game (builds if needed)
./run.sh

# Build and run together
./build.sh && ./run.sh

# Clean and rebuild
./build.sh clean && ./run.sh
```

**Windows**:

```cmd
REM Build the project
build.bat

REM Run the game (builds if needed)
run.bat

REM Build and run together
build.bat && run.bat
```

### Using Make (Unix-like systems only)

If you have `make` installed, use the Makefile for cleaner syntax:

```bash
make build      # Build the project
make run        # Run the game (requires prior build)
make run-build  # Clean build and run
make clean      # Remove compiled classes
make help       # Show all available targets
```

### Using Eclipse

**Method 1: Automatic Build**

1. Project → Build Project (or Ctrl+B)
2. Eclipse compiles to `farm_defense/bin/` directory
3. Check Problems view for compilation errors

**Method 2: Clean Build**

1. Project → Clean
2. Select "Clean all projects"
3. Click Clean
4. Project automatically rebuilds

### Manual Command-Line Build

If scripts are unavailable, compile manually.

**macOS/Linux**:

```bash
javac -d farm_defense/bin -sourcepath farm_defense/src \
      $(find farm_defense/src -name "*.java")
```

Run application:
```bash
cd farm_defense
java -cp bin controller.Game
```

**Windows**:

```cmd
cd farm_defense
javac -d bin -sourcepath src (list all .java files)
java -cp bin controller.Game
```

### Build System Behavior

The build scripts and make targets provide automated compilation with error checking and status reporting. Scripts handle Java version validation, directory creation, compilation failures, and informative console output.

### Troubleshooting Build Issues

**Error: "cannot find symbol: class X"**
- Ensure all Java files in package are included in compilation
- Check import statements for typos
- Verify file exists in expected location

**Error: "incompatible types"**
- Review type mismatches in assignments and method calls
- Check casting and conversion operations
- Verify generic types are correctly specified

**Error: "Method not found"**
- Confirm method exists in correct class
- Verify method signature matches call site
- Check inheritance hierarchy for overridden methods

---

## Running the Project

### Quick Start

Use the provided scripts to build and run:

**macOS/Linux**:
```bash
./run.sh           # Build if needed, then run
./run.sh build     # Force clean build and run
```

**Windows**:
```cmd
run.bat            # Build if needed, then run
run.bat build      # Force clean build and run
```

**Using Make** (macOS/Linux):
```bash
make run-build     # Clean build and run
make run           # Run (requires prior build)
```

### Using IDE

#### Eclipse

1. Right-click on project in Package Explorer
2. Run As → Java Application
3. Select `Game` class
4. Click OK

#### IntelliJ IDEA

1. Locate `Game.java` in Project browser
2. Right-click → Run 'Game.main()'
3. Or: Control+Shift+R (macOS) / Shift+F10 (Windows/Linux)

#### Visual Studio Code

1. Open `Game.java`
2. Click "Run" CodeLens above main method
3. Or: Terminal → New Terminal and run script

### Manual Command-Line Execution

After building with scripts above, run directly:

**macOS/Linux**:
```bash
cd farm_defense
java -cp bin controller.Game
```

**Windows**:
```cmd
cd farm_defense
java -cp bin controller.Game
```

### Game Display

Upon successful launch, the game displays:
- **Resolution**: 768x576 pixels
- **Frame Rate**: 60 FPS
- **Initial State**: Main Menu screen

Controls visible in-game:
- Arrow keys: Movement
- Number keys (1-0): Inventory slots
- Space: Action/attack
- P: Pause
- ESC: Menu

### Running Tests

Currently, the project lacks a formal test suite. To add testing:

1. Create `tests/` directory parallel to `src/`
2. Add JUnit 4 or 5 library to classpath
3. Create test classes mirroring source structure
4. Implement unit tests for core logic

Example test structure:
```
farm_defense/
├── src/
├── tests/
│   ├── controller/
│   ├── model/
│   └── util/
└── bin/
```

Example test class (`tests/util/MathUtilTest.java`):
```java
import org.junit.Test;
import static org.junit.Assert.*;
import util.MathUtil;

public class MathUtilTest {
    @Test
    public void testRandomNumber() {
        int result = MathUtil.randomNumber(0, 100);
        assertTrue(result >= 0 && result <= 100);
    }
}
```

---

## Project Organization

### Source Code Packages

Each Java package corresponds to a functional area:

| Package | Purpose | Key Classes |
|---------|---------|------------|
| `controller` | Game orchestration and input | Game, KeyInput, Handler |
| `controller.objectHandling` | Entity management | Handler, ID |
| `model` | Game state and logic | GameObject, Player, Sound |
| `model.gameObjects` | Entity implementations | Zombie, Turret, NPC |
| `model.Inventory` | Item management | Inventory, InventoryItem |
| `model.items` | Item definitions | Item, ItemManager |
| `model.skin` | Character variants | Skin, SkinManager |
| `util` | Utilities | MathUtil, ImageUtil, SaveManager |
| `view` | Rendering and UI | Window, HUD, TileManager |
| `view.fullMenu` | Full-screen menus | MainMenu, PauseMenu, StoreMenu |
| `view.sideMenu` | Context menus | TradeMenu, TurretMenu |
| `view.map` | Terrain rendering | LightManager |
| `view.map.tile` | Tile management | TileManager, Tile |
| `view.map.building` | Structure rendering | BuildingManager, Building |
| `view.map.farming` | Crop rendering | FarmingManager, Crop |

### Resource Organization

Game assets are organized by type under `resources/`:

```
resources/
├── animations/      # Multi-frame animations
├── buildings/       # Structure sprites
├── farming/         # Crop images
├── fonts/          # Custom fonts
├── icon/           # UI icons
├── inventory/      # Item icons
├── maps/           # Map data files (.txt)
├── npc/            # NPC graphics
├── npcscripts/     # Dialogue data
├── player/         # Player sprite sheets
├── reference/      # Design documents
├── sound/          # Audio files
├── text/           # Text data files
├── tiles/          # Terrain sprites
├── trader/         # Trading UI graphics
└── zombie/         # Zombie sprites
```

### Resource Access Pattern

Resources load through utility classes:

```java
// Loading images
import util.ImageUtil;
BufferedImage sprite = ImageUtil.addImage(width, height, "resources/path/image.png");

// Loading text files
import util.TxtFileUtil;
String[] lines = TxtFileUtil.loadFile("resources/maps/map1.txt");

// Loading audio
import model.Sound;
Sound.rifleSound();  // Pre-registered sound effect
```

---

## Development Workflow

### Feature Branch Workflow (Git)

1. **Start new feature**:
   ```bash
   git checkout -b feature/new-feature-name
   git pull origin main
   ```

2. **Make changes**:
   ```bash
   # Edit files
   git add modified_files
   git commit -m "Implement new feature"
   ```

3. **Push changes**:
   ```bash
   git push origin feature/new-feature-name
   ```

4. **Create Pull Request**:
   - On GitHub, click "Compare & pull request"
   - Describe changes and rationale
   - Request code review
   - Merge to main after approval

### Code Review Checklist

Review code for:

- Architecture compliance (follows MVC separation)
- Code style consistency
- Proper error handling
- Performance implications
- Documentation completeness
- Test coverage

### Merge Conflicts

If conflicts arise:

1. Update local branch:
   ```bash
   git fetch origin
   git rebase origin/main
   ```

2. Resolve conflicts in affected files:
   ```
   <<<<<<< HEAD
   your changes
   =======
   their changes
   >>>>>>> branch-name
   ```

3. Choose correct version and remove markers

4. Complete merge:
   ```bash
   git add resolved_files
   git rebase --continue
   ```

---

## Adding New Features

### Adding a New Menu Screen

**Goal**: Add game settings menu

**Steps**:

1. **Create menu class** (`src/view/fullMenu/SettingsMenu.java`):
```java
package view.fullMenu;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public class SettingsMenu {
    
    private boolean visible = false;
    
    // Menu state
    private int selectedOption = 0;
    
    public SettingsMenu() {
        // Initialize menu components
    }
    
    public void render(Graphics g) {
        if (!visible) return;
        
        // Draw background
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, 768, 576);
        
        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Settings", 300, 100);
        
        // Draw options
        drawOption(g, "Sound Volume", 150, selectedOption == 0);
        drawOption(g, "Difficulty", 250, selectedOption == 1);
        drawOption(g, "Back", 350, selectedOption == 2);
    }
    
    private void drawOption(Graphics g, String text, int y, boolean selected) {
        g.setColor(selected ? Color.YELLOW : Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(text, 300, y);
    }
    
    public void handleKeyInput(int key) {
        // Process menu navigation
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
```

2. **Add static reference in Game.java**:
```java
public static SettingsMenu settingsMenu;

// In constructor:
settingsMenu = new SettingsMenu();
```

3. **Add GameState enum value**:
```java
enum GameState {
    // ... existing states
    Settings()
}
```

4. **Integrate into game loop** (Game.render()):
```java
if (gamestate == GameState.Settings) {
    settingsMenu.render(g);
}
```

5. **Add menu navigation** (KeyInput.java):
```java
if (Game.gamestate == GameState.Settings) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
        settingsMenu.selectPrevious();
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        settingsMenu.selectNext();
    }
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        settingsMenu.selectOption();
    }
}
```

### Adding a New Enemy Type

**Goal**: Add flying zombie variant

**Steps**:

1. **Create new class** (`src/model/gameObjects/FlyingZombie.java`):
```java
package model.gameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import controller.objectHandling.ID;

public class FlyingZombie extends Zombie {
    
    private int altitude = 100;  // Height above ground
    
    public FlyingZombie(int x, int y) {
        super(x, y, ID.Zombie);  // Reuses Zombie ID for grouping
        speed = super.speed * 1.5;  // Faster movement
    }
    
    @Override
    public void tick() {
        super.tick();  // Use base zombie logic
        
        // Override altitude-based rendering
        screenY -= altitude;
    }
    
    @Override
    public void render(Graphics g) {
        super.render(g);
        
        // Draw shadow on ground
        g.setColor(new java.awt.Color(0, 0, 0, 100));
        g.fillOval(screenX + 20, screenY + altitude + 40, 30, 10);
    }
}
```

2. **Register in ZombieSpawner.java**:
```java
// In spawn method, add random chance:
if (MathUtil.randomNumber(0, 100) < 10) {
    handler.addObject(new FlyingZombie(x, y));
} else {
    handler.addObject(new Zombie(x, y, ID.Zombie));
}
```

3. **Test and balance** difficulty scaling

### Adding a New Item Type

**Goal**: Add healing potion item

**Steps**:

1. **Add item icon** (48x48 PNG):
   - Save to `resources/inventory/potions/health_potion.png`

2. **Register in ItemManager.java**:
```java
public static void loadItems() {
    // In the loadItems method, add:
    Item healthPotion = new Item(60, "Health Potion", 
        ImageUtil.addImage(48, 48, "resources/inventory/potions/health_potion.png"));
    items.put(60, healthPotion);
}
```

3. **Add item handling in Inventory.java**:
```java
// Modify setSelected to handle new item type
if (inventory[selected].getID() == 60) {
    Game.player.setWeaponState(Game.player.stateEmpty());
}

// Add use logic in Player.java tick() method
if (equippedItem.getID() == 60 && actionPressed) {
    Game.player.heal(50);  // Heal player
    Game.inventory.removeItem(selected, 1);  // Consume potion
}
```

4. **Test item pickup and inventory interaction**

---

## Debugging and Testing

### Using IDE Debugger (Eclipse Example)

1. **Set breakpoint**:
   - Double-click line number to set breakpoint (blue dot appears)

2. **Debug as Java Application**:
   - Right-click project → Debug As → Java Application

3. **Execution control**:
   - Step Over (F6): Execute current line
   - Step Into (F5): Enter method calls
   - Step Out (F7): Exit current method
   - Resume (F8): Continue until next breakpoint

4. **Inspect variables**:
   - View Variables panel to see current values
   - Right-click variable → Inspect: Evaluate expression

### Manual Testing Checklist

Use this checklist when adding features:

- [ ] Feature compiles without errors
- [ ] Feature runs without crashes
- [ ] Feature functions as intended
- [ ] Edge cases handled (invalid input, null values)
- [ ] No performance degradation
- [ ] Visuals render correctly
- [ ] Audio (if applicable) plays correctly
- [ ] Integration with existing systems works
- [ ] Game states transition properly
- [ ] Save/load preserves feature state (if applicable)

### Performance Profiling

Monitor performance using simple frame timing:

```java
// In Game.java run() method:
long frameStartTime = System.currentTimeMillis();

// ... tick and render code ...

long frameTime = System.currentTimeMillis() - frameStartTime;
if (frameTime < FRAME_TIME_MS) {
    try {
        Thread.sleep(FRAME_TIME_MS - frameTime);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

Watch fps counter in HUD to identify performance issues.

### Logging

Add simple logging for debugging (no external dependencies):

```java
// Create Logger utility
package util;

public class Logger {
    public static final boolean DEBUG = true;
    
    public static void log(String message) {
        if (DEBUG) {
            System.out.println("[LOG] " + message);
        }
    }
    
    public static void error(String message, Exception e) {
        System.err.println("[ERROR] " + message);
        e.printStackTrace();
    }
}
```

Usage:
```java
Logger.log("Zombie spawned at " + x + ", " + y);
Logger.error("Failed to load image", exception);
```

---

## Code Style and Conventions

### Naming Conventions

```java
// Classes: PascalCase
public class PlayerCharacter { }
public class ZombieSpawner { }

// Methods and variables: camelCase
public void updatePosition() { }
private int zombieCount = 0;

// Constants: UPPER_SNAKE_CASE
public static final int TILE_SIZE = 48;
public static final float DEFAULT_SPEED = 2.5f;

// Package names: lowercase
package controller.objectHandling;
package model.gameObjects;

// Enum values: UPPER_CASE
enum GameState { RUNNING, PAUSED, MENU }
```

### Code Formatting

**Indentation**: 4 spaces (not tabs)

**Line length**: Maximum 120 characters

**Braces**: Opening on same line, closing on new line (Allman style)
```java
if (condition) {
    doSomething();
}
else {
    doOtherThing();
}
```

**Blank lines**: 
- One blank line between methods
- One blank line between logical sections in method

### Documentation Comments

```java
/**
 * Updates the player position based on current input state.
 * Checks collision with terrain and entities before confirming movement.
 * 
 * @param deltaTime elapsed time in milliseconds since last update
 * @return true if position changed, false otherwise
 */
public boolean updatePosition(long deltaTime) {
    // Implementation
}

/**
 * The Player class represents the user-controlled character in the game world.
 * It manages position, inventory, health, and weapon state.
 * 
 * @author Carl Gombert
 * @version 1.0
 */
public class Player extends GameObject {
    // Implementation
}
```

### Import Organization

```java
import java.awt.*;              // Java standard library
import java.util.*;

import javax.swing.*;           // Java extensions

import controller.*;            // Project packages
import model.*;
import view.*;
import util.*;
```

---

## Common Tasks and Solutions

### Task 1: Changing Game Window Size

Edit in `Game.java`:
```java
public static final int WIDTH = 16 * 48;    // Change first number
public static final int HEIGHT = 12 * 48;   // Change second number
```

Also update in `Window.java` to match.

### Task 2: Adjusting Game Speed/Difficulty

Edit constants in `Game.java`:
```java
public static final int SPAWN_RATE = 100;   // Zombies per night wave
public static final double DIFFICULTY_SCALE = 1.1;  // Difficulty multiplier
```

### Task 3: Adding New Weapon

1. Create weapon class: `Weapon.java` in `model/`
2. Add sprite to `resources/weapons/`
3. Register weapon in `ItemManager`
4. Add hit detection logic in `Projectile.java`
5. Test collision with enemies

### Task 4: Creating Custom Map

1. Edit `resources/maps/map1.txt` with tile values (0-15 for terrain)
2. Create corresponding building/farming maps in same directory
3. Validate in TileManager that dimensions match (40x40)
4. Test in-game for visual correctness and collisions

### Task 5: Implementing Save/Load

Complete the `SaveManager.java` class:

```java
package util;

import java.io.*;
import controller.Game;
import model.Inventory.Inventory;

public class SaveManager {
    
    private static final String SAVE_DIR = "farm_defense/saves/";
    
    public static void saveGame(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(SAVE_DIR + filename + ".sav"))) {
            
            // Save player state
            out.writeInt(Game.player.getWorldX());
            out.writeInt(Game.player.getWorldY());
            out.writeInt(Game.player.getHealth());
            out.writeInt(Game.player.getCoins());
            
            // Save inventory
            out.writeObject(Game.inventory.getInventory());
            
            // Save game time
            out.writeInt(Game.timeHours);
            out.writeInt(Game.timeMinutes);
            
        } catch (IOException e) {
            System.err.println("Failed to save game");
            e.printStackTrace();
        }
    }
    
    public static void loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(SAVE_DIR + filename + ".sav"))) {
            
            // Load player state
            int x = in.readInt();
            int y = in.readInt();
            int health = in.readInt();
            int coins = in.readInt();
            
            Game.player.setWorldX(x);
            Game.player.setWorldY(y);
            Game.player.setHealth(health);
            Game.player.setCoins(coins);
            
            // Load inventory and time...
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game");
            e.printStackTrace();
        }
    }
}
```

---

## Troubleshooting

### Common Issues and Solutions

**Issue: "Cannot find symbol: class Game"**

Solution: Ensure classpath includes `bin/` directory when running:
```bash
java -cp bin:. controller.Game
```

**Issue: Game window is blank or black**

Solutions:
1. Verify resource files exist in correct paths
2. Check TileManager initialization completes without exceptions
3. Ensure player sprite loads correctly
4. Review render() method for null pointer exceptions

**Issue: Game crashes on startup with NullPointerException**

Solution: Check initialization order in Game constructor:
- Handler must be created before adding objects
- Player must be created before HUD
- All managers must be initialized before game loop starts

**Issue: Zombies don't spawn or spawn rarely**

Solution: Check ZombieSpawner in Game.tick():
1. Verify nightCount is incrementing
2. Check spawn rate constants
3. Confirm Handler.addObject() succeeds
4. Check zombie limit isn't reached

**Issue: Collision detection not working**

Solutions:
1. Verify tile collision flags set correctly
2. Check getBounds() returns correct rectangles
3. Ensure collision checking runs before movement
4. Review Rectangle.intersects() logic

**Issue: Inventory items not showing**

Solutions:
1. Verify item IDs registered in ItemManager
2. Check image files exist and load correctly
3. Ensure HUD renderInventory() displays properly
4. Confirm inventory slots contain items

**Issue: Audio not playing**

Solutions:
1. Verify sound files exist in `resources/sound/`
2. Check Sound class initializes properly
3. Ensure Java sound system enabled on system
4. Test with different audio formats (WAV vs MP3)

**Issue: Game runs slowly or stutters**

Solutions:
1. Profile frame timing to identify bottleneck
2. Reduce zombie count for testing
3. Optimize collision detection (broad-phase first)
4. Cache images rather than loading per-frame
5. Consider object pooling for projectiles

### Getting Help

If issues persist:

1. Check GitHub Issues tab for known problems
2. Review recent commits for related changes
3. Compare with main branch: `git diff main`
4. Enable Logger debug output for diagnostics
5. Post detailed stack trace to issues

---

## Conclusion

This development guide provides comprehensive instructions for setting up, building, running, and extending Farm Defense. Follow the established patterns and conventions when contributing new features to maintain code consistency and architecture integrity. Refer to the Architecture document for detailed component descriptions and to the README for game overview.

