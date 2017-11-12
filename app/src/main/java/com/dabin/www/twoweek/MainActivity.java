package com.dabin.www.twoweek;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.dabin.www.twoweek.adapter.HomeAdapter;
import com.dabin.www.twoweek.bean.NewBase;
import com.dabin.www.twoweek.presenter.Presenter;
import com.dabin.www.twoweek.tools.API;
import com.dabin.www.twoweek.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView{

    private XRecyclerView xRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xrecyclerview);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new Presenter(this).setUrl(API.BASEURL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Presenter(MainActivity.this).setUrl(API.BASEURL);
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                /*new Presenter(MainActivity.this).setUrl(API.BASEURL);*/
                xRecyclerView.loadMoreComplete();
            }
        });

    }


    @Override
    public void getData(final List<NewBase.DataBean> data) {
        HomeAdapter homeAdapter = new HomeAdapter(MainActivity.this, data);
        xRecyclerView.setAdapter(homeAdapter);
        homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void dianji(View view, final int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("该图片")
                        .setMessage("是否下载")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              showDownloadProgressDialog(MainActivity.this,data.get(position).getImg());
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
    }


    private void showDownloadProgressDialog(Context context,String url) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);                    //设置不可点击界面之外的区域让对话框小时
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         //进度条类型
        progressDialog.show();
      /*  String downloadUrl = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk"; //这里写你的apk url地址*/
        new DownloadAPK(progressDialog).execute(url);
    }


    //下载APK
    public class DownloadAPK extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        File file;

        public DownloadAPK(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;

            try {
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);

                int fileLength = conn.getContentLength();
                bis = new BufferedInputStream(conn.getInputStream());
                String fileName = Environment.getExternalStorageDirectory().getPath() + "/magkare/action.jpg";
                file = new File(fileName);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                byte data[] = new byte[4 * 1024];
                long total = 0;
                int count;
                while ((count = bis.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(data, 0, count);
                    fos.flush();
                }
                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            openFile(file);
            progressDialog.dismiss();
        }

        private void openFile(File file) {
            if (file!=null){
                Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                MainActivity.this.startActivity(intent);*/
            }

        }
    }
}
