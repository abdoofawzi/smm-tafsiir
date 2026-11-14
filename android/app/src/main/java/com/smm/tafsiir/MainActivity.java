package com.smm.tafsiir;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    static final String HOME = "https://abdoofawzi.github.io/smm-tafsiir/";
    WebView web;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#14213d"));
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setUserAgentString(s.getUserAgentString() + " SMMTafsiirApp/1");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri u = r.getUrl();
                if ("abdoofawzi.github.io".equals(u.getHost())) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception e) { }
                return true;
            }
            @Override
            public void onReceivedError(WebView v, WebResourceRequest r, WebResourceError e) {
                if (r.isForMainFrame()) {
                    v.loadDataWithBaseURL(null,
                        "<html><body style='background:#14213d;color:#fff;font-family:sans-serif;text-align:center;padding-top:40%'>"
                        + "<h2>Internet ma jiro</h2><p>Fadlan hubi internetka, kadibna riix.</p>"
                        + "<button style='background:#e9a23b;border:0;padding:12px 28px;border-radius:8px;font-size:16px' "
                        + "onclick=\"location.href='" + HOME + "'\">Isku day mar kale</button></body></html>",
                        "text/html", "utf-8", null);
                }
            }
        });
        if (b != null) web.restoreState(b); else web.loadUrl(HOME);
    }

    @Override protected void onSaveInstanceState(Bundle o) { super.onSaveInstanceState(o); web.saveState(o); }

    @Override
    public void onBackPressed() {
        // let the page close its own sheets/menus first
        web.evaluateJavascript("(window.smmBack&&window.smmBack())?'1':'0'", res -> {
            if ("\"1\"".equals(res)) return;
            if (web.canGoBack()) web.goBack(); else finish();
        });
    }
}
