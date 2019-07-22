package id.co.blogspot.diansano.appcopydbfromassets;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    TextView tvAnggota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAnggota = findViewById(R.id.tv_anggota);

        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();
                // --copy db dari folder asset ke folder database
                copyDB(getBaseContext().getAssets().open("anggota.db"), new FileOutputStream(destPath + "/anggota.db"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getContact(1);
        if (c.moveToFirst()) {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();
    }

    public void DisplayContact(Cursor c) {
       /* Toast.makeText(this, "id: " + c.getString(0) + "\n" + "Name: " +
                c.getString(1) + "\n" + "Alamat: " + c.getString(2),
        Toast.LENGTH_SHORT).show();*/
       tvAnggota.setText("id: " + c.getString(0) + "\n" + "Nama: " +
               c.getString(1) + "\n" + "Alamat: " + c.getString(2));
    }

    private void copyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //--copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }
}
