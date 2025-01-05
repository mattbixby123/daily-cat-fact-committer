# Daily Cat Fact Committer

An automated Java application that fetches a random cat fact daily and commits it to this repository. The project uses the [Cat Facts API](https://catfact.ninja/) to generate a new markdown file each day containing an interesting fact about cats.

## Features

- Automatically fetches a random cat fact daily at 9:00 AM
- Creates a markdown file with the fact and date
- Automatically commits and pushes to the repository
- Uses system wake scheduling to ensure reliable execution

## Prerequisites

- Java Development Kit (JDK)
- Git installed and configured
- macOS (for pmset scheduling)
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

The project uses cron and pmset for automated execution:

1. Set up cron job to run at 9:00 AM daily:
   ```bash
   crontab -e
   # Add the following line:
   0 9 * * * cd /Users/[username]/Documents/Coding/IntelliJ-IDEA/daily-cat-fact-committer && ./run.sh >> /Users/[username]/Documents/Coding/IntelliJ-IDEA/daily-cat-fact-committer/cron.log 2>&1
   ```

2. Schedule system wake at 8:55 AM daily:
   ```bash
   sudo pmset repeat wake MTWRFSU 8:55:00
   ```

3. Verify scheduled wake times:
   ```bash
   pmset -g sched
   ```

## System Requirements

- macOS with power management capabilities
- System must be:
    - Plugged into power
    - Not manually put to sleep or shut down
    - Internet-connected

## Logging

The application logs its output to `cron.log` in the project directory. You can monitor the logs using:
```bash
tail -f cron.log
```

## Maintenance Commands

### Updating Wake Schedule
```bash
# View current schedule
pmset -g sched

# Change wake time (e.g., to 7:55 AM)
sudo pmset repeat wake MTWRFSU 7:55:00

# Cancel wake schedule
sudo pmset repeat cancel
```

### Updating Cron Job
```bash
# Edit cron jobs
crontab -e

# View current cron jobs
crontab -l

# Remove all cron jobs
crontab -r
```

### Checking Logs
```bash
# View entire log file
cat cron.log

# View live updates to log
tail -f cron.log

# Clear log file
> cron.log
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