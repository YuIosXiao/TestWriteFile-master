package example.com.testwritefile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/4/20.
 */

public class ConfigurationDisplayAdapter extends RecyclerView.Adapter<ConfigurationDisplayAdapter.ConfigurationDisplayViewHolder> {

    private LayoutInflater inflater;
    private Activity mActivity;
    private List<String> list;

    public ConfigurationDisplayAdapter(Activity mActivity, List<String> list) {
        this.mActivity = mActivity;
        this.list = list;
        inflater = LayoutInflater.from(mActivity);
    }

    /**
     * ViewHolder类
     */
    public class ConfigurationDisplayViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rl_configurationdisplay;
        public TextView tv_configurationdisplay;
        public Button btn_configurationdisplay;

        public ConfigurationDisplayViewHolder(View view) {
            super(view);
            tv_configurationdisplay = (TextView) view.findViewById(R.id.tv_configurationdisplay);
            btn_configurationdisplay = (Button) view.findViewById(R.id.btn_configurationdisplay);
            rl_configurationdisplay = (RelativeLayout) view.findViewById(R.id.rl_configurationdisplay);
        }
    }


    @Override
    public ConfigurationDisplayAdapter.ConfigurationDisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_configurationdisplay, parent, false);
        ConfigurationDisplayViewHolder viewHolder = new ConfigurationDisplayViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConfigurationDisplayViewHolder holder, final int position) {
        holder.tv_configurationdisplay.setText(list.get(position));
        holder.btn_configurationdisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
        holder.rl_configurationdisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "position----->" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mActivity,
                        EditConfigurationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("position", position);
                intent.putStringArrayListExtra("configurationlist", (ArrayList<String>) list);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        EditConfigurationActivity.deleteDataFromFile(MainActivity.targetFilePath, list);
    }
}
