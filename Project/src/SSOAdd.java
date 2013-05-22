import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

public class SSOAdd implements EventSystem.EventListener
{
    
    private static SSOAdd singleton = new SSOAdd();
    public static SSOAdd getSingleton()
    {
        return singleton;
    }
    
    private EventSystem eventSystem;
    private JFrame frame;
    private JList<Service> list;
    private DefaultListModel<Service> listModel;
    
    private SSOAdd()
    {
        listModel = new DefaultListModel<Service>();
        eventSystem = EventSystem.getSingleton();
        
        eventSystem.addListener(EventSystem.LOAD_SERVICE, this);
        eventSystem.addListener(EventSystem.CLEAR_SERVICES, this);
        eventSystem.addListener(EventSystem.REMOVE_EVENT, this);
        
        create();
    }
    
    private void create()
    {
        frame = new JFrame("SSO tilføj");
        
        list = new JList<Service>(listModel);
        
        //Show service name and alternate between white/light-gray backgrounds
        list.setCellRenderer(new DefaultListCellRenderer()
			{
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
				{
                    JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    
                    Service s = (Service) value;
                    setText(s.getName());
                    
					if ((index & 1) == 0) // is index even?
                    {
                        setBackground(Color.WHITE);
                    }
                    else
                    {
                        setBackground(Color.LIGHT_GRAY);
                    }
                    
					return label;
				}
			});
        
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(list.VERTICAL);
        list.setFixedCellHeight(20);
        
		JScrollPane scrollableList = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        list.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    Service s = list.getSelectedValue();
                    if (s != null)
                    {
                        hideGUI();
                        list.clearSelection();
                        eventSystem.trigger_event("ADD_SERVICE_EVENT", s);
                    }
                }
            });
        
        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(list);
        
        frame.pack();
        frame.setResizable( false );
        
    }
    
    public void event(String event, Object payload)
    {
        switch(event)
        {
            case EventSystem.LOAD_SERVICE:
                loadService((Service) payload);
                break;
            case EventSystem.CLEAR_SERVICES:
                clearServices();
                break;
            case EventSystem.REMOVE_EVENT:
                listModel.addElement((Service) payload);
                break;
            default:
                break;
        }
    }
    
    public void clearServices()
    {
        listModel.clear();
    }
    
    public void loadService(Service s)
    {
        if(! s.isUsed())
        {
            listModel.addElement(s);
        }
    }
    
    public void showGUI()
    {
        frame.setVisible(true);
    }

    public void hideGUI()
    {
        frame.setVisible(false);
    }
}