# Attack on Titan (Mid) - Fabric 1.20.1

A functional mid-tier Attack on Titan mod.

## Features

- **Titan Serum** – Use to inherit either Attack Titan or War Hammer Titan (random)
- **Transform** (Key: **R**) – Transform / detransform with lightning + steam particles
- **Attack Titan** – Extra damage, high health, passive regeneration while transformed
- **War Hammer Titan** – Press **G** while transformed to forge a Crystal Sword (cooldown)
- **ODM Gear** – Right-click or press **F** to grapple. Gas system included
- **Dual Blades** – Decent melee weapon
- **Thunder Spears** – Throwable, high damage vs Pure Titans + explosion effect
- **Pure Titans** – Basic hostile Titans you can fight

## Controls

| Key | Action                    |
|-----|---------------------------|
| R   | Transform / Detransform   |
| F   | ODM Grapple               |
| G   | War Hammer Forge (sword)  |

## How to Build the JAR

### Requirements
- JDK 17 or newer
- Git (optional)

### Steps

1. Clone or download this repository
2. Open a terminal in the project root (`aot-mod`)
3. Run:

```bash
./gradlew build
```

(On Windows: `gradlew.bat build`)

4. The finished JAR will be in:

```
build/libs/aot-mod-1.0.0.jar
```

5. Put that JAR into your Minecraft `mods` folder (Fabric 1.20.1 + Fabric API required).

## First Time Setup (if gradlew is missing)

If the Gradle wrapper scripts are missing, run:

```bash
gradle wrapper --gradle-version 8.8
```

Then build again.

## Notes (Mid version)

- No custom Titan models yet (player just gets big stats)
- Crystal structures are particle-only for now
- ODM is simple velocity pull (not full swinging physics)
- Sounds use vanilla placeholders
- Perfect base to expand with GeckoLib models later

## License

MIT
