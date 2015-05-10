package srcmlolol;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLListener implements ParseTreeListener {
    private Map<Integer, String> token2name;
    private Map<String, Integer> name2token;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Stack<Element> stack;
    public Document doc;

    public XMLListener(String filename, Map<String, Integer> tokenMap) throws ParserConfigurationException{
        stack = new Stack<Element>();

        token2name = new HashMap<Integer, String>();
        name2token = tokenMap;
        for (String key : name2token.keySet()){
            token2name.put(name2token.get(key), key);
        }

        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        doc = builder.newDocument();

        Element unit = doc.createElement("unit");
        unit.setAttribute("filename", filename);

        doc.appendChild(unit);
        stack.add(unit);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        String name = ctx.getClass().getSimpleName().replace("Context", "");
        Element rule = doc.createElement(name);
        rule.setAttribute("start_line", Integer.toString(ctx.start.getLine()));
        rule.setAttribute("start_line_char", Integer.toString(ctx.start.getCharPositionInLine()));
        rule.setAttribute("start_char", Integer.toString(ctx.start.getStartIndex()));

        // occasionally stop isn't set. prefer start token instead.
        if (ctx.stop == null){
            rule.setAttribute("end_line", Integer.toString(ctx.start.getLine()));
            rule.setAttribute("end_line_char", Integer.toString(
                    (ctx.start.getCharPositionInLine() + ctx.start.getText().length())));
            rule.setAttribute("end_char", Integer.toString(ctx.start.getStopIndex()));
        }
        else{
            rule.setAttribute("end_line", Integer.toString(ctx.stop.getLine()));
            rule.setAttribute("end_line_char", Integer.toString(
                    (ctx.stop.getCharPositionInLine() + ctx.stop.getText().length())));
            rule.setAttribute("end_char", Integer.toString(ctx.stop.getStopIndex()));
        }

        // attaching to last elem on stack
        stack.peek().appendChild(rule);
        stack.add(rule);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        // oh no!!
        stack.pop();
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        String text = node.getText();
        if (text.equals("<EOF>")) {
            return;
        }
        Token token = node.getSymbol();

        Element tokenelem = doc.createElement(token.getClass().getSimpleName());
        int type = token.getType();
        tokenelem.setAttribute("type", Integer.toString(type));
        tokenelem.setAttribute("name", token2name.get(type));
        tokenelem.appendChild(doc.createTextNode(StringEscapeUtils.escapeXml10(text)));
        tokenelem.setAttribute("start_line", Integer.toString(token.getLine()));
        tokenelem.setAttribute("end_line", Integer.toString(token.getLine()));
        tokenelem.setAttribute("start_line_char", Integer.toString(token.getCharPositionInLine()));
        tokenelem.setAttribute("end_line_char", Integer.toString(
                (token.getCharPositionInLine() + token.getText().length())));
        tokenelem.setAttribute("start_char", Integer.toString(token.getStartIndex()));
        tokenelem.setAttribute("end_char", Integer.toString(token.getStopIndex()));
        stack.peek().appendChild(tokenelem);
    }
}
