package br.com.isacdeveloper.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.isacdeveloper.listadecompras.BDHelper.ProdutosBd;
import br.com.isacdeveloper.listadecompras.model.Produtos;

public class FormularioProdutos extends AppCompatActivity {

    TextView txtNomeProduto;
    EditText editText_NomeProd, editText_Descricao, editText_Quantidade;
    Button btn_poliform;
    Produtos editarProduto, produto;
    ProdutosBd bdHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_produtos);

        produto = new Produtos();
        bdHelper = new ProdutosBd(FormularioProdutos.this);

        Intent intent = getIntent();
        editarProduto = (Produtos) intent.getSerializableExtra("produto-escolhido");

        initFormulario();

        if (editarProduto != null) { //Se o produto for cadastrado, cria a estrutura do botão com setText "Modificar" mais as opções editar
            btn_poliform.setText("Modificar");

            txtNomeProduto.setText("Alterar Produto - " + editarProduto.getNomeProduto());
            editText_NomeProd.setText(editarProduto.getNomeProduto());
            editText_Descricao.setText(editarProduto.getDescricao());
            editText_Quantidade.setText(editarProduto.getQuantidade() + "");

            produto.setId(editarProduto.getId());

        } else { //Sendo falso recupera os campos e os botões da activity que foram definidos no XML e setText "Cadastrar" no Botão
            btn_poliform.setText("Cadastrar Novo Produto");
        }

        btn_poliform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_NomeProd.getText().toString().length() > 0 && !editText_NomeProd.getText().toString().equals("")) { //Cria a validação do botão para não retornar nulo(nullExceptionPointer)
                    if (editText_Descricao.getText().toString().length() > 0 && !editText_Descricao.getText().toString().equals("")) {
                        if (editText_Quantidade.getText().toString().length() > 0 && !editText_Quantidade.getText().toString().equals("")) {


                            produto.setNomeProduto(editText_NomeProd.getText().toString());
                            produto.setDescricao(editText_Descricao.getText().toString());
                            produto.setQuantidade(Integer.parseInt(editText_Quantidade.getText().toString()));


                            if (btn_poliform.getText().toString().equals("Cadastrar Novo Produto")) {
                                Toast.makeText(FormularioProdutos.this, "Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
                                bdHelper.salvarProduto((produto));
                                bdHelper.close();
                                voltarInicio();
                            } else {
                                Toast.makeText(FormularioProdutos.this, "Modificado com Sucesso!", Toast.LENGTH_LONG).show();
                                bdHelper.alterarProduto(produto);
                                bdHelper.close();
                                voltarInicio();
                            }
                        } else {
                            Toast.makeText(FormularioProdutos.this, "Preencha a Quantidade!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(FormularioProdutos.this, "Preencha a Descrição do Produto!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FormularioProdutos.this, "Preencha o Nome!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void initFormulario() {
        txtNomeProduto = findViewById(R.id.txtNomeProduto);
        editText_NomeProd = findViewById(R.id.editText_NomeProduto);
        editText_Descricao = findViewById(R.id.editText_Descricao);
        editText_Quantidade = findViewById(R.id.editText_Quantidade);

        btn_poliform = findViewById(R.id.btn_poliform); //poliform = polimorfismo ou sofre alterações dependendo da condição/situação
    }

    public void voltarInicio() {
        Intent intent = new Intent(FormularioProdutos.this, MainActivity.class);
        startActivity(intent);
        finish();
        limpaActivity();
    }

    public void limpaActivity() { //Deleta as activities criadas posteriormente, eliminando a pilha
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }


}
