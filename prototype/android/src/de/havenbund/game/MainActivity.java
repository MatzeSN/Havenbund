package de.havenbund.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.webkit.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

/** Offline Android host. Only the four bundled, allowlisted assets can be loaded. */
public final class MainActivity extends Activity {
    private WebView game;
    private static final String ORIGIN = "https://appassets.androidplatform.net";
    private static final Map<String,String> ASSETS = new java.util.HashMap<>();
    static {
        ASSETS.put("/index.html", "text/html"); ASSETS.put("/style.css", "text/css");
        ASSETS.put("/engine.js", "application/javascript"); ASSETS.put("/app.js", "application/javascript");
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new WebView(this);
        game.setBackgroundColor(0xfff5f2e9);
        game.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
          @Override public android.view.WindowInsets onApplyWindowInsets(View view, android.view.WindowInsets insets) {
            view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
          }
        });
        setContentView(game);
        WebSettings settings = game.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setSupportMultipleWindows(false);
        game.setWebChromeClient(new WebChromeClient());
        game.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !isLocal(request.getUrl());
            }
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (isLocal(uri) && "GET".equals(request.getMethod())) {
                    try {
                        return new WebResourceResponse(ASSETS.get(uri.getPath()), "UTF-8",
                            getAssets().open(uri.getPath().substring(1)));
                    } catch (IOException ignored) { }
                }
                return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found",
                    java.util.Collections.emptyMap(), new ByteArrayInputStream(new byte[0]));
            }
        });
        game.loadUrl(ORIGIN + "/index.html");
    }
    private static boolean isLocal(Uri uri) {
        return "https".equals(uri.getScheme()) && "appassets.androidplatform.net".equals(uri.getHost())
            && uri.getPort() == -1 && ASSETS.containsKey(uri.getPath());
    }
    @Override protected void onPause() {
        game.evaluateJavascript("if(typeof save==='function')save();", null);
        game.onPause();
        super.onPause();
    }
    @Override protected void onResume() { super.onResume(); if (game != null) game.onResume(); }
    @Override public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Havenbund verlassen?")
            .setMessage("Dein Spielstand wird auf diesem Gerät gespeichert.")
            .setNegativeButton("Weiterspielen", null)
            .setPositiveButton("Speichern und beenden", new android.content.DialogInterface.OnClickListener() {
                @Override public void onClick(android.content.DialogInterface dialog, int which) {
                    game.evaluateJavascript("if(typeof save==='function')save();", new ValueCallback<String>() {
                        @Override public void onReceiveValue(String value) { finish(); }
                    });
                }
            }).show();
    }
    @Override protected void onDestroy() { if (game != null) { game.destroy(); game = null; } super.onDestroy(); }
}
