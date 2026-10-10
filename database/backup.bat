
@echo off
setlocal

set "BACKUP_DIR=%~dp0backups"
set "MYSQLDUMP=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldump.exe"
set "DB_USER=student_app"
set "DB_NAME=student_management"
set "TIMESTAMP=%DATE:/=-%_%TIME::=-%"
set "TIMESTAMP=%TIMESTAMP: =_%"

if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

if not exist "%MYSQLDUMP%" (
    echo ERROR: mysqldump.exe was not found.
    pause
    exit /b 1
)

set "BACKUP_FILE=%BACKUP_DIR%\student_management_%TIMESTAMP%.sql"

echo Creating database backup...
"%MYSQLDUMP%" --no-tablespaces -u %DB_USER% -p "%DB_NAME%" > "%BACKUP_FILE%"

if errorlevel 1 (
    echo ERROR: Backup failed.
    del "%BACKUP_FILE%" 2>nul
    pause
    exit /b 1
)

echo Backup completed successfully:
echo "%BACKUP_FILE%"
pause
endlocal
