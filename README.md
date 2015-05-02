srcmlolol
=========

`srcmlolol` is an attempt to use ANTLR 4 and any given grammar to parse source
code into XML over a weekend and without a combined 1MM of funding ([1][], [2][]).


[1]: http://www.nsf.gov/awardsearch/showAward?AWD_ID=1305292
[2]: http://www.nsf.gov/awardsearch/showAward?AWD_ID=1305217

Usage
-----

1. Compile grammar with ANTLR4:

        antlr4 Java.g4 -o .
        javac *.java

2. Run with srcmlolol:

        java -cp /usr/share/java/antlr-complete.jar:. srcmlolol.App Java Lol.java

All XML will be written to stdout. Use redirection like an adult.