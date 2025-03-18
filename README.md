# Daily Cat Fact Committer

An automated Java application that fetches a random cat fact daily and commits it to this repository. The project uses the [Cat Facts API](https://catfact.ninja/) to generate a new markdown file each day containing an interesting fact about cats.

## Features

- Automatically fetches a random cat fact daily (scheduled for 11:40am on my device)
- Creates a markdown file with the fact and date
- Automatically commits and pushes to the repository
- Uses macOS launchd service for reliable scheduling

## Prerequisites

- Java Development Kit (JDK)
- Git installed and configured
- macOS
- Required Java libraries:
    - Jackson (JSON parsing)

## Project Structure

```
daily-cat-fact-committer/
├── bin/                    # Compiled .class files
├── lib/                    # JAR dependencies
├── src/                    # Source code
│   └── DailyCatFactCommit.java
├── build.sh               # Build script
└── run.sh                 # Run script
```

## Setup

1. Clone the repository:
   ```bash
   git clone git@github.com:mattbixby123/daily-cat-fact-committer.git
   cd daily-cat-fact-committer
   ```

2. Make the build script executable:
   ```bash
   chmod +x build.sh
   ```

3. Build the project:
   ```bash
   ./build.sh
   ```

## Automation Setup

The project uses launchd for automated execution:

1. Create a launch agent plist file:
   ```bash
   mkdir -p ~/Library/LaunchAgents
   nano ~/Library/LaunchAgents/com.user.catfact.plist
   ```

2. Add the following content to the plist file (adjust paths as needed):
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
   <plist version="1.0">
   <dict>
       <key>Label</key>
       <string>com.user.dailycatfact</string>
       <key>ProgramArguments</key>
       <array>
           <string>/bin/bash</string>
           <string>/Users/[username]/Documents/Coding/IntelliJ-IDEA/daily-cat-fact-committer/run.sh</string>
       </array>
       <key>StartCalendarInterval</key>
       <dict>
           <key>Hour</key>
           <integer>9</integer>
           <key>Minute</key>
           <integer>0</integer>
       </dict>
       <key>StandardOutPath</key>
       <string>/Users/[username]/Documents/Coding/IntelliJ-IDEA/daily-cat-fact-committer/launch.log</string>
       <key>StandardErrorPath</key>
       <string>/Users/[username]/Documents/Coding/IntelliJ-IDEA/daily-cat-fact-committer/launch.log</string>
   </dict>
   </plist>
   ```

3. Load the launch agent:
   ```bash
   launchctl load ~/Library/LaunchAgents/com.user.catfact.plist
   ```

## System Requirements

- macOS
- System must be:
    - Internet-connected
    - Not shut down (sleep is okay - launchd will wake the system)

## Logging

The application logs its output to `launch.log` in the project directory. You can monitor the logs using:
```bash
tail -f launch.log
```

### Maintenance Commands / Managing Launch Agent

```bash
# Unload the launch agent
unload ~/Library/LaunchAgents/com.mattbixby.catfact.plist


# Load the launch agent
launchctl load -w ~/Library/LaunchAgents/com.mattbixby.catfact.plist

# Check if the launch agent is running
launchctl list | grep com.mattbixby.catfact
```

### If the plist is 'missing' try this command
```bash
ls -la ~/Library/LaunchAgents/ 
```
### the ~/Library folder is hidden by default in macOS. That's why you can access it via Terminal but don't see it in Finder.
### Here's how to access this hidden folder - In Finder:
###### Click on the "Go" menu in the Finder menu bar
###### Hold down the Option (⌥) key, and you'll see "Library" appear in the dropdown menu
###### Click on "Library" while still holding the Option key
###### Navigate to the "LaunchAgents" folder inside
### Alternatively, you can jump directly there:
###### In Finder, press Command+Shift+G (⌘+⇧+G)
###### Type ~/Library/LaunchAgents
###### Click "Go"
### Or make hidden files visible permanently:
###### Open Terminal
###### Run: defaults write com.apple.finder AppleShowAllFiles TRUE
###### Then restart Finder: killall Finder


### Checking Logs

```bash
# View entire log file
cat launch.log

# View live updates to log
tail -f launch.log

# Clear log file
> launch.log
```

### Git Repository
```bash
# Update repository URL (if needed)
git remote set-url origin new_url_here

# View current remote URL
git remote -v
```

## Credits

- Cat facts provided by [Cat Facts API](https://catfact.ninja/)
- Built using Java and the Jackson library for JSON parsi