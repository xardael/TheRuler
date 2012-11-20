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
	GrammarMeta createGrammar(GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param id
	 */
	Grammar findGrammar(Long id) throws IOException;

	/**
	 * 
	 * @param id
	 */
	GrammarMeta findGrammarMeta(Long id) throws Exception;

	List<GrammarMeta> findAllGrammarMetas() throws IOException;

	/**
	 * 
	 * @param grammar
	 */
	void updateGrammar(Grammar grammar) throws IOException;

	/**
	 * 
	 * @param grammarMeta
	 */
	void updateGrammarMeta(GrammarMeta grammarMeta) throws IOException;

	/**
	 * 
	 * @param grammar
	 */
	void updateGrammarContent(Grammar grammar) throws IOException;

	/**
	 * 
	 * @param grammarMeta
	 */
	void deletaGrammar(GrammarMeta grammarMeta) throws IOException;

}