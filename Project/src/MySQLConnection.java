import java.sql.*;

import java.util.concurrent.Executor;

// TODO: All sql queries, should use prepared statements!

// Singleton, for the single MySQLConnection, that we're going to maintain
class MySQLConnection implements EventSystem.EventListener
{
    private class MySQLExecutor implements Executor
    {
        public void execute(Runnable r) 
        {
            new Thread(r).start();
        }
    }

    // Server parameters
    private static final String SERVER = "localhost";
    public static final String DATABASE = "services";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection = null;
    public Connection getConnection()
    {
        return connection;
    }

    private static MySQLConnection singleton = new MySQLConnection();
    public static MySQLConnection getSingleton()
    {
        return singleton;
    }

    private MySQLConnection()
    {
        EventSystem eventSystem = EventSystem.getSingleton();
        eventSystem.addListener(EventSystem.LOGOUT_EVENT, this);
        eventSystem.addListener(EventSystem.LOGIN_EVENT, this);
    }

    private void killConnection()
    {
        try
        {
            connection.abort(new MySQLExecutor());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        connection = null;
    }

    private void startConnection()
    {
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://" + SERVER + "/" + DATABASE, USERNAME, PASSWORD);

        }
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("An error occurred: " + e);
            CentralErrorStation.mysql_connection_error();
        }
    }

    // Event handling
    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.LOGOUT_EVENT:
                // Terminate our SQL database connection
                killConnection();
                break;
            case EventSystem.LOGIN_EVENT:
                String[] strings = (String[]) payload;
                String username = strings[0];
                String password = strings[1];
                // Open our SQL database connection
                startConnection();
                break;
        }
    }
}
