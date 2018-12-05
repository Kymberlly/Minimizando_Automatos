package principal;

/**
 *
 * @author Kymberlly Melo e Hernani Junior
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Funcoes f = new Funcoes();
        
        f.insereEstados();
        f.estInalcan();
        f.completaAut();
        f.mostraMensagem();
        f.writeXML();
    }
    
}
