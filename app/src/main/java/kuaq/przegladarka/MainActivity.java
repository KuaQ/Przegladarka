package kuaq.przegladarka;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    EditText putUrl;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //identyfikatory
        putUrl = (EditText)findViewById(R.id.putUrl);
        webView = (WebView)findViewById(R.id.webView);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });
        //edittext funkcje
        putUrl.clearFocus();


        //ustaweiania
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        //tworzenie dialogu
       progressDialog = new ProgressDialog(MainActivity.this);
       progressDialog.setMessage("Ładowanie strony..");


        //nadpisanie klienta webview
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
            }

        //pokazywanie okna podczas ladowania strony
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                String webUrl = webView.getUrl();
                putUrl.setText(webUrl);
            }

        });

        //domyslnei ladwoana strona
        setWebViewUrl(webView, "www.zst.com.pl");

        //pobieranie tekstu po wcisnieciu enter
        putUrl.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if (event.getAction() == KeyEvent.ACTION_DOWN){

                    switch (keyCode){

                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            setWebViewUrl(webView, putUrl.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    //sprawdzanie http i ladowanie storony
    private void setWebViewUrl(WebView webView, String s) {

        if(s.length() >= 7){
            String isHttps = s.substring(0, 7);
            String isHttp = s.substring(0, 6);

            if(isHttps.equals("https://")||isHttp.equals("http://")){
                webView.loadUrl(s);

            }else{
            webView.loadUrl("http://"+s);
        }
        }else{
            webView.loadUrl("http://"+s);
        }
        System.out.println(webView.getOriginalUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            webView.reload();
        }
        return super.onOptionsItemSelected(item);
    }
}
