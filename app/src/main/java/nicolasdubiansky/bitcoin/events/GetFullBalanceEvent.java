package nicolasdubiansky.bitcoin.events;

/**
 * Created by Nicolas on 25/9/2017.
 */

public class GetFullBalanceEvent {
    private String address;
    private Integer height;

    public GetFullBalanceEvent(String address, Integer height) {
        this.address = address;
        this.height = height;
    }

    public GetFullBalanceEvent(String address) {
        this.address = address;
        this.height = null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
