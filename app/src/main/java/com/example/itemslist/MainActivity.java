package com.example.itemslist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.android.material.navigation.NavigationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;
    EditText input;
    ImageView enter;
    Toast t;
    ActionBarDrawerToggle toggle;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    String sensorData;

    @SuppressLint({"ResourceType", "ServiceCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        toggle = new ActionBarDrawerToggle(this, findViewById(R.id.drawerLayout), R.string.open, R.string.close);
        toggle.syncState();

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exitButton:
                        makeToast("Exiting...");

                        sendNotification("We are still watching you...");

                        finish();

                        System.exit(0);

                        return true;
                    case R.id.text_notify:
                        sendNotification("Notification from nav");

                        return true;
                    case R.id.gyro_data:
                        makeToast(sensorData);
                    default:
                        return false;
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (gyroscopeSensor == null) {
            Toast.makeText(this, "No gyro for your scope", Toast.LENGTH_SHORT).show();
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                sensorData =
                    event.values[0] + " / " +
                    event.values[1] + " / " +
                    event.values[2]
                ;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        Bundle extras = getIntent().getExtras();
        String username;

        if (extras != null) {
            username = extras.getString("username");
        }

        listView = findViewById(R.id.listview);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);

        items = new ArrayList<>();

        /**
         * Delete item on click.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = items.get(position);

                makeToast(name);

            }
        });

        /**
         * Delete item on long click.
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                makeToast("Removed: " + items.get(position));

                removeItem(position);

                return false;
            }
        });

        adapter = new ListViewAdapter(getApplicationContext() ,items);
        listView.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();

                if (text == null || text.length() == 0) {
                    makeToast("Enter an item.");
                } else {
                    if (addItem(text)) {
                        input.setText("");

                        makeToast("Added :" + text);
                    }
                }
            }
        });

        loadContent();
    }

    /**
     * Load list content on app load.
     */
    public void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");

        byte[] content = new byte[(int) readFrom.length()];

        try {
            FileInputStream stream = new FileInputStream(readFrom);

            stream.read(content);

            String s = new String(content);

            s = s.substring(1, s.length() - 1);

            String split[] = s.split(", ");

            items = new ArrayList<>(Arrays.asList(split));

            adapter = new ListViewAdapter(this, items);

            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();

        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));

            writer.write(items.toString().getBytes());

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    /**
     * Display text in toast.
     *
     * @param s string text for toast.
     */
    private void makeToast(String s) {
        if (t != null) t.cancel();

        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);

        t.show();
    }

    /**
     * Send notification.
     *
     * @param msg String
     */
    public void sendNotification(String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "lemubitA")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(msg)
                .setContentText("Notification from Shopping List")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(101, builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(gyroscopeEventListener);
    }

    /**
     * Create Notification channel.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ShoppingListChannel";
            String description = "Channel for item list";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("lemubitA", name, importance);

            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Add item to list.
     *
     * @param item string
     * @return boolean
     */
    public static boolean addItem(String item) {
        try {
            items.add(item);

            adapter.notifyDataSetChanged();

            return true;
        } catch (Error e) {
//            makeToast("Error: " + e);
        }

        return false;
    }

    /**
     * Remove item
     *
     * @param remove int
     * @return boolean
     */
    public static boolean removeItem(int remove) {
        try {
            items.remove(remove);

            adapter.notifyDataSetChanged();

            return true;
        } catch (Error e) {
//            makeToast("Error: " + e);
        }

        return false;
    }
}