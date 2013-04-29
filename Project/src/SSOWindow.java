import java.lang.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;
import javax.swing.text.*;
import javax.swing.table.*;

class SSOWindow implements Runnable
{
    private class SSOGrid
    {
        private JTable table;
        private DefaultTableModel model;

        public SSOGrid()
        {
        }

        private void addToTable(Object o)
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

        public JPanel createGUI(java.util.List<Service> services)
        {
            JPanel grid = new JPanel();
            grid.setLayout(new GridLayout(1, 1));

            // Make an immutable table
            model = new DefaultTableModel(0,3)
            {
                public boolean isCellEditable(int row, int column)
                {
                    return false;
                }
            };

            // Add all our services
            for(Service s : services)
            {
                /*
                // Get the logo
                Image logo = s.getLogo();
                // Scale it to 64x64
                ImageIcon scaledLogo = new ImageIcon(LoginSSOConnectionHandler.resize(logo, 64, 64));
                // Add it to the table
                addToTable(scaledLogo);
                */
                addToTable(s);
            }

            table = new JTable(model)
            {
                //  Returning the Class of each column will allow different
                //  renderers to be used based on Class
                public Class getColumnClass(int column)
                {
                    return getValueAt(0, column).getClass();
                }
            };

            table.setDefaultRenderer(Service.class, new DefaultTableCellRenderer()
                    {
                        // TODO: Figure out why it renders the entire row out,
                        // using the last icon, but it doesn't do this with
                        // setText(...)
                        public void setValue(Object value) {
                            if(value != null) {
                                Service s = (Service) value;
                                // Get the logo
                                Image logo = s.getLogo();
                                // Scale it to 64x64
                                ImageIcon scaledLogo = new ImageIcon(LoginSSOConnectionHandler.resize(logo, 64, 64));
                                // Render it
                                setIcon(scaledLogo);

                                setText(((Service) value).getName());
                                setText("");
                                /*
                                setText("!");
                                */
                            }
                            else {
                                setText("");
                            }
                        }
                    }); 

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

        public void addSelectionCallback(ListSelectionListener sl)
        {
            table.getColumnModel().getSelectionModel().addListSelectionListener(sl);
            table.getSelectionModel().addListSelectionListener(sl);
        }

        // Returns the Service corresponding to the GUI selection (if any)
        // Returns null when no selection is made
        public Service getSelection()
        {
            int column = table.getSelectedColumn();
            int row = table.getSelectedRow();
            System.out.println("Selection at (x,y)=(" + column + " , " + row + ");");
            if(column == -1 || row == -1)
            {
                return null;
            }
            else
            {
                return (Service) table.getValueAt(row, column);
            }
        }
    }

    class SSOButtons
    {
        private SSOGrid grid;

        private JButton add;
        private JButton remove;
        private JButton reconnect;
        private JButton edit;

        private JButton refresh;
        private JButton logout;

        private void updateButtons(boolean hasSelection)
        {
            if(hasSelection) 
            {
                remove.setEnabled(true); 
                reconnect.setEnabled(true); 
                edit.setEnabled(true); 
            }
            else 
            {
                remove.setEnabled(false); 
                reconnect.setEnabled(false); 
                edit.setEnabled(false);
            }
        }

        public SSOButtons(SSOGrid s)
        {
            grid = s;

            grid.addSelectionCallback(new ListSelectionListener()
                    {
                        private boolean isValidSelection() {
                            Service s = grid.getSelection();
                            return s != null;
                        }

                        public void valueChanged(ListSelectionEvent e) {
                            updateButtons(isValidSelection());
                        }
                    });
        }

        private JPanel createBorderButton(JButton button)
        {
            JPanel button_panel = new JPanel();
            button_panel.setLayout(new GridLayout(1, 1));
            button_panel.add(button);
            return button_panel;
        }

        public JPanel createGUI()
        {
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(2, 1));

            JPanel buttons_top = new JPanel();
            buttons_top.setLayout(new GridLayout(1, 4));
            JPanel buttons_bot = new JPanel();
            buttons_bot.setLayout(new GridLayout(1, 2));
            buttons.add(buttons_top);
            buttons.add(buttons_bot);

            add = new JButton("Add");
            remove = new JButton("Remove");
            reconnect = new JButton("(Re)Connect");
            edit = new JButton("Edit");
            refresh = new JButton("Refresh");
            logout = new JButton("Logout");
            logout.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e) {
                        login.showGUI();
                        hideGUI();
                    }
                });

            updateButtons(false);

            buttons_top.add(createBorderButton(add));
            buttons_top.add(createBorderButton(remove));
            buttons_top.add(createBorderButton(reconnect));
            buttons_top.add(createBorderButton(edit));

            buttons_bot.add(createBorderButton(refresh));
            buttons_bot.add(createBorderButton(logout));

            return buttons;
        }
    }

    private LoginSSO login;
    java.util.List<Service> services;

    // TODO: Test constructor, remove this
    public SSOWindow(LoginSSO login_prompt)
    {
        login = login_prompt;

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

    private void createLoginGUI()
    {
        //Create and set up the window.
        frame = new JFrame("SSO");

        // Overview holder panel
        JPanel overview = new JPanel();
        overview.setLayout(new BoxLayout(overview, BoxLayout.Y_AXIS));

        SSOGrid grid = new SSOGrid();
        overview.add(grid.createGUI(services));

        SSOButtons buttons = new SSOButtons(grid);
        overview.add(buttons.createGUI());

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
