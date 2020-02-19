package com.marketzone.app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.marketzone.app.PostLog;
import com.marketzone.app.R;

public class settingsFragment extends Fragment {

   settingsViewModel svm;
    private ListView lv_set;
    String uid;

    public PostLog mainActivity;

   String [] stgs = {"Change password", "Change name" , "Change address" , "Change email", "Change number"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        svm =
                ViewModelProviders.of(this).get(settingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        lv_set = (ListView) root.findViewById(R.id.list_settings);

        lv_set.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1 , stgs));
        mainActivity = (PostLog)getActivity();
        uid = mainActivity.UID;

        lv_set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(view.getContext(), Change_pass.class);

                    intent.putExtra("UID", uid);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(view.getContext(), Change_name.class);
                    intent.putExtra("UID", uid);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(view.getContext(), Change_address.class);

                    intent.putExtra("UID", uid);
                    startActivity(intent);
                }

                else if (position == 3) {
                    Intent intent = new Intent(view.getContext(), Change_email.class);

                    intent.putExtra("UID", uid);
                    startActivity(intent);
                }

                else if (position == 4) {
                    Intent intent = new Intent(view.getContext(), Change_number.class);

                    intent.putExtra("UID", uid);
                    startActivity(intent);
                }
            }
        });

        return root;







    }
}