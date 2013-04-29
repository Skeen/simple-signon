import javax.swing.SwingUtilities;

class Driver
{
    public static void main(String[] args) 
    {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new LoginSSO());
    }
}


/* SQL Database tables
 * 
 *  uid | user | service
 *  User Id, username, service ID's
 *
 *  sid | dep_id | service_form(cid) | logo_id | service_name
 *  service ID, services depended on, method for service login, logo id, name of service
 * 
 *  sidd | uid | sid
 *  user/service id, user id, service id
 *  
 *  id | sidd | key | value
 *  unique id(or use sidd/key as id), user/service id, key/datatype, value/data
 *
 *  logo_id | logodata | timestamp
 *  logo id, the image as data, timestamp
 *
 *  cid | timestamp | classfile/loginform
 */