import java.io.IOException;
import java.io.*;

class CiscoVPNServiceType implements ServiceType
{
    private static final String cisco_vpn_path = "C:\\Program Files (x86)\\Cisco\\Cisco AnyConnect Secure Mobility Client\\vpncli.exe";
    private static final String cisco_vpn_connect = "connect";
    private static final String cisco_vpn_disconnect = "disconnect";
    private static final String cisco_vpn_status = "state";
    private static final String cisco_vpn_server = "vpn.au.dk";

    public boolean isConnected()
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

    public boolean connect()
    {
        return false;
    }

    public boolean disconnect()
    {
        return false;
    }
}
