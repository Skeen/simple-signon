import java.awt.*;
import java.util.Random;

import javax.swing.JPanel;

import com.exproxy.processors.HttpMessageProcessor;

class Service implements Runnable, EventSystem.EventListener
{
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
    
    private EventSystem eventSystem;
    
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

    public Service(Service dependency, String name, String logo_path, ServiceType service_type, boolean auto_connect, boolean in_use)
    {
        this.dependency = dependency;
        this.name = name;
        this.logo = Utilities.loadImage(logo_path);
        if(service_type == null)
        {
            type = new DefaultServiceType(null);
        }
        else
        {
            type = service_type;
        }
        this.auto_connect = auto_connect;
        this.connect = auto_connect;
        this.in_use = in_use;
        this.status = Status.NOTCONNECTED;
        
        eventSystem = EventSystem.getSingleton();
        eventSystem.addListener(EventSystem.REMOVE_EVENT, this);
        eventSystem.addListener(EventSystem.RECONNECT_EVENT, this);
        eventSystem.addListener(EventSystem.EDIT_ACCEPT_EVENT, this);
        
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
            default:
                break;
        }
    }
    
    public HttpMessageProcessor getHttpProcessor()
    {
        // TODO: REMOVE HARD_CODING
        if(type instanceof WebServiceType)
        {
            return new CompositeHttpMessageProcessor(new ElevPlan(), new ElevPlanModifier());
        }
        return null;
    }

    public void double_click()
    {
        System.out.println("DOUBLE CLICK ON " + name);
        type.double_click();
    }

    private void do_callback_if_status_changed(Status s)
    {
        if(s != status)
        {
            EventSystem eventSystem = EventSystem.getSingleton();
            status = s;
            eventSystem.trigger_event("UPDATE_GUI", this);
            System.out.println("CALLBACK");
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
}
