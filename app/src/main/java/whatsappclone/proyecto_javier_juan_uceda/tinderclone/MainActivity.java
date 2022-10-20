package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import whatsappclone.proyecto_javier_juan_uceda.tinderclone.Cards.Cards;
import whatsappclone.proyecto_javier_juan_uceda.tinderclone.Cards.arrayAdapter;
import whatsappclone.proyecto_javier_juan_uceda.tinderclone.Matches.MatchesActivity;

public class MainActivity extends ParentActivity implements View.OnClickListener, SwipeFlingAdapterView.OnItemClickListener, ValueEventListener {
    private Cards[] Cards_data;
    private whatsappclone.proyecto_javier_juan_uceda.tinderclone.Cards.arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    private String userSex;
    private String oppositeUserSex;
    private Button logOut, goToSettings, goToMatches;
    SwipeFlingAdapterView flingContainer;
    private String currentUId;

    ListView listView;
    List<Cards> rowItems;

    private DatabaseReference usersDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();





        rowItems = new ArrayList<Cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                makeToast("Left");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                makeToast("Right");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(this);

        checkUserSex();

    }

    public void goToMatches() {
        GoToScreen(MatchesActivity.class);
    }

    private void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(this);

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(this);

    }

    private void setUI() {
        logOut = findViewById(R.id.logOut);
        goToSettings = findViewById(R.id.goToSettings);
        goToMatches = findViewById(R.id.goToMatches);

        logOut.setOnClickListener(this);
        goToSettings.setOnClickListener(this);
        goToMatches.setOnClickListener(this);
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    makeToast("new Connection");
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getOppositeSexUsers(){
        DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserSex);
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)){
                    String profileImageUrl = "default";
                    if (dataSnapshot.child("profileImageUrl").getValue() != null && !dataSnapshot.child("profileImageUrl").getValue().equals("default")){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), profileImageUrl);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void logoutUser() {
        mAuth.signOut();
        GoToScreen(ChooseLoginRegistrationActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logOut: logoutUser();break;
            case R.id.goToSettings: goToSettings();break;
            case R.id.goToMatches: goToMatches();break;
        }
    }

    private void goToSettings() {
        GoToScreen(SettingsActivity.class);
    }

    @Override
    public void onItemClicked(int i, Object o) {
        makeToast("Item Clicked");
    }

    /**
     * This method will be called with a snapshot of the data at this location. It will also be called
     * each time that data changes.
     *
     * @param snapshot The current data at the location
     */
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()){
            if (snapshot.child("sex").getValue() != null){
                userSex = snapshot.child("sex").getValue().toString();
                switch (userSex){
                    case "Male":
                        oppositeUserSex = "Female";
                        break;
                    case "Female":
                        oppositeUserSex = "Male";
                        break;
                }
                getOppositeSexUsers();
            }
        }
    }

    /**
     * This method will be triggered in the event that this listener either failed at the server, or
     * is removed as a result of the security and Firebase Database rules. For more information on
     * securing your data, see: <a
     * href="https://firebase.google.com/docs/database/security/quickstart" target="_blank"> Security
     * Quickstart</a>
     *
     * @param error A description of the error that occurred
     */
    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}