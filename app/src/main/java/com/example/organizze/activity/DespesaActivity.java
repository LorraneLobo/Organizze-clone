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
import com.example.organizze.helper.MoneyTextWatcher;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DespesaActivity extends AppCompatActivity {

    Locale mLocale = new Locale("pt", "BR");

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private Double despesaTotal;
    private Double despesaAtualizada;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValorDespesa);

        campoValor.addTextChangedListener(new MoneyTextWatcher(campoValor, mLocale));

        //Exibe a data atual
        campoData.setText(DateCustom.dataAtual());
        recuperarDespesaTotal();

    }

    public void salvarDespesa(View v){
            if (validarCamposDespesa()){
            String data = campoData.getText().toString();

            String valorFormatado = campoValor.getText().toString()
                        .replaceAll("[%sR$\\s]", "")
                        .replace(".",",")
                        .replace(",",".");
            Double valorRecuperado = Double.parseDouble(valorFormatado);

            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");


            despesaAtualizada = despesaTotal + valorRecuperado ;
            atualizarDespeza(despesaAtualizada);

            movimentacao.salvar(data);

            finish();
        }
    }

    private boolean validarCamposDespesa() {
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(campoValor);
        editTextList.add(campoData);
        editTextList.add(campoCategoria);
        editTextList.add(campoDescricao);

        boolean isValid = true;

        for (EditText editText : editTextList) {
            if (editText.getText().toString().isEmpty()) {
                editText.setError("Preencha o campo");
                isValid = false;
            }
        }

        return isValid;
    }

    public void recuperarDespesaTotal(){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarDespeza(Double despesa){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}