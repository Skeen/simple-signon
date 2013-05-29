import javax.swing.SwingUtilities;

// TODO: Info til de requestede features;
//
// Create shortcuts; http://stackoverflow.com/questions/13145942/creating-a-shortcut-file-from-java
// Højre click popup; http://stackoverflow.com/questions/766956/how-do-i-create-a-right-click-context-menu-in-java-swing
// Lav en fil med computer specifikke informationer (f.eks vindue størrelse).

class Driver
{
    public static void main(String[] args) 
    {
        // Simply get these, to ensure that the singletons will be created
        // (such that events and such will be recieved)
        MySQLConnection.getSingleton();
        MySQLUpdater.getSingleton();
        Transition.getSingleton();
        SSOAdd.getSingleton();
        SSOEdit.getSingleton();
        SSOWindow.getSingleton();

        // Get the proxy
        Proxy proxy = Proxy.getSingleton();
        // Run the proxy
        new Thread(proxy).start();

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's Login GUI.
        SwingUtilities.invokeLater(new Runnable()
                {
                    public void run() 
                    {
                        SSOLogin login = SSOLogin.getSingleton();
                        login.showGUI();
                    }
                });
    }
}
