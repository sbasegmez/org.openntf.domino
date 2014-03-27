/* Generated By:JJTree: Do not edit this line. ASTExtendedParameter.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.openntf.domino.formula.ast;

import java.util.Set;

import org.openntf.domino.formula.AtFormulaParserImpl;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaReturnException;
import org.openntf.domino.formula.ValueHolder;

public class ASTExtendedParameter extends SimpleNode {
	String paramName;

	public ASTExtendedParameter(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	public void toFormula(final StringBuilder sb) {
		//throw new UnsupportedOperationException();
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {
		return children[0].evaluate(ctx);

	}

	public void init(final String image) {
		paramName = image;
	}

	public boolean isOptional() {
		return jjtGetNumChildren() == 1;
	}

	public String getName() {
		return paramName;
	}

	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		// TODO Auto-generated method stub

	}
}
/* JavaCC - OriginalChecksum=27ceadaec21dfc469d85e8548c548253 (do not edit this line) */
