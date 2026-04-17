@echo off
setlocal

cd /d "%~dp0"

set "IMAGE_NAME=auth-service:latest"

echo =======================================
echo Building auth-service...
call mvn -T 1C -pl auth-service -am clean install || goto :error

echo =======================================
echo Building Docker image %IMAGE_NAME%...
docker build -t %IMAGE_NAME% .\auth-service || goto :error

echo =======================================
echo Build completed:
echo auth-service\target\auth-service-1.0.0.jar
echo Docker image: %IMAGE_NAME%
exit /b 0

:error
echo Build auth-service failed.
exit /b 1
