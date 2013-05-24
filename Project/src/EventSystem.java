import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class EventSystem
{
    //SSOAdd and SSOWindow add the service depending on whether they are active or not.
    public static final String LOAD_SERVICE         = "LOAD_SERVICE";
    
    //Clears services from lists held by SSOAdd and SSOWindow
    public static final String CLEAR_SERVICES       = "CLEAR_SERVICES";
    
    //Add button in SSOWindow was pushed. Show SSOAdd 
    public static final String ADD_EVENT            = "ADD_EVENT";
    
    //Adds a service to list of active services, which is held by SSOWindow
    public static final String ADD_SERVICE_EVENT    = "ADD_SERVICE_EVENT";
    
    //Removes a service from the list held by SSOWindow, it is added to list held by SSOAdd.
    public static final String REMOVE_EVENT         = "REMOVE_EVENT";
    
    //Edit button in SSOWindow was pushed. Show SSOEdit
    public static final String EDIT_EVENT           = "EDIT_EVENT";
    
    //Accept button in SSOEdit was pushed.
    public static final String EDIT_ACCEPT_EVENT    = "EDIT_ACCEPT_EVENT";
    
    //Reconnect button in SSOWindow was pushed
    public static final String RECONNECT_EVENT      = "RECONNECT_EVENT";
    
    //Refresh button in SSOWindow was pushed
    public static final String REFRESH_EVENT        = "REFRESH_EVENT";
    
    //Logout button in SSOWidnow was pushed
    public static final String LOGOUT_EVENT         = "LOGOUT_EVENT";
    
    //Double click or enter on a service in SSOWindow was detected. Open the page for that service
    public static final String SERVICE_ACTIVATE     = "SERVICE_ACTIVATE";
    
    //Update the various GUIs to show new state of services, etc
    public static final String UPDATE_GUI           = "UPDATE_GUI";

    public interface EventListener
    {
        public void event(String event, Object payload);
    }

    private Map<String, List<EventListener>> event_listeners;

    private static EventSystem eventsystem = new EventSystem();
    public static EventSystem getSingleton()
    {
        return eventsystem;
    }
    
    private EventSystem()
    {
        event_listeners = new HashMap<String, List<EventListener>>();
    }

    private List<EventListener> getList(String event)
    {
        // Ensure the table exists
        if(event_listeners.containsKey(event) == false)
        {
            event_listeners.put(event, new ArrayList<EventListener>());
        }
        return event_listeners.get(event);
    }

    public synchronized void addListener(String event, EventListener listen)
    {
        // Get the list of listeners
        List<EventListener> listeners = getList(event);
        // Add us
        listeners.add(listen);
    }

    public synchronized boolean removeListener(String event, EventListener listen)
    {
        // Get the list of listeners
        List<EventListener> listeners = getList(event);
        // Add us
        return listeners.remove(listen);
    }

    public synchronized void trigger_event(String event, Object payload)
    {
        // Get the list of listeners
        List<EventListener> listeners = getList(event);
        // Add us
        for(EventListener listen : listeners)
        {
            listen.event(event, payload);
        }
    }
}
