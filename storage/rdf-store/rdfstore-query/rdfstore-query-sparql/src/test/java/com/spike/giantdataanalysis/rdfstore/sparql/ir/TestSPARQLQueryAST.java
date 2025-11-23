package com.spike.giantdataanalysis.rdfstore.sparql.ir;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import com.spike.giantdataanalysis.rdfstore.sparql.SPARQLQueryLexer;
import com.spike.giantdataanalysis.rdfstore.sparql.SPARQLQueryParser;
import com.spike.giantdataanalysis.rdfstore.sparql.ast.SPARQLQueryAST;

public class TestSPARQLQueryAST {
  public static String TEST_QUERY_DIR = "src/test/resources/sparql";

  @Test
  public void testVisitor() throws IOException {
    Path path = Paths.get(TEST_QUERY_DIR, "simple-query/simple-query.query");
    CharStream cs = CharStreams.fromPath(path);
    SPARQLQueryLexer lexer = new SPARQLQueryLexer(cs);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    SPARQLQueryParser parser = new SPARQLQueryParser(tokens);

    ParseTree tree = parser.queryUnit();
    System.out.println(tree.toStringTree(parser));
    SPARQLQueryAST ast = new SPARQLQueryAST(parser);
    ast.visit(tree);
  }
}
