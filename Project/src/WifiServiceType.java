import java.io.IOException;

class WifiServiceType implements ServiceType
{
    private String test_host = null;

    public WifiServiceType(String test_host)
    {
        this.test_host = test_host;
    }

    private static boolean ping(String host)
    {
        boolean isReachable = false;
        try
        {
            Process proc = new ProcessBuilder("ping", host).start();

            int exitValue = proc.waitFor();
            System.out.println("Exit Value:" + exitValue);
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

    public boolean isConnected()
    {
        return ping(test_host);
    }

    public boolean connect()
    {
        return false;
    }

    public boolean disconnect()
    {
        return false;
    }
}
