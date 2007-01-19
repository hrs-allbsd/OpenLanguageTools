package org.jvnet.olt.lucene;

import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import java.util.*;

/**
 * Custom implementation of QueryParser for
 * real Levenstein based fuzzy queries
 */
public class CustomPhraseParser extends QueryParser {
    
    // maximum number of terms that we will handle
    public final int MAX_TERMS = 100;
    
    /**
     * Create new instance of CustomPhraseParser
     *
     * @param field the to search
     * @param analyzer the analyzer to use
     */
    public CustomPhraseParser(String field, Analyzer analyzer) {
        super(field, analyzer);
    }
    
    /**
     * Parse a query string
     *
     * @param queryText the query to parse
     *
     * @throws ParseException if there is a problem to parse the query
     */
    public Query parse(String queryText) throws ParseException {
        // do the parsing by yourself ;-)
        Query orig = super.parse(queryText);
        
        // make boolean query and add there our fuzzy queries
        HashSet terms = new HashSet();
        orig.extractTerms(terms);
        
        Iterator it = terms.iterator();
        BooleanQuery query = new BooleanQuery();
        while(it.hasNext()) {
            Term term = (Term)it.next();
            Query fq = new FuzzyQuery(term);
            query.add(fq, BooleanClause.Occur.SHOULD);
        }
        return query;
    }
    
}

