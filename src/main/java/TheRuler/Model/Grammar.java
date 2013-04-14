package TheRuler.Model;

/**
 * Represents srgs grammar with XML content and meta informations.
 *
 * @author Peter Gren
 */
public class Grammar {

    private GrammarMeta meta;
    private String content;

    public Grammar() {
        meta = new GrammarMeta();
    }

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

    /*
     * Nested grammarMeta stuff
     */
    public Long getId() {
        return this.meta.getId();
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.meta.setId(id);
    }

    public String getName() {
        return this.meta.getName();
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.meta.setName(name);
    }

    public String getDescription() {
        return this.meta.getDescription();
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.meta.setDescription(description);
    }

    public String getDate() {
        return this.meta.getDate();
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.meta.setDate(date);
    }
}