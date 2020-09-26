package com.vk.custommall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName,InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InputName =(EditText)findViewById(R.id.register_name_input);
        InputPassword=(EditText)findViewById(R.id.register_password_input);
        InputPhoneNumber=(EditText)findViewById(R.id.register_phone_number_input);
        CreateAccountButton=(Button)findViewById(R.id.register_btn);

        loadingBar= new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }



    private void createAccount()
    {
        String name=InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Write Your Name..", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Write Your Phone Number..", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Write Your Password..", Toast.LENGTH_SHORT).show();
        }
       else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait ,while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);


            ValidatePhoneNumber(name,phone,password);
            loadingBar.show();
        }
    }




   public static void addAdmin()
   {
       final DatabaseReference RootRef;
       RootRef = FirebaseDatabase.getInstance().getReference();
       HashMap<String,Object>  userdataMap =new HashMap<>();
       userdataMap.put("phone","9826940169");
       userdataMap.put("name","Vikash Yadav");
       userdataMap.put("password","newadmin");
               RootRef.child("Admins").child("9826940169").updateChildren(userdataMap);
   }












    private void ValidatePhoneNumber(final String name, final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object>  userdataMap =new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                           if(task.isSuccessful())
                           {

                               Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                               startActivity(intent);
                             //  Toast.makeText(RegisterActivity.this, "Congratulations ! Your Account Created Successfully ", Toast.LENGTH_SHORT).show();

                           }
                           else
                           {
                               loadingBar.dismiss();
                               Toast.makeText(RegisterActivity.this, "Network error please try again", Toast.LENGTH_SHORT).show();

                           }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "The no."+phone+"already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try Again Using another number", Toast.LENGTH_SHORT).show();

                    Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}