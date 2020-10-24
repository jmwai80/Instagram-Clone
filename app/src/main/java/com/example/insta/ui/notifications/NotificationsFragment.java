package com.example.insta.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.insta.LoginActivity;
import com.example.insta.MainActivity;
import com.example.insta.R;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        Button logOut = root.findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null)
                        {
                            Log.e(getTag(), "Issue with login", e);
                            return;
                        }
                        goLoginActivity();
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        return root;
    }

    private void goLoginActivity()
    {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }
}