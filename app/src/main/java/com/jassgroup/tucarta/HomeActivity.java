package com.jassgroup.tucarta;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.acceso.clases.Usuario;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.database.ScriptDatabaseUsuario;
import com.jassgroup.profile.EditarProfileActivity;
import com.jassgroup.tucarta.fragments.InicioFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

public class HomeActivity extends AppCompatActivity {

    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private Bundle savedInstanceStateApp;

    private static final String KEY_CICLE = "ESTADO_FRAGMENT";
    private static final String TAG = "PREFS";

    // Contexto de la aplicación
    private Context contexto;

    ////////////////////
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private DatabaseReference mDatabase;

    private Usuario miUsuario;

    //Fragments
    Fragment fInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ////////////////////////////////////////////
        Log.d(KEY_CICLE, "onCreate-ACT");
        savedInstanceStateApp = savedInstanceState;

        contexto = this;

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("usuarios");
        /*if (user != null) {
            // User is signed in
            //logeo activo, se envia a pantalla principal
            /*Intent intent = new Intent(IniciarSesionActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();* /
            mDatabase.child(user.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            miUsuario = dataSnapshot.getValue(Usuario.class);
                            // ...
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });


        } else {
            // No user is signed in
        }*/
        /*getResources()
                .getDrawable(R.drawable.profile5)*/

        miUsuario = new Usuario(GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.APELLIDOS),
                GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.NOMBRES),
                GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CORREO),
                GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CLAVE),
                GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.TELEFONO),
                GestorDatabase.getInstance(contexto).obtenerValorUsuario(ScriptDatabaseUsuario.Column.IMAGEN));

        IProfile profile = null;

        if(miUsuario.imagen.compareTo("---")==0 || miUsuario.imagen.isEmpty()){
            profile = new ProfileDrawerItem()
                    .withName(miUsuario.nombres+" ,"+miUsuario.apellidos)
                    .withEmail(miUsuario.correo)
                    .withIcon(R.drawable.profile3);
        }else{
            // Create a few sample profile
            // NOTE you have to define the loader logic too. See the CustomApplication for more details
            profile = new ProfileDrawerItem()
                    .withName(miUsuario.nombres+" ,"+miUsuario.apellidos)
                    .withEmail(miUsuario.correo)
                    .withIcon(Uri.parse(miUsuario.imagen));
        }



        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
            .withActivity(this)
            .withCompactStyle(false)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                    profile,
                    new ProfileSettingDrawerItem()
                            .withName(getResources().getString(R.string.txt_drawer_perfil_config))
                            .withIcon(GoogleMaterial.Icon.gmd_settings)
                    .withIdentifier(800),
                    new ProfileSettingDrawerItem()
                            .withName(getResources().getString(R.string.txt_drawer_perfil_cerrar_sesion))
                            .withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                    .withIdentifier(900)
            )

            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        if(profile.getIdentifier()==900){
                            //cerrar sesion de la app
                            mAuth.signOut();
                            Toast.makeText(HomeActivity.this, "Cerrando sesion de usuario...!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(profile.getIdentifier()==800){
                            Toast.makeText(HomeActivity.this, "Administrar Perfil",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, EditarProfileActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                        return false;
                    }
                })
            .withSavedInstance(savedInstanceState)
            .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_home).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(10).withSelectable(true),
                        new SectionDrawerItem().withName("Seccion"),
                        new PrimaryDrawerItem().withName("Item A").withIcon(GoogleMaterial.Icon.gmd_account_circle).withIdentifier(100).withSelectable(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_config).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(930).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_ayuda).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(930).withSelectable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem
                        if (drawerItem != null && drawerItem instanceof Nameable) {

                            if (drawerItem != null) {
                                Intent intent = null;
                                if (drawerItem.getIdentifier() == 10) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_home);
                                    if(savedInstanceStateApp==null){
                                        //primera vez que carga la activity
                                        fInicio = InicioFragment.newInstance("valor1","valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container,fInicio,"TAGINICIO").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentInicio - ACT");
                                    }else{
                                        //ya cargo anteriormente la activity, existe un estado
                                        fInicio = (InicioFragment)getSupportFragmentManager().findFragmentByTag("TAGINICIO");

                                        if(fInicio==null){
                                            fInicio = InicioFragment.newInstance("valor1","valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fInicio, "TAGINICIO").commit();
                                            Log.d(KEY_CICLE, "new FragmentInicio - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }
                                }

                                if (intent != null) {

                                }
                            }
                        }



                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withFireOnInitialOnClick(true)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(10, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
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

}
