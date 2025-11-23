package com.spike.giantdataanalysis.rdfstore.sparql;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class TestSPARQLSampleQuery {

  public static String TEST_QUERY_DIR = "src/test/resources/sparql";
  private static int index = 1;
  private static final Map<Path, String> errorMsgCollector = Maps.newHashMap();

  public static void main(String[] args) throws Exception {
    Path testQueryFileRootDir = Paths.get(TEST_QUERY_DIR);
    FileVisitor<Path> visitor = new FileVisitor<Path>() {

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        parse(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }
    };
    Files.walkFileTree(testQueryFileRootDir, visitor);

    for (Path path : errorMsgCollector.keySet()) {
      System.err.println(path + " => " + errorMsgCollector.get(path));
    }
  }

  private static void parse(Path path) throws IOException {
    System.out.println("Parse: [" + Strings.padStart(String.valueOf(index), 3, '0') + "]" + path);

    ANTLRErrorStrategy errorStrategy = new DefaultErrorStrategy() {
    };

    class MyErrorListener extends BaseErrorListener {
      private Path path;

      public MyErrorListener(Path path) {
        this.path = path;
      }

      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
          int charPositionInLine, String msg, RecognitionException e) {
        errorMsgCollector.put(path, msg);
      }
    }
    ;

    CharStream cs = CharStreams.fromPath(path);
    SPARQLQueryLexer lexer = new SPARQLQueryLexer(cs);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    SPARQLQueryParser parser = new SPARQLQueryParser(tokens);

    parser.setErrorHandler(errorStrategy);
    parser.addErrorListener(new MyErrorListener(path));

    ParseTree tree = parser.queryUnit();
    System.out.println(tree.toStringTree(parser));
    // System.out.println();
    index++;
  }
}
