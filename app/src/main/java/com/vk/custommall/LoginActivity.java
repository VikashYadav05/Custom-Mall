package com.vk.custommall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.vk.custommall.Model.Users;
import com.vk.custommall.Prevalent.Prevalent;

import io.paperdb.Paper;

import static com.vk.custommall.RegisterActivity.addAdmin;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber ,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    public String parentDbName="Users";

    private TextView adminLink;
    private TextView notAdminLink;

  private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        InputPhoneNumber =(EditText)findViewById(R.id.login_phone_number_input);
        InputPassword=(EditText)findViewById(R.id.login_password_input);
        LoginButton=(Button)findViewById(R.id.login_btn);

        adminLink=(TextView)findViewById(R.id.admin_pannel_link);
        notAdminLink=(TextView)findViewById(R.id.not_admin_pannel_link);

        loadingBar=new ProgressDialog(this);

        chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_chkb);

        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Admin Login");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });


    }

    private void loginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Write Your Phone Number..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Write Your Password..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait , while we are checking the credintials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone ,password);

        }
    }


    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData =snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Users"))
                           {
                            Prevalent.currentOnlineUser=usersData;
                            Toast.makeText(LoginActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            intent.putExtra("passname",Prevalent.currentOnlineUser.getName());
                            startActivity(intent);


                           }
                          else if(parentDbName.equals("Admins"))
                           {
                               Toast.makeText(LoginActivity.this, "Welcome Admin you are logged in successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();
                               Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                               startActivity(intent);
                           }

                        }

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Create new account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}