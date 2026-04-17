@echo off
setlocal

cd /d "%~dp0"

echo =======================================
echo Starting auth-service with docker compose...
docker compose up -d auth-service
if errorlevel 1 goto :error

echo auth-service is running in the compose project.
exit /b 0

:error
echo Run auth-service failed.
exit /b 1
