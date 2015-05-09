package srcmlolol;

import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLListener implements ParseTreeListener {
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Stack<Element> stack;
    public Document doc;

    public XMLListener(String filename) throws ParserConfigurationException{
        stack = new Stack<Element>();

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
        rule.setAttribute("end_line", Integer.toString(ctx.stop.getLine()));
        rule.setAttribute("start_line_char", Integer.toString(ctx.start.getCharPositionInLine()));
        rule.setAttribute("end_line_char", Integer.toString(
                (ctx.stop.getCharPositionInLine() + ctx.stop.getText().length())));
        rule.setAttribute("start_char", Integer.toString(ctx.start.getStartIndex()));
        rule.setAttribute("end_char", Integer.toString(ctx.stop.getStopIndex()));

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
        if (stack.peek().hasChildNodes()){
            NodeList children = stack.peek().getChildNodes();
            Node lastchild = children.item(children.getLength() - 1);
            if (lastchild.getNodeType() == Node.TEXT_NODE){
                text = " " + text;
            }
        }
        stack.peek().appendChild(doc.createTextNode(StringEscapeUtils.escapeXml10(text)));
    }
}
