package com.example.finalwork189050934;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForthFragment extends Fragment {
    private Button bt_logout;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity)getActivity();
            switch(v.getId()){
                case R.id.bt_alterPwd:
                    mainActivity.gotoAlterPwd();
                    break;
            }
        }
    };

    public ForthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the lv_income_item for this fragment
        View view = inflater.inflate(R.layout.fragment_forth, container, false);

        sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sp.edit();

        bt_logout = view.findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                Toast.makeText(getActivity(),"注销用户成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_alterPwd).setOnClickListener(listener);
    }
}
