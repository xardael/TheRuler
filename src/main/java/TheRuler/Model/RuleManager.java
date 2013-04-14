package TheRuler.Model;

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
     * @param grammarMeta Grammar meta.
     * @param rule Rule object.
     */
    void addRule(Rule rule, GrammarMeta grammarMeta) throws Exception;

    /**
     * Retrieves rule with given ID in given grammar from
     * database and returns it as a Rule object.
     * 
     * @param id Rule ID.
     * @param grammarMeta GrammarMeta object with ID.
     * @return Selected rule. If rule with given ID does not exist returns null.
     */
    Rule findRuleById(String id, GrammarMeta grammarMeta) throws Exception;

    /**
     * Returns list of all rules in given grammar.
     *
     * @param grammarMeta GrammarMeta with ID of grammar.
     * @return List of all rules in given grammar.
     */
    List<Rule> findAllRules(GrammarMeta grammarMeta) throws Exception;

    /**
     * Provides pattern match search rule IDs.
     * 
     * @param id Part of ID to find.
     * @param grammarMeta GrammarMeta of grammar you want to search in.
     * @return List of rules which contain given string in ID.
     */
    List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws Exception;

    /**
     * Update rule in a database.
     *
     * @param rule Rule object with ID.
     * @param grammarMeta GrammarMeta object of grammar containing given rule with ID.
     */
    void updateRule(Rule rule, GrammarMeta grammarMeta) throws Exception;

    /**
     * Deletes given rule from BaseX database.
     *
     * @param rule Rule object with ID.
     * @param grammarMeta GrammarMeta object of grammar containing given rule with ID.
     */
    void deleteRule(Rule rule, GrammarMeta grammarMeta) throws Exception;
}