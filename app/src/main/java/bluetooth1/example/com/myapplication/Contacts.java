package bluetooth1.example.com.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.database.Cursor;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

public class Contacts extends AppCompatActivity {
    public final int RESULT_PICK_CONTACT = 1;
    SQLiteDatabase db;
    TextView tv;
    String callnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        tv = (TextView) findViewById(R.id.textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        settextview();
        setSupportActionBar(toolbar);
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String number = "tel:" + callnumber.toString();
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse(number.toString()));
                    try {
                        startActivity(phoneIntent);
                        Toast.makeText(Contacts.this, "Calling", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Contacts.this, "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
    }
    public void settextview(){
        try {
            db = this.openOrCreateDatabase("aa", SQLiteDatabase.OPEN_READWRITE, null);
            tv.setText("");

            Cursor cursname = db.rawQuery("select name from contact_numbers;", null);
            Cursor cursnumber = db.rawQuery("select number from contact_numbers;", null);
            if (cursname.moveToFirst()) {
                tv.append(cursname.getString(0).toString());
                tv.append("\n");
            }
            if (cursnumber.moveToFirst()) {
                callnumber=cursnumber.getString(0).toString();
                tv.append(cursnumber.getString(0).toString());
            }
        }
        catch(Exception e){

        }
    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case RESULT_PICK_CONTACT:
                        contactPicked(data);
                        break;
                }

            } else {
                Log.e("MainActivity", "Failed to pick contact");
            }
        }


    private void contactPicked(Intent data) {
            Cursor cursor = null;
            try {
                String phoneNo = null;
                String name = null;
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();

                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);
                databaseconnection.context = getApplicationContext();
                databaseconnection.init();
                String sql = "select * from contact_numbers where number='"+phoneNo+"'";
                Cursor curso = databaseconnection.getData(sql);
                if(curso.moveToNext()){
                    databaseconnection.close();
                    Toast.makeText(getApplicationContext(),"Contact already added!!!", Toast.LENGTH_LONG).show();
                    settextview();
                } else {
                    databaseconnection.delete();
                    sql = "insert into contact_numbers values('" + name + "','" + phoneNo + "')";
                    databaseconnection.putData(sql);
                    settextview();
                    databaseconnection.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}


