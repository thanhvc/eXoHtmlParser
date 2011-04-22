package exo.html.parser;

import junit.framework.TestCase;

public class ParserTest extends TestCase {

	private Parser parser = null;
	private final String source = "<i>test</i>";
	protected void setUp() throws Exception {
		super.setUp();
		initParser();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		parser = null;
	}
	
	private void initParser() {
		parser = new Parser(source);
	}
	
	public void testConstructor() throws Exception {
		assertNotNull(parser.getRequestSupport());
		assertNotNull(parser.getRequestSupport().getContext());
		assertNotNull(parser.getRequestSupport().getRequest());
		assertEquals(source, parser.getRequestSupport().getContext().getPage().source.getText());
	}
	
	public void testParser() throws Exception {
		parser.parse();
	}

}
