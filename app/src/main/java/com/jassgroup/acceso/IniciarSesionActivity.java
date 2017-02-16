package com.jassgroup.acceso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jassgroup.acceso.clases.Usuario;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.tucarta.HomeActivity;
import com.jassgroup.tucarta.MainActivity;
import com.jassgroup.tucarta.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener  {

    //tag de la actividad
    private static final String TAG = "actIniciarSesion";

    //contexto de la actividad
    private Context contexto;

    //variables de la aplicacion
    private Map<String,String> mMap;
    private ProgressDialog pDialog;

    private CoordinatorLayout coordinatorLayout;

    //toolbar de la aplicacion
    private Toolbar toolbar;

    ///variables principales de la clase login
    private String usuario;
    private String clave;
    private String estado;
    private String apenom;
    private String correo;
    private String iduser;

    private EditText cajaUsuario;
    private EditText cajaClave;

    private Button miBotonIngresar;
    private TextView miTextoRegistro;
    private TextView miTextoOlvido;

    ////////////////////
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private DatabaseReference mDatabase;

    private Usuario miUsuario;

    private ProgressDialog mProgressDialog;

    private FirebaseUser user;

    private Context context = this;
    //////////////////////

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Iniciando Sesion...!");
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
        setContentView(R.layout.activity_iniciar_sesion);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        //inicializando Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.txt_iniciar_sesion);

        pDialog = new ProgressDialog(this);

        miBotonIngresar = (Button) findViewById(R.id.btn_guardar);
        miTextoRegistro = (TextView) findViewById(R.id.texto_registro_ahora);
        miTextoOlvido = (TextView) findViewById(R.id.texto_olvido_clave);
        cajaUsuario = (EditText) findViewById(R.id.txtUsuario);
        cajaClave = (EditText) findViewById(R.id.txtClave);

        contexto = this;

        //miBotonIngresar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));


        miBotonIngresar.setOnClickListener(this);
        miTextoRegistro.setOnClickListener(this);
        miTextoOlvido.setOnClickListener(this);

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(IniciarSesionActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No user is signed in
        }

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("usuarios");

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
            case R.id.btn_guardar:
                //click boton ingresar
                validarAcceso();
                break;
            case R.id.texto_registro_ahora:
                //click boton ir a registrar
                Intent intent = new Intent(this, RegistroUsuarioActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.texto_olvido_clave:
                //click boton ir a registrar
                Intent intent2 = new Intent(this, ReestablecerClaveActivity.class);
                startActivity(intent2);
                //finish();
                break;
        }
    }

    private void validarAcceso(){
        DetectorRed cd = new DetectorRed(getApplicationContext());

        Boolean isInternetPresent = cd.estasConectadoInternet(); // true or false

        if(!isInternetPresent){
            //hay servicio de internet
            //no hay internet
            /*Toast.makeText(getApplicationContext(),
                    "No Esta Conectado a Internet...!", Toast.LENGTH_SHORT).show();*/
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No Esta Conectado a Internet...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        ///validamos si hay datos en los campos
        if(cajaUsuario.getText().toString().isEmpty()){
            /*Toast.makeText(getApplicationContext(),
                    "Ingrese su Usuario o Correo, porfavor...!",Toast.LENGTH_SHORT).show();*/
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Ingrese su Usuario o Correo, porfavor...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        if(cajaClave.getText().toString().isEmpty()){
            /*Toast.makeText(getApplicationContext(),
                    "Ingrese su Clave, porfavor...!",Toast.LENGTH_SHORT).show();*/
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Ingrese su Clave, porfavor...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        ////obtenemos y validamos los datos de login
        usuario = cajaUsuario.getText().toString();
        clave = cajaClave.getText().toString();

        //pDialog.setMessage("Verificando Datos...!");
        //pDialog.show();
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(usuario, clave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        //hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            hideProgressDialog();
                            Log.w(TAG, "signInWithEmail", task.getException());
                            /*Toast.makeText(IniciarSesionActivity.this, "Inicio de sesion fallido...!",
                                    Toast.LENGTH_SHORT).show();*/
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Inicio de sesion fallido...!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{

                            user = FirebaseAuth.getInstance().getCurrentUser();

                            mDatabase.child(user.getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user value
                                            hideProgressDialog();
                                            miUsuario = dataSnapshot.getValue(Usuario.class);

                                            GestorDatabase.getInstance(context).EliminarUsuarios();

                                            GestorDatabase.getInstance(context).registrarUsuario(user.getUid(),miUsuario);

                                            Intent intent = new Intent(IniciarSesionActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                            // ...
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            hideProgressDialog();
                                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                        }
                                    });


                        }

                        // ...
                    }
                });
        ///////////////////////////////////////////////
    }

}
