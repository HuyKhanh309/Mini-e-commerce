@echo off
setlocal

cd /d "%~dp0"

set "IMAGE_NAME=core-service:latest"

echo =======================================
echo Building core-service...
call mvn -T 1C -pl core-service -am clean install || goto :error

echo =======================================
echo Building Docker image %IMAGE_NAME%...
docker build -t %IMAGE_NAME% .\core-service || goto :error

echo =======================================
echo Build completed:
echo core-service\target\core-service-1.0.0.jar
echo Docker image: %IMAGE_NAME%
exit /b 0

:error
echo Build core-service failed.
exit /b 1
