
@echo off
setlocal

set "MYSQL=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

if not exist "%MYSQL%" (
    echo ERROR: mysql.exe was not found.
    pause
    exit /b 1
)

if "%~1"=="" (
    echo Usage: restore.bat path\to\backup.sql
    pause
    exit /b 1
)

if not exist "%~1" (
    echo ERROR: Backup file not found.
    pause
    exit /b 1
)

echo.
echo Choose the restore target:
echo 1. student_management_test (recommended for testing)
echo 2. student_management (production database)
set /p "CHOICE=Enter 1 or 2: "

if "%CHOICE%"=="1" (
    set "TARGET_DB=student_management_test"
) else if "%CHOICE%"=="2" (
    set "TARGET_DB=student_management"
) else (
    echo Invalid choice. Restore cancelled.
    pause
    exit /b 1
)

echo.
echo WARNING: SQL from the backup will be executed on %TARGET_DB%.
set /p "CONFIRM=Type YES to continue: "

if /I not "%CONFIRM%"=="YES" (
    echo Restore cancelled.
    exit /b 0
)

"%MYSQL%" -u student_app -p "%TARGET_DB%" < "%~1"

if errorlevel 1 (
    echo ERROR: Restore failed. Check the password and database permissions.
    pause
    exit /b 1
)

echo Restore completed successfully.
pause
endlocal
