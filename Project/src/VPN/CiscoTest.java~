import java.io.*;
import java.lang.RuntimeException;

class CiscoTest
{
    private static final String cisco_vpn_path = "C:\\Program Files (x86)\\Cisco\\Cisco AnyConnect Secure Mobility Client\\vpncli.exe";
    private static final String cisco_vpn_connect = "connect";
    private static final String cisco_vpn_disconnect = "disconnect";
    private static final String cisco_vpn_status = "state";
    private static final String cisco_vpn_server = "vpn.au.dk";

    private static boolean getStatus()
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(new String[]
                    {
                        cisco_vpn_path,
                        cisco_vpn_status,
                        cisco_vpn_server
                    });
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

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

    private static void disconnect()
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

    private static void connect()
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(new String[] 
                    {
                        cisco_vpn_path,
                        cisco_vpn_connect,
                        cisco_vpn_server
                    });

            InputStream is = p.getInputStream();
            OutputStream os = p.getOutputStream();

            while(true)
            {
                try
                {
                    p.exitValue();
                    break;
                }
                catch(IllegalThreadStateException e)
                {
                    os.write('\n');
                    os.write('\r');
                    os.write('\n');
                }
                while(2 <= is.available())
                {
                    System.out.print((char) is.read());
                }
            }
            /*
            for(String line = br.readLine(); line != null; line = br.readLine())
            {
                if(line.equals("  >> error: Connect not available. Another AnyConnect application is running"))
                {
                    throw new RuntimeException("PLEASE CLOSE ANY RUNNING VPN ANY_CONNECT APPLICATION");
                }
                else
                {
                    bw.newLine();
                    bw.flush();
                    bw.write("\n");
                    bw.flush();
                    bw.write("\r\n");
                    bw.flush();
                }
                System.out.println(line);
            }
            */
            System.out.println("ISSUES");
            p.waitFor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void killProcess(int pid)
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) 
            {
                Process p = rt.exec("taskkill /F /PID " + pid);
                p.waitFor();
            }
            else
            {
                Process p = rt.exec("kill -9 " + pid);
                p.waitFor();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void killProcess(String name)
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) 
            {
                Process p = rt.exec("taskkill /F /IM " + name);
                p.waitFor();
            }
            else
            {
                //TODO: Unix support
                //rt.exec("kill -9 " +....);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void killVPN()
    {
        //killProcess("vpnagent.exe");
        killProcess("vpncli.exe");
    }

    public static void main(String[] args)
    {
        boolean status = getStatus();
        System.out.print("Connection is ");
        if(!status)
        {
            System.out.print("not ");
        }
        System.out.println("up");
    }
}
