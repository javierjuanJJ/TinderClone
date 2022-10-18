package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseLoginRegistrationActivity extends ParentActivity implements View.OnClickListener {

    private Button mLogin, mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration);

        setUI();
    }

    private void setUI() {
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        mLogin.setOnClickListener(this);

        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                GoToScreen(LoginActivity.class);
                break;
            case R.id.register:
                GoToScreen(RegistrationActivity.class);
                break;
        }
    }
}