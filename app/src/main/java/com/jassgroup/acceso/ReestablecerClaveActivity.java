package com.jassgroup.acceso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.tucarta.HomeActivity;
import com.jassgroup.tucarta.MainActivity;
import com.jassgroup.tucarta.R;

import java.util.Map;

public class ReestablecerClaveActivity extends AppCompatActivity implements View.OnClickListener  {

    //tag de la actividad
    private static final String TAG = "actRenovarClave";

    //contexto de la actividad
    private Context contexto;

    //variables de la aplicacion
    private Map<String,String> mMap;
    private ProgressDialog pDialog;

    //toolbar de la aplicacion
    private Toolbar toolbar;

    ///variables principales de la clase login
    private String usuario;

    private EditText cajaUsuario;

    private Button miBotonRenovar;

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
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Enviando correo para proceso de renovacion de clave...!");
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
        setContentView(R.layout.activity_reestablecer_clave);

        //inicializando Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.txt_iniciar_sesion);

        pDialog = new ProgressDialog(this);

        miBotonRenovar = (Button) findViewById(R.id.btn_renovarclave);
        cajaUsuario = (EditText) findViewById(R.id.txtUsuario);

        contexto = this;

        //miBotonRenovar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));

        miBotonRenovar.setOnClickListener(this);

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
            Intent intent = new Intent(ReestablecerClaveActivity.this, MainActivity.class);
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
        hideProgressDialog();
    }
    // [END on_stop_remove_listener]

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_renovarclave:
                //click boton ingresar
                renovarclave();
                break;
        }
    }

    private void renovarclave(){
        DetectorRed cd = new DetectorRed(getApplicationContext());

        Boolean isInternetPresent = cd.estasConectadoInternet(); // true or false

        if(!isInternetPresent){
            //hay servicio de internet
            //no hay internet
            Toast.makeText(getApplicationContext(),
                    "No Esta Conectado a Internet...!", Toast.LENGTH_SHORT).show();
            return;
        }

        ///validamos si hay datos en los campos
        if(cajaUsuario.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingreseo Correo, porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }

        ////obtenemos y validamos los datos de login
        usuario = cajaUsuario.getText().toString();

        showProgressDialog();

        mAuth.sendPasswordResetEmail(usuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getApplicationContext(),
                                    "Correo enviado para renovacion de clave, reviselo porfavor...!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
