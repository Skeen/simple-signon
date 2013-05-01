import java.io.IOException;

class WifiServiceType extends PingServiceType
{
    private volatile boolean connecting = false;
    public WifiServiceType(String host)
    {
        super(host);
    }

    private static int disconnectWifi()
    {
        try
        {
            Process proc = new ProcessBuilder("netsh", "wlan disconnect").start();

            int exitValue = proc.waitFor();
            System.out.println("Disconnect:" + exitValue);
            return exitValue;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    private static int connectWifi(String profile)
    {
        try
        {
            Process proc = new ProcessBuilder("netsh", "connect", "name=" + profile).start();

            int exitValue = proc.waitFor();
            System.out.println("Disconnect:" + exitValue);
            return exitValue;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public Service.Status getStatus()
    {
        // Are we connecting currently
        if(connecting)
        {
            return Service.Status.CONNECTING;
        }
        // If we're not connecting, let ping decide our state
        else
        {
            return super.getStatus();
        }
    }

    public void connect()
    {
        connecting = true;
        // Start Connecting
        connectWifi("AU-Gadget");
        while(super.getStatus() != Service.Status.CONNECTED);
        // Done Connecting
        connecting = false;
    }

    public void disconnect()
    {
        disconnectWifi();
    }
}
