import com.exproxy.EXProxy;
import com.exproxy.processors.HttpMessageProcessor;

import java.net.InetAddress;

import java.util.List;
import java.util.ArrayList;

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

    private EXProxy exproxy = null;
    private Proxy()
    {
        try
        {
            InetAddress addr = InetAddress.getByName(localHost);
            exproxy = new EXProxy(addr, localPort, backlog, keystore, store_password, key_password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // Add a proxy processor
    private List<HttpMessageProcessor> processors = new ArrayList<HttpMessageProcessor>();
    public void addHttpMessageProcessor(HttpMessageProcessor processor)
    {
        System.out.println("HttpMessageProcessor added");

        exproxy.addRequestProcessor(processor);
        exproxy.addResponseProcessor(processor);

        processors.add(processor);
    }

    public void removeAllHttpMessageProcessors()
    {
        System.out.println("All HttpMessageProcessors removed");

        for(HttpMessageProcessor proxy_processor : processors)
        {
            exproxy.removeRequestProcessor(proxy_processor);
            exproxy.removeResponseProcessor(proxy_processor);
        }
        processors.clear();
    }

    public void run()
    {
        try
        {
            // Start the proxy
            exproxy.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
