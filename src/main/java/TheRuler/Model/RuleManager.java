package TheRuler.Model;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author pyty
 */
public interface RuleManager {

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	void addRule(Rule rule, GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param id
	 * @param grammarMeta
	 */
	Rule findRuleById(String id, GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param grammarMeta
	 */
	List<Rule> findAllRules(GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param id
	 * @param grammarMeta
	 */
	List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	void updateRule(Rule rule, GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	void deleteRule(Rule rule, GrammarMeta grammarMeta) throws IOException;

}