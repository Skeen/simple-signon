import com.exproxy.processors.HttpMessageProcessor;

import com.exproxy.HttpMessage;

import java.util.List;
import java.util.ArrayList;

class CompositeHttpMessageProcessor implements HttpMessageProcessor
{
    List<HttpMessageProcessor> processors = new ArrayList<HttpMessageProcessor>(); 

    public CompositeHttpMessageProcessor()
    {
    }

    public CompositeHttpMessageProcessor(HttpMessageProcessor processor1, HttpMessageProcessor processor2)
    {
        processors.add(processor1);
        processors.add(processor2);
    }

    public CompositeHttpMessageProcessor(List<HttpMessageProcessor> processors)
    {
        this.processors = processors;
    }

    public boolean doContinue(HttpMessage input)
    {
        boolean b = true;
        for(HttpMessageProcessor p : processors)
        {
            b = b && p.doContinue(input);
        }
        return b;
    }

    public boolean doSend(HttpMessage input)
    {
        boolean b = true;
        for(HttpMessageProcessor p : processors)
        {
            b = b && p.doSend(input);
        }
        return b;
    }

    public HttpMessage process(HttpMessage input)
    {
        for(HttpMessageProcessor p : processors)
        {
            input = p.process(input);
        }
        return input;
    }
}
