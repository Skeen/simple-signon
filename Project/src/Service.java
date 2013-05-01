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

    public enum Status {
        NOTCONNECTED, // Not going to
        DISCONNECTED, // Was connected, lost connection
        CONNECTING,   // (re)connecting
        CONNECTED     // Connected
    }

    public Service(Service dependency, String name, String logo_path, ServiceType service_type, boolean auto_connect)
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

    private JPanel t;

    public void seed(JPanel t)
    {
        this.t = t;
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
                }
            }
            t.updateUI();
        }
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
}
