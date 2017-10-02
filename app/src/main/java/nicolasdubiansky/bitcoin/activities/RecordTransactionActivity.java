package nicolasdubiansky.bitcoin.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.events.GetBalanceEvent;
import nicolasdubiansky.bitcoin.events.GetFullBalanceEvent;
import nicolasdubiansky.bitcoin.events.GetFullBalanceEventSuccess;
import nicolasdubiansky.bitcoin.utils.adapters.RecordTransactionAdapter;
import nicolasdubiansky.bitcoin.entities.User;
import nicolasdubiansky.bitcoin.utils.AbstractActivity;
import nicolasdubiansky.bitcoin.utils.adapters.DividerItemDecoration;
import nicolasdubiansky.bitcoin.web_services.rest_entities.FullBalance;

/**
 * Created by Nicolas on 26/9/2017.
 */

public class RecordTransactionActivity extends AbstractActivity {

    @BindView(R.id.record_transaction_recylcer_view_id)
    RecyclerView recyclerTransaction;
    @BindView(R.id.refresh_record_transaction_button_id)
    FloatingActionButton refreshButton;
    private String address;
    private RecordTransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachRoot(R.layout.activity_record_transaction);
        ButterKnife.bind(this);
        hideAppBar();
        registerOnEventBus(this);
        address = User.getInstance().getBitcoinAddress();
        setUpRecyclcerTransaction();
        getSavedRecord();
        getRecordTransaction(true);
    }

    private void getSavedRecord() {
        FullBalance fullBalance = sharedPreferences.getFullBalance();
        if (fullBalance != null) {
            User.getInstance().setFullBalance(fullBalance);
            adapter.setTransactions(User.getInstance().getFullBalance().getTxs());
        }
    }

    private void getRecordTransaction(boolean showDialog) {
        postEvent(new GetFullBalanceEvent(address), getString(R.string.gettin_full_record_transaction), showDialog);
    }

    @Subscribe
    public void getRecordTransactionSuccess(GetFullBalanceEventSuccess event) {
        User.getInstance().setFullBalance(event.getFullBalance());
        adapter.setTransactions(User.getInstance().getFullBalance().getTxs());
        stopDialog();
    }

    private void setUpRecyclcerTransaction() {
        adapter = new RecordTransactionAdapter();
        recyclerTransaction.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider_recycler_view));
        recyclerTransaction.setAdapter(adapter);
        recyclerTransaction.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransaction.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && refreshButton.getVisibility() == View.VISIBLE) {
                    refreshButton.hide();
                } else if (dy < 0 && refreshButton.getVisibility() != View.VISIBLE) {
                    refreshButton.show();
                }
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
        getRecordTransaction(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterOnEventBus(this);
        sharedPreferences.saveFullBalance(User.getInstance().getFullBalance());
    }

    @OnClick(R.id.refresh_record_transaction_button_id)
    public void refreshRecordClick() {
        getRecordTransaction(true);
    }


}
