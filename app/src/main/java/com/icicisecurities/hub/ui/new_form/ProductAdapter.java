package com.icicisecurities.hub.ui.new_form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icicisecurities.hub.R;
import com.icicisecurities.hub.ui.new_form.model.ProductVO;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<ProductVO> {

    //High priority UI variables goes below.....
    private Context context;
    private int resource;

    //Medium priority NON-UI variables goes below.....
    private ArrayList<ProductVO> listObject;
    private LayoutInflater inflater;



    //Least priority variables goes below....
    private final String TAG = "KeyValueAdapter";

    public ProductAdapter(Context context, int resource, ArrayList<ProductVO> listObject) {
        super(context, resource, listObject);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.resource = resource;
        this.listObject = listObject;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }//PersonalDetailsEducationAdapter constructor closes here.....


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return  getCustomView(position, convertView, parent);
    }//getDropDownView closes here....

    public View getCustomView(int position, View convertView, ViewGroup parent){

        convertView = inflater.inflate(R.layout.spinner_row_layout, null);

//        convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item,null);

        /////////////............INITIALIZATIONS GOES BELOW.............\\\\\\\\\\\\\\\\\\\\
        TextView txtView = (TextView) convertView.findViewById(R.id.spinnerTextView);

        ////////////.............SETTING DATA..........\\\\\\\\\\\\\\\\\\\\
        ProductVO object = listObject.get(position);//This will give the POJO object at this position....
        txtView.setText(object.getProductType());

        return convertView;

    }//getCustomView closes here....
}
