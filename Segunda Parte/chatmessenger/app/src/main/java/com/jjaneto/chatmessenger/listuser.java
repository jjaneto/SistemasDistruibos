package com.jjaneto.chatmessenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class listuser extends AppCompatActivity implements View.OnClickListener{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private FloatingActionButton botaoemail;
    private ListView lv;
    private List<String> lista;

    private void associa(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.lista2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        lista = new ArrayList<String>(Arrays.asList(new String("lixo")));
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                lista);
//
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent inta = new Intent(listuser.this, chat.class);
                inta.putExtra("receptor", lista.get(i));

                startActivity(inta);
//                startActivity(new Intent(listuser.this, chat.class));
//                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listuser);

//        Snackbar.make(, "Bem vindo " + telaLogin.usuario, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Seila mano", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listuser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab){
            AlertDialog.Builder bd = new AlertDialog.Builder(this);

            bd.setTitle("Deseja enviar mensagem para grupo ou para um usuário?");
            bd.setPositiveButton("Usuário",
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            getReceiver(false);
                        }
                    });

            bd.setNegativeButton("Grupo",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            getReceiver(true);
                        }
                    });

            bd.show();
        }
    }

    public void getReceiver(final boolean ehGrupo){
        final AlertDialog.Builder bd = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        if(ehGrupo){
            bd.setTitle("Digite o nome do grupo");
        }else{
            bd.setTitle("Digite o nome do usuario");
        }
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        bd.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which){
                        String s = input.getText().toString();
                        criaUser(ehGrupo, s);
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

    public void criaUser(boolean ehGrupo, String who){
        lista.add(who);
    }
}
