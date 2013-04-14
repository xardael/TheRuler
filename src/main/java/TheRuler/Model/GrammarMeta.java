package TheRuler.Model;

/**
 *
 * @author pyty
 */
public class GrammarMeta {

    private Long id;
    private String name;
    private String description;
    private String date;

    public Long getId() {
        return this.id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (description == null) {
            //return "";
        }

        return this.description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        if (this.date == null) {
            return "";
        } else {
            return this.date;
        }
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }
}