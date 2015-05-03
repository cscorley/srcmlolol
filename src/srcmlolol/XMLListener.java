package srcmlolol;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public class XMLListener implements ParseTreeListener {
    private int depth = 0;

    private void indent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(" ");
        }
        System.out.print(sb.toString());
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        indent();
        depth++;

        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(ctx.getClass().getSimpleName().replace("Context", ""));

        sb.append(" start_line=");
        sb.append(ctx.start.getLine());

        sb.append(" end_line=");
        sb.append(ctx.stop.getLine());

        sb.append(" start_line_char=");
        sb.append(ctx.start.getCharPositionInLine());

        sb.append(" end_line_char=");
        sb.append(ctx.stop.getCharPositionInLine() + ctx.stop.getText().length());

        sb.append(" start_char=");
        sb.append(ctx.start.getStartIndex());

        sb.append(" end_char=");
        sb.append(ctx.stop.getStopIndex());

        sb.append(">");
        System.out.println(sb.toString());
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
        if (!text.equals("<EOF>")) {
            System.out.println(text);
        }
    }

}
