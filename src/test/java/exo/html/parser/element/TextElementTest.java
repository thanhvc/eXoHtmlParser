package exo.html.parser.element;

import exo.html.parser.Element;
import junit.framework.TestCase;

public class TextElementTest extends TestCase {

	private Element element = null;
	private String text = "tile of <i>space</i>";
	protected void setUp() throws Exception {
		super.setUp();
		initialize();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Initialize the data for Test case.
	 */
	private void initialize() {
		element = new TextElement(text);
	}
	
	public void testConstructor() throws Exception {
				
		int expectStartPosition = 0;
		
		int expectEndPosition = text.length();
		
		assertEquals(expectStartPosition, element.getStartPosition());
		
		assertEquals(expectEndPosition, element.getEndPosition());
	}
	
	

}
