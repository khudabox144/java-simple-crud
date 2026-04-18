@echo off
set JAVAFX_PATH=C:\javafx-sdk-21\lib

echo ========================================
echo   Student Record Management System
echo ========================================
echo.

echo Java Version:
java -version
echo.

echo Compiling...
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -d . model\*.java controller\*.java Main.java

if %errorlevel% equ 0 (
    echo.
    echo Compilation successful!
    echo Starting application...
    echo.
    java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml Main
) else (
    echo.
    echo Compilation failed!
    pause
)