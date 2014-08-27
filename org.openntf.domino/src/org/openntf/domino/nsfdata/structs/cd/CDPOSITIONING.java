package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.LENGTH_VALUE;
import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This CD record contains position information for a layer box. (editods.h)
 * 
 * @since Lotus Notes/Domino 6.0
 *
 */
public class CDPOSITIONING extends CDRecord {

	public static enum Scheme {
		STATIC((byte) 0), ABSOLUTE((byte) 1), RELATIVE((byte) 2), FIXED((byte) 3);

		private final byte value_;

		private Scheme(final byte value) {
			value_ = value;
		}

		public byte getValue() {
			return value_;
		}

		public static Scheme valueOf(final byte typeCode) {
			for (Scheme type : values()) {
				if (type.getValue() == typeCode) {
					return type;
				}
			}
			throw new IllegalArgumentException("No matching Scheme found for type code " + typeCode);
		}
	}

	static {
		addFixed("Scheme", Byte.class);
		addFixed("bReserved", Byte.class);
		addFixed("ZIndex", Integer.class);
		addFixed("Top", LENGTH_VALUE.class);
		addFixed("Left", LENGTH_VALUE.class);
		addFixed("Bottom", LENGTH_VALUE.class);
		addFixed("Right", LENGTH_VALUE.class);
		addFixed("BrowserLeftOffset", Double.class);
		addFixed("BrowserRightOffset", Double.class);
	}

	public CDPOSITIONING(final SIG signature, final ByteBuffer data) {
		super(signature, data);

		System.out.println(this);
	}

	public Scheme getScheme() {
		return Scheme.valueOf((Byte) getStructElement("Scheme"));
	}
}
