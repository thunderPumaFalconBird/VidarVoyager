package com.vv.game.utils;

import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;

/**
 * The Database class uses the singleton pattern. It is used by any class that needs to store or retrieve data.
 * Prepared statements are used to protect against SQL injections.
 */
public class DatabaseInterface {
    //TODO Change name,url,user,password

    private static final String DATABASE_NAME = "DatabaseName";
    private static final String DATABASE_URL = "localhost";
    private static final String DATABASE_USER = "userName";
    private static final String DATABASE_PASSWORD = "password";
    private static final String DATABASE_DRIVER = "org.postgresql.Driver";
    private Connection connection;
    private static final DatabaseInterface instance = new DatabaseInterface();


    private DatabaseInterface(){
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
    public static DatabaseInterface getInstance() { return instance; }

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

        try {
            String query = "SELECT id FROM vidar_voyager.users WHERE username = ?;";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1,user.getUsername());

            ResultSet result = statement.executeQuery();

            if(result.next()){
                usernameTaken = true;
            }

        } catch (Exception e) {
            System.out.println("checkUsernameTaken Failed");
            System.out.println(e);
        }
        return usernameTaken;
    }

    /**
     * This method insert a user into the users table.
     * @param user stores the information that user entered
     */
    public boolean insertUser(User user) {
        int rowsAffected = 0;

        String query = "INSERT INTO vidar_voyager.users"
                + "(username, first_name, last_name, ";
        if (!user.getMiddleInitial().isEmpty()) {
            query += "middle_initial,";
        }
        if (!user.getDateOfBirth().isEmpty()) {
            query += "date_of_birth,";
        }
        query += "email, created_on, updated_on)"
                + "VALUES (?,?,?,";
        if (!user.getMiddleInitial().isEmpty()) {
            query += "?,";
        }
        if (!user.getDateOfBirth().isEmpty()) {
            query += "?,";
        }
        query += "?, current_timestamp, current_timestamp);";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            if(!user.getMiddleInitial().isEmpty()){
                statement.setString(4, user.getMiddleInitial());
                if(!user.getDateOfBirth().isEmpty()){
                    statement.setDate(5, Date.valueOf(user.getDateOfBirth()));
                    statement.setString(6, user.getEmail());
                }
            }
            else if(user.getMiddleInitial().isEmpty() && !user.getDateOfBirth().isEmpty()){
                statement.setDate(4, Date.valueOf(user.getDateOfBirth()));
                statement.setString(5, user.getEmail());
            }
            else {
                statement.setString(4, user.getEmail());
            }

            rowsAffected = statement.executeUpdate();

        } catch (Exception e){
            System.out.println(e);
        }

        String query2 = "INSERT INTO vidar_voyager.accounts (user_id, password) VALUES ("
                + "(SELECT id FROM vidar_voyager.users WHERE username = ?),"
                + "crypt(?, gen_salt('bf')));";

        try{
            PreparedStatement statement2 = connection.prepareStatement(query2);

            statement2.setString(1, user.getUsername());
            statement2.setString(2, user.getPassword());

            rowsAffected = statement2.executeUpdate();

        } catch (Exception e){
            System.out.println("InsertUser Failed");
            System.out.println(e);
        }

        return rowsAffected != 0;
    }

    /**
     * This method checks the user entered username and password. It returns true if the username and password were
     * correct.
     * @param user username and password are stored in user
     * @return true if password is correct
     */
    public boolean checkUserPassword(User user) throws SQLException {
        boolean returnValue = false;

        String query = "SELECT username FROM vidar_voyager.users u "
                + "RIGHT JOIN vidar_voyager.accounts a ON u.id = a.user_id WHERE "
                + "u.username = ? AND a.password = crypt(?, a.password);";

        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());

        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()){
            String username = resultSet.getString("username");
            if(username.equals(user.getUsername())){
                returnValue = true;
            }
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

        String query = "INSERT INTO vidar_voyager.login_events (user_id, ip_address, logged_in_on, logged_out_on) VALUES ("
                + "(SELECT id FROM vidar_voyager.users WHERE username = ?), CAST(? AS inet),current_timestamp, current_timestamp);";

        String query2 = "SELECT id FROM vidar_voyager.login_events WHERE user_id = "
                + "(SELECT id FROM vidar_voyager.users WHERE username = ?)"
                + "ORDER BY logged_in_on DESC\n" +
                "LIMIT 1;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getInet());

            returnValue = statement.executeUpdate();
            if(returnValue != 0){
                PreparedStatement statement2 = connection.prepareStatement(query2);
                statement2.setString(1, user.getUsername());

                ResultSet resultSet = statement2.executeQuery();
                if(resultSet.next()){
                    user.setLog_in_on_id(resultSet.getInt("id"));
                }
            }

        } catch (Exception e){
            System.out.println("createLogInEvent Failed");
            System.out.println(e);
        }

        return returnValue != 0;
    }

    public boolean createLogInAttempt(User user) {
        int returnValue = 0;
        boolean isActiveUser = checkUsernameTaken(user);

        String query = "INSERT INTO vidar_voyager.login_attempt (";
        if (isActiveUser) {
            query += "user_id,";
        }
        query += "ip_address, login_attempt_on) VALUES (";
        if(isActiveUser){
            query +=  "(SELECT id FROM vidar_voyager.users WHERE username = ?),";
        }
        query += "CAST(? AS inet), current_timestamp);";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            if (isActiveUser){
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getInet());
            }
            else{
                statement.setString(1, user.getInet());
            }

            returnValue = statement.executeUpdate();

        } catch (Exception e){
            System.out.println(e);
        }

        return returnValue != 0;
    }

    /**
     * This method updates the logged_out_on attribute of the login event when the player logs out.
     * @param user
     * @return true if it was successful. false if it was not.
     */
    public boolean updateLogInEvent(User user) {
        int returnValue = 0;

        String query = "UPDATE vidar_voyager.login_events SET logged_out_on = current_timestamp"
                + " WHERE user_id = (SELECT id FROM vidar_voyager.users WHERE username = ?) AND id = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getUsername());
            statement.setInt(2, user.getLog_in_on_id());

            returnValue = statement.executeUpdate();

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
                + "(SELECT id FROM vidar_voyager.users WHERE username = ?),?,current_timestamp);";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getUsername());
            statement.setInt(2, seconds);

            returnValue = statement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e + " " + returnValue + " tables returned");
        }
    }

    public void dispose(){
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
