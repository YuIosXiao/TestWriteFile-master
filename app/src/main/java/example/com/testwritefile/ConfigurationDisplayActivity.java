package example.com.testwritefile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * Created by admin on 2018/4/20.
 */

public class ConfigurationDisplayActivity extends AppCompatActivity {

    private Activity mActivity;
    private RecyclerView rv_configurationdisplay;
    private ConfigurationDisplayAdapter configurationDisplayAdapter;
    private LinearLayoutManager layoutManager;//布局管理器
    private Button button1;
    public static boolean modifyflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = ConfigurationDisplayActivity.this;
        setContentView(R.layout.activity_configurationdisplay);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,
                        EditConfigurationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        rv_configurationdisplay = (RecyclerView) findViewById(R.id.rv_configurationdisplay);
        layoutManager = new LinearLayoutManager(mActivity);


        List<String> list = FileUtils.readFile2List(MainActivity.targetFilePath, "utf-8");
        if (!StringUtils.isBlank(list) && list.size() > 0) {
            configurationDisplayAdapter = new ConfigurationDisplayAdapter(mActivity, list);
            rv_configurationdisplay.setLayoutManager(layoutManager);// 设置布局管理器
            layoutManager.setOrientation(OrientationHelper.VERTICAL);// 设置为垂直布局，这也是默认的
            rv_configurationdisplay.addItemDecoration(new SpacesItemDecoration2(0, 30));
            rv_configurationdisplay.setAdapter(configurationDisplayAdapter);// 设置适配器
        } else {
            Toast.makeText(mActivity, "自定白名单数据为空！", Toast.LENGTH_LONG).show();
        }
    }
}
