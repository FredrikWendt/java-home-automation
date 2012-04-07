package se.wendt.test;

import java.io.IOException;
import java.io.InputStream;

public class StringInputStream extends InputStream {

	private final byte[] bytes;
	private int offset = 0;

	public StringInputStream(String data) {
		this.bytes = data.getBytes();
	}

	@Override
	public int read() throws IOException {
		if (offset < bytes.length) {
			return bytes[offset++];
		}
		return -1;
	}

}
