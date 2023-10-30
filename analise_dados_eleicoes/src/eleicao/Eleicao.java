package eleicao;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import leitura.Leitura;

public class Eleicao{
    
    private HashMap<String, Candidato> candidatos = new HashMap<>();
    private HashMap<String, Partidos> partidos = new HashMap<>();

    /**
     * Construtor da classe Eleicao que inicializa os dados da eleição com base nos arquivos de candidatos
     * e votação fornecidos.
     *
     * @param diretorioArquivoCandidatos O caminho para o arquivo de candidatos.
     * @param diretorioArquivoVotos O caminho para o arquivo de votação.
     */
    public Eleicao(String tipoCandidato, String diretorioArquivoCandidatos, String diretorioArquivoVotos) {
        
        String tipoCandidatoCorreto = tipoCandidato.substring(2).toUpperCase();
        this.candidatos = Leitura.arquivoCandidatos(tipoCandidatoCorreto, diretorioArquivoCandidatos);
        this.setPartidos();
        Leitura.arquivosVotacao(diretorioArquivoVotos, this, tipoCandidatoCorreto);
    }

    /**
     * Associa os candidatos aos partidos com base em seus números de partido, criando e populando
     * objetos Partidos correspondentes para cada número de partido único encontrado nos candidatos.
     */
    private void setPartidos(){
        for(Candidato c : candidatos.values()){
            String numeroPartido = c.getNumeroPartido();

            if(partidos.containsKey(numeroPartido)){
                Partidos p = partidos.get(numeroPartido);
                p.addCandidato(c);
            }
            else{
                Partidos p = new Partidos(c.getSiglaPartido(), c.getNumeroPartido());
                p.addCandidato(c);
                partidos.put(numeroPartido, p);
            }
        }
    }

    /**
     * Associa os votos a candidatos, partidos ou votos em branco/nulos com base no índice de votos.
     *
     * @param votos Um objeto Votos representando os votos a serem associados.
     */
    public void setVotos(Votos votos) {
        String indiceVotos = votos.getIdxVotos();
        //System.out.println(votos.getNomeVotos() + votos.getQtdVotos());
        if (indiceVotos.equals("-3")) {
            // Votos que foram destinados aos partidos.
            associarVotosPartidos(votos);
        } else if (indiceVotos.equals("-1")) {
            // Votos em branco ou nulos.
            // não foram utilizados nessa pesquisa
        } else {
            // Votos para candidatos.
            associarVotosCandidatos(votos);
        }
    }

    /**
    * Associa os votos de um candidato aos candidatos e partidos correspondentes.
    *
    * @param votos Um objeto Votos contendo informações sobre os votos do candidato.
    */
    private void associarVotosCandidatos(Votos votos) {
        
        String indiceCandidato = votos.getIdxVotos();
        Candidato candidato = this.candidatos.get(indiceCandidato);
        candidato.addVotos(votos.getQtdVotos());
    
        String indicePartido = candidato.getNumeroPartido();
        Partidos partido = this.partidos.get(indicePartido);
    
        if (candidato.getVotosSaoValidos()) 
            partido.addVotosNominais(votos.getQtdVotos());
        
        if (candidato.getVotosVaoParaLegenda())
            partido.addVotosLegenda(votos.getQtdVotos());
        
        if(!candidato.getVotosNaoValidos())
            partido.addVotosTotaisDoPartido(votos.getQtdVotos());
            
    }

    private void associarVotosPartidos(Votos votos) {
        
        String idx = votos.getNumeroVotavel();
        if (this.partidos.containsKey(idx)) {
            Partidos partido = this.partidos.get(idx);
            partido.addVotosLegenda(votos.getQtdVotos());
            partido.addVotosTotaisDoPartido(votos.getQtdVotos());
        }
    }

    /**
     * Obtém uma lista de candidatos do tipo especificado, classificados por votação.
     *
     * @param tipoCandidato O tipo de candidato desejado ("FEDERAL" ou "ESTADUAL").
     * @return Uma lista de candidatos do tipo especificado, classificados por votação em ordem decrescente.
     */
    public LinkedList<Candidato> getCandidatosMaisVotados(String tipoCandidato) {
        LinkedList<Candidato> result = new LinkedList<>();
        
        for(Candidato c : candidatos.values()){
            
            String aux = c.getTipoCandidato().toString();
            
            if(aux.equals(tipoCandidato) && c.getEhCandidatoDeferido())
                result.add(c);
        }
        Collections.sort(result, new CandidatoComparator());
        return result;
    }

    /**
     * Obtém uma lista de partidos classificados por quantidade totais de votos.
     *
     * @param tipoCandidato O tipo de candidato desejado ("FEDERAL" ou "ESTADUAL").
     * @return Uma lista de partidos classificados por votação em ordem decrescente.
     */
    public LinkedList<Partidos> getPartidosMaisVotados(String tipoCandidato) {
        
        LinkedList<Partidos> result = new LinkedList<>();
        
        for(Partidos p : partidos.values()){
            result.add(p);
        }
        
        Collections.sort(result, new PartidosComparator());
            
        return result;
    }
    
    /**
    * Um comparador personalizado para a classe Candidato, usado para ordenar candidatos 
    * com base nos votos totais e, em caso de empate, na data de nascimento.
    */
    public class CandidatoComparator implements Comparator<Candidato> {
        /**
         * Compara dois candidatos com base nos votos totais e, em caso de empate, na data de nascimento.
         *
         * @param cand1 O primeiro candidato a ser comparado.
         * @param cand2 O segundo candidato a ser comparado.
         * @return Um valor negativo se cand1 tem menos votos ou  votos iguais mais é mais novo,
         * um valor positivo se cand1 é tem mais votos ou votos iguais e é mais velhos que cand2 
         * e 0 se os votos e as idades forem iguais.
         */
        @Override
        public int compare(Candidato cand1, Candidato cand2) {
            int resultado = Integer.compare(cand2.getVotosTotais(), cand1.getVotosTotais());
             
            if(resultado == 0){
                Date dataCandidato1 = cand1.getDataNascimento();
                Date dataCandidato2 = cand2.getDataNascimento();
                resultado = dataCandidato1.compareTo(dataCandidato2);
            }
            
            return resultado;
        }
    }
    /**
    * Um comparador personalizado para a classe Partidos, usado para ordenar partidos 
    * com base nos votos totais do partido e, em caso de empate, no número do partido.
    */
    public class PartidosComparator implements Comparator<Partidos> {
        /**
         * Compara dois partidos com base nos votos totais do partido e, em caso de empate, no número do partido.
         *
         * @param part1 O primeiro partido a ser comparado.
         * @param part2 O segundo partido a ser comparado.
         * @return Um valor negativo se part1 é tem menos votos que part2, ou se os votos forem iguais e part1 tem numero 
         * de partido menor de part2, um valor positivo se part1 tem mais votos que part2 e 0 se eles forem iguais.
         */
        @Override
        public int compare(Partidos part1, Partidos part2) {
            int resultado = Integer.compare(part2.getVotosTotaisDoPartido(), part1.getVotosTotaisDoPartido());
            if(resultado == 0){
                resultado = part1.getNumeroPartido().compareTo(part2.getNumeroPartido());
            }
            return resultado;
        }
    }
    

    /**
     * Organiza a lista de candidatos em duas outras listas com base em critérios específicos.
     *
     * @param candidatosPorVotos A lista de candidatos ordenada por votação.
     * @param relatorioDois Uma lista que conterá os candidatos eleitos.
     * @param relatorioTres Uma lista que conterá todos os candidatos.
     */
    public void getCandidatosOrganizados(LinkedList<Candidato> candidatosPorVotos, LinkedList<Candidato> relatorioDois, LinkedList<Candidato> relatorioTres) {
        int cont = 0; 
        while(cont < candidatosPorVotos.size()){
            Candidato c = candidatosPorVotos.get(cont);
            
            if(c.getCandidatoFoiEleito())
            {   
                    relatorioDois.add(c);
                }
                
                relatorioTres.add(c);
                cont++;
            }
    }
        
    /**
     * Obtém uma lista de partidos com base nos candidatos mais votados do tipo especificado em cada partido.
     *
     * @param tipoCandidato O tipo de candidato desejado ("FEDERAL" ou "ESTADUAL").
     * @return Uma lista de partidos organizadar pelos candidatos com mais votos de cada partido.
     */
    public LinkedList<Partidos> getPartidosComCandidatosMaisVotados(String tipoCandidato) {
        LinkedList<Partidos> resultPartidos = new LinkedList<>();
        LinkedList<Candidato> resultCandidatos = new LinkedList<>();

        for(Partidos p : partidos.values()){
            Candidato c = p.getCandidatoMaisVotado(tipoCandidato);
                if(c == null)
                    continue;
            resultCandidatos.add(c);
        }
        
        Collections.sort(resultCandidatos, new CandidatoComparator());
        int cont = 0;
        
        while(cont < resultCandidatos.size()){
            Partidos partido = partidos.get(resultCandidatos.get(cont).getNumeroPartido());
            resultPartidos.add(partido);
            cont++;
        }

        return resultPartidos;
    }

    public int getQtdCandidatos(){
        return candidatos.size();
    }

    public int getQtdPartidos(){
        return partidos.size();
    }

        
}
