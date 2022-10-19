package whatsappclone.proyecto_javier_juan_uceda.tinderclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends ParentActivity implements View.OnClickListener, ValueEventListener, OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot> {

    private EditText mNameField, mPhoneField;

    private Button mBack, mConfirm;

    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;

    private String userId, name, phone, profileImageUrl;

    private Uri resultUri;
    private String userSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUI();
    }

    private void setUI() {
        userSex = getIntent().getExtras().getString("userSex");

        mNameField = findViewById(R.id.name);
        mPhoneField = findViewById(R.id.phone);

        mProfileImage = findViewById(R.id.profileImage);

        mBack = findViewById(R.id.back);
        mConfirm = findViewById(R.id.confirm);


        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mProfileImage.setOnClickListener(this);

        userId = mAuth.getCurrentUser().getUid();

        Log.i("user", "User " + userSex + " " + userId);

        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(userId);

        getUserInfo();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profileImage:

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

                break;
            case R.id.confirm:
                saveUserInformation();
                break;

            case R.id.back:
                finish();
                break;
        }
    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(this);

    }

    private void saveUserInformation() {
        name = mNameField.getText().toString();
        phone = mPhoneField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        mCustomerDatabase.updateChildren(userInfo);
        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(this);
            uploadTask.addOnSuccessListener(this);
        }else{
            finish();
        }
    }

    /**
     * This method will be called with a snapshot of the data at this location. It will also be called
     * each time that data changes.
     *
     * @param snapshot The current data at the location
     */
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists() && snapshot.getChildrenCount()>0){
            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
            if(map.get("name")!=null){
                name = map.get("name").toString();
                mNameField.setText(name);
            }
            if(map.get("phone")!=null){
                phone = map.get("phone").toString();
                mPhoneField.setText(phone);
            }
            Glide.with(getApplication()).clear(mProfileImage);
            if(map.get("profileImageUrl")!=null){
                profileImageUrl = map.get("profileImageUrl").toString();
                switch(profileImageUrl){
                    case "default":
                        Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                        break;
                    default:
                        Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        break;
                }
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

    @Override
    public void onFailure(@NonNull Exception e) {
        finish();
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
        while (!urlTask.isSuccessful());

        Uri downloadUrl = urlTask.getResult();

        Map userInfo = new HashMap();
        userInfo.put("profileImageUrl", downloadUrl.toString());
        mCustomerDatabase.updateChildren(userInfo);

        finish();
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }
}