import eleicao.Eleicao;
import relatorio.Relatorio;

public class App {
    public static void main(String[] args) throws Exception {

        if(args.length != 4){
            System.out.println("ERROR : Quantidade incorreta de parametros!");
            System.out.println("java -jar deputados.jar <opcao_de_cargo> <caminho_arquivo_candidatos>"+
                    "<caminho_arquivo_votacao> <data>");
            System.exit(1);
        }
        
        Eleicao eleicao = new Eleicao(args[0], args[1], args[2]);       
        Relatorio.gerarRelatorio(eleicao, args[0], args[3]);
    }
}
