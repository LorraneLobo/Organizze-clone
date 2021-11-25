package com.example.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.btnCadastrar);

        botaoCadastrar.setOnClickListener(v -> {
            String textoNome = campoNome.getText().toString();
            String textoEmail = campoEmail.getText().toString();
            String textoSenha = campoSenha.getText().toString();

            //Validar se os campos foram preenchidos
            if (!textoNome.isEmpty() && !textoEmail.isEmpty() && !textoSenha.isEmpty()){

                usuario = new Usuario();
                usuario.setNome(textoNome);
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                cadastrarUsuario();
            }else {
                Toast.makeText(CadastroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cadastrarUsuario() {

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(CadastroActivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {

                        String execao = "";
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e){
                            execao = "A senha deve conter pelo menos 6 caracteres";
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            execao = "Por favor, digite um e-mail válido!";
                        }catch (FirebaseAuthUserCollisionException e){
                            execao = "Essa conta já foi cadastrada!";
                        }catch (Exception e){
                            execao = "Erro ao cadastrar o usuário: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(CadastroActivity.this, execao, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}