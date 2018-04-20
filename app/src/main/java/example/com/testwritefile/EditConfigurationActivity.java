package example.com.testwritefile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/4/19.
 */

public class EditConfigurationActivity extends AppCompatActivity {

    private Activity mActivity;
    private EditText et_editconfiguration_content;
    private Button btn_editconfiguration_submit;
    private String content = "";
    private List<String> configurationlist;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editconfiguration);
        mActivity = EditConfigurationActivity.this;
        et_editconfiguration_content = (EditText) findViewById(R.id.et_editconfiguration_content);
        btn_editconfiguration_submit = (Button) findViewById(R.id.btn_editconfiguration_submit);


        btn_editconfiguration_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = et_editconfiguration_content.getText().toString().toLowerCase();
                if (!StringUtils.isBlank(content)) {
                    if (!StringUtils.isBlank(configurationlist)) {
                        configurationlist.set(position, content);
                        if (deleteDataFromFile(MainActivity.targetFilePath, configurationlist)) {
                            Intent intent = new Intent(mActivity,
                                    ConfigurationDisplayActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(mActivity, "修改失败！", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (addDataToFile(MainActivity.targetFilePath, content)) {
                            Intent intent = new Intent(mActivity,
                                    ConfigurationDisplayActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(mActivity, "添加失败！", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(mActivity, "内容不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        try {
            Intent intent = getIntent();
            position = intent.getIntExtra("position", 0);
            configurationlist = intent.getStringArrayListExtra("configurationlist");
            if (!StringUtils.isBlank(configurationlist)) {
                et_editconfiguration_content.setText(configurationlist.get(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 添加数据
     *
     * @param filepath
     */
    public static boolean addDataToFile(String filepath, String content) {
        boolean addflag = false;
        if (FileUtils.isFileExists(filepath)) {
            List<String> list = FileUtils.readFile2List(filepath, "utf-8");
            StringBuilder sbstr = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i == (list.size() - 1)) {
                    sbstr.append(list.get(i) + "\r\n" + content);
                } else {
                    sbstr.append(list.get(i) + "\r\n");
                }

            }
            addflag = writeDataToFile(filepath, sbstr.toString(), false);
        } else {
            if (FileUtils.createOrExistsFile(filepath)) {
                addflag = writeDataToFile(filepath, content, false);
            } else {
                Log.i("11111", "----->创建文件失败！");
                addflag = false;
            }
        }
        return addflag;
    }

    /**
     * 将数据写入到文件中
     */
    public static boolean writeDataToFile(String filepath, String content, boolean append) {
        boolean writeflag = false;
        InputStream in = new ByteArrayInputStream(content.getBytes());
        if (FileUtils.writeFileFromIS(filepath, in, append)) {
            Log.i("22222", "----->文件写入成功！");
            writeflag = true;
        } else {
            Log.i("22222", "----->文件写入失败！");
            writeflag = false;
        }
        return writeflag;
    }

    /**
     * 区分是域名还是ip段
     */
    private static String pattern_ip = "(\\d*\\.){3}\\d*";//ip正则验证

    public static Map<String, List<String>> domainOrIp(List<String> list) {
        Map<String, List<String>> listMap = new HashMap<>();
        List<String> domainList = new ArrayList<>();
        List<String> ipList = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            String[] arrays = list.get(j).trim().split("\\s+");
            Pattern ipPattern = Pattern.compile(pattern_ip);
            for (int i = 0; i < arrays.length; i++) {
                Matcher matcher = ipPattern.matcher(arrays[i]);
                boolean ipAddressflag = matcher.find();
                if (ipAddressflag) {
                    ipList.add(arrays[i]);
                } else {
                    domainList.add(arrays[i]);
                }
            }
        }
        listMap.put("domainList", domainList);
        listMap.put("ipList", ipList);
        return listMap;
    }

    /**
     * 将android_d2ocustomize文件数据转化成android_d2o文件格式
     */
    public static StringBuilder conversionFlieData(Map<String, List<String>> contentMap) {
        List<String> list = FileUtils.readFile2List("", "utf-8");
        StringBuilder sbstr = new StringBuilder();
        if (!StringUtils.isBlank(list) && list.size() > 0) {
            //文件中已经有内容
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).toString().startsWith("proxy_domain")) {
                    if (contentMap.get("domainList").size() > 0) {
                        if (contentMap.get("domainList").size() == 1) {
                            sbstr.append(list.get(i).toString() + " " + contentMap.get("domainList").get(0) + "\r\n");
                        } else {
                            for (int j = 0; j < contentMap.get("domainList").size(); j++) {
                                if (j == 0) {
                                    sbstr.append(list.get(i).toString() + " " + contentMap.get("domainList").get(j) + " ");
                                } else if (j == (contentMap.get("domainList").size() - 1)) {
                                    sbstr.append(contentMap.get("domainList").get(j) + "\r\n");
                                } else {
                                    sbstr.append(contentMap.get("domainList").get(j) + " ");
                                }
                            }
                        }
                    } else {
                        sbstr.append(list.get(i).toString() + "\r\n");
                    }
                } else if (list.get(i).toString().startsWith("ipproxy")) {
                    if (contentMap.get("ipList").size() > 0) {
                        if (contentMap.get("ipList").size() == 1) {
                            sbstr.append(list.get(i).toString() + " " + contentMap.get("ipList").get(0) + "\r\n");
                        } else {
                            for (int j = 0; j < contentMap.get("ipList").size(); j++) {
                                if (j == 0) {
                                    sbstr.append(list.get(i).toString() + " " + contentMap.get("ipList").get(j) + " ");
                                } else if (j == (contentMap.get("ipList").size() - 1)) {
                                    sbstr.append(contentMap.get("ipList").get(j) + "\r\n");
                                } else {
                                    sbstr.append(contentMap.get("ipList").get(j) + " ");
                                }
                            }
                        }
                    } else {
                        sbstr.append(list.get(i).toString() + "\r\n");
                    }
                }
            }
        } else {
            //文件中没有内容
            if (contentMap.get("domainList").size() > 0) {
                if (contentMap.get("domainList").size() == 1) {
                    sbstr.append("proxy_domain" + " " + contentMap.get("domainList").get(0) + "\r\n");
                } else {
                    for (int j = 0; j < contentMap.get("domainList").size(); j++) {
                        if (j == 0) {
                            sbstr.append("proxy_domain" + " " + contentMap.get("domainList").get(j) + " ");
                        } else if (j == (contentMap.get("domainList").size() - 1)) {
                            sbstr.append(contentMap.get("domainList").get(j) + "\r\n");
                        } else {
                            sbstr.append(contentMap.get("domainList").get(j) + " ");
                        }
                    }
                }
            } else {
                sbstr.append("proxy_domain" + "\r\n");
            }
            if (contentMap.get("ipList").size() > 0) {
                if (contentMap.get("ipList").size() == 1) {
                    sbstr.append("ipproxy" + " " + contentMap.get("ipList").get(0) + "\r\n");
                } else {
                    for (int j = 0; j < contentMap.get("ipList").size(); j++) {
                        if (j == 0) {
                            sbstr.append("ipproxy" + " " + contentMap.get("ipList").get(j) + " ");
                        } else if (j == (contentMap.get("ipList").size() - 1)) {
                            sbstr.append(contentMap.get("ipList").get(j) + "\r\n");
                        } else {
                            sbstr.append(contentMap.get("ipList").get(j) + " ");
                        }
                    }
                }
            } else {
                sbstr.append("ipproxy" + "\r\n");
            }
        }
        return sbstr;
    }

    /**
     * 删除或者修改文件中的数据（利用重写文件内容来实现）
     */
    public static boolean deleteDataFromFile(String filepath, List<String> list) {
        boolean deleteflag = false;
        if (FileUtils.isFileExists(filepath)) {
            StringBuilder sbstr = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sbstr.append(list.get(i) + "\r\n");
            }
            deleteflag = writeDataToFile(filepath, sbstr.toString(), false);
        }
        return deleteflag;
    }


}
