<div align="center">

<h1>RedRune-II</h1>

<a href="https://github.com/your-org/redrune-ii">
  <img src="https://i.imgur.com/X0OdMTf.png" alt="RedRune-II Logo">
</a>

[![Revision](https://img.shields.io/badge/revision-667-important)](https://oldschool.runescape.wiki/w/Update:God_Wars_Instancing_and_Soul_Wars_Improvements)
[![License](https://img.shields.io/badge/license-ISC-informational)](https://opensource.org/licenses/ISC)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-KTS_8.x-brightgreen?logo=gradle)](https://gradle.org)

<h3>Modern, modular RuneScape 667 emulator written in Kotlin & Java</h3>

<a href="#features">Features</a> &nbsp;&bull;&nbsp;
<a href="#quickstart">Quickstart</a> &nbsp;&bull;&nbsp;
<a href="#development">Development</a> &nbsp;&bull;&nbsp;
<a href="#architecture">Architecture</a> &nbsp;&bull;&nbsp;
<a href="#resources">Resources</a> &nbsp;&bull;&nbsp;
<a href="#credits">Credits</a>

<br><br>
<img src="https://i.imgur.com/OZ317on.png" alt="RedRune In-Game Preview">

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

> Ensure Java 8+ and Gradle are installed.

### 1. Clone the repository

```bash
git clone https://github.com/your-org/redrune-ii.git
cd redrune-ii
