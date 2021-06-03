package com.example.retrofit2stub;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    String API_URL = "https://api.edamam.com/";
    String key = "a529ba1725b949bc5ca23426cc8a093c";
    String id = "ceabcb28";
    String diet = "balanced";
    String calories = "0 - 10000";
    TextView textViewSearch;
    TextView textViewCalMin;
    TextView textViewCalMax;
    ListView listView;
    ImageListAdapter imageAdapter;
    Spinner spinner;


    interface PixabayAPI {
        @GET("/search")
        Call<Response> search(@Query("q") String item, @Query("app_key") String key, @Query("app_id") String id, @Query("diet") String diet,
                              @Query("from") int start, @Query("to") int end, @Query("calories") String calories);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSearch = findViewById(R.id.textSearch);
        listView = findViewById(R.id.listView);
        textViewCalMin = findViewById(R.id.textCalMin);
        textViewCalMax = findViewById(R.id.textCalMax);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void startSearch(String item) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PixabayAPI api = retrofit.create(PixabayAPI.class);

        calories = textViewCalMin.getText().toString() + "-" + textViewCalMax.getText().toString();

        Call<Response> call = api.search(item, key, id, diet, 0, 9, calories);  // создали запрос

        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                Response r = response.body();
                displayResults(r.hits);
                Toast toast = Toast.makeText(getApplicationContext(), "Найдено: " + r.count + " рецептов", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ошибка: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        };
        call.enqueue(callback);
    }

    public void displayResults(Hit[] hits) {
        imageAdapter = new ImageListAdapter(getApplicationContext(),hits);
        listView.setAdapter(imageAdapter);
    }

    public void onSearchClick(View v) {
        diet = spinner.getSelectedItem().toString();
        startSearch(textViewSearch.getText().toString());
    }
}
