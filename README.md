srcmlolol
=========

`srcmlolol` is an attempt to write a more general [srcML][] that uses ANTLR 4 
and any given grammar to parse source code into XML over a weekend without a 
combined 1MM of funding ([1][], [2][]).


[1]: http://www.nsf.gov/awardsearch/showAward?AWD_ID=1305292
[2]: http://www.nsf.gov/awardsearch/showAward?AWD_ID=1305217
[srcML]: http://srcml.org

Usage
-----

1. Compile grammar with ANTLR4:

        antlr4 Java.g4 -o .
        javac *.java

2. Run with srcmlolol:

        java -cp /usr/share/java/antlr-complete.jar:. srcmlolol.App Java Lol.java

All XML will be written to stdout. Use redirection like an adult.
