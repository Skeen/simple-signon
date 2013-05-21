import javax.swing.SwingUtilities;
import java.util.List;
import com.exproxy.processors.HttpMessageProcessor;

class Transition
{
    private static class LoginScript implements Runnable
    {
        private String username;
        private String password;

        public LoginScript(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public void run() 
        {
            SSOConnectionHandler connection = new SSOConnectionHandler(username, password);
            List<Service> services = connection.getServices();

            // Hide the login prompt
            SSOLogin login = SSOLogin.getSingleton();
            login.hideGUI();
            // Show the tray
            SSOTray tray = SSOTray.getSingleton();
            tray.showGUI();
            // Show the window (load it as well)
            SSOWindow window = SSOWindow.getSingleton();
            window.loadServices(services);
            window.showGUI();
            // Activate the proxy
            Proxy proxy = Proxy.getSingleton();
            for(Service s : services)
            {
                // Setup the service, and make it runnable
                new Thread(s).start();

                HttpMessageProcessor processor = s.getHttpProcessor();
                if(processor != null)
                {
                    proxy.addHttpMessageProcessor(processor);
                }
            }
        }
    }

    private static class LogoutScript implements Runnable
    {
        public void run() 
        {
            // Hide SSO window
            SSOWindow window = SSOWindow.getSingleton();
            window.clearServices();
            window.hideGUI();
            // Hide the tray
            SSOTray tray = SSOTray.getSingleton();
            tray.hideGUI();
            // Disable the proxy
            Proxy proxy = Proxy.getSingleton();
            proxy.removeAllHttpMessageProcessors();
            // Show the loginWindow
            SSOLogin login = SSOLogin.getSingleton();
            login.clearPasswordField();
            login.showGUI();
        }
    }

    public static void login(String username, String password)
    {
        SwingUtilities.invokeLater(new LoginScript(username, password));

        System.out.println("Connect using; " + username + " : " + password);
    }

    public static void logout()
    {
        SwingUtilities.invokeLater(new LogoutScript());
    }
}
