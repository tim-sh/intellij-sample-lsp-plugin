# LSP-Based Plugin for IntelliJ IDEA

## Overview
This project is a sample LSP-based plugin for IntelliJ IDEA.

## Prerequisites
- IntelliJ IDEA Ultimate or WebStorm
- Node.js in PATH

## Installation and Startup

To clone this project, install dependencies, and start an instance of IntelliJ IDEA with the plugin installed, run the following commands: 

```
git clone https://github.com/tim-sh/intellij-sample-lsp-plugin.git
cd intellij-sample-lsp-plugin/lsp-server
npm install
cd ..
./gradlew runIde
```

## Usage

1. Open a new project in the IDE
2. Create a new file with the extension `.txt`
3. Type `hello world` or any other text in the file
4. The first 5 characters should be marked with a `SAMPLE` warning