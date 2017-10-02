package nicolasdubiansky.bitcoin.web_services.rest_entities;

import java.util.List;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class Transaction {
    //reception date. Example: 2017-09-22T17:44:03Z
    private String received;
    //confirmation date: null if has not confirmed yet
    private String confirmed;
    private List<TransactionOutput> outputs;

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public Integer getAmount() {
        if (outputs != null && !outputs.isEmpty()) {
            return outputs.get(0).getValue();
        }
        return 0;
    }
}
