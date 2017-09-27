package nicolasdubiansky.bitcoin.events;

/**
 * Created by Nicolas on 25/9/2017.
 */

public class GetBalanceEvent {
    private String address;

    public GetBalanceEvent(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
