package com.vv.game.utils;

import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;

/**
 * The Database class uses the singleton pattern. It is used by any class that needs to store or retrieve data.
 */
public class Database {
    //TODO Change name,url,user,password
    private static final String DATABASE_NAME = "username";
    private static final String DATABASE_URL = "localhost";
    private static final String DATABASE_USER = "username";
    private static final String DATABASE_PASSWORD = "password";
    private static final String DATABASE_DRIVER = "org.postgresql.Driver";
    private Connection connection;
    private static final Database instance = new Database();


    private Database(){
        try {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection("jdbc:postgresql://" + DATABASE_URL + ":5432/"
                    + DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD);
            if (connection != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to get the single instance of Database.
     * @return Database
     */
    public static Database getInstance() { return instance; }

    /**
     * This method is used to determine if the database is connected.
     * @return true if connected
     */
    public boolean isConnected(){ return connection != null; }

    /**
     * This method is used to check that a user's input for username is not already taken.
     * @param user
     * @return true if the username is taken. false if it is not.
     */
    public boolean checkUsernameTaken(User user){
        boolean usernameTaken = false;

        Statement statement;
        try {
            String query = "SELECT id FROM vidar_voyager.users WHERE username = '" + user.getUsername() + "';";

            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if(result.next()){
                usernameTaken = true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return usernameTaken;
    }

    /**
     * This method insert a user into the users table.
     * @param user stores the information that user entered
     */
    public boolean insertUser(User user) {
        int returnValue = 0;
        Statement statement;

        String query = "INSERT INTO vidar_voyager.users"
                + "(username, first_name, last_name, ";
        if (!user.getMiddleInitial().isEmpty()) {
            query += "middle_initial,";
        }
        if (!user.getDateOfBirth().isEmpty()) {
            query += "date_of_birth,";
        }
        query += "email, created_on, updated_on)"
                + "VALUES ("
                + "'" + user.getUsername() + "',"
                + "'" + user.getFirstName() + "',"
                + "'" + user.getLastName() + "',";
        if (!user.getMiddleInitial().isEmpty()) {
            query += "'" + user.getMiddleInitial() + "',";
        }
        if (!user.getDateOfBirth().isEmpty()) {
            query += "'" + user.getDateOfBirth() + "',";
        }
        query += "'" + user.getEmail() + "',"
                + " current_timestamp,"
                + " current_timestamp"
                + ");"
        ;

        try {
            statement = connection.createStatement();
            returnValue = statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e);
        }

        //ADD USER TO ACCOUNTS TABLE
        if (returnValue != 0) {
            String query2 = "INSERT INTO vidar_voyager.accounts (user_id, password) VALUES ("
                    + "(SELECT id FROM vidar_voyager.users WHERE username = '" + user.getUsername() + "'),"
                    + "crypt('" + user.getPassword() + "', gen_salt('bf')));";
            try {
                statement = connection.createStatement();
                returnValue = statement.executeUpdate(query2);
            } catch (Exception e) {
                returnValue = 0;
                System.out.println(e);
            }
        }
        return returnValue != 0;
    }

    /**
     * This method checks the user entered username and password. It returns true if the username and password were
     * correct.
     * @param user username and password are stored in user
     * @return true if password is correct
     */
    public boolean checkUserPassword(User user) throws SQLException {
        boolean returnValue = false;
        Statement statement;
        try {
            String query = "SELECT username FROM vidar_voyager.users u "
                    + "RIGHT JOIN vidar_voyager.accounts a ON u.id = a.user_id WHERE "
                    + "u.username = '" + user.getUsername()
                    + "' AND a.password = crypt('" + user.getPassword() + "', a.password);";

            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if(result.next()){
                String username = result.getString("username");
                if(username.equals(user.getUsername())){
                    returnValue = true;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return returnValue;
    }

    /**
     * This method is used to created a login event when a user logs in.
     * @param user
     * @return true if it was successful. false if it was not
     */
    public boolean createLogInEvent(User user) {
        int returnValue = 0;
        String query = "INSERT INTO vidar_voyager.login_events (user_id, logged_in_on, logged_out_on) VALUES ("
                + "(SELECT id FROM vidar_voyager.users WHERE username = '" + user.getUsername() + "'),"
                + "current_timestamp, current_timestamp);";
        Statement statement;
        try {
            statement = connection.createStatement();
            returnValue = statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e);
        }

        //store id of login event so that the logout time can be updated.
        String query2 = "SELECT id FROM vidar_voyager.login_events WHERE user_id = "
                + "(SELECT id FROM vidar_voyager.users WHERE username = '" + user.getUsername() + "')"
                + "ORDER BY logged_in_on DESC\n" +
                "LIMIT 1;";
        ResultSet result = null;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query2);

            if(result.next()){
                user.setLog_in_on_id(result.getInt("id"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return returnValue != 0;
    }

    /**
     * This method updates the login event when the player logs out.
     * @param user
     * @return true if it was successful. false if it was not.
     */
    public boolean updateLogInEvent(User user) {
        int returnValue = 0;
        String query = "UPDATE vidar_voyager.login_events SET logged_out_on = current_timestamp"
                + " WHERE user_id = (SELECT id FROM vidar_voyager.users WHERE username = '"
                + user.getUsername() + "') AND id = '"
                + user.getLog_in_on_id() + "';";

        Statement statement;
        try {
            statement = connection.createStatement();
            returnValue = statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e);
        }

        return returnValue != 0;
    }

    /**
     * This method adds a minesweeper score to the high scores table
     * @param user current user
     * @param seconds the time it took the user to complete minesweeper
     */
    public void addHighScoreMineSweeper(User user, int seconds){
        int returnValue = 0;
        String query = "INSERT INTO vidar_voyager.high_scores_minesweeper (user_id, seconds, created_on) VALUES ("
                + "(SELECT id FROM vidar_voyager.users WHERE username = '" + user.getUsername() + "'),"
                + "'" + seconds + "',"
                + "current_timestamp);";
        Statement statement;
        try {
            statement = connection.createStatement();
            returnValue = statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e + " " + returnValue + " tables returned");
        }
    }


}
