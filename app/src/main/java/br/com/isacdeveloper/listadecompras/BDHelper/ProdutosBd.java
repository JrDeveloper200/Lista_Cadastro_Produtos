package br.com.isacdeveloper.listadecompras.BDHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.isacdeveloper.listadecompras.model.Produtos;

public class ProdutosBd extends SQLiteOpenHelper {

    private static final String DATABASE = "bdprodutos";
    private static final int VERSION = 1;

    public ProdutosBd(Context context) { //Cria instacia no bd SQLite
        super(context, DATABASE, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String produto = "CREATE TABLE produtos(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nomeproduto TEXT NOT NULL, descricao TEXT NOT NULL, quantidade INTEGER);";
        sqLiteDatabase.execSQL(produto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String produto = "DROP TABLE IF EXISTS produtos";
        sqLiteDatabase.execSQL(produto);
    }


    //Método salvar produtos no BD
    public void salvarProduto(Produtos produto) {
        ContentValues dados = new ContentValues();

        dados.put("nomeproduto", produto.getNomeProduto());
        dados.put("descricao", produto.getDescricao());
        dados.put("quantidade", produto.getQuantidade());

        getWritableDatabase().insert("produtos", null, dados);
    }


    //Método Alterar produtos BD
    public void alterarProduto(Produtos produto) {
        ContentValues dados = new ContentValues();

        dados.put("nomeproduto", produto.getNomeProduto());
        dados.put("descricao", produto.getDescricao());
        dados.put("quantidade", produto.getQuantidade());

        String[] args = {produto.getId().toString()};
        getWritableDatabase().update("produtos", dados, "id=?", args);
    }


    //Deletar Produtos BD
    public void deletarProduto(Produtos produto) {

        String[] args = {produto.getId().toString()};
        getWritableDatabase().delete("produtos", "id=?", args);
    }


    //Método para Listar os Dados no BD
    public ArrayList<Produtos> getLista() {
        String[] columns = {"id", "nomeproduto", "descricao", "quantidade"};
        Cursor cursor = getWritableDatabase().query("produtos", columns, null, null, null, null, null, null);
        ArrayList<Produtos> produtos = new ArrayList<Produtos>();


        while (cursor.moveToNext()) {
            Produtos produto = new Produtos();
            produto.setId(cursor.getLong(0));
            produto.setNomeProduto(cursor.getString(1));
            produto.setDescricao(cursor.getString(2));
            produto.setQuantidade(cursor.getInt(3));

            produtos.add(produto);
        }
        return produtos;
    }
}
