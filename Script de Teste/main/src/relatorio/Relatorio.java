package relatorio;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

import eleicao.*;

public abstract class Relatorio {

    /**
     * Gera um relatório com várias seções com base nos resultados da eleição.
     *
     * @param eleicao A eleição da qual deseja gerar o relatório.
     * @param tipoCandidato O tipo de candidato desejado (por exemplo, "FEDERAL" ou "ESTADUAL").
     * @param dataString A data da eleição no formato de string.
     */
    public static void gerarRelatorio(Eleicao eleicao, String tipoCandidato, String dataString){  
        // Converte o tipo de candidato para maiúsculas e remove os dois primeiros caracteres
        String tipoCandidatoCorreto = tipoCandidato.substring(2).toUpperCase();

        // Obtém uma lista de candidatos mais votados
        LinkedList<Candidato> candidatosPorVotos = eleicao.getCandidatosMaisVotados(tipoCandidatoCorreto);

        // Inicializa listas para relatórios
        LinkedList<Candidato> relatorioCandidatosEleitos = new LinkedList<>();
        LinkedList<Candidato> relatorioTodosCandidatos = new LinkedList<>();

        // Organiza a lista de candidatos
        eleicao.getCandidatosOrganizados(candidatosPorVotos, relatorioCandidatosEleitos, relatorioTodosCandidatos);

        // Imprime os relatórios 1 e 2
        printRelatorioCandidatosEleitos(relatorioCandidatosEleitos, relatorioCandidatosEleitos.size(), tipoCandidatoCorreto);
        printRelatorioCandidatosMaisVotados(relatorioTodosCandidatos, relatorioCandidatosEleitos.size());
        printRelatorioCandidatosEleitosDiferentesSistemas(relatorioCandidatosEleitos, relatorioTodosCandidatos, relatorioCandidatosEleitos.size());

        // Obtém uma lista de partidos mais votados
        LinkedList<Partidos> partidosPorTotalVotos = eleicao.getPartidosMaisVotados(tipoCandidatoCorreto);

        // Imprime o relatório 6
        printRelatorioVotacaoPartidos(partidosPorTotalVotos, tipoCandidatoCorreto);

        // Obtém uma lista de partidos com candidatos mais votados
        LinkedList<Partidos> partidosComCandidatosMaisVotados = eleicao.getPartidosComCandidatosMaisVotados(tipoCandidatoCorreto);

        // Imprime o relatório 7
        printRelatorioPrimeirosUltimosPorPartido(partidosComCandidatosMaisVotados, tipoCandidatoCorreto);

        // Imprime o relatório 8
        printRelatorioDistribuicaoPorFaixaEtaria(relatorioCandidatosEleitos, dataString);

        // Imprime o relatório 9
        printRelatorioDistribuicaoPorGenero(relatorioCandidatosEleitos);

        // Imprime o relatório 10
        printRelatorioVotos(partidosPorTotalVotos, tipoCandidatoCorreto);   

    }
    /**
     * Imprime o ralatório do número de vagas.
     * Imprime o relatório dos candidatos eleitos e seus votos.
     *
     * @param candidatosEleitos Uma lista de candidatos eleitos.
     * @param numeroVagas O número de vagas disponíveis.
     * @param tipoCandidato O tipo de candidato (por exemplo, "ESTADUAL" ou "FEDERAL").
     */

    private static void printRelatorioCandidatosEleitos(LinkedList<Candidato> candidatosEleitos, int numeroVagas, String tipoCandidato) {
        System.out.println();
        System.out.println("Número de vagas: " + numeroVagas);
        System.out.println();
        
        String cargo = tipoCandidato.equals("ESTADUAL") ? "Deputados estaduais eleitos:" : "Deputados federais eleitos:";
        System.out.println(cargo);
        
        int cont = 1;
        for (Candidato candidato : candidatosEleitos) {
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
            String votosFormatados = formato.format(candidato.getVotosTotais());        
    
            String prefixo = (candidato.getNumeroFederacao() != -1) ? "*" : "";
            String nomeCandidato = candidato.getNomeCandidato().toUpperCase();
            String siglaPartido = candidato.getSiglaPartido();
    
            System.out.printf("%d - %s%s (%s, %s votos)%n", cont, prefixo, nomeCandidato, siglaPartido, votosFormatados);
            cont++;
        }
        
        System.out.println();
    }

    /**
     * Imprime o relatório dos candidatos mais votados em ordem decrescente de votação.
     *
     * @param candidatosMaisVotados Uma lista de candidatos mais votados.
     * @param numeroVagas O número de vagas disponíveis.
     */
    private static void printRelatorioCandidatosMaisVotados(LinkedList<Candidato> candidatosMaisVotados, int numeroVagas) {
        System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        
        int cont = 1;
        for (Candidato candidato : candidatosMaisVotados) {
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
            String votosFormatados = formato.format(candidato.getVotosTotais());
            
            String prefixo = (candidato.getNumeroFederacao() != -1) ? "*" : "";
            String nomeCandidato = candidato.getNomeCandidato().toUpperCase();
            String siglaPartido = candidato.getSiglaPartido();

            System.out.printf("%d - %s%s (%s, %s votos)%n", cont, prefixo, nomeCandidato, siglaPartido, votosFormatados);
            
            if(cont == numeroVagas)
                break;
            
            cont++;
        }
        
        System.out.println();
    }
    
    /**
     * Imprime relatórios dos candidatos que teriam sido eleitos sob diferentes sistemas.
     *
     * @param candidatosEleitosMajoritariamente Lista de candidatos eleitos no sistema majoritário.
     * @param candidatosEleitosProporcionalmente Lista de candidatos eleitos no sistema proporcional.
     * @param numeroVagas O número de vagas disponíveis.
     */
     private static void printRelatorioCandidatosEleitosDiferentesSistemas(LinkedList<Candidato> candidatosEleitosMajoritariamente,
     LinkedList<Candidato> candidatosEleitosProporcionalmente, int numeroVagas) {
        System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
        System.out.println("(com sua posição no ranking de mais votados)");

        int cont = 1;
        int indexMajoritario = 0;
        int indexProporcional = 0;

        while (cont <= numeroVagas) {
            Candidato candidatoMajoritario = candidatosEleitosMajoritariamente.get(indexMajoritario);
            Candidato candidatoProporcional = candidatosEleitosProporcionalmente.get(indexProporcional);

            if (candidatoMajoritario.getCandidatoEhIgual(candidatoProporcional)) {
                indexMajoritario++;
                indexProporcional++;
                cont++;
                continue;
            } 
            else {
                NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
                String votosFormatados = formato.format(candidatoProporcional.getVotosTotais());

                String prefixo = (candidatoProporcional.getNumeroFederacao() != -1) ? "*" : "";
                String nomeCandidato = candidatoProporcional.getNomeCandidato().toUpperCase();
                String siglaPartido = candidatoProporcional.getSiglaPartido();

                System.out.printf("%d - %s%s (%s, %s votos)%n", cont, prefixo, nomeCandidato, siglaPartido, votosFormatados);
            }

            indexProporcional++;
            cont++;
        }

        System.out.println();

        System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
        System.out.println("(com sua posição no ranking de mais votados)");

        cont = 1;

        while (cont <= numeroVagas) {
            Candidato candidato = candidatosEleitosMajoritariamente.get(cont - 1);

            if (candidatosEleitosProporcionalmente.indexOf(candidato) + 1 > numeroVagas) {
                NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
                String votosFormatados = formato.format(candidato.getVotosTotais());

                String prefixo = (candidato.getNumeroFederacao() != -1) ? "*" : "";
                String nomeCandidato = candidato.getNomeCandidato().toUpperCase();
                String siglaPartido = candidato.getSiglaPartido();

                System.out.printf("%d - %s%s (%s, %s votos)%n", candidatosEleitosProporcionalmente.indexOf(candidato) + 1,
                prefixo, nomeCandidato, siglaPartido, votosFormatados);
            }
            cont++;
        }

        System.out.println();
    }
    
    /**
     * Imprime o relatório da votação dos partidos e o número de candidatos eleitos.
     *
     * @param partidosPorVotos Uma lista de partidos ordenados por votos.
     * @param tipoCandidato O tipo de candidato (por exemplo, "ESTADUAL" ou "FEDERAL").
     */
    private static void printRelatorioVotacaoPartidos(LinkedList<Partidos> partidosPorVotos, String tipoCandidato) {
        System.out.println("Votação dos partidos e número de candidatos eleitos:");
    
        int cont = 1;

        while (cont <= partidosPorVotos.size()) {
            Partidos partido = partidosPorVotos.get(cont - 1);
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
    
            String votosTotais = formato.format(partido.getVotosTotaisDoPartido());
            String votosLegenda = formato.format(partido.getVotosLegenda());
            String votosNominais = formato.format(partido.getVotosNominais());
            System.out.printf("%d - %s - %s, %s",cont, partido.getSiglaPartido(), partido.getNumeroPartido(), votosTotais); 
            
                if(partido.getVotosTotaisDoPartido() > 1)
                    System.out.print("votos ");
                else
                    System.out.print("voto ");
                

            System.out.printf("(%s ", votosNominais);

                if(partido.getVotosNominais() > 1)
                    System.out.print("nominais e ");
                else
                    System.out.print("nominal e ");
                

            System.out.printf("%s de legenda), ", votosLegenda);
    
            int qtdCandidatosEleitos = partido.getCandidatosEleitos(tipoCandidato);
    
            System.out.print(qtdCandidatosEleitos);
    
            if (qtdCandidatosEleitos > 1) {
                System.out.print(" candidatos eleitos");
            } else {
                System.out.print(" candidato eleito");
            }

            cont++;    
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Imprime o relatório dos primeiros e últimos colocados de cada partido.
     *
     * @param partidos Uma lista de partidos.
     * @param tipoCandidato O tipo de candidato (por exemplo, "ESTADUAL" ou "FEDERAL").
     */
    private static void printRelatorioPrimeirosUltimosPorPartido(LinkedList<Partidos> partidos, String tipoCandidato) {
        System.out.println("Primeiro e último colocados de cada partido:");

        int cont = 1;

        while (cont <= partidos.size()) {
            Partidos partido = partidos.get(cont - 1);
            Candidato candidatoMaisVotado = partido.getCandidatoMaisVotado(tipoCandidato);
            Candidato candidatoMenosVotado = partido.getCandidatoMenosVotado(tipoCandidato);
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

            if (candidatoMaisVotado == null) {
                break;
            }

            String votosCandidatoMaisVotado = formato.format(candidatoMaisVotado.getVotosTotais());
            String votosCandidatoMenosVotado = formato.format(candidatoMenosVotado.getVotosTotais());

            String pluralVotosMais = "";
            String pluralVotosMenos = "";

            if(candidatoMaisVotado.getVotosTotais() > 1){
                pluralVotosMais+= "s";
            }
            if(candidatoMenosVotado.getVotosTotais() > 1){
                pluralVotosMenos+= "s";
            }

            System.out.printf("%d - %s - %s, %s (%s, %s voto%s) / %s (%s, %s voto%s)\n",
                cont, partido.getSiglaPartido(), partido.getNumeroPartido(), candidatoMaisVotado.getNomeCandidato(),
                candidatoMaisVotado.getNumeroCandidato(), votosCandidatoMaisVotado, pluralVotosMais,
                candidatoMenosVotado.getNomeCandidato(), candidatoMenosVotado.getNumeroCandidato(), votosCandidatoMenosVotado, pluralVotosMenos);
            cont++;
        }

        System.out.println();
    }
    
    /**
     * Imprime o relatório da distribuição de candidatos eleitos por faixa etária na data da eleição.
     *
     * @param candidatosEleitos Uma lista de candidatos eleitos.
     * @param dataString A data da eleição no formato de string.
     */
    private static void printRelatorioDistribuicaoPorFaixaEtaria(LinkedList<Candidato> candidatosEleitos, String dataString) {
        int totalCandidatos = candidatosEleitos.size();

        int idadeMenor30 = 0;
        int idade30a39 = 0;
        int idade40a49 = 0;
        int idade50a59 = 0;
        int idadeMaiorOuIgual60 = 0;

        int cont = 0;
        while (cont < candidatosEleitos.size()) {
            Candidato candidato = candidatosEleitos.get(cont);
            int idade = candidato.getIdadeCandidato(dataString); 

            if (idade < 30) 
                idadeMenor30++;
            else if (idade < 40)
                idade30a39++;
            else if (idade < 50)
                idade40a49++;
            else if (idade < 60)
                idade50a59++;
            else
                idadeMaiorOuIgual60++;
            
            cont++;
        }

        System.out.println("Eleitos, por faixa etária (na data da eleição):");
        System.out.println("      Idade < 30: " + idadeMenor30 + " (" + calcularPorcentagem(idadeMenor30, totalCandidatos) + "%)");
        System.out.println("30 <= Idade < 40: " + idade30a39 + " (" + calcularPorcentagem(idade30a39, totalCandidatos)  + "%)");
        System.out.println("40 <= Idade < 50: " + idade40a49 + " (" + calcularPorcentagem(idade40a49, totalCandidatos) + "%)");
        System.out.println("50 <= Idade < 60: " + idade50a59 + " (" + calcularPorcentagem(idade50a59, totalCandidatos) + "%)");
        System.out.println("60 <= Idade     : " + idadeMaiorOuIgual60 + " (" + calcularPorcentagem(idadeMaiorOuIgual60, totalCandidatos) + "%)");
        System.out.println();

    }

    private static String calcularPorcentagem(int idade, int total) {
        DecimalFormat formato = new DecimalFormat("0.00");
        String porcentagem = formato.format((idade / (double) total) * 100);
        return porcentagem;
    }

    /**
     * Imprime o relatório da distribuição de candidatos eleitos por gênero.
     *
     * @param candidatosEleitos Uma lista de candidatos eleitos.
     */
    private static void printRelatorioDistribuicaoPorGenero(LinkedList<Candidato> candidatosEleitos) {
        int cont = 0;
        int qtdMasculino = 0;
        int qtdFeminino = 0;
        while (cont < candidatosEleitos.size()) {
            Candidato candidato = candidatosEleitos.get(cont);
                if(candidato.getEhMasculino()){
                    qtdMasculino++;
                }else
                    qtdFeminino++;
            
            cont++;
        }

        System.out.println("Eleitos, por gênero:");
        System.out.println("Feminino:  " + qtdFeminino +" (" + calcularPorcentagem(qtdFeminino, candidatosEleitos.size()) + "%)");
        System.out.println("Masculino: " + qtdMasculino + " (" + calcularPorcentagem(qtdMasculino, candidatosEleitos.size())  + "%)");
        System.out.println();

    }

    /**
     * Imprime o relatório com informações sobre o total de votos válidos, votos nominais e votos de legenda.
     *
     * @param partidos Uma lista de partidos.
     * @param tipoCandidato O tipo de candidato para o qual os votos estão sendo relatados.
     */
    private static void printRelatorioVotos(LinkedList<Partidos> partidos, String tipoCandidato) {
        int cont = 0;
        int votosValidos = 0;
        int votosLegenda = 0;
        int votosNominais = 0;
        while ( cont < partidos.size()) {
            Partidos p = partidos.get(cont);
            votosValidos += p.getVotosTotaisDoPartido();   
            votosLegenda += p.getVotosLegenda();
            votosNominais += p.getVotosNominais();
            cont++;
        }

        NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));            
        String votosValidosFormatado = formato.format(votosValidos);
        String votosLegendaFormatado = formato.format(votosLegenda);
        String votosNominaisFormatado = formato.format(votosNominais);


        System.out.println("Total de votos válidos:    " + votosValidosFormatado);
        System.out.println("Total de votos nominais:   " + votosNominaisFormatado + " (" + calcularPorcentagem(votosNominais, votosValidos)  + "%)");
        System.out.println("Total de votos de legenda: " + votosLegendaFormatado + " (" + calcularPorcentagem(votosLegenda, votosValidos)  + "%)");
        System.out.println();

        
    }
}