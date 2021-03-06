# BBL Organizer

## Prerequisites

* Install [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/)

* Install [Maven](http://maven.apache.org/)

* Install [node.js](http://nodejs.org)

* Install [Grunt](http://gruntjs.com)

* Install [Bower](http://bower.io/) (Optional)

* Install [Ruby](https://www.ruby-lang.org/en/) & [Compass](http://compass-style.org/) (Optional)

## Installation

```bash
cd bblorganizer-web/
npm install
```

Import the project in Eclipse ("Existing Maven project").
Go to Servers view & create a new Tomcat 7 server.
Configure it to "Use Tomcat installation".
Edit the `context.xml` of the Tomcat server in Eclipse and add this :

```xml
    <Resource name="jdbc/BBLOrganizer" auth="Container" type="javax.sql.DataSource"
              maxActive="100" maxIdle="30" maxWait="10000"
              driverClassName="org.h2.jdbcx.JdbcDataSource"
              username="sa" password=""
              url="jdbc:h2:/tmp/BBLOrganizer;AUTO_SERVER=TRUE" />
```

Deploy [h2-1.4.178.jar](http://search.maven.org/remotecontent?filepath=com/h2database/h2/1.4.178/h2-1.4.178.jar) in the Tomcat `lib/`

## Development

```bash
cd bblorganizer-web/
grunt serve
```

Develop in `bblorganizer-web/src/main/filtered-webapp`.

## Database

Because IRAJ Blank uses an in-memory embedded database engine ([h2](http://h2database.com)), it requires a JDBC driver to deal with it. It is available [here](http://h2database.com/html/download.html).

If you use [Tomcat](http://tomcat.apache.org/), you just have to put it in the TOMCAT_DIR/lib.

You'll have to define the DataSource `jdbc/IrajBlank`. If you use Tomcat, this is done by adding this in your `context.xml` :

```xml
    <Resource name="jdbc/IrajBlank" auth="Container" type="javax.sql.DataSource"
              maxActive="100" maxIdle="30" maxWait="10000"
              username="sa" password="" driverClassName="org.h2.jdbcx.JdbcDataSource"
              url="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"/>
```

## Build

Deploy irajblank-web on your favorite Java application server (tested with Tomcat 7).

Execute `npm install` to download front-end dependencies and run one of these two configurations :

### Development workflow

`grunt serve`

Grunt will download front-end dependencies, build the files and track your changes.

### Production workflow

`grunt build`

Grunt will download front-end dependencies, and minify all your files automatically.
