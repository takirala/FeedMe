/*
package smartfoodcluster.feedme.qrcode;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
//import com.google.api.services.sqladmin.SQLAdminScopes;

// ...

//import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;

//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;



//import com.google.api.client.json.JsonFactory;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.clouddevices.CloudDevices;
import com.google.api.services.clouddevices.model.CloudDeviceChannel;
import com.google.api.services.clouddevices.model.Command;
import com.google.api.services.clouddevices.model.Device;
import com.google.api.services.clouddevices.model.DevicesListResponse;
import com.google.api.services.clouddevices.model.RegistrationTicket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeaveService {

  // See https://developers.google.com/weave/v1/dev-guides/getting-started/authorizing#setup
  // on how to set up your project and obtain client ID, client secret and API key.
  private static final String APP_USER_ID = "smartfoodcluster";
  private static final String CLIENT_ID = "970745565527-f9o46idgqj2l835m64in1ga82qdk1vnf.apps.googleusercontent.com";
  private static final String CLIENT_SECRET = "dDlA2D5IveNHpI8EOzaD7wHG";
  private static final String API_KEY = "AIzaSyBrRdAF6EmVDAhyONKf9yMNaiTC3Flqlgw";
  private static final String AUTH_SCOPE = "https://www.googleapis.com/auth/clouddevices";

  // Redirect URL for client side installed apps.
  private static final String REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob";

  private static final File CREDENTIALS_CACHE_FILE = new File("credentials_cache.json");

  private static AppEngineCredentialStore credentialStore =
        new AppEngineCredentialStore();

  // Command definitions of a new device if we need to create it.
  private static final String COMMAND_DEFS = "{" +
      "    \"storage\": {" +
      "     \"list\": {" +
      "       \"parameters\": {" +
      "        \"path\": {" +
      "          \"type\": \"string\"," +
      "          \"isRequired\": true" +
      "        }," +
      "        \"continuationToken\": {" +
      "          \"type\": \"string\"" +
      "        }," +
      "        \"entryCount\": {" +
      "          \"type\": \"integer\"" +
      "        }" +
      "       }" +
      "      }" +
      "     }," +
      "     \"_blinkLed\": {" +
      "     }" +
      "    }";

  //public static void main(String[] args) throws IOException {
  //  new WeaveService().run();
  //}

  private final NetHttpTransport httpTransport = new NetHttpTransport();
  private final JacksonFactory jsonFactory = new JacksonFactory();

  public String run() {
    CloudDevices apiClient;
    try {
      apiClient = getApiClient();
    } catch (IOException e) { throw new RuntimeException("Could not get API client", e); }

    DevicesListResponse devicesListResponse;
    try {
      System.out.println("get device list");
      // Listing devices, request to devices.list API method, returns a list of devices
      // available to user. More details about the method:
      // https://developers.google.com/weave/v1/reference/cloud-api/devices/list
      devicesListResponse = apiClient.devices().list().execute();
    } catch (IOException e) { throw new RuntimeException("Could not list devices", e); }
    List<Device> devices = devicesListResponse.getDevices();
    Device device;
    if (devices == null || devices.isEmpty()) {
      System.out.println("No devices, creating one.");
      return "no devices found";
    } else {
  		int i =0;
      String moisture_device_id = "93a73a43-0668-2fa0-2af5-b423b2a69167";
      String device_list_id;
      System.out.println("Chose the device:");
  		for(i = 0; i < devices.size(); i++) {
        device_list_id = devices.get(i).getId();
        if(device_list_id.equals(moisture_device_id)) break;
  		System.out.println( i + ": " + devices.get(i).getId());
      }

      	//BufferedReader inn = new BufferedReader(new InputStreamReader(System.in));
      	//System.out.println("Enter the device number:");
      	//String device_id = inn.readLine();

      device = devices.get(i);
      sendCommand("LED_ON",apiClient,device.getId());
      try {
        //System.out.println("Available device: \n" + jsonFactory.toPrettyString(devices.get(i)));
        ObjectMapper mapper = new ObjectMapper();

        // To put all of the JSON in a Map<String, Object>
        Map<String, Object> map = mapper.readValue(jsonFactory.toPrettyString(devices.get(i)), Map.class);

        // Accessing the three target data elements
        Map<String, Object> stateVarMap = (Map) map.get("state");
        Map<String, Object> stateMap = (Map) stateVarMap.get("_ledflasher");
        return (String) stateMap.get("_moisture");

        //return jsonFactory.toPrettyString(devices.get(i));
      } catch (IOException e) { throw new RuntimeException("Could not get device details", e); }
		  //System.out.println("Available device: \n" + jsonFactory.toPrettyString(devices.get(i)));
    }
  }

  private void sendCommand(String cmd, CloudDevices apiClient, String device_id) {
      boolean led_status = true;
      if(cmd.equals("LED_ON")) {
          led_status = true;
      } else if(cmd.equals("LED_OFF")) {
          led_status = false;
      }

      System.out.println("Sending a new command to the device");
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("_led", 2);
      parameters.put("_on", led_status);
      Command command = new Command()
          .setName("_ledflasher._set")  // Command name to execute.
          .setParameters(parameters)  // Required command parameter.
          .setDeviceId(device_id);  // Device to send the command to.
      // Calling commands.insert method to send command to the device, more details about the method:
      // https://developers.google.com/weave/v1/reference/cloud-api/commands/insert

      try {
        command = apiClient.commands().insert(command).execute();
      } catch (IOException e) { throw new RuntimeException("Could not insert command", e); }

      // The state of the command will be "queued". In normal situation a client may request
      // command again via commands.get API method to get command execution results, but our fake
      // device does not actually receive any commands, so it will never be executed.
      try {
        System.out.println("Sent command to the device:\n" + jsonFactory.toPrettyString(command));
      } catch (IOException e) { throw new RuntimeException(e); }
  }

  */
/**
   * Registers a new device making authenticated user the owner, check for more details:
   * https://developers.google.com/weave/v1/dev-guides/getting-started/register
   * @return the device just created
   *//*

  private Device createDevice(CloudDevices apiClient) throws IOException {
    GenericJson commandDefs =
        jsonFactory.createJsonParser(COMMAND_DEFS).parseAndClose(GenericJson.class);
    Device deviceDraft = new Device()
        .setDeviceKind("storage")
        .setSystemName("NAS 12418")
        .setDisplayName("Network Access Storage")
        .setChannel(new CloudDeviceChannel().setSupportedType("xmpp"))
        .set("commandDefs", commandDefs);
    RegistrationTicket ticket = apiClient.registrationTickets().insert(
        new RegistrationTicket()
            .setOauthClientId(CLIENT_ID)
            .setDeviceDraft(deviceDraft)
            .setUserEmail("me"))
        .execute();
    ticket = apiClient.registrationTickets().finalize(ticket.getId()).execute();
    return ticket.getDeviceDraft();
  }

  private CloudDevices getApiClient() throws IOException {
    // Try to load cached credentials.
    System.out.println("getApiClient");
    //AppIdentityCredential credential =
        //new AppIdentityCredential(Collections.singleton(AUTH_SCOPE));
    //GoogleCredential credential = GoogleCredential.getApplicationDefault();
    GoogleCredential credential = getCachedCredential();
	  //credential = null;
    if (credential == null) {
      System.out.println("Did not find cached credentials");
      credential = authorize();
    }
    if (credential.createScopedRequired()) {
        credential = credential.createScoped(Collections.singleton(AUTH_SCOPE));
    }
    System.out.println("returning api client");
    return new CloudDevices.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("Weave Sample")
        .setServicePath("clouddevices/v1")
        .setGoogleClientRequestInitializer(new CommonGoogleClientRequestInitializer(API_KEY))
        .build();
  }

  */
/**
   * Goes through Google OAuth2 authorization flow. See more details:
   * https://developers.google.com/weave/v1/dev-guides/getting-started/authorizing
   *//*

  private GoogleCredential authorize() throws IOException {
    System.out.println("authorize");
    // Generate the URL to send the user to grant access.
    // There are also other flows that may be used for authorization:
    // https://developers.google.com/accounts/docs/OAuth2
    String authorizationUrl = new GoogleAuthorizationCodeRequestUrl(
        CLIENT_ID, REDIRECT_URL, Collections.singleton(AUTH_SCOPE)).build();
    // Direct user to the authorization URI.
    System.out.println("Go to the following link in your browser:");
    System.out.println(authorizationUrl);
    // Get authorization code from user.
    //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    //System.out.println("What is the authorization code?");
    //String authorizationCode = in.readLine();
    String authorizationCode = "4/xaobdFkSVWCmvSB6ePM1pSzZSMWwuO6IbBppWijHGyM";

    // Use the authorization code to get an access token and a refresh token.
    GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, authorizationCode,
        REDIRECT_URL).execute();

    //cacheCredential(response.getRefreshToken());
    // Use the access and refresh tokens to set up credentials.
    GoogleCredential credential = new GoogleCredential.Builder()
        .setJsonFactory(jsonFactory)
        .setTransport(httpTransport)
        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
        .build()
        .setFromTokenResponse(response);

    credentialStore.store(APP_USER_ID, credential);

    return credential;
  }

  private GoogleCredential getCachedCredential() {
      GoogleCredential credential = new GoogleCredential.Builder()
              .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
              .setTransport(httpTransport)
              .setJsonFactory(jsonFactory)
              .build();
      if (credentialStore.load(APP_USER_ID, credential)) {
          return credential;
      }
      return null;
  }

  private void cacheCredential(String refreshToken) {
    GenericJson json = new GenericJson();
    json.setFactory(jsonFactory);
    json.put("client_id", CLIENT_ID);
    json.put("client_secret", CLIENT_SECRET);
    json.put("refresh_token", refreshToken);
    json.put("type", "authorized_user");
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(CREDENTIALS_CACHE_FILE);
      out.write(json.toPrettyString().getBytes(Charset.defaultCharset()));
    } catch (IOException e) {
      System.err.println("Error caching credentials");
      e.printStackTrace();
    } finally {
      if (out != null) {
        try { out.close(); } catch (IOException e) { */
/* Ignore. *//*
 }
      }
    }
  }
}
*/
