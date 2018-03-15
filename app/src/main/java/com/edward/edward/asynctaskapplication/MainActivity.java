package com.edward.edward.asynctaskapplication;

import com.edward.edward.asynctaskapplication.utils.MyAsyncTask;
import com.edward.edward.asynctaskapplication.utils.asynctask.IDoInBackground;
import com.edward.edward.asynctaskapplication.utils.asynctask.IIsViewActive;
import com.edward.edward.asynctaskapplication.utils.asynctask.IPostExecute;
import com.edward.edward.asynctaskapplication.utils.asynctask.IPreExecute;
import com.edward.edward.asynctaskapplication.utils.asynctask.IProgressUpdate;
import com.edward.edward.asynctaskapplication.utils.asynctask.IPublishProgress;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextView = findViewById(R.id.main_textview);
        loadData();
    }

    /**
     * 全功能调用方式
     */
    private void loadData() {
        MyAsyncTask.<String, Integer, Boolean>newBuilder()
                .setPreExecute(new IPreExecute() {
                    @Override
                    public void onPreExecute() {
                        mainTextView.setText("开始下载数据……");
                    }
                })
                .setDoInBackground(new IDoInBackground<String, Integer, Boolean>() {
                    @Override
                    public Boolean doInBackground(IPublishProgress<Integer> publishProgress, String... strings) {
                        try {
                            for (int i = 1; i < 11; i++) {
                                Thread.sleep(1000);
                                publishProgress.showProgress(i);
                            }
                        } catch (Exception e) {
                            return false;
                        }
                        return true;
                    }
                })
                .setProgressUpdate(new IProgressUpdate<Integer>() {
                    @Override
                    public void onProgressUpdate(Integer... values) {
                        mainTextView.setText("正在下载数据，当前进度为：" + (values[0] * 100 / 10) + "%");
                    }
                })
                .setViewActive(new IIsViewActive() {
                    @Override
                    public boolean isViewActive() {
                        return MainActivity.this.isViewActive();
                    }
                })
                .setPostExecute(new IPostExecute<Boolean>() {
                    @Override
                    public void onPostExecute(Boolean aBoolean) {
                        if (aBoolean) {
                            mainTextView.setText("下载成功");
                        } else {
                            mainTextView.setText("下载失败");
                        }
                    }
                })
                .start("参数");
    }

    /**
     * @return 判断当前Activity是否处于活跃状态
     */
    public boolean isViewActive() {
        return !(isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()));
    }

    /**
     * 最简短的调用方式
     */
    private void saveData() {
        MyAsyncTask.<Void, Void, Void>newBuilder()
                .setDoInBackground(new IDoInBackground<Void, Void, Void>() {
                    @Override
                    public Void doInBackground(IPublishProgress<Void> publishProgress, Void... voids) {
                        //TODO:执行数据保存
                        return null;
                    }
                })
                .start();
    }
}
