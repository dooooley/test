package com.example.mccarthy.Calculate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        //region 规避主线程禁止网络访问设置
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        //endregion
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    double numa = 0;
    double numb = 0;
    //运算符号
    String calcsysmbol = "";

    //规避主线程访问网络限制
    private String _request(String _pathurl) {
        try {
            URL _url = new URL(_pathurl);
            HttpURLConnection _urlCon = (HttpURLConnection) _url.openConnection();
            InputStream _inStream = new BufferedInputStream(_urlCon.getInputStream());
            String _result = "";
            Scanner _scanner = new Scanner(_inStream).useDelimiter("\\A");
            _result = _scanner.hasNext() ? _scanner.next() : "";
            return _result;
        } catch (Exception e) {
            return "";
        }
        //return "";
    }

    private void netRequset() {
        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                new AlertDialog.Builder(MainActivity.this).setTitle("Net Result")
                        .setNegativeButton("确定", null)
                        .setPositiveButton("取消", null)
                        .setMessage(s).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result = _request("http://www.baidu.com");
                return result;
            }
        }.execute();
    }

    public void btnClick(View view) throws Exception {
        String str = ((Button) view).getText().toString();
        //获取输入框
        EditText _calcEditText = (EditText) findViewById(R.id.editText);
        TextView _resTextView = (TextView) findViewById(R.id.resultTextView);

        netRequset();
        //new AlertDialog.Builder(this).setTitle("Net Result")
        //        .setNegativeButton("确定", null)
        //        .setPositiveButton("取消", null)
        //        .setMessage(netRequest()).show();

        if (true)
            return;
        //region 下面跟网络请求无关啦 计算相关
        switch (str) {
            case "MC":
            case "mc":
            case "C":
                numa = 0;
                numb = 0;
                _calcEditText.setText("");
                break;
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case ".":
                _calcEditText.setText(_calcEditText.getText() + str);
                if (numa != 0) {

                }
                break;
            case "+":
            case "/":
            case "-":
            case "*":
                //设置运算符号
                TextView _sbTextView = (TextView) findViewById(R.id.sysmbolTextView);
                _sbTextView.setText(str);


                if (numa == 0)
                    numa = Double.parseDouble(_calcEditText.getText().toString());
                else if (numb == 0) {
                    String tempstr = _calcEditText.getText().toString();
                    String _value = tempstr.substring(tempstr.indexOf(str) + 1, tempstr.length());
                    numb = Double.parseDouble(_value);

                } else {
                    double _res = calcResult(numa, numb, calcsysmbol);
                    //第一波计算完成
                    _calcEditText.setText(Double.toString(_res));
                }
                calcsysmbol = str;
                _calcEditText.setText(_calcEditText.getText() + str);
                break;
            case "=":
                double _res = calcResult(numa, numb, calcsysmbol);
                //第一波计算完成
                _calcEditText.setText(Double.toString(_res));
                break;


        }
        //endregion

        //操作完成后始终保持光标在最后一位
        _calcEditText.setSelection(_calcEditText.getText().length());
    }

    private double calcResult(double _numa, double _numb, String _sysmbol) {
        double result = 0;
        //region 代码折叠
        switch (_sysmbol) {
            case "+":
                result = _numa + _numb;
                break;
            case "-":
                result = _numa - _numb;
                break;
            case "*":
                result = _numa * _numb;
                break;
            case "/":
                result = _numa / _numb;
                break;
        }
        //endregion
        numb = 0;
        numa = result;
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.mccarthy.Calculate/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.mccarthy.Calculate/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
