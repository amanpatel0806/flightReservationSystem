@echo off
echo Running Flight Reservation System with data persistence...
echo This will preserve existing data.
echo.
java -cp "target/flightReservationSystem-1.0-SNAPSHOT.jar;C:\Users\basit\.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" org.flightreservation.boundary.LoginView
echo.
echo Press any key to exit...
pause >nul