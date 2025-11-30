#!/bin/bash
echo "Running Flight Reservation System with data persistence..."
echo "This will preserve existing data."
echo

java -cp "target/flightReservationSystem-1.0-SNAPSHOT.jar:$HOME/.m2/repository/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar" \
  org.flightreservation.boundary.LoginView

echo
read -n 1 -s -r -p "Press any key to exit..."
echo
