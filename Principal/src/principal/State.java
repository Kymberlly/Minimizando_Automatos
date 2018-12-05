/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

/**
 *
 * @author tnany
 */
public class State {
    private int id;
    private String name;
    private String x;
    private String y;
    private boolean ini;
    private boolean fin;

    public State(int id, String name, String x, String y, boolean ini, boolean fin) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.ini = ini;
        this.fin = fin;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public boolean isIni() {
        return ini;
    }

    public boolean isFin() {
        return fin;
    }
    
    
}
