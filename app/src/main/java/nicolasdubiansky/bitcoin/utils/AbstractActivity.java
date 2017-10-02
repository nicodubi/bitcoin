package nicolasdubiansky.bitcoin.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;

import java.util.ArrayList;

import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.events.CreateAddressEvent;
import nicolasdubiansky.bitcoin.events.ErrorResponseEvent;
import nicolasdubiansky.bitcoin.events.SuccessResponseDefault;
import nicolasdubiansky.bitcoin.web_services.retrofit.RetroiftServiceExecutor;


/**
 * Created by Nicolas on 25/09/2017.
 */

public class AbstractActivity extends AppCompatActivity {

    private FrameLayout root;
    protected ProgressDialog dialog;

    private boolean openFromNotification;
    protected CustomSharedPreferences sharedPreferences;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFromNotification = getIntent().getBooleanExtra(Constants.OPEN_FROM_NOTIFICATION, false);
        initSingletons();
        verificateUserSingleton();
        setContentView(R.layout.activity_abstract);
        root = (FrameLayout) findViewById(R.id.root_abstract);
        sharedPreferences = new CustomSharedPreferences(this);
    }

    private void initSingletons() {
        EventBus.getDefault();
        RetroiftServiceExecutor.getInstance();
        User.getInstance();
    }

    protected void attachRoot(int layoutRes) {
        View view = getLayoutInflater().inflate(layoutRes, null, false);
        root.addView(view);
    }

    protected void postEvent(Object event, String dialogTitle, boolean showProgressDialog) {
        if (deviceHasConnection()) {
            if (showProgressDialog) {
                showProgressDialog(dialogTitle);
            }
            EventBus.getDefault().post(event);
        } else {
            showSnackBarToEnableInternet();
        }

    }

    private void showSnackBarToEnableInternet() {
        Snackbar.make(root, R.string.not_internet_connection, Snackbar.LENGTH_LONG).
                setAction(R.string.wifi, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    }
                }).show();
    }

    protected void setAppBarTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void hideAppBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private boolean deviceHasConnection() {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return ((mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) ||
                (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING));
    }

    protected void showProgressDialog(@Nullable String title) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
        }
        dialog.setTitle(title == null ? getString(R.string.dialog_waiting) : title);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


    protected void stopDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Subscribe
    public void onSuccesssResponseDefaultEvent(SuccessResponseDefault event) {
        stopDialog();
    }

    @Subscribe
    public void onErrorResponseDefaultEvent(ErrorResponseEvent event) {
        stopDialog();
        String errorMsg = (event.getError() == null) ? getString(R.string.default_error_event) : event.getError();
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    protected void attachRoot(View view) {
        root.addView(view);
    }

    protected void registerOnEventBus(Object register) {
        if (!EventBus.getDefault().isRegistered(register)) {
            EventBus.getDefault().register(register);
        }
    }

    protected void unregisterOnEventBus(Object register) {
        if (EventBus.getDefault().isRegistered(register)) {
            EventBus.getDefault().unregister(register);
        }
    }

    protected void verificateUserSingleton() {
        if ((!User.getInstance().isAppInit() || this.openFromNotification) ||
                User.getInstance().getBitcoinAddress() == null || User.getInstance().getBitcoinAddress().isEmpty()) {
            Log.d("SINGLETON LOST USER", "Recovering user info...");
            refreshToken();
            RetroiftServiceExecutor.getInstance();
            EventBus.getDefault();
        }
    }

    private void refreshToken() {
        User.getInstance().setAppInit(true);
        CustomSharedPreferences sharedPreferences = new CustomSharedPreferences(this);
        User.getInstance().setAddress(sharedPreferences.getUserAddress());
        //TODO in case we have a expired token or a null token for an OS KILL we have to get a new one.
        //TODO for this challenge code it´s enough getting the saved address.
        /*Credentials credentials = sharedPreferences.getUserCredentials();
        if (credentials != null) {
            showProgressDialog();
            User.getInstance().setCredentials(credentials);
            EventBus.getDefault().post(new TokenRefreshEventRequest(credentials));
        }
        */
    }

    //TODO response to refresh token success for future challenges
    /*
    @Subscribe
    public void refreshTokenSuccess(TokenRefreshEventSuccess eventSuccess) {
        UserLoginResponse response = eventSuccess.getLoginResponse();
        User.getInstance().setUserLoginResponse(response);
        stopDialog();
    }

    //TODO response to refresh token error for future challenges
    @Subscribe
    public void refreshTokenError(TokenRefreshEventError eventError) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.EMAIL_LOGIN_MODAL_WAY, true);
        stopDialog();
        startActivity(intent);
    }
    */

    /**
     * Handle unexpected event bus exception
     *
     * @param exceptionEvent
     */
    @Subscribe
    public void notSuscriberEventBusException(SubscriberExceptionEvent exceptionEvent) {
        stopDialog();
    }

}
