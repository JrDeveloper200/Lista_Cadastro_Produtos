package br.com.isacdeveloper.listadecompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import br.com.isacdeveloper.listadecompras.BDHelper.ProdutosBd;
import br.com.isacdeveloper.listadecompras.model.Produtos;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    Button btnCadastrar;
    ProdutosBd bdHelper;
    ArrayList<Produtos> listview_produtos;
    Produtos produto;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFormulario();

        btnCadastrar.setOnClickListener(new android.view.View.OnClickListener() {//Redireciona o usuario para a tela de formulario cadastro
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormularioProdutos.class);
                startActivity(intent);
            }
        });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {//Aqui cria-se o a intenção da tela alterar informações do cadastro
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Produtos produtoEscolhido = (Produtos) adapter.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, FormularioProdutos.class);
                i.putExtra("produto-escolhido", produtoEscolhido);
                startActivity(i);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                produto = (Produtos) adapter.getItemAtPosition(position);
                return false;
            }
        });
    }

    private void initFormulario() {

        bdHelper = new ProdutosBd(MainActivity.this);
        btnCadastrar = findViewById(R.id.btn_cadastrar);

        lista = findViewById(R.id.listview_produtos);
        registerForContextMenu(lista);

    }

    @Override //Cria a ação do menu de contexto ao pressionar o item por um momento
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuDelete = menu.add("Deletar este Produto");
        menuDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                bdHelper = new ProdutosBd(MainActivity.this);
                bdHelper.deletarProduto(produto);
                bdHelper.close();
                carregarProduto();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarProduto();
    }

    public void carregarProduto() { //Carrega o a lista dos produtos cadastrados no aplicativo
        bdHelper = new ProdutosBd(MainActivity.this);
        listview_produtos = bdHelper.getLista();
        bdHelper.close();

        if (listview_produtos != null) {
            adapter = new ArrayAdapter<Produtos>(MainActivity.this, android.R.layout.simple_list_item_1, listview_produtos);
            lista.setAdapter(adapter);
        }
    }

    @Override//Método destroy cache activity
    protected void onDestroy() {
        stopService(new Intent(this, FormularioProdutos.class));
        super.onDestroy();
    }
}
