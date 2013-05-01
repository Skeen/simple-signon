class DefaultServiceType implements ServiceType
{
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
