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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.tucarta.HomeActivity;
import com.jassgroup.tucarta.MainActivity;
import com.jassgroup.tucarta.R;
import com.jassgroup.acceso.clases.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuarioActivity extends AppCompatActivity implements View.OnClickListener  {

    //tag de la actividad
    private static final String TAG = "actRegistroUsuario";

    //contexto de la actividad
    private Context contexto;

    //variables de la aplicacion
    private Map<String,String> mMap;
    private ProgressDialog pDialog;

    //toolbar de la aplicacion
    private Toolbar toolbar;

    ///variables principales de la clase login
    private String apellidos;
    private String nombres;
    private String correo;
    private String clave;

    private EditText cajaApe;
    private EditText cajaNom;
    private EditText cajaCorreo;
    private EditText cajaClave;

    private TextView txtLogin;

    private Button miBotonCrear;

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
    private DatabaseReference mDatabase;


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Registrando usuario...!");
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
        setContentView(R.layout.activity_registro_usuario);

        //inicializando Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.txt_registrar_cuenta);

        pDialog = new ProgressDialog(this);

        miBotonCrear = (Button) findViewById(R.id.botonCrear);

        cajaApe = (EditText) findViewById(R.id.txtApellidos);
        cajaNom = (EditText) findViewById(R.id.txtNombres);
        cajaNom = (EditText) findViewById(R.id.txtNombres);
        cajaCorreo = (EditText) findViewById(R.id.txtCorreo);
        cajaClave = (EditText) findViewById(R.id.txtClave);

        txtLogin = (TextView) findViewById(R.id.txtIniciarSesion);

        contexto = this;

        pDialog = new ProgressDialog(this);

        //miBotonCrear.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));


        miBotonCrear.setOnClickListener(this);
        txtLogin.setOnClickListener(this);

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
            Intent intent = new Intent(RegistroUsuarioActivity.this, HomeActivity.class);
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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.botonCrear:
                //click boton ir a registrar
                RegistrarUsuario();
                break;
            case R.id.txtIniciarSesion:
                Intent intent = new Intent(RegistroUsuarioActivity.this, IniciarSesionActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void RegistrarUsuario(){
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
        if(cajaApe.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su apellidos completos , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cajaNom.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su apellidos completos , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cajaCorreo.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su correo correctamente , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(cajaClave.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su clave, porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }

        ////obtenemos y validamos los datos de login
        apellidos = cajaApe.getText().toString();
        nombres = cajaNom.getText().toString();
        correo = cajaCorreo.getText().toString();
        clave = cajaClave.getText().toString();
        // Tag used to cancel the request

        //pDialog.setMessage("Registrando Usuario...!");
        //pDialog.show();

        /*Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("nombre", nombres.toUpperCase());
        jsonParams.put("apellido", apellidos.toUpperCase());
        jsonParams.put("usuario", correo.toLowerCase());
        jsonParams.put("password", clave.trim());
        jsonParams.put("email", correo.toLowerCase());
        jsonParams.put("telefono", "-");
        jsonParams.put("codpais", "+51");*/

        registrarUsuario(apellidos,nombres,correo,clave,"---");

        ///////////////////////////////////////////////
    }

    private void registrarUsuario(final String apellidos, final String nombres, final String correo, final String clave, final String telefono){

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        String uid = task.getResult().getUser().getUid();

                        Usuario miUsuario = new Usuario(apellidos,nombres,correo,clave,telefono,"---");

                        GestorDatabase.getInstance(context).EliminarUsuarios();
                        GestorDatabase.getInstance(context).registrarUsuario(uid,miUsuario);

                        mDatabase.child(uid).setValue(miUsuario);
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistroUsuarioActivity.this, "No se pudo registrar usuario, intente en otro momento porfavor...!",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            //guardamos los demas datos

                            Intent intent = new Intent(RegistroUsuarioActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }


                        // ...
                    }
                });


    }
}
