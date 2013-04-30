/*
import javax.swing.table.*;

import java.io.*;

public class CiscoConnectorThread extends ConnectorThread
{
    private DefaultTableModel model;
    private int row_id;

    private static final String cisco_vpn_path = "C:\\Program Files (x86)\\Cisco\\Cisco AnyConnect Secure Mobility Client\\vpncli.exe";
    private static final String cisco_vpn_connect = "connect";
    private static final String cisco_vpn_disconnect = "disconnect";
    private static final String cisco_vpn_server = "vpn.au.dk";


    public CiscoConnectorThread(DefaultTableModel model, int row_id)
    {
        super(model, row_id);
    }
    
    private void connect()
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cisco_vpn_path + " " + cisco_vpn_connect + " " + cisco_vpn_server);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            OutputStream out = p.getOutputStream();
            //InputStream err = p.getErrorStream();


            for(String line = br.readLine(); line != null; line = br.readLine())
            {
                if(line.equals("Group: [ComputerScience]\n"))
                {
                    out.write('\n');
                }
                else if(line.equals("Username: [skeen]\n"))
                {
                    out.write('\n');
                }
                else if(line.equals("Password:\n"))
                {
                    out.write('\n');
                }
            }
            

            p.destroy();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }

    public void run()
    {
        connect();
        model.setValueAt("CONNECTED", row_id, 4);
    }
}
*/
