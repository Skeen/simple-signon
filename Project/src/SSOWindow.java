import java.lang.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.text.*;
import javax.swing.table.*;

class SSOWindow implements Runnable
{
    java.util.List<Service> services;

    // TODO: Test constructor, remove this
    public SSOWindow()
    {
        this.services = new ArrayList<Service>();
        services.add(new Service(null, "WIFI", "resource/Wifi.png", null));
        services.add(new Service(null, "VPN", "resource/vpn.png", null));
        services.add(new Service(null, "It's learning", "resource/its_learning.png", null));
        services.add(new Service(null, "Bulb", "resource/Bulb.gif", null));
        services.add(new Service(null, "Bus", "resource/Bus.png", null));
        services.add(new Service(null, "Car", "resource/Car.png", null));
        services.add(new Service(null, "Clock", "resource/Clock.png", null));
        
        services.add(new Service(null, "akis", "resource/akis.png", null));
        services.add(new Service(null, "akis_green", "resource/akis_green.png", null));
        services.add(new Service(null, "bug", "resource/bug.png", null));
        services.add(new Service(null, "Dragon", "resource/Dragon.png", null));
    }

    public SSOWindow(java.util.List<Service> services)
    {
        this.services = services;
    }

    private JFrame frame;

    private JPanel createBorderButton(String str)
    {
        JPanel button_panel = new JPanel();
        button_panel.setLayout(new GridLayout(1, 1));
        button_panel.add(new JButton(str));
        return button_panel;
    }

    private JPanel createBorderButton(ImageIcon icon)
    {
        JPanel button_panel = new JPanel();
        button_panel.setLayout(new GridLayout(1, 1));
        button_panel.add(new JButton(icon));
        return button_panel;
    }

    private JPanel createButtons()
    {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2, 1));

        JPanel buttons_top = new JPanel();
        buttons_top.setLayout(new GridLayout(1, 4));
        JPanel buttons_bot = new JPanel();
        buttons_bot.setLayout(new GridLayout(1, 2));
        buttons.add(buttons_top);
        buttons.add(buttons_bot);

        buttons_top.add(createBorderButton("Add"));
        buttons_top.add(createBorderButton("Remove"));
        buttons_top.add(createBorderButton("(Re)Connect"));
        buttons_top.add(createBorderButton("Edit"));
        
        buttons_bot.add(createBorderButton("Refresh"));
        buttons_bot.add(createBorderButton("Logout"));

        return buttons;
    }

    private void addToTable(DefaultTableModel model, Object o)
    {
        while(true)
        {
            // If the model is empty
            if (model.getRowCount() == 0)
            {
                model.setRowCount(1);
                // Go from the top again
                continue;
            }

            // Find next null in current row, and insert there
            int last_row = model.getRowCount() - 1;
            int column_count = model.getColumnCount();
            for(int x=0; x<column_count; x++)
            {
                Object value = model.getValueAt(last_row, x);
                if(value == null)
                {
                    model.setValueAt(o, last_row, x);
                    return;
                }
            }
            // The row is full, make a new one
            model.setRowCount(model.getRowCount()+1);
            // And go from the top again
            continue;
        }
    }

    private JPanel createGRID()
    {
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(1, 1));

        // Make an immutable table
        DefaultTableModel model = new DefaultTableModel(0,3)
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        // Add all our services
        for(Service s : services)
        {
            // Get the logo
            Image logo = s.getLogo();
            // Scale it to 64x64
            ImageIcon scaledLogo = new ImageIcon(LoginSSOConnectionHandler.resize(logo, 64, 64));
            // Add it to the table
            addToTable(model, scaledLogo);
        }

        JTable table = new JTable(model)
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        // No header
        table.setTableHeader(null);

        // Select one cell at a time
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(true);

        for(int x=0; x<table.getRowCount(); x++)
        {
            table.setRowHeight(x, 64);
        }
        
        grid.add(new JScrollPane(table));

        grid.setPreferredSize(new Dimension(0,64*3));
        return grid;
    }

    private void createLoginGUI()
    {
        //Create and set up the window.
        frame = new JFrame("SSO");
        
        // Overview holder panel
        JPanel overview = new JPanel();
        overview.setLayout(new BoxLayout(overview, BoxLayout.Y_AXIS));

        overview.add(createGRID());
        overview.add(createButtons());

        frame.add(overview);

        frame.pack();
        frame.setResizable( false );
    }

    public void showGUI()
    {
        frame.setVisible(true);
    }

    public void hideGUI()
    {
        frame.setVisible(false);
    }

    public void run()
    {
        // Create the gui frame
        createLoginGUI();
        // And make it visible
        showGUI();
    }
}
