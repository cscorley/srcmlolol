package srcmlolol;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class XMLListener implements ParseTreeListener {

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println("HI " + ctx.getClass().getCanonicalName() + " " + ctx.getText() );
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println("BYE " + ctx.getClass().getCanonicalName() + " " + ctx.getText() );
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
    }

    @Override
    public void visitTerminal(TerminalNode node) {
    }

}
