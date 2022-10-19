package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class ParentActivity extends AppCompatActivity {

    private Intent getIntent(Context screenFrom, Class<?> screenDestiny){
        Intent intent = new Intent(screenFrom, screenDestiny);
        return intent;
    }

    private void GoToScreen(Context screenFrom, Class<?> screenDestiny){
        startActivity(getIntent(screenFrom, screenDestiny));
        finish();
    }

    public void GoToScreen(Intent intent){
        startActivity(intent);
        finish();
    }

    public void GoToScreen(Class<?> screenDestiny){
        GoToScreen(this, screenDestiny);
    }

    public void makeToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
