import java.awt.*;
import java.util.Random;

import com.exproxy.processors.HttpMessageProcessor;

import java.util.Map;
import java.util.HashMap;

import java.lang.Class;
import java.lang.reflect.Constructor;

class Service implements Runnable, EventSystem.EventListener 
{
    // The UserService ID
    private int user_service_id;
    // The Status of the service
    private Status status;
    // The service (if any) that this one depends on
    private Service dependency;
    // The name of this service
    private String name;
    // The logo for this service
    private Image logo;
    // The type of this service
    private ServiceType type;
    // Whether this service is auto-connect
    private boolean auto_connect;
    // Whether this service is in list of used services
    private boolean in_use;
    // The proxy filter (if any)
    private HttpMessageProcessor proxy_filter;
    // The configuration map
    private Map<String, String> configuration_map;
    
    public enum Status {
        NOTCONNECTED, // Not going to
        DISCONNECTED, // Was connected, lost connection
        CONNECTING,   // (re)connecting
        CONNECTED     // Connected
    }

    // Make an enum class, and a to string method on that
    private String status_to_string(Status s)
    {
        switch(s)
        {
            case NOTCONNECTED:
                return "Not Connected";
            case DISCONNECTED:
                return "Disconnected";
            case CONNECTING:
                return "Connecting";
            case CONNECTED:
                return "Connected";
            // This will never happen
            default:
                return null;
        }
    }

    private boolean connect;

    public Service(int user_service_id, Service dependency, String name, String logo_path, ServiceType service_type, boolean auto_connect, boolean in_use, Map<String, String> initMap)
    {
        this.user_service_id = user_service_id;
        this.dependency = dependency;
        this.name = name;
        this.logo = Utilities.loadImage(logo_path);
        this.type = service_type;
        this.auto_connect = auto_connect;
        this.connect = auto_connect;
        this.in_use = in_use;
        this.status = Status.NOTCONNECTED;
        this.configuration_map = initMap;
        
        // Register with the event system
        EventSystem eventSystem = EventSystem.getSingleton();
        eventSystem.addListener(EventSystem.REMOVE_EVENT, this);
        eventSystem.addListener(EventSystem.RECONNECT_EVENT, this);
        eventSystem.addListener(EventSystem.EDIT_ACCEPT_EVENT, this);
        eventSystem.addListener(EventSystem.SERVICE_ACTIVATE, this);

        // ProxyFilter code
        proxy_filter = null;
        if(type instanceof WebServiceType)
        {
            if(initMap.containsKey("PROXY_FILTER_CLASS"))
            {
                try
                {
                    String proxy_filter_name = initMap.get("PROXY_FILTER_CLASS");
                    Class proxy_filter_class = Class.forName(proxy_filter_name);
                    Constructor proxy_filter_constructor = proxy_filter_class.getConstructor(Map.class);
                    proxy_filter = (HttpMessageProcessor) proxy_filter_constructor.newInstance(initMap);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            //proxy_filter = new CompositeHttpMessageProcessor(new ElevPlan(initMap), new ElevPlanModifier());
        }
        
        if(auto_connect)
        {
            new Thread(new Runnable() 
                    {
                        public void run() {
                            type.connect();
                        }
                    }).start();
        }
    }

    public Map<String,String> getConfigurationMap()
    {
        return configuration_map;
    }

    public int getUserServiceID()
    {
        return user_service_id;
    }
    
    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.REMOVE_EVENT:
                disconnect();
                break;
            case EventSystem.RECONNECT_EVENT:
                reconnect();
                break;
            case EventSystem.EDIT_ACCEPT_EVENT:
                edit(payload);
                break;
            case EventSystem.SERVICE_ACTIVATE:
                // Only the clicked service, trigger the events effect
                if(payload == this)
                {
                    type.double_click();
                }
                break;
            default:
                break;
        }
    }
    
    public HttpMessageProcessor getProxyFilter()
    {
        return proxy_filter;
    }

    private void do_callback_if_status_changed(Status s)
    {
        if(s != status)
        {
            status = s;
            EventSystem eventSystem = EventSystem.getSingleton();
            eventSystem.trigger_event("UPDATE_GUI", this);
            // Show info at the tray icon
            if(status == Status.DISCONNECTED)
            {
                // Show an error at the tray icon
                SSOTray tray = SSOTray.getSingleton();
                tray.showError(name + " " + status_to_string(status));
            }
            else
            {
                // Show an error at the tray icon
                SSOTray tray = SSOTray.getSingleton();
                tray.showInfo(name + " " + status_to_string(status));
            }
        }
    }

    public void run()
    {
        while(true)
        {
            Utilities.delay(1000);
            Status new_status = type.getStatus();
            if(new_status == Status.DISCONNECTED && auto_connect == false)
            {
                new_status = Status.NOTCONNECTED;
            }
            do_callback_if_status_changed(new_status);
        }
    }
    
    private void connect()
    {
        connect = true;
        type.connect();
    }

    private void disconnect()
    {
        connect = false;
        type.disconnect();
    }
    
    private void reconnect()
    {
        if(connect)
        {
            disconnect();
        }
        else
        {
            connect();
        }
    }
    
    private void edit(Object payload)
    {
    }
    
    // Return whether this service is set to be connected.
    public boolean getConnectStatus()
    {
        return connect;
    }

    public Image getLogo()
    {
        return logo;
    }

    public String getName()
    {
        return name;
    }

    public Status getStatus()
    {
        return status;
    }
    
    public boolean isUsed()
    {
        return in_use;
    }
    
    public boolean autoconnect()
    {
        return auto_connect;
    }

    public void set_autoconnect(boolean b)
    {
        auto_connect = b;
    }
}
