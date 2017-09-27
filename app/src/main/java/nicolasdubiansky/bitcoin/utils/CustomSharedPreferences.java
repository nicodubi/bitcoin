package nicolasdubiansky.bitcoin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 25/09/2017.
 */

public class CustomSharedPreferences {
    private SharedPreferences sp;
    private Gson gson;
    private SharedPreferences.Editor editor;

    public CustomSharedPreferences(Context context) {
        sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES, context.MODE_PRIVATE);
        editor = sp.edit();
        gson = new Gson();
    }

    /**
     * Save user address if it´s valid
     *
     * @param address neither null address nor empty
     */
    public void saveUserAddress(Address address) {
        if (address != null && address.getAddress() != null && !address.getAddress().isEmpty()) {
            String addressToString = gson.toJson(address);
            editor.putString(Constants.USER_CREDENTIALS, addressToString);
            editor.apply();
        }
    }

    public Address getUserAddress() {
        String addressToString = sp.getString(Constants.USER_CREDENTIALS, null);
        if (addressToString != null) {
            Address address = gson.fromJson(addressToString, Address.class);
            return address;
        }
        return null;
    }


    public void removeSavedAddress() {
        editor.remove(Constants.USER_CREDENTIALS);
        editor.apply();
    }

}
