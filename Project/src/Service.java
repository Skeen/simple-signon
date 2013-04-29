import java.awt.*;

class Service
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
