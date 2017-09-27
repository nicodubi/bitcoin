package nicolasdubiansky.bitcoin.events;

import nicolasdubiansky.bitcoin.web_services.rest_entities.FullBalance;

/**
 * Created by Nicolas on 25/9/2017.
 */

public class GetFullBalanceEventSuccess {
    private FullBalance fullBalance;

    public GetFullBalanceEventSuccess(FullBalance fullBalance) {
        this.fullBalance = fullBalance;
    }

    public FullBalance getFullBalance() {
        return fullBalance;
    }
}
