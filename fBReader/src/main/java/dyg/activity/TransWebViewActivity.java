package dyg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.Toast;

import org.geometerplus.zlibrary.ui.android.R;

public class TransWebViewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tranlate_web);
        Intent intent = getIntent();
        String string = intent.getStringExtra("key");
        if (string == null) {
            Toast.makeText(this, "no key word ,can't translate", Toast.LENGTH_SHORT).show();
            finish();
        }
        WebView webView = (WebView) findViewById(R.id.layout_webview);
        webView.loadUrl("http://apii.dict.cn/mini.php?q=" + string);

    }
}
