package nicolasdubiansky.bitcoin.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.greenrobot.eventbus.Subscribe;

import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.events.CreateAddressEventSuccess;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 2/10/2017.
 */

public class AddressViewModel extends ViewModel {
    private MutableLiveData<Address> address;

    public LiveData<Address> getAddress() {
        if (address == null) {
            address = new MutableLiveData<Address>();
        }
        return address;
    }

    @Subscribe
    public void generateAddressSuccess(CreateAddressEventSuccess eventSuccess) {
        User.getInstance().setAddress(eventSuccess.getAddress());
        address.setValue(eventSuccess.getAddress());
    }

    public Address getAddressValue() {
        return address.getValue();
    }
}
