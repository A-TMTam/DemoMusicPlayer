package com.example.demomusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView ListView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView = findViewById(R.id.ListViewSong);

        runtimePermisson();

    }

    public  void runtimePermisson(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findsong (File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for (File singlefile: files){
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                arrayList.addAll(findsong(singlefile));
            }
            else {
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                    arrayList.add(singlefile);
                }
            }
        }

        return arrayList;
    }

    void displaySongs(){
        final ArrayList<File> mySongs = findsong(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "")
                    .replace(".wav", "");
        }

        /*ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_expandable_list_item_1, items);
        ListView.setAdapter(myAdapter);*/

        customAdapter customAdapter = new customAdapter();
        ListView.setAdapter(customAdapter);
    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textSong = myView.findViewById(R.id.txtSongName);
            textSong.setSelected(true);
            textSong.setText(items[i]);

            return myView;
        }
    }
}
