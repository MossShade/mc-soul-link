# SoulLink Challenge Mod
A server-sided Minecraft Fabric mod that adds a co-op SoulLink challenge mechanic.  
Your soul is linked with friends! Everyone shares the same health, hunger, and saturation pool.


## Features
- **Shared Resources**: Health, hunger, and saturation are pooled among players.
- **Totem of Undying**: Totem of Undying can be used to prevent the death of the person who is holding it.  
  Note that this will only work if the player holding the Totem is the one to take lethal damage.


## Requirements
- Fabric API

## Commands
All commands are prefixed with `/soullink`.
- `/soullink status`: Checks if the mod is enabled and displays current pool status. (Health, hunger, saturation, etc)
- `/soullink reset`: Resets the pool status to default values (20 HP, 20 hunger, 5 saturation, etc). Requires OP permission.
- `/soullink config enable true|false`: Enables or disables the mod.

## Installation
- Install [Fabric enabled server](https://fabricmc.net/use/server/)
- Download [Fabric API](https://modrinth.com/mod/fabric-api)
- Download this mod's Jar file suitable for your version
- Copy both Fabric API and this Jar to your minecraft server's `mods` folder

Note: Since this is a server-only mod, all clients, including vanilla clients, are able to join the server.


### Resource Pack
The mod uses a language resource pack to provide translations. (Currently only English)

#### Download on Clients
Download the resource pack and place it in your client `resourcepacks` folder.

#### Push from Server
Alternatively, you can set the server to push the resource pack to clients by setting the following in `server.properties`:
```
require-resource-pack=true
resource-pack=<Link to resource pack>
resource-pack-sha1=<SHA-1 hash of resource pack zip>
```

The link must be a web accessible URL (cannot point to a local file).  
Google Drive can be used to host the resource pack:
- Upload the resource pack zip to Google Drive
- Share the file with "Anyone with the link" permission\
- Copy the ID of the file from the URL `https://drive.google.com/file/d/<ID of gdrive file>/view?usp=sharing`
- Replace `<ID of gdrive file>` with the ID of the file in the URL with the following format:
- `https\://drive.usercontent.google.com/download?export\=download&id\=<ID of gdrive file>`
- Set `resource-pack` to the URL of the file


## Known Issues
- Inconsistent: Client desync when one player receives lethal damage, but a totem saves them. This has mostly been fixed, but issue may rarely occur.  
  Workaround: Affected clients must disconnect and reconnect to the server.

