package eleicao;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

enum TipoCandidato{
    NAOIMPORTA(0),
    EXTRA(1),
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

enum TipoDestinacaoVotos{
    VALIDO(0), LEGENDA(1), NAOVALIDO(2);

    private final int value;
    public int getTipoDestinacaoVotos(){
        return value;
    }

    TipoDestinacaoVotos(int x){
        this.value = x;
    }
}

public class Candidato{

    private TipoCandidato tipoCandidato; 
    private SituacaoCandidato situacaoCandidato;
    private String numeroCandidato;
    private String nomeCandidato;
    private String numeroPartido;
    private String siglaPartido;
    private int numeroFederacao;
    private Date dataNascimento;
    private SituacaoAposTurno situacaoAposTurno;
    private Genero genero;
    private TipoDestinacaoVotos tipoDestinacaoVotos;
    private int votosTotais;
    private String idxCandidato;
    
    /**
     * Construtor da classe Candidato que inicializa os dados do candidato com base em uma linha de dados.
     *
     * @param line Uma linha de dados que contém informações do candidato separadas por ponto e vírgula.
     * @throws ParseException Se ocorrer um erro durante a conversão de data.
     */
    public Candidato(String line){        
        try(Scanner lineScanner = new Scanner(line))
        {
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

            lineScanner.useDelimiter(";");
            int coluna = 0;
            while(lineScanner.hasNext()){
                String itemComAspas = lineScanner.next();
                String itemSemAspas = itemComAspas.replace("\"", "");

                if(coluna == 13)
                {
                    //CD_CARGO: 6 para deputado federal e 7 para deputado estadual;
                    if(itemSemAspas.equals("6")){
                        this.tipoCandidato = TipoCandidato.FEDERAL;
                    }
                    else if(itemSemAspas.equals("7")){
                        this.tipoCandidato = TipoCandidato.ESTADUAL;
                    }
                    else if(itemSemAspas.equals("9") || itemSemAspas.equals("10") || itemSemAspas.equals("4")){
                        //SUPLENTE 1, SUPLENTE 2 E VICE-GOVERNADOR precisam ser identificados para os relatórios com partidos.
                        this.tipoCandidato = TipoCandidato.EXTRA;
                    }else{
                        this.tipoCandidato = TipoCandidato.NAOIMPORTA;
                    }
                }

                if(coluna == 68)
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

                if(coluna == 16)
                {
                    //NR_CANDIDATO: número do candidato; 
                    this.numeroCandidato = itemSemAspas;
                }
                
                if(coluna == 18)
                {
                    //NM_URNA_CANDIDATO: nome do candidato na urna;
                    this.nomeCandidato = itemSemAspas;
                }

                if(coluna == 27)
                {
                    //NR_PARTIDO: número do partido;
                    this.numeroPartido = itemSemAspas;
                }

                if(coluna == 28)
                {
                    //SG_PARTIDO: sigla do partido;
                    this.siglaPartido = itemSemAspas;
                } 

                if(coluna == 30)
                {
                    //NR_FEDERACAO: numero da federação, com -1 representando candidato em partido isolado (que não participa de federação)
                    this.numeroFederacao = Integer.parseInt(itemSemAspas);
                }

                if(coluna == 42)
                {
                    //DT_NASCIMENTO: data de nascimento do candidato no formato dd/mm/aaaa;
                    if (itemSemAspas != null && !itemSemAspas.isEmpty()) {
                        this.dataNascimento = formatoData.parse(itemSemAspas);
                    } else {
                        /**
                         * Inicializei como uma nova data para não interferir no comparador de candidatos.
                         */
                        this.dataNascimento = new Date();
                    }
                }  

                if(coluna == 56)
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
                if(coluna == 45){
                    //CD_GENERO; 2 representando masculino e 4 representando feminino;
                    if(itemSemAspas.equals("2")){
                        this.genero = Genero.MASCULINO;
                    }
                    else if(itemSemAspas.equals("3")){
                        this.genero = Genero.FEMININO;
                    }
                }   
                if(coluna == 67){
                    //NM_TIPO_DESTINACAO_VOTOS: quando for "Válido (legenda)" os votos deste candidato vão 
                    //para a legenda (e devem ser computados para a legenda, mesmo em caso de CD_SITUACAO_CANDIDADO_TOT diferente de 2 ou 16)
                    String itemSemAcentos = Normalizer.normalize(itemSemAspas, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    itemSemAcentos = itemSemAcentos.toUpperCase();
                    if(itemSemAcentos.equals("VALIDO (LEGENDA)")){
                        this.tipoDestinacaoVotos = TipoDestinacaoVotos.LEGENDA;
                    }
                    else if(itemSemAcentos.equals("VALIDO")){
                        this.tipoDestinacaoVotos = TipoDestinacaoVotos.VALIDO;
                    }
                    else{
                        this.tipoDestinacaoVotos = TipoDestinacaoVotos.NAOVALIDO;
                    }
                }   

                if(coluna == 15){
                    //SQ_CANDIDATO = Variável utilizada como conecção entre as informações da eleição;
                    this.idxCandidato = itemSemAspas;
                }

                coluna++;
            }
            lineScanner.close();
        }catch (ParseException e) {
            System.out.printf("Erro durante a conversao dos dados do candidato = [%s]", line);
        }
    }

    public void addVotos(int votos){
        this.votosTotais += votos;
    }

    public String getIdxCandidato() {
        return idxCandidato;
    }

    public int getVotosTotais() {
        return votosTotais;
    }

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

    public int getNumeroFederacao() {
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

    public String getTipoDestinacaoVotos() {
        return tipoDestinacaoVotos.toString();
    }

    public boolean getCandidatoFoiEleito() {
        if(getSituacaoAposTurno().equals(SituacaoAposTurno.ELEITO2) 
        || getSituacaoAposTurno().equals(SituacaoAposTurno.ELEITO3)){
            return true;
        }else{
            return false;
        }

    }

    public boolean getCandidatoEhIgual(Candidato outroCandidato) {
        String idxThis = this.getIdxCandidato();
        String idxOther = outroCandidato.getIdxCandidato();
        
        if(idxThis.compareTo(idxOther) == 0)
            return true;
        else
            return false;
    }

    public boolean getVotosSaoValidos() {
        if(this.getTipoDestinacaoVotos().equals(TipoDestinacaoVotos.VALIDO.toString()))
            return true;
        
        return false;
        
    }

    public boolean getVotosVaoParaLegenda() {
        if(this.getTipoDestinacaoVotos().equals(TipoDestinacaoVotos.LEGENDA.toString()))
            return true;
        
        return false;
    }

    public boolean getVotosNaoValidos() {
        if(this.getTipoDestinacaoVotos().equals(TipoDestinacaoVotos.NAOVALIDO.toString()))
            return true;
        
        return false;
    }
    
    /**
     * Calcula a idade de um candidato com base na data de nascimento fornecida.
     *
     * @param dataString A data de nascimento do candidato no formato "dd/MM/yyyy".
     * @return A idade do candidato em anos, ou -1 se houver um erro na análise da data.
     */
    public int getIdadeCandidato(String dataString) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dataAtual = formato.parse(dataString); 
            LocalDate localDate1 = this.dataNascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate2 = dataAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Calcula a diferença entre as datas usando o objeto Period
            Period period = Period.between(localDate1, localDate2);

            // Retorna a diferença em dias (ou outras unidades, como anos, meses, etc.)
            return period.getYears();

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        
    }

    /**
     * Verifica se o gênero do candidato corresponde ao tipo de gênero MASCULINO.
     *
     * @return true se o gênero do candidato corresponder ao gênero MASCULINO, caso contrário, false.
     */
    public boolean getEhMasculino() {
        if(this.genero == Genero.MASCULINO)
            return true;

        return false;
    }

    /**
     * Verifica se o tipo do candidato corresponde ao tipo desejado.
     *
     * @param tipoCandidatoDesejado O tipo de candidato desejado a ser verificado.
     * @return true se o tipo do candidato corresponder ao tipo desejado, caso contrário, false.
     */
    public boolean verificaCandidato(String tipoCandidatoDesejado) {
        if(tipoCandidatoDesejado.equals(this.tipoCandidato.toString()))
            return true;    
        
        return false;
    }

    public boolean getEhCandidatoDeferido() {
        if(this.situacaoCandidato == SituacaoCandidato.DEFERIDA16
        || this.situacaoCandidato == SituacaoCandidato.DEFERIDA2)
            return true;
        
        return false;
    }

    public boolean ehNecessario() {
        if(this.tipoCandidato.toString().equals(TipoCandidato.NAOIMPORTA.toString()))
            return false;
        
        return true;
    }


}

