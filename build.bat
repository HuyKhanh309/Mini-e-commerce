@echo off
echo =======================================
echo "Cleaning & installing project..."
call mvn -T 1C clean install || goto :error

echo =======================================
echo Build finished. WAR/JAR file created in target\

echo =======================================
echo Build docker compose...
docker-compose up --build || goto :error

echo =======================================
echo Done! App running at http://localhost:8765
pause
exit /b 0

:error
echo Something went wrong! Check the logs above.
pause
exit /b 1
