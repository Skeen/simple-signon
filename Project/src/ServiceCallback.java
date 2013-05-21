// Allows services to call back to SSOWindow and update it as needed
public interface ServiceCallback
{
    public void callback(Service s);

}
