package com.ci24.datafono;
import com.ci24.functions.Datafono;
import com.ingenico.pclutilities.*;
import com.ingenico.pclservice.*;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.util.Log;
import org.json.JSONArray;

import static org.apache.cordova.device.Device.TAG;

public class Comdata extends CordovaPlugin {
 private CallbackContext callbackContext;
 private String test="Conexion Iniciada";
 private String ser="Servicio Finalizado";
 private String saludo;
  private Datafono datafono;
  protected PclService mPclService;

  public Activity getActivity() {    return this.cordova.getActivity();  }
  private Intent getIntent() {     return getActivity().getIntent();   }
  private void setIntent(Intent intent) {     getActivity().setIntent(intent);   }

 @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) {

       if (action.equals("init")) {
         Datafono.getInstance().initService(getActivity());
          callbackContext.success(test);

        }else if(action.equals("check")){
          boolean check= Datafono.getInstance().isCompanionConnected();
            if(check==true){
              callbackContext.success("Connected");
            }else{
              callbackContext.success("Not Connected");
            }

       }else if(action.equals("finish")){
         Datafono.getInstance().releaseService(getActivity());
         callbackContext.success(ser);

       }else if(action.equals("f")){
      boolean barC=Datafono.getInstance().openBarCode();
         if(barC==true){
           callbackContext.success("Barcode Open");
         }else{
           callbackContext.success("Error Opening Barcode");
         }
       }
     else if(action.equals("transactionEX")){
         int appNumber=0;
         TransactionIn transIn = new TransactionIn();
         TransactionOut transOut = new TransactionOut();
      //   transIn.setAmount("000000500000");
      //   transIn.setCurrencyCode("978");
      //   transIn.setOperation("C");
      //   transIn.setTermNum("58");
      //   transIn.setAuthorizationType("0");
      //   transIn.setCtrlCheque("0");
      //   transIn.setUserData1("Oscar");


        // byte[] extDataIn = new byte[0];
        JSONArray dataIn=args;
         byte extDataIn[]=new byte[]{(byte)0xFC,(byte)0x56,(byte)0xDF,(byte)0xFF,(byte)0x25,(byte)0x0A,(byte)0x37,(byte)0x36,(byte)0x44,(byte)0x46,(byte)0x42,(byte)0x46,(byte)0x37,(byte)0x33,(byte)0x42,(byte)0x35,(byte)0x9A,(byte)0x03,(byte)0x17,(byte)0x01,(byte)0x26,(byte)0x9F,(byte)0x21,(byte)0x03,(byte)0x12,(byte)0x28,(byte)0x00,(byte)0xDC,(byte)0x01,(byte)0x0A,(byte)0xDF,(byte)0xFE,(byte)0x53,(byte)0x0A,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x32,(byte)0x20,(byte)0x20,(byte)0x20,(byte)0x20,(byte)0xDF,(byte)0xFF,(byte)0x2B,(byte)0x0A,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x31,(byte)0x20,(byte)0x20,(byte)0x20,(byte)0x20,(byte)0xDF,(byte)0xFE,(byte)0x40,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x09,(byte)0x00,(byte)0x00,(byte)0xDF,(byte)0xFF,(byte)0x22,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x97,(byte)0x98,(byte)0xDF,(byte)0xFF,(byte)0x23,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x79,(byte)0x87,(byte)0x98};
         byte[] extDataOut = new byte[8000];
         long[] sizebuff=new long[8000];

         int sizeIn=88;


         Datafono.getInstance().doTransactionEx(transIn,transOut,appNumber,extDataIn,sizeIn,extDataOut,sizebuff);


         callbackContext.success("transacition");


       }
     else if(action.equals("t")){
         TransactionIn transIn = new TransactionIn();
         TransactionOut result=new TransactionOut();


         String amount = "995";
         transIn.setAmount(amount);
         transIn.setCurrencyCode("978");
         transIn.setOperation("C");
         transIn.setTermNum("58");
         Log.d(TAG,"envio Tramas!!!!");
         boolean trans = Datafono.getInstance().doTransaction(transIn,result);

         callbackContext.success("getInfo");

       }






        return true;

    }



}
