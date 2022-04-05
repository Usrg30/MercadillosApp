package com.example.mercadillosmadridmapa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mercadillosmadridmapa.databinding.ActivityMapsBinding;

import com.example.mercadillosmadridmapa.dto.Mercadillos;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Mercadillos>, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private String distrito;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView nresultados;

    private Spinner spinner;
    private String[] opciones = {"ARGANZUELA", "BARAJAS", "CARABANCHEL", "CENTRO", "CHAMARTIN", "CHAMBERI",
            "CIUDAD LINEAL",
            "FUENCARRAL-EL PARDO",
            "HORTALEZA",
            "LATINA",
            "MONCLOA-ARAVACA",
            "MORATALAZ",
            "PUENTE DE VALLECAS",
            "RETIRO",
            "SALAMANCA",
            "SAN BLAS-CANILLEJAS",
            "USERA","VICALVARO",
            "VILLA DE VALLECAS","VILLAVERDE"};




    public final static String ETIQUETA_LOG = "AppEjemplos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        this.progressBar = findViewById(R.id.pbc);
        this.recyclerView = findViewById(R.id.rvc);
        this.nresultados = findViewById(R.id.nresultados);

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
       ((ArrayAdapter)spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner = findViewById(R.id.spinnerv);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

      //  for mercadillo en mercadillos {
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       // }

    }





    @NonNull
    @Override
    public Loader<Mercadillos> onCreateLoader(int id, @Nullable Bundle args) {

        BuscarMercadillos buscarMercadillos = null;

        buscarMercadillos = new BuscarMercadillos(this /*,distro*/);


        return buscarMercadillos;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLoadFinished(@NonNull Loader<Mercadillos> loader, Mercadillos mercadillos) {

    //   Log.d(MainActivity.ETIQUETA_LOG, "onLoadFinished");
        //Log.d(MainActivity.ETIQUETA_LOG, "Listado recibido eventos= " + rc.toString());
        StringBuilder stringBuilder = new StringBuilder();

        mercadillos.getlista_mercadillos().forEach(e -> {
            Log.d(ETIQUETA_LOG, e.getTitle());
            stringBuilder.append(e.getTitle()+"\n");

        });

        this.progressBar.setVisibility(View.INVISIBLE);

        // return results
        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Mercadillos> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      //  Log.d(MainActivity.ETIQUETA_LOG, "SÍ HAY INTERNET");

        // llamar api with filtros

        this.progressBar.setVisibility(View.VISIBLE);

      //  Log.d(MainActivity.ETIQUETA_LOG, "Opción nueva seleccionada en el spinner");
        TextView textView = (TextView)view;
        //      distrito = textView.getText().toString();
     //   Log.d(MainActivity.ETIQUETA_LOG, "Opción tocada " + textView.getText().toString());
        if (RedUtil.hayInternet(this)) {

            //CON AsyncTaskLoader
            LoaderManager lm = LoaderManager.getInstance(this);
            lm.initLoader(37, null, this);

        } else {
      //      Log.d(MainActivity.ETIQUETA_LOG, "NO HAY INTERNET");
            Toast.makeText(this, "SIN CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //SERÍA INVOCADA CUANDO CAMBIA EL ADAPTER Y UNA OPCIÓN SELECCIONADA DEJA DE ESTAR DISPONIBLE
    }
}