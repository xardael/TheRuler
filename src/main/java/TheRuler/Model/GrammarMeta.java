package TheRuler.Model;

/**
 * Holder for grammr meta information.
 *
 * @author Peter Gren
 */
public class GrammarMeta implements java.io.Serializable {

    private Long id;
    private String name;
    private String description;
    private String date;

    /**
     * Gets grammar ID.
     * 
     * @return Grammar ID.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets grammr ID.
     * 
     * @param id Grammar ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets grammar name.
     * 
     * @return Grammar name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets grammar name.
     * 
     * @param name Grammar name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets grammar description.
     * 
     * @return Grammar desciption.
     */
    public String getDescription() {
        if (description == null) {
            //return "";
        }

        return this.description;
    }

    /**
     * Sets grammar description.
     * 
     * @param description Grammar description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets grammar creation date in defulat date format
     * Config.DATE_FORMAT_STORED.
     * 
     * @return Grammar date in defulat date format Config.DATE_FORMAT_STORED.
     */
    public String getDate() {
        if (this.date == null) {
            return "";
        } else {
            return this.date;
        }
    }

    /**
     * Sets grammar creation date in defulat date format
     * Config.DATE_FORMAT_STORED.
     *
     * @param date Grammar creation date in defulat date format Config.DATE_FORMAT_STORED.
     */
    public void setDate(String date) {
        this.date = date;
    }
}