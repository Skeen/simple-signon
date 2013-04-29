import java.awt.*;
import java.util.Random;

import javax.swing.JTable;

class Service implements Runnable
{
    public enum Status {
        NOTCONNECTED, // Not going to
        DISCONNECTED, // Was connected, lost connection
        CONNECTING,   // (re)connecting
        CONNECTED     // Connected
    }
    
    public Service(Service dependency, String name, String logo_path, String service_type)
    {
        this.dependency = dependency;
        this.name = name;
        this.logo = SSOTray.createImage(logo_path);
        this.type = service_type;
        this.status = Status.DISCONNECTED;
    }

    private int getRandomBetween(int min, int max)
    {
        Random rand = new Random();
        return min + rand.nextInt(max - min + 1);
    }

    private void delay()
    {
        try
        {
            int min = 100;
            int max = 1000;
            // Just sleep
            Thread.sleep(getRandomBetween(min, max));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private JTable t;

    public void seed(JTable t)
    {
        this.t = t;
    }

    public void run()
    {
        while(true)
        {
            delay();
            int id = getRandomBetween(0,3);
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
                    System.out.println("WOW");
                    break;
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

    // The Status of the service
    private Status status;
    // The service (if any) that this one depends on
    private Service dependency;
    // The name of this service
    private String name;
    // The logo for this service
    private Image logo;
    // The type of this service
    private String type;
}
