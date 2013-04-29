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

class LoginSSOConnectionHandler
{
    private LoginSSO login;
    private SSOTray tray;
    private SSOWindow window;

    public LoginSSOConnectionHandler(LoginSSO login)
    {
        this.login = login;
    }

    void connect(String username, String password)
    {
        login.hideGUI();

        tray = new SSOTray();
        //SwingUtilities.invokeLater(tray);
        window = new SSOWindow(login);
        SwingUtilities.invokeLater(window);

        System.out.println("Connect using; " + username + " : " + password);
    }

    public static BufferedImage resize(Image srcImg, int w, int h)
    {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
}
