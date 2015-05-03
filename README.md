srcMLOLOL
=========

`srcMLOLOL` is a proof-of-concept to write a more general [srcML][] that uses
ANTLR 4 and any given grammar to parse source code into XML.

[srcML]: http://srcml.org

Why
---

srcML relies on a heavily customized ANTLR 2 grammar with loads of actions to
build XML representations of an AST. This customization means that supporting
any more languages outside of the C-like will be difficult and expensive.
Indeed, maintenance and development costs for srcML is [already][1]
[expensive][2]. Unfortunately, not all software is written in C-like languages,
severely limiting the scope of applications srcML has for software engineering
research.

More recent versions of ANTLR provide listeners and visitors. srcMLOLOL will
solve the language support issue by relying on a listener to emit XML.

[1]: http://www.nsf.gov/awardsearch/showAward?AWD_ID=1305217
[2]: http://www.nsf.gov/awardsearch/showAward?AWD_ID=1305292

Usage
-----

1. Compile grammar with ANTLR4:

        antlr4 Java.g4
        javac *.java

2. Compile and run srcMLOLOL:

        ant
        java -jar dist/srcMLOLOL.jar Java Lol.java

All XML will be written to stdout.

Note that this example used the Java grammar, but it's not required! [Use
whichever grammar you want](https://github.com/antlr/grammars-v4). srcMLOLOL
doesn't care.
