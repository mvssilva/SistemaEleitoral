#ifndef PESSOA_H
#define PESSOA_H

#include <string>
using namespace std;

class Pessoa{
    string nome;

public:
    Pessoa();
    Pessoa(string nome);
    string getNome();
    void setNome(string nome);
    
};

void imprimePessoa(Pessoa p);

#endif