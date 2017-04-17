package function_class;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import sockettest.example.com.myapplication.R;

/**
 * Created by Administrator on 2017/2/27.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List< Map<String,Object>> mStrings;
    private  List<GridViewDataObject> mGridViewDataObject;
    private  String mStringValue = "";
    private  int mytype  = 0;



    public String getStringValue() {
        return mStringValue;
    }

    public void setStrings(List<Map<String, Object>> strings) {
        mStrings = strings;
    }

    public void setGridViewDataObject(List<GridViewDataObject> gridViewDataObject) {
        mGridViewDataObject = gridViewDataObject;
    }

    public GridViewAdapter(Context context, List<Map<String,Object>> mstrings){
        mContext = context;
        mStrings = mstrings;
    }
    public GridViewAdapter(Context context, List<Map<String,Object>> mstrings, int type) {
        mContext = context;
        mStrings = mstrings;
        mytype = type;
    }
    public GridViewAdapter(Context context, List<GridViewDataObject> gridViewDataObject,String s) {
        mContext = context;
        mGridViewDataObject = gridViewDataObject;
        mytype = 1;
    }

    @Override
    public int getCount() {
        if (mytype==1){
             if (mGridViewDataObject.size() > 0) {
                 return mGridViewDataObject.size();
             }
        }else if (mStrings.size() > 0) {
            return mStrings.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }


    //缓存girdview单元对象
    public final class ViewHolder {
        public TextView theName;
        public EditText value;// GridView中的输入
    }
    private Integer index = -1;//记录控件ID

    class MyTextWatcher implements TextWatcher {

        private ViewHolder mHolder;

        public MyTextWatcher(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable!=null && !"".equals(editable.toString())){
                int position = (Integer) mHolder.value.getTag();
                mGridViewDataObject.get(position).setValue(editable.toString());//editText值发生改变时候存入 mGridViewDataObject链表中
//                Log.w("更变",editable.toString());
            }
        }
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            ViewHolder holder = null;
            switch (mytype) {
                case 1:
                    if (view == null) {
                        holder = new ViewHolder();
                        view = LayoutInflater.from(mContext).inflate(R.layout.the_control_item, null);
                        holder.theName = (TextView) view.findViewById(R.id.textView);
                        holder.value = (EditText) view.findViewById(R.id.editText);
                        holder.value.setTag(i);
                        holder.value.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                                    index = (Integer) view.getTag();
                                return false;
                            }
                        });
                        holder.value.addTextChangedListener(new MyTextWatcher(holder));
                        view.setTag(holder);
                    } else {
                        holder = (ViewHolder) view.getTag();
                        holder.value.setTag(i);
                    }
                    if (mGridViewDataObject.get(i).getName() != null && !"".equals(mGridViewDataObject.get(i).getName())) {
                        holder.theName.setGravity(Gravity.CENTER);
                        holder.theName.setText(mGridViewDataObject.get(i).getName());
                    }
                    if (mGridViewDataObject.get(i).getValue() != null && !"".equals(mGridViewDataObject.get(i).getValue())) {
                        holder.value.setGravity(Gravity.CENTER);
                        holder.value.setText(mGridViewDataObject.get(i).getValue());
                    }
                    break;
                default:
                    if (view == null) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
                        TextView textView = (TextView) view.findViewById(R.id.textView);
                        Map<String, Object> map = mStrings.get(i);
//                        Log.w("111", String.valueOf(map.get("content")));
                        textView.setText(String.valueOf(map.get("content")));
                        textView.setGravity(Gravity.CENTER);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
