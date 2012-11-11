/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TheRuler.Model;

import java.util.Date;

/**
 *
 * @author pyty
 */
public class Grammar {
    public Long id;
    public String name;
    public String description;
    public String xmlContent;
    public Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }
    
    
}
