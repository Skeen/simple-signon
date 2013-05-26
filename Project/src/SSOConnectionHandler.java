import java.lang.*;

import java.io.*;
import java.util.*;

import javax.swing.SwingUtilities;

import java.lang.Class;
import java.lang.reflect.Constructor;

import com.exproxy.processors.HttpMessageProcessor;

import java.sql.*;

class SSOConnectionHandler
{
    private String lookup_username;

    public SSOConnectionHandler(String username, String password)
    {
        this.lookup_username = username;
    }

    private Map<String,String> getInitMap(int service_id, int user_service_id)
        throws Exception
    {
        Map<String,String> initMap = new HashMap<String,String>();

        // Get all key-value pairs from the server
        Connection connection = MySQLConnection.getSingleton().getConnection();
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery("SELECT key_entry, value_entry FROM " + MySQLConnection.DATABASE + ".`key_value` " +
                                     "join service_indirection on idservice_indirection = service_indirection_id " +
                                     "where service_id = " + service_id + " or user_service_id = " + user_service_id);

        while(r.next()) 
        {
            String key = r.getString("key_entry");
            String value = r.getString("value_entry");

            //System.out.println(key + " : " + value);

            initMap.put(key, value);
        }

        return initMap;
    }

    List<Service> getServices()
    {
        List<Service> services = new ArrayList<Service>();
        // Read the services from sql server
        try
        {
            Connection connection = MySQLConnection.getSingleton().getConnection();
            Statement s = connection.createStatement();
            ResultSet r = s.executeQuery("SELECT service_id, user_service_id, service_logo, service_name, service_type_name, auto_connect, in_use " +
                    "FROM " + MySQLConnection.DATABASE + ".`user_service` " +
                    "join service on user_service.service_id = service.idservice " + 
                    "join service_type on service.service_type_id = idservice_type " +
                    "where user_service.username = \"" + lookup_username + "\"");

            while(r.next()) 
            {
                // Dependency
                Service dependency = null;
                // Strings
                String name = r.getString("service_name");
                String logo_path = r.getString("service_logo");
                // Service_type
                String service_type_string = r.getString("service_type_name");
                ServiceType service_type = null;
                // Now let's get the map, the constructors needs
                int service_id = r.getInt("service_id");
                int user_service_id = r.getInt("user_service_id");
                Map<String,String> initMap = getInitMap(service_id, user_service_id);
                // Non-null service_type class, so let's ask the class
                // loader to find it
                Class service_type_class = Class.forName(service_type_string);
                // Let's find the constructor that takes a map
                Constructor service_type_constructor = service_type_class.getConstructor(Map.class);
                // Now construct the service type, using the map we've just
                // created
                service_type = (ServiceType) service_type_constructor.newInstance(initMap);
                // Booleans
                boolean auto_connect = r.getBoolean("auto_connect");
                boolean in_use       = r.getBoolean("in_use");

                // Create the service
                services.add(new Service(user_service_id, dependency, name, logo_path, service_type, auto_connect, in_use, initMap));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return services;
    }
}
