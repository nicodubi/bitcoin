package nicolasdubiansky.bitcoin.web_services.rest_entities;

import java.util.List;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class TransactionOutput {

    private Long value;
    private List<String> addresses;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
