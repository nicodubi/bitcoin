package nicolasdubiansky.bitcoin.utils.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nicolasdubiansky.bitcoin.R;
import nicolasdubiansky.bitcoin.utils.CurrencyConverter;
import nicolasdubiansky.bitcoin.utils.DateFormatter;
import nicolasdubiansky.bitcoin.web_services.rest_entities.Transaction;

/**
 * Created by Nicolas on 2/10/2017.
 */

public class RecordTransactionAdapter extends RecyclerView.Adapter<RecordTransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, new ItemClickListener() {
            @Override
            public void onItemClick(ViewHolder view, int position) {
                Transaction transaction = transactions.get(position);
                //TODO if we have to show more info about a transaction or to go to another screen
                //TODO for this challenge just show confirmed date on clicking
                boolean isConfirmed = (transaction.getConfirmed() != null && !transaction.getConfirmed().isEmpty());
                if (isConfirmed) {
                    String formatedConfirmedDate = DateFormatter.getInstance().getSimpleFormattedDate(transaction.getConfirmed());
                    Toast.makeText(parent.getContext(), parent.getContext().getString(R.string.confirmed_date_transaction, formatedConfirmedDate), Toast.LENGTH_SHORT).show();
                } else {
                    String formatedReceiveDate = DateFormatter.getInstance().getSimpleFormattedDate(transaction.getReceived());
                    Toast.makeText(parent.getContext(), parent.getContext().getString(R.string.pending_date_transaction, formatedReceiveDate), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        String formatedDate = (DateFormatter.getInstance().getSimpleFormattedDate(transaction.getReceived()));
        holder.receivedDate.setText("Received: " + formatedDate);
        boolean isConfirmed = (transaction.getConfirmed() != null && !transaction.getConfirmed().isEmpty());
        holder.stateImage.setImageResource((isConfirmed) ? R.drawable.confirmed_transaction_icon : R.drawable.pending_transaction_icon);
        holder.amount.setText("Amount: " + String.valueOf(CurrencyConverter.shatoshiToBitcoin(transaction.getAmount())));
    }

    @Override
    public int getItemCount() {
        return (transactions != null) ? transactions.size() : 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private ItemClickListener listener;
        @BindView(R.id.transaction_view_received_date_id)
        public TextView receivedDate;
        @BindView(R.id.transaction_view_amount_id)
        public TextView amount;
        @BindView(R.id.transaction_view_state_id)
        public ImageView stateImage;

        public ViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            listener = clickListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ViewHolder.this.listener != null) {
                        ViewHolder.this.listener.onItemClick(ViewHolder.this, getAdapterPosition());
                    }
                }
            });
        }
    }

    interface ItemClickListener {
        void onItemClick(ViewHolder view, int position);
    }
}
