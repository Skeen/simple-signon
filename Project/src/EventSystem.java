import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class EventSystem
{
    public static final String LOAD_SERVICE         = "LOAD_SERVICE";
    public static final String CLEAR_SERVICES       = "CLEAR_SERVICES";
    public static final String ADD_EVENT            = "ADD_EVENT";
    public static final String ADD_SERVICE_EVENT    = "ADD_SERVICE_EVENT";
    public static final String REMOVE_EVENT         = "REMOVE_EVENT";
    public static final String EDIT_EVENT           = "EDIT_EVENT";
    public static final String EDIT_ACCEPT_EVENT    = "EDIT_ACCEPT_EVENT";
    public static final String RECONNECT_EVENT      = "RECONNECT_EVENT";
    public static final String REFRESH_EVENT        = "REFRESH_EVENT";
    public static final String LOGOUT_EVENT         = "LOGOUT_EVENT";
    public static final String DOUBLE_CLICK         = "DOUBLE_CLICK";
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

    public void addListener(String event, EventListener listen)
    {
        // Get the list of listeners
        List<EventListener> listeners = getList(event);
        // Add us
        listeners.add(listen);
    }

    public boolean removeListener(String event, EventListener listen)
    {
        // Get the list of listeners
        List<EventListener> listeners = getList(event);
        // Add us
        return listeners.remove(listen);
    }

    public void trigger_event(String event, Object payload)
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
