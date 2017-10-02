package nicolasdubiansky.bitcoin.activities;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.utils.AbstractActivity;
import nicolasdubiansky.bitcoin.utils.CustomSharedPreferences;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Address;

/**
 * Created by Nicolas on 25/09/2017.
 */

public class SplashActivity extends AbstractActivity {

    private static final long SPLASH_TIME_MILLSECONDS = 1000;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User.getInstance().setAppInit(true);
        super.onCreate(savedInstanceState);
        attachRoot(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                checkIfIsFirstTime();
                stopTimer();
            }
        };
        timer.schedule(timerTask, SPLASH_TIME_MILLSECONDS, SPLASH_TIME_MILLSECONDS);
    }

    private void checkIfIsFirstTime() {
        CustomSharedPreferences sharedPref = new CustomSharedPreferences(this);
        Address saveAddress = sharedPref.getUserAddress();
        //uncomment to simulate that there is not generating address yet
        //saveAddress = null;
        if (saveAddress == null) {
            goToGenerateAddressActivity();
        } else {
            User.getInstance().setAddress(saveAddress);
            goToBalanceActivity();
        }
    }

    private void goToBalanceActivity() {
        Intent intent = new Intent(this, WalletBalanceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToGenerateAddressActivity() {
        Intent intent = new Intent(this, GenerateAddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
