package nicolasdubiansky.bitcoin.web_services.rest_entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas on 24/9/2017.
 */

public class Address {

    private String address;
    @SerializedName("private")
    private String privateKey;
    @SerializedName("public")
    private String publicKey;
    private String wif;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getWif() {
        return wif;
    }

    public void setWif(String wif) {
        this.wif = wif;
    }
}
