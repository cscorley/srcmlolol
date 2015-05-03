package srcmlolol;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class XMLListener implements ParseTreeListener {
    private int depth = 0;

    private void indent(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++){
            sb.append(" ");
        }
        System.out.print(sb.toString());
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        indent();
        String name = ctx.getClass().getSimpleName().replace("Context", "");
        System.out.println("<" + name + ">");
        depth++;
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        depth--;
        indent();
        String name = ctx.getClass().getSimpleName().replace("Context", "");
        System.out.println("</" + name + ">");
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        indent();
        String text = node.getText();
        if (text.equals("<EOF>")){
            System.out.println("<EOF />");
        }
        else{
            System.out.println(text);
        }
    }

}
