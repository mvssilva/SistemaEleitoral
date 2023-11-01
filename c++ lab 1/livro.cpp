#include <iostream>
#include "livro.h"

using namespace std;

Livro :: Livro(){}

Livro :: Livro(string titulo, Pessoa p){
    this->titulo = titulo;
    this->autor = p;
}

string Livro :: getTitulo(){
    return this->titulo;
}

Pessoa Livro :: getAutor(){
    return this->autor;
}

void Livro :: setTitulo(string titulo){
    this->titulo = titulo;
}

void imprimeLivro(Livro l){
    cout << "Livro: " << l.getTitulo() << "\nAutor: " << l.getAutor().getNome() << endl;
}