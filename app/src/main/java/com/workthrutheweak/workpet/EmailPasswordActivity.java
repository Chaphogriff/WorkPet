package com.workthrutheweak.workpet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workthrutheweak.workpet.databinding.ActivityEmailpasswordBinding;
import com.workthrutheweak.workpet.notification.ReminderBroadcast;

import java.util.HashMap;
import java.util.Map;

public class EmailPasswordActivity extends AppCompatActivity {

    private ActivityEmailpasswordBinding binding;
    private static final String TAG = "Email&Password";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERGOLD = "gold";
    private static final String KEY_USERXP = "xp";
    private static final String KEY_USERLVL = "lvl";


    private AlertDialog dialog;
    private AlertDialog.Builder logdialogbuilder;
    private EditText laddress;
    private EditText lpassword;
    private Button lbackbutton;
    private Button loginbutton;
    private TextView ltitle;
    private TextView lpass;
    private TextView lemail;


    private AlertDialog.Builder signdialogbuilder;
    private EditText saddress;
    private EditText spassword;
    private Button sbackbutton;
    private Button signupbutton;
    private TextView stitle;
    private TextView spass;
    private TextView semail;
    private Button logbutton;
    private Button signButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);
        ReminderBroadcast.createNotificationChannel(this);

        //Setup View Binding variable
        binding = ActivityEmailpasswordBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        mAuth = FirebaseAuth.getInstance();

        logbutton = binding.logbutton;
        signButton = binding.accbutton;

        logbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createlogPopupDialog();
            }
        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createsignPopupDialog();
            }
        });
    }

    private void createlogPopupDialog() {
        logdialogbuilder = new AlertDialog.Builder(this);
        final View logPopupView = getLayoutInflater().inflate(R.layout.activity_logpopup, null);
        //dialog;
        ltitle = (TextView) logPopupView.findViewById(R.id.logtitle);
        lemail = (TextView) logPopupView.findViewById(R.id.logemail);
        lpass = (TextView) logPopupView.findViewById(R.id.logpass);
        laddress = (EditText) logPopupView.findViewById(R.id.editTextLogEmail);
        lpassword = (EditText) logPopupView.findViewById(R.id.editTextLogPassword);
        loginbutton = (Button) logPopupView.findViewById(R.id.loginbutton);
        lbackbutton = (Button) logPopupView.findViewById(R.id.logbackbutton);
        logdialogbuilder.setView(logPopupView);
        dialog = logdialogbuilder.create();
        dialog.show();

        lbackbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                signInWithEmailAndPassword(laddress.getText().toString().trim(), lpassword.getText().toString().trim());
            }
        });
    }

    private void createsignPopupDialog() {
        signdialogbuilder = new AlertDialog.Builder(this);
        final View signPopupView = getLayoutInflater().inflate(R.layout.activity_signpopup, null);
        //dialog;
        stitle = (TextView) signPopupView.findViewById(R.id.signtitle);
        semail = (TextView) signPopupView.findViewById(R.id.signemail);
        spass = (TextView) signPopupView.findViewById(R.id.signpass);
        saddress = (EditText) signPopupView.findViewById(R.id.editTextSignEmail);
        spassword = (EditText) signPopupView.findViewById(R.id.editTextSignPassword);
        signupbutton = (Button) signPopupView.findViewById(R.id.signupbutton);
        sbackbutton = (Button) signPopupView.findViewById(R.id.signbackbutton);
        signdialogbuilder.setView(signPopupView);
        dialog = signdialogbuilder.create();
        dialog.show();

        sbackbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createUserWithEmailAndPassword(saddress.getText().toString().trim(), spassword.getText().toString().trim());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {

        startActivity(new Intent(this, MainActivity.class));
    }

    void createUserWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirstLogUser(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void FirstLogUser(FirebaseUser user) {
        String email = user.getEmail();
        int usergold = 0;
        int userxp = 0;

        Map<String, Object> profil = new HashMap<>();
        profil.put(KEY_EMAIL, email);
        profil.put(KEY_USERGOLD, usergold);
        profil.put(KEY_USERXP, userxp);
        profil.put(KEY_USERLVL, userxp);

        db.collection("Users").document(user.getUid()).set(profil)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EmailPasswordActivity.this, "Profile loaded successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EmailPasswordActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
