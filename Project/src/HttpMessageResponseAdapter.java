import com.exproxy.HttpMessage;
import com.exproxy.HttpMessageResponse;
import com.exproxy.HttpMessageBody;

import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.ProtocolVersion;
import org.apache.http.Header;

import java.net.URL;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.ByteArrayInputStream; 
import java.io.ByteArrayOutputStream;

class HttpMessageResponseAdapter extends HttpMessageResponse
{
    private class HttpMessageResponseBodyAdapter implements HttpMessageBody
    {
        private byte[] data;

        public HttpMessageResponseBodyAdapter(HttpEntity entity)
        {
            try
            {
                InputStream input = entity.getContent();
                ByteArrayOutputStream data_list = new ByteArrayOutputStream();
                while(true)
                {
                    int data_byte = input.read();
                    // End of Stream
                    if(data_byte == -1)
                    {
                        break;
                    }
                    // Fine data
                    data_list.write(data_byte);
                }
                data = data_list.toByteArray();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
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

    private URL parse_url(String value)
    {
        try
        {
            return new URL(value);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public HttpMessageResponseAdapter(HttpMessage input, HttpResponse response)
    {
        // Construct super
        super();
        // Start setting ourself
        // http://reuse.pagesperso-orange.fr/exproxy/javadoc/com/exproxy/HttpMessageResponse.html
        // http://reuse.pagesperso-orange.fr/exproxy/javadoc/com/exproxy/HttpMessage.html
        //
        // Use; http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/HttpResponse.html
        // Set recieve information
        setToHost(input.getToHost());
        setToPort(input.getToPort());

        // Set sender information
        if(response.containsHeader("Host"))
        {
            // There's atleast 1
            Header[] host_headers = response.getHeaders("Host");
            // If there's more than 1
            if(host_headers.length > 1)
            {
                System.err.println("MULTIPLE HOST HEADERS?");
            }
            else
            {
                Header host_header = host_headers[0];
                String value = host_header.getValue();
                URL url_value = parse_url(value);

                String host = url_value.getHost();
                setFromHost(host);
                
                int port = url_value.getPort();
                if(port != -1)
                {
                    setFromPort(port);
                }
                else
                {
                    setFromPort(80);
                }
            }
        }
        else
        {
            System.err.println("No Host header?");
        }

        // Headers
        Header[] headers = response.getAllHeaders();
        Map<String,List<String>> set_headers = new HashMap<String,List<String>>();
        for(Header h : headers)
        {
            // Required datastructures
            String key = null;
            List<String> value = new ArrayList<String>();
            // Fill the above with real data
            key = h.getName();
            value.add(h.getValue());
            // Add it to the map
            set_headers.put(key, value);
        }
        setHeaders(set_headers);

        // Body
        HttpEntity entity = response.getEntity();
        setBody(new HttpMessageResponseBodyAdapter(entity));

        // Status
        StatusLine status = response.getStatusLine();
        setStatusCode(status.getStatusCode());
        setReasonPhrase(status.getReasonPhrase());

        // Version
        ProtocolVersion protocol_version = response.getProtocolVersion();
        setProtocol(protocol_version.getProtocol());
        setVersion(protocol_version.getMajor() + "." + protocol_version.getMinor());
    }
}
