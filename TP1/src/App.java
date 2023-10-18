import java.io.FileInputStream;
import java.util.Scanner;

import candidato.Candidato;
import candidato.ListaCandidatos;

import votacao.*;

public class App {
    public static void main(String[] args) throws Exception {
        
        ListaCandidatos listaCandidatos = new ListaCandidatos();
        try(FileInputStream fin = new FileInputStream("testcandidatos.csv");
            Scanner s = new Scanner(fin, "ISO-8859-1"))
        {
            while(s.hasNextLine()){
                String line = s.nextLine();
                Candidato candidato = new Candidato(line);
                listaCandidatos.addListaCandidatos(candidato);
            }
            s.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(listaCandidatos.getQtdCandidatos());
        
        ListaVotos listaVotos = new ListaVotos();
        try(FileInputStream fin = new FileInputStream("testvotacao.csv");
            Scanner s = new Scanner(fin, "ISO-8859-1"))
        {
            while(s.hasNextLine()){
                String line = s.nextLine();
                Votos votos = new Votos(line);
                listaVotos.addListaVotos(votos);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(listaVotos.getQtdRegistroVotos());

    }
}
