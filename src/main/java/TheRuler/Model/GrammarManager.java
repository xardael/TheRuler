package TheRuler.Model;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author pyty
 */
public interface GrammarManager {

	/**
	 * 
	 * @param grammarMeta
	 */
	GrammarMeta createGrammar(GrammarMeta grammarMeta) throws Exception;

	/**
	 * 
	 * @param id
	 */
	Grammar findGrammar(Long id) throws Exception;

	/**
	 * 
	 * @param id
	 */
	GrammarMeta findGrammarMeta(Long id) throws Exception;

	List<GrammarMeta> findAllGrammarMetas() throws Exception;

	/**
	 * 
	 * @param grammar
	 */
	void updateGrammar(Grammar grammar) throws Exception;

	/**
	 * 
	 * @param grammarMeta
	 */
	void updateGrammarMeta(GrammarMeta grammarMeta) throws Exception;

	/**
	 * 
	 * @param grammar
	 */
	void updateGrammarContent(Grammar grammar) throws Exception;

	/**
	 * 
	 * @param grammarMeta
	 */
	void deletaGrammar(GrammarMeta grammarMeta) throws Exception;

}