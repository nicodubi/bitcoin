package nicolasdubiansky.bitcoin.web_services.rest_entities;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class Balance {
    private String address;
    private Integer total_received;
    private Integer total_sent;
    //partial balance
    private Long balance;
    private Long unconfirmed_balance;
    //balance + uncofirmed_balance
    private Long final_balance;
    private Integer n_tx;
    private Integer unconfirmed_n_tx;
    //count of transactions
    private Integer final_n_tx;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTotal_received() {
        return total_received;
    }

    public void setTotal_received(Integer total_received) {
        this.total_received = total_received;
    }

    public Integer getTotal_sent() {
        return total_sent;
    }

    public void setTotal_sent(Integer total_sent) {
        this.total_sent = total_sent;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getUnconfirmed_balance() {
        return unconfirmed_balance;
    }

    public void setUnconfirmed_balance(Long unconfirmed_balance) {
        this.unconfirmed_balance = unconfirmed_balance;
    }

    public Long getFinal_balance() {
        return final_balance;
    }

    public void setFinal_balance(Long final_balance) {
        this.final_balance = final_balance;
    }

    public Integer getN_tx() {
        return n_tx;
    }

    public void setN_tx(Integer n_tx) {
        this.n_tx = n_tx;
    }

    public Integer getUnconfirmed_n_tx() {
        return unconfirmed_n_tx;
    }

    public void setUnconfirmed_n_tx(Integer unconfirmed_n_tx) {
        this.unconfirmed_n_tx = unconfirmed_n_tx;
    }

    public Integer getFinal_n_tx() {
        return final_n_tx;
    }

    public void setFinal_n_tx(Integer final_n_tx) {
        this.final_n_tx = final_n_tx;
    }
}
