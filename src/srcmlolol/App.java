package srcmlolol;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;

public class App {
    public static final String LEXER_START_RULE_NAME = "tokens";

    protected String grammarName;
    protected String startRuleName = "";
    protected final List<String> inputFiles = new ArrayList<String>();
    protected String encoding = null;

    public App(String[] args) throws Exception {
        if (args.length < 2) {
            System.err
                    .println("java App GrammarName [-start startRuleName] [-encoding = encodingname] [input-filename(s)]");
            System.err.println("Use startRuleName='tokens' if GrammarName is a lexer grammar.");
            System.err.println("If no startRuleName given, it will assume the first rule appearing in the grammar.");
            System.err.println("Omit input-filename to read from stdin.");
            return;
        }
        int i = 0;
        this.grammarName = args[i];
        i++;
        while (i < args.length) {
            String arg = args[i];
            i++;
            if (arg.charAt(0) != '-') { // input file name
                this.inputFiles.add(arg);
                continue;
            } else if (arg.equals("-start")){
                if (i >= args.length) {
                    System.err.println("missing rule name on -start");
                    return;
                }
                this.startRuleName = args[i];
                i++;
            } else if (arg.equals("-encoding")) {
                if (i >= args.length) {
                    System.err.println("missing encoding on -encoding");
                    return;
                }
                this.encoding = args[i];
                i++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        App app = new App(args);
        if (args.length >= 2) {
            app.process();
        }
    }

    public void process() throws Exception {
        String lexerName = this.grammarName + "Lexer";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class<? extends Lexer> lexerClass = null;
        try {
            lexerClass = cl.loadClass(lexerName).asSubclass(Lexer.class);
        } catch (java.lang.ClassNotFoundException cnfe) {
            // might be pure lexer grammar; no Lexer suffix then
            lexerName = this.grammarName;
            try {
                lexerClass = cl.loadClass(lexerName).asSubclass(Lexer.class);
            } catch (ClassNotFoundException cnfe2) {
                System.err.println("Can't load " + lexerName + " as lexer or parser");
                return;
            }
        }

        Constructor<? extends Lexer> lexerCtor = lexerClass.getConstructor(CharStream.class);
        Lexer lexer = lexerCtor.newInstance((CharStream) null);

        Class<? extends Parser> parserClass = null;
        Parser parser = null;
        if (!this.startRuleName.equals(App.LEXER_START_RULE_NAME)) {
            String parserName = this.grammarName + "Parser";
            parserClass = cl.loadClass(parserName).asSubclass(Parser.class);
            if (parserClass == null) {
                System.err.println("Can't load " + parserName);
            }
            Constructor<? extends Parser> parserCtor = parserClass
                    .getConstructor(TokenStream.class);

            parser = parserCtor.newInstance((TokenStream) null);
        }

        if (this.inputFiles.size() == 0) {
            InputStream is = System.in;
            Reader r;
            if (this.encoding != null) {
                r = new InputStreamReader(is, this.encoding);
            } else {
                r = new InputStreamReader(is);
            }

            this.process(lexer, parserClass, parser, is, r);
            return;
        }
        for (String inputFile : this.inputFiles) {
            InputStream is = System.in;
            if (inputFile != null) {
                is = new FileInputStream(inputFile);
            }
            Reader r;
            if (this.encoding != null) {
                r = new InputStreamReader(is, this.encoding);
            } else {
                r = new InputStreamReader(is);
            }

            if (this.inputFiles.size() > 1) {
                System.err.println(inputFile);
            }
            this.process(lexer, parserClass, parser, is, r);
        }
    }

    protected void process(Lexer lexer, Class<? extends Parser> parserClass, Parser parser,
            InputStream is, Reader r) throws IOException, IllegalAccessException,
            InvocationTargetException, PrintException {
        try {
            ANTLRInputStream input = new ANTLRInputStream(r);
            lexer.setInputStream(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            tokens.fill();

            if (this.startRuleName.equals(App.LEXER_START_RULE_NAME)) {
                return;
            }

            parser.setBuildParseTree(true);
            parser.setTokenStream(tokens);
            if (this.startRuleName.equals("")){
                this.startRuleName = parser.getRuleNames()[0];
            }

            try {
                Method startRule = parserClass.getMethod(this.startRuleName);
                ParserRuleContext tree = (ParserRuleContext) startRule.invoke(parser,
                        (Object[]) null);

                ParseTreeWalker walker = new ParseTreeWalker();
                XMLListener proxy = new XMLListener();

                System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                walker.walk(proxy, tree);

            } catch (NoSuchMethodException nsme) {
                System.err.println("No method for rule " + this.startRuleName
                        + " or it has arguments");
            }
        } finally {
            if (r != null) {
                r.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
}
