package nicolasdubiansky.bitcoin.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.entities.QrCodeGenerator;

/**
 * Created by Nicolas on 1/10/2017.
 */

public class AddressView extends LinearLayout {
    private static final Integer QR_IMAGE_DIMEN = 200;

    @BindView(R.id.address_qr_code_image_id)
    ImageView addressQR;
    @BindView(R.id.address_text_textview_id)
    TextView addressText;

    public AddressView(Context context) {
        this(context, null);
    }

    public AddressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.address_view_layout, this);
        ButterKnife.bind(this);
    }

    //for this challenge code it´s a simple String, but in the future we have to receive an address abstraction (SOLID Principle dependency inversion)
    public void setAddress(String address) {
        addressText.setText(address);
        Bitmap qrCodeBitmap = QrCodeGenerator.generateQrCodeImage(address, QR_IMAGE_DIMEN);
        if (qrCodeBitmap == null) {
            Toast.makeText(getContext(), R.string.qr_code_generation_error, Toast.LENGTH_SHORT).show();
        } else {
            addressQR.setImageBitmap(qrCodeBitmap);
        }
    }


}
