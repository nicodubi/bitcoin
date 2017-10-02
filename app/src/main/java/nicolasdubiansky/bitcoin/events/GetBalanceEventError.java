package nicolasdubiansky.bitcoin.events;

/**
 * Created by Nicolas on 1/10/2017.
 */

public class GetBalanceEventError {
    private String errorMsg;

    public GetBalanceEventError(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
