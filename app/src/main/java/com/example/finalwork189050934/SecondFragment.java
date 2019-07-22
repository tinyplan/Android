package com.example.finalwork189050934;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {
    private ListView lv_income;
    private TextView tv_allIncome;
    private ArrayList<Map<String,Object>> incomeList;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity)getActivity();
            switch(v.getId()){
                case R.id.bt_incomeAdd:
                    mainActivity.gotoIncomeAdd();
                    break;
            }
        }
    };

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the lv_income_item for this fragment
        View view=inflater.inflate(R.layout.fragment_second, container, false);

        Intent intent = getActivity().getIntent();
        incomeList = (ArrayList<Map<String, Object>>) intent.getSerializableExtra("incomeList");

        lv_income = (ListView) view.findViewById(R.id.lv_income);
        View header = inflater.inflate(R.layout.lv_income_header,null);
        lv_income.addHeaderView(header);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),incomeList,R.layout.lv_income_item,
                                    new String[]{"productName","money","type","mark","time"},
                                    new int[]{R.id.tv_productName,R.id.tv_money,R.id.tv_type,R.id.tv_mark,R.id.tv_time});
        lv_income.setAdapter(adapter);

        String msg = intent.getStringExtra("income");
        tv_allIncome = view.findViewById(R.id.tv_allIncome);
        tv_allIncome.setText(msg);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_incomeAdd).setOnClickListener(listener);
    }
}
