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

class ItsLearning implements HttpMessageProcessor
{
    private Map<String,String> initMap;

    public ItsLearning(Map<String,String> input)
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
                        private byte[] data = new String("__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUKMTc0OTEyMzAxMg9kFgIFBEZvcm0PZBYGAgEPZBYEZg9kFgICAQ9kFgxmDxUBEWN0bDAwX2RycEN1c3RvbWVyZAICD2QWAgIBDxYCHgRUZXh0BQtBQVJIVVMgVEVDSGQCBA8PFgIeB1Zpc2libGVoZBYCAgEPDxYCHwBlZGQCBQ8PFgIfAWhkFgYCAxAQDxYCHgtfIURhdGFCb3VuZGdkZBYAMtwEAAEAAAD%2F%2F%2F%2F%2FAQAAAAAAAAAEAQAAAOIBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuRGljdGlvbmFyeWAyW1tTeXN0ZW0uU3RyaW5nLCBtc2NvcmxpYiwgVmVyc2lvbj0yLjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldLFtTeXN0ZW0uU3RyaW5nLCBtc2NvcmxpYiwgVmVyc2lvbj0yLjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODldXQMAAAAHVmVyc2lvbghDb21wYXJlcghIYXNoU2l6ZQADAAiSAVN5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkdlbmVyaWNFcXVhbGl0eUNvbXBhcmVyYDFbW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTIuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dCAAAAAAJAgAAAAAAAAAEAgAAAJIBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuR2VuZXJpY0VxdWFsaXR5Q29tcGFyZXJgMVtbU3lzdGVtLlN0cmluZywgbXNjb3JsaWIsIFZlcnNpb249Mi4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1iNzdhNWM1NjE5MzRlMDg5XV0AAAAAC2QCBQ8PFgIfAWhkFgICAQ8PFgIfAAUIRm9ydHPDpnRkZAIHDw8WAh8BZ2QWAgIBDxAPFgIeB0NoZWNrZWRnZGRkZAIGDw8WAh8BZxYCHgVzdHlsZQUhbWFyZ2luLXRvcDoyMHB4O3BhZGRpbmctbGVmdDowcHg7FgYCAQ8PFgIfAAULQnJ1Z2VybmF2bjpkZAIHDw8WAh8ABQxBZGdhbmdza29kZTpkZAINDw8WAh8ABQdMb2cgcMOlZGQCCA8PFgIfAWdkZAIFD2QWAgIBD2QWAgIBDxYCHgtfIUl0ZW1Db3VudAIFFgpmD2QWAmYPFQIdL2hlbHAvdHV0b3JpYWxzL3R1dG9yaWFscy5odG0JVHV0b3JpYWxzZAIBD2QWAmYPFQIXaHR0cDovL2l0c2xlYXJuaW5nLm1vYmkMTW9iaWxlIGxvZ2luZAICD2QWAmYPFQIcaHR0cHM6Ly9leGFtLml0c2xlYXJuaW5nLmNvbQpFeGFtIGxvZ2luZAIDD2QWAmYPFQIfaHR0cHM6Ly9zdXBwb3J0Lml0c2xlYXJuaW5nLmNvbQlIZWxwIGRlc2tkAgQPZBYCZg8VAixodHRwczovL3d3dy5pdHNsZWFybmluZy5jb20vQ2xlYW5Db29raWUuYXNweBlDbGVhbiBpdHNsZWFybmluZyBjb29raWVzZAIDD2QWBgIBD2QWBGYPZBYCZg8WAh4FY2xhc3MFBHNraW5kAgEPZBYCZg8WAh8GBRFjb250YWluZXJmdW5jdGlvbmQCAw9kFgRmD2QWAmYPFgIfBgUEc2tpbmQCAQ9kFgJmDxYCHwYFEWNvbnRhaW5lcmZ1bmN0aW9uZAIFD2QWBmYPZBYCZg8WAh8GBQRza2luZAIBD2QWAmYPFgIfBgURY29udGFpbmVyZnVuY3Rpb24WAmYPDxYKHgtOYXZpZ2F0ZVVybAUnL2Vwb3J0Zm9saW8vbWlzYy9Ccm93c2VFcG9ydGZvbGlvcy5hc3B4HgZUYXJnZXRlHgdUb29sVGlwBRFTw7hnIGkgZVBvcnRmb2xpbx4IQ3NzQ2xhc3NkHgRfIVNCAgJkZAICD2QWAmYPZBYWZg9kFgRmDxYCHglpbm5lcmh0bWwFBE5hdm5kAgEPFgIfDAULQWRnYW5nc2tvZGVkAgEPZBYEZg8WBB8MBckBPGEgaHJlZj0iLzczNDgvIiBjbGFzcz0iaWNvbmFuZGxpbmsiIHRhcmdldD0iX2JsYW5rIj48aW1nIHNyYz0iaHR0cHM6Ly9zdGF0aWNzLml0c2xlYXJuaW5nLmNvbS92My40MS4xLjE2MS9pY29ucy94cC9lcG9ydGZvbGlvMTYucG5nIiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIGFsdD0iIiAvPjxzcGFuPk1vcnRlbiZuYnNwO0phcmxhazwvc3Bhbj48L2E%2BHgdoZWFkZXJzBRlFUG9ydGZvbGlvTGlzdF9jdGwwMV9uYW1lZAIBDxYEHwxlHw0FHUVQb3J0Zm9saW9MaXN0X2N0bDAxX3Bhc3N3b3JkZAICDxYCHwYFCWFsdGVybmF0ZRYEZg8WBB8MBc0BPGEgaHJlZj0iL2FsaW4wMzg3LyIgY2xhc3M9Imljb25hbmRsaW5rIiB0YXJnZXQ9Il9ibGFuayI%2BPGltZyBzcmM9Imh0dHBzOi8vc3RhdGljcy5pdHNsZWFybmluZy5jb20vdjMuNDEuMS4xNjEvaWNvbnMveHAvZXBvcnRmb2xpbzE2LnBuZyIgd2lkdGg9IjE2IiBoZWlnaHQ9IjE2IiBhbHQ9IiIgLz48c3Bhbj5BbGluYSZuYnNwO0JhdW1hbmU8L3NwYW4%2BPC9hPh8NBRlFUG9ydGZvbGlvTGlzdF9jdGwwMV9uYW1lZAIBDxYEHwxlHw0FHUVQb3J0Zm9saW9MaXN0X2N0bDAxX3Bhc3N3b3JkZAIDD2QWBGYPFgQfDAXQATxhIGhyZWY9Ii9BU00vIiBjbGFzcz0iaWNvbmFuZGxpbmsiIHRhcmdldD0iX2JsYW5rIj48aW1nIHNyYz0iaHR0cHM6Ly9zdGF0aWNzLml0c2xlYXJuaW5nLmNvbS92My40MS4xLjE2MS9pY29ucy94cC9lcG9ydGZvbGlvMTYucG5nIiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIGFsdD0iIiAvPjxzcGFuPkFsZXhhbmRlciBTZWpyJm5ic3A7TWFkc2VuPC9zcGFuPjwvYT4fDQUZRVBvcnRmb2xpb0xpc3RfY3RsMDFfbmFtZWQCAQ8WBB8MZR8NBR1FUG9ydGZvbGlvTGlzdF9jdGwwMV9wYXNzd29yZGQCBA8WAh8GBQlhbHRlcm5hdGUWBGYPFgQfDAXVATxhIGhyZWY9Ii8xNTc2Mi8iIGNsYXNzPSJpY29uYW5kbGluayIgdGFyZ2V0PSJfYmxhbmsiPjxpbWcgc3JjPSJodHRwczovL3N0YXRpY3MuaXRzbGVhcm5pbmcuY29tL3YzLjQxLjEuMTYxL2ljb25zL3hwL2Vwb3J0Zm9saW8xNi5wbmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgYWx0PSIiIC8%2BPHNwYW4%2BUnVuZSBNw7hsbGVyJm5ic3A7QWxicmVjaHRzZW48L3NwYW4%2BPC9hPh8NBRlFUG9ydGZvbGlvTGlzdF9jdGwwMV9uYW1lZAIBDxYEHwxlHw0FHUVQb3J0Zm9saW9MaXN0X2N0bDAxX3Bhc3N3b3JkZAIFD2QWBGYPFgQfDAXRATxhIGhyZWY9Ii9idWVuby8iIGNsYXNzPSJpY29uYW5kbGluayIgdGFyZ2V0PSJfYmxhbmsiPjxpbWcgc3JjPSJodHRwczovL3N0YXRpY3MuaXRzbGVhcm5pbmcuY29tL3YzLjQxLjEuMTYxL2ljb25zL3hwL2Vwb3J0Zm9saW8xNi5wbmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgYWx0PSIiIC8%2BPHNwYW4%2BQXNnZXIgSGF1dG9wJm5ic3A7RHJld3Nlbjwvc3Bhbj48L2E%2BHw0FGUVQb3J0Zm9saW9MaXN0X2N0bDAxX25hbWVkAgEPFgQfDGUfDQUdRVBvcnRmb2xpb0xpc3RfY3RsMDFfcGFzc3dvcmRkAgYPFgIfBgUJYWx0ZXJuYXRlFgRmDxYEHwwF0AE8YSBocmVmPSIvTUlLWi8iIGNsYXNzPSJpY29uYW5kbGluayIgdGFyZ2V0PSJfYmxhbmsiPjxpbWcgc3JjPSJodHRwczovL3N0YXRpY3MuaXRzbGVhcm5pbmcuY29tL3YzLjQxLjEuMTYxL2ljb25zL3hwL2Vwb3J0Zm9saW8xNi5wbmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgYWx0PSIiIC8%2BPHNwYW4%2BTWlra2VsIFJvaGRlJm5ic3A7UmljaHRlcjwvc3Bhbj48L2E%2BHw0FGUVQb3J0Zm9saW9MaXN0X2N0bDAxX25hbWVkAgEPFgQfDGUfDQUdRVBvcnRmb2xpb0xpc3RfY3RsMDFfcGFzc3dvcmRkAgcPZBYEZg8WBB8MBdEBPGEgaHJlZj0iL2Nhb2wvIiBjbGFzcz0iaWNvbmFuZGxpbmsiIHRhcmdldD0iX2JsYW5rIj48aW1nIHNyYz0iaHR0cHM6Ly9zdGF0aWNzLml0c2xlYXJuaW5nLmNvbS92My40MS4xLjE2MS9pY29ucy94cC9lcG9ydGZvbGlvMTYucG5nIiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIGFsdD0iIiAvPjxzcGFuPkNhcm9saW5lIEFtYWxpZSZuYnNwO09sc2VuPC9zcGFuPjwvYT4fDQUZRVBvcnRmb2xpb0xpc3RfY3RsMDFfbmFtZWQCAQ8WBB8MZR8NBR1FUG9ydGZvbGlvTGlzdF9jdGwwMV9wYXNzd29yZGQCCA8WAh8GBQlhbHRlcm5hdGUWBGYPFgQfDAXIATxhIGhyZWY9Ii8yNzEwLyIgY2xhc3M9Imljb25hbmRsaW5rIiB0YXJnZXQ9Il9ibGFuayI%2BPGltZyBzcmM9Imh0dHBzOi8vc3RhdGljcy5pdHNsZWFybmluZy5jb20vdjMuNDEuMS4xNjEvaWNvbnMveHAvZXBvcnRmb2xpbzE2LnBuZyIgd2lkdGg9IjE2IiBoZWlnaHQ9IjE2IiBhbHQ9IiIgLz48c3Bhbj5PbGUmbmJzcDtOeXN0csO4bTwvc3Bhbj48L2E%2BHw0FGUVQb3J0Zm9saW9MaXN0X2N0bDAxX25hbWVkAgEPFgQfDGUfDQUdRVBvcnRmb2xpb0xpc3RfY3RsMDFfcGFzc3dvcmRkAgkPZBYEZg8WBB8MBdMBPGEgaHJlZj0iLzc3MzYvIiBjbGFzcz0iaWNvbmFuZGxpbmsiIHRhcmdldD0iX2JsYW5rIj48aW1nIHNyYz0iaHR0cHM6Ly9zdGF0aWNzLml0c2xlYXJuaW5nLmNvbS92My40MS4xLjE2MS9pY29ucy94cC9lcG9ydGZvbGlvMTYucG5nIiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIGFsdD0iIiAvPjxzcGFuPlN0dWRpZXBsYW5lciZuYnNwO0hUWCDDhXJodXM8L3NwYW4%2BPC9hPh8NBRlFUG9ydGZvbGlvTGlzdF9jdGwwMV9uYW1lZAIBDxYEHwxlHw0FHUVQb3J0Zm9saW9MaXN0X2N0bDAxX3Bhc3N3b3JkZAIKDxYCHwYFCWFsdGVybmF0ZRYEZg8WBB8MBdQBPGEgaHJlZj0iLzEzNzQ4LyIgY2xhc3M9Imljb25hbmRsaW5rIiB0YXJnZXQ9Il9ibGFuayI%2BPGltZyBzcmM9Imh0dHBzOi8vc3RhdGljcy5pdHNsZWFybmluZy5jb20vdjMuNDEuMS4xNjEvaWNvbnMveHAvZXBvcnRmb2xpbzE2LnBuZyIgd2lkdGg9IjE2IiBoZWlnaHQ9IjE2IiBhbHQ9IiIgLz48c3Bhbj5MaXNlIEhvdWtqw6ZyJm5ic3A7U8O4cmVuc2VuPC9zcGFuPjwvYT4fDQUZRVBvcnRmb2xpb0xpc3RfY3RsMDFfbmFtZWQCAQ8WBB8MZR8NBR1FUG9ydGZvbGlvTGlzdF9jdGwwMV9wYXNzd29yZGQCBA9kFgQCAw8WAh8BaGQCBw9kFgRmD2QWAmYPFgIfBgUEc2tpbmQCAQ9kFgJmDxYCHwYFEWNvbnRhaW5lcmZ1bmN0aW9uZGQ%2BVf%2BQlqqY2VVZksjmbbdCtHd1iQ%3D%3D&__EVENTVALIDATION=%2FwEWFALe3oH7BgLsi4D%2BBwL85KqQCwLj5KqQCwLi5KqQCwLh5KqQCwLg5KqQCwLm5KqQCwL75KqQCwLj5OqTCwLj5OaTCwLj5OKTCwLj5N6TCwLj5NqTCwLj5NaTCwLj5NKTCwLj5M6TCwLMutrVDgLVyZaWAwKk4vWlBIZLaaHbQdSEAlU2Uh9WgWwr47KI&language=9&ctl00%24Username=llntest&ctl00%24Password=Svc1sommer&ctl00%24ButtonLogin=Log+p%C3%A5").getBytes();

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

            set_headers.put(HttpMessage.HEADER_CONTENT_TYPE, Arrays.asList(new String[]{"application/x-www-form-urlencoded"}));
            set_headers.put(HttpMessage.HEADER_COOKIE, Arrays.asList(new String[]{"ASP.NET_SessionId=ultudfisawmism3t5vfpux45; ActiveTimestamp=635050035754538136; login=CustomerId=776; ExternalIdpSelection=-2"}));
            set_headers.put(HttpMessage.HEADER_CONNECTION, Arrays.asList(new String[]{HttpMessage.HEADER_KEEP_ALIVE}));

            set_headers.put(HttpMessage.HEADER_ACCEPT_LANGUAGE , Arrays.asList(new String[]{"da","en-us;q=0.7","en;q=0.3"}));
            set_headers.put("host", Arrays.asList(new String[]{"aarhustech.itslearning.com"}));
            set_headers.put(HttpMessage.HEADER_CONTENT_LENGTH, Arrays.asList(new String[]{"6759"}));
            set_headers.put(HttpMessage.HEADER_ACCEPT, Arrays.asList(new String[]{"text/html","application/xhtml+xml","application/xml;q=0.9","*/*;q=0.8"}));
            set_headers.put(HttpMessage.HEADER_USER_AGENT, Arrays.asList(new String[]{"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0"}));
            set_headers.put("referer", Arrays.asList(new String[]{"https://aarhustech.itslearning.com/elogin/"}));

            setHeaders(set_headers);

            // Protocol
            setProtocol("HTTP");

            // setTo(Host/Port)
            setToHost("aarhustech.itslearning.com");
            setToPort(443);

            // Version
            setVersion("1.1");

            // Method
            setMethod(HttpMessageMethod.POST);

            // URI
            setUri("/elogin/default.aspx");
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
            if(request.getToHost().toLowerCase().equals("www.aarhustech.itslearning.com") &&
               request.getUri().equals("/SSO_AUTO_LOGIN"))
            {
                return new ElevPlanRequest(request, initMap.get("USERNAME"), initMap.get("PASSWORD"));
            }
        }
        return input;
    }
}
