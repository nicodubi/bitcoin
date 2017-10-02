package nicolasdubiansky.bitcoin.entities;


import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Balance;
import nicolasdubiansky.bitcoin.web_services.rest_entities.FullBalance;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class User {
    private Address address;
    private static User instance;
    private boolean appInit;
    private Balance balance;
    private FullBalance fullBalance;

    private User() {
        if (address == null) {
            address = new Address();
        }

        if (balance == null) {
            balance = new Balance();
        }

        if (fullBalance == null) {
            fullBalance = new FullBalance();
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

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public FullBalance getFullBalance() {
        return fullBalance;
    }

    public void setFullBalance(FullBalance fullBalance) {
        this.fullBalance = fullBalance;
    }
}
