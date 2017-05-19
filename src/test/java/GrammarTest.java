package kc2tei;


import kc2tei.parser.*;
import kc2tei.lexer.*;
import kc2tei.node.*;

import java.io.*;

import org.junit.*;

public class GrammarTest {

  private Start lexParse(File file) throws Exception {

      // create lexer
      Lexer l = new Lexer(new PushbackReader(new FileReader(file), 1024));

      // print tokens
      while (true) {
        Token t = l.next();
        if (t instanceof EOF) {
          break;
        }
        System.out.println(t.getClass() + " " + t);
      }

      // create new lexer with new file reader which reads from beginning of the file again
      l = new Lexer(new PushbackReader(new FileReader(file), 1024));

      // create parser
      Parser p = new Parser(l);

      // return parsed content
      return p.parse();
    }

  @Test
  public void testLexParse() throws Exception {
    Start tree = lexParse(new File(getClass().getResource("/sample_kc_file.s1h").getFile()));
    Assert.assertNotNull(tree);
  }
}

