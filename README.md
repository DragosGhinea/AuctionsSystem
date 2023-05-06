# AuctionsSystem | Stage 2

## JDBC & HikariCP

In this stage we had to store persistent application data, which was done
via a JDBC connection to a PostgreSQL database stored in a Docker container.
The database connection was enhanced by using HikariCP
to provide an efficient connection pool.

## Database Diagram

![DatabaseDiagram](https://github.com/DragosGhinea/AuctionsSystem/blob/Stage2/DatabaseDiagram.svg)

## Audit Service | CSV storage

We also had to implement an audit system for the interrogations
created in the first stage. I have done it by using Log4j2 with a
customized layout of AbstractCsvLayout, inspired by CsvLogEventLayout.

The fields that compose a log are:
 - action - a general type of action that can happen, chosen from an enum
 - details - readable extra information about the action
 - byWho - who or what triggers the log event
 - logType - classic INFO/WARN/ERROR/etc log type
 - timestamp - when did the event occur



