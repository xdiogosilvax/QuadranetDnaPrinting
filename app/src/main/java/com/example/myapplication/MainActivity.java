package com.example.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Build;
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private IDnaService _dnaService; //service to call epos

    private static IDAL dal;
    private WebView mywebView;
   // static{
   //     System.loadLibrary("DeviceConfig");
   // }
    protected void onCreate(Bundle savedInstanceState) {


        String serialNumber = getSerialNumber();
        String ipAddress =getIPAddress();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
         //String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        tryPrinter();
        try{
        mywebView=(WebView) findViewById(R.id.webview);
 
        WebSettings webSettings=mywebView.getSettings();

       webSettings.setJavaScriptEnabled(true);
       webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
            mywebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
        mywebView.loadUrl("https://dbxdev.quadranet.co.uk/Login/1875AF6E-4E1D-49B2-819C-32837D15FAF3/P/"+ipAddress+"/"+serialNumber);

        }
        catch (Exception ex){}
    }

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

            printer.leftIndent(10);

            // continue printing...
            // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hellodiogo);
            byte wordSpace = 2;
            byte lineSpace = 30;

            printer.getDotLine();

            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("Diogo's Bistro \n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("Address123\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("HP11 1AW\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("Tel:123456789\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("test@quadranet.co.uk\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("**************************\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("1x   Beer     £01.00\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("1x   Wine     £100.00\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("2x   Food     £100.00\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("2x ItemTotal  £201.00\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("VAT(20%)      %30.12\n",null);
            printer.leftIndent(10);
            printer.spaceSet(wordSpace, lineSpace);
            printer.printStr("**************************\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            LocalDateTime now = LocalDateTime.now();
            printer.printStr(now +"\n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("Table: Terrace  \n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("Server: Diogo  \n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("OrderNo: 123424243  \n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
            printer.printStr("VAT No.: 098978765567  \n",null);
            printer.spaceSet(wordSpace, lineSpace);
            printer.leftIndent(10);
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

    // Get the device's serial number
    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    // Get the device's IP address
    public static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (isIPv4) {
                            return sAddr;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}