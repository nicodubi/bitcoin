package nicolasdubiansky.bitcoin.entities;


import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class User {
    private Address address;
    private static User instance;
    private boolean appInit;

    private User() {
        if (address == null) {
            address = new Address();
        }
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return user bitcoin address. null if user hasn´t one.
     */
    public String getBitcoinAddress() {
        if (address != null) {
            return address.getAddress();
        }
        return null;
    }

    public boolean isAppInit() {
        return appInit;
    }

    public void setAppInit(boolean appInit) {
        this.appInit = appInit;
    }
}
