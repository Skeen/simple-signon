import java.sql.*;

class MySQLUpdater implements EventSystem.EventListener
{
    // Singleton
    private static MySQLUpdater singleton = new MySQLUpdater();
    public static MySQLUpdater getSingleton()
    {
        return singleton;
    }

    private MySQLUpdater()
    {
        EventSystem eventSystem = EventSystem.getSingleton();
        eventSystem.addListener(EventSystem.ADD_SERVICE_EVENT, this);
        eventSystem.addListener(EventSystem.REMOVE_EVENT, this);
        eventSystem.addListener(EventSystem.EDIT_ACCEPT_EVENT, this);
    }

    private void service_in_use(Service s, boolean state)
    {
        try
        {
            Connection connection = MySQLConnection.getSingleton().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE " + MySQLConnection.DATABASE + ".`user_service` set in_use = " + state + " where user_service_id = " + s.getUserServiceID());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // Event handling
    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.ADD_SERVICE_EVENT:
                // Change the in_use bit, on the database (to true)
                service_in_use((Service) payload, true);
                break;
            case EventSystem.REMOVE_EVENT:
                // Change the in_use bit, on the database (to false)
                service_in_use((Service) payload, false);
                break;
            case EventSystem.EDIT_ACCEPT_EVENT:
                // Update the SQL server, with the;
                // * new username
                // * new password
                // * new autoconnect status
                // TODO: Implement this
                break;
        }
    }
}
