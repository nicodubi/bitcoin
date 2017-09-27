package nicolasdubiansky.bitcoin.activities;

import android.os.Bundle;

import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.utils.AbstractActivity;

/**
 * Created by Nicolas on 26/9/2017.
 */

public class WalletBalanceActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachRoot(R.layout.activity_wallet_balance);
    }
}
