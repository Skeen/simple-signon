import javax.swing.SwingUtilities;

class Driver
{
    public static void main(String[] args) 
    {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new LoginSSO());
    }
}
