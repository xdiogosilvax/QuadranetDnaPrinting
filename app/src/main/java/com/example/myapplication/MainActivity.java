package com.example.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Printer;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication.service.DnaService;
import com.example.myapplication.service.IDnaService;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;

public class MainActivity extends Activity {

    private IDnaService _dnaService; //service to call epos

    private static IDAL dal;
    private WebView mywebView;
   // static{
   //     System.loadLibrary("DeviceConfig");
   // }
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
         String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        //move printer test code to a separate method
        //tryPrinter();

        mywebView=(WebView) findViewById(R.id.webview);

        WebSettings webSettings=mywebView.getSettings();

       webSettings.setJavaScriptEnabled(true);
       webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        mywebView.loadUrl("https://dbxqa3.quadranet.co.uk/Login/564e699f-0f8f-48f3-b2b0-60a56de9563d");

    }
//    public class mywebClient extends WebViewClient {
 //       @Override
  //      public void onPageStarted(WebView view, String url, Bitmap favicon) {
   //         super.onPageStarted(view,url,favicon);
    //    }
     //   @Override
      //  public boolean shouldOverrideUrlLoading(WebView view, String url) {
       //     view.loadUrl(url);
        //    return true;
       // }
    //}
    //@Override
    //public void onBackPressed() {
     //   if (mywebView.canGoBack()) {
      //      mywebView.goBack();
       // } else {
        //    super.onBackPressed();
        //}
   // }
    public IDAL getDal() {
        if(dal == null){ //dal is a private static IDAL variable of the application
            try {
                dal = NeptuneLiteUser.getInstance().getDal(this);
            } catch (Exception e) {}
        }
        return dal;
    }

    private void tryPrinter()
    {
        try {
            IPrinter printer= getDal().getPrinter();
            printer.init();
            EFontTypeAscii asciiFontType = EFontTypeAscii.FONT_12_24;
            EFontTypeExtCode fontTypeExtCode = EFontTypeExtCode.FONT_24_24;
            printer.fontSet(asciiFontType, fontTypeExtCode);
            // continue printing...
            // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hellodiogo);
            byte wordSpace = 2;
            byte lineSpace = 30;
            printer.spaceSet(wordSpace, lineSpace);
            printer.printStr("Hello Diogo \n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.printStr("************\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.printStr("Diogo test\n",null);

            //printer.printBitmap(bitmap);
            printer.start();
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //connect to service
        Intent serviceIntent = new Intent(this, DnaService.class);
        startService(serviceIntent);
        bindService(serviceIntent, _serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {

        if (_serviceConnection != null)
        {
            unbindService(_serviceConnection);
        }

        super.onStop();
    }

    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _dnaService = ((DnaService.DnaServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}