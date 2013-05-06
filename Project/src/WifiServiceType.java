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
            Process proc = new ProcessBuilder(new String[]
                    {
                        "netsh",
                        "wlan",
                        "disconnect"
                    }).start();

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

    // Oprettelse af profil
    /* // Creates a profile, given a profile.xml document (given as argument)
     * private static int createProfile(String path_to_profile)
     * {
     *  try
     *  {
     *      Process proc = new ProcessBuilder(new String[]
     *              {
     *                  "netsh",
     *                  "wlan",
     *                  "add",
     *                  "profile",
     *                  "filename=\"" + path_to_profile + "\"
     *              }).start();
     *
     *           int exitValue = proc.waitFor();
     *          System.out.println("CreateProfile:" + exitValue);
     *         return exitValue;
     *    }
     *   catch (IOException e)
     *  {
     *     System.out.println(e.getMessage());
     *    e.printStackTrace();
     *     }
     *    catch (InterruptedException e)
     *   {
     *      e.printStackTrace();
     * }
     *  return -1;
     * }
     */

    // Antager at profilen 'profile' allerede findes
    private static boolean connectWifi(String profile)
    {
        boolean did_connect = false;
        try
        {
            Process proc = new ProcessBuilder(new String[]
                    {
                        "netsh",
                        "wlan",
                        "connect",
                        "name=" + profile
                    }).start();

            int exitValue = proc.waitFor();
            System.out.println("Connect:" + exitValue);
            if(exitValue == 0)
            {
                did_connect = true;
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
        return did_connect;
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
        // TODO: Only connect if host is different!!
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
