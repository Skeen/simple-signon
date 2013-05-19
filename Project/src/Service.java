import java.awt.*;
import java.util.Random;

import javax.swing.JPanel;

class Service implements Runnable
{
    // The Status of the service
    private Status status;
    // The service (if any) that this one depends on
    private Service dependency;
    // The name of this service
    private String name;
    // The logo for this service
    private Image logo;
    // The type of this service
    private ServiceType type;
    // Whether this service is auto-connect
    private boolean auto_connect;
    // Whether this service is in list of used services
    private boolean in_use;

    public enum Status {
        NOTCONNECTED, // Not going to
        DISCONNECTED, // Was connected, lost connection
        CONNECTING,   // (re)connecting
        CONNECTED     // Connected
    }

    private boolean connect;

    public Service(Service dependency, String name, String logo_path, ServiceType service_type, boolean auto_connect, boolean in_use)
    {
        this.dependency = dependency;
        this.name = name;
        this.logo = Utilities.loadImage(logo_path);
        if(service_type == null)
        {
            type = new DefaultServiceType();
        }
        else
        {
            type = service_type;
        }
        this.auto_connect = auto_connect;
        this.connect = auto_connect;
        this.in_use = in_use;
        this.status = Status.NOTCONNECTED;
        if(auto_connect)
        {
            new Thread(new Runnable() 
                    {
                        public void run() {
                            type.connect();
                        }
                    }).start();
        }
    }

    private ServiceCallback callback;

    public void seed(ServiceCallback callback)
    {
        this.callback = callback;
    }

    public void run()
    {
        while(true)
        {
            Utilities.delay(1000);
            if(auto_connect == false)
            {
                status = Status.NOTCONNECTED;
            }
            else
            {
                status = type.getStatus();
                if(status == Status.DISCONNECTED)
                {
                    // Show an error at the tray icon
                    SSOTray tray = SSOTray.getSingleton();
                    tray.showError(name + " disconnected");
                }
                else
                {
                    /*
                    // Show an error at the tray icon
                    SSOTray tray = SSOTray.getSingleton();
                    tray.showInfo(name + " connected");
                    */
                }
            }
            callback.callback(this);
        }
    }
    
    public void connect()
    {
        connect = true;
        type.connect();
        //TODO: actually connect
    }
    public void disconnect()
    {
        connect = false;
        type.disconnect();
        //TODO: stop thread and close current connect
    }

    // Return whether this service is set to be connected.
    public boolean getConnectStatus()
    {
        return connect;
    }

    public Image getLogo()
    {
        return logo;
    }

    public String getName()
    {
        return name;
    }

    public Status getStatus()
    {
        return status;
    }
    
    public boolean isUsed()
    {
        return in_use;
    }
    
    public boolean autoconnect()
    {
        return auto_connect;
    }
}
