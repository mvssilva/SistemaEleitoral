#include "emprestimo.h"

Emprestimo::Emprestimo(){}

Emprestimo::Emprestimo(Pessoa *p, Livro *l)
{
    this->isAtual = true;
    this->pessoa = p;
    this->livro = l;
}

Pessoa *Emprestimo::getUsuario()
{
    return this->pessoa;
}

Livro *Emprestimo::getLivro()
{
    return this->livro;
}

bool Emprestimo::emprestimoSituacao()
{
    return this->isAtual;
}

void Emprestimo::emprestimoFoiEntregue()
{
    this->isAtual = false;
}
