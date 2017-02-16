package com.jassgroup.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.database.ScriptDatabaseApp;

/**
 * Created by kenwhiston on 15/05/2016.
 */
public class SplashActivity extends AppCompatActivity {

    private String intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //COMPROBAMOS SI INTRO ES 1, SE VA A PRINCIPAL, SINO A INTRO

        intro = GestorDatabase.getInstance(this).obtenerValorApp(ScriptDatabaseApp.Column.INTRO);

        if(intro.compareTo("1")==0){
            //ya se vio intro, se va a principal
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, IntroAppActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
