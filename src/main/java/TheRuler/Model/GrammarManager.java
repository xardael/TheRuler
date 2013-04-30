package TheRuler.Model;

import TheRuler.Exceptions.DatabaseException;
import java.util.List;

/**
 * Database manager for grammars.
 *
 * @author Peter Gren
 */
public interface GrammarManager {

    /**
     * Persist grammar meta information as a new grammar with blank content
     * into a database.
     *
     * @param grammarMeta Grammar meta.
     * @return GrammerMeta object with newly created ID.
     */
    GrammarMeta createGrammar(GrammarMeta grammarMeta) throws DatabaseException;

    /**
     * Retrieves grammar meta information and grammar content from
     * database and returns it as a Grammar object.
     * 
     * @param id Grammar object for given ID.
     * @return All grammar information in Grammr object. If grammar meta with
     *         given ID does not exist, returns null.
     */
    Grammar findGrammar(Long id) throws DatabaseException;

    /**
     * Retrieves grammar meta information from
     * database and returns it wraped in a GrammarMeta object.
     *
     * @param id Grammr ID.
     * @return Grammr meta information in GrammarMeta object. If grammar with
     *         given ID does not exist, returns null.
     */
    GrammarMeta findGrammarMeta(Long id) throws DatabaseException;

    /**
     * Returns grammar meta for all grammars in database.
     * 
     * @return List of GrammarMetas.
     */
    List<GrammarMeta> findAllGrammarMetas() throws DatabaseException;

    /**
     * Updates grammar meta information and grammar content in database
     * according to given Grammar object.
     *
     * @param grammar Grammar stated for update.
     */
    void updateGrammar(Grammar grammar) throws DatabaseException;

    /**
     * Updates grammar meta information in database
     * according to given GrammarMeta object.
     * 
     * @param grammarMeta GrammarMeta stated for update.
     */
    void updateGrammarMeta(GrammarMeta grammarMeta) throws DatabaseException;

    /**
     * Delete grammr from database.
     *
     * @param grammarMeta GrammarMeta containg ID of grammar stated for deletion.
     */
    void deletaGrammar(GrammarMeta grammarMeta) throws DatabaseException;
}