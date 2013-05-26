import com.exproxy.EXProxy;
import com.exproxy.processors.HttpMessageProcessor;

import java.net.InetAddress;

import java.util.List;
import java.util.ArrayList;

// http://reuse.pagesperso-orange.fr/exproxy/index.html
public class Proxy implements EventSystem.EventListener, Runnable
{
    private static final String localHost = "127.0.0.1";
    public static final int localPort = 8001;
    private static final int backlog = 100;
    private static final String keystore = "resource/keystore";
    private static final char[] store_password = "storepass".toCharArray();
    private static final char[] key_password = "keypass".toCharArray();

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

        EventSystem eventSystem = EventSystem.getSingleton();
        eventSystem.addListener(EventSystem.LOAD_SERVICE, this);
        eventSystem.addListener(EventSystem.CLEAR_SERVICES, this);
        eventSystem.addListener(EventSystem.ADD_SERVICE_EVENT, this);
        eventSystem.addListener(EventSystem.REMOVE_EVENT, this);
    }

    private boolean add_service_if_http_processor(Service s)
    {
        HttpMessageProcessor processor = s.getProxyFilter();
        if(processor != null)
        {
            addHttpMessageProcessor(processor);
            return true;
        }
        return false;
    }

    private boolean remove_service_if_http_processor(Service s)
    {
        HttpMessageProcessor processor = s.getProxyFilter();
        if(processor != null)
        {
            removeHttpMessageProcessor(processor);
            return true;
        }
        return false;
    }

    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.LOAD_SERVICE:
                // Load the service
                Service s = (Service) payload;
                // Check if this is an active service
                boolean active = s.isUsed();
                if(active)
                {
                    // Add it (if valid)
                    add_service_if_http_processor(s);
                }
                // Otherwise fall through
                break;
            case EventSystem.CLEAR_SERVICES:
                // Clear all services
                removeAllHttpMessageProcessors();
                break; 
            case EventSystem.ADD_SERVICE_EVENT:
                // Add it (if valid)
                add_service_if_http_processor((Service) payload);
                break;
            case EventSystem.REMOVE_EVENT:
                // Remove it (if valid)
                remove_service_if_http_processor((Service) payload);
                break;
        }
    }

    // Add a proxy processor
    private List<HttpMessageProcessor> processors = new ArrayList<HttpMessageProcessor>();
    private void addHttpMessageProcessor(HttpMessageProcessor processor)
    {
        System.out.println("HttpMessageProcessor added");

        exproxy.addRequestProcessor(processor);
        exproxy.addResponseProcessor(processor);

        processors.add(processor);
    }

    private void removeHttpMessageProcessor(HttpMessageProcessor processor)
    {
        System.out.println("HttpMessageProcessor removed");

        exproxy.removeRequestProcessor(processor);
        exproxy.removeResponseProcessor(processor);

        processors.remove(processor);
    }

    private void removeAllHttpMessageProcessors()
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
            exproxy.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            CentralErrorStation.proxy_port_taken_error();
        }
    }
}
