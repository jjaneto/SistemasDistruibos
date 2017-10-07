package com.jjaneto.chatmessenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.ArrayList;

/**
 * Created by jjaneto on 05/10/2017.
 */

public class chat_grupo extends AppCompatActivity {

    ListView lv;
    ArrayList<mensagem> listaDeMensagem;
    ArrayList<mensagem> arrMensagens;
    Button btSend;
    private Toolbar toolbar;
    private TextView tv;
    private EditText editMessage;
    private ArrayAdapter<mensagem> adapter;

    private String grupo;
    private String usuario;

    private Button btsuperior;

    public classGrupo grupo_classe;

    CharSequence options[] = new CharSequence[]{"Adicionar Usuario", "Remover Usuario", "Deletar Grupo"};

    private void associa() {
        //
        btsuperior = (Button) findViewById(R.id.btsuperior_chat_tela);
        btsuperior.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                createAlert();
            }
        });
        //
        toolbar = (Toolbar) findViewById(R.id.toolbar_chattela);
        tv = (TextView) toolbar.findViewById(R.id.nome_inside_toolbar);
        tv.setText(grupo);
        lv = (ListView) findViewById(R.id.list_chat_tela);
        btSend = (Button) findViewById(R.id.btnSend);
        editMessage = (EditText) findViewById(R.id.txt);

        //
        receiveMessage();
        grupo_classe = new classGrupo();
        grupo_classe.createGroup(usuario, grupo);
        //
        arrMensagens = new ArrayList<mensagem>();

        adapter = new ArrayAdapter<mensagem>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                arrMensagens);

        lv.setAdapter(adapter);

        btSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editMessage.getText().toString().isEmpty()) {
                    String ss = editMessage.getText().toString();
                    mensagem msgproto = new mensagem(usuario, grupo, ss);

                    arrMensagens.add(msgproto);

                    adapter.notifyDataSetChanged();
                    editMessage.setText("");
                    System.err.println("Mandando mensagem pro grupo " + grupo + " de " + usuario);
                    System.err.println("Mensagem: " + ss);
                    grupo_classe.sendMessageToGroup(usuario, grupo, ss);
                }
            }
        });


    }

    private void createAlert() {
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        bd.setTitle("Selecione a opção");
        bd.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        getUsuarioAlvo(1);

                        break;
                    case 1:
                        getUsuarioAlvo(2);
                        break;
                    case 2:
                        grupo_classe.deleteGroup(grupo);
                        setResult(0);
                        finish();
                        break;
                }
                Toast.makeText(getApplicationContext(), "cliquei na opcao " + which, Toast.LENGTH_LONG).show();
            }
        });
        bd.show();
    }

    public void getUsuarioAlvo(final int op){
        final AlertDialog.Builder bd = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        bd.setTitle("Digite o usuário alvo");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        bd.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which){
                        String ss = input.getText().toString();
                        if(op == 1){
                            grupo_classe.addUserToGroup(ss, grupo);
                        }else{
                            grupo_classe.deleteUser(ss, grupo);
                        }

                    }
                });

        bd.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

        bd.setView(input);
        bd.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_grupo_tela);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            grupo = extra.get("grupo").toString();
            usuario = extra.get("usuario").toString();
            Toast.makeText(this, "Chat com " + grupo, Toast.LENGTH_LONG).show();
        }

        associa();



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(chat_grupo.this, listuser.class));
//        moveTaskToBack(true);  // "Hide" your current Activity
    }


    public void receiveMessage() {
        final classReceive whatsnew = new classReceive(usuario);
        whatsnew.start();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    if(classReceive.sinal == true){
                        atualizaListView(whatsnew.getMensagem());
                        classReceive.sinal = false;
                    }
                }
            }
        }.start();
    }


    public void atualizaListView(final mensagem received) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(received);
            }
        });
    }

}
