# Daily Cat Fact Committer

An automated Java application that fetches a random cat fact daily and commits it to this repository. The project uses the [Cat Facts API](https://catfact.ninja/) to generate a new markdown file each day containing an interesting fact about cats.

## Features

- Automatically fetches a random cat fact daily at 9:00 AM
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
   nano ~/Library/LaunchAgents/com.user.dailycatfact.plist
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
   launchctl load ~/Library/LaunchAgents/com.user.dailycatfact.plist
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

## Maintenance Commands

### Managing Launch Agent
```bash
# Load the launch agent
launchctl load ~/Library/LaunchAgents/com.user.dailycatfact.plist

# Unload the launch agent
launchctl unload ~/Library/LaunchAgents/com.user.dailycatfact.plist

# Check if the launch agent is running
launchctl list | grep dailycatfact

# Start the job manually (for testing)
launchctl start com.user.dailycatfact
```

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
- Built using Java and the Jackson library for JSON parsing