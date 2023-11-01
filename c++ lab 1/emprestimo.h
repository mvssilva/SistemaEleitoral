#ifndef EMPRESTIMO_H
#define EMPRESTIMO_H

#include <string>
#include "livro.h"
#include "pessoa.h"
using namespace std;

class Emprestimo{
    bool isAtual;
    Pessoa *pessoa;
    Livro *livro;

public:
    Emprestimo();
    Emprestimo(Pessoa *p, Livro *l);
    Pessoa * getUsuario();
    Livro * getLivro();
    bool emprestimoSituacao();
    void emprestimoFoiEntregue();
    
};

#endif