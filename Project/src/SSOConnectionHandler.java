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

class SSOConnectionHandler
{
    public SSOConnectionHandler()
    {
    }

    java.util.List<Service> services = new ArrayList<Service>();

    void connect(String username, String password)
    {
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

        /*
        SwingUtilities.invokeLater(new Runnable()
                {
                    public void run() {
                        tray = SSOTray.getSingleton();
                        tray.showGUI();
                    }
                });
        */
        SwingUtilities.invokeLater(new Runnable()
                {
                    public void run() {
                        SSOLogin login = SSOLogin.getSingleton();
                        login.hideGUI();
                        SSOWindow window = SSOWindow.getSingleton();
                        window.loadServices(services);
                        window.showGUI();
                    }
                });

        System.out.println("Connect using; " + username + " : " + password);
    }
}
