package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends ParentActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener, OnCompleteListener<AuthResult>  {

    private Button mRegister;
    private EditText mEmail, mPassword, mName;
    private RadioGroup mRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private RadioButton radioButton;

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

        mName = findViewById(R.id.name);

        mRadioGroup = findViewById(R.id.radioGroup);

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

                int selectId = mRadioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(selectId);

                if(radioButton.getText() == null){
                    return;
                }

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, this);
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(!task.isSuccessful()){
            makeToast("sign up error ");

            if (task.getException() != null) {
                Log.e("errorRegistration",task.getException().getMessage() + " " + task.getException().toString());
            }


        }
        else {
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            Map userInfo = new HashMap<>();
            userInfo.put("name", mName.getText().toString());
            userInfo.put("sex", radioButton.getText().toString());
            userInfo.put("profileImageUrl", "default");
            currentUserDb.updateChildren(userInfo);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            GoToScreen(MainActivity.class);
        }

    }
}