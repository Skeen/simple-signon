import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

class SSOTray
{
    private static SSOTray singleton;
    public static SSOTray getSingleton()
    {
        if(singleton == null)
        {
            singleton = new SSOTray();
        }
        return singleton;
    }

    private static final String SystemTrayPath = "resource/SystemTray.png";
    private SSOTray()
    {
        create();
    }

    private TrayIcon trayIcon = null;

    public void showError(String str)
    {
        trayIcon.displayMessage("SSO Error", str, TrayIcon.MessageType.ERROR);
    }

    public void showWarning(String str)
    {
        trayIcon.displayMessage("SSO Warning", str, TrayIcon.MessageType.WARNING);
    }

    public void showInfo(String str)
    {
        trayIcon.displayMessage("SSO Info", str, TrayIcon.MessageType.INFO);
    }

    public void showMessage(String str)
    {
        trayIcon.displayMessage("SSO Message", str, TrayIcon.MessageType.NONE);
    }

    private void create() 
    {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) 
        {
            System.err.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(Utilities.loadImage(SystemTrayPath));// "Tray Icon"));
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Simple-Signon SystemTray");

        // Create a popup menu components
        MenuItem openItem = new MenuItem("Open / Close");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(openItem);
        popup.addSeparator();
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        ActionListener openWindow = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOWindow window = SSOWindow.getSingleton();
                if(window.isShown()) {
                    window.hideGUI();
                }
                else {
                    window.showGUI();
                }
            }
        };

        trayIcon.addActionListener(openWindow);
        openItem.addActionListener(openWindow);

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                    "SSO (SingleSignOn) Solution developed by DJ-JOE");
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final SystemTray tray = SystemTray.getSystemTray();
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }

    public void showGUI()
    {
        final SystemTray tray = SystemTray.getSystemTray();
        try 
        {
            tray.add(trayIcon);
        }
        catch (AWTException e) 
        {
            System.err.println("TrayIcon could not be added.");
            return;
        }
    }

    public void hideGUI()
    {
        final SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }
}
