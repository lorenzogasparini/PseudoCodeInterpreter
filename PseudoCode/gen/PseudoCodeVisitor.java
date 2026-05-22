// Generated from /home/lorenzo/Scrivania/Università/Anno3/Linguaggi/Laboratorio/Progetto-20260514/PseudoCodeProject/PseudoCode/src/main/antlr4/it/univr/pl/PseudoCode.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PseudoCodeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PseudoCodeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PseudoCodeParser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(PseudoCodeParser.MainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonDet}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonDet(PseudoCodeParser.NonDetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decl}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(PseudoCodeParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignOp}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignOp(PseudoCodeParser.AssignOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code incDec}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncDec(PseudoCodeParser.IncDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(PseudoCodeParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifElse}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElse(PseudoCodeParser.IfElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code seq}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq(PseudoCodeParser.SeqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(PseudoCodeParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code out}
	 * labeled alternative in {@link PseudoCodeParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut(PseudoCodeParser.OutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mulDivMod}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivMod(PseudoCodeParser.MulDivModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code readExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReadExp(PseudoCodeParser.ReadExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(PseudoCodeParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numeric}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric(PseudoCodeParser.NumericContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSub}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(PseudoCodeParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andOr}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOr(PseudoCodeParser.AndOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(PseudoCodeParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(PseudoCodeParser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(PseudoCodeParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cmpExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpExp(PseudoCodeParser.CmpExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parExp}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExp(PseudoCodeParser.ParExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code char}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar(PseudoCodeParser.CharContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pow}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPow(PseudoCodeParser.PowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(PseudoCodeParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ternary}
	 * labeled alternative in {@link PseudoCodeParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernary(PseudoCodeParser.TernaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intNum}
	 * labeled alternative in {@link PseudoCodeParser#num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntNum(PseudoCodeParser.IntNumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decNum}
	 * labeled alternative in {@link PseudoCodeParser#num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecNum(PseudoCodeParser.DecNumContext ctx);
}