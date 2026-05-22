// Generated from /home/lorenzo/Scrivania/Università/Anno3/Linguaggi/Laboratorio/Progetto-20260514/PseudoCodeProject/PseudoCode/src/main/antlr4/it/univr/pl/PseudoCode.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PseudoCodeParser}.
 */
public interface PseudoCodeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PseudoCodeParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(PseudoCodeParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link PseudoCodeParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(PseudoCodeParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonDet}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterNonDet(PseudoCodeParser.NonDetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonDet}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitNonDet(PseudoCodeParser.NonDetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decl}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterDecl(PseudoCodeParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decl}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitDecl(PseudoCodeParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignOp}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterAssignOp(PseudoCodeParser.AssignOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignOp}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitAssignOp(PseudoCodeParser.AssignOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code incDec}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterIncDec(PseudoCodeParser.IncDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code incDec}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitIncDec(PseudoCodeParser.IncDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterWhile(PseudoCodeParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitWhile(PseudoCodeParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifElse}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterIfElse(PseudoCodeParser.IfElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifElse}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitIfElse(PseudoCodeParser.IfElseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code seq}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterSeq(PseudoCodeParser.SeqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code seq}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitSeq(PseudoCodeParser.SeqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterAssign(PseudoCodeParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitAssign(PseudoCodeParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code out}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void enterOut(PseudoCodeParser.OutContext ctx);
	/**
	 * Exit a parse tree produced by the {@code out}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 */
	void exitOut(PseudoCodeParser.OutContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mulDivMod}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterMulDivMod(PseudoCodeParser.MulDivModContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mulDivMod}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitMulDivMod(PseudoCodeParser.MulDivModContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterReadExp(PseudoCodeParser.ReadExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitReadExp(PseudoCodeParser.ReadExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterString(PseudoCodeParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitString(PseudoCodeParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numeric}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNumeric(PseudoCodeParser.NumericContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numeric}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNumeric(PseudoCodeParser.NumericContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addSub}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(PseudoCodeParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addSub}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(PseudoCodeParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andOr}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterAndOr(PseudoCodeParser.AndOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andOr}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitAndOr(PseudoCodeParser.AndOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code not}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNot(PseudoCodeParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code not}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNot(PseudoCodeParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eqExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterEqExp(PseudoCodeParser.EqExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eqExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitEqExp(PseudoCodeParser.EqExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(PseudoCodeParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(PseudoCodeParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cmpExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterCmpExp(PseudoCodeParser.CmpExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cmpExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitCmpExp(PseudoCodeParser.CmpExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterParExp(PseudoCodeParser.ParExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitParExp(PseudoCodeParser.ParExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code char}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterChar(PseudoCodeParser.CharContext ctx);
	/**
	 * Exit a parse tree produced by the {@code char}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitChar(PseudoCodeParser.CharContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pow}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterPow(PseudoCodeParser.PowContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pow}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitPow(PseudoCodeParser.PowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterId(PseudoCodeParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitId(PseudoCodeParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ternary}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterTernary(PseudoCodeParser.TernaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ternary}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitTernary(PseudoCodeParser.TernaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intNum}
	 * labeled alternative in {@link PseudoCodeParser#num}.
	 * @param ctx the parse tree
	 */
	void enterIntNum(PseudoCodeParser.IntNumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intNum}
	 * labeled alternative in {@link PseudoCodeParser#num}.
	 * @param ctx the parse tree
	 */
	void exitIntNum(PseudoCodeParser.IntNumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decNum}
	 * labeled alternative in {@link PseudoCodeParser#num}.
	 * @param ctx the parse tree
	 */
	void enterDecNum(PseudoCodeParser.DecNumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decNum}
	 * labeled alternative in {@link PseudoCodeParser#num}.
	 * @param ctx the parse tree
	 */
	void exitDecNum(PseudoCodeParser.DecNumContext ctx);
}