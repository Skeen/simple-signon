import java.lang.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.text.*;
import javax.swing.table.*;

// TODO: When connect botton click;
// 1. Load the users RSA key
// 2. Unlock the RSA key (with the given password)
// 3. Decrypt SSO login file (using the RSA key)
// 4. Read the decrypted file, into a stream
//      *** POSSIBLE FILE LAYOUT ***
//          SERVICE_TYPE \t USERNAME \t PASSWORD \t WEBPAGE (\t MORE_INFO)?
//      *** Example ***
//          HTML_FORM \t jmi \t 13231 \t www.login.intra.aarhus.tech.dk
// 5. Open a 'process' window, with all the sites to be logged into, and their
// status; *NOT CONNECTED, CONNECTING, CONNECTED, DISCONNECTED*, possibly in a
// table-like structure
// 6. Process the services to be logged into, updating the 'process' window, as
// logins pass through.
// 7. Possibly minimize the SSO application to tray, automatically (depending on
// the users setting).
//
// NOTE; 1-4, can be simplified to simply reading a non-encrypted file (ONLY FOR
// PROTOTYPING)
// NOTE; 5, can be simplified to a console output, though we'll need user input
// on it
// Note: 7, is just my own idea
//
// In order to achieve this, we'll need;
// 1-4; a valid password protected RSA key for the user, possibly handed out by
// the IT administration. - We'll also need a way to decrypt, and open RSA keys
// from java.
// 5; Simple java.swing code will do.
// 6; Logging into services is the job of the program backend, and should
// possibly be done, using HTML POST requests, and injecting cookies into the
// active browsers on the system (for HTML forms). For logging into the WIFI and
// such, we'll possibly need to rely and C code + JNI, or some thirdpart library.
// 7; Simple java.swing code wil do.
//
// We'll possibly be interrested in looking to, US Patent 20030195963 A1;
// http://www.google.com/patents/US20030195963
//
// Simplied 1-4: Done
// 5: Done
// 6: Partly done (GUI part)

public class SSOLogin
{
    private static SSOLogin singleton = new SSOLogin();
    public static SSOLogin getSingleton()
    {
        return singleton;
    }

    private SSOLogin()
    {
        create();
    }
    
    private JFrame frame;
    private JPanel input;
    private JTextField username_field;
    private JPasswordField password_field;

    private JFrame create()
    {
        //Create and set up the window.
        frame = new JFrame("SSO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Overview holder panel
        JPanel overview = new JPanel();
        overview.setLayout(new GridLayout(3,1));

        // Add a title
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(2,1));
        title.add(new JLabel("Aarhus Tech", JLabel.CENTER));
        title.add(new JLabel("Single sign-on (SSO)", JLabel.CENTER));

        // Input panel
        input = new JPanel();
        GridLayout input_layout = new GridLayout(2,2);
        input_layout.setHgap(20);
        //input_layout.setVgap(10);
        input.setLayout(input_layout);
        // And contents
        input.add(new JLabel("Brugernavn:"));
        username_field = new JTextField();
        input.add(username_field);

        input.add(new JLabel("Kodeord:"));
        password_field = new JPasswordField();
        input.add(password_field);

        // Connect botton
        JPanel connect = new JPanel();
        JButton botton = new JButton("Forbind");
        botton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e) 
                    {
                        System.out.println("Connect clicked");
                        String username = username_field.getText();
                        String password = new String(password_field.getPassword());

                        EventSystem eventSystem = EventSystem.getSingleton();
                        eventSystem.trigger_event(EventSystem.LOGIN_EVENT, new String[]{username, password});
                    }
                });
        connect.add(botton);

        // Add it together
        overview.add(title);
        overview.add(input);
        overview.add(connect);

        // Add the overview to the frame
        frame.getContentPane().add(overview);
        // Enter maps to Connect
        frame.getRootPane().setDefaultButton(botton);

        // Display the window.
        frame.pack();

        // Return the frame
        return frame;
    }

    public void clearPasswordField()
    {
        password_field = new JPasswordField();
    }
 
    public void showGUI()
    {
        frame.setVisible(true);
    }

    public void hideGUI()
    {
        frame.setVisible(false);
    }
}
