import java.lang.*;
import javax.swing.*;
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

public class SSOEdit implements EventSystem.EventListener
{
    /* Edit an SSO Service:
     *  Set Username
     *  Set Password
     *  Autologin
     */
     
    private static SSOEdit singleton = new SSOEdit();
    public static SSOEdit getSingleton()
    {
        return singleton;
    }
    
    private Service service;
    
    private JFrame frame;
    private EventSystem eventSystem;
    
    private JTextField userInput;
    private JPasswordField passInput;
    private JPasswordField repeatInput;
    private JCheckBox autoConBox;
    
    private SSOEdit()
    {
        eventSystem = EventSystem.getSingleton();
        
        eventSystem.addListener("EDIT_EVENT", this);
        
        create();
    }

    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.EDIT_EVENT:
                prepareEdit((Service) payload);
                break;
            default:
                break;
        }
    }
    
    public void prepareEdit(Service s)
    {
        service = s;
        // Clear the fields
        userInput.setText("");
        passInput.setText("");
        repeatInput.setText("");
        // Check the box, with the current autoconnect status
        autoConBox.setSelected(s.autoconnect());

        // Load the values into username and password fields
        Map<String, String> info_map = s.getConfigurationMap();
        if(info_map.containsKey("USERNAME"))
        {
            String username = info_map.get("USERNAME");
            userInput.setText(username);
        }
        if(info_map.containsKey("PASSWORD"))
        {
            String password = info_map.get("PASSWORD");
            passInput.setText(password);
        }
        // Show the GUI
        showGUI();
    }
    
    public void showGUI()
    {
        frame.setVisible(true);
    }

    public void hideGUI()
    {
        frame.setVisible(false);
    }
    
    private void create()
    {
        //Create and set up the window.
        frame = new JFrame("SSO Rediger");
        
        //frame.setLayout(new BorderLayout());
		
        //Create panel with username label and text input
        JLabel userLabel = new JLabel(" Brugernavn: ");
		userInput = new JTextField(10);
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(userInput, BorderLayout.EAST);
        
        //Create panel with password label and text input
        JLabel passLabel = new JLabel(" Kodeord: ");
		passInput = new JPasswordField(10);
		JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passInput, BorderLayout.EAST);
        
        //Create panel with repeat of password label and text input
        JLabel repeatLabel = new JLabel(" Gentag Kodeord: ");
		repeatInput = new JPasswordField(10);
		JPanel repeatPanel = new JPanel(new BorderLayout());
        repeatPanel.add(repeatLabel, BorderLayout.WEST);
        repeatPanel.add(repeatInput, BorderLayout.EAST);
        
        //Create panel with auto connect checkmark, and Accept/Cancel buttons.
        autoConBox = new JCheckBox("Autoforbind: ");
        JButton accept = new JButton("Godkend");
        JButton cancel = new JButton("Annuller");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(autoConBox);
        buttonPanel.add(accept);
        buttonPanel.add(cancel);

        autoConBox.addActionListener(new ActionListener()
        {
                public void actionPerformed(ActionEvent e) 
                {
                    service.set_autoconnect(autoConBox.isSelected());
                }
        });
        
        //Add events when clicking buttons
        cancel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    hideGUI();
                    service = null;
                }
            });
            
        accept.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    String username = userInput.getText();
                    String password = new String(passInput.getPassword());
                    String repeated = new String(repeatInput.getPassword());
                    boolean autocon = autoConBox.isSelected();
                    
                    if (password.equals(repeated))
                    {
                        hideGUI();
                        Object[] array = new Object[] {service, username, password, autocon};
                        eventSystem.trigger_event("EDIT_ACCEPT_EVENT", array);
                        service = null;
                    }
                    else
                    {
                        SSOTray tray = SSOTray.getSingleton();
                        tray.showError("Kodeord og gentaget kodeord var ikke ens.");
                    }
                }
            });
        
        //Set this window to use box layout and add panels
        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(userPanel);
        pane.add(passPanel);
        pane.add(repeatPanel);
        pane.add(buttonPanel);
        
        frame.pack();
        frame.setResizable( false );
    }
}
