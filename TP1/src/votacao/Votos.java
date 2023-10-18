package votacao;
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

public class Votos {
    private TipoCandidato tipoCandidato;
    private String numeroVotavel;
    private int qtdVotos;
    
    public TipoCandidato getTipoCandidato() {
        return tipoCandidato;
    }
    
    public String getNumeroVotavel() {
        return numeroVotavel;
    }

    public int getQtdVotos() {
        return qtdVotos;
    }

    public Votos(String line){
        try(Scanner lineScanner = new Scanner(line))
        {
            lineScanner.useDelimiter(";");
            int cont = 0;
            while(lineScanner.hasNext()){
                String itemComAspas = lineScanner.next();
                String itemSemAspas = itemComAspas.replace("\"", "");

                //ystem.out.printf("[%d -- %s]\n", cont, itemSemAspas);
                
                if(cont == 17){
                    if(itemSemAspas.equals("6")){
                        this.tipoCandidato = TipoCandidato.FEDERAL;
                    }
                    else if(itemSemAspas.equals("7")){
                        this.tipoCandidato = TipoCandidato.ESTADUAL;
                    }
                }

                if(cont == 19){
                    this.numeroVotavel = itemSemAspas;
                }

                if(cont == 21){
                    this.qtdVotos = Integer.parseInt(itemSemAspas);
                }
                
                cont++;
            }
        lineScanner.close();
        }
        catch (Exception e) {
            System.out.printf("Erro durante a conversão dos dados de votacao = [%s]", line);
        }
        
    }
}
