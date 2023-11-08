package eleicao;

import java.util.Scanner;

public class Votos {
    private TipoCandidato tipoCandidato;
    private String numeroVotavel;
    private int qtdVotos;
    private String idxVotos;
    private String nomeVotos;

    /**
     * Construtor da classe Votos que analisa uma linha de dados de votação no formato CSV e inicializa os campos da classe.
     *
     * @param line A linha de dados de votação no formato CSV.
     */
    public Votos(String line){
        try(Scanner lineScanner = new Scanner(line))
        {
            lineScanner.useDelimiter(";");
            int coluna = 0;
            while(lineScanner.hasNext()){
                String itemComAspas = lineScanner.next();
                String itemSemAspas = itemComAspas.replace("\"", "");

                if(coluna == 17){
                    if(itemSemAspas.equals("6")){
                        this.tipoCandidato = TipoCandidato.FEDERAL;
                    }
                    else if(itemSemAspas.equals("7")){
                        this.tipoCandidato = TipoCandidato.ESTADUAL;
                    }
                    else{
                        this.tipoCandidato = TipoCandidato.NAOIMPORTA;
                    }
                }

                if(coluna == 19){
                    this.numeroVotavel = itemSemAspas;
                }

                if(coluna == 21){
                    this.qtdVotos = Integer.parseInt(itemSemAspas);
                }

                if(coluna == 23){
                    this.idxVotos = itemSemAspas;
                }
                if(coluna == 20){
                    this.nomeVotos = itemSemAspas;
                }
                
                coluna++;
            }
            lineScanner.close();
        }
        catch (Exception e) {
            System.out.printf("Erro durante a conversao dos dados de votacao = [%s]", line);
        }
        
    }

    /**
    * Verifica se o tipo de candidato da instância é igual ao tipo de candidato especificado.
    *
    * @param tipoCandidato O tipo de candidato desejado (por exemplo, "ESTADUAL" ou "FEDERAL").
    * @return true se o tipo de candidato da instância for igual ao tipo de candidato especificado, caso contrário, false.
    */
    public boolean verificaCandidato(String tipoCandidato) {
        if(tipoCandidato.equals(this.tipoCandidato.toString()))
            return true;

        return false;
    }

    public String getIdxVotos() {
        return idxVotos;
    }

    public TipoCandidato getTipoCandidato() {
        return tipoCandidato;
    }
    
    public String getNumeroVotavel() {
        return numeroVotavel;
    }

    public int getQtdVotos() {
        return qtdVotos;
    }

    public void setQtdVotos(int i) {
        this.qtdVotos = i;
    }

    public void addVotos(int i) {
        this.qtdVotos += i;
    }

    public String getNomeVotos() {
        return nomeVotos;
    }

}

