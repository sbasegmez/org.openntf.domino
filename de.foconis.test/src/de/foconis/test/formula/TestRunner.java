/* Generated By:JJTree&JavaCC: Do not edit this line. AtFormulaParser.java */
package de.foconis.test.formula;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jline.ANSIBuffer;
import jline.Terminal;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.formula.AtFormulaParser;
import org.openntf.domino.formula.DominoFormatter;
import org.openntf.domino.formula.FormulaContext;
import org.openntf.domino.formula.ParseException;
import org.openntf.domino.formula.ast.Node;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Strings;

public class TestRunner implements Runnable {
	protected Database db;

	private boolean VIRTUAL_CONSOLE = false;

	public static void main(final String[] args) {
		DominoThread thread = new DominoThread(new TestRunner(), "My thread");
		thread.start();
	}

	public TestRunner() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
		VIRTUAL_CONSOLE = Terminal.getTerminal().getTerminalWidth() < 10;
	}

	// Some Formatters
	public String NTF(final Object o) {
		if (VIRTUAL_CONSOLE)
			return o.toString();
		ANSIBuffer ab = new ANSIBuffer();
		return ab.cyan(o.toString()).toString();
	}

	public String LOTUS(final Object o) {
		if (VIRTUAL_CONSOLE)
			return o.toString();
		ANSIBuffer ab = new ANSIBuffer();
		return ab.magenta(o.toString()).toString();
	}

	public String ERROR(final Object o) {
		if (VIRTUAL_CONSOLE)
			return o.toString();
		ANSIBuffer ab = new ANSIBuffer();
		return ab.red(o.toString()).toString();
	}

	public String SUCCESS() {
		if (VIRTUAL_CONSOLE)
			return "[OK]\t";
		ANSIBuffer ab = new ANSIBuffer();
		ab.append("[");
		ab.green("OK");
		ab.append("]\t");

		return ab.toString();
	}

	public String FAIL() {
		if (VIRTUAL_CONSOLE)
			return "[FAIL]\t";
		ANSIBuffer ab = new ANSIBuffer();
		ab.append("[");
		ab.red("FAIL");
		ab.append("]\t");
		return ab.toString();
	}

	String formatTime(final long time) {
		return String.format("%.3f ms", (double) time / 1000000);
	}

	@Override
	public void run() {
		try {

			for (Fixes fix : Fixes.values())
				Factory.getSession().setFixEnable(fix, true);
			DominoUtils.setBubbleExceptions(true);

			File file = new File("tests/");
			FilenameFilter filefilter = new FilenameFilter() {
				public boolean accept(final File dir, final String name) {
					return name.endsWith(".txt");
				}
			};

			// Reading directory contents
			File[] files = file.listFiles(filefilter);

			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i]);
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				String line;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (Strings.isBlankString(line)) {

					} else if (line.startsWith("#")) {
						NTF(line);
					} else {
						execute(line, true, true, true);
					}
				}
				br.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(Factory.dumpCounters(true));
		db = null;
		Factory.terminate();
		System.out.println(Factory.dumpCounters(true));
	}

	private Document createDocument() {
		if (db == null)
			db = Factory.getSession().getDatabase("", "log.nsf");
		try {
			Document doc = db.createDocument();
			return doc;
		} catch (NullPointerException npe) {
			System.err.println("Cannot create demo doc. Is your server running?");
			return null;
		}
	}

	private void fillDemoDoc(final Map<String, Object> doc, final double rndVal) {

		doc.put("rnd", new double[] { rndVal });

		doc.put("text1", "This is a test string");
		doc.put("text2", new String[] { "1", "2", "3" });

		doc.put("int1", new int[] { 1 });
		doc.put("int2", new int[] { 1, 2, 3 });
		Map<String, String> map = new HashMap<String, String>();
		map.put("K1", "v1");
		map.put("K2", "v2");
		doc.put("mime1", map);

	}

	private String dump(final Object lotus) {
		// TODO Auto-generated method stub
		int width = Terminal.getTerminal().getTerminalWidth() - 20;
		String s = lotus + "";
		if (s.length() > width && width > 0) {
			s = s.substring(0, width) + "... (l=" + width + ")";
		}

		return s;

	}

	protected void execute(final String line, final boolean testLotus, final boolean testDoc, final boolean testMap) {
		// TODO Auto-generated method stub

		List<Object> ntfDocResult = null;
		List<Object> ntfMapResult = null;
		List<Object> lotusResult = null;
		Throwable ntfError = null;
		boolean lotusFailed = false;
		boolean parserFailed = false;
		// Setup procedure, prepare the demo docs & maps
		StringBuffer errors = new StringBuffer();

		double rnd = Math.random();
		Document ntfDoc = createDocument();
		Document lotusDoc = createDocument();
		Map<String, Object> ntfMap = new HashMap<String, Object>();

		fillDemoDoc(ntfDoc, rnd);
		fillDemoDoc(lotusDoc, rnd);
		fillDemoDoc(ntfMap, rnd);
		lotus.domino.Session rawSession = Factory.toLotus(Factory.getSession());
		lotus.domino.Document rawDocument = Factory.toLotus(lotusDoc);
		if (testLotus) {
			try {

				lotusResult = rawSession.evaluate(line, rawDocument);
			} catch (NotesException e) {
				errors.append(LOTUS("\tLotus failed: ") + ERROR(e) + "\n");
				lotusFailed = true;
			} catch (Throwable t) {
				System.err.println(ERROR("FATAL") + LOTUS("\tLotus failed: ") + ERROR(t));
			}
		}

		// benchmark the AtFormulaParser
		Node ast = null;
		try {
			ast = AtFormulaParser.getInstance().parse(line);
		} catch (ParseException e) {
			errors.append(NTF("\tParser failed: ") + ERROR(e) + "\n");
			e.printStackTrace();
			parserFailed = true;
		} catch (Throwable t) {
			System.err.println(ERROR("FATAL") + NTF("\tParser failed: ") + ERROR(t));
			t.printStackTrace();
		}

		if (!parserFailed) {
			if (testDoc) {
				try {
					FormulaContext ctx1 = new FormulaContext(ntfDoc, DominoFormatter.getInstance());
					ntfDocResult = ast.solve(ctx1);
				} catch (RuntimeException e) {
					errors.append(NTF("\tDoc-Evaluate failed: ") + ERROR(e) + "\n");
					ntfError = e;
					parserFailed = true;
				} catch (Throwable t) {
					System.err.println(ERROR("FATAL") + NTF("\tDoc-Evaluate failed: ") + ERROR(t));
				}
			}
			if (testMap) {
				try {
					// benchmark the evaluate with a map as context
					FormulaContext ctx2 = new FormulaContext(ntfMap, DominoFormatter.getInstance());
					ntfMapResult = ast.solve(ctx2);
				} catch (RuntimeException e) {
					errors.append(NTF("\tMap-Evaluate failed: ") + ERROR(e) + "\n");
					ntfError = e;
					parserFailed = true;
				} catch (Throwable t) {
					System.err.println(ERROR("FATAL") + NTF("\tMap-Evaluate failed: ") + ERROR(t));
				}
			}
		}

		if (lotusFailed && parserFailed) {
			System.out.println(SUCCESS() + dump(line + " = UNDEFINED"));

			return;
		}

		if (testLotus && testDoc) {
			if (compareList(ntfDocResult, lotusResult)) {
				System.out.println(SUCCESS() + line + " = " + dump(ntfDocResult));
			} else {
				System.err.println(FAIL() + NTF("DOC:") + line);
				System.err.println("\tResult:   " + dump(ntfDocResult) + " Size: " + ((ntfDocResult == null) ? 0 : ntfDocResult.size()));
				System.err.println("\tExpected: " + dump(lotusResult) + " Size: " + ((lotusResult == null) ? 0 : lotusResult.size()));
				if (parserFailed || lotusFailed) {
					System.err.println(errors.toString());
					if (ntfError != null) {
						ntfError.printStackTrace(System.err);
					}
				}
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
				try {
					console.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			if (parserFailed) {
				ntfError.printStackTrace();
			}
			System.err.println("\tDocResult:   " + dump(ntfDocResult) + " Size: " + ((ntfDocResult == null) ? 0 : ntfDocResult.size()));
			System.err.println("\tMapResult:   " + dump(ntfMapResult) + " Size: " + ((ntfMapResult == null) ? 0 : ntfMapResult.size()));
		}
		System.out.println(NTF("Read fields\t") + ast.getReadFields());
		System.out.println(NTF("Modified fields\t") + ast.getModifiedFields());
		System.out.println(NTF("Variables\t") + ast.getVariables());
		System.out.println(NTF("Functions\t") + ast.getFunctions());

	}

	private boolean compareList(final List<Object> list1, final List<Object> list2) {
		boolean equals = true;
		if (list1 == null && list2 == null)
			return true;
		if (list1 == null || list2 == null)
			return false;

		if (list1.size() == 0 && list2.size() == 1) {
			if ("".equals(list2.get(0)))
				return true;
		}
		if (list2.size() == 0 && list1.size() == 1) {
			if ("".equals(list1.get(0)))
				return true;
		}

		if (list1.size() == list2.size()) {
			for (int i = 0; i < list1.size(); i++) {
				Object a = list1.get(i);
				Object b = list2.get(i);
				if (a == null && b == null) {

				} else if (a == null || b == null) {
					equals = false;
					break;
				} else if (a instanceof Boolean && b instanceof Number) {
					if ((Boolean) a) {
						if (Double.compare(1.0, ((Number) b).doubleValue()) != 0) {
							equals = false;
							break;
						}
					} else {
						if (Double.compare(0.0, ((Number) b).doubleValue()) != 0) {
							equals = false;
							break;
						}
					}
				} else if (a instanceof Number && b instanceof Number) {
					if (Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue()) != 0) {
						equals = false;
						break;
					}
				} else if (a instanceof lotus.domino.DateTime && b instanceof lotus.domino.DateTime) {
					lotus.domino.DateTime dt1 = (lotus.domino.DateTime) a;
					lotus.domino.DateTime dt2 = (lotus.domino.DateTime) b;
					try {
						if (dt1.timeDifference(dt2) != 0)
							equals = false;
					} catch (NotesException e) {
						e.printStackTrace();
						equals = false;
					}

				} else if (!a.equals(b)) {
					equals = false;
					break;
				}
			}
		} else {
			equals = false;
		}
		return equals;
	}
}
