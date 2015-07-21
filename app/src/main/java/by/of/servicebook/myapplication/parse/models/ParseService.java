package by.of.servicebook.myapplication.parse.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by Pavel on 25.05.2015.
 */
@ParseClassName("Service")
public class ParseService extends ParseObject {
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_NAME = "service_name";
    public static final String CLIENT_EMAIL = "client_email";
    public static final String FILE = "file";

    public ParseService(){}

    public ParseService(String serviceId, String serviceName, String clientEmail, ParseFile parseFile){
        put(SERVICE_ID, serviceId);
        put(SERVICE_NAME, serviceName);
        put(CLIENT_EMAIL, clientEmail);
        put(FILE, parseFile);
    }

}
