package nicolasdubiansky.bitcoin.utils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;

import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.events.ErrorResponseEvent;
import nicolasdubiansky.bitcoin.events.SuccessResponseDefault;
import nicolasdubiansky.bitcoin.web_services.retrofit.RetroiftServiceExecutor;


/**
 * Created by Nicolas on 25/09/2017.
 */

public class AbstractActivity extends FragmentActivity {

    private FrameLayout root;
    protected ProgressDialog dialog;

    private boolean openFromNotification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFromNotification = getIntent().getBooleanExtra(Constants.OPEN_FROM_NOTIFICATION, false);
        initSingletons();
        verificateUserSingleton();
        setContentView(R.layout.activity_abstract);
        root = (FrameLayout) findViewById(R.id.root_abstract);
        //printHashKey();
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

    protected void showProgressDialog(@Nullable String title) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setTitle(title == null ? getString(R.string.dialog_waiting) : title);
        }
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
        if (!User.getInstance().isAppInit() || ((this.openFromNotification) &&
                (User.getInstance().getBitcoinAddress() == null || User.getInstance().getBitcoinAddress().isEmpty()))) {
            Log.d("SINGLETON LOST USER", "Recovering user info...");
            refreshToken();
        }
    }

    private void refreshToken() {
        User.getInstance().setAppInit(true);
        CustomSharedPreferences sharedPreferences = new CustomSharedPreferences(this);
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

    @Subscribe
    public void notSuscriberEventBusException(SubscriberExceptionEvent exceptionEvent) {
        stopDialog();
    }

}
