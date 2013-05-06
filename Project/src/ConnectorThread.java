import java.lang.Runnable;
import java.lang.Thread;
import javax.swing.table.*;

import java.util.Random;

public class ConnectorThread implements Runnable
{
    private DefaultTableModel model;
    private int row_id;

    public ConnectorThread(DefaultTableModel model, int row_id)
    {
        this.model = model;
        this.row_id = row_id;
    }
    
    private void connect()
    {
        try
        {
            // Random delay to simulate connection
            Random rand = new Random();
            // From 1, to 10 seconds
            int min = 1000;
            int max = 10000;
            // Just sleep
            Thread.sleep(min + rand.nextInt(max - min + 1));
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
