package nicolasdubiansky.bitcoin.events;

import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class CreateAddressEventSuccess {
    private Address address;

    public CreateAddressEventSuccess(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
