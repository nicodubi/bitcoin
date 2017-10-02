package nicolasdubiansky.bitcoin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Balance;
import nicolasdubiansky.bitcoin.web_services.rest_entities.FullBalance;

/**
 * Created by Nicolas on 25/09/2017.
 */

public class CustomSharedPreferences {
    private SharedPreferences sp;
    private Gson gson;
    private SharedPreferences.Editor editor;

    public CustomSharedPreferences(Context context) {
        //context is not saved as member variable avoiding leaks
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
            editor.putString(Constants.USER_CREDENTIALS_SP, addressToString);
            editor.apply();
        }
    }

    public Address getUserAddress() {
        String addressToString = sp.getString(Constants.USER_CREDENTIALS_SP, null);
        if (addressToString != null) {
            Address address = gson.fromJson(addressToString, Address.class);
            return address;
        }
        return null;
    }


    public void removeSavedAddress() {
        editor.remove(Constants.USER_CREDENTIALS_SP);
        editor.apply();
    }

    public void saveFirstTimeUserReceiveMoney() {
        editor.putBoolean(Constants.FIRST_TIME_USER_RECEIVE_MONEY_SP, false);
        editor.apply();
    }

    public boolean isFirstTimeUserReceiveMoney() {
        return sp.getBoolean(Constants.FIRST_TIME_USER_RECEIVE_MONEY_SP, true);
    }

    public void saveBalance(Balance balance) {
        String balanceString = gson.toJson(balance);
        editor.putString(Constants.USER_BALANCE_SP, balanceString);
        editor.apply();
    }

    public Balance getBalance() {
        String balanceString = sp.getString(Constants.USER_BALANCE_SP, null);
        if (balanceString == null) {
            return null;
        } else {
            Balance balance = gson.fromJson(balanceString, Balance.class);
            return balance;
        }
    }

    public void saveFullBalance(FullBalance balance) {
        String fullBalanceString = gson.toJson(balance);
        editor.putString(Constants.USER_FULL_BALANCE_SP, fullBalanceString);
        editor.apply();
    }

    public FullBalance getFullBalance() {
        String balanceString = sp.getString(Constants.USER_FULL_BALANCE_SP, null);
        if (balanceString == null) {
            return null;
        } else {
            FullBalance balance = gson.fromJson(balanceString, FullBalance.class);
            return balance;
        }
    }
}
