@echo off
echo Starting Flight Reservation System...
echo.
echo Options:
echo 1. Run with data reset (first time or to reset data)
echo 2. Run with data persistence (normal operation)
echo.
echo Please select an option (1 or 2):

choice /c 12 /m "Select option"

if errorlevel 2 goto run_with_persistence
if errorlevel 1 goto run_with_reset

:run_with_reset
echo.
echo Running with data reset...
java -cp "target/flightReservationSystem-1.0-SNAPSHOT.jar;C:\Users\basit\.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" org.reservation.Main
goto end

:run_with_persistence
echo.
echo Running with data persistence...
java -cp "target/flightReservationSystem-1.0-SNAPSHOT.jar;C:\Users\basit\.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" org.flightreservation.boundary.LoginView
goto end

:end
echo.
echo Press any key to exit...
pause >nul