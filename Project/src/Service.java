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

    public enum Status {
        NOTCONNECTED, // Not going to
        DISCONNECTED, // Was connected, lost connection
        CONNECTING,   // (re)connecting
        CONNECTED     // Connected
    }

    public Service(Service dependency, String name, String logo_path, ServiceType service_type)
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
        this.status = Status.DISCONNECTED;
    }

    private void delay()
    {
        try
        {
            int min = 100;
            int max = 1000;
            // Just sleep
            Thread.sleep(Utilities.getRandomBetween(min, max));
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
            delay();
            // Check if the service is connected
            boolean isConnected = type.isConnected();
            if(isConnected)
            {
                status = Status.CONNECTED;
            }
            else
            {
                int id = Utilities.getRandomBetween(0,3);
                switch(id)
                {
                    case 0:
                        status = Status.NOTCONNECTED;
                        break;
                    case 1:
                        status = Status.DISCONNECTED;
                        break;
                    case 2:
                        status = Status.CONNECTING;
                        break;
                    case 3:
                        status = Status.CONNECTED;
                        break;
                    default:
                        System.err.println("WHAT");
                        break;
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
