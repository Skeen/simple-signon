import java.lang.*;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;
import javax.swing.text.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

class SSOWindow
{
    private static SSOWindow singleton;
    public static SSOWindow getSingleton()
    {
        if(singleton == null)
        {
            singleton = new SSOWindow();
        }
        return singleton;
    }

    private SSOGrid serviceGrid;
    private JPanel servicePanel;
    private JFrame frame;

    // TODO: Test constructor, remove this
    private SSOWindow()
    {
        create();
    }

    private void create()
    {
        //Create and set up the window.
        frame = new JFrame("SSO");
        
        // Create GUI elements
        serviceGrid = new SSOGrid();
        servicePanel = serviceGrid.createGUI();
        JPanel buttonPanel = createButtonPanel(serviceGrid);
        
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(buttonPanel, BorderLayout.SOUTH);
        pane.add(servicePanel, BorderLayout.CENTER);
        
        frame.pack();
        frame.setResizable( false );
    }
    
    public void loadServices(java.util.List<Service> services)
    {
        serviceGrid.clearServices();
        serviceGrid.addServices(services);
        
        // TODO: Start connecting
        for(Service s : services)
        {
            s.seed(servicePanel);
            new Thread(s).start();
        }
    }
    
    public void showGUI()
    {
        frame.setVisible(true);
    }

    public void hideGUI()
    {
        frame.setVisible(false);
    }

    public boolean isShown()
    {
        return frame.isVisible();
    }
    
    private JButton add;
    private JButton remove;
    private JButton reconnect;
    private JButton edit;

    private JButton refresh;
    private JButton logout;
        
    public JPanel createButtonPanel(SSOGrid grid)
    {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2, 1));
        
        JPanel buttons_top = new JPanel();
        buttons_top.setLayout(new GridLayout(1, 4));
        JPanel buttons_bot = new JPanel();
        buttons_bot.setLayout(new GridLayout(1, 2));
        
        add = new JButton("Add");
        remove = new JButton("Remove");
        reconnect = new JButton("(Re)Connect");
        edit = new JButton("Edit");
        refresh = new JButton("Refresh");
        logout = new JButton("Logout");
        
        //Button event actions
        edit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    SSOEdit edit = new SSOEdit();
                    edit.showGUI();
                }
            });
                    
        logout.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    SwingUtilities.invokeLater(new Runnable()
                        {
                            public void run() {
                                // Hide ourselves login prompt
                                hideGUI();
                                // Hide the tray
                                SSOTray tray = SSOTray.getSingleton();
                                tray.hideGUI();
                                // Show the loginWindow
                                SSOLogin login = SSOLogin.getSingleton();
                                login.clearPasswordField();
                                login.showGUI();
                            }
                        });
                }
            });
        updateButtons(false);
        
        //Button fades out fades in depending on selection
        serviceGrid.addSelectionCallback(new ListSelectionListener()
            {
                private boolean isValidSelection() {
                    Service s = serviceGrid.getSelection();
                    return s != null;
                }
                
                public void valueChanged(ListSelectionEvent e) {
                    updateButtons(isValidSelection());
                }
            });
        
        buttons_top.add(add);
        buttons_top.add(remove);
        buttons_top.add(reconnect);
        buttons_top.add(edit);
        
        buttons_bot.add(refresh);
        buttons_bot.add(logout);
        
        buttons.add(buttons_top);
        buttons.add(buttons_bot);
        
        return buttons;
    }
    
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
    
    private class SSOGrid
    {
        private JTable table;
        private DefaultTableModel model;

        public SSOGrid()
        {
        }

        public void clearServices()
        {
        }

        public void addServices(java.util.List<Service> services)
        {
            for(Service s : services)
            {
                addService(s);
            }
        }

        public void addService(Service s)
        {
            addToTable(s);
        }

        private void updateTableHeight()
        {
            for(int x=0; x<table.getRowCount(); x++)
            {
                table.setRowHeight(x, 64);
            }
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
                        updateTableHeight();
                        return;
                    }
                }
                // The row is full, make a new one
                model.setRowCount(model.getRowCount()+1);
                // And go from the top again
                continue;
            }
        }

        private Image getOverlay(Service.Status status)
        {
            final String not_connected_image = "resource/Pictogram/nothing_placeholder.png";
            final String disconnected_image = "resource/Pictogram/Kryds.png";
            final String connecting_image = "resource/Pictogram/Tandhjul.png";
            final String connected_image = "resource/Pictogram/Flueben.png";

            String path_to_image = null;
            switch(status)
            {
                case NOTCONNECTED:
                    path_to_image = not_connected_image;
                    break;
                case DISCONNECTED:
                    path_to_image = disconnected_image;
                    break;
                case CONNECTING:
                    path_to_image = connecting_image;
                    break;
                case CONNECTED:
                    path_to_image = connected_image;
                    break;
            }
            // Load and return the image
            return Utilities.loadImage(path_to_image);
        }

        public JPanel createGUI()
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
                                Image logo = Utilities.resizeImage(s.getLogo(), 64, 64);
                                // Add the overlay
                                Image overlay = Utilities.resizeImage(getOverlay(s.getStatus()),24,24);
                                // Render it
                                setIcon(new ImageIcon(Utilities.overlayImage(logo,overlay)));

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
}
