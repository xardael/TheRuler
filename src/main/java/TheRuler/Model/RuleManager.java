package TheRuler.Model;

import java.util.List;

/**
 *
 * @author Peter Gren
 */
public interface RuleManager {

    /**
     *
     * @param rule
     * @param grammarMeta
     */
    void addRule(Rule rule, GrammarMeta grammarMeta) throws Exception;

    /**
     *
     * @param id
     * @param grammarMeta
     */
    Rule findRuleById(String id, GrammarMeta grammarMeta) throws Exception;

    /**
     *
     * @param grammarMeta
     */
    List<Rule> findAllRules(GrammarMeta grammarMeta) throws Exception;

    /**
     *
     * @param id
     * @param grammarMeta
     */
    List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws Exception;

    /**
     *
     * @param rule
     * @param grammarMeta
     */
    void updateRule(Rule rule, GrammarMeta grammarMeta) throws Exception;

    /**
     *
     * @param rule
     * @param grammarMeta
     */
    void deleteRule(Rule rule, GrammarMeta grammarMeta) throws Exception;
}