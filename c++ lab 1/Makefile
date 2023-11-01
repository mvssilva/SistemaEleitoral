hello: hello.o pessoa.o livro.o emprestimo.o
	g++ -o hello hello.o pessoa.o livro.o emprestimo.o

hello.o: hello.cpp livro.h pessoa.h emprestimo.h
	g++ -c -o hello.o hello.cpp

livro.o: livro.cpp livro.h pessoa.h
	g++ -c -o livro.o livro.cpp

pessoa.o: pessoa.cpp pessoa.h
	g++ -c -o pessoa.o pessoa.cpp
	
emprestimo.o: emprestimo.cpp emprestimo.h livro.h pessoa.h
	g++ -c -o emprestimo.o emprestimo.cpp

clean:
	rm *.o
	rm hello