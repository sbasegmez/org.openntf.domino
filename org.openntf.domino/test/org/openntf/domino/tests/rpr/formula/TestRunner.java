/* Generated By:JJTree&JavaCC: Do not edit this line. AtFormulaParser.java */
package org.openntf.domino.tests.rpr.formula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.openntf.domino.formula.AtFormulaParser;
import org.openntf.domino.formula.AtFunction;
import org.openntf.domino.formula.AtFunctionFactory;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ast.SimpleNode;
import org.openntf.domino.impl.Base;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.Factory;

public class TestRunner extends TestRunnerStdIn {
	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new TestRunner(), "My thread");
		thread.start();
	}

	public TestRunner() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		try {
			System.out.println("Please type a Lotus domino @formula. Quit with CTRL+Z:");
			SimpleNode n = null;
			List<Object> v = null;
			//String str = "t:={start}; @for(i:=1;i != 10; i:= i + 1; t:=t:@if(i = 1; {one} ; i <= 3; {two or three}; {four or more})); t";
			//String str = "x:=1:2*+32:64:1;x**x**x**x";
			String str = "@time(1800;2;3;4;15;18)";
			//String str = "@Transform((1:2:3)*+(0:3:6:9);{x};x*x)";
			System.out.println("Formula to test: " + str);

			long time = System.currentTimeMillis();

			//String str = "\"ab\\n\\x\\\"xyzz\"";
			//String str = "t:={start}; @for(i:=1;i != 10; i:= i + 1; t:=t:@Text(i)); @Transform(t;{x};x+{ test }+t)";
			//System.out.println(str);
			List<AtFunction> funcs = new ArrayList<AtFunction>();
			funcs.addAll(AtFunctionFactory.getInstance().getFunctions().values());

			Collections.sort(funcs, new Comparator<AtFunction>() {
				@Override
				public int compare(final AtFunction o1, final AtFunction o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
			for (AtFunction func : funcs) {
				System.out.println(func);
			}

			AtFormulaParser parser = AtFormulaParser.getInstance();

			for (int i = 1; i < 10000; i++) {
				java.io.StringReader sr = new java.io.StringReader(str);
				//java.io.Reader r = new java.io.BufferedReader(sr);
				n = parser.parse(sr);
			}
			time = System.currentTimeMillis() - time;
			System.err.println("[FormulaEngine] 10000x building AST tree\ttook " + time + "ms.");
			//n.dump("");

			time = System.currentTimeMillis();
			for (int i = 1; i < 10000; i++) {
				FormulaContext ctx = new FormulaContext(null, parser.getFormatter());
				v = n.evaluate(ctx).toList();
			}
			time = System.currentTimeMillis() - time;
			System.err.println("[FormulaEngine] 10000x evaluating AST tree\ttook " + time + "ms.");

			System.out.println("Result:\t" + v);

			Session sess = Base.toLotus(Factory.getSession());
			long startEvaluate = System.currentTimeMillis();
			try {
				time = System.currentTimeMillis();
				for (int i = 1; i < 10000; i++) {
					v = sess.evaluate(str);
				}
				time = System.currentTimeMillis() - time;
				System.err.println("[NotesNative] 10000x calling session.evaluate\ttook " + time + "ms.");
				System.out.println("Result:\t" + v);

			} catch (NotesException e) {
				e.printStackTrace();
			}

			System.out.println("Thank you.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Factory.terminate();
		System.out.println(Factory.dumpCounters(true));
	}
}
