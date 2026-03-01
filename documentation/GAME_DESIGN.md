# Farm Defense Game Design Document

## Table of Contents

1. [High-Level Concept](#high-level-concept)
2. [Game Loop and States](#game-loop-and-states)
3. [Core Mechanics](#core-mechanics)
4. [Resource Management](#resource-management)
5. [Combat System](#combat-system)
6. [Progression and Difficulty](#progression-and-difficulty)
7. [User Interface](#user-interface)
8. [Win and Loss Conditions](#win-and-loss-conditions)
9. [Conclusion](#conclusion)

---

## High-Level Concept

### Game Overview

Farm Defense is a hybrid farming and tower defense game where players manage a farm while defending it against nocturnal zombie attacks. The game implements resource management through farming during daytime hours and combat during nighttime zombie waves. The game runs indefinitely with escalating difficulty, providing endless gameplay progression.

---

## Game Loop and States

### State Machine Design

```
                    ┌─────────────┐
                    │ Main Menu   │
                    └──────┬──────┘
                           │ (Start Game)
                           ▼
                    ┌─────────────┐
                    │   Running   │──────────┐
                    │   (Day)     │          │ (Bankrupt)
                    └─────┬───────┘          │
                          │ (Night falls)    │
                          ▼                  ▼
                    ┌─────────────┐    ┌──────────────┐
                    │   Running   │    │ Bankrupt Menu│
                    │   (Night)   │    └─────┬────────┘
                    └─────┬───────┘          │ (Restart)
                          │ (Day rises)      │
                          └─────────────────┘
                    
                  Special States (from Running)
                  ├─ Paused (P key)
                  ├─ Store (right-click NPC)
                  ├─ Dead (health = 0)
                  ├─ Bench (crafting menu)
                  └─ Pause Menu (ESC)
```

### Frame Processing

Each frame follows a consistent sequence:

```
Frame N:
├─ Input Processing
│  └─ Poll keyboard state
├─ Update Phase (tick)
│  ├─ Player movement and action
│  ├─ Entity updates (zombies, turrets)
│  ├─ Collision detection
│  ├─ Health/status updates
│  ├─ Animation advancement
│  └─ Day-night cycle updates
├─ Render Phase
│  ├─ Clear display
│  ├─ Render terrain tiles
│  ├─ Render entities
│  ├─ Render UI overlays
│  ├─ Render active menu (if any)
│  └─ Flip display buffer
└─ Sleep to maintain 60 FPS
```

Target frame time: 16.67 milliseconds (60 FPS)

---

## Core Mechanics

### Movement and Navigation

**Player Movement System**

Players control the character using arrow keys or WASD:
- Each key press increments a direction counter
- Movement velocity calculated as sum of active directions
- Allows smooth diagonal movement when multiple keys pressed simultaneously

**Collision Handling**

Movement blocked by:
- Map boundaries (hard stops at map edges)
- Terrain tiles marked as collision (water, mountains)
- Built structures (turrets, walls)
- Other entities (NPCs, zombies)

When collision detected:
1. Position reversion to last valid state
2. Partial movement along one axis if available (sliding)
3. Feedback to player through visual blocking

**Screen Coordinates and Camera**

The game uses a camera system centered on the player:
```
screen_x = world_x - camera_offset_x
screen_y = world_y - camera_offset_y

where:
camera_offset_x = player_world_x - (DISPLAY_WIDTH / 2)
camera_offset_y = player_world_y - (DISPLAY_HEIGHT / 2)
```

This creates the illusion of a camera following the player while the world remains static.

### Farming System

**Farmland Mechanics**

Farmland represents tillable terrain suitable for crop cultivation. Farming progression follows sequential stages:

1. **Untilled State**: Default state of farmland
   - Looks like grass or dirt
   - Crops cannot be planted
   - Requires tilling action first

2. **Tilling Action**: Player activates tilling tool
   - Requires equipped tilling tool (ID 0 in inventory)
   - Applied to single tile by moving to tile and activating
   - Consumes no resources or items
   - Instantaneous (single-frame operation)

3. **Tilled State**: Ready for planting
   - Tile appears visually distinct (darker, prepared soil)
   - Remains tilled indefinitely until crop planted
   - Player can plant seeds at this stage

4. **Planting Action**: Deploys seed to tilled ground
   - Requires seed in inventory (ID 30-39 range)
   - Specific seed type planted affects harvest
   - Different seeds produce different crops
   - Planting consumes one seed from inventory

5. **Growth Cycle**: Crop develops through stages
   - Stage 0: Sprout (newly planted)
   - Stage 1-3: Growing (intermediate development)
   - Stage 4: Mature (ready to harvest)
   - Growth automatic, triggered by time progression
   - Watering affects growth rate (optional mechanic)

6. **Harvest Action**: Collect mature crop
   - Only possible at stage 4 (mature)
   - Yields coins and potentially seeds
   - Clears tile to untilled state
   - Harvest amount increases with crop quality

**Crop Types and Yields**

Different seeds produce different crops with varying yields:

| Seed Type | Growth Time | Coin Yield | Seed Yield | Notes |
|-----------|------------|-----------|-----------|-------|
| Wheat | 120 seconds | 50 | 2 | Standard crop |
| Corn | 180 seconds | 75 | 1 | Higher value |
| Carrots | 90 seconds | 30 | 3 | Quick return |

Growth time inversely affects yield—fast crops return less value than slow crops.

### Interaction System

**Distance-Based Interactions**

Interactions trigger when player occupies same or adjacent tile:

```
Player Interaction Range:
    ┌─────┬─────┬─────┐
    │     │ adj │     │
    ├─────┼─────┼─────┤
    │ adj │ [P] │ adj │ [P] = Player position
    ├─────┼─────┼─────┤
    │     │ adj │     │
    └─────┴─────┴─────┘
```

**Interaction Activation**

Interactions activate when:
1. Player moves to interaction range
2. Player presses action key (Space)
3. System performs context-specific action

Context determines action:
- NPC dialogue box: Opens trading menu
- Turret: Opens upgrade menu
- Crop at stage 4: Harvest interaction shows
- Dirt tile: Tilling action queued

---

## Resource Management

### Currency System

**Coins**

Coins represent the player's wealth and primary currency:

**Coin Sources**:
- Harvesting crops (primary income)
- Selling items to trader
- Finding treasure (rare)
- Initial player funds: 500 coins

**Coin Uses**:
- Purchasing seeds (variable cost per seed)
- Buying weapon ammunition
- Constructing defensive structures
- Upgrading turrets

**Economic Win Condition**:
Accumulate sufficient coins to purchase expensive late-game items and upgrades.

### Inventory Management

**Capacity**

Inventory holds exactly 10 distinct item slots:
- Each slot contains one item type with count
- Slots are numbered 1-0 (matching keyboard keys)
- Empty slots appear blank in UI

**Item Stacking**

Most items stack (increasing count), but some occupy full slots:
- Ammunition: Stacks up to 999 rounds
- Seeds: Stack up to 999 units
- Special items: Typically single slot (turret blueprint, torch)
- Equipment: Single item per slot (weapons)

**Inventory Access**

Players select active item using number keys:
```
Key: 1  2  3  4  5  6  7  8  9  0
Slot: 1  2  3  4  5  6  7  8  9  10
```

Selected item determines player weapon state and available actions.

**Item Categories and Roles**

| ID Range | Category | Purpose | Count |
|----------|----------|---------|-------|
| 0-9 | Ammunition (Guns) | Ranged weapons | Variable |
| 10-19 | Melee Weapons | Close combat | Limited |
| 20-29 | Building Materials | Structure construction | Limited |
| 30-39 | Seeds | Agricultural planting | Variable |
| 40-49 | Consumables | Temporary effects | Variable |
| 50 | Turret Blueprint | Defensive construction | Limited |
| 51 | Torch | Darkness resistance | Single |

### Bankruptcy Mechanics

**Bankruptcy Conditions**

Player enters bankruptcy if all following conditions are true:

1. **No Viable Income** (`noCoins`):
   - Cannot afford a seed packet
   - Lowest seed cost exceeds current coins
   - Traps player in resource deficit

2. **Poor Inventory** (`badInventory`):
   - No seeds in inventory
   - No harvestable crops available
   - Cannot initiate farming cycle

3. **No Active Crops** (`noCrops`):
   - Zero crops currently planted
   - Prevents future harvests
   - Combined with above = no recovery path

**Bankruptcy Recovery**

Player can avoid bankruptcy by:
- Defending against zombies (no current reward, but prevents resource loss)
- Harvesting crops before they mature (alternative: accept smaller yield)
- Rationing seeds to maintain at least one crop
- Finding alternative income sources (trading, treasure)

**Bankruptcy Consequence**

Upon bankruptcy:
1. Game state transitions to `GameState.Bankrupt`
2. BankruptMenu displays with "Game Over" message
3. Options to restart or load save (if implemented)
4. Player forfeit current run

---

## Combat System

### Player Combat

**Weapon Selection**

Player equips weapon via inventory selection:
- Slot 1-9: Gun ammunition (ranged)
- Slot 10-19: Melee weapon (close range)
- Slot 50: Turret placement tool

**Ranged Combat (Guns)**

When players select gun ammunition:
1. Player assumes gun-ready stance
2. Space bar triggers shot
3. Creates Projectile object at player position
4. Direction determined by player facing direction
5. Projectile travels outward until hitting target or range limit

**Projectile Mechanics**:
- Velocity: Fixed per weapon (typically 8-12 px/frame)
- Lifetime: Maximum range (typically 500-800 pixels)
- Damage per hit: Determined by ammo type (1-5 health points)
- Penetration: Bullets stop on first zombie hit

**Melee Combat**

When players select melee weapon:
1. Player assumes melee-ready stance
2. Space bar triggers swing
3. Creates attack hitbox adjacent to player
4. Single-frame hitbox checks for zombie collision
5. Damage applied to any zombie in range

**Melee Characteristics**:
- Range: Adjacent tiles only (48 pixels)
- Damage: Higher than ranged (2-10 per swing)
- Speed: Faster attack rate than ranged (every 0.5 seconds)
- Knockback: Pushes zombies away

### Zombie Combat

**Zombie Targeting**

Zombies employ intelligent target selection:

1. **Priority Order**:
   ```
   Proximity Check:
   ├─ Nearby crops (within 150 pixels) → Seek/eat
   ├─ Visible turrets → Attack
   ├─ Player in range → Attack player
   └─ Wander randomly → Default behavior
   ```

2. **Crop Seeking** (approximately 50% of zombies):
   - Zombies marked as `cropSeeking = true`
   - Pathfind toward nearest harvestable crop
   - Prioritize over player unless player very close
   - Eat/destroy crop on arrival

3. **Turret Destruction**:
   - Zombies recognize turrets as threats
   - Attack turrets blocking path to crop/player
   - Takes multiple hits to destroy turret (health tracked)
   - Damaged turrets become less effective

### Turret Automated Defense

**Turret Mechanics**

Turrets provide passive defense with automatic targeting:

1. **Detection Phase** (every frame):
   ```
   Scan for zombies within range (default: 300 pixels)
   ├─ Calculate distance to each zombie
   ├─ Track closest zombie
   └─ Set `targeted = true` if found
   ```

2. **Aiming Phase** (every 95-40 frames based on cooldown upgrade):
   ```
   Rotate toward target
   ├─ Calculate angle to target
   ├─ Rotate turret visually
   └─ Fire when angle within tolerance
   ```

3. **Firing Phase** (if targeted):
   ```
   Discharge burst of projectiles
   ├─ Create N projectiles (N = bullets variable)
   ├─ Space projectiles over 5-frame burst
   ├─ Play fire sound effect
   └─ Enter cooldown period
   ```

4. **Cooldown Phase** (variable based on upgrades):
   ```
   Wait before next targeting cycle
   └─ Duration: (base - cooldownLevel * 5) frames
   ```

**Turret Upgrades**

Turrets can be upgraded through turret menu when player intersects turret:

| Upgrade Type | Effect | Cost | Max Level |
|--------------|--------|------|-----------|
| Range | Extends detection radius | 50 coins | 5 |
| Damage | Increases projectile damage | 50 coins | 5 |
| Cooldown | Reduces fire rate cooldown | 50 coins | 5 |

Each upgrade level costs progressively more (cost formula: `base_cost * (level + 1)`).

---

## Progression and Difficulty

### Day-Night Cycle

**Time Progression**

Game tracks time in minutes and hours:
- Time advances during gameplay (rate: 1 game-minute per ~100 real-time frames)
- Cycle: 0-23 hours (24-hour day)
- Night: Hours 20-7 (12 hours)
- Day: Hours 7-20 (12 hours)

**Cycle Effects**

| Time Period | Game State | Characteristics |
|-------------|-----------|-----------------|
| Hour 7-19 | Day | Zombies inactive, farming focus, bright lighting |
| Hour 20-23 | Night | Zombies spawn, combat priority, dim lighting |
| Hour 0-6 | Night | Continued zombie waves, darkness overlay |
| Hour 7 | Dawn | Zombies cease spawning, lighting transitions |

**Visual Feedback**

Lighting system provides time cues:
- Day: Full brightness, normal colors
- Dusk: Amber tinting, gradual darkening
- Night: Significant darkness, reduced visibility
- Torch item: Generates personal light circle
- Dawn: Gradual brightening

### Difficulty Scaling

**Night Count Variable**

Variable `nightCount` tracks cumulative night intensity:
- Increments each frame during night (approximately +0.05 per frame)
- Resets to 0 at dawn
- Affects zombie speed and health
- Creates scaling difficulty within single night

**Zombie Scaling Formula**

Zombie properties scale with nightCount:
```
speed = 1 + nightCount/10
health = 10 + 2 * nightCount

Example progression (within single night):
├─ Early night (nightCount = 0): speed=1, health=10
├─ Mid night (nightCount = 5): speed=1.5, health=20
├─ Late night (nightCount = 10): speed=2, health=30
└─ Very late (nightCount = 20): speed=3, health=50
```

**Spawn Rate Escalation**

ZombieSpawner increases zombie count with each successive night:

```
Night 1: 5-8 initial zombies
Night 2: 8-12 initial zombies
Night 3: 12-20 initial zombies
Night N: 5-8 + (2-4 per successive night)
```

**Cumulative Difficulty**

Multiple nights compound difficulty:
- Earlier nights provide resource accumulation time
- Later nights assume players have superior infrastructure
- Difficulty expected to eventually overwhelm player
- Design encourages strategic planning before each night

---

## User Interface

### Heads-Up Display (HUD)

**Persistent Information Overlay**

HUD displays vital information during gameplay:

```
┌─────────────────────────────────────────────────────────┐
│ Health: [████░░░░] 80/100    Ammo: 25   Coins: 1500    │
│ Time: 18:45 | Night Intensity: ████░░░░                │
│                                                          │
│ Inventory Slots:                                         │
│ [1]Gun   [2]Seed   [3] -    [4] -    [5]Turret [6] -   │
│  ↑Selected                                               │
└─────────────────────────────────────────────────────────┘
```

**Health Bar**

- Visual representation of player health (current/maximum)
- Red bar depletes when damaged
- Restores when consuming healing items
- Player dies when health reaches zero

**Ammo Counter**

- Displays current ammunition count for selected weapon
- Updates when shots fired or ammo picked up
- May display "∞" for unlimited weapons or empty
- Ammo depletion forces weapon switching

**Coin Display**

- Real-time currency balance
- Increases when harvesting crops
- Decreases when purchasing items or building structures
- Critical bankruptcy warning when near threshold

**Time Display**

- Current in-game time (HH:MM format)
- Visual day/night indicator (sun/moon icon)
- Darkness overlay intensity indicator
- Helps player plan activities

**Inventory Display**

- 10 inventory slots numbered 1-0
- Selected slot highlighted
- Shows item count for stacked items
- Visual item icons for quick identification

### Menu Systems

**Main Menu**

Initial screen upon game launch:
- "Farm Defense" title
- Options: Start Game, Load Game, Settings, Exit
- Background art or animation
- Control instructions visible

**Pause Menu**

Triggered by pressing ESC during gameplay:
- "Game Paused" overlay
- Options: Resume, Settings, Save, Exit to Menu
- Game simulation continues frozen in background
- Transparent overlay preserves context

**Store Menu**

Accessed by interacting with NPC:
- Displays available items for purchase
- Shows item descriptions and costs
- Purchase interface (select item, confirm)
- Exit button to resume gameplay
- Inventory space checking before purchase

**Turret Menu**

Accessed by intersecting with turret:
- Displays turret stats: range, damage, cooldown
- Upgrade buttons for each stat
- Shows costs for each upgrade and current level
- Visual feedback on upgrade effects
- Exit when moving away from turret

**Death Menu**

Shown when player health reaches zero:
- "You Died" message
- Statistics: Zombies killed, coins earned, crops harvested
- Options: Restart Run, Load Save, Main Menu
- Cannot resume from death (must restart)

### Interaction Prompts

**Context-Aware Indicators**

When player approaches interactive element:
- On-screen indicator appears (text or icon)
- Example: "[E] Harvest" when near mature crop
- Example: "[E] Open Store" when near NPC
- Disappears when out of interaction range

---

## Win and Loss Conditions

### Loss Conditions

**Immediate Loss (Game Over)**

Player loses the current run if:

1. **Player Death**:
   - Health depleted to zero or below
   - Caused by zombie contact in combat
   - Shows death menu with statistics
   - Allows restart or return to menu

2. **Bankruptcy**:
   - Cannot afford seeds (no coins)
   - No seeds in inventory
   - No crops planted or harvestable
   - All three conditions must be true simultaneously
   - Shows bankruptcy menu

**Zombie Elimination Risk**

If zombies reach player:
1. Contact triggers damage calculation
2. Player knocked back
3. Health decreases
4. If health ≤ 0, player dies
5. Multiple zombie hits compound damage

### Win Conditions

**Current Implementation**

The game currently lacks defined win conditions, encouraging indefinite play:
- Players accumulate resources
- Defend against progressively harder waves
- Competition against self (high scores, resources)
- No explicit "victory" state

**Intended Win Conditions** (design notes for future):

Option A: Time-based Victory
- Survive until in-game day 100
- Progressively harder nights encourage preparation
- Scoreboard tracks waves survived

Option B: Resource Victory
- Accumulate specific currency amount (e.g., 50,000 coins)
- Encourages both farming and survival
- Buildable milestone progression

Option C: Structure Victory
- Construct all available turret types
- Upgrade all turrets to maximum
- Demonstrates mastery of systems

Option D: Endless Mode
- Unending difficulty increase
- Leaderboard for waves survived or time lasted
- Current implementation reflects this philosophy

---



---

## Conclusion

Farm Defense implements a hybrid farming and tower defense system with resource management as the core mechanic. Difficulty escalates through the night-count variable, creating increasing challenge across successive nights. Players manage competing priorities between farming (resource generation) and combat (survival), with game continuation dependent on maintaining economic viability and health.

