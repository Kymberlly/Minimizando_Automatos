package principal;

//import java.awt.List;
//import java.util.ArrayList;

/**
 *
 * @author Kym
 */
public class Automato implements Comparable<Automato>{
    
    private int est0;
    private int est1;
    private String simb;
     
    //criar lista de char's para salvar todos os char's do automato
    //e comparar com os faltantes
    private int ini;
    private int[]finais = new int[2];
    

    public Automato(){ }
    
    public Automato(int est0, int est1, String simb){
        this.est0 = est0;
        this.est1 = est1;
        this.simb = simb;
    }

    @Override
    public int compareTo(Automato aut){
        if(this.getEst0() > aut.getEst0()){
            return 1;
        }
        if(this.getEst0() < aut.getEst0()){
            return -1;
        }
        
        return 0;
    }
    
    
    public void setEst0(int est0){
        this.est0 = est0;
    }
    
    public void setEst1(int est1){
        this.est1 = est1;
    }
        
    public void setSimb(String simb){
        this.simb = simb;
    }
    
    public int getEst0(){
        return est0;
    }
    
    public int getEst1(){
        return est1;
    }
    
    public String getSimb(){
        return simb;
    }
    
    public int getIni(){
        return ini;
    }
    
    public int getFin(int i){
        int a = finais[i];
        return a;
    }
    
    
}
