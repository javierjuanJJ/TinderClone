package whatsappclone.proyecto_javier_juan_uceda.tinderclone.Matches;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import whatsappclone.proyecto_javier_juan_uceda.tinderclone.ParentActivity;
import whatsappclone.proyecto_javier_juan_uceda.tinderclone.R;

public class MatchesActivity extends ParentActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        setUI();
    }

    private void setUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);


        for (int i=0;i<100;i++){
            MatchesObject obj = new MatchesObject(Integer.toString(i));
            resultsMatches.add(obj);
        }

        mMatchesAdapter.notifyDataSetChanged();
    }

    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }

}