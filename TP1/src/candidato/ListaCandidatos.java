package candidato;

import java.util.LinkedList;

public class ListaCandidatos {
    private LinkedList<Candidato> registroCandidatos = new LinkedList<>();

    public void addListaCandidatos(Candidato c){
        this.registroCandidatos.add(getQtdCandidatos(), c);
    }

    public int getQtdCandidatos(){
        return this.registroCandidatos.size();
    }
}
