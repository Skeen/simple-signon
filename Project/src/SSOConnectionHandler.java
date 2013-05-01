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
        // TODO: Wifi Service, should be able to connect to wifi, given a
        // profile name, and disconnect as well (netsh)

        services.add(new Service(null, "WIFI", "resource/Wifi.png", new WifiServiceType("8.8.8.8"), true));
        services.add(new Service(null, "DNS", "resource/DNS.png", new PingServiceType("www.google.com"), true));
        services.add(new Service(null, "VPN", "resource/vpn.png", new CiscoVPNServiceType(), true));
        services.add(new Service(null, "It's learning", "resource/its_learning.png", null, false));
        services.add(new Service(null, "Bulb", "resource/Bulb.gif", null, false));
        services.add(new Service(null, "Bus", "resource/Bus.png", null, false));
        services.add(new Service(null, "Car", "resource/Car.png", null, false));
        services.add(new Service(null, "Clock", "resource/Clock.png", null, false));

        services.add(new Service(null, "akis", "resource/akis.png", null, false));
        services.add(new Service(null, "akis_green", "resource/akis_green.png", null, false));
        services.add(new Service(null, "bug", "resource/bug.png", null, false));
        services.add(new Service(null, "Dragon", "resource/Dragon.png", null, false));

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
