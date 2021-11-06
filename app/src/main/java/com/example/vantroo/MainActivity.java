package com.example.vantroo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webview;
    private LinearLayout lnrBlock;
    Activity activity;
    ProgressBar progressBar;
    private Button btnReload;

    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnrBlock = findViewById(R.id.lnr_block);

        activity = MainActivity.this;

        progressBar = findViewById(R.id.progressBar);
        webview = (WebView) findViewById(R.id.webview);

        btnReload = findViewById(R.id.btn_reload);
        btnReload.setOnClickListener(_onclickListener);

        reload();


    }

    public void loadErrorPage(WebView webview) {
        if (webview != null) {
            webview.loadUrl("about:blank");
            lnrBlock.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private final View.OnClickListener _onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_reload: {
                    reload();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    private void reload() {
        lnrBlock.setVisibility(View.GONE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
                view.loadUrl(url);
                progressBar.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
                progressBar.setVisibility(View.GONE);
            }


        });

        webview.loadUrl("https://vantroo.com/");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                progDailog.dismiss();
                loadErrorPage(webview);
            }

        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
        });


        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Log.d(TAG,url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
    }

    private String TAG = "MyActivity";

    private void checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }


}