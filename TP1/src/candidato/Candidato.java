package candidato;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

enum TipoCandidato{
    FEDERAL(6),
    ESTADUAL(7);

    private final int value;

    public int getTipoCandidato() {
        return value;
    }
    TipoCandidato(int x){
        this.value = x;
    }
}

enum SituacaoCandidato{
    DEFERIDA2(2),
    DEFERIDA16(16),
    NAODEFERIDA(0);

    private final int value;

    public int getSituacaoCandidato() {
        return value;
    }
    SituacaoCandidato(int x){
        this.value = x;
    }
}

enum SituacaoAposTurno{
    ELEITO2(2),
    ELEITO3(3),
    NAOELEITO(0);

    private final int value;

    public int getSituacaoAposTurno() {
        return value;
    }
    SituacaoAposTurno(int x){
        this.value = x;
    }
}

enum Genero{
    MASCULINO(2),
    FEMININO(3);

    private final int value;

    public int getGenero() {
        return value;
    }
    Genero(int x){
        this.value = x;
    }
}

enum TipoDestinacaoVotos{VALIDO, NAOVALIDO}

public class Candidato {
    private TipoCandidato tipoCandidato;
    private SituacaoCandidato situacaoCandidato;
    private String numeroCandidato;
    private String nomeCandidato;
    private String numeroPartido;
    private String siglaPartido;
    private String numeroFederacao;
    private Date dataNascimento;
    private SituacaoAposTurno situacaoAposTurno;
    private Genero genero;
    private TipoDestinacaoVotos tipoDestinacaoVotos;
    
    public TipoCandidato getTipoCandidato() {
        return tipoCandidato;
    }

    public SituacaoCandidato getSituacaoCandidato() {
        return situacaoCandidato;
    }

    public String getNumeroCandidato() {
        return numeroCandidato;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public String getNumeroPartido() {
        return numeroPartido;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public String getNumeroFederacao() {
        return numeroFederacao;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public SituacaoAposTurno getSituacaoAposTurno() {
        return situacaoAposTurno;
    }

    public Genero getGenero() {
        return genero;
    }

    public TipoDestinacaoVotos getTipoDestinacaoVotos() {
        return tipoDestinacaoVotos;
    }

    public Candidato(String line){        
        try(Scanner lineScanner = new Scanner(line))
        {
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

            lineScanner.useDelimiter(";");
            int cont = 0;
            while(lineScanner.hasNext()){
                String itemComAspas = lineScanner.next();
                String itemSemAspas = itemComAspas.replace("\"", "");

                //System.out.printf("[%d -- %s]\n", cont, itemSemAspas);

                if(cont == 13)
                {
                    //CD_CARGO: 6 para deputado federal e 7 para deputado estadual;
                    if(itemSemAspas.equals("6")){
                        this.tipoCandidato = TipoCandidato.FEDERAL;
                    }
                    else if(itemSemAspas.equals("7")){
                        this.tipoCandidato = TipoCandidato.ESTADUAL;
                    }
                }

                if(cont == 68)
                {
                    //CD_SITUACAO_CANDIDADO_TOT: processar apenas aqueles com os valores 2 ou 16 que representam candidatos com candidatura deferida;
                    if(itemSemAspas.equals("2")){
                        this.situacaoCandidato = SituacaoCandidato.DEFERIDA2;
                    }
                    else if(itemSemAspas.equals("16")){
                        this.situacaoCandidato = SituacaoCandidato.DEFERIDA16;
                    }
                    else{
                        this.situacaoCandidato = SituacaoCandidato.NAODEFERIDA;   
                    }
                }

                if(cont == 16)
                {
                    //NR_CANDIDATO: número do candidato; 
                    this.numeroCandidato = itemSemAspas;
                }
                
                if(cont == 18)
                {
                    //NM_URNA_CANDIDATO: nome do candidato na urna;
                    this.nomeCandidato = itemSemAspas;
                }

                if(cont == 27)
                {
                    //NR_PARTIDO: numero do partido;
                    this.numeroPartido = itemSemAspas;
                }

                if(cont == 28)
                {
                    //SG_PARTIDO: sigla do partido;
                    this.siglaPartido = itemSemAspas;
                } 

                if(cont == 30)
                {
                    //NR_FEDERACAO: numero da federacão, com -1 representando candidato em partido isolado (que não participa de federação)
                    this.numeroFederacao = itemSemAspas;
                }

                if(cont == 42)
                {
                    //DT_NASCIMENTO: data de nascimento do candidato no formato dd/mm/aaaa;
                    this.dataNascimento = formatoData.parse(itemSemAspas);
                }  

                if(cont == 56)
                {
                    //CD_SIT_TOT_TURNO: 2 ou 3 representando candidato eleito;
                    if(itemSemAspas.equals("2")){
                        this.situacaoAposTurno = SituacaoAposTurno.ELEITO2;
                    }
                    else if(itemSemAspas.equals("3")){
                        this.situacaoAposTurno = SituacaoAposTurno.ELEITO3;
                    }
                    else{
                        this.situacaoAposTurno = SituacaoAposTurno.NAOELEITO;
                    }
                }   
                if(cont == 45){
                    //CD_GENERO; 2 representando masculino e 4 representando feminino;
                    if(itemSemAspas.equals("2")){
                        this.genero = Genero.MASCULINO;
                    }
                    else if(itemSemAspas.equals("3")){
                        this.genero = Genero.FEMININO;
                    }
                }   
                if(cont == 67){
                    //NM_TIPO_DESTINACAO_VOTOS: quando for "Válido" os votos deste candidato vão para a legenda (e devem ser computados para a legenda, mesmo em caso de CD_SITUACAO_CANDIDADO_TOT diferente de 2 ou 16)
                    String itemSemAcentos = Normalizer.normalize(itemSemAspas, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    if(itemSemAcentos.equals("Valido")){
                        this.tipoDestinacaoVotos = TipoDestinacaoVotos.VALIDO;
                    }
                    else{
                        this.tipoDestinacaoVotos = TipoDestinacaoVotos.NAOVALIDO;
                    }
                }   
                cont++;
            }

            lineScanner.close();
        }catch (ParseException e) {
            System.out.printf("Erro durante a conversão dos dados do candidato = [%s]", line);
        }

    }
}
