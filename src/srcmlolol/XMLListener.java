package srcmlolol;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class XMLListener implements ParseTreeListener {

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println("<" + ctx.getClass().getCanonicalName() + ">");
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println("</" + ctx.getClass().getCanonicalName() + ">" );
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        System.out.println(node.getText());
    }

}
