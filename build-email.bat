@echo off
setlocal

cd /d "%~dp0"

set "IMAGE_NAME=email-service:latest"

echo =======================================
echo Building email-service...
call mvn -T 1C -pl email-service -am clean install || goto :error

echo =======================================
echo Building Docker image %IMAGE_NAME%...
docker build -t %IMAGE_NAME% .\email-service || goto :error

echo =======================================
echo Build completed:
echo email-service\target\email-service-0.0.1-SNAPSHOT.jar
echo Docker image: %IMAGE_NAME%
exit /b 0

:error
echo Build email-service failed.
exit /b 1
