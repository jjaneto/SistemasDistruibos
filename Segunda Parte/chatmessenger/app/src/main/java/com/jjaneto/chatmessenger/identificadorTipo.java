package com.jjaneto.chatmessenger;

/**
 * Created by jjaneto on 06/10/2017.
 */

public class identificadorTipo {

    private boolean ehGrupo;
    private String nome;


    public identificadorTipo(boolean ehGrupo, String nome){
        this.ehGrupo = ehGrupo;
        this.nome = nome;
    }

    @Override
    public String toString(){
        if(ehGrupo){
            return "Grupo: " + nome;
        }else{
            return "User: "  + nome;
        }
    }

    public boolean getEhGrupo(){
        return ehGrupo;
    }

    public String nome(){
        return this.nome;
    }
}
