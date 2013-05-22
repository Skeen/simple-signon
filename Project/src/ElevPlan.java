import com.exproxy.processors.HttpMessageProcessor;

import com.exproxy.HttpMessage;
import com.exproxy.HttpMessageRequest;
import com.exproxy.HttpMessageMethod;
import com.exproxy.HttpMessageBody;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import java.io.ByteArrayInputStream;

class ElevPlan implements HttpMessageProcessor
{
    private Map<String,String> initMap;

    public ElevPlan(Map<String,String> input)
    {
        this.initMap = input;
    }

    private class ElevPlanRequest extends HttpMessageRequest
    {
        private HttpMessageRequest old_request = null;

        public ElevPlanRequest(HttpMessageRequest request, final String brugernavn, final String password)
        {
            this.old_request = request;

            // Body
            setBody(new HttpMessageBody()
                    {
                        private byte[] data = new String("brugernavn=" + brugernavn + "&adgangskode=" + password + "&LOG+IND=Log+ind&uniloginbrugernavn=&uniloginbrugernavnold=&verifikation=").getBytes();

                        public byte[] getContent()
                        {
                            return data;
                        }

                        public ByteArrayInputStream	getContentStream()
                        {
                            return new ByteArrayInputStream(data);
                        }
                    });

            // setFrom(Host/Port)
            setFromHost(old_request.getFromHost());
            setFromPort(old_request.getFromPort());

            // Headers
            Map<String,List<String>> set_headers = new HashMap<String,List<String>>();

            set_headers.put("content-type", Arrays.asList(new String[]{"application/x-www-form-urlencoded"}));
            set_headers.put("cookie", Arrays.asList(new String[]{"ASPSESSIONIDASSQRQSQ=FFBGFJMADBKAMHNCFOJJCKPG; BALANCEID=mycluster.node2; ASP.NET_SessionId=n13kxj45s3e41cubw3cyufah; V%5FAUO=ROLLETYPE=MED&ERPRODSKOLE=N&PERSONID=1535855&SKOLEID=177&ROLLEID=42782&BRUGERNAVN=lanie962&ERUEVEJL=N&uniloginbrugernavn=&SKOLENR=203&LINIERPRSIDE=50; NoFormRestorePages="}));
            set_headers.put("connection", Arrays.asList(new String[]{"keep-alive"}));

            set_headers.put("accept-language", Arrays.asList(new String[]{"da","en-us;q=0.7","en;q=0.3"}));
            set_headers.put("host", Arrays.asList(new String[]{"www.elevplan.dk"}));
            set_headers.put("content-length", Arrays.asList(new String[]{"116"}));
            set_headers.put("accept", Arrays.asList(new String[]{"text/html","application/xhtml+xml","application/xml;q=0.9","*/*;q=0.8"}));
            set_headers.put("user-agent", Arrays.asList(new String[]{"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0"}));
            set_headers.put("referer", Arrays.asList(new String[]{"https://www.elevplan.dk/offentlig/Default.aspx"}));

            setHeaders(set_headers);

            // Protocol
            setProtocol("HTTP");

            // setTo(Host/Port)
            setToHost("www.elevplan.dk");
            setToPort(443);

            // Version
            setVersion("1.1");

            // Method
            setMethod(HttpMessageMethod.POST);

            // URI
            setUri("/app/moduler/logon/plogon.asp");
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
        if(input instanceof HttpMessageRequest)
        {
            HttpMessageRequest request = (HttpMessageRequest) input;
            /*
            System.out.println("HOST: " + request.getToHost());
            System.out.println("URI: " + request.getUri());
            */
            if(request.getToHost().toLowerCase().equals("www.elevplan.dk") &&
               request.getUri().equals("/SSO_AUTO_LOGIN"))
            {
                return new ElevPlanRequest(request, initMap.get("USERNAME"), initMap.get("PASSWORD"));
            }
        }
        return input;
    }
}
