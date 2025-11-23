package com.spike.giantdataanalysis.rdfstore.sql;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser.RootContext;
import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser.SqlStatementsContext;
import com.spike.giantdataanalysis.rdfstore.sql.TestMySqlParser.MyErrorListener;

/**
 * 
 */
public class TestMySqlParserVisitor {
  public static void main(String[] args) throws IOException {
    Path path = Paths.get(TestMySqlParser.all_path);
    CharStream rawCS = CharStreams.fromPath(path);
    CaseChangingCharStream cs = new CaseChangingCharStream(rawCS, true);
    MySqlLexer lexer = new MySqlLexer(cs);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MySqlParser parser = new MySqlParser(tokens);

    parser.setErrorHandler(TestMySqlParser.errorStrategy);
    parser.addErrorListener(new MyErrorListener(path));

    // ParseTree tree = parser.root();
    // System.out.println(tree.toStringTree(parser));
    RootContext rootContext = parser.root();
    SqlStatementsContext stmts = rootContext.sqlStatements();
    for (ParseTree stmt : stmts.children) {
      System.out.println(stmt.toStringTree(parser));
    }

    MySqlParserVisitor<Void> visitor = new SimpleMySqlParserVisitor();
    visitor.visitRoot(rootContext);
  }
}
