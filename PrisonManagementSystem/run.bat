@echo off
REM Run the app; if MySQL connector jar exists in lib\ use it, otherwise run demo-mode
setlocal enabledelayedexpansion
set PROJ_DIR=%~dp0
set OUT=%PROJ_DIR%out
set LIB_DIR=%PROJ_DIR%lib
set SRC_DIR=%PROJ_DIR%src
set JAR=
for %%F in ("%LIB_DIR%\mysql-connector-java-*.jar") do (
  set JAR=%%~fF
)
if not defined JAR (
  for %%F in ("%SRC_DIR%\mysql-connector-j-*.jar") do (
    set JAR=%%~fF
  )
)
if not defined JAR (
  for %%F in ("%SRC_DIR%\mysql-connector-java-*.jar") do (
    set JAR=%%~fF
  )
)
if defined JAR (
  echo Using JDBC driver: %JAR%
  java -cp "%OUT%;%JAR%" Main.App
) else (
  echo MySQL connector not found in %LIB_DIR% or %SRC_DIR% - running in demo mode (MockDBManager)
  java -cp "%OUT%" Main.App
)
endlocal
