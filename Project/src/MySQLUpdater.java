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
            service_in_use_worker(s, state);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void service_in_use_worker(Service s, boolean state)
        throws Exception
    {
        String databaseString = MySQLConnection.DATABASE + ".`user_service`";
        String updateString =   "UPDATE " + databaseString + " set in_use = ? where user_service_id = ?";

        Connection connection = MySQLConnection.getSingleton().getConnection();
        PreparedStatement statement = connection.prepareStatement(updateString);

        statement.setBoolean(1, state);
        statement.setInt(2, s.getUserServiceID());
        
        statement.execute();
    }

    private int get_indirection_id_from_user_service_id(int user_service_id)
    {
        try
        {
            String databaseString = MySQLConnection.DATABASE + ".`service_indirection`";
            String updateString =   "SELECT idservice_indirection FROM " + databaseString + " where user_service_id = ?";

            Connection connection = MySQLConnection.getSingleton().getConnection();
            PreparedStatement statement = connection.prepareStatement(updateString);

            statement.setInt(1, user_service_id);

            ResultSet r = statement.executeQuery();

            Integer value = null;
            while(r.next())
            {
                // Check for return of multiple rows
                if(value != null)
                {
                    System.err.println("CRITICAL ISSUE AT MYSQL UPDATER");
                    System.exit(1);
                }
                value = r.getInt("idservice_indirection");
            }
            if(value == null)
            {
                System.err.println("CRITICAL ISSUE2 AT MYSQL UPDATER");
                System.exit(1);
            }
            return (int) value;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    private void update_key_value_info(int indirection_id, String key, String value)
        throws Exception
    {
        String databaseString = MySQLConnection.DATABASE + ".`key_value`";
        String updateString =   "INSERT INTO " + databaseString + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE value_entry = ?";

        Connection connection = MySQLConnection.getSingleton().getConnection();
        PreparedStatement statement = connection.prepareStatement(updateString);

        statement.setInt(1, indirection_id);
        statement.setString(2, key);
        statement.setString(3, value);
        statement.setString(4, value);
        
        statement.execute();
    }

    private void service_autoconnect(Service s, boolean state)
        throws Exception
    {
        String databaseString = MySQLConnection.DATABASE + ".`user_service`";
        String updateString =   "UPDATE " + databaseString + " set auto_connect = ? where user_service_id = ?";

        Connection connection = MySQLConnection.getSingleton().getConnection();
        PreparedStatement statement = connection.prepareStatement(updateString);

        statement.setBoolean(1, state);
        statement.setInt(2, s.getUserServiceID());
        
        statement.execute();
    }

    private void update_edit_info(Service s, String username, String password, boolean autoconnect)
    {
        try
        {
            // The key-value ones
            int indirection_id = get_indirection_id_from_user_service_id(s.getUserServiceID());
            update_key_value_info(indirection_id, "USERNAME", username);
            update_key_value_info(indirection_id, "PASSWORD", password);
            // The static one
            service_autoconnect(s, autoconnect);
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
                Object[] data = (Object[]) payload;
                update_edit_info((Service) data[0], (String) data[1], (String) data[2], (boolean) data[3]); 
                break;
        }
    }
}
