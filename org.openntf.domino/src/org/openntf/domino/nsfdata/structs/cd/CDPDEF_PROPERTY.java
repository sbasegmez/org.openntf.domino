package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

/**
 * This doesn't appear in the API documentation, but is presumably part of Composite Apps.
 *
 */
public class CDPDEF_PROPERTY extends CDRecord {

	public CDPDEF_PROPERTY(final SIG signature, final ByteBuffer data) {
		super(signature, data);
	}

}