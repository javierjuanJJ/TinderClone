package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ParentActivity extends AppCompatActivity {
    private void GoToScreen(Context screenFrom, Class<?> screenDestiny){
        Intent intent = new Intent(screenFrom, screenDestiny);
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
