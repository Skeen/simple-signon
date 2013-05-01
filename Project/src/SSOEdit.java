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

public class SSOEdit
{
    /* Edit an SSO Service:
     *  Set Username
     *  Set Password
     *  Autologin
     */
     
    private Service service;
    
    private JFrame frame;
    /*
    private JPanel inputPanel;
    private JTextField passInput;
    private JTextField userInput;
    private JLabel userLabel;
    private JLabel passLabel;
    */
    
    // For testing, should take a service
    public SSOEdit()
    {
        create();
    }
    
    public SSOEdit(Service service)
    {
        this.service = service;
        create();
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
        frame = new JFrame("SSO Edit");
        
        //frame.setLayout(new BorderLayout());
		
        //Create panel with username label and text input
        JLabel userLabel = new JLabel(" Username: ");
		JTextField userInput = new JTextField(10);
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(userInput, BorderLayout.EAST);
        
        //Create panel with password label and text input
        JLabel passLabel = new JLabel(" Password: ");
		JTextField passInput = new JTextField(10);
		JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passInput, BorderLayout.EAST);
        
        //Create panel with repeat of password label and text input
        JLabel repeatLabel = new JLabel(" Repeat Password: ");
		JTextField repeatInput = new JTextField(10);
		JPanel repeatPanel = new JPanel(new BorderLayout());
        repeatPanel.add(repeatLabel, BorderLayout.WEST);
        repeatPanel.add(repeatInput, BorderLayout.EAST);
        
        //Create panel with auto connect checkmark, and Accept/Cancel buttons.
        JCheckBox autoConBox = new JCheckBox("Autoconnect");
        //JCheckBox autoConBox = new JCheckBox("Autoconnect: ", Services.do you autoconnect());
        JButton accept = new JButton("Accept");
        JButton cancel = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(autoConBox);
        buttonPanel.add(accept);
        buttonPanel.add(cancel);
        
        //Add events when clicking buttons
        cancel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    SSOEdit edit = new SSOEdit();
                    edit.hideGUI();
                    frame.dispose();
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