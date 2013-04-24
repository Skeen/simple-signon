import java.lang.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.text.*;
import javax.swing.table.*;

class LoginSSOConnectionHandler
{
    void connect(String username, String password)
    {
        System.out.println("Connect using; " + username + " : " + password);

        JFrame frame = new JFrame("Connection Frame");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] columnNames = {"SERVICE_TYPE",
                                "USERNAME",
                                "PASSWORD",
                                "WEBPAGE",
                                "LOGGED-IN"};

        // Make the table uneditable
        DefaultTableModel model = new DefaultTableModel(null, columnNames)
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
                java.util.List<Object> data = new ArrayList<Object>();
                for(Object o : line.split(" "))
                {
                    data.add(o);
                }
                data.add("NOT_CONNECTED");

                model.insertRow(model.getRowCount(), data.toArray());

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
                ConnectorThread connector = new ConnectorThread(model, model.getRowCount() - 1);
                new Thread(connector).start();
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

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
