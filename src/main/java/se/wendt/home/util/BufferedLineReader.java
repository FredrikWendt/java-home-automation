package se.wendt.home.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

public class BufferedLineReader extends BufferedReader implements Iterable<String> {

	public BufferedLineReader(Reader in) {
		super(in);
	}

	public BufferedLineReader(InputStream inputStream) {
		super(new InputStreamReader(inputStream));
	}

	@Override
	public Iterator<String> iterator() {
		return new XIterator(this);
	}

	class XIterator implements Iterator<String> {

		private final BufferedReader reader;

		public XIterator(BufferedReader reader) {
			this.reader = reader;
			prepareNextLine();
		}

		private String nextLine;

		@Override
		public boolean hasNext() {
			return nextLine != null;
		}

		@Override
		public String next() {
			String lineToReturn = nextLine;
			prepareNextLine();
			return lineToReturn;
		}

		private void prepareNextLine() {
			try {
				nextLine = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				nextLine = null;
			}
		}

		@Override
		public void remove() {
		}
	}

}
