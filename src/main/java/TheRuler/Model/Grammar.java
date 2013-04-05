package TheRuler.Model;

/**
 * 
 * @author pyty
 */
public class Grammar {

	private GrammarMeta meta;
	private String content;

	public GrammarMeta getMeta() {
		return this.meta;
	}

	/**
	 * 
	 * @param meta
	 */
	public void setMeta(GrammarMeta meta) {
		this.meta = meta;
	}

        /**
         * Raw XML SRGS Grammar
         * 
         * @return 
         */
	public String getContent() {
		return this.content;
	}

	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

}