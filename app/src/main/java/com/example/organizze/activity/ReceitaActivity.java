package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private Double receitaTotal;
    private Double receitaAtualizada;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoData = findViewById(R.id.editDataReceita);
        campoCategoria = findViewById(R.id.editCategoriaReceita);
        campoDescricao = findViewById(R.id.editDescricaoReceita);
        campoValor = findViewById(R.id.editValorReceita);

        //Exibe a data atual
        campoData.setText(DateCustom.dataAtual());
        recuperarReceitaTotal();
    }

    public void salvarReceita(View v){
        if (validarCamposReceita()){

            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");


            receitaAtualizada = receitaTotal + valorRecuperado ;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);

            finish();
        }
    }

    private boolean validarCamposReceita() {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty() && !textoData.isEmpty() && !textoCategoria.isEmpty() && !textoDescricao.isEmpty()) {
            return true;
        } else {
            Toast.makeText(ReceitaActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            if (textoValor.isEmpty()){
                campoValor.setError("Preencha o campo");
            }
            if (textoData.isEmpty()){
                campoData.setError("Preencha o campo");
            }
            if (textoCategoria.isEmpty()){
                campoCategoria.setError("Preencha o campo");
            }
            if (textoDescricao.isEmpty()){
                campoDescricao.setError("Preencha o campo");
            }
            return false;
        }
    }

    public void recuperarReceitaTotal(){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceita(Double receita){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}