import java.net.URI;
import java.awt.Desktop;

import java.util.Map;

class WebServiceType implements ServiceType
{
    private URI webpage = null;

    public WebServiceType(Map<String,String> input)
    {
        String webpage = input.get("URL");
        try
        {
            this.webpage = new URI(webpage);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Service.Status getStatus()
    {
        return Service.Status.NOTCONNECTED;
    }

    public void connect()
    {
    }

    public void double_click()
    {
        openWebpage(webpage);
    }

    public void disconnect()
    {
    }

    private boolean openWebpage(URI uri)
    {
        Desktop desktop = null;
        if(Desktop.isDesktopSupported())
        {
            desktop = Desktop.getDesktop();
            if(desktop.isSupported(Desktop.Action.BROWSE))
            {
                try 
                {
                    desktop.browse(uri);
                    return true;
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
