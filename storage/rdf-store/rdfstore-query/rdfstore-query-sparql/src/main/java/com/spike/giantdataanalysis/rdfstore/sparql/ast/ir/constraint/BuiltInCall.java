package com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.constraint;

import com.spike.giantdataanalysis.rdfstore.sparql.ast.ir.term.Var;

/**
 * <pre>
[121]   BuiltInCall   ::=     Aggregate
| 'STR' '(' Expression ')'
| 'LANG' '(' Expression ')'
| 'LANGMATCHES' '(' Expression ',' Expression ')'
| 'DATATYPE' '(' Expression ')'
| 'BOUND' '(' Var ')'
| 'IRI' '(' Expression ')'
| 'URI' '(' Expression ')'
| 'BNODE' ( '(' Expression ')' | NIL )
| 'RAND' NIL
| 'ABS' '(' Expression ')'
| 'CEIL' '(' Expression ')'
| 'FLOOR' '(' Expression ')'
| 'ROUND' '(' Expression ')'
| 'CONCAT' ExpressionList
| SubstringExpression
| 'STRLEN' '(' Expression ')'
| StrReplaceExpression
| 'UCASE' '(' Expression ')'
| 'LCASE' '(' Expression ')'
| 'ENCODE_FOR_URI' '(' Expression ')'
| 'CONTAINS' '(' Expression ',' Expression ')'
| 'STRSTARTS' '(' Expression ',' Expression ')'
| 'STRENDS' '(' Expression ',' Expression ')'
| 'STRBEFORE' '(' Expression ',' Expression ')'
| 'STRAFTER' '(' Expression ',' Expression ')'
| 'YEAR' '(' Expression ')'
| 'MONTH' '(' Expression ')'
| 'DAY' '(' Expression ')'
| 'HOURS' '(' Expression ')'
| 'MINUTES' '(' Expression ')'
| 'SECONDS' '(' Expression ')'
| 'TIMEZONE' '(' Expression ')'
| 'TZ' '(' Expression ')'
| 'NOW' NIL
| 'UUID' NIL
| 'STRUUID' NIL
| 'MD5' '(' Expression ')'
| 'SHA1' '(' Expression ')'
| 'SHA256' '(' Expression ')'
| 'SHA384' '(' Expression ')'
| 'SHA512' '(' Expression ')'
| 'COALESCE' ExpressionList
| 'IF' '(' Expression ',' Expression ',' Expression ')'
| 'STRLANG' '(' Expression ',' Expression ')'
| 'STRDT' '(' Expression ',' Expression ')'
| 'sameTerm' '(' Expression ',' Expression ')'
| 'isIRI' '(' Expression ')'
| 'isURI' '(' Expression ')'
| 'isBLANK' '(' Expression ')'
| 'isLITERAL' '(' Expression ')'
| 'isNUMERIC' '(' Expression ')'
| RegexExpression
| ExistsFunc
| NotExistsFunc
 * </pre>
 */
public class BuiltInCall {
  public enum BuiltInCallTypeEnum {
    Aggregate, STR, LANG, LANGMATCHES, DATATYPE, BOUND, IRI, URI, BNODE, RAND, ABS, CEIL, FLOOR,
    ROUND, CONCAT, SubstringExpression, STRLEN, StrReplaceExpression, UCASE, LCASE, ENCODE_FOR_URI,
    CONTAINS, STRSTARTS, STRENDS, STRBEFORE, STRAFTER, YEAR, MONTH, DAY, HOURS, MINUTES, SECONDS,
    TIMEZONE, TZ, NOW, UUID, STRUUID, MD5, SHA1, SHA256, SHA384, SHA512, COALESCE, IF, STRLANG,
    STRDT, sameTerm, isIRI, isURI, isBLANK, isLITERAL, isNUMERIC, RegexExpression, ExistsFunc,
    NotExistsFunc
  }

  public BuiltInCallTypeEnum type;

  public Aggregate aggregate;
  public SubstringExpression substringExpression;
  public StrReplaceExpression strReplaceExpression;
  public RegexExpression regexExpression;
  public ExistsFunc existsFunc;
  public NotExistsFunc notExistsFunc;

  public Expression expression;
  public Expression expression2;
  public Expression expression3;
  public Var var;
  public boolean isNil = false;
  public ExpressionList expressionList;

}
