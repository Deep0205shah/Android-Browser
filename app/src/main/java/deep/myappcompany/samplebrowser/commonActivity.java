package deep.myappcompany.samplebrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import deep.myappcompany.samplebrowser.databinding.ActivityMainBinding;

public class commonActivity extends AppCompatActivity {

    ProgressBar superProgressBar;
    WebView superWebView;
    ImageView superImageView;
    LinearLayout superLinearLayout;
    String myCurrentUrl;
    myDbHandlerBookmarks dbhandlerBook;
    Button goButton;
    myDbHandlerHistory dbhandler;
    ActivityMainBinding binding;
    EditText UrlBar;
    public static final String broadcastStringForAction="checkingInternet";
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        superProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        UrlBar = (EditText)findViewById(R.id.UrlBar);
        goButton = (Button)findViewById(R.id.goButton);
        superWebView = (WebView) findViewById(R.id.myWebView);
        superImageView = (ImageView) findViewById(R.id.myImageView);
        superLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        dbhandler = new myDbHandlerHistory(this,null,null,1);
        dbhandlerBook = new myDbHandlerBookmarks(this,null, null, 1);
        superProgressBar.setMax(100);
        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(broadcastStringForAction);
        Intent serviceIntent = new Intent(this,MyService.class);
        startService(serviceIntent);
        Bundle extras = getIntent().getExtras();
        String s=extras.getString("urls");


        superWebView.loadUrl(s);
        superWebView.getSettings().setJavaScriptEnabled(true);
        superWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                superLinearLayout.setVisibility(View.VISIBLE);
                UrlBar.setText(url.toCharArray(),0,23);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                superLinearLayout.setVisibility(View.GONE);
                super.onPageFinished(view, url);
                myCurrentUrl = url;
                savedata();
            }
        });
        superWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                UrlBar.setText(view.getUrl());
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImageView.setImageBitmap(icon);
            }
        });
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = UrlBar.getText().toString();
                if(str.startsWith("www.") && !(str.startsWith("http://") || str.startsWith("https://"))){
                    str = "http://" + str;
                }
                if(!str.startsWith("www.") && !(str.startsWith("http://") || str.startsWith("https://"))){

                    str=str.replace(" ","+");
                    str="https://www.google.com/search?q=".concat(str);
                }
                superWebView.loadUrl(str);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_back:
                onBackPressed();
                break;

            case R.id.menu_for:
                onforPressed();
                break;
            case R.id.menu_ref:
                superWebView.reload();
                break;
            case R.id.menu_share:
                Intent shareintent = new Intent(Intent.ACTION_SEND);
                shareintent.setType("Text/plain");
                shareintent.putExtra(Intent.EXTRA_TEXT,myCurrentUrl);
                shareintent.putExtra(Intent.EXTRA_SUBJECT,"Copied URL");
                startActivity(Intent.createChooser(shareintent,"Share URL"));
                break;
            case R.id.menu_bkm:
                onBookPressed();
                Toast.makeText(this, "Page Successfully added to Bookmarks!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_bkmList:
                Intent in = new Intent(getApplicationContext(),MainActivity2.class);

                startActivity(in);
                break;
            case R.id.menu_history:
                Intent inte = new Intent(getApplicationContext(),history.class);

                startActivity(inte);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(broadcastStringForAction)){
                if(!intent.getStringExtra("online_status").equals("true"))
                    Toast.makeText(commonActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    public boolean isOnline(Context c){
        ConnectivityManager cm =(ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni !=null && ni.isConnectedOrConnecting())
            return true;
        else
            return false;

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver,mIntentFilter);
    }


    private void onforPressed(){
        if(superWebView.canGoForward()){
            superWebView.goForward();
        } else  {
            Toast.makeText(this, "Can't go further!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        if(superWebView.canGoBack()){
            superWebView.goBack();
        } else  {
            finish();
        }
    }
    private void onBookPressed(){
        websites web = new websites(superWebView.getUrl());
        dbhandlerBook.addurl(web);
        savedata();

    }
    private void savedata(){
        websites webv = new websites(superWebView.getUrl());
        dbhandler.addurl(webv);
    }

}