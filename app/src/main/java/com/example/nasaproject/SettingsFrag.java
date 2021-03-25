package com.example.nasaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private Button fSwitch;
    //private Button cSwitch;
    private EditText editText;
    private Switch switch1;
    private ToggleButton f_c_toggle;
    private Button saveButton;
    private TextView textView;
    private Button applyTextButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String IP = "text";
    public static final String SWITCH1 = "switch1";


    private String text;
    private boolean switchOnOff;


    public SettingsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFrag newInstance(String param1, String param2) {
        SettingsFrag fragment = new SettingsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings,container,false);



        // Inflate the layout for this fragment

        f_c_toggle =(ToggleButton)rootView.findViewById(R.id.fc_toggle);
        //fSwitch =(Button)rootView.findViewById(R.id.F_button);
        //cSwitch =(Button)rootView.findViewById(R.id.C_button);
        editText =(EditText)rootView.findViewById(R.id.ip_edittext);
        textView =(TextView)rootView.findViewById(R.id.textView);
        switch1=(Switch)rootView.findViewById(R.id.switch1);
        saveButton =(Button)rootView.findViewById(R.id.save_button);
        applyTextButton =(Button)rootView.findViewById(R.id.apply_button);


        applyTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView.setText(editText.getText().toString());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData();
            }
        });

        loadData();
        upDateViews();

        /*sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        iPLookUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change to save button!!!!!
                String iP = iPLookUp.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(IP, iP);
                editor.commit();
            }


        });*/



        //Toast.makeText(SettingsFrag.this,"IP changed",Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    public void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IP,textView.getText().toString());
        editor.putBoolean(SWITCH1, switch1.isChecked());

        editor.apply();

        Toast.makeText(getContext(),"IP saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

        text = sharedPreferences.getString(IP,"");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }
    public void upDateViews(){
        textView.setText(text);
        switch1.setChecked(switchOnOff);
    }

}
