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

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

class SSOWindow implements ServiceCallback
{
    private static SSOWindow singleton = new SSOWindow();
    public static SSOWindow getSingleton()
    {
        return singleton;
    }

    private JPanel servicePanel;
    private JPanel buttonPanel;
    private JFrame frame;
    
    private JButton add;
    private JButton remove;
    private JButton reconnect;
    private JButton edit;
    private JButton refresh;
    private JButton logout;

    private JList<Service> serviceList;
    
	private DefaultListModel<Service> model;
    private DefaultListModel<Service> availableServices;
    
    private SSOWindow THIS;
    
    // TODO: Test constructor, remove this
    private SSOWindow()
    {
        this.THIS = this;
        create();
    }

    private void create()
    {
        //Create and set up the window.
        frame = new JFrame("SSO");
        
        // Create GUI elements
        servicePanel = createServicePanel();
        buttonPanel = createButtonPanel();
        
        availableServices = new DefaultListModel<Service>();
        
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(buttonPanel, BorderLayout.SOUTH);
        pane.add(servicePanel, BorderLayout.CENTER);
        frame.pack();
        frame.setMinimumSize(new Dimension(64*2*4+10, 100));
    }
    
    public void loadServices(java.util.List<Service> services)
    {
        clearServices();
        //Add the services to the active and inactive listmodels.
        for(Service s : services)
        {
            if(s.isUsed())
            {
                model.addElement(s);
            }
            else
            {
                availableServices.addElement(s);
            }
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
    
    public void callback(Service s)
    {
        if(getSelection() == s)
        {
            if(s.getConnectStatus())
            {
                reconnect.setText("Stop forbindelse");
            }
            else
            {
                reconnect.setText("Opret forbindelse");
            }
        }
        servicePanel.updateUI();
        buttonPanel.updateUI();
    }
    
    
    // Returns the Service corresponding to the GUI selection (if any)
    // Returns null when no selection is made
    public Service getSelection()
    {
        return serviceList.getSelectedValue();
    }
    
    public void clearServices()
    {
        // This method should remove all services AND stop them, in whatever
        // they are doing (including all the subthreads of all services).
        
        model.clear();
        availableServices.clear();
        
    }
	
    public void addService(Service s)
    {
		model.addElement(s);
        availableServices.removeElement(s);
    }
    
    public void removeService(Service s)
    {
        model.removeElement(s);
        availableServices.addElement(s);
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
	
    private JPanel createButtonPanel()
    {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2, 1));
        
        JPanel buttons_top = new JPanel();
        buttons_top.setLayout(new GridLayout(1, 4));
        JPanel buttons_bot = new JPanel();
        buttons_bot.setLayout(new GridLayout(1, 2));
        
        add = new JButton("Tilføj");
        remove = new JButton("Fjern");
        reconnect = new JButton("Opret forbindelse");
        edit = new JButton("Rediger");
        refresh = new JButton("Opdater");
        logout = new JButton("Log af");
        
        //Button event actions
        edit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    Service s = getSelection();
                    SSOEdit edit = new SSOEdit(s);
                    edit.showGUI();
                }
            });
                    
        logout.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    Transition.logout();
                }
            });
        
        add.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    //TODO: Use a popup context menu instead of a window
                    //TODO: Use a seperate list (using the same JList instance is buggy)
                    SSOAdd ssoAdd = new SSOAdd(THIS, availableServices);
                    ssoAdd.showGUI();
                }
            });
            
            
        remove.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    removeService(getSelection());
                }
            });
        
        reconnect.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    Service s = getSelection();
                    
                    if (s.getConnectStatus())
                    {
                        s.disconnect();
                        reconnect.setText("Opret forbindelse");
                    }
                    else
                    {
                        s.connect();
                        reconnect.setText("Stop forbindelse");
                    }
                }
            });
        
        updateButtons(false);
        
        //Button fades out fades in depending on selection
        addSelectionCallback(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e) 
                {
                    Service s = getSelection();
                    boolean valid = (s != null);
                    if ( valid )
                    {
                        if (s.getConnectStatus())
                        {
                            reconnect.setText("Stop forbindelse");
                        }
                        else
                        {
                            reconnect.setText("Opret forbindelse");
                        }
                    }
                    updateButtons((valid));
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
    
    public void addSelectionCallback(ListSelectionListener sl)
    {
        serviceList.addListSelectionListener(sl);
    }
    
    private JPanel createServicePanel()
    {
        JPanel grid = new JPanel();
		
		model = new DefaultListModel<Service>();
		
		serviceList = new JList<Service>(model);
		
        serviceList.setCellRenderer(new DefaultListCellRenderer()
			{
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
				{
                    JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    Service s = (Service) value;
                    // Get the logo
                    Image logo = s.getLogo().getScaledInstance(64,64,Image.SCALE_SMOOTH);
                    // Add the overlay
                    Image overlay = getOverlay(s.getStatus()).getScaledInstance(24,24,Image.SCALE_SMOOTH);
                    // Render it
                    setIcon(new ImageIcon(Utilities.overlayImage(logo,overlay)));
                    
                    //setText(((Service) value).getName());
                    setText("");
                    return label;
				}
			});

        serviceList.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent evt) 
                {
                    JList list = (JList) evt.getSource();
                    // If double clicking
                    if (evt.getClickCount() == 2)
                    {
                        int index = list.locationToIndex(evt.getPoint());
                        Service s = (Service) list.getModel().getElementAt(index);
                        s.double_click();
                    }
                }
            });
        
		
        // Select one cell at a time
        serviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serviceList.setLayoutOrientation(serviceList.HORIZONTAL_WRAP);
        serviceList.setFixedCellWidth(64*2);
        serviceList.setFixedCellHeight(64);
		serviceList.setVisibleRowCount(0);
		JScrollPane scrollableList = new JScrollPane(serviceList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollableList.setPreferredSize(new Dimension(64*2*4+5, 64*3+5));
        grid.setLayout(new GridLayout(1,1));
		grid.add(scrollableList);
        return grid;
    }
}
