import java.io.IOException;
import java.io.*;

class CiscoVPNServiceType implements ServiceType
{
    private static final String cisco_vpn_path = "C:\\Program Files (x86)\\Cisco\\Cisco AnyConnect Secure Mobility Client\\vpncli.exe";
    private static final String cisco_vpn_connect = "connect";
    private static final String cisco_vpn_disconnect = "disconnect";
    private static final String cisco_vpn_status = "state";
    private static String cisco_vpn_server = null;
    private static String cisco_vpn_profile = null;

    public CiscoVPNServiceType(String host, String profile)
    {
        this.cisco_vpn_server = host;
        this.cisco_vpn_profile = profile;
    }

    private volatile boolean connecting = false;

    private boolean isConnected()
    {
        try
        {
            Process proc = new ProcessBuilder(new String[]
                    {
                        cisco_vpn_path,
                        cisco_vpn_status,
                        cisco_vpn_server
                    }).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            for(String line = br.readLine(); line != null; line = br.readLine())
            {
                if(line.equals("  >> state: Connected"))
                {
                    return true;
                }
                else if (line.equals("  >> state: Disconnected"))
                {
                    return false;
                }
            }
            // If we get here, something went wrong, and we did not get the
            // result we wanted, hence throw an exception
            throw new RuntimeException("SHIT");
            //p.waitFor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("SHIT");
        }
    }

    public Service.Status getStatus()
    {
        // Are we connecting currently
        if(connecting)
        {
            return Service.Status.CONNECTING;
        }
        else
        {
            boolean connected = isConnected();
            // Are we connected
            if(connected)
            {
                return Service.Status.CONNECTED;
            }
            // We are not connecting, and not connected, hence we must be
            // disconnected
            else
            {
                return Service.Status.DISCONNECTED;
            }
        }
    }

    public void connect()
    {
        // TODO: Implementation
    }

    public void disconnect()
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(new String[]
                    {
                        cisco_vpn_path,
                        cisco_vpn_disconnect,
                        cisco_vpn_server
                    });
            p.waitFor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
