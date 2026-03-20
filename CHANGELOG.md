# SoulLink Challenge Mod
A server-sided Minecraft Fabric mod that adds a co-op SoulLink challenge mechanic.

## Changes
- **Shared Resources**: Health, hunger, and saturation are pooled among players.
- **Totem of Undying**: Totem of Undying can be used to prevent the death of the person who is holding it.  
  Note that this will only work if the player holding the Totem is the one to take lethal damage.

## Known Issues
- Inconsistent: Client desync when one player receives lethal damage, but a totem saves them. This has mostly been fixed, but issue may rarely occur.  
  Workaround: Affected clients must disconnect and reconnect to the server.
