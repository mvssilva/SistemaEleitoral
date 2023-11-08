package eleicao;

import java.util.Date;
import java.util.HashMap;

public class Partidos {

    private String siglaPartido;
    private String numeroPartido;
    private HashMap<String, Candidato> candidatosPartido = new HashMap<>();
    private int votosTotaisDoPartido;
    private int votosLegenda;
    private int votosNominais;
    
    /**
     * Construtor da classe Partidos que inicializa a sigla e o número do partido.
     *
     * @param sigla A sigla do partido.
     * @param numero O número do partido.
     */
    public Partidos(String sigla, String numero){
        this.siglaPartido = sigla;
        this.numeroPartido = numero;
    }
    
    /**
     * Obtém o número de candidatos eleitos do tipo especificado neste partido.
     *
     * @param tipoCandidato O tipo de candidato desejado ("FEDERAL" ou "ESTADUAL").
     * @return O número de candidatos eleitos do tipo especificado neste partido.
     */
    public int getCandidatosEleitos(String tipoCandidato) {

        int cont = 0;
        
        for(Candidato c : candidatosPartido.values()){
            String aux = c.getTipoCandidato().toString();
            if(c.getCandidatoFoiEleito()){
                if(aux.equals(tipoCandidato))
                    cont++;
            }
        }
        return cont;
    }

    /**
     * Obtém o candidato mais votado do tipo especificado neste partido.
     *
     * @param tipoCandidato O tipo de candidato desejado ("FEDERAL" ou "ESTADUAL").
     * @return O candidato mais votado do tipo especificado neste partido, ou null se não houver candidatos desse tipo.
     */
    public Candidato getCandidatoMaisVotado(String tipoCandidato) {
        int votosMaior = 0;
        String keyMaior = "";

        boolean aux = true;

        for(Candidato c : candidatosPartido.values()){
            if(tipoCandidato.equals(c.getTipoCandidato().toString())){
                if(c.getVotosNaoValidos())
                    continue;
                if(aux){
                    aux = false;
                    votosMaior = c.getVotosTotais();
                    keyMaior = c.getIdxCandidato();
                }
                else if(votosMaior < c.getVotosTotais()){
                    votosMaior = c.getVotosTotais();
                    keyMaior = c.getIdxCandidato();
                }
            }
        }

        return candidatosPartido.get(keyMaior);
        
    }

    /**
     * Obtém o candidato menos votado do tipo especificado neste partido.
     *
     * @param tipoCandidato O tipo de candidato desejado ("FEDERAL" ou "ESTADUAL").
     * @return O candidato menos votado do tipo especificado neste partido, ou null se não houver candidatos desse tipo ou todos tiverem votos iguais a zero.
     */
    public Candidato getCandidatoMenosVotado(String tipoCandidato) {
        int votosMenor = 0;
        String keyMenor = "";

        boolean aux = true;

        for(Candidato c : candidatosPartido.values()){
            if(tipoCandidato.equals(c.getTipoCandidato().toString())){
                if(c.getVotosNaoValidos())
                    continue;
                if(aux){
                    aux = false;
                    votosMenor = c.getVotosTotais();
                    keyMenor = c.getIdxCandidato();
                }
                else if(votosMenor > c.getVotosTotais()){
                    votosMenor = c.getVotosTotais();
                    keyMenor = c.getIdxCandidato();
                }
                else if(votosMenor == c.getVotosTotais()){
                    Date dataCandidato1 = c.getDataNascimento();
                    Date dataCandidato2 = candidatosPartido.get(keyMenor).getDataNascimento();
                    int result = dataCandidato1.compareTo(dataCandidato2);
                    if(result == 1){
                        votosMenor = c.getVotosTotais();
                        keyMenor = c.getIdxCandidato();
                    }
                }
            }
        }

        return candidatosPartido.get(keyMenor);
    }

    public void addCandidato(Candidato c){
        this.candidatosPartido.put(c.getIdxCandidato(), c);
    }
    
    public String getSiglaPartido() {
        return siglaPartido;
    }

    public String getNumeroPartido() {
        return numeroPartido;
    }

    public void addVotosLegenda(int qtdVotos) {
        this.votosLegenda += qtdVotos;
    }
    
    public void addVotosNominais(int qtdVotos) {
        this.votosNominais += qtdVotos;
    }
    
    public void addVotosTotaisDoPartido(int qtdVotos) {
        this.votosTotaisDoPartido += qtdVotos;
    }
    
    public int getVotosTotaisDoPartido() {
        return votosTotaisDoPartido;
    }
    
    public int getVotosLegenda() {
        return votosLegenda;
    }
    
    public int getVotosNominais() {
        return votosNominais;
    }
}
