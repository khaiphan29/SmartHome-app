package dashboard.iot.bku.roomcontrol;

/**
 * Created by khaiphan on 12/05/2022.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText loginText, passwordText, repasswordText;
    Button btnRegister, btnReturn;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginText = findViewById(R.id.reg_user_edit);
        passwordText = findViewById(R.id.reg_password_edit);
        repasswordText = findViewById(R.id.reg_repassword_edit);
        btnRegister = findViewById(R.id.register_confirm_button);
        btnReturn = findViewById(R.id.return_to_login_button);
        db = new DBHandler(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = loginText.getText().toString();
                String passText = passwordText.getText().toString();
                String repassText = repasswordText.getText().toString();

                if (userText.equals("") || passText.equals("") || repassText.equals(""))
                    Toast.makeText(RegisterActivity.this, "Username and password cannot be blank", Toast.LENGTH_SHORT).show();
                else {
                    if (passText.equals(repassText)) {
                        Boolean checkuser = db.checkusername(userText);
                        if (checkuser == false) {
                            Boolean insert = db.insertUserData(userText, passText);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), RoomService.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
}
}
