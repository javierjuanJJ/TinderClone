package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends ParentActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener, OnCompleteListener<AuthResult> {

    private Button mRegister;
    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setUI();
    }

    private void setUI() {
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = this;


        mRegister = findViewById(R.id.register);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mRegister.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, this);
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(!task.isSuccessful()){
            makeToast("sign up error");
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            GoToScreen(RegistrationActivity.this, MainActivity.class);
        }
    }
}