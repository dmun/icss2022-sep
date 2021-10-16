package nl.han.ica.icss.parser;

import java.util.Stack;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new HANStack<>();
	}

    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		ASTNode stylesheet = new Stylesheet();
		currentContainer.push(stylesheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		ast.setRoot((Stylesheet) currentContainer.pop());
	}

	@Override
	public void enterVariable_assignment(ICSSParser.Variable_assignmentContext ctx) {
		var variableAssignment = new VariableAssignment();
		currentContainer.push(variableAssignment);
	}

	@Override
	public void exitVariable_assignment(ICSSParser.Variable_assignmentContext ctx) {
		var variableAssignment = currentContainer.pop();
		currentContainer.peek().addChild(variableAssignment);
	}

	@Override
	public void enterVariable_name(ICSSParser.Variable_nameContext ctx) {
		var variableName = new VariableReference(ctx.getText());
		currentContainer.push(variableName);
	}

	@Override
	public void exitVariable_name(ICSSParser.Variable_nameContext ctx) {
		var variableName = currentContainer.pop();
		currentContainer.peek().addChild(variableName);
	}

	@Override
	public void enterStyle_rule(ICSSParser.Style_ruleContext ctx) {
		var styleRule = new Stylerule();
		currentContainer.push(styleRule);
	}

	@Override
	public void exitStyle_rule(ICSSParser.Style_ruleContext ctx) {
		var styleRule = currentContainer.pop();
		currentContainer.peek().addChild(styleRule);
	}

	@Override
	public void enterSelector(ICSSParser.SelectorContext ctx) {
		ASTNode selector;
		var text = ctx.getText();
		switch (text.charAt(0)) {
			case '.':
				selector = new ClassSelector(text);
				break;
			case '#':
				selector = new IdSelector(text);
				break;
			default:
				selector = new TagSelector(text);
				break;
		}
		currentContainer.push(selector);
	}

	@Override
	public void exitSelector(ICSSParser.SelectorContext ctx) {
		var selector = currentContainer.pop();
		currentContainer.peek().addChild(selector);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		var declaration = new Declaration(ctx.getText());
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		var declaration = currentContainer.pop();
		currentContainer.peek().addChild(declaration);
	}

	@Override
	public void enterProperty_name(ICSSParser.Property_nameContext ctx) {
		var propertyName = new PropertyName(ctx.getText());
		currentContainer.push(propertyName);
	}

	@Override
	public void exitProperty_name(ICSSParser.Property_nameContext ctx) {
		var propertyName = currentContainer.pop();
		currentContainer.peek().addChild(propertyName);
	}

	@Override
	public void enterLiteral(ICSSParser.LiteralContext ctx) {
		ASTNode literal;
		var text = ctx.getText();
		if (text.startsWith("#")) {
			literal = new ColorLiteral(text);
		} else if (text.endsWith("px")) {
			literal = new PixelLiteral(text);
		} else if (text.endsWith("%")) {
			literal = new PercentageLiteral(text);
		} else if (text.equals("TRUE") || text.equals("FALSE")) {
			literal = new BoolLiteral(text);
		} else {
			literal = new ScalarLiteral(text);
		}
		currentContainer.push(literal);
	}


	@Override
	public void exitLiteral(ICSSParser.LiteralContext ctx) {
		var literal = currentContainer.pop();
		currentContainer.peek().addChild(literal);
	}

}