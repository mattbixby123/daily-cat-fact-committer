#!/bin/zsh

# Create bin directory
mkdir -p bin

# Compile
javac -cp "lib/*" -d bin src/DailyCatFactCommit.java

# Create run script
cat > run.sh << 'INNEREOF'
#!/bin/zsh
cd "$(dirname "$0")"
java -cp "bin:lib/*" DailyCatFactCommit
INNEREOF

# Make run script executable
chmod +x run.sh

echo "Compilation complete. To test the program, run: ./run.sh"