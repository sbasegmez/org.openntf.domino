/* Generated By:JJTree: Do not edit this line. ASTAtDoWhile.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
/*
 * © Copyright FOCONIS AG, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.formula.ast;

import java.util.Set;

import org.openntf.domino.formula.AtFormulaParserImpl;
import org.openntf.domino.formula.EvaluateException;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.FormulaReturnException;
import org.openntf.domino.formula.ValueHolder;
import org.openntf.domino.formula.ValueHolder.DataType;

public class ASTAtDoWhile extends SimpleNode {

	public ASTAtDoWhile(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	/**
	 * AtDoWhile returns always TRUE, or an Error-ValueHolder, if an error occurs in the last parameter.
	 * 
	 * @throws EvaluateException
	 *             TODO
	 */
	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {

		ValueHolder ret = null;
		if (children != null) {
			do {
				for (int i = 0; i < children.length; ++i) {
					ret = children[i].evaluate(ctx);
				}
				if (ret == null)
					break; // should not happen

				if (ret.dataType == DataType.ERROR)
					return ret;

			} while (ret.isTrue(ctx));
		}
		return ValueHolder.valueOf(1); // returns always TRUE

	}

	public void toFormula(final StringBuilder sb) {
		sb.append("@DoWhile");
		appendParams(sb);
	}

	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		functions.add("@dowhile");
	}
}
/* JavaCC - OriginalChecksum=3d9a997fad4a26e001d6dec704a9797f (do not edit this line) */
