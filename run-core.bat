@echo off
setlocal

cd /d "%~dp0"

echo =======================================
echo Starting core-service with docker compose...
docker compose up -d core-service
if errorlevel 1 goto :error

echo core-service is running in the compose project.
exit /b 0

:error
echo Run core-service failed.
exit /b 1
