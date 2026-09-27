# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [Unreleased]
- Nothing brewing currently.


## [1.0.5] - 2026-09-27

### Changes
- Updated to Minecraft version 26.3
- Updated Gradle to 9.7.1
- Updated resource pack build pipeline to pickup on decimal-formatted versions


## [1.0.4] - 2026-06-16

### Changes
- Updated to Minecraft version 26.2


## [1.0.3] - 2026-05-19

### Changes
- Updated to Minecraft version 26.1.2

### Removed
- Resource pack is no longer required for English clients as the server sends default English responses for commands


## [1.0.2] - 2026-04-05

### Changes
- Updated to Minecraft version 26.1.1


## [1.0.1] - 2026-04-05

### Changes
- Updated to Minecraft version 26.1

### Fixed
- Fixed the inconsistency where clients could desync when receiving lethal damage, but a totem saves one of the players.


## [1.0.0] - 2026-03-20

### Added
- **Shared Resources**: Health, hunger, and saturation are pooled among players.
- **Totem of Undying**: Totem of Undying can be used to prevent the death of the person who is holding it.  
  Note that this will only work if the player holding the Totem is the one to take lethal damage.

### Known Issues
- Inconsistent: Client desync when one player receives lethal damage, but a totem saves them. This has mostly been fixed, but issue may rarely occur.  
  Workaround: Affected clients must disconnect and reconnect to the server.
