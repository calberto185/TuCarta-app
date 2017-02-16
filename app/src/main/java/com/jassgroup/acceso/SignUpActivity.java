package com.jassgroup.acceso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//import com.gc.materialdesign.views.ButtonFlat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jassgroup.tucarta.HomeActivity;
import com.jassgroup.tucarta.MainActivity;
import com.jassgroup.tucarta.R;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener  {

    //tag de la actividad
    private static final String TAG = "actSignUp";

    //contexto de la actividad
    private Context contexto;

    //variables de la aplicacion
    private Map<String,String> mMap;
    private ProgressDialog pDialog;

    private String estado;

    private Button miBotonIngresar;
    private Button miBotonRegistro;

    ////////////////////
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private ProgressDialog mProgressDialog;

    private Context context = this;
    //////////////////////

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Cargando Información...!");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        pDialog = new ProgressDialog(this);

        miBotonIngresar = (Button) findViewById(R.id.botonIniciarSesion);
        miBotonRegistro = (Button) findViewById(R.id.botonEmpezar);


        contexto = this;


        miBotonIngresar.setOnClickListener(this);
        miBotonRegistro.setOnClickListener(this);

        //COMPROBAMOS SI ESTADO ES 1, SE ENCUENTRA ACTIVO SU LOGIN Y SE LE ENVIA A PRINCIPAL
        //estado = EventoDatabase.getInstance(this).obtenerValor("estado");

        /*if(estado.compareTo("1")==0){
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        ///////////////////
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No user is signed in
        }

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.botonIniciarSesion:
                //click boton ir a iniciar sesion
                intent = new Intent(this, IniciarSesionActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.botonEmpezar:
                //click boton ir a registrar
                intent = new Intent(this, RegistroUsuarioActivity.class);
                startActivity(intent);
                //finish();
                break;
        }
    }

}
