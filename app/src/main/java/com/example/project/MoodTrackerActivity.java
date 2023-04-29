package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
public class MoodTrackerActivity extends AppCompatActivity {
    private static int curmoodid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView title;
    TextView subtitle;
    TextView quote;
    ListView listView;
    Button addmood;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> mood = new ArrayList<>();
    ArrayList<Integer> moodid = new ArrayList<>();
    public static int getmoodid(){
        return curmoodid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        title = findViewById(R.id.title);
        quote = findViewById(R.id.titleqoute);
        listView = findViewById(R.id.moodlist);
        addmood = findViewById(R.id.addmood);

        //make sure to step up for current user
        String user = FirebaseAuth.getInstance().getUid();



        addmood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),AddMood.class);
                startActivity(i);
            }
        });

        db.collection("mood").orderBy("id", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot docsnapshot,
                                @Nullable FirebaseFirestoreException e) {
                date.clear();

                for (DocumentSnapshot snapshot : docsnapshot) {

                    if (e != null) {
                        Log.w("Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        date.add(snapshot.getString("date"));
                        desc.add(snapshot.getString("description"));
                        mood.add(snapshot.getString("moodtype"));
                        moodid.add(Integer.valueOf(snapshot.get("id").toString()));
                        Log.d("", "Current data: " + snapshot.getData());

                    } else {
                        Log.d("", "Current data: null");
                    }

                }
                if(moodid.size()!=0) {
                    curmoodid = moodid.get(0);
                }
                else{
                    curmoodid =0;
                }

                Adapter adapter = new Adapter(getApplicationContext(), date, desc, mood);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }

        });



    }

    class Adapter extends ArrayAdapter<String> {

        ArrayList<String> date;
        ArrayList<String> desc;
        ArrayList<String> mood;
        Context context;

        Adapter(Context c, ArrayList<String> date, ArrayList<String> desc, ArrayList<String> mood) {
            super(c, R.layout.moodlog, R.id.date, date);
            this.context = c;
            this.date = date;
            this.desc = desc;
            this.mood = mood;
        }


        @NonNull
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layout = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layout.inflate(R.layout.moodlog, parent, false);
            TextView vdate = row.findViewById(R.id.date);
            TextView vdesc = row.findViewById(R.id.description);
            ImageView vmood = row.findViewById(R.id.emoji);

            vdate.setText(date.get(position));
            vdesc.setText(desc.get(position));

            if (mood.get(position) != null) {

                if (mood.get(position).equalsIgnoreCase("sad")) {
                    vmood.setImageResource(R.drawable.sad);
                }
                if (mood.get(position).equalsIgnoreCase("happy")) {
                    vmood.setImageResource(R.drawable.happy);
                }

                if (mood.get(position).equalsIgnoreCase("angry")) {
                    vmood.setImageResource(R.drawable.angry);
                }

            }


            return row;
        }
    }
}