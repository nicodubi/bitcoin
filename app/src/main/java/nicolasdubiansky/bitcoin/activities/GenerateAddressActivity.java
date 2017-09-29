package nicolasdubiansky.bitcoin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import nicolasdubiansky.bitcoin.entities.QrCodeGenerator;
import nicolasdubiansky.bitcoin.events.CreateAddressEvent;
import nicolasdubiansky.bitcoin.events.CreateAddressEventSuccess;
import nicolasdubiansky.bitcoin.utils.AbstractActivity;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 26/9/2017.
 */

public class GenerateAddressActivity extends AbstractActivity {

    private static final Integer QR_IMAGE_DIMEN = 200;
    public static final String ADDRESS_TEXT_KEY = "ADDRESS TEXT KEY";

    @BindView(R.id.address_qr_code_image)
    ImageView addressQrCode;
    @BindView(R.id.address_text_textview)
    TextView addressText;
    @BindView(R.id.save_address_textview)
    FloatingActionButton saveAddress;
    @BindView(R.id.generate_new_address_textview)
    FloatingActionButton generateNewAddress;

    //TODO pass to view model architecture!
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachRoot(R.layout.activity_generate_address);
        ButterKnife.bind(this);
        registerOnEventBus(this);
        generateAddressFirstTime();
    }

    private void generateAddressFirstTime() {
        generateNewAddress();
    }

    @OnClick(R.id.generate_new_address_textview)
    public void generateNewAddress() {
        postEvent(new CreateAddressEvent(),getString(R.string.generating_address));
    }



    @Subscribe
    public void generateAddressSuccess(CreateAddressEventSuccess eventSuccess) {
        address = eventSuccess.getAddress();
        addressText.setText(address.getAddress());
        Bitmap qrCodeBitmap = QrCodeGenerator.generateQrCodeImage(address.getAddress(), QR_IMAGE_DIMEN);
        if (qrCodeBitmap == null) {
            Toast.makeText(this, R.string.qr_code_generation_error, Toast.LENGTH_SHORT).show();
        } else {
            addressQrCode.setImageBitmap(qrCodeBitmap);
        }
        stopDialog();
    }

    @OnClick(R.id.save_address_textview)
    public void saveAddress() {
        if (address != null && address.getAddress() != null) {
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
        Snackbar.make(addressText, R.string.must_generate_address, Snackbar.LENGTH_SHORT).
                setAction(R.string.generate_an_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        generateNewAddress();
                    }
                }).show();
    }


    private void goToWalletBalanceActivity() {
        Intent intent = new Intent(this, WalletBalanceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ADDRESS_TEXT_KEY, address.getAddress());
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
