import java.awt.*;

class Service
{
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

    // The service (if any) that this one depends on
    private Service dependency;
    // The name of this service
    private String name;
    // The logo for this service
    private Image logo;
    // The type of this service
    private String type;
}
