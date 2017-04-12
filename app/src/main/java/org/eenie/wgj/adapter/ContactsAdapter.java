package org.eenie.wgj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.Contacts;

import java.util.List;

/**
 * Created by Eenie on 2017/4/11 at 14:29
 * Email: 472279981@qq.com
 * Des:
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<Contacts> contacts;
    private int layoutId;

    public ContactsAdapter(List<Contacts> contacts, int layoutId) {
        this.contacts = contacts;
        this.layoutId = layoutId;

    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Contacts contact = contacts.get(position);
        if (position == 0 || !contacts.get(position-1).getFirstAlphabet().equals(contact.getFirstAlphabet())) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(contact.getFirstAlphabet());
        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }
        holder.name.setText(contact.getName());
        holder.surname.setText(contact.getName().substring(0,1));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIndex;
        private RelativeLayout contacts;
        private TextView surname;
        private TextView name;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            contacts = (RelativeLayout) itemView.findViewById(R.id.item_contacts);
            surname = (TextView) itemView.findViewById(R.id.tv_surname);
            name= (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}