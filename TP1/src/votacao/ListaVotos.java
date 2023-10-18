package votacao;

import java.util.LinkedList;

public class ListaVotos {
    private LinkedList<Votos> registroVotos = new LinkedList<>();

    public void addListaVotos(Votos v){
        this.registroVotos.add(getQtdRegistroVotos(), v);
    }

    public int getQtdRegistroVotos(){
        return registroVotos.size();
    }
}
