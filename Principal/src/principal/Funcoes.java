package principal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Kymberlly Melo e Hernani Junior
 */
public class Funcoes{
    
    private ArrayList<Automato> aut;
    private ArrayList<State> states;
    private String[]alfabeto ;
    private Set<String> alfabetoA;
    private Map<String,int[]> IniciaisFinais;
    
    private int inicial;
    private int[] finais;
    
    /**
     * Insere todos os simbolos do automato
     * em um array responsavel pelo alfabeto
     */
    public void setAlfa(){
        alfabeto = new String[alfabetoA.size()];
        int i = 0;
        for(String str : alfabetoA){
            alfabeto[i] = str;
            i++;
        }
    }
    
    
    /**
     * seta a var ini com o ID do estado inicial
     * seta a var finais[] com os id's dos estados finais
     */
    public void setIniFin(){
        int [] iniciaisA = IniciaisFinais.get("INICIAL");
        int [] finaisA = IniciaisFinais.get("FINAL");
        
        int count=0;
        for(int i = 0;i < finaisA.length;i++){
            if(finaisA[i]!=-1){
                count ++;
            }    
        }
        
        finais = new int[count];
        for(int i = 0;i < finaisA.length;i++){
            if(finaisA[i]!=-1){
                finais[i] = finaisA[i];
            }    
        }
        inicial = iniciaisA[0];
    }
    
    
    /**
     * Inserindo estados do arquivo exemplo.jff
     * na lista de Automatos
     */
    public void insereEstados(){
       try {
            aut = getAutomato("arquivos/exemplo.jff");
            states = getStates("arquivos/exemplo.jff");
            alfabetoA = getAlfabetoA("arquivos/exemplo.jff");
            IniciaisFinais = getIniFin("arquivos/exemplo.jff");
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Retornando mensagem de finalizacao de execucao
     */
    public void mostraMensagem(){
        System.out.println("Arquivo saida.jff criado na pasta 'arquivos' no diretorio do projeto.");
    }
    
    
    /**
     * Completa o automato - Para cada estado
     * deve existir transicao para todos os simbolos
     * do alfabeto. Se nao existir, cria um novo estado
     * e faz a transicao
     */
    public void completaAut(){
        
        int estadoN = aut.size();
        setAlfa();
        setIniFin();
        String [] alf2 = new String[alfabeto.length];
        
        for(int i=0; i<aut.size(); i++){
            for(int j=0; j<aut.size(); j++){
                if(aut.get(j).getEst0() == i && aut.get(j).getEst1() != estadoN){ 
                    
                    for(int k=0; k<alfabeto.length; k++){                        
                        if(aut.get(j).getSimb().equals(alfabeto[k])){                            
                            alf2[k] = aut.get(j).getSimb();
                            
                        } else{
                            aut.add(new Automato(aut.get(j).getEst0(), estadoN, alfabeto[k]));
                        }
                    }
                }
            } 
        }

        //Remove duplicatas desnecessarias
        comparaIguais(estadoN);
        //Inserindo o alfabeto no estado complemento criado anteriormente
        estFinal(estadoN); 
    }
    
    
    /**
     * Remove supostas duplicatas criadas na hora de
     * completar o automato
     * @param estadoN - Estado atual
     */
    public void comparaIguais(int estadoN){
        
        for(int i=0; i<aut.size(); i++){
            for(int j=i+1; j<aut.size(); j++){
                
                if(aut.get(i).getEst0() == aut.get(j).getEst0()){
                    if(aut.get(i).getSimb().equals(aut.get(j).getSimb())){
                        if(aut.get(j).getEst1() == estadoN){
                            aut.remove(j);
                        }
                    }
                } 
            }
        }   
    }
    
    /**
     * Inserir o alfabeto no estado final
     * criado para completar o automato
     * @param estadoN - Quantidade de estados do automato antes do complemento
     */
    public void estFinal(int estadoN){
        for(int i=0; i<alfabeto.length; i++){
            aut.add(new Automato(estadoN, estadoN, alfabeto[i]));
        }
    }
    
    
    /**
     * Remove estados inalcancaveis do automato
     */
    public void estInalcan(){
        
        boolean[]estados = new boolean[aut.size()];
        //Organizando estados em ordem decrescente
        Collections.sort(aut);
        
        for(int i=0; i<aut.size(); i++){
            for(int j=0; j<aut.size(); j++){
                if(aut.get(i).getEst1() == aut.get(j).getEst0()){
                   estados[j] = true;
                }    
            }
        }
        
        for(int i=0; i<aut.size(); i++){    
            if(!estados[i]){
                if(aut.get(i).getEst0() != inicial){
                    
                    //Removendo automato da lista
                    aut.remove(i);
                    
                    //Chamando funcao de remover novamente para
                    //verificar se existem mais estados inalcancaveis
                    estInalcan();
                }
            }
        }
    }
    
    /**
     * Recebe como parametro o arquivo do JFLAP
     * Retorna um ArrayList com o automato, e suas transicoes
     */
    public ArrayList<Automato> getAutomato(String filename) throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.parse(xmlFile);
        NodeList list = document.getElementsByTagName("transition");
        ArrayList<Automato> transitions = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {

            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                String from = element.getElementsByTagName("from").item(0).getTextContent();
                String to = element.getElementsByTagName("to").item(0).getTextContent();
                String simb = element.getElementsByTagName("read").item(0).getTextContent();
                
                Automato automato = new Automato((Integer.parseInt(from)),Integer.parseInt(to),simb);
                //System.out.println(from+","+to+","+simb);
                transitions.add(automato);
            }
        }

       
        return transitions;
    }
    
    public Set<String> getAlfabetoA(String filename) throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.parse(xmlFile);
        NodeList list = document.getElementsByTagName("transition");
        Set<String> alfabetoB = new HashSet<>();
        for (int i = 0; i < list.getLength(); i++) {

            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String simb = element.getElementsByTagName("read").item(0).getTextContent();
                alfabetoB.add(simb);
            }
        }

        
       /* for (String str : alfabetoB){
            System.out.print(str);
        }
        */
       
        return alfabetoB;
    }
    
    /**
     * Recebe como parametro o arquivo do JFLAP
     * Retorna um ArrayList com as posicoes,nomes e id dos estados
     */
    public ArrayList<State> getStates(String filename) throws ParserConfigurationException, SAXException, IOException{
        
        File xmlFile = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.parse(xmlFile);
        NodeList states_list = document.getElementsByTagName("state");
        
        boolean [] finais = new boolean [states_list.getLength()];  //pode ter mais de um
        boolean [] iniciais = new boolean[states_list.getLength()]; //pode ter so um
        ArrayList<State> states = new ArrayList<>();
        for (int i = 0; i < states_list.getLength(); i++) {

            Node node = states_list.item(i);
            
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                int id = Integer.parseInt(element.getAttribute("id"));
                String name = element.getAttribute("name");
                String x = element.getElementsByTagName("x").item(0).getTextContent();
                String y = element.getElementsByTagName("y").item(0).getTextContent();
                
                boolean ini = false;
                boolean fin = false;
                
                if (element.getElementsByTagName("initial").getLength() >= 1){
                    ini = true;
                }
                if (element.getElementsByTagName("final").getLength() >=1){
                    fin = true;
                }
                State state = new State(id,name,x,y,ini,fin);
                states.add(state);
            }
            
        }
        
        return states;
    }
    
    public Map<String,int[]> getIniFin(String filename) throws ParserConfigurationException, SAXException, IOException{
        
        File xmlFile = new File(filename);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.parse(xmlFile);
        NodeList states_list = document.getElementsByTagName("state");
        Map<String, int[]> IniFin = new HashMap<String, int[]>();
        
        
        int [] finais = new int [states_list.getLength()];  //pode ter mais de um
        int [] iniciais = new int[states_list.getLength()]; //pode ter so um
        for (int i = 0; i < states_list.getLength(); i++) {
            iniciais[i] = -1;
            finais[i] = -1;
        }
        
        int iniC = 0;
        int finC = 0;
        for (int i = 0; i < states_list.getLength(); i++) {

            Node node = states_list.item(i);
            
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                String id = element.getAttribute("id");
                
                boolean ini = false;
                boolean fin = false;
                
                if (element.getElementsByTagName("initial").getLength() >= 1){
                    ini = true;
                    iniciais[iniC] = Integer.parseInt(id);
                    iniC++;
                }
                if (element.getElementsByTagName("final").getLength() >=1){
                    fin = true;
                    finais[finC] = Integer.parseInt(id);
                    finC++;
                    
                }
            }
        }
        IniFin.put("INICIAL",iniciais);
        IniFin.put("FINAL",finais);
        return IniFin;
    }
    /**
     * Recebe como parametro o automato minimizado e escreve de volta
     * no XML para leitura do JFLAP
     */
    public void writeXML(){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //add elements to Document
            Element rootElement = doc.createElement("structure");
            //append root element to document
            doc.appendChild(rootElement);
            
            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode("fa"));
            Element automaton = doc.createElement("automaton");
            
            rootElement.appendChild(type);
            rootElement.appendChild(automaton);
            
            
            for (Iterator<State> i = states.iterator();i.hasNext();){
                State state = i.next();
                int id = (state.getId());
                int right = 0;
                for (Automato automato : aut){
                    int  est0 = (automato.getEst0());
                    int  est1 = (automato.getEst1());
                    
                    if((id == est0)||(id == est1)){
                        right++;
                    }
                }
                if (right == 0){
                    i.remove();
                }
            }
            
            for (Iterator<Automato> i = aut.iterator();i.hasNext();){
                Automato auto = i.next();
                int est0 = (auto.getEst0());
                int est1 = (auto.getEst1());
                int right = 0;
                double x=0,y=0;
                for (State state : states){
                    int  id = (state.getId());
                    x = Double.parseDouble(state.getX());
                    y = Double.parseDouble(state.getY());
                    if((id == est0)){
                        right++;
                    }
                }
                if (right == 0){
                    int nID = (est0);
                    String name = ("q"+Integer.toString(est0));
                    
                    String nx =Double.toString(x+50.0);
                    String ny = Double.toString(y+50.0);
                    
                    State statex = new State(nID,name,nx,ny,false,false);
                    states.add(statex);
                    //i.remove();
                }
            }
            //para cada estado, pega os dados e escreve no XML
            for(State state : states){
                String id = Integer.toString(state.getId());
                String name = state.getName();
                String x = state.getX();
                String y = state.getY();
                boolean ini = state.isIni();
                boolean fin = state.isFin();
                automaton.appendChild(getElementState(doc, id, name,x,y,ini,fin));
            }

            //para cada transicao, pega os dados e escreve no XML
            for (Automato automato : aut) {
                String  est0 = Integer.toString(automato.getEst0());
                String  est1 = Integer.toString(automato.getEst1());
                String  simb = automato.getSimb();
                automaton.appendChild(getElementTransition(doc, est0, est1, simb));
            }
            
            //for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //write to console or file
            StreamResult file = new StreamResult(new File("arquivos/saida.jff"));

            //write data
            transformer.transform(source, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param doc
     * @param id -id do estado
     * @param name -nome do estado
     * @param x - posicao x
     * @param y -posicao y
     * @param iniB - inicial ou nao
     * @param finB - final ou nao
     * @return - o Node com os dados para serem escritos no XML
     */
    private static Node getElementState(Document doc, String id, String name,String x, String y,
            boolean iniB, boolean finB) {
        Element state = doc.createElement("state");

        //set id attribute
        state.setAttribute("id", id);
        state.setAttribute("name", name);

        //create x element
        state.appendChild(getElements(doc, state, "x", x));

        //create y element
        state.appendChild(getElements(doc, state, "y", y));

        //create initial element
        if (iniB == true){
            state.appendChild(getElements(doc, state, "initial", ""));
        }
        

        //create final element
        if(finB == true){
            state.appendChild(getElements(doc, state, "final", ""));
        }
        
        return state;
    }
    
    /**
     * 
     * @param doc
     * @param from -es0
     * @param to -es1
     * @param read - sim
     * @return - Node com os dados para serem escritos no XML
     */
    private static Node getElementTransition(Document doc, String from, String to, String read) {
        Element transition = doc.createElement("transition");

        //create from element
        transition.appendChild(getElements(doc, transition, "from", from));

        //create to element
        transition.appendChild(getElements(doc, transition, "to", to));

        //create read element
        transition.appendChild(getElements(doc, transition, "read", read));

        return transition;
    }
    
    /**
     * Abstracao para facilitar o metodo de escrever no XML
     */
    private static Node getElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
    
