// References:
// SPARQL 1.1 Query: https://www.w3.org/TR/sparql11-query/#grammar
// XML 1.1 Notation: https://www.w3.org/TR/2004/REC-xml11-20040204/#sec-notation
grammar SPARQLQuery;

//---------------------------------------------------------------------------
// 1 parser grammar
//---------------------------------------------------------------------------
//[1] 查询单元
queryUnit: query;
//[2] 查询语句
query:  prologue
        ( selectQuery | constructQuery | describeQuery | askQuery )
        valuesClause;

//[3] 更新单元
updateUnit: update;

//[4] 序言: BASE, PREFIX
prologue: 	( baseDecl | prefixDecl )*;
//[5] BASE声明
baseDecl: K_BASE IRIREF;
//[6] PREFIX声明
prefixDecl: K_PREFIX PNAME_NS IRIREF;

//[7] SELECT查询
selectQuery: selectClause datasetClause* whereClause solutionModifier;
//[8] 子SELECT
subSelect: selectClause whereClause solutionModifier valuesClause;
//[9] SELECT子句
selectClause: K_SELECT ( K_DISTINCT | K_REDUCED )? ( ( var | ( '(' expression K_AS var ')' ) )+ | '*' );

//[10] CONSTRUCT查询
constructQuery: K_CONSTRUCT ( constructTemplate datasetClause* whereClause solutionModifier
                            | datasetClause* K_WHERE '{' triplesTemplate? '}' solutionModifier
                            );

//[11] DESCRIBE查询
describeQuery: K_DESCRIBE ( varOrIri+ | '*' ) datasetClause* whereClause? solutionModifier;

//[12] ASK查询
askQuery: K_ASK datasetClause* whereClause solutionModifier;

//[13] 数据集子句
datasetClause: K_FROM ( defaultGraphClause | namedGraphClause );
//[14] 默认图子句
defaultGraphClause: sourceSelector;
//[15] 命名图子句
namedGraphClause: K_NAMED sourceSelector;
//[16] 源选择器
sourceSelector: iri;
//[17] WHERE子句
whereClause: K_WHERE? groupGraphPattern;
//[18] 答案修饰符
solutionModifier: groupClause? havingClause? orderClause? limitOffsetClauses?;
//[19] GRROUP BY子句
groupClause: K_GROUP K_BY groupCondition+;
//[20] GROUP BY条件
groupCondition: builtInCall
                | functionCall
                | '(' expression ( K_AS var )? ')'
                | var;
//[21] HAVING子句
havingClause: K_HAVING havingCondition+;
//[22] HAVING条件
havingCondition: constraint;
//[23] ORDER BY子句
orderClause: K_ORDER K_BY orderCondition+;
//[24] ORDER BY条件
orderCondition: ( ( K_ASC | K_DESC ) brackettedExpression )
                | ( constraint | var );
//[25] LIMIT/OFFSET子句
limitOffsetClauses: limitClause offsetClause?
                    | offsetClause limitClause?;
//[26] LIMIT子句
limitClause: K_LIMIT INTEGER;
//[27] OFFSET子句
offsetClause: K_OFFSET INTEGER;
//[28] VALUES子句
valuesClause: ( K_VALUES dataBlock )?;

//[29] 更新语句
update: prologue ( update1 ( ';' update )? )?;
//[30] 具体更新语句
update1: load
         | clear
         | drop
         | add
         | move
         | copy
         | create
         | insertData
         | deleteData
         | deleteWhere
         | modify;
//[31] 加载
load: K_LOAD K_SILENT? iri ( K_INTO graphRef )?;
//[32] 清空图
clear: K_CLEAR K_SILENT? graphRefAll;
//[33] 删除图
drop: K_DROP K_SILENT? graphRefAll;
//[34] 创建图
create: K_CREATE K_SILENT? graphRef;
//[35] 添加图
add: K_ADD K_SILENT? graphOrDefault K_TO graphOrDefault;
//[36] 移动图
move: K_MOVE K_SILENT? graphOrDefault K_TO graphOrDefault;
//[37] 拷贝图
copy: K_COPY K_SILENT? graphOrDefault K_TO graphOrDefault;
//[38] 插入数据
insertData: K_INSERT K_DATA quadData;
//[39] 删除数据
deleteData: K_DELETE K_DATA quadData;
//[40] 按条件删除
deleteWhere: K_DELETE K_WHERE quadPattern;
//[41] 修改
modify: ( K_WITH iri )? ( deleteClause insertClause? | insertClause ) usingClause* K_WHERE groupGraphPattern;
//[42] 删除子句
deleteClause: K_DELETE quadPattern;
//[43] 插入子句
insertClause: K_INSERT quadPattern;
//[44] USING子句
usingClause: K_USING ( iri | K_NAMED iri );
//[45] 图声明: DEFAULT或GRAPH
graphOrDefault: K_DEFAULT | K_GRAPH? iri;
//[46] 图引用
graphRef: K_GRAPH iri;
//[47] 所有图引用: 图引用, default, named, all
graphRefAll: graphRef | K_DEFAULT | K_NAMED | K_ALL;
//[48] triple模式的集合
quadPattern: '{' quads '}';
//[49] 不带变量的triple模式的集合
quadData: '{' quads '}';
//[50] triple集合
quads: triplesTemplate? ( quadsNotTriples '.'? triplesTemplate? )*;
//[51] 非triple的triple集合: GRAPH
quadsNotTriples: K_GRAPH varOrIri '{' triplesTemplate? '}';
//[52] triple模板
triplesTemplate: triplesSameSubject ( '.' triplesTemplate? )?;
//[53] 分组图模式: 可能有复杂约束的一组triple
groupGraphPattern: '{' ( subSelect | groupGraphPatternSub ) '}';
//[54] 子分组图模式
groupGraphPatternSub: triplesBlock? ( graphPatternNotTriples '.'? triplesBlock? )*;
//[55] triple块
triplesBlock: triplesSameSubjectPath ( '.' triplesBlock? )?;
//[56] 非triple的图模式
graphPatternNotTriples: groupOrUnionGraphPattern
                        | optionalGraphPattern
                        | minusGraphPattern
                        | graphGraphPattern
                        | serviceGraphPattern
                        | filter
                        | bind
                        | inlineData;
//[57] 可选图模式: OPTIONAL
optionalGraphPattern: K_OPTIONAL groupGraphPattern;
//[58] 图的图模式: GRAPH
graphGraphPattern: K_GRAPH varOrIri groupGraphPattern;
//[59] 服务的图模式: SERVICE
serviceGraphPattern: K_SERVICE K_SILENT? varOrIri groupGraphPattern;
//[60] 绑定表达式到变量: BIND
bind: K_BIND '(' expression K_AS var ')';
//[61] 行内数据: VALUES
inlineData: K_VALUES dataBlock;
//[62] 数据块
dataBlock: inlineDataOneVar | inlineDataFull;
//[63] 单个变量的内联数据
inlineDataOneVar: var '{' dataBlockValue* '}';
//[64] 完整的内联数据
inlineDataFull: ( NIL | '(' var* ')' ) '{' ( '(' dataBlockValue* ')' | NIL )* '}';
//[65] 数据块值
dataBlockValue: iri
                |	rDFLiteral
                |	numericLiteral
                |	booleanLiteral
                |	K_UNDEF;
//[66] MINUS图模式
minusGraphPattern: K_MINUS groupGraphPattern;
//[67] UNION图模式
groupOrUnionGraphPattern: groupGraphPattern ( K_UNION groupGraphPattern )*;
//[68] 过滤器: FILTER
filter: K_FILTER constraint;
//[69] 约束
constraint: brackettedExpression
            | builtInCall
            | functionCall;
//[70] 函数调用
functionCall: iri argList;
//[71] 参数列表
argList: NIL
        | '(' K_DISTINCT? expression ( ',' expression )* ')';
//[72] 表达式列表
expressionList: NIL
                | '(' expression ( ',' expression )* ')';
//[73] CONSTRUCT模板
constructTemplate: '{' constructTriples? '}';
//[74] CONSTRUCT triple
constructTriples: triplesSameSubject ( '.' constructTriples? )?;
//[75] 同一Subject的triple
triplesSameSubject: varOrTerm propertyListNotEmpty
                    | triplesNode propertyList;
//[76] 属性列表
propertyList: propertyListNotEmpty?;
//[77] 非空的属性列表
propertyListNotEmpty: verb objectList ( ';' ( verb objectList )? )*;
//[78] Verb
verb: varOrIri | K_A;
//[79] Object列表
objectList: object ( ',' object )*;
//[80] Object
object: graphNode;
//[81] 同一Subject路径的triple
triplesSameSubjectPath: varOrTerm propertyListPathNotEmpty
                        | triplesNodePath propertyListPath;
//[82] 属性列表路径
propertyListPath: propertyListPathNotEmpty?;
//[83] 非空属性列表路径
propertyListPathNotEmpty: ( verbPath | verbSimple ) objectListPath ( ';' ( ( verbPath | verbSimple ) objectList )? )*;
//[84] Verb路径
verbPath: path;
//[85] 简单Verb
verbSimple: var;
//[86] Object列表路径
objectListPath: objectPath ( ',' objectPath )*;
//[87] Object路径
objectPath: graphNodePath;
//[88] 路径
path: pathAlternative;
//[89] 路径备选
pathAlternative: pathSequence ( '|' pathSequence )*;
//[90] 路径序列
pathSequence: pathEltOrInverse ( '/' pathEltOrInverse )*;
//[91] 路径元素
pathElt: pathPrimary pathMod?;
//[92] 路径元素或逆路径元素
pathEltOrInverse: pathElt
                  | '^' pathElt;
//[93] 路径模式
pathMod: '?'
         | '*'
         | '+';
//[94] 基本路径
pathPrimary: iri
             | K_A
             | '!' pathNegatedPropertySet
             | '(' path ')';
//[95] 路径补属性集
pathNegatedPropertySet: pathOneInPropertySet
                        | '(' ( pathOneInPropertySet ( '|' pathOneInPropertySet )* )? ')';
//[96] 属性集中长度为1的路径
pathOneInPropertySet: iri
                      | K_A
                      | '^' ( iri | K_A );
//[97] 整数
integer: INTEGER;
//[98] triple节点
triplesNode: collection
             |	blankNodePropertyList;
//[99] 空节点属性列表
blankNodePropertyList: '[' propertyListNotEmpty ']';
//[100] triple节点路径
triplesNodePath: collectionPath |	blankNodePropertyListPath;
//[101] 空节点属性列表路径
blankNodePropertyListPath: '[' propertyListPathNotEmpty ']';
//[102] 图节点集合
collection: '(' graphNode+ ')';
//[103] 图节点路径集合
collectionPath: '(' graphNodePath+ ')';
//[104] 图节点
graphNode: varOrTerm
           | triplesNode;
//[105] 图节点路径
graphNodePath: varOrTerm
               | triplesNodePath;
//[106] 变量或图项
varOrTerm: var
           | graphTerm;
//[107] 变量或IRI
varOrIri: var | iri;
//[108] 变量: $var, ?var
var: VAR1
     | VAR2;
//[109] 图项
graphTerm: iri
           | rDFLiteral
           | numericLiteral
           | booleanLiteral
           | blankNode
           | NIL;
//[110] 表达式
expression: conditionalOrExpression;
//[111] 或条件表达式: OR
conditionalOrExpression: conditionalAndExpression ( K_OR conditionalAndExpression )*;
//[112] 与条件表达式: AND
conditionalAndExpression: valueLogical ( K_AND valueLogical )*;
//[113] 值逻辑
valueLogical: relationalExpression;
//[114] 关系表达式: =, !=, <, >, <=, >=, IN, NOT IN
relationalExpression: numericExpression ( K_EQ numericExpression
                                        | K_NEQ numericExpression
                                        | K_LT numericExpression
                                        | K_GT numericExpression
                                        | K_LTE numericExpression
                                        | K_GTE numericExpression
                                        | K_IN expressionList
                                        | K_NOT K_IN expressionList
                                        )?;
//[115] 数值表达式
numericExpression: additiveExpression;
//[116] 可加的数值表达式
additiveExpression: multiplicativeExpression ( '+' multiplicativeExpression
                                             | '-' multiplicativeExpression
                                             | ( numericLiteralPositive | numericLiteralNegative ) ((('*' | '/') unaryExpression ))*
                                             )*;
//[117] 可乘的数值表达式
multiplicativeExpression: unaryExpression ( (('*'|'/') unaryExpression) )*;
//[118] 一元表达式
unaryExpression: '!' primaryExpression
                 | '+' primaryExpression
                 | '-' primaryExpression
                 | primaryExpression;
//[119] 原始表达式
primaryExpression: brackettedExpression
                   | numericLiteral
                   | booleanLiteral
                   | rDFLiteral
                   |  builtInCall
                   | iriOrFunction
                   | var;
//[120] 带括号的表达式
brackettedExpression: '(' expression ')';
//[121] 内建调用
builtInCall: aggregate
	        | K_STR '(' expression ')'
	        | K_LANG '(' expression ')'
	        | K_LANGMATCHES '(' expression ',' expression ')'
	        | K_DATATYPE '(' expression ')'
	        | K_BOUND '(' var ')'
	        | K_IRI '(' expression ')'
	        | K_URI '(' expression ')'
	        | K_BNODE ( '(' expression ')' | NIL )
	        | K_RAND NIL
	        | K_ABS '(' expression ')'
	        | K_CEIL '(' expression ')'
	        | K_FLOOR '(' expression ')'
	        | K_ROUND '(' expression ')'
	        | K_CONCAT expressionList
	        | substringExpression
	        | K_STRLEN '(' expression ')'
	        | strReplaceExpression
	        | K_UCASE '(' expression ')'
	        | K_LCASE '(' expression ')'
	        | K_ENCODE_FOR_URI '(' expression ')'
	        | K_CONTAINS '(' expression ',' expression ')'
	        | K_STRSTARTS '(' expression ',' expression ')'
	        | K_STRENDS '(' expression ',' expression ')'
	        | K_STRBEFORE '(' expression ',' expression ')'
	        | K_STRAFTER '(' expression ',' expression ')'
	        | K_YEAR '(' expression ')'
	        | K_MONTH '(' expression ')'
	        | K_DAY '(' expression ')'
	        | K_HOURS '(' expression ')'
	        | K_MINUTES '(' expression ')'
	        | K_SECONDS '(' expression ')'
	        | K_TIMEZONE '(' expression ')'
	        | K_TZ '(' expression ')'
	        | K_NOW NIL
	        | K_UUID NIL
	        | K_STRUUID NIL
	        | K_MD5 '(' expression ')'
	        | K_SHA1 '(' expression ')'
	        | K_SHA256 '(' expression ')'
	        | K_SHA384 '(' expression ')'
	        | K_SHA512 '(' expression ')'
	        | K_COALESCE expressionList
	        | K_IF '(' expression ',' expression ',' expression ')'
	        | K_STRLANG '(' expression ',' expression ')'
	        | K_STRDT '(' expression ',' expression ')'
	        | K_sameTerm '(' expression ',' expression ')'
	        | K_isIRI '(' expression ')'
	        | K_isURI '(' expression ')'
	        | K_isBLANK '(' expression ')'
	        | K_isLITERAL '(' expression ')'
	        | K_isNUMERIC '(' expression ')'
	        | regexExpression
	        | existsFunc
	        | notExistsFunc;
//[122] 正则表达式: REGEX
regexExpression: K_REGEX '(' expression ',' expression ( ',' expression )? ')';
//[123] 字符串子串表达式: SUBSTR
substringExpression: K_SUBSTR '(' expression ',' expression ( ',' expression )? ')';
//[124] 字符串替换表达式: REPLACE
strReplaceExpression: K_REPLACE '(' expression ',' expression ',' expression ( ',' expression )? ')';
//[125] EXIST函数
existsFunc: K_EXISTS groupGraphPattern;
//[126] NOT EXISTS函数
notExistsFunc: K_NOT K_EXISTS groupGraphPattern;
//[127] 内建聚合调用: COUNT, SUM, MIN, MAX. AVG, SAMPLE, GROUP_CONCAT
aggregate: K_COUNT '(' K_DISTINCT? ( '*' | expression ) ')'
          | K_SUM '(' K_DISTINCT? expression ')'
          | K_MIN '(' K_DISTINCT? expression ')'
          | K_MAX '(' K_DISTINCT? expression ')'
          | K_AVG '(' K_DISTINCT? expression ')'
          | K_SAMPLE '(' K_DISTINCT? expression ')'
          | K_GROUP_CONCAT '(' K_DISTINCT? expression ( ';' K_SEPARATOR '=' string )? ')';
//[128] IRI或函数
iriOrFunction: iri argList?;
//[129] RDF字面量
rDFLiteral: string ( LANGTAG | ( '^^' iri ) )?;
//[130] 数值字面量
numericLiteral: numericLiteralUnsigned
                | numericLiteralPositive
                | numericLiteralNegative;
//[131] 无符号数值字面量
numericLiteralUnsigned: INTEGER
                        | DECIMAL
                        | DOUBLE;
//[132] 正数值字面量
numericLiteralPositive: INTEGER_POSITIVE
                        | DECIMAL_POSITIVE
                        | DOUBLE_POSITIVE;
//[133] 负数值字面量
numericLiteralNegative: INTEGER_NEGATIVE
                        | DECIMAL_NEGATIVE
                        | DOUBLE_NEGATIVE;
//[134] 布尔字面量: TRUE, FALSE
booleanLiteral: K_true
                | K_false;
//[135] 字符串
string: STRING_LITERAL1
        | STRING_LITERAL2
        | STRING_LITERAL_LONG1
        | STRING_LITERAL_LONG2;
//[136] IRI
iri: IRIREF
     | prefixedName;
//[137] 带前缀的名字
prefixedName: PNAME_LN
              | PNAME_NS;
//[138] 空节点
blankNode: BLANK_NODE_LABEL
           | ANON;

//---------------------------------------------------------------------------
// 2 lexer grammar
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
// KEYWORDS
// REF: https://github.com/antlr/antlr4/blob/master/doc/case-insensitive-lexing.md
//---------------------------------------------------------------------------

fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

K_OR: '||';
K_AND: '&&';
K_EQ: '=';
K_NEQ: '!=';
K_LT: '<';
K_GT: '>';
K_LTE: '<=';
K_GTE: '>=';
K_ABS: A B S  ;
K_ADD: A D D  ;
K_ALL: A L L  ;
K_AS: A S  ;
K_ASC: A S C  ;
K_ASK: A S K  ;
K_AVG: A V G  ;
K_BASE: B A S E  ;
K_BIND: B I N D  ;
K_BNODE: B N O D E  ;
K_BOUND: B O U N D  ;
K_BY: B Y  ;
K_CEIL: C E I L  ;
K_CLEAR: C L E A R  ;
K_COALESCE: C O A L E S C E  ;
K_CONCAT: C O N C A T  ;
K_CONSTRUCT: C O N S T R U C T  ;
K_CONTAINS: C O N T A I N S  ;
K_COPY: C O P Y  ;
K_COUNT: C O U N T  ;
K_CREATE: C R E A T E  ;
K_DATATYPE: D A T A T Y P E  ;
K_DAY: D A Y  ;
K_DEFAULT: D E F A U L T  ;
K_DELETE: D E L E T E  ;
K_DESC: D E S C  ;
K_DESCRIBE: D E S C R I B E  ;
K_DISTINCT: D I S T I N C T  ;
K_DROP: D R O P  ;
K_ENCODE_FOR_URI: E N C O D E '_' F O R '_' U R I  ;
K_EXISTS: E X I S T S  ;
K_FILTER: F I L T E R  ;
K_FLOOR: F L O O R  ;
K_FROM: F R O M  ;
K_GRAPH: G R A P H  ;
K_GROUP: G R O U P  ;
K_GROUP_CONCAT: G R O U P '_' C O N C A T  ;
K_HAVING: H A V I N G  ;
K_HOURS: H O U R S  ;
K_IF: I F  ;
K_IN: I N  ;
K_INSERT: I N S E R T  ;
K_INTO: I N T O  ;
K_IRI: I R I  ;
K_LANG: L A N G  ;
K_LANGMATCHES: L A N G M A T C H E S  ;
K_LCASE: L C A S E  ;
K_LIMIT: L I M I T  ;
K_LOAD: L O A D  ;
K_MAX: M A X  ;
K_MD5: M D '5'  ;
K_MIN: M I N  ;
K_MINUS: M I N U S  ;
K_MINUTES: M I N U T E S  ;
K_MONTH: M O N T H  ;
K_MOVE: M O V E  ;
K_NAMED: N A M E D  ;
K_NOT: N O T  ;
K_NOW: N O W  ;
K_OFFSET: O F F S E T  ;
K_OPTIONAL: O P T I O N A L  ;
K_ORDER: O R D E R  ;
K_PREFIX: P R E F I X  ;
K_RAND: R A N D  ;
K_REDUCED: R E D U C E D  ;
K_REGEX: R E G E X  ;
K_REPLACE: R E P L A C E  ;
K_ROUND: R O U N D  ;
K_SAMPLE: S A M P L E  ;
K_SECONDS: S E C O N D S  ;
K_SELECT: S E L E C T  ;
K_SEPARATOR: S E P A R A T O R  ;
K_SERVICE: S E R V I C E  ;
K_SHA1: S H A '1'  ;
K_SHA256: S H A '256'  ;
K_SHA384: S H A '384'  ;
K_SHA512: S H A '512'  ;
K_SILENT: S I L E N T  ;
K_STR: S T R  ;
K_STRAFTER: S T R A F T E R  ;
K_STRBEFORE: S T R B E F O R E  ;
K_STRDT: S T R D T  ;
K_STRENDS: S T R E N D S  ;
K_STRLANG: S T R L A N G  ;
K_STRLEN: S T R L E N  ;
K_STRSTARTS: S T R S T A R T S  ;
K_STRUUID: S T R U U I D  ;
K_SUBSTR: S U B S T R  ;
K_SUM: S U M  ;
K_TIMEZONE: T I M E Z O N E  ;
K_TO: T O  ;
K_TZ: T Z  ;
K_UCASE: U C A S E  ;
K_UNDEF: U N D E F  ;
K_UNION: U N I O N  ;
K_URI: U R I  ;
K_USING: U S I N G  ;
K_UUID: U U I D  ;
K_VALUES: V A L U E S  ;
K_WHERE: W H E R E  ;
K_WITH: W I T H  ;
K_YEAR: Y E A R  ;
K_A: A  ;
K_false: F A L S E  ;
K_isBLANK: I S B L A N K  ;
K_isIRI: I S I R I  ;
K_isLITERAL: I S L I T E R A L  ;
K_isNUMERIC: I S N U M E R I C  ;
K_isURI: I S U R I  ;
K_sameTerm: S A M E T E R M  ;
K_true: T R U E  ;
K_DATA: D A T A;

// 注释
COMMENT: '#' ~[\r\n]* -> skip;

//---------------------------------------------------------------------------
// Productions for terminals
//---------------------------------------------------------------------------

//[139] IRI引用
//IRIREF::=  	'<' ([^<>"{}|^`\]-[#x00-#x20])* '>'
IRIREF: '<' (
              ~( '<' | '>' | '"' | '{' | '}' | '|' | '^' | '\\' | '`' | '\u0000'..'\u0020' )
            )*
        '>';
//[140] PREFIX名称
PNAME_NS: PN_PREFIX? ':';
//[141] PREFIX本地名称
PNAME_LN: PNAME_NS PN_LOCAL;
//[142] 空节点标签
BLANK_NODE_LABEL: '_:' ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?;
//[143] ?变量
VAR1: '?' VARNAME;
//[144] $变量
VAR2: '$' VARNAME;
//[145] 语言标记
LANGTAG: '@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*;
//[146] 整数
INTEGER: [0-9]+;
//[147] 小数
DECIMAL: [0-9]* '.' [0-9]+;
//[148] 双精度小数
DOUBLE: [0-9]+ '.' [0-9]* EXPONENT | '.' ([0-9])+ EXPONENT | ([0-9])+ EXPONENT;
//[149] 正整数
INTEGER_POSITIVE: '+' INTEGER;
//[150] 正小数
DECIMAL_POSITIVE: '+' DECIMAL;
//[151] 正精度小数
DOUBLE_POSITIVE: '+' DOUBLE;
//[152] 负整数
INTEGER_NEGATIVE: '-' INTEGER;
//[153] 负小数
DECIMAL_NEGATIVE: '-' DECIMAL;
//[154] 负双精度小数
DOUBLE_NEGATIVE: '-' DOUBLE;
//[155] 指数
EXPONENT: [eE] [+-]? [0-9]+;
//[156] 单引号字符串
//STRING_LITERAL1::=  	"'" ( ([^#x27#x5C#xA#xD]) | ECHAR )* "'"
STRING_LITERAL1: '\'' ( ~('\u0027' | '\u005C' | '\u000A' | '\u000D') | ECHAR )* '\'';
//[157] 双引号字符串
//	STRING_LITERAL2::=  	'"' ( ([^#x22#x5C#xA#xD]) | ECHAR )* '"'
STRING_LITERAL2: '"'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D') | ECHAR )* '"';
//[158] 三单引用字符串
//STRING_LITERAL_LONG1::=  	"'''" ( ( "'" | "''" )? ( [^'\] | ECHAR ) )* "'''"
STRING_LITERAL_LONG1: '\'\'\'' ( ( '\'' | '\'\'' )? ( [^'\\] | ECHAR ) )* '\'\'\'';
//[159] 三双引号字符串
STRING_LITERAL_LONG2: '"""' ( ( '"' | '""' )? ( [^"\\] | ECHAR ) )* '"""';
//[160] 转义字符
//ECHAR::=  	'\' [tbnrf\"']
ECHAR: '\\' [tbnrf\\"'];
//[161] ()
NIL: '(' WS* ')';
//[162] 空白
//WS::=  	#x20 | #x9 | #xD | #xA
WS: [ \r\t\n]+ -> skip;
NON_SKIP_WS: [ \r\t\n]+;
//[163] []
ANON: '[' WS* ']';
//[164] ???
//PN_CHARS_BASE::=  	[A-Z]
// | [a-z]
// | [#x00C0-#x00D6]
// | [#x00D8-#x00F6]
// | [#x00F8-#x02FF]
// | [#x0370-#x037D]
// | [#x037F-#x1FFF]
// | [#x200C-#x200D]
// | [#x2070-#x218F]
// | [#x2C00-#x2FEF]
// | [#x3001-#xD7FF]
// | [#xF900-#xFDCF]
// | [#xFDF0-#xFFFD]
// | [#x10000-#xEFFFF]
PN_CHARS_BASE: [A-Z]
               | [a-z]
               | '\u00C0'..'\u00D6'
               | '\u00D8'..'\u00F6'
               | '\u00F8'..'\u02FF'
               | '\u0370'..'\u037D'
               | '\u037F'..'\u1FFF'
               | '\u200C'..'\u200D'
               | '\u2070'..'\u218F'
               | '\u2C00'..'\u2FEF'
               | '\u3001'..'\uD7FF'
               | '\uF900'..'\uFDCF'
               | '\uFDF0'..'\uFFFD'
               | '\u{10000}'..'\u{EFFFF}' ;
//[165] ???
PN_CHARS_U: PN_CHARS_BASE
            | '_';
//[166] 变量名称
//VARNAME::=  	( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )*
VARNAME: ( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] | '\u00B7' | '\u0300'..'\u036F' | '\u203F'..'\u2040')*;
//[167]
//PN_CHARS::=  	PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
PN_CHARS: PN_CHARS_U
          //| '-'                     // '-': 数值表达式问题
          | [0-9]
          | '\u00B7'
          | '\u0300'..'\u036F'
          | '\u203F'..'\u2040' ;
//[168] ???
PN_PREFIX: PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?;
//[169] ???
PN_LOCAL: (PN_CHARS_U | ':' | [0-9] | PLX ) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX) )?;
//[170] ???
PLX: PERCENT
     | PN_LOCAL_ESC;
//[171] ???
PERCENT: '%' HEX HEX;
//[172] 十六进制字符
HEX: [0-9] | [A-F] | [a-f];
//[173] ???
PN_LOCAL_ESC: '\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%' );
