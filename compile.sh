#!/bin/bash

# JavaFX 21 path
JAVAFX_PATH="/usr/local/javafx-sdk-21/lib"

# Check if JavaFX exists
if [ ! -d "$JAVAFX_PATH" ]; then
    echo "❌ JavaFX 21 not found at $JAVAFX_PATH"
    echo "Please run: sudo mv ~/Downloads/javafx-sdk-21 /usr/local/"
    exit 1
fi

echo "✅ Using JavaFX from: $JAVAFX_PATH"
echo "✅ Java version:"
java -version
echo ""

# Create data directory
mkdir -p data

# Compile all Java files
echo "📦 Compiling Java files..."
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml -d . model/*.java controller/*.java Main.java

# Check if compilation succeeded
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilation successful!"
    echo "🚀 Starting Student Management System..."
    echo ""
    java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml Main
else
    echo ""
    echo "❌ Compilation failed. Please check the errors above."
fi
