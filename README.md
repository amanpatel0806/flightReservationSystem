# Flight Reservation System

This is a Java Swing application for flight reservation with SQLite database backend.

## Features

- User authentication with 3 roles: Customer, Flight Agent, and System Administrator
- Flight search and display
- Booking management (create, modify, cancel reservations)
- Customer management (add, update, view customers)
- Flight management (add, update, delete flights) - Admin only
- Payment simulation
- Booking history

## Architecture

The application follows a 3-layer architecture:

1. **Presentation Layer (Boundary Classes)** - Swing GUI components
2. **Business Logic Layer (Control Classes)** - Controllers handling business logic
3. **Data Layer (Entity + DAO Classes)** - Data access objects and entities

## Prerequisites

- Java 17 or higher
- Maven (for dependency management)
- IntelliJ IDEA (recommended IDE)

## How to Run

### Running in IntelliJ IDEA

1. Open IntelliJ IDEA
2. Select "Open" from the welcome screen or go to File → Open
3. Navigate to the project directory and select the folder containing the `pom.xml` file
4. IntelliJ will automatically detect this as a Maven project and import it

#### First Time Running or Resetting Data

If you're running the application for the first time or want to reset all data:

1. Run the `Main` class located in `src/main/java/org/reservation/Main.java`
   - This will delete the existing database and initialize it with sample data
   - Default users will be created (see "Default Users" section below)

#### Normal Execution (Data Persistence)

If you want to run the application and keep existing data:

1. Run the `LoginView` class located in `src/main/java/org/flightreservation/boundary/LoginView.java`
   - This will preserve existing data in the database
   - You can log in with existing users

### Running from Command Line

#### Building the JAR file

1. Open terminal/command prompt in the project directory
2. Build the project using Maven:
   ```
   mvn clean package
   ```

#### Running the JAR file

After building, the JAR file will be created in the `target` directory.

##### Option 1: Using the provided batch files (Windows only)

The project includes convenient batch files for Windows:

- `run-reset.bat` - Runs the application with data reset (deletes existing data)
- `run-persistent.bat` - Runs the application with data persistence (preserves existing data)
- `run-app.bat` - Interactive script that lets you choose between reset or persistent mode

Simply double-click on the desired batch file to run the application.

##### Option 2: Manual execution

Run the application using one of these commands or use the bat file to run the jar file:

For first time run or data reset:

```
java -cp "target/flightReservationSystem-1.0-SNAPSHOT.jar;C:\Users\basit\.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" org.reservation.Main
```

For normal execution (data persistence):

```
java -cp "target/flightReservationSystem-1.0-SNAPSHOT.jar;C:\Users\basit\.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" org.flightreservation.boundary.LoginView
```

Note: You may need to adjust the path to the SQLite JDBC driver based on your Maven repository location.

## Default Users

- Admin: username `admin`, password `admin123`
- Agent: username `agent`, password `agent123`
- Customer: username `customer`, password `customer123`

## Database

The application uses SQLite and creates a `flight_reservation.db` file in the project directory on first run.

When running the `Main` class, the database is deleted and reinitialized with sample data.
When running the `LoginView` class directly, existing data is preserved.

## Packages

- `org.flightreservation.entity` - Domain entities (POJOs)
- `org.flightreservation.controller` - Business logic controllers
- `org.flightreservation.data` - Data access layer (DAOs and DatabaseManager)
- `org.flightreservation.boundary` - GUI views (Swing components)
- `org.reservation` - Main application entry point
