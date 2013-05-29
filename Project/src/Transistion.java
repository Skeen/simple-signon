import javax.swing.SwingUtilities;
import java.util.List;
import com.exproxy.processors.HttpMessageProcessor;

class Transition implements EventSystem.EventListener
{
    private static Transition singleton = new Transition();
    public static Transition getSingleton()
    {
        return singleton;
    }

    private Transition()
    {
        EventSystem eventSystem = EventSystem.getSingleton();
        eventSystem.addListener(EventSystem.LOGOUT_EVENT, this);
        eventSystem.addListener(EventSystem.LOGIN_EVENT, this);
    }
    
    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.LOGOUT_EVENT:
                logout();
                break;
            case EventSystem.LOGIN_EVENT:
                String[] strings = (String[]) payload;
                String username = strings[0];
                String password = strings[1];
                login(username, password);
                break;
        }
    }
    
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
            
            EventSystem eventSystem = EventSystem.getSingleton();
            
            // Hide the login prompt
            SSOLogin login = SSOLogin.getSingleton();
            login.hideGUI();
            // Show the tray
            SSOTray tray = SSOTray.getSingleton();
            // Show the window (load it as well)
            SSOWindow window = SSOWindow.getSingleton();
            // Load each service
            for(Service s : services)
            {
                eventSystem.trigger_event(EventSystem.LOAD_SERVICE, s);
                // Setup the service, and make it runnable
                new Thread(s).start();
                /*
                   HttpMessageProcessor processor = s.getHttpProcessor();
                   if(processor != null)
                   {
                   proxy.addHttpMessageProcessor(processor);
                   }
                   */
            }
            tray.showGUI();
            window.showGUI();
        }
    }

    private static class LogoutScript implements Runnable
    {
        public void run() 
        {
            EventSystem eventSystem = EventSystem.getSingleton();

            // Clear all services from the system
            eventSystem.trigger_event(EventSystem.CLEAR_SERVICES, null);
            
            // Hide SSO main window
            SSOWindow window = SSOWindow.getSingleton();
            window.hideGUI();
            // Hide SSO add window
            SSOAdd add_window = SSOAdd.getSingleton();
            add_window.hideGUI();
            // Hide SSO edit window
            SSOEdit edit_window = SSOEdit.getSingleton();
            edit_window.hideGUI();
            // Hide the tray
            SSOTray tray = SSOTray.getSingleton();
            tray.hideGUI();
            // Show the loginWindow
            SSOLogin login = SSOLogin.getSingleton();
            login.clearPasswordField();
            login.showGUI();
        }
    }

    private static void login(String username, String password)
    {
        SwingUtilities.invokeLater(new LoginScript(username, password));

        System.out.println("Connect using; " + username + " : " + password);
    }

    private static void logout()
    {
        SwingUtilities.invokeLater(new LogoutScript());
    }
}
