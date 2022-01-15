package com.example.itemslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;
    EditText input;
    ImageView enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);

        items = new ArrayList<>();
        items.add("Apple");
        items.add("Apple 2");
        items.add("Apple 3");
        items.add("Apple 4");
        items.add("Apple 5");

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
    }

    Toast t;

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