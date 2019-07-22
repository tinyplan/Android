package com.example.finalwork189050934;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {
    private ListView lv_expend;
    private ArrayList<Map<String,Object>> expendList;
    private TextView tv_allExpend;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity)getActivity();
            switch(v.getId()){
                case R.id.bt_expendAdd:
                    mainActivity.gotoExpendAdd();
                    break;
            }
        }
    };

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the lv_income_item for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        Intent intent = getActivity().getIntent();
        expendList = (ArrayList<Map<String, Object>>) intent.getSerializableExtra("expendList");

        lv_expend = (ListView) view.findViewById(R.id.lv_expend);
        View header = inflater.inflate(R.layout.lv_income_header,null);
        lv_expend.addHeaderView(header);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),expendList,R.layout.lv_income_item,
                new String[]{"productName","money","type","mark","time"},
                new int[]{R.id.tv_productName,R.id.tv_money,R.id.tv_type,R.id.tv_mark,R.id.tv_time});
        lv_expend.setAdapter(adapter);

        String msg = intent.getStringExtra("expend");
        tv_allExpend = view.findViewById(R.id.tv_allExpend);
        tv_allExpend.setText(msg);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_expendAdd).setOnClickListener(listener);
    }
}
