package com.winning.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.winning.kbms.medical.entity.LinstrFileInfo;

public class PaodingTest
{
    public static void main (String[] args) throws IOException, ParseException
    {
        
//        IndexWriter indexWriter = null;
//        Document doc = null;
////        Analyzer analyzer = new StandardAnalyzer (Version.LUCENE_45) ;
//        Analyzer analyzer = new IKAnalyzer ()  ;
//        Directory dir = FSDirectory.open (new File ("F:\\luc002"));
//        try
//        {
//            IndexWriterConfig config = new IndexWriterConfig (Version.LUCENE_45,analyzer);
//            indexWriter = new IndexWriter (dir,config);
////            indexWriter = new IndexWriter (dir, null);
//            doc = new Document ();
//            doc.add (new Field ("name", "注射用果糖二磷酸钠", TextField.TYPE_STORED));
//            indexWriter.addDocument (doc);
//            indexWriter.commit ();
//            indexWriter.close ();
//        }
//        catch (Exception e)
//        {
//        }
        searchLinstrFileInfoByTerm("name", "果糖二磷酸钠注射液",3);
       
    }
    
    
    public static List <LinstrFileInfo> searchLinstrFileInfoByTerm (String field, String name, int num) throws IOException, ParseException
    {
        List <LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        String path ="F:\\luc002";
        Directory dir = FSDirectory.open (new File (path));
        DirectoryReader directoryReader = DirectoryReader.open (dir);
        IndexSearcher searcher = new IndexSearcher (directoryReader);
        BooleanQuery booleanQuery = new BooleanQuery();
        Analyzer analyzer = new IKAnalyzer ()  ;
//        Analyzer analyzer = new StandardAnalyzer (Version.LUCENE_45) ;
//        Query query = new TermQuery (new Term (field, name));
        QueryParser parser01 = new QueryParser (Version.LUCENE_45, field, analyzer);
//        Query query1 = new TermQuery (new Term (field, "2"));
        booleanQuery.add (parser01.parse (name), BooleanClause.Occur.SHOULD);
        System.out.println (parser01.parse (name));
//        booleanQuery.add (query, BooleanClause.Occur.SHOULD);
        TopDocs tds = searcher.search (booleanQuery, num);
        System.out.println ("一共查询了：" + tds.totalHits);
        list = new ArrayList <LinstrFileInfo> ();
        for (ScoreDoc sd : tds.scoreDocs)
        {
            Document doc = searcher.doc (sd.doc);
            list.add (info);
            System.out.println ("搜索的结果：" + doc.get ("name") );
        }
        return null;
    }
}
