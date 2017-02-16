package com.jassgroup.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jassgroup.acceso.clases.Usuario;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.database.ScriptDatabaseUsuario;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.tucarta.HomeActivity;
import com.jassgroup.tucarta.MainActivity;
import com.jassgroup.tucarta.R;

public class EditarProfileActivity extends AppCompatActivity {

    //tag de la actividad
    private static final String TAG = "Editar Perfil";

    //contexto de la actividad
    private Context contexto;

    // iniciamos toolbar
    private Toolbar toolbar;
    String titulo = "CheckInApp";

    //variables de la aplicacion
    private ProgressDialog pDialog;

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

    //////////////////////
    private String apellidos;
    private String nombres;
    private String correo;
    private String clave;
    private String telefono;
    private String imagen;

    private EditText txtApellidos;
    private EditText txtNombres;
    private EditText txtCorreo;
    private EditText txtTelefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_profile);

        pDialog = new ProgressDialog(this);

        contexto = this;

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            titulo = this.getResources().getString(R.string.txt_drawer_perfil_config);
            toolbar.setTitle(titulo);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //Toast.makeText(ActivityDetalleNoticia.this, "Navigation Icon pressed", Toast.LENGTH_LONG).show();
                    EditarProfileActivity.this.onBackPressed();
                }
            });
        }

        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtNombres = (EditText) findViewById(R.id.txtNombres);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);

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
            /*Intent intent = new Intent(EditarProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();*/
        } else {
            // No user is signed in
        }

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("usuarios");

        iniciarPerfil();
    }

    private void iniciarPerfil(){
        apellidos = GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.APELLIDOS);
        nombres = GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.NOMBRES);
        correo = GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CORREO);
        clave = GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CLAVE);
        telefono = GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.TELEFONO);
        imagen = GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.IMAGEN);

        txtApellidos.setText(apellidos);
        txtNombres.setText(nombres);
        txtCorreo.setText(correo);
        txtTelefono.setText(telefono);
    }


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
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "enableForegroundMode");
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_guardar) {

            guardarPerfil();

            return  true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void guardarPerfil(){


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
        if(txtApellidos.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su apellidos completos , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(txtNombres.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su apellidos completos , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }


        ////obtenemos y validamos los datos de login
        apellidos = txtApellidos.getText().toString();
        nombres = txtNombres.getText().toString();
        telefono = txtTelefono.getText().toString();

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Usuario miUsuario = new Usuario(apellidos,nombres,correo,clave,telefono,imagen);

        /*mDatabase.child(uid).child("apellidos").setValue(apellidos);
        mDatabase.child(uid).child("nombres").setValue(nombres);
        mDatabase.child(uid).child("telefono").setValue(telefono);*/

        showProgressDialog();

        mDatabase.child(uid).setValue(miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated.");
                    GestorDatabase.getInstance(contexto).actualizarUsuario(uid,miUsuario);

                    Toast.makeText(getApplicationContext(),
                            "Perfil actualizado...!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Log.d(TAG, "User profile not updated.");
                    Toast.makeText(getApplicationContext(),
                            "Error intente en otro momento...!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
