package leitura;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import eleicao.Candidato;
import eleicao.Eleicao;
import eleicao.Votos;

public abstract class Leitura {
    
    /**
     * Lê os dados de candidatos de um arquivo e retorna um HashMap contendo os candidatos
     * que correspondem ao tipo especificado na linha de comando.
     *
     * @param tipoDesejado O tipo de candidato desejado ("ESTADUAL" ou "FEDERAL").
     * @param diretorioArquivo O caminho para o arquivo de candidatos.
     * @return Um HashMap onde as chaves são os índices dos candidatos (SQ_CANDIDATOS) e os valores 
     * são objetos Candidato que correspondem ao tipo desejado.
     * @throws FileNotFoundException Se o arquivo de candidatos não for encontrado.
     * @throws IOException Se ocorrerem erros de leitura no arquivo.
     */
    public static HashMap<String, Candidato> arquivoCandidatos(String tipoCandidatoDesejado, String diretorioArquivo){
        boolean primeiraLinha = true;
        HashMap<String, Candidato> listaCandidatos = new HashMap<>();
        
        try(FileInputStream fin = new FileInputStream(diretorioArquivo);
            Scanner s = new Scanner(fin, "ISO-8859-1"))
        {
            while(s.hasNextLine()){
                String line = s.nextLine();
                if(primeiraLinha){
                    primeiraLinha = false;
                    continue;
                }
                Candidato candidato = new Candidato(line);

                //Não posso delimitar o tipo de candidato por que se não perco partidos.
                listaCandidatos.put(candidato.getIdxCandidato(), candidato);
            }
            s.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return listaCandidatos; 
    }

    /**
     * Lê os dados de votação do arquivo e associa os votos á eleição, considerando o tipo de candidato desejado.
     *
     * @param diretorioArquivo O caminho para o arquivo de votação.
     * @param eleicao A instância de Eleicao a qual os votos serão associados.
     * @param tipoCandidatoDesejado O tipo de candidato desejado ("ESTADUAL" ou "FEDERAL").
     * @throws FileNotFoundException Se o arquivo de votação não for encontrado.
     * @throws IOException Se ocorrerem erros de leitura no arquivo de votação.
     */
    public static void arquivosVotacao(String diretorioArquivo, Eleicao eleicao, String tipoCandidatoDesejado){
        boolean primeiraLinha = true;

        try(FileInputStream fin = new FileInputStream(diretorioArquivo);
            Scanner s = new Scanner(fin, "ISO-8859-1"))
        {
            while(s.hasNextLine()){
                String line = s.nextLine();
                if(primeiraLinha){
                    primeiraLinha = false;
                    continue;
                }
                    
                Votos votos = new Votos(line);
                if(votos.verificaCandidato(tipoCandidatoDesejado)){
                    eleicao.setVotos(votos);
                }
                else{
                    
                }
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
