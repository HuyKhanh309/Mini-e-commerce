@echo off
setlocal

cd /d "%~dp0"

echo =======================================
echo Starting email-service with docker compose...
docker compose up -d email-service
if errorlevel 1 goto :error

echo email-service is running in the compose project.
exit /b 0

:error
echo Run email-service failed.
exit /b 1
