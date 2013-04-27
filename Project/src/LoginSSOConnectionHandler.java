import java.lang.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.text.*;
import javax.swing.table.*;

class LoginSSOConnectionHandler
{
    private LoginSSO login;
    private SSOTray tray;
    //private SSOWindow window;

    public LoginSSOConnectionHandler(LoginSSO login)
    {
        this.login = login;
        login.hideGUI();
    }

    void connect(String username, String password)
    {
        //SwingUtilities.invokeLater(new SSOTray());

        System.out.println("Connect using; " + username + " : " + password);

        JFrame frame = new JFrame("Connection Frame");

        /*
        // Make the table uneditable
        DefaultTableModel model = new DefaultTableModel()
        {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try
        {
            final String file_path = "resource/data.txt";
            BufferedReader br = new BufferedReader(new FileReader(file_path));

            // TODO: Validate the data.txt file format
            for(String line = br.readLine(); line != null; line = br.readLine()) 
            {
                // Add a line to the table
                for(Object o : line.split(" "))
                {
                    java.util.List<Object> data = new ArrayList<Object>();
                    data.add(o);
                    model.insertRow(model.getRowCount(), data.toArray());
                    //data.add(o);
                }
                //data.add("NOT_CONNECTED");

                //model.insertRow(model.getRowCount(), data.toArray());

                /*
                // Start the connector thread
                Scanner s = new Scanner(line).useDelimiter(" ");
                String input_service_type = s.next();
                String input_username = s.next();
                String input_password = s.next();
                String input_webpage = s.next();
                // Debug output
                System.out.println(input_service_type + "\t" +
                                   input_username + "\t" +
                                   input_password + "\t" +
                                   input_webpage);
                */
                // Start a worker thread, to process the request
                /*
                ConnectorThread connector = new ConnectorThread(model, model.getRowCount() - 1);
                new Thread(connector).start();
                */
        /*
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        frame.add(scrollPane);
        */

        Image busIcon = SSOTray.createImage("resource/Bus.png");
        ImageIcon busImg = new ImageIcon(resize(busIcon, 64, 64));
        Image carIcon = SSOTray.createImage("resource/Car.png");
        ImageIcon carImg = new ImageIcon(resize(carIcon, 64, 64));
        Image clockIcon = SSOTray.createImage("resource/Clock.png");
        ImageIcon clockImg = new ImageIcon(resize(clockIcon, 64, 64));

        String[] columnNames = {null, null};
        Object[][] data =
        {
            {busImg, "Bus"},
            {carImg, "Car"},
            {clockImg, "Clock"},
        };
        

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable( model )
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        // Select one cell at a time
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(true);


        for(int x=0; x<table.getRowCount(); x++)
        {
            table.setRowHeight(x, 64);
        }
        
        frame.add(new JScrollPane(table));
        //updateRowHeights(table);
        
        // Display the window.
        frame.setSize(64*3,64*3);
        frame.setResizable( false );
        frame.setVisible(true);
    }

    public static BufferedImage resize(Image srcImg, int w, int h)
    {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
}
