import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;

import com.exproxy.HttpMessage;
import com.exproxy.HttpMessageRequest;
import com.exproxy.HttpMessageResponse;
import com.exproxy.processors.HttpMessageProcessor;

import java.util.List;
import java.util.Map;

class DumpHttpMessage implements HttpMessageProcessor
{
    public static void dumpInfo(HttpMessage input)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(File.createTempFile("log",".log",new File("logs"))));

            writer.write("!!!DUMPING INFO!!!");
            writer.newLine();
            writer.newLine();

            writer.write("getBody().getContent():");
            writer.newLine();
            byte[] bytes = input.getBody().getContent();
            char[] chars = new String(bytes).toCharArray();
            writer.write(chars, 0, chars.length);
            writer.newLine();
            writer.newLine();

            writer.write("getFromHost():");
            writer.newLine();
            writer.write(input.getFromHost());
            writer.newLine();
            writer.newLine();

            writer.write("getFromPort():");
            writer.newLine();
            writer.write("" + input.getFromPort());
            writer.newLine();        
            writer.newLine();

            writer.write("getHeaders():");
            writer.newLine();
            Map<String,List<String>> headers = input.getHeaders();
            for (Map.Entry<String, List<String>> entry : headers.entrySet())
            {
                writer.write("KEY: " + entry.getKey());
                writer.newLine();
                writer.write("VALUES: ");
                for(String str :  entry.getValue())
                {
                    writer.write(str + ",");
                }
                writer.newLine();
            }
            writer.newLine();

            writer.write("getHeadersString():");
            writer.newLine();
            writer.write(input.getHeadersString());
            writer.newLine();
            writer.newLine();

            writer.write("getProtocol():");
            writer.newLine();
            writer.write(input.getProtocol());
            writer.newLine();
            writer.newLine();

            writer.write("getStartLine():");
            writer.newLine();
            writer.write(input.getStartLine());
            writer.newLine();
            writer.newLine();

            writer.write("getToHost():");
            writer.newLine();
            writer.write(input.getToHost());
            writer.newLine();
            writer.newLine();

            writer.write("getToPort():");
            writer.newLine();
            writer.write("" + input.getToPort());
            writer.newLine();
            writer.newLine();

            writer.write("getVersion():");
            writer.newLine();
            writer.write("" + input.getVersion());
            writer.newLine();
            writer.newLine();

            if(input instanceof HttpMessageRequest)
            {
                HttpMessageRequest request = (HttpMessageRequest) input;

                writer.write("getMethod():");
                writer.newLine();
                writer.write(request.getMethod().toString());
                writer.newLine();
                writer.newLine();

                writer.write("getStartLine():");
                writer.newLine();
                writer.write(request.getStartLine());
                writer.newLine();
                writer.newLine();

                writer.write("getUri():");
                writer.newLine();
                writer.write(request.getUri());
                writer.newLine();
                writer.newLine();
            }
            else if(input instanceof HttpMessageResponse)
            {
                HttpMessageResponse response = (HttpMessageResponse) input;

                writer.write("getReasonPhrase():");
                writer.newLine();
                writer.write(response.getReasonPhrase());
                writer.newLine();
                writer.newLine();

                writer.write("getRFCReasonPhrase():");
                writer.newLine();
                writer.write(response.getRFCReasonPhrase());
                writer.newLine();
                writer.newLine();

                writer.write("getStartLine():");
                writer.newLine();
                writer.write(response.getStartLine());
                writer.newLine();
                writer.newLine();

                writer.write("getStatusCode():");
                writer.newLine();
                writer.write(response.getStatusCode());
                writer.newLine();
                writer.newLine();
            }

            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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
        dumpInfo(input);
        System.out.println("DUMPED!");
        return input;
    }
}
