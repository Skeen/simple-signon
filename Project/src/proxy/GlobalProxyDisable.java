package proxy;

import com.exproxy.HttpMessage;
import com.exproxy.processors.HttpMessageProcessor;

class GlobalProxyDisable implements HttpMessageProcessor
{
    // Global lockdown code
    public static boolean is_global_disabled = false;

    public static synchronized boolean is_disabled()
    {
        return is_global_disabled;
    }

    public static synchronized void disable()
    {
        is_global_disabled = true;
    } 
    
    public static synchronized void enable()
    {
        is_global_disabled = false;
    } 

    // Constructor, variables and methods
    private HttpMessageProcessor processor = null;

    public GlobalProxyDisable(HttpMessageProcessor processor)
    {
        this.processor = processor;
    }

    public boolean doContinue(HttpMessage input)
    {
        // Don't run the processor
        if(is_disabled())
        {
            return true;
        }
        else
        {
            return processor.doContinue(input);
        }
    }

    public boolean doSend(HttpMessage input)
    {
        // Don't run the processor
        if(is_disabled())
        {
            return true;
        }
        else
        {
            return processor.doSend(input);
        }
    }

    public HttpMessage process(HttpMessage input)
    {
        // Don't run the processor
        if(is_disabled())
        {
            return input;
        }
        else
        {
            return processor.process(input);
        }
    }
}
