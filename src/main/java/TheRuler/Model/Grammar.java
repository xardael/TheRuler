package TheRuler.Model;

/**
 * Represents SRGS grammar with XML content and meta information.
 *
 * @author Peter Gren
 */
public class Grammar implements java.io.Serializable {

    private GrammarMeta meta;
    private String content;

    public Grammar() {
        meta = new GrammarMeta();
    }

    /**
     * Gets grammar meta information wraped in grammar meta object. 
     * Grammar meta does not contain XML content.
     * 
     * @return  Grammar meta information in GrammarMeta
     */
    public GrammarMeta getMeta() {
        return this.meta;
    }

    /**
     * Sets grammar meta information from grammar meta object.
     *
     * @param meta Grammar meta infromation in GrammarMeta object.
     */
    public void setMeta(GrammarMeta meta) {
        this.meta = meta;
    }

    /**
     * Gets raw XML SRGS Grammar as a String.
     *
     * @return XML SRGS Grammar content as a String.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets grammar content.
     * 
     * @param content SRGS grammar as a String.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /*****************************************************************
     * Nested grammarMeta stuff
     *****************************************************************/
    
    /**
     * Gets grammar ID.
     * 
     * @return Grammar ID.
     */
    public Long getId() {
        return this.meta.getId();
    }

    /**
     * Sets grammr ID.
     * 
     * @param id Grammar ID.
     */
    public void setId(Long id) {
        this.meta.setId(id);
    }

    /**
     * Gets grammar name.
     * 
     * @return Grammar name.
     */
    public String getName() {
        return this.meta.getName();
    }

    /**
     * Sets grammar name.
     * 
     * @param name Grammar name.
     */
    public void setName(String name) {
        this.meta.setName(name);
    }

    /**
     * Gets grammar description.
     * 
     * @return Grammar desciption.
     */
    public String getDescription() {
        return this.meta.getDescription();
    }

    /**
     * Sets grammar description.
     * 
     * @param description Grammar description.
     */
    public void setDescription(String description) {
        this.meta.setDescription(description);
    }

    /**
     * Gets grammar creation date in defulat date format
     * Config.DATE_FORMAT_STORED.
     * 
     * @return Grammar date in defulat date format Config.DATE_FORMAT_STORED.
     */
    public String getDate() {
        return this.meta.getDate();
    }

    /**
     * Sets grammar creation date in defulat date format
     * Config.DATE_FORMAT_STORED.
     *
     * @param date Grammar creation date in defulat date format Config.DATE_FORMAT_STORED.
     */
    public void setDate(String date) {
        this.meta.setDate(date);
    }
}