import com.exproxy.processors.HttpMessageProcessor;

import com.exproxy.HttpMessage;
import com.exproxy.HttpMessageRequest;
import com.exproxy.HttpMessageResponse;
import com.exproxy.HttpMessageMethod;
import com.exproxy.HttpMessageBody;

import java.util.List;
import java.util.ArrayList;

import java.io.ByteArrayInputStream;

class ElevPlanModifier implements HttpMessageProcessor
{
    private class InjectionHttpMessageBody implements HttpMessageBody
    {
        private byte[] data;

        public InjectionHttpMessageBody(HttpMessage input, String injection_code)
        {
            byte[] old_data = input.getBody().getContent();
            byte[] injection_data = injection_code.getBytes();

            data = new byte[old_data.length + injection_data.length];
            /*
            System.arraycopy(old_data, 0, data, 0, old_data.length);
            System.arraycopy(injection_data, 0, data, old_data.length, injection_data.length);
            */
            System.arraycopy(injection_data, 0, data, 0, injection_data.length);
            System.arraycopy(old_data, 0, data, injection_data.length, old_data.length);
        }

        public byte[] getContent()
        {
            return data;
        }

        public ByteArrayInputStream	getContentStream()
        {
            return new ByteArrayInputStream(data);
        }
    }


    private List<Integer> active_ports = new ArrayList<Integer>();

    public ElevPlanModifier()
    {
    }

    public boolean doContinue(HttpMessage input)
    {
        return true;
    }

    public boolean doSend(HttpMessage input)
    {
        return true;
    }

    public HttpMessage process(HttpMessage input)
    {
        if(input instanceof HttpMessageRequest)
        {
            HttpMessageRequest request = (HttpMessageRequest) input;

            if(request.getToHost().toLowerCase().equals("www.elevplan.dk") &&
               request.getUri().toLowerCase().equals("/offentlig/default.aspx"))
            {
                int response_port = request.getFromPort();
                active_ports.add(response_port);
                
                System.out.println("REQUEST_PORT: " + response_port);
            }
        }
        else if(input instanceof HttpMessageResponse)
        {
            HttpMessageResponse response = (HttpMessageResponse) input;

            int response_port = response.getToPort();
            boolean contains = active_ports.contains(response_port);
            if(contains)
            {
                // Add a login link button
                String html_injection_code = "<FORM>" +
                                                "<INPUT Type=\"BUTTON\" VALUE=\"SSO Login\" ONCLICK=\"window.location.href='https://www.elevplan.dk/SSO_AUTO_LOGIN'\">" + 
                                             "</FORM>";
                // TODO: parse response with jsoup (http://jsoup.org/cookbook/modifying-data/set-html),
                // and insert the injection code, using the same
                response.setBody(new InjectionHttpMessageBody(input, html_injection_code));

                System.out.println("RESPONSE_PORT: " + response_port);

                // Clean up
                active_ports.remove((Integer)response_port);

                return response;
            }
        }
        return input;
    }
}
