package com.cmsc355.contactapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class ContactsAdapter extends RecyclerView.Adapter {

    //holds all the contacts we want to display
    private ArrayList<Contact> contactArrayList;

    //ViewHolder holds references to each view inside a generated item on the list, so we can access them later
    private class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        TextView txtName;

        ViewHolder(View view) {
            super(view);
            layout = view;
            txtName = view.findViewById(R.id.contact_name);
        }
    }

    //constructor method
    ContactsAdapter(ArrayList<Contact> contactList) {
        this.contactArrayList = contactList;
    }

    public void add(int position, Contact item) {
        contactArrayList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        contactArrayList.remove(position);
        notifyItemRemoved(position);
    }

    //this is called to make a new item on the list - it adds the views to the activity then returns
    //the viewholder to be able to reference these views later
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    //this is where we actually modify the contents of the views. In this case, we just set the contact's
    //name and make the whole thing clickable. If you click it, it takes you to the Contact Info screen
    //with all the fields disabled for editing initially
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Contact contact = contactArrayList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        String name = contact.getName();
        viewHolder.txtName.setText(name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContactInfoActivity.class);
                intent.putExtra("ContactID", contact.getId());
                intent.putExtra("isEditable", false);
                view.getContext().startActivity(intent);
            }
        });
    }

    //actually really important. this determines how many elements it's going to inflate, so
    //this must return the desired number of elements after the constructor is called
    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }
}
