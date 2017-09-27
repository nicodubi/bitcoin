package nicolasdubiansky.bitcoin.web_services.rest_entities;

import java.util.List;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class TransactionOutput {

    private Integer value;
    private List<String> addresses;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
