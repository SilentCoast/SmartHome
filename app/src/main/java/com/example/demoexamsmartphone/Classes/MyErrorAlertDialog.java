package com.example.demoexamsmartphone.Classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.android.volley.VolleyError;

public final class MyErrorAlertDialog {
    private MyErrorAlertDialog(){}
    public  static void ShowAlertDialog(Context context,String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
    public  static void ShowAlertDialog(Context context, VolleyError error){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String errorMessage = error.toString();
        if(error.getMessage()!=null){
            errorMessage+="\n" + errorMessage;
        }
        builder.setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
