package deep.myappcompany.samplebrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    myDbHandlerBookmarks dbHandlerBook = new myDbHandlerBookmarks(this,null,null,1);
    WebView mywebview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final List<String> books = dbHandlerBook.databaseTOString();
        Collections.reverse(books);
        if(books.size()>0){
            ArrayAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, books);
            ListView mylist = (ListView) findViewById(R.id.listViewBookmark);
            mylist.setAdapter(myAdapter);

            mylist.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String url = books.get(i);
                            Intent intent = new Intent(view.getContext(),commonActivity.class);
                            intent.putExtra("urls",url);

                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }



    }
}