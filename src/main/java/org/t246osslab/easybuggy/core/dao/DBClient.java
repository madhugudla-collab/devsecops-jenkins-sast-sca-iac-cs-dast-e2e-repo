package org.t246osslab.easybuggy.core.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.ApplicationUtils;
import org.t246osslab.easybuggy.core.utils.Closer;

/**
 * Database client to provide database connections.
 */
public final class DBClient {

    private static final Logger log = LoggerFactory.getLogger(DBClient.class);
    private static final String FALSE_VALUE = "'false', '', ''"; // SECURITY FIX: Define constant for repeated literal
    private static final String TRUE_VALUE = "'true', '', ''"; // SECURITY FIX: Define constant for repeated literal

    static {
        Statement stmt = null;
        Connection conn= null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // create a user table and insert sample users
            createUsersTable(stmt);

        } catch (SQLException e) {
            log.error("SQLException occurs: ", e);
        } finally {
            Closer.close(stmt);
            Closer.close(conn);
        }
    }

    // squid:S1118: Utility classes should not have public constructors
    private DBClient() {
        throw new IllegalAccessError("This class should not be instantiated.");
    }
    
    /**
     * Returns a database connection to connect a database.
     * 
     * @return A database connection
     */
    public static Connection getConnection() throws SQLException {
        final String dbDriver = ApplicationUtils.getDatabaseDriver();
        final String dbUrl = ApplicationUtils.getDatabaseURL();
        if (!StringUtils.isBlank(dbDriver)) {
            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException occurs: ", e);
            }
        }
        return DriverManager.getConnection(dbUrl);
    }
    
    private static void createUsersTable(Statement stmt) throws SQLException {
        try {
            stmt.executeUpdate("drop table users");
        } catch (SQLException e) {
            // ignore exception if existing the table
            log.debug("SQLException occurs: ", e);
        }
        // create users table
        stmt.executeUpdate("create table users (id varchar(10) primary key, name varchar(30), password varchar(30), " +
                "secret varchar(100), ispublic varchar(5), phone varchar(20), mail varchar(100))");

        // insert private (invisible) user records
        insertUser(stmt, "admin", "admin", "password", RandomStringUtils.randomNumeric(10), FALSE_VALUE);
        insertUser(stmt, "admin02", "admin02", "pas2w0rd", RandomStringUtils.randomNumeric(10), FALSE_VALUE);
        insertUser(stmt, "admin03", "admin03", "pa33word", RandomStringUtils.randomNumeric(10), FALSE_VALUE);
        insertUser(stmt, "admin04", "admin04", "pathwood", RandomStringUtils.randomNumeric(10), FALSE_VALUE);
        
        // insert public (test) user records
        insertUser(stmt, "user00", "Mark", "password", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user01", "David", "pa32w0rd", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user02", "Peter", "pa23word", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user03", "James", "patwired", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user04", "Benjamin", "password", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user05", "Eric", "pas2w0rd", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user06", "Sharon", "pa3world", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user07", "Pamela", "pathwood", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user08", "Jacqueline", "password", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
        insertUser(stmt, "user09", "Michelle", "pas2w0rd", RandomStringUtils.randomNumeric(10), TRUE_VALUE);
    }

    private static void insertUser(Statement stmt, String id, String name, String password, String secret, String isPublic) throws SQLException { // SECURITY FIX: Use method for repeated insert logic
        String query = "insert into users values (?, ?, ?, ?, " + isPublic + ")"; // SECURITY FIX: Use PreparedStatement to prevent SQL injection
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) { // SECURITY FIX: Use PreparedStatement
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, secret);
            pstmt.executeUpdate();
        }
    }
}