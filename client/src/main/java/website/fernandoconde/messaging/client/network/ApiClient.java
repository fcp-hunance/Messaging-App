package website.fernandoconde.messaging.client.network;

import website.fernandoconde.messaging.client.service.MessageClient;
import website.fernandoconde.messaging.client.service.MessageReceiver;
import website.fernandoconde.messaging.client.service.TokenManager;

public class ApiClient {

    private final String apiUrl;
    private final TokenManager tokenManager;

    public ApiClient(String apiUrl, String username, String password) {
        this.apiUrl = apiUrl;
        this.tokenManager = new TokenManager(apiUrl, username, password);
    }

    public MessageReceiver messageReceiver() {
        return new MessageReceiver(apiUrl, tokenManager);
    }

    public MessageClient messageClient() {
        return new MessageClient(apiUrl, tokenManager);
    }
}
