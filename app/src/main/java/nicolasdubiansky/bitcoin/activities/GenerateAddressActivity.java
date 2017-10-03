package nicolasdubiansky.bitcoin.activities;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.custom_views.AddressView;
import nicolasdubiansky.bitcoin.entities.QrCodeGenerator;
import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.events.CreateAddressEvent;
import nicolasdubiansky.bitcoin.events.CreateAddressEventSuccess;
import nicolasdubiansky.bitcoin.utils.AbstractActivity;
import nicolasdubiansky.bitcoin.utils.CustomSharedPreferences;
import nicolasdubiansky.bitcoin.view_models.AddressViewModel;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 26/9/2017.
 */

public class GenerateAddressActivity extends AbstractActivity {

    public static final String ADDRESS_TEXT_KEY = "ADDRESS TEXT KEY";

    @BindView(R.id.address_view_id)
    AddressView addressView;
    @BindView(R.id.save_address_button_id)
    FloatingActionButton saveAddress;
    @BindView(R.id.generate_new_address_button_id)
    FloatingActionButton generateNewAddress;
    private AddressViewModel addressViewModel;
    //TODO pass to view model architecture!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.generate_address));
        attachRoot(R.layout.activity_generate_address);
        ButterKnife.bind(this);
        registerOnEventBus(this);
        initViewModel();
        //generateAddressFirstTime();
    }

    private void initViewModel() {
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        registerOnEventBus(addressViewModel);
        addressViewModel.getAddress().observe(this, new Observer<Address>() {
            @Override
            public void onChanged(@Nullable Address address) {
                if (address != null && address.getAddress() != null) {
                    addressView.setAddress(address.getAddress());
                }
                stopDialog();
            }
        });
        if (addressViewModel.getAddressValue() == null || addressViewModel.getAddressValue().getAddress() == null) {
            generateAddressFirstTime();
        }

    }

    private void generateAddressFirstTime() {
        generateNewAddress();
    }

    @OnClick(R.id.generate_new_address_button_id)
    public void generateNewAddress() {
        postEvent(new CreateAddressEvent(), getString(R.string.generating_address), true);
    }

    /*
        @Subscribe
        public void generateAddressSuccess(CreateAddressEventSuccess eventSuccess) {
            User.getInstance().setAddress(eventSuccess.getAddress());
            address = eventSuccess.getAddress();
            addressView.setAddress(address.getAddress());
            stopDialog();
        }
    */
    @OnClick(R.id.save_address_button_id)
    public void saveAddress() {
        if (addressViewModel.getAddressValue() != null && addressViewModel.getAddressValue().getAddress() != null) {
            showConfirmAddressDialog();
        } else {
            showGenerateAddressAlert();
        }
    }

    private void showConfirmAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm_address_dialog_title).
                setMessage(R.string.confirm_address_dialog_message).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToWalletBalanceActivity();
                    }
                }).
                setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void showGenerateAddressAlert() {
        Snackbar.make(addressView, R.string.must_generate_address, Snackbar.LENGTH_SHORT).
                setAction(R.string.generate_an_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        generateNewAddress();
                    }
                }).show();
    }


    private void goToWalletBalanceActivity() {
        CustomSharedPreferences sharedPreferences = new CustomSharedPreferences(this);
        sharedPreferences.saveUserAddress(addressViewModel.getAddressValue());
        Intent intent = new Intent(this, WalletBalanceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ADDRESS_TEXT_KEY, addressViewModel.getAddressValue().getAddress());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterOnEventBus(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerOnEventBus(this);
    }

}
