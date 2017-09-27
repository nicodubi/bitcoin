package nicolasdubiansky.bitcoin.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
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
    @BindView(R.id.address_qr_code_image)
    ImageView addressQrCode;
    @BindView(R.id.address_text_textview)
    TextView addressText;
    @BindView(R.id.save_address_textview)
    TextView saveAddress;
    @BindView(R.id.generate_new_address_textview)
    TextView generateNewAddress;

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
        showProgressDialog(getString(R.string.generating_address));
        EventBus.getDefault().post(new CreateAddressEvent());
    }

    @Subscribe
    public void generateAddressSuccess(CreateAddressEventSuccess eventSuccess) {
        address = eventSuccess.getAddress();
        addressText.setText(address.getAddress());
        Bitmap qrCodeBitmap = QrCodeGenerator.generateQrCodeImage(address.getAddress(),QR_IMAGE_DIMEN);
        if(qrCodeBitmap==null){
            Toast.makeText(this,R.string.qr_code_generation_error,Toast.LENGTH_SHORT).show();
        }else{
            addressQrCode.setImageBitmap(qrCodeBitmap);
        }
        stopDialog();
    }

    @OnClick(R.id.save_address_textview)
    public void saveAddress() {

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
