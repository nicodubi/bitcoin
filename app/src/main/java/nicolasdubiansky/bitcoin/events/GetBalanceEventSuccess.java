package nicolasdubiansky.bitcoin.events;

import nicolasdubiansky.bitcoin.web_services.rest_entities.Balance;

/**
 * Created by Nicolas on 25/9/2017.
 */

public class GetBalanceEventSuccess {
    private Balance balance;

    public GetBalanceEventSuccess(Balance balance) {
        this.balance = balance;
    }

    public Balance getBalance() {
        return balance;
    }
}
