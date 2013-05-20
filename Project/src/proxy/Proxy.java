package proxy;

import com.exproxy.EXProxy;

import java.net.InetAddress;

// http://reuse.pagesperso-orange.fr/exproxy/index.html
public class Proxy
{
    private static String localHost = "127.0.0.1";
    private static int localPort = 8001;
    private static int backlog = 100;
    private static String keystore = "resource/keystore";
    private static char[] store_password = "storepass".toCharArray();
    private static char[] key_password = "keypass".toCharArray();

    // Singleton
    private static Proxy singleton = new Proxy();
    public static Proxy getSingleton()
    {
        return singleton;
    }

    private Proxy()
    {
    }

    // Facade pattern, proxy hides the globalproxydisable
    public static boolean is_disabled()
    {
        return GlobalProxyDisable.is_disabled();
    }

    public static void disable()
    {
        System.out.println("PROXY DISABLED");
        GlobalProxyDisable.disable();
    } 
    
    public static void enable()
    {
        System.out.println("PROXY ENABLED");
        GlobalProxyDisable.enable();
    } 

    public void run()
    {
        try
        {
            InetAddress addr = InetAddress.getByName(localHost);
            EXProxy exproxy = new EXProxy(addr, localPort, backlog, keystore, store_password, key_password);

            // TODO: All processors should be added, via the GlobalProxyDisable
            // class.

            exproxy.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
