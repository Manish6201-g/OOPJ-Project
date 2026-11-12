# CampusConnect – Campus Lost & Found Management System

A Java 25 Swing + JDBC + MySQL desktop application demonstrating OOP, interfaces, inheritance, exception handling, and database connectivity.

## Features
- Student registration and login (PBKDF2 password hashing for new registrations).
- Legacy plain-text demo passwords are upgraded to PBKDF2 automatically on successful login.
- Report Lost and Found items; reports are linked to the logged-in student.
- Browse/search items by title, category, location and type.
- Submit claims for open Found items reported by another student.
- My Claims screen for students.
- Admin claim review with approve/reject actions. Approval marks the item as CLAIMED.
- Dashboard counts and refresh; logout clears the in-memory session.

## Requirements
- JDK 25+
- Maven 3.8+
- MySQL 8+

## Database setup
1. Open MySQL Workbench and run `sql/schema.sql`.
2. If you already have the `campus_lost_found` database and its three tables, keep your existing data; do not drop the database.
3. Ensure MySQL user `campus_app` has privileges on `campus_lost_found`.
4. Create or update an ADMIN account by changing an existing trusted user's role in Workbench, for example:

```sql
USE campus_lost_found;
UPDATE users SET role='ADMIN' WHERE email='your-admin-email@example.com';
```

Do not make public student registration create admin accounts.

## Configure database credentials (macOS zsh)
The application deliberately does not store a database password in source code. In the VS Code terminal, run:

```bash
export CAMPUS_DB_USER='campus_app'
export CAMPUS_DB_PASSWORD='YOUR_MYSQL_PASSWORD'
```

Optional URL override:

```bash
export CAMPUS_DB_URL='jdbc:mysql://localhost:3306/campus_lost_found?serverTimezone=UTC'
```

Then run from the project root:

```bash
mvn clean compile exec:java
```

The environment variables remain available in that terminal session. If you open a new terminal, set them again. Never commit your real password.

## Existing database compatibility
- Existing tables from the earlier project schema are compatible.
- Existing demo users with plain-text passwords can still log in once; their password is then upgraded to PBKDF2 format automatically.
- Newly registered users are stored with PBKDF2 hashes.
- Existing user passwords should be changed if they were exposed in a shared ZIP or chat.

## Important claim rules
- Only an OPEN item of type FOUND can be claimed.
- A student cannot claim their own found-item report.
- Duplicate pending claims for the same user/item are blocked.
- Admin approval marks the item status as CLAIMED; rejection leaves the item OPEN.

## Project packages
- `model`: User, Item, LostItem, FoundItem, Claim
- `interfaces`: Searchable
- `dao`: JDBC database access
- `service`: validation, session, password hashing
- `ui`: Swing screens
- `exceptions`: custom validation exception

## Notes
This is a college demonstration project. For a production deployment, add stronger authorization, audit logging, rate limiting, password reset, and more detailed transactional concurrency controls.
