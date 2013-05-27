import java.io.IOException;
import java.io.*;

import java.util.Map;

class CiscoVPNServiceType implements ServiceType
{
    private static final String cisco_vpn_path = "C:\\Program Files (x86)\\Cisco\\Cisco AnyConnect Secure Mobility Client\\vpncli.exe";
    private static final String cisco_vpn_connect = "connect";
    private static final String cisco_vpn_disconnect = "disconnect";
    private static final String cisco_vpn_status = "state";
    private static String cisco_vpn_server = null;
    private static String cisco_vpn_profile = null;

    public CiscoVPNServiceType(Map<String,String> input)
    {
        this.cisco_vpn_server = input.get("host");
        this.cisco_vpn_profile = input.get("profile");
    }

    public void double_click()
    {
    }

    private volatile boolean connecting = false;

    private Boolean isConnected()
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
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        // If we get here, something went wrong, and we did not get the
        // result we wanted, or we got an exception, so lets return null
        return null;
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
            Boolean connected = isConnected();
            // We're not connected
            if(connected == null || connected == false)
            {
                return Service.Status.DISCONNECTED;
            }
            // Are we connected
            else
            {
                return Service.Status.CONNECTED;
            }
        }
    }

    public void connect()
    {
        // TODO: Implementation
    }

    public void disconnect()
    {
        /*
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
        */
    }
}
