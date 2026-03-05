<center>
    <h1>TO-DO : Banner image</h1>
    What is the point of turning Chorus into Purpur if you can't use its teleporting abilities?
</center>

<br>

## About

By powering Purpur Blocks using redstone, you can create elevator platforms that teleport you in between them.

_The mod supports a few different modes;_

### Passive (default)

Passive is the simplest mode, all you need to do is permanently power the Purpur Block with redstone, 
and it will teleport up by jumping, and down by crouching.

### Naïve power

Naïve power activates on being powered by redstone. The moment it gets powered it will teleport all entities on top of it to the nearest platform.

Going up takes priority over going down, so if there are platforms both above and below, it will teleport you up.

### Leveled power

Leveled power is similar to naïve power, but it uses the strength of the redstone signal to determine which platform to teleport to.

Depending on the strength of the redstone signal, it will teleport to the nth platform it can reach from there.

A signal strength of 1 will teleport to the lowest platform, and a signal strength of 15 will teleport to 15th floor.

<br>

## Configuration

On its own, the mod will not create a config file.
To change settings, you can install [Fzzy Config](https://modrinth.com/mod/fzzy-config).

To load changes to the config file, you can use the vanilla ``/reload`` command.

```toml
# The maximum distance between two elevator platforms.
maxElevatorDistance = 16
# The cooldown in seconds after using an elevator platform during which you can't use another one.
elevatorCooldownSeconds = 0.05
# Whether an elevator should work while the player is sprinting.
activateWhileSprinting = false
# Whether players riding vehicles should be teleported by the elevator. e.g minecarts and boats
allowVehicles = true
# Whether players riding mounts should be teleported by the elevator. e.g horses and pigs
allowMounts = true
```