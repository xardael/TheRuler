package TheRuler.Model;

/**
 * Represents a rule of SRGS grammar.
 * 
 * @author Peter Gren
 */
public class Rule implements java.io.Serializable {

    private String id;
    private String content;
    private Long grammarId;

    /**
     * Gets rule id. 
     * 
     * @return Rule ID.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets rule ID.
     *
     * @param id Rule ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets XML content of grammar as a string.
     * 
     * @return Grammar content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets grammar content.
     *
     * @param content Grammar content as a String.
     */ 
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets an ID of grammar containing this rule.
     * 
     * @return An ID of grammar containing this rule.
     */
    public Long getGrammarId() {
        return grammarId;
    }

    /**
     * Sets an ID of grammar containing this rule.
     * 
     * @param grammarId An ID of grammar containing this rule.
     */
    public void setGrammarId(Long grammarId) {
        this.grammarId = grammarId;
    }
}