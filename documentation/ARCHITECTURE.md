open "Farm Defense.app"# Farm Defense Architecture and Design Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture Design](#architecture-design)
3. [Core Systems](#core-systems)
4. [Component Details](#component-details)
5. [Data Flow](#data-flow)
6. [Design Patterns](#design-patterns)
7. [Directory Structure](#directory-structure)

---

## Project Overview

### Project Scope

The game combines two primary mechanics:

- **Farming System**: Players cultivate crops on farmland, manage soil conditions, and harvest produce for resources.
- **Defense System**: Players construct defensive structures (turrets), manage ammunition and resources, and defend their farm against waves of zombies during nighttime periods.

The game operates on a day-night cycle with escalating difficulty. During the day, players farm and prepare defenses. During the night, zombie waves attack with increasing intensity and frequency.

---

## Architecture Design

### Three-Tier Architecture Model

The codebase is organized into three distinct logical tiers following the Model-View-Controller (MVC) architectural pattern:

#### 1. Controller Tier (`controller/`)

The Controller tier manages game state transitions, input handling, and orchestration of core game systems. This layer bridges user input and world state changes.

**Key Responsibilities:**
- Game loop execution and timing
- Event handling (keyboard input, game state changes)
- Coordination between model and view systems
- Time management and day-night cycle control

**Core Classes:**
- `Game.java`: Central game manager and runtime orchestrator
- `KeyInput.java`: Keyboard event handler
- `Handler.java`: Dynamic object management system

#### 2. Model Tier (`model/`)

The Model tier contains the game world's state and logic, independent of rendering. It includes all game entities, inventory systems, and rules governing game mechanics.

**Key Responsibilities:**
- Entity state representation
- Game logic and mechanics implementation
- Physics and collision handling
- Item and inventory management
- Sound effect management

**Core Classes:**
- `GameObject.java`: Abstract base class for all dynamic entities
- `Player.java`: Player character representing the user
- `gameObjects/`: Specific entity implementations
- `Inventory/`: Inventory and item management
- `items/`: Item definitions and managers
- `Sound.java`: Audio playback coordination

#### 3. View Tier (`view/`)

The View tier handles all rendering operations, including the heads-up display, menus, and map rendering. It translates model state into visual representation on screen.

**Key Responsibilities:**
- Graphics rendering
- User interface presentation
- Map tile rendering
- Lighting calculations
- Menu display and navigation

**Core Classes:**
- `Window.java`: Application window setup
- `HUD.java`: In-game heads-up display
- `map/`: Terrain and object rendering systems
- `fullMenu/`: Full-screen menus (main menu, pause, etc.)
- `sideMenu/`: Context-sensitive side panels
- `GraphicBuilder.java`: Graphics resource management

### Dependency Flow

```
Controller (orchestration) -> Model (state/logic) -> View (rendering)
                         ↓
                    KeyInput/Game events
```

The Controller layer depends on both Model and View layers. The Model layer is independent of View (though it maintains references for convenience). The View layer may read Model state for rendering but does not modify it.

---

## Core Systems

### 1. Game Loop System

**Location**: `controller/Game.java`

**Architecture**:
The Game class extends Canvas and implements Runnable, establishing the game loop architecture:

```
Initialization Phase:
├─ Create Handler (object manager)
├─ Initialize Player
├─ Initialize all Managers (TileManager, BuildingManager, FarmingManager)
├─ Create HUD
├─ Initialize Inventory
└─ Set GameState to MainMenu

Runtime Loop (60 FPS target):
├─ Input Processing (via KeyInput)
├─ Update Phase (tick())
│  ├─ Handler.tick() - updates all entities
│  ├─ Player update and collision detection
│  ├─ Day-night cycle updates
│  ├─ Spawner updates
│  └─ Manager updates
├─ Render Phase (render())
│  ├─ Clear buffer
│  ├─ TileManager rendering
│  ├─ Handler rendering (all objects)
│  ├─ HUD rendering
│  └─ Menu rendering (if active)
└─ Display (BufferStrategy)
```

**Key Constants**:
- Display Resolution: 768x576 pixels (16x12 tiles at 48px each)
- Target FPS: 60 frames per second
- Map Size: 40x40 tiles
- Tile Size: 48x48 pixels

**Game States**:
```java
enum GameState {
    Paused,      // Game paused, waiting for resume
    Running,     // Active gameplay
    MainMenu,    // Title/main menu screen
    Dead,        // Player death screen
    Bankrupt,    // Economic failure screen
    Store        // In-game shop interface
}
```

### 2. Object Management System

**Location**: `controller/objectHandling/Handler.java`

**Architecture**:
The Handler class maintains a LinkedList of all dynamic GameObject instances and provides centralized update and render operations.

**Responsibilities**:
- Add/remove game objects dynamically
- Invoke tick() on all objects each frame
- Invoke render() on all objects each frame
- Track statistics (zombies killed counter)
- Ensure player renders last (z-order requirement)

**Data Structure**:
```
Handler
├─ LinkedList<GameObject> objects
│  ├─ Player (always at index 0)
│  ├─ Zombies (variable count)
│  ├─ Turrets (variable count)
│  ├─ NPCs (fixed count)
│  ├─ Projectiles (variable count)
│  └─ Spawners (fixed count)
└─ int zombiesKilled (statistics)
```

**Object Identification**:
Objects are categorized by ID enum for type checking and behavior routing:
```java
enum ID {
    Player,
    Zombie,
    Turret,
    Projectile,
    ZombieSpawner,
    NPC
}
```

### 3. Entity System

**Location**: `model/GameObject.java` and `model/gameObjects/`

**Base Class Architecture**:
All dynamic game entities inherit from GameObject, establishing common interface and state.

**Common State**:
```
Position Coordinates:
├─ World Coordinates (worldX, worldY)
│  └─ Absolute position within game world
├─ Screen Coordinates (screenX, screenY)
│  └─ Relative position on display, camera-relative
└─ Direction (0-3)
   ├─ 0: Front/South
   ├─ 1: Back/North
   ├─ 2: Left/West
   └─ 3: Right/East

Velocity:
├─ speedX, speedY (integer)
└─ Used for physics and collision prediction
```

**Common Interface**:
```java
abstract class GameObject {
    abstract void tick();      // Update logic, called once per frame
    abstract void render(Graphics g);  // Render representation
    abstract Rectangle getBounds();    // Collision bounds
}
```

**Key Entities**:

- **Player**: Controlled character with health, ammunition, coins, and inventory
- **Zombie**: AI enemy that seeks crops or player, deals damage on contact
- **Turret**: Defensive structure that automatically targets and fires at zombies
- **Projectile**: Ammunition fired by player weapons or turrets
- **NPC**: Non-player character that provides narrative and trading
- **ZombieSpawner**: System that generates zombie waves during night

### 4. Inventory and Item System

**Location**: `model/Inventory/` and `model/items/`

**Architecture**:
The Inventory system manages the player's items using a hybrid array-hashmap structure:

**Data Structure**:
```
Inventory
├─ InventoryItem[] inventory (size 10)
│  └─ Fixed slots for quick access
├─ HashMap<Integer, InventoryItem> items
│  └─ Efficient ID-based lookup
├─ int selected (current slot)
└─ int seedCropCount (resource tracking)
```

**Item Categories** (by ID ranges):
- 0-9: Ammunition (guns)
- 10-19: Melee weapons
- 20-29: Building materials
- 30-39: Seeds and crops
- 40-49: Special items
- 50: Turret blueprint
- 51: Torch (lighting)

**Item Data**:
```java
class InventoryItem {
    Item item;          // Item definition and metadata
    int count;          // Quantity (stack size)
}

class Item {
    int id;             // Unique identifier
    String name;        // Display name
    BufferedImage icon; // Inventory icon
}
```

**Weapon State Mapping**:
Selected inventory items determine player weapon state:
- ID 0: Tilling tool
- ID 1-9: Gun (firearm)
- ID 10-19: Melee weapon
- ID 20-29: Building state
- ID 30-39: Planting mode
- ID 50: Turret placement
- ID 51: Torch/lighting
- Otherwise: Empty hands

### 5. Tile and Map System

**Location**: `view/map/tile/TileManager.java` and related files

**Architecture**:
The game world is a grid-based map rendered as tiles. TileManager manages tile definitions, map layout, and collision data.

**Map Structure**:
```
Tile[][] mapTileNum (40x40 grid)
├─ Each cell references a Tile definition
├─ Values 0-15: Terrain types
├─ Values 16+: Special tiles (water, collision)
└─ Stored as text file (map1.txt)
```

**Tile Properties**:
```java
class Tile {
    BufferedImage image;   // Visual representation
    boolean collision;     // Collision flag for physics
}
```

**Tile Types**:
- 0, 14, 15: Grass variants (walkable)
- 1-13: Water (collision, non-walkable)
- Additional types: Farmland, buildings, specialized terrain

**Coordinate Systems**:
- **World Coordinates**: Absolute position in game world (0-1920 for 40x48 tiles)
- **Screen Coordinates**: Position relative to camera/player (0-768, 0-576)
- **Tile Coordinates**: Grid position (0-39, 0-39)

**Conversion Constants**:
```
tile_world_coord = tile_x * TILE_SIZE
world_coord_offset = player_world_coord - (DISPLAY_WIDTH / 2)
screen_coord = world_coord - world_coord_offset
```

### 6. Farming System

**Location**: `view/map/farming/FarmingManager.java` and `Crop.java`

**Architecture**:
The farming system manages cultivation state, crop growth, and harvest mechanics.

**Crop State Machine**:
```
Untilled → Tilled → Planted → Growing (4 stages) → Harvestable → Harvested
```

**Data Structure**:
```
FarmingManager
├─ Crop[][] crops (40x40 grid, one per tile)
│  └─ null if no crop or terrain type incompatible
├─ int cropsPlanted (count tracking)
└─ Growth mechanics (stage progression, timing)
```

**Crop Properties**:
```java
class Crop {
    int growth_stage;        // 0-4 (unharvested only)
    CropType type;           // Identifies crop species
    int health;              // Crop vitality
    byte color_modifier;     // Visual variation
    double last_watered;     // Timestamp for irrigation tracking
}
```

**Growth Mechanics**:
- Crops progress through growth stages automatically over time
- Watering affects growth rate
- Graphics change per growth stage
- Harvest yields resources (coins, seeds)

### 7. Building System

**Location**: `view/map/building/BuildingManager.java` and `Building.java`

**Architecture**:
The building system manages construction placement, turret placement, and building state.

**Data Structure**:
```
BuildingManager
├─ Building[][] buildings (40x40 grid)
├─ Health maps for durability tracking
├─ Rotation data for orientation
└─ Cost management and validation
```

**Building Types**:
- Defensive turrets (primary structures)
- Walls (barriers)
- Fences (visual/organizational)
- Structures with varying costs

**Placement Validation**:
- Terrain compatibility checking
- Collision detection with existing structures
- Cost verification against inventory
- Rotation and orientation support

### 8. Lighting System

**Location**: `view/map/LightManager.java`

**Architecture**:
Dynamic lighting for day-night cycle visualization.

**Features**:
- Gradual lighting transitions
- Darkness overlay during nighttime
- Player-centric light source
- Torch mechanics for player-generated light

---

## Component Details

### Player Character

**File**: `model/gameObjects/Player.java`

**Attributes**:
```
Physical State:
├─ Position: worldX, worldY
├─ Speed: speedX, speedY, direction
├─ Collision: XtileCollision, YtileCollision
└─ Rendering: currImage (current animation frame)

Combat State:
├─ Health: 0-400 (starts 400)
├─ Ammunition: variable count
└─ Weapon state: Gun, Melee, Build, etc.

Economic State:
├─ Coins: currency for purchases
├─ Earned coins: profit tracking
└─ Inventory: 10-slot inventory array

Status Conditions:
├─ Bankrupt flags (tracking insufficient resources)
├─ Hit by zombie (knockback mechanic)
├─ Locked state (UI interaction, animations)
└─ Animation state (step counter for walking)
```

**Animation System**:
```
Skin system manages multiple character variants:
├─ Direction animations (front, back, left, right)
├─ Per-direction animation frames
├─ Frame timing via step counter
└─ Different skins loadable per player session
```

**Weapon States**:
```java
enum w_State {
    Gun,      // Firearm equipped
    Melee,    // Melee weapon equipped
    Build,    // Building mode active
    Tilling,  // Farming tool equipped
    Empty,    // No weapon/tool
    Planting, // Seed placement mode
    Turret,   // Turret placement mode
    Torch     // Light source active
}
```

**Key Methods**:
- `tick()`: Update animation, process movement, handle interactions
- `render(Graphics)`: Draw character sprite with direction/animation state
- `takeDamage(double)`: Handle zombie contact and knockback
- `handleMovement()`: Process directional input and collision

### Zombie Enemy

**File**: `model/gameObjects/Zombie.java`

**Attributes**:
```
Physical State:
├─ Health: 10 + 2*nightCount (scales with difficulty)
├─ Speed: 1 + nightCount/10 (escalates with night progression)
├─ Direction: moving toward target
└─ Animation: directional walking frames

Behavior State:
├─ Target angle: calculated toward crop or player
├─ Crop seeking: approximately 50% spawn crop-focused
├─ Crop eating: state when at crop location
├─ Building destruction: targeted building demolition mode
└─ Sound state: ambient noise emission tracking
```

**Targeting Priority**:
1. Nearby crops (if crop_seeking behavior)
2. Destructible buildings
3. Player character
4. Idle wandering

**Difficulty Scaling**:
- **Night Count**: Tracks progression through night cycles
- Speed increases with night count: `speed = 1 + nightCount/10`
- Health increases with night count: `health = 10 + 2*nightCount`
- Results in exponential difficulty escalation

**Destruction Mechanics**:
- Zombies can destroy turrets and walls
- Multiple zombies can collectively damage buildings
- Building health tracked separately from zombie health

### Turret Defense Structure

**File**: `model/gameObjects/Turret.java`

**Attributes**:
```
Base Configuration:
├─ Position: worldX, worldY (tile-aligned)
├─ Range: detection radius (default 300 pixels)
├─ Shots per volley: bullets variable
└─ Angle: current rotation toward target

Upgrade Levels:
├─ rangeLevel: extends attack range
├─ damageLevel: increases projectile damage
├─ cooldownLevel: reduces fire rate
└─ Each level reduces/increases values via formula
```

**Firing Sequence**:
```
1. Scanning Phase: Searches for target within range
2. Lock Phase: Rotates toward nearest zombie
3. Shooting Phase: Fires burst of projectiles
   ├─ Count: controlled by 'bullets' variable
   ├─ Rate: 1 projectile per 5 frames
   └─ Sound: rifleSound() on completion
4. Cooldown Phase: Waits before re-targeting
```

**Upgrade System**:
Turrets can be enhanced through a menu interface:
- Upgrades purchased with coins
- Each upgrade tier costs progressively more
- Levels scale damage, range, and cooldown values

### Projectile System

**File**: `model/gameObjects/Projectile.java`

**Attributes**:
```
Physical State:
├─ Speed: high velocity (varies by source)
├─ Direction: angle toward target
├─ Range: maximum travel distance
└─ Damage: impact damage value

Source Tracking:
├─ Origin: worldX, worldY spawn point
├─ Owner: ID of firing entity (Player or Turret)
├─ Velocity angle: trigonometric trajectory
```

**Lifecycle**:
1. Creation at weapon position with velocity vector
2. Per-frame movement with collision checking
3. Hit detection against target type
4. Removal on hit or range expiration
5. Damage application and target removal

### NPC System

**File**: `model/gameObjects/NPC.java`

**Architecture**:
NPCs provide narrative interaction, trading, and tutorial mechanics.

**Features**:
- Script-driven dialogue system
- Trading interface
- Static or simple movement patterns
- Narrative progression tracking

---

## Data Flow

### Update Phase (tick)

```
Game.tick() [Controller]
├─ Input processing via KeyInput
├─ Player.tick()
│  ├─ Store movement commands
│  ├─ Update animation state
│  ├─ Check collisions with map
│  ├─ Process interactions
│  └─ Handle knockback/damage
├─ Handler.tick()
│  └─ For each GameObject:
│     ├─ Update position
│     ├─ Check collisions
│     └─ Process behavior logic
├─ ZombieSpawner.tick()
│  └─ Generate new zombies based on night cycle
├─ FarmingManager.tick()
│  └─ Progress crop growth stages
├─ BuildingManager.tick()
│  └─ Process turret behaviors
├─ LightManager.tick()
│  └─ Manage day-night transitions
└─ Day-night cycle updates
   ├─ nightTimer increment
   ├─ nightCount update (describes intensity)
   └─ GameState transitions (Night vs Day)
```

### Render Phase (render)

```
Game.render(Graphics) [View]
├─ Clear Canvas (black background)
├─ TileManager.render(g)
│  ├─ Render all visible tiles
│  ├─ Apply camera offset
│  └─ [worldX - playerX offset]
├─ Handler.render(g)
│  └─ For each GameObject:
│     ├─ Calculate screenX, screenY
│     ├─ Call object.render(g)
│     └─ Player always last (top layer)
├─ FarmingManager.render(g)
│  └─ Render visual crop representations
├─ BuildingManager.render(g)
│  └─ Render turrets, walls, structures
├─ HUD.render(g)
│  ├─ Health bar
│  ├─ Ammo counter
│  ├─ Coin display
│  ├─ Inventory display
│  ├─ Night indicator
│  └─ Game statistics
├─ LightManager.render(g)
│  └─ Apply darkness overlay or lighting effects
├─ Menu system.render(g) [if active]
│  └─ Render appropriate menu based on GameState
└─ Display buffered frame
```

### Collision Detection Flow

```
Player collision check:
├─ Get next position based on velocity
├─ Check against TileManager for solid tiles
│  ├─ Query tile collision flags
│  ├─ Compute affected tile grid cells
│  └─ Set XtileCollision, YtileCollision flags
├─ Check against BuildingManager structures
│  └─ Query building bounds rectangles
├─ Check against other GameObjects
│  ├─ Zombie contact → takeDamage()
│  └─ NPC contact → show dialogue
├─ Apply collision response
│  ├─ Revert velocity if collision blocked
│  └─ Slide along surfaces if partial collision
└─ Update world coordinates
```

### Event Flow

```
User Input (KeyEvent)
├─ KeyInput listener detects keystroke
├─ Store key state (pressed/released)
├─ Player.tick() reads key state
├─ Movement commands generated
├─ Collision detection
├─ Position updates
├─ Interaction checks
└─ HUD updates

Inventory Selection (numeric key)
├─ Inventory.setSelected(slot)
├─ Query item in slot
├─ Player.setWeaponState() based on item type
├─ HUD.setInventorySelection() for highlighting
└─ Next frame renders new state

Game State Transition
├─ Bankrupt condition check (bankruptcy checks)
├─ If condition met:
│  ├─ Game.gamestate = BANKRUPT
│  ├─ BankruptMenu.show()
│  └─ Game loop switches to menu rendering
├─ Similarly for Death, Pause, Store states
└─ Menu actions trigger state transitions back to Running
```

---

## Design Patterns

### 1. Model-View-Controller (MVC)

The architecture implements a variant of MVC with clear separation:

- **Model**: `model/` package contains pure state and logic
- **View**: `view/` package handles all rendering
- **Controller**: `controller/` manages input and coordination

### 2. Entity-Component System (Simplified)

GameObjects function as entities with common interface:
- Base GameObject class provides common state and contract
- Subclasses implement entity-specific behavior
- Handler manages collection of diverse entities uniformly

### 3. State Pattern

Multiple systems use explicit state machines:

- Player weapon states (`w_State` enum)
- Game states (GameState enum)
- Zombie behavior states (crop-seeking vs combat)
- Crop growth states (untilled → harvested progression)

### 4. Manager Pattern

Specialized managers handle subsystems:
- TileManager: Map and terrain
- BuildingManager: Structures and turrets
- FarmingManager: Crops and harvesting
- LightManager: Illumination and day-night cycle
- ItemManager: Item definitions and lookups
- SkinManager: Character variant management

### 5. Singleton-like Pattern

Global static references in Game class:
```java
public static Handler handler;
public static HUD hud;
public static Player player;
public static TileManager tileManager;
```

Enables convenient system access throughout codebase while maintaining single instances.

### 6. Factory Pattern

ItemManager and SkinManager create/load objects:
- `ItemManager.getItem(id)`: Returns item definition
- `SkinManager.getSkin(name)`: Returns character variant
- Centralizes object creation logic

### 7. Observer-like Pattern (Event Driven)

Game loop continuously polls state and processes updates:
- Input system (KeyInput) maintains state
- Game loop checks input state each frame
- View components read model state each render frame

---

## Directory Structure

### Root Structure

```
/Farm-Defense
├── LICENSE                 # Project license
├── README.md              # Project overview
├── ARCHITECTURE.md        # This document
├── DEVELOPMENT.md         # Development guide
├── documentation/         # Additional docs
├── farm_defense/          # Main project
│   ├── bin/              # Compiled bytecode
│   ├── src/              # Java source code
│   ├── resources/        # Game assets
│   └── saves/            # Player save files
└── images/               # Project images (README)
```

### Source Code Structure (`src/`)

#### Controller Package (`src/controller/`)

```
controller/
├── Game.java              # Main game class, rendering engine, game loop
├── KeyInput.java          # Keyboard input handler
└── objectHandling/
    ├── Handler.java       # Dynamic object manager
    └── ID.java           # Entity type enumeration
```

**Responsibilities**: Game state orchestration, input processing, object lifecycle management.

#### Model Package (`src/model/`)

```
model/
├── GameObject.java        # Abstract base class for all entities
├── Sound.java            # Audio playback manager
├── TradeItem.java        # Trading system data structure
├── gameObjects/
│   ├── Player.java       # Player character implementation
│   ├── Zombie.java       # Zombie enemy implementation
│   ├── Turret.java       # Defense structure implementation
│   ├── Projectile.java   # Ammunition/projectile implementation
│   ├── NPC.java          # Non-player character
│   └── ZombieSpawner.java # Wave spawning system
├── Inventory/
│   ├── Inventory.java    # Player inventory container
│   └── InventoryItem.java # Individual inventory slot representation
├── items/
│   ├── Item.java         # Item data definition
│   └── ItemManager.java  # Item registry and factory
└── skin/
    ├── Skin.java         # Character variant representation
    └── SkinManager.java  # Skin registry and factory
```

**Responsibilities**: Game entity state, mechanics implementation, inventory management.

#### View Package (`src/view/`)

```
view/
├── Window.java           # JFrame and display setup
├── HUD.java             # Heads-up display (statistics, inventory, buffs)
├── GraphicBuilder.java  # Graphics resource caching
├── MapEditorHelper.java # Map editing utilities
├── fullMenu/
│   ├── MainMenu.java    # Title screen
│   ├── PauseMenu.java   # Pause overlay
│   ├── DeathMenu.java   # Death screen
│   ├── BankruptMenu.java # Economic failure screen
│   ├── StoreMenu.java   # Item shop
│   └── BenchMenu.java   # Crafting workbench
├── sideMenu/
│   ├── TradeMenu.java   # NPC trading interface
│   └── TurretMenu.java  # Turret upgrade interface
└── map/
    ├── LightManager.java # Lighting and day-night cycle
    ├── tile/
    │   ├── TileManager.java  # Terrain and map management
    │   └── Tile.java        # Individual tile representation
    ├── building/
    │   ├── BuildingManager.java # Structure management
    │   └── Building.java       # Individual structure
    └── farming/
        ├── FarmingManager.java  # Crop management
        └── Crop.java          # Individual crop representation
```

**Responsibilities**: All rendering, UI presentation, visual state representation.

#### Utility Package (`src/util/`)

```
util/
├── GraphicsUtil.java    # Graphics rendering helpers
├── ImageUtil.java       # Image loading and manipulation
├── MathUtil.java        # Mathematical utilities (trigonometry, randomness)
├── SaveManager.java     # Game saving/loading
├── TileUtil.java        # Tile coordinate conversion
└── TxtFileUtil.java     # Text resource file parsing
```

**Responsibilities**: Cross-cutting utilities supporting other packages.

### Resources Structure (`resources/`)

```
resources/
├── animations/          # Animation frame assets
├── buildings/
│   ├── stone/          # Stone structure graphics
│   └── wood/           # Wooden structure graphics
├── farming/            # Crop graphics
├── fonts/              # Font files
├── icon/               # UI icons
├── inventory/
│   ├── seeds/          # Seed item icons
│   └── walls/          # Wall item icons
├── maps/
│   ├── map1.txt        # Terrain tilemap data
│   ├── buildingmap.txt # Building placement data
│   ├── buildingrotationmap.txt # Structure rotation data
│   ├── buildinghealth.txt # Structure durability data
│   ├── farmlandmap.txt # Farmable terrain data
│   └── cropstagemap.txt # Crop growth progression data
├── npc/                # NPC character graphics
├── npcscripts/         # NPC dialogue scripts
├── player/             # Player sprite sheets
├── sound/
│   ├── effects/        # Sound effects
│   └── music/          # Background music
├── text/
│   ├── controls.txt    # Control scheme documentation
│   └── credits.txt     # Credits information
├── tiles/              # Terrain tiles
├── trader/             # Trading interface graphics
├── zombie/             # Zombie sprite sheets
└── skins/              # Alternative player character skins
```

**Data Files Format**:

Map data files store terrain information as delimited text:
- Each integer represents a tile type or structure reference
- Row-major ordering for 40x40 grid
- Space or comma delimiters between values
- Load process in TileManager queries resource files

### Build Output (`bin/`)

```
bin/
├── controller/         # Compiled controller classes
├── model/              # Compiled model classes
├── util/               # Compiled utility classes
├── view/               # Compiled view classes
└── resources/          # Symlink/copy of game assets
```

---

## Development Workflow Recommendations

### Adding a New Entity Type

1. Create class in `model/gameObjects/` extending GameObject
2. Implement required abstract methods: `tick()`, `render()`, `getBounds()`
3. Add new ID to `controller/objectHandling/ID.java` enum
4. Register entity in Handler if it requires dynamic management
5. Add sprite assets to `resources/`
6. Implement rendering in `view/` if special display needed

### Adding a New Item

1. Create item asset (48x48 PNG icon)
2. Place in appropriate `resources/inventory/` subdirectory
3. Register in `ItemManager.loadItems()`
4. Assign ID number in item range
5. Test inventory integration and weapon state mapping

### Adding a New Game System

1. Create manager class in `model/` or `view/` (appropriate layer)
2. Add static reference in Game class
3. Initialize in Game constructor
4. Call tick() in Game.tick() if state-based
5. Call render() in Game.render() if visual
6. Document state in this architectural guide

### Modifying Map Layout

1. Edit map file in `resources/maps/map1.txt`
2. File format: 40x40 grid of tile IDs separated by spaces
3. Regenerate building/farming data if structure placement changed
4. Test in-game for collision and gameplay impact
5. Consider performance impact of terrain complexity

---

## Performance Considerations

### Rendering Optimization

- **Tile caching**: TileManager pre-loads tile images to avoid per-frame loading
- **Screen culling**: Only visible tiles and objects render (implicit in coordinate system)
- **Image scaling**: ApplicationFramework.addImage() pre-scales for memory efficiency
- **Double buffering**: BufferStrategy provides flicker-free rendering

### Physics Optimization

- **Tile-based collision**: Grid-based terrain simplifies broad-phase collision
- **Discrete collision checking**: AABB rectangles tested each frame
- **Object pool consideration**: For future high-count projectile waves, pool objects

### Frame Rate Maintenance

- **Fixed timestep**: Game loop targets 60 FPS
- **No adaptive quality**: Consistent rendering demands
- **Memory management**: LinkedList avoids allocation during active gameplay

---

## Known Limitations and Areas for Improvement

### Current Limitations

1. **No object pooling**: Projectiles and zombies allocated/deallocated dynamically
2. **Collision detection**: Simple AABB rectangles, no advanced physics
3. **Graphics scaling**: Fixed 48-pixel tiles, limited resolution adaptability
4. **Audio polyphony**: Limited simultaneous sound channels
5. **Save system**: SaveManager class is empty, no persistent storage

### Potential Enhancements

1. Implement comprehensive save/load system
2. Add networking for multiplayer functionality
3. Implement advanced particle effects
4. Create modding framework for custom maps/skins
5. Optimize with custom camera/viewport system
6. Add procedural wave generation
7. Implement streaming terrain system for expandable maps

---

## Conclusion

The three-tier architecture successfully manages complexity across 40+ source files while maintaining clear separation of concerns. Future development should prioritize completing the save system and optimizing the object lifecycle management to support larger enemy waves without performance degradation.

