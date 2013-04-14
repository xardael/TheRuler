package TheRuler.Model;

/**
 *
 * @author pyty
 */
public class Rule {

    private String id;
    private String content;

    public String getId() {
        return this.id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

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