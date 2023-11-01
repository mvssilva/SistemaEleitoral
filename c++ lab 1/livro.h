#ifndef LIVRO_H
#define LIVRO_H

#include <string>
#include "pessoa.h"
using namespace std;

class Livro{
    string titulo;
    Pessoa autor;

public:
    Livro();
    Livro(string titulo, Pessoa p);
    string getTitulo();
    Pessoa getAutor();
    void setTitulo(string titulo);
    
};

void imprimeLivro(Livro l);

#endif