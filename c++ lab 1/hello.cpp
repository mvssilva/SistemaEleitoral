#include "pessoa.h"
#include "emprestimo.h"
#include "livro.h"

#include <iostream>
using namespace std;

int main(){
    Pessoa m("Marcos");
    Pessoa assis("Machado de Assis");
    Livro casmurro("Dom Casmurro", assis);

    Emprestimo e(&m, &casmurro);

    imprimeLivro(casmurro);
    imprimePessoa(m);
    string result = e.emprestimoSituacao() ? "Livro ainda não foi entregue.\n" : "Livro foi entregue\n"; 
    cout << result;
}