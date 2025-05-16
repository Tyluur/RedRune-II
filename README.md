<div align="center">

<h1>RedRune-II</h1>

<a href="https://github.com/Tyluur/RedRune-II">
  <img src="https://img.tyluur.com/2025/05/646/2025-05-16_06-47-42.png" alt="RedRune-II Logo">
</a>

[![Revision](https://img.shields.io/badge/revision-667-important)](https://oldschool.runescape.wiki/w/Update:God_Wars_Instancing_and_Soul_Wars_Improvements)
[![License](https://img.shields.io/badge/license-ISC-informational)](https://opensource.org/licenses/ISC)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-KTS_8.x-brightgreen?logo=gradle)](https://gradle.org)

<h3>Modern, modular RuneScape 667 emulator written in Kotlin & Java</h3>

<br><br>

</div>

---

## 🗡️ Features

| Category         | Details                                                                 |
|------------------|-------------------------------------------------------------------------|
| Revision         | Full support for RuneScape 667 protocol                                 |
| Modern Codebase  | Written in Kotlin with Gradle KTS                                       |
| Custom Streams   | `InputStream`, `OutputStream`, `Stream` classes for RS-specific packets |
| Cache System     | Integration with `.dat`/`.idx` cache files and indexed store reading    |
| NPC Definitions  | Fully featured NPC parsing with opcode handling and encoding            |
| Sprite Loading   | Supports indexed sprite parsing from archive files                      |
| Logging          | Powered by `kotlin-inline-logger` and Logback                           |
| Extensible       | Easily add or modify content using Kotlin modules                       |

---

## 🚀 Quickstart

> Requires Java 8+ and Gradle 8.x

```bash
git clone https://github.com/Tyluur/RedRune-II.git
cd RedRune-II
./gradlew run
```

### 🔗 Downloads

- [667 Cache (v3) – displee.com](https://displee.com/archive/rs2/667/667v3%20cache.rar)
- [667 Client (Preconfigured) – MEGA.nz](https://mega.nz/file/NNhHSKhb#k6i0yWFX1tYKWSR3Ad7xTlk4uCPFXOk6L7Pr92Mb4wY)

> Extract the cache to your preferred directory and configure your client to load it from there.
