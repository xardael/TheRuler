package TheRuler.Model;

import java.util.List;

public interface RuleManager {

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	void addRule(Rule rule, GrammarMeta grammarMeta);

	/**
	 * 
	 * @param id
	 * @param grammarMeta
	 */
	Rule findRuleById(String id, GrammarMeta grammarMeta);

	/**
	 * 
	 * @param grammarMeta
	 */
	List<Rule> findAllRules(GrammarMeta grammarMeta);

	/**
	 * 
	 * @param id
	 * @param grammarMeta
	 */
	List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta);

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	void updateRule(Rule rule, GrammarMeta grammarMeta);

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	void deleteRule(Rule rule, GrammarMeta grammarMeta);

}