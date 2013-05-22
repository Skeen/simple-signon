import java.util.Map;

class DefaultServiceType implements ServiceType
{
    public DefaultServiceType(Map<String,String> input)
    {
    }

    public void double_click()
    {
    }

    public Service.Status getStatus()
    {
        return Service.Status.DISCONNECTED;
    }

    public void connect()
    {
    }

    public void disconnect()
    {
    }
}
