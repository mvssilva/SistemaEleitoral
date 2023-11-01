#include "pessoa.h"
#include <iostream>

Pessoa :: Pessoa(){}

Pessoa :: Pessoa(string nome){
    this->nome = nome;
}

string Pessoa :: getNome(){
    return this->nome;
}

void Pessoa :: setNome(string nome){
    this->nome = nome;
}

void imprimePessoa(Pessoa p){
    cout << p.getNome() << endl;
}

