package TheRuler.Model;

import TheRuler.Exceptions.DatabaseException;
import TheRuler.Exceptions.RuleExistsException;
import java.util.List;

/**
 * Database manager for rules, which works with BaseX XML database.
 * 
 * @author Peter Gren
 */
public interface RuleManager {

    /**
     * Persist given rule as a new rule into a database.
     *
     * @param rule Rule object.
     */
    void addRule(Rule rule) throws DatabaseException, RuleExistsException;

    /**
     * Retrieves rule with given ID in given grammar from
     * database and returns it as a Rule object.
     * 
     * @param id Rule ID.
     * @param grammarMeta GrammarMeta object with ID.
     * @return Selected rule. If rule with given ID does not exist returns null.
     */
    Rule findRuleById(String id, GrammarMeta grammarMeta) throws DatabaseException;

    /**
     * Returns list of all rules in given grammar.
     *
     * @param grammarMeta GrammarMeta with ID of grammar.
     * @return List of all rules in given grammar.
     */
    List<Rule> findAllRules(GrammarMeta grammarMeta) throws DatabaseException;

    /**
     * Provides pattern match search rule IDs.
     * 
     * @param id Part of ID to find.
     * @param grammarMeta GrammarMeta of grammar you want to search in.
     * @return List of rules which contain given string in ID.
     */
    List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws DatabaseException;

    /**
     * Update rule in a database.
     *
     * @param rule Rule object with ID.
     */
    void updateRule(Rule rule) throws DatabaseException;

    /**
     * Deletes given rule from BaseX database.
     *
     * @param rule Rule object with ID.
     */
    void deleteRule(Rule rule) throws DatabaseException;
}