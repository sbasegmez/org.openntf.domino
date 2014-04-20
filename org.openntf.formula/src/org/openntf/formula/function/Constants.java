package org.openntf.formula.function;

import org.openntf.formula.FormulaContext;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

public enum Constants {
	;

	public static class Factory extends FunctionFactory {

		public Factory() {
			super(Constants.class);
		}
	}

	@ParamCount(0)
	// Do not know if that will work
	public static ValueHolder atAll(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atNothing() {
		return ValueHolder.valueNothing();
	}

	@ParamCount(0)
	public static ValueHolder atDeleteField() {
		return ValueHolder.valueUnavailable();
	}

	@ParamCount(0)
	public static ValueHolder atUnavailable() {
		return ValueHolder.valueUnavailable();
	}

	@ParamCount(0)
	public static ValueHolder atFalse(final FormulaContext ctx) {
		return ctx.FALSE;
	}

	@ParamCount(0)
	public static ValueHolder atNo(final FormulaContext ctx) {
		return ctx.FALSE;
	}

	@ParamCount(0)
	public static ValueHolder atTrue(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atYes(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atSuccess(final FormulaContext ctx) {
		return ctx.TRUE;
	}

	@ParamCount(0)
	public static ValueHolder atError(final FormulaContext ctx) {
		throw new IllegalArgumentException("@Error");
	}
}
