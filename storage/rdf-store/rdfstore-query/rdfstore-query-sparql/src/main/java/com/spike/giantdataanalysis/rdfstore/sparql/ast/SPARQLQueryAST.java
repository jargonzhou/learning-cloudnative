package com.spike.giantdataanalysis.rdfstore.sparql.ast;

import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.rdfstore.sparql.SPARQLQueryParser;
import com.spike.giantdataanalysis.rdfstore.sparql.SPARQLQueryParser.*;
import com.spike.giantdataanalysis.rdfstore.sparql.SPARQLQueryVisitor;
import com.spike.giantdataanalysis.rdfstore.sparql.exception.ASTParseException;

/**
 * IR generator for SPARQL Query Language.
 * <p>
 * NOTE: IR is rather complicated.
 */
public class SPARQLQueryAST implements SPARQLQueryVisitor<Void> {
  private static final Logger LO = LoggerFactory.getLogger(SPARQLQueryAST.class);

  private final SPARQLQueryParser parser;

  public SPARQLQueryAST(SPARQLQueryParser parser) {
    this.parser = parser;
  }

  @Override
  public Void visit(ParseTree tree) {
    LO.debug("visit: {}", tree.getText());
    if (tree instanceof QueryUnitContext) {
      return visitQueryUnit((QueryUnitContext) tree);
    }
    throw ASTParseException.newE("Support SAPRQL Query only currently!");
  }

  @Override
  public Void visitChildren(RuleNode node) {
    LO.debug("visitChildren: {}", node);
    return null;
  }

  @Override
  public Void visitTerminal(TerminalNode node) {
    LO.debug("visitTerminal: {}", node);
    return null;
  }

  @Override
  public Void visitErrorNode(ErrorNode node) {
    LO.debug("visitErrorNode: {}", node);
    return null;
  }

  @Override
  public Void visitQueryUnit(QueryUnitContext ctx) {
    LO.debug("visitQueryUnit: {}", ctx.toStringTree(parser));

    ParseTree gQueryContext = ctx.getChild(0);
    Preconditions.checkState(gQueryContext instanceof QueryContext);
    return visitQuery((QueryContext) gQueryContext);
  }

  @Override
  public Void visitQuery(QueryContext ctx) {
    LO.debug("visitQuery: {}", ctx.toStringTree(parser));

    List<ParseTree> children = ctx.children;
    Preconditions.checkArgument(children.size() == 3);
    Preconditions.checkArgument(children.get(0) instanceof PrologueContext);
    Preconditions.checkArgument(children.get(1) instanceof SelectQueryContext
        || children.get(1) instanceof ConstructQueryContext
        || children.get(1) instanceof DescribeQueryContext
        || children.get(1) instanceof AskQueryContext);
    Preconditions.checkArgument(children.get(2) instanceof ValuesClauseContext);
    return null;
  }

  @Override
  public Void visitUpdateUnit(UpdateUnitContext ctx) {
    LO.debug("visitUpdateUnit: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPrologue(PrologueContext ctx) {
    LO.debug("visitPrologue: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBaseDecl(BaseDeclContext ctx) {
    LO.debug("visitBaseDecl: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPrefixDecl(PrefixDeclContext ctx) {
    LO.debug("visitPrefixDecl: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitSelectQuery(SelectQueryContext ctx) {
    LO.debug("visitSelectQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitSubSelect(SubSelectContext ctx) {
    LO.debug("visitSubSelect: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitSelectClause(SelectClauseContext ctx) {
    LO.debug("visitSelectClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitConstructQuery(ConstructQueryContext ctx) {
    LO.debug("visitConstructQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDescribeQuery(DescribeQueryContext ctx) {
    LO.debug("visitDescribeQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitAskQuery(AskQueryContext ctx) {
    LO.debug("visitAskQuery: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDatasetClause(DatasetClauseContext ctx) {
    LO.debug("visitDatasetClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDefaultGraphClause(DefaultGraphClauseContext ctx) {
    LO.debug("visitDefaultGraphClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNamedGraphClause(NamedGraphClauseContext ctx) {
    LO.debug("visitNamedGraphClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitSourceSelector(SourceSelectorContext ctx) {
    LO.debug("visitSourceSelector: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitWhereClause(WhereClauseContext ctx) {
    LO.debug("visitWhereClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitSolutionModifier(SolutionModifierContext ctx) {
    LO.debug("visitSolutionModifier: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGroupClause(GroupClauseContext ctx) {
    LO.debug("visitGroupClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGroupCondition(GroupConditionContext ctx) {
    LO.debug("visitGroupCondition: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitHavingClause(HavingClauseContext ctx) {
    LO.debug("visitHavingClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitHavingCondition(HavingConditionContext ctx) {
    LO.debug("visitHavingCondition: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitOrderClause(OrderClauseContext ctx) {
    LO.debug("visitOrderClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitOrderCondition(OrderConditionContext ctx) {
    LO.debug("visitOrderCondition: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitLimitOffsetClauses(LimitOffsetClausesContext ctx) {
    LO.debug("visitLimitOffsetClauses: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitLimitClause(LimitClauseContext ctx) {
    LO.debug("visitLimitClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitOffsetClause(OffsetClauseContext ctx) {
    LO.debug("visitOffsetClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitValuesClause(ValuesClauseContext ctx) {
    LO.debug("visitValuesClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitUpdate(UpdateContext ctx) {
    LO.debug("visitUpdate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitUpdate1(Update1Context ctx) {
    LO.debug("visitUpdate1: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitLoad(LoadContext ctx) {
    LO.debug("visitLoad: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitClear(ClearContext ctx) {
    LO.debug("visitClear: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDrop(DropContext ctx) {
    LO.debug("visitDrop: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitCreate(CreateContext ctx) {
    LO.debug("visitCreate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitAdd(AddContext ctx) {
    LO.debug("visitAdd: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitMove(MoveContext ctx) {
    LO.debug("visitMove: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitCopy(CopyContext ctx) {
    LO.debug("visitCopy: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitInsertData(InsertDataContext ctx) {
    LO.debug("visitInsertData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDeleteData(DeleteDataContext ctx) {
    LO.debug("visitDeleteData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDeleteWhere(DeleteWhereContext ctx) {
    LO.debug("visitDeleteWhere: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitModify(ModifyContext ctx) {
    LO.debug("visitModify: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDeleteClause(DeleteClauseContext ctx) {
    LO.debug("visitDeleteClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitInsertClause(InsertClauseContext ctx) {
    LO.debug("visitInsertClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitUsingClause(UsingClauseContext ctx) {
    LO.debug("visitUsingClause: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphOrDefault(GraphOrDefaultContext ctx) {
    LO.debug("visitGraphOrDefault: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphRef(GraphRefContext ctx) {
    LO.debug("visitGraphRef: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphRefAll(GraphRefAllContext ctx) {
    LO.debug("visitGraphRefAll: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitQuadPattern(QuadPatternContext ctx) {
    LO.debug("visitQuadPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitQuadData(QuadDataContext ctx) {
    LO.debug("visitQuadData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitQuads(QuadsContext ctx) {
    LO.debug("visitQuads: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitQuadsNotTriples(QuadsNotTriplesContext ctx) {
    LO.debug("visitQuadsNotTriples: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitTriplesTemplate(TriplesTemplateContext ctx) {
    LO.debug("visitTriplesTemplate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGroupGraphPattern(GroupGraphPatternContext ctx) {
    LO.debug("visitGroupGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGroupGraphPatternSub(GroupGraphPatternSubContext ctx) {
    LO.debug("visitGroupGraphPatternSub: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitTriplesBlock(TriplesBlockContext ctx) {
    LO.debug("visitTriplesBlock: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphPatternNotTriples(GraphPatternNotTriplesContext ctx) {
    LO.debug("visitGraphPatternNotTriples: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitOptionalGraphPattern(OptionalGraphPatternContext ctx) {
    LO.debug("visitOptionalGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphGraphPattern(GraphGraphPatternContext ctx) {
    LO.debug("visitGraphGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitServiceGraphPattern(ServiceGraphPatternContext ctx) {
    LO.debug("visitServiceGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBind(BindContext ctx) {
    LO.debug("visitBind: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitInlineData(InlineDataContext ctx) {
    LO.debug("visitInlineData: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDataBlock(DataBlockContext ctx) {
    LO.debug("visitDataBlock: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitInlineDataOneVar(InlineDataOneVarContext ctx) {
    LO.debug("visitInlineDataOneVar: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitInlineDataFull(InlineDataFullContext ctx) {
    LO.debug("visitInlineDataFull: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitDataBlockValue(DataBlockValueContext ctx) {
    LO.debug("visitDataBlockValue: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitMinusGraphPattern(MinusGraphPatternContext ctx) {
    LO.debug("visitMinusGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGroupOrUnionGraphPattern(GroupOrUnionGraphPatternContext ctx) {
    LO.debug("visitGroupOrUnionGraphPattern: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitFilter(FilterContext ctx) {
    LO.debug("visitFilter: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitConstraint(ConstraintContext ctx) {
    LO.debug("visitConstraint: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitFunctionCall(FunctionCallContext ctx) {
    LO.debug("visitFunctionCall: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitArgList(ArgListContext ctx) {
    LO.debug("visitArgList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitExpressionList(ExpressionListContext ctx) {
    LO.debug("visitExpressionList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitConstructTemplate(ConstructTemplateContext ctx) {
    LO.debug("visitConstructTemplate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitConstructTriples(ConstructTriplesContext ctx) {
    LO.debug("visitConstructTriples: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitTriplesSameSubject(TriplesSameSubjectContext ctx) {
    LO.debug("visitTriplesSameSubject: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPropertyList(PropertyListContext ctx) {
    LO.debug("visitPropertyList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPropertyListNotEmpty(PropertyListNotEmptyContext ctx) {
    LO.debug("visitPropertyListNotEmpty: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitVerb(VerbContext ctx) {
    LO.debug("visitVerb: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitObjectList(ObjectListContext ctx) {
    LO.debug("visitObjectList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitObject(ObjectContext ctx) {
    LO.debug("visitObject: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitTriplesSameSubjectPath(TriplesSameSubjectPathContext ctx) {
    LO.debug("visitTriplesSameSubjectPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPropertyListPath(PropertyListPathContext ctx) {
    LO.debug("visitPropertyListPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPropertyListPathNotEmpty(PropertyListPathNotEmptyContext ctx) {
    LO.debug("visitPropertyListPathNotEmpty: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitVerbPath(VerbPathContext ctx) {
    LO.debug("visitVerbPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitVerbSimple(VerbSimpleContext ctx) {
    LO.debug("visitVerbSimple: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitObjectListPath(ObjectListPathContext ctx) {
    LO.debug("visitObjectListPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitObjectPath(ObjectPathContext ctx) {
    LO.debug("visitObjectPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPath(PathContext ctx) {
    LO.debug("visitPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathAlternative(PathAlternativeContext ctx) {
    LO.debug("visitPathAlternative: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathSequence(PathSequenceContext ctx) {
    LO.debug("visitPathSequence: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathElt(PathEltContext ctx) {
    LO.debug("visitPathElt: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathEltOrInverse(PathEltOrInverseContext ctx) {
    LO.debug("visitPathEltOrInverse: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathMod(PathModContext ctx) {
    LO.debug("visitPathMod: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathPrimary(PathPrimaryContext ctx) {
    LO.debug("visitPathPrimary: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathNegatedPropertySet(PathNegatedPropertySetContext ctx) {
    LO.debug("visitPathNegatedPropertySet: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPathOneInPropertySet(PathOneInPropertySetContext ctx) {
    LO.debug("visitPathOneInPropertySet: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitInteger(IntegerContext ctx) {
    LO.debug("visitInteger: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitTriplesNode(TriplesNodeContext ctx) {
    LO.debug("visitTriplesNode: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBlankNodePropertyList(BlankNodePropertyListContext ctx) {
    LO.debug("visitBlankNodePropertyList: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitTriplesNodePath(TriplesNodePathContext ctx) {
    LO.debug("visitTriplesNodePath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBlankNodePropertyListPath(BlankNodePropertyListPathContext ctx) {
    LO.debug("visitBlankNodePropertyListPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitCollection(CollectionContext ctx) {
    LO.debug("visitCollection: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitCollectionPath(CollectionPathContext ctx) {
    LO.debug("visitCollectionPath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphNode(GraphNodeContext ctx) {
    LO.debug("visitGraphNode: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphNodePath(GraphNodePathContext ctx) {
    LO.debug("visitGraphNodePath: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitVarOrTerm(VarOrTermContext ctx) {
    LO.debug("visitVarOrTerm: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitVarOrIri(VarOrIriContext ctx) {
    LO.debug("visitVarOrIri: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitVar(VarContext ctx) {
    LO.debug("visitVar: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitGraphTerm(GraphTermContext ctx) {
    LO.debug("visitGraphTerm: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitExpression(ExpressionContext ctx) {
    LO.debug("visitExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitConditionalOrExpression(ConditionalOrExpressionContext ctx) {
    LO.debug("visitConditionalOrExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitConditionalAndExpression(ConditionalAndExpressionContext ctx) {
    LO.debug("visitConditionalAndExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitValueLogical(ValueLogicalContext ctx) {
    LO.debug("visitValueLogical: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitRelationalExpression(RelationalExpressionContext ctx) {
    LO.debug("visitRelationalExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNumericExpression(NumericExpressionContext ctx) {
    LO.debug("visitNumericExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitAdditiveExpression(AdditiveExpressionContext ctx) {
    LO.debug("visitAdditiveExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
    LO.debug("visitMultiplicativeExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitUnaryExpression(UnaryExpressionContext ctx) {
    LO.debug("visitUnaryExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPrimaryExpression(PrimaryExpressionContext ctx) {
    LO.debug("visitPrimaryExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBrackettedExpression(BrackettedExpressionContext ctx) {
    LO.debug("visitBrackettedExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBuiltInCall(BuiltInCallContext ctx) {
    LO.debug("visitBuiltInCall: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitRegexExpression(RegexExpressionContext ctx) {
    LO.debug("visitRegexExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitSubstringExpression(SubstringExpressionContext ctx) {
    LO.debug("visitSubstringExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitStrReplaceExpression(StrReplaceExpressionContext ctx) {
    LO.debug("visitStrReplaceExpression: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitExistsFunc(ExistsFuncContext ctx) {
    LO.debug("visitExistsFunc: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNotExistsFunc(NotExistsFuncContext ctx) {
    LO.debug("visitNotExistsFunc: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitAggregate(AggregateContext ctx) {
    LO.debug("visitAggregate: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitIriOrFunction(IriOrFunctionContext ctx) {
    LO.debug("visitiriOrFunction: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitRDFLiteral(RDFLiteralContext ctx) {
    LO.debug("visitRDFLiteral: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNumericLiteral(NumericLiteralContext ctx) {
    LO.debug("visitNumericLiteral: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNumericLiteralUnsigned(NumericLiteralUnsignedContext ctx) {
    LO.debug("visitNumericLiteralUnsigned: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNumericLiteralPositive(NumericLiteralPositiveContext ctx) {
    LO.debug("visitNumericLiteralPositive: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitNumericLiteralNegative(NumericLiteralNegativeContext ctx) {
    LO.debug("visitNumericLiteralNegative: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBooleanLiteral(BooleanLiteralContext ctx) {
    LO.debug("visitBooleanLiteral: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitString(StringContext ctx) {
    LO.debug("visitString: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitIri(IriContext ctx) {
    LO.debug("visitIri: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitPrefixedName(PrefixedNameContext ctx) {
    LO.debug("visitPrefixedName: {}", ctx.toStringTree(parser));
    return null;
  }

  @Override
  public Void visitBlankNode(BlankNodeContext ctx) {
    LO.debug("visitBlankNode: {}", ctx.toStringTree(parser));
    return null;
  }

}
