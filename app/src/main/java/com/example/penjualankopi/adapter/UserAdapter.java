package com.example.penjualankopi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.penjualankopi.R;
import com.example.penjualankopi.model.User;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Activity context;
    private List<User> userList;

    public UserAdapter(Activity context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() { return userList.size(); }

    @Override
    public Object getItem(int position) { return userList.get(position); }

    @Override
    public long getItemId(int position) { return userList.get(position).getId(); }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        TextView tvEmail = view.findViewById(R.id.tvUserEmail);
        TextView tvRole = view.findViewById(R.id.tvUserRole);

        User user = userList.get(position);
        tvEmail.setText(user.getEmail());
        tvRole.setText(user.getRole());

        return view;
    }
}
