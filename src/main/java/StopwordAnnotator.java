import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotator;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;

import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.Pair;

/**
 * User: jconwell
 * CoreNlp Annotator that checks if in coming token is a stopword
 */
public class StopwordAnnotator implements Annotator, CoreAnnotation<Pair<Boolean, Boolean>> {
    /**
     * Property key to specify the comma delimited list of custom stopwords
     */
    public static final String STOPWORDS_LIST = "stopword-list";

    /**
     * Property key to specify if stopword list is case insensitive
     */
    public static final String IGNORE_STOPWORD_CASE = "ignore-stopword-case";

    /**
     * Property key to specify of StopwordAnnotator should check word lemma as stopword
     */
    public static final String CHECK_LEMMA = "check-lemma";

    private static Class<? extends Pair> boolPair = Pair.makePair(true, true).getClass();

    private Properties props;
    private CharArraySet stopwords;
    private boolean checkLemma;

    public StopwordAnnotator(String name, Properties props) {
        this.props = props;

        this.checkLemma = Boolean.parseBoolean(props.getProperty(CHECK_LEMMA, "false"));

        if (this.props.containsKey(STOPWORDS_LIST)) {
            String stopwordList = props.getProperty(STOPWORDS_LIST);
            boolean ignoreCase = Boolean.parseBoolean(props.getProperty(IGNORE_STOPWORD_CASE, "false"));
            this.stopwords = getStopWordList(stopwordList, ignoreCase);
        } else {
            this.stopwords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
        }
    }

    @Override
    public void annotate(Annotation annotation) {
        if (stopwords != null && stopwords.size() > 0 && annotation.containsKey(TokensAnnotation.class)) {
            List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
            for (CoreLabel token : tokens) {
                boolean isWordStopword = stopwords.contains(token.word().toLowerCase());
                boolean isLemmaStopword = checkLemma && stopwords.contains(token.word().toLowerCase());
                Pair<Boolean, Boolean> pair = Pair.makePair(isWordStopword, isLemmaStopword);
                token.set(StopwordAnnotator.class, pair);
            }
        }
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied(){
        return Collections.singleton(this.getClass());
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requires() {
        Set<Class<? extends CoreAnnotation>> theSet = new HashSet<>();
        theSet.add(CoreAnnotations.TokensAnnotation.class);
        theSet.add(CoreAnnotations.SentencesAnnotation.class);
        if (checkLemma) {
            theSet.add(CoreAnnotations.PartOfSpeechAnnotation.class);
            theSet.add(CoreAnnotations.LemmaAnnotation.class);
        }
        return theSet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Pair<Boolean, Boolean>> getType() {
        return (Class<Pair<Boolean, Boolean>>) boolPair;
    }

    public static CharArraySet getStopWordList(String stopwordList, boolean ignoreCase) {
        String[] terms = stopwordList.split(",");
        CharArraySet stopwordSet = new CharArraySet(terms.length, ignoreCase);
        stopwordSet.addAll(Arrays.asList(terms));
        return CharArraySet.unmodifiableSet(stopwordSet);
    }
}