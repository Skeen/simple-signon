import java.io.*;

class PingServiceType implements ServiceType
{
    private volatile Boolean connected = null;
    public PingServiceType(final String host)
    {
        new Thread(new Runnable()
                {
                    public void run() {
                        while(true) {
                            connected = ping(host);
                        }
                    }
                }).start();
    }

    private static boolean ping(String host)
    {
        boolean isReachable = false;
        try
        {
            Process proc = new ProcessBuilder("ping"/*, "-n 1"*/, host).start();

            int exitValue = proc.waitFor();
            //System.out.println("Exit Value:" + exitValue);
            if(exitValue == 0)
            {
                isReachable = true;
            }
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
        return isReachable;
    }
    
    public Service.Status getStatus()
    {
        // If we got no ping report yet, let's wait for it
        if(connected == null)
        {
            return Service.Status.CONNECTING;
        }
        // Are we connected
        else if(connected)
        {
            return Service.Status.CONNECTED;
        }
        // We are not connecting, we must be disconnected
        else
        {
            return Service.Status.DISCONNECTED;
        }
    }

    public void connect()
    {
    }

    public void disconnect()
    {
    }
}
