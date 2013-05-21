import java.lang.*;

import java.io.*;
import java.util.*;

import javax.swing.SwingUtilities;

import java.lang.Class;
import java.lang.reflect.Constructor;

import com.exproxy.processors.HttpMessageProcessor;

class SSOConnectionHandler
{
    public SSOConnectionHandler(String username, String password)
    {
    }

    List<Service> getServices()
    {
        List<Service> services = new ArrayList<Service>();
        // Read the services from a comma-seperated-file (code ready for sql server)
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("resource/data.csv"));
            for(String line = br.readLine(); line != null; line = br.readLine())
            {
                String[] items = line.split("\\s*,\\s*");
                // Dependency
                Service dependency = null;
                if(items[0].equals("") == false)
                {
                    //TODO: Set dependency to the required service class
                }
                // Strings
                String name         = items[1];
                String logo_path    = items[2];
                // Service_type
                ServiceType service_type = null;
                if(items[3].equals("") == false)
                {
                    // Non-null service_type class, so let's ask the class
                    // loader to find it
                    Class service_type_class = Class.forName(items[3]);
                    // Let's find the constructor that takes a single string
                    // (TODO: Should be the one that takes a Serializable)
                    Constructor service_type_constructor = service_type_class.getConstructor(String.class);
                    // Now construct the service type, using the string we've
                    // loaded in the csv file.
                    service_type = (ServiceType) service_type_constructor.newInstance(items[4]);
                }
                // Booleans
                boolean auto_connect = Boolean.parseBoolean(items[5]);
                boolean in_use       = Boolean.parseBoolean(items[6]);

                // Create the service
                services.add(new Service(dependency, name, logo_path, service_type, auto_connect, in_use));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return services;
    }
}
