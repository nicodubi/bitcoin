package nicolasdubiansky.bitcoin.events;

/**
 * Created by Nicolas on 25/9/2017.
 */

public class SendBitcoinsEventSuccess {
    private String receiverAddress;

    public SendBitcoinsEventSuccess(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }
}
