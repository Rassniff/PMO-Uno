@echo off
set JAVAFX_LIB=%~dp0javafx-sdk-24.0.1\lib
java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -jar uno-game.jar
pause