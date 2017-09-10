package SD;

import java.util.Scanner;

/**
 *
 * @author jjaneto
 */
public class MainClass {
    
    private String nameUser;
    private String whoReceives;
    private boolean grupo;

    public MainClass(String nameUser) {
        this.nameUser = nameUser;
        grupo = false;
    }
    
    public boolean ehGrupo(){
        return grupo;
    }
    
    public String receptor(){
        return whoReceives;
    }
    
    public void runChat(){
        Scanner sc = new Scanner(System.in);
        System.out.print("User: ");
        String w = sc.nextLine();
        MainClass m = new MainClass(w);    
        while(true){
            if(ehGrupo()){
                System.out.print("");
            }else{
                
            }
        }
    }
    
    
}
