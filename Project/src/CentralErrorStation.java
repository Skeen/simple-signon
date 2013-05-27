import javax.swing.JOptionPane;

class CentralErrorStation
{
    public static void mysql_connection_error()
    {
        JOptionPane.showMessageDialog(null,
                "Critical SQL Error: Ensure that the SQL server is running,\n" +
                "and is loaded with valid data, then please try again",
                "Critical SQL Error",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public static void proxy_port_taken_error()
    {
        JOptionPane.showMessageDialog(null,
                "Critical Proxy Error: The proxy port, has already been taken,\n" +
                "ensure that the program is not already running,\n" + 
                "or close any program, which may be using the proxy port,\n" +
                "then please try again",
                "Critical Proxy Error",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
