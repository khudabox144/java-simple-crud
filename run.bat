@echo off
set JAVAFX_PATH=C:\javafx-sdk-21\lib

echo Compiling...
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -d . model\*.java controller\*.java Main.java

if %errorlevel% equ 0 (
    echo Running...
    java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml Main
) else (
    echo Compilation failed!
    pause
)