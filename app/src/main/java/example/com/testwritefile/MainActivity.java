package example.com.testwritefile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Activity mActivity;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private TextView tv1;

    private String filepath = "";
    private String targetfilepath = "";

    public static String originalFilePath = "";
    public static String targetFilePath = "";
    public static String usesFilePath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = MainActivity.this;
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        tv1 = (TextView) findViewById(R.id.tv1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDataToFile(targetfilepath);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyFileTOFile(targetfilepath, filepath);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,
                        ConfigurationDisplayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditConfigurationActivity.createUseFile(originalFilePath, usesFilePath, targetFilePath);
            }
        });


        filepath = mActivity.getExternalFilesDir(null) + File.separator + "android_d2o";
        targetfilepath = mActivity.getExternalFilesDir("myfolder") + File.separator + "android_d2o";
        deleteMyFile(targetfilepath);


        originalFilePath = mActivity.getExternalFilesDir(null) + File.separator + "android_d2o";
        targetFilePath = mActivity.getExternalFilesDir("myfolder") + File.separator + "android_d2o_customize";
        usesFilePath = mActivity.getExternalFilesDir("myfolder") + File.separator + "android_d2o_new";
    }

    public void writeDataToFile(String filepath) {
        if (FileUtils.isFileExists(filepath)) {
            List<String> list = FileUtils.readFile2List(filepath, "utf-8");
            StringBuilder sbstr = new StringBuilder();
//            sbstr.append("-----haha \r\n");
            if (!StringUtils.isBlank(list) && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Log.i("11111", "----->" + list.get(i).toString());
                    if (list.get(i).toString().startsWith("proxy_domain")) {
                        Log.i("11111", "proxy_domain----->" + i);
                        sbstr.append(list.get(i).toString() + " " + "haha" + "\r\n");
                    } else {
                        Log.i("11111", "其他----->" + i);
                        sbstr.append(list.get(i).toString() + "\r\n");
                    }
                }
                InputStream in = new ByteArrayInputStream(sbstr.toString().getBytes());
                if (FileUtils.writeFileFromIS(filepath, in, false)) {
                    Log.i("11111", "----->文件写入成功！");
                    tv1.setText(FileUtils.readFile2String(filepath, "utf-8"));
                } else {
                    Log.i("11111", "----->文件写入失败！");
                }
            }

        } else {
            Log.i("11111", "----->文件不存在！");
        }
    }

    public void copyFileTOFile(String targetfilepath, String filepath) {
        if (FileUtils.isFileExists(filepath)) {
            if (FileUtils.copyFile(filepath, targetfilepath)) {
                Log.i("11111", "----->复制文件成功！");
            } else {
                Log.i("11111", "----->复制文件失败！");
            }
        } else {
            Log.i("11111", "----->原文件不存在！");
        }
    }

    public void deleteMyFile(String targetfilepath) {
        if (FileUtils.isFileExists(targetfilepath)) {
            if (FileUtils.deleteFile(targetfilepath)) {
                Log.i("11111", "----->删除文件成功！");
            } else {
                Log.i("11111", "----->删除文件失败！");
            }
        } else {
            Log.i("11111", "----->将要被删除的文件不存在！");
        }
    }
}
