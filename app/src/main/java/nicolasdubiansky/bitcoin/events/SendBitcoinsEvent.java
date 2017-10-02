package nicolasdubiansky.bitcoin.events;

import nicolasdubiansky.bitcoin.utils.Constants;

/**
 * Created by Nicolas on 25/9/2017.
 */

public class SendBitcoinsEvent {
    private String address;
    private String urlToSendMoney;

    public SendBitcoinsEvent(String address) {
        this.address = address;
        urlToSendMoney = Constants.URL_SEND_BITCOINS_ENDPOINT;
    }

    public String getAddress() {
        return address;
    }

    public String getUrlToSendMoney() {
        return urlToSendMoney + address;
    }
}
