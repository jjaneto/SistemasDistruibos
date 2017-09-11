package SD;

import java.util.Scanner;

/**
 *
 * @author jjaneto
 */
public class MainClass {
    
    private String nameUser;
    private String whoReceives;
    private boolean ehGrupo;
    private Send ssend;
    private Receive rreceive;
    private Scanner sc;
    private Group grupo;

    public MainClass() {
//        this.nameUser = nameUser;
        whoReceives = new String();
        nameUser = new String();
        ehGrupo = false;
    }
    
    public boolean ehGrupo(){
        return ehGrupo;
    }
    
    public String getUser(){
        return nameUser;
    }
    
    public void setUser(String nameUser){
        this.nameUser = nameUser;
    }
    
    public String getReceptor(){
        return whoReceives;
    }
    
    public void setReceptor(String whoReceives){
        this.whoReceives = whoReceives;
    }
    
    public boolean ehComando(String w){
        switch(w.charAt(0)){
            case '@':
                setReceptor(w.substring(1, w.length()));
                ehGrupo = false;
                return true;
            case '#':
                setReceptor(w.substring(1, w.length()));
                return ehGrupo = true;
            case '!':
                String subs = w.substring(1, w.length());
                String[] tokens = subs.split(" ");
                if(subs.equals("addUser")){
                    grupo.addUserToGroup(tokens[1], tokens[2]);
                }else if(subs.equals("delUser")){
                    grupo.deleteUser(tokens[1], tokens[2]);
                }else if(subs.equals("addGroup")){
                    grupo.createGroup(tokens[1]);
                }else if(subs.equals("delGroup")){
                    grupo.deleteGroup(tokens[1]);
                }
                return true;                
            default:
                return false;
        }
    }
    
    public void enviaMenssagem(String w){
        if(getReceptor().isEmpty()){
            System.err.println("Você não pode mandar mensagem sem que um usuário ou um grupo a receba!");
        }else{
            if(!ehGrupo){
                new Send(getUser(), getReceptor(), w, "").start();
            }else grupo.sendMessageToGroup(getUser(), getReceptor(), w);
        }        
    }
    
    public void runChat(){
        sc = new Scanner(System.in);
        System.out.print("User: ");
        String w = sc.nextLine();
        setUser(w);
//        MainClass m = new MainClass(w);    
        while(true){
            if(getReceptor().isEmpty()){
                System.out.print(">> ");
            }else{
                if(ehGrupo()){
                    System.out.print(getReceptor() + "* >> ");
                }else{
                    System.out.print(getReceptor() + " >> ");
                }
            }
            w = sc.nextLine();
            if(!ehComando(w)){
                enviaMenssagem(w);
            }
        }
    }
    
    public static void main(String args[]){
        new MainClass().runChat();
    }
    
    
}
