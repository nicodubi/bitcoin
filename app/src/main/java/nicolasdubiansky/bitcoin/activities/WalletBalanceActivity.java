package nicolasdubiansky.bitcoin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.custom_views.AddressView;
import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.events.GetBalanceEvent;
import nicolasdubiansky.bitcoin.events.GetBalanceEventError;
import nicolasdubiansky.bitcoin.events.GetBalanceEventSuccess;
import nicolasdubiansky.bitcoin.events.SendBitcoinsEvent;
import nicolasdubiansky.bitcoin.events.SendBitcoinsEventSuccess;
import nicolasdubiansky.bitcoin.utils.AbstractActivity;
import nicolasdubiansky.bitcoin.utils.CurrencyConverter;
import nicolasdubiansky.bitcoin.utils.CustomSharedPreferences;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Balance;

/**
 * Created by Nicolas on 26/9/2017.
 */

public class WalletBalanceActivity extends AbstractActivity {

    @BindView(R.id.swipe_refresh_waller_balance_id)
    SwipeRefreshLayout swipeRefreshBalance;
    @BindView(R.id.address_view_id)
    AddressView addressView;
    @BindView(R.id.receive_money_button_id)
    FloatingActionButton receiveMoneyButton;
    @BindView(R.id.partial_balance_text_id)
    TextView partialBalance;
    @BindView(R.id.unconfirmed_balance_text_id)
    TextView unconfirmedBalance;
    @BindView(R.id.total_balance_text_id)
    TextView totalBalance;
    @BindView(R.id.transaction_record_textview_id)
    TextView transactionRecord;
    private String address;
    private Balance balance;
    private Boolean isFirstTimeUserReceiveMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.wallet_balance));
        attachRoot(R.layout.activity_wallet_balance);
        ButterKnife.bind(this);
        registerOnEventBus(this);
        getAddress();
        checkIsFirstTimeReceivingMoney();
        addressView.setAddress(address);
        setUpSwipeRefreshBalance();
        getSavedBalance();
    }

    private void getSavedBalance() {
        Balance savedBalance = sharedPreferences.getBalance();
        if (savedBalance != null) {
            refreshBalance(savedBalance);
        }
    }

    private void setUpSwipeRefreshBalance() {
        swipeRefreshBalance.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBalanceRequest(false);
                swipeRefreshBalance.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerOnEventBus(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBalanceRequest(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterOnEventBus(this);
        sharedPreferences.saveBalance(User.getInstance().getBalance());
    }


    private void refreshBalanceRequest(boolean showDialog) {
        postEvent(new GetBalanceEvent(address), getString(R.string.getting_balance_dialog), showDialog);
    }

    private void refreshBalance(Balance balance) {
        this.balance = balance;
        User.getInstance().setBalance(balance);
        partialBalance.setText(String.valueOf(CurrencyConverter.shatoshiToBitcoin(balance.getBalance())));
        unconfirmedBalance.setText(String.valueOf(CurrencyConverter.shatoshiToBitcoin(balance.getUnconfirmed_balance())));
        totalBalance.setText(String.valueOf(CurrencyConverter.shatoshiToBitcoin(balance.getFinal_balance())));
    }

    @Subscribe
    public void getBalanceEventSuccess(GetBalanceEventSuccess event) {
        refreshBalance(event.getBalance());
        stopDialog();
    }


    @Subscribe
    public void getBalanceEventError(GetBalanceEventError event) {
        stopDialog();
        Toast.makeText(this, event.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void sendMoneyEventSuccess(SendBitcoinsEventSuccess event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopDialog();
        refreshBalanceRequest(false);
    }


    private void getAddress() {
        //address from another activity bundle
        String addressText = getIntent().getStringExtra(GenerateAddressActivity.ADDRESS_TEXT_KEY);
        if (addressText != null) {
            address = addressText;
            return;
        }

        //address from User instance
        if (User.getInstance().getBitcoinAddress() != null) {
            address = User.getInstance().getBitcoinAddress();
            return;
        }

        //address from shared preferences
        CustomSharedPreferences sharedPref = new CustomSharedPreferences(this);
        Address savedAddress = sharedPref.getUserAddress();
        if (savedAddress != null) {
            address = savedAddress.getAddress();
        }

    }

    @OnClick(R.id.receive_money_button_id)
    public void receiveMoney() {
        if (isFirstTimeUserReceiveMoney) {
            dialogToReceiveMoneyFirstTime();
        } else {
            postEvent(new SendBitcoinsEvent(address), getString(R.string.receiving_money), true);
        }
    }

    private void dialogToReceiveMoneyFirstTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.receive_moeny_dialog_title).
                setMessage(R.string.confirm_receiving_money_dialog_message).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomSharedPreferences sp = new CustomSharedPreferences(WalletBalanceActivity.this);
                        sp.saveFirstTimeUserReceiveMoney();
                        isFirstTimeUserReceiveMoney = false;
                        postEvent(new SendBitcoinsEvent(address), getString(R.string.receiving_money), true);
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

    private void checkIsFirstTimeReceivingMoney() {
        CustomSharedPreferences sp = new CustomSharedPreferences(this);
        isFirstTimeUserReceiveMoney = sp.isFirstTimeUserReceiveMoney();
    }

    @OnClick(R.id.transaction_record_textview_id)
    public void goToRecordTransaction() {
        Intent intent = new Intent(this, RecordTransactionActivity.class);
        startActivity(intent);
    }

}
