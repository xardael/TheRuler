package TheRuler.Model;

import java.util.List;

public interface GrammarManager {

	/**
	 * 
	 * @param grammarMeta
	 */
	GrammarMeta createGrammar(GrammarMeta grammarMeta);

	/**
	 * 
	 * @param id
	 */
	Grammar findGrammar(Long id);

	/**
	 * 
	 * @param id
	 */
	GrammarMeta findGrammarMeta(Long id);

	List<GrammarMeta> findAllGrammarMetas();

	/**
	 * 
	 * @param grammar
	 */
	void updateGrammar(Grammar grammar);

	/**
	 * 
	 * @param grammarMeta
	 */
	void updateGrammarMeta(GrammarMeta grammarMeta);

	/**
	 * 
	 * @param grammar
	 */
	void updateGrammarContent(Grammar grammar);

	/**
	 * 
	 * @param grammarMeta
	 */
	void deletaGrammar(GrammarMeta grammarMeta);

}