# AgentOS SDK - Cursor AI Development Guide

**Version:** v0.3.5  
**Release Date:** 2025.7.2

---

## 📋 Overview

This guide helps you integrate AgentOS SDK-specific development rules into **Cursor**. After integration, you can quickly build AgentOS SDK features in Cursor and achieve a revolutionary AI-assisted development experience.

### What is Cursor AI?

**Cursor** is an AI-powered code editor built on VS Code. It is more than a code suggestion tool—it is a complete AI programming assistant. Cursor integrates advanced AI models such as GPT-4o and Claude 4 Sonnet, and can:

- 🤖 **AI code generation**: Generate complete code blocks and functions from natural language
- 🧠 **Intelligent codebase understanding**: Deeply understand your entire project structure and context
- 🔧 **Automated programming tasks**: From simple code completion to complex multi-file refactoring
- 💬 **Conversational programming**: Solve coding problems and optimize code through AI chat
- 🚀 **Agent mode**: AI can autonomously complete end-to-end programming tasks
- 🐛 **Intelligent debugging**: Automatically detect and fix code issues

### Why integrate AgentOS SDK development rules?

By integrating AgentOS SDK-specific Cursor Rules, you can:

- 📚 **Get domain expertise**: AI provides professional suggestions based on AgentOS SDK docs
- 🎯 **Accurate code generation**: Automatically generate code that follows AgentOS SDK conventions
- 🔍 **Intelligent error detection**: Identify common AgentOS SDK usage errors
- 📖 **Instant doc lookup**: Quickly access API docs and samples during development
- 🚀 **Improve efficiency**: Reduce time spent searching docs and debugging

## 🚀 Quick Start

### 1. Preparation

Before you begin, make sure you have:
- Installed the **Cursor AI code editor** ([Download](https://cursor.com))
- Prepared an Android development environment
- Understood basic AgentOS SDK concepts

### 2. Integrate AgentOS SDK development rules

#### Step 1: Download the rule package
1. Download the provided [`cursor-rules-dependencies.zip`](https://github.com/orionagent/agentos-sdk/blob/main/cursor-rules-dependencies.zip)
2. Ensure the download is complete and not corrupted

#### Step 2: Extract and integrate rules
1. Extract the zip file in your Android project root directory
2. You can extract in an existing project, or create a new project in Android Studio and then extract

**The extracted directory structure should include:**
```
your-project/
├── app/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── .gradle/
├── .cursor/          # Cursor development rule configuration
├── Agent/            # AgentOS SDK documentation
├── Robot/            # Robot API documentation
├── README.md         # AgentOS README
└── FAQ.md            # AgentOS FAQ
```

#### Step 3: Handle existing Cursor configuration

**If a `.cursor` folder already exists in your project root:**

⚠️ **Important reminder**: To avoid overwriting existing config, do this manually:

1. Open the extracted `.cursor/rules/` folder
2. Copy `doc-devguide.mdc` into your existing `.cursor/rules/` folder
3. Directly overwriting the `.cursor` folder may cause loss of your other Cursor Rules configurations

#### Step 4: Verify rule integration

1. Open your project with Cursor
2. Go to Cursor settings: `Settings` → `Rules` → `Project rules`
3. Confirm `doc-devguide.mdc` is active and appears in the rules list

## 🔧 Troubleshooting

**Q: What if the development rules do not take effect?**
A: Check the following:
- Whether `.cursor/rules/doc-devguide.mdc` exists
- Restart the Cursor editor
- Confirm that the project root directory structure is correct

---

*Last updated: July 2, 2025*
