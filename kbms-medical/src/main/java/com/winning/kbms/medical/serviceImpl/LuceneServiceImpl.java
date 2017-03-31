package com.winning.kbms.medical.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.winning.kbms.medical.entity.LinstrFileInfo;
import com.winning.kbms.medical.service.LuceneService;
import com.winning.kbms.medical.vo.Pager;

@Service
public class LuceneServiceImpl implements LuceneService, InitializingBean {
    private String compressFolderPath;

    @Override
    public List<LinstrFileInfo> findLinstrFileWithLucene(String arg)
            throws IOException, ParseException {

        List<LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        Directory dir = FSDirectory.open(new File(compressFolderPath));
        Analyzer analyzer = new IKAnalyzer();
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        BooleanQuery bQuery = new BooleanQuery();
        QueryParser parser01 = new QueryParser(Version.LUCENE_45,
                "productNameCn", analyzer);

        QueryParser parser02 = new QueryParser(Version.LUCENE_45, "tradeName",
                analyzer);

        Query query01 = parser01.parse(arg);
        Query query02 = parser02.parse(arg);

        bQuery.add(query01, BooleanClause.Occur.SHOULD);
        bQuery.add(query02, BooleanClause.Occur.SHOULD);

        TopDocs topDocs = indexSearcher.search(bQuery, null, 10);
        ScoreDoc[] hits = topDocs.scoreDocs;

        list = new ArrayList<LinstrFileInfo>();
        for (int n = 0; n < hits.length; n++) {
            info = new LinstrFileInfo();
            Document hitDoc = indexSearcher.doc(hits[n].doc);
            info.setId(Integer.parseInt(hitDoc.get("id")));
            info.setProductNameCn(hitDoc.get("productNameCn"));
            info.setTradeName(hitDoc.get("tradeName"));
            info.setSpecification(hitDoc.get("specification"));
            list.add(info);
        }
        return list;
    }

    @Override
    public Pager findLinstrFileInfoList(String arg, String type, int startPage,
            int maxRows) throws IOException, ParseException {
        // String MY_WEBAPP_REAL_PATH =
        // Constants.SPRING_CONTAINER_WEB.getServletContext().getRealPath("");
        // String MY_WEBAPP_REAL_PATH = "F:";
        Pager pager = new Pager(startPage, maxRows);
        List<LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        File lFile = new File(compressFolderPath);
        Directory dir = FSDirectory.open(lFile);
        Analyzer analyzer = new IKAnalyzer();
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        BooleanQuery bQuery = new BooleanQuery();
        QueryParser parser01 = new QueryParser(Version.LUCENE_45,
                "productNameCn", analyzer);

        QueryParser parser03 = new QueryParser(Version.LUCENE_45, "indication",
                analyzer);

        QueryParser parser04 = new QueryParser(Version.LUCENE_45, "taboo",
                analyzer);

        Query query01 = parser01.parse(arg);
        Query query03 = parser03.parse(arg);
        Query query04 = parser04.parse(arg);

        if ("00".equals(type)) {
            bQuery.add(query01, BooleanClause.Occur.SHOULD);
            // bQuery.add (query02, BooleanClause.Occur.SHOULD);
        } else if ("01".equals(type)) {
            bQuery.add(query03, BooleanClause.Occur.SHOULD);
        } else if ("02".equals(type)) {
            bQuery.add(query04, BooleanClause.Occur.SHOULD);
        }

        TopScoreDocCollector results = TopScoreDocCollector.create(
                pager.getEndIndex(), false);

        // TopDocs topDocs =
        indexSearcher.search(bQuery, results);
        TopDocs topDocs = results.topDocs(pager.getStartIndex(),
                pager.getEndIndex());
        pager.setTotalRows(topDocs.totalHits);

        ScoreDoc[] hits = topDocs.scoreDocs;

        list = new ArrayList<LinstrFileInfo>();
        for (int n = 0; n < hits.length; n++) {
            Document hitDoc = indexSearcher.doc(hits[n].doc);
            info = generateLinstrFileInfo(hitDoc);
            list.add(info);
        }
        pager.setResults(list);
        return pager;
    }

    private LinstrFileInfo generateLinstrFileInfo(Document hitDoc) {

        LinstrFileInfo info = null;
        if (null != hitDoc) {
            info = new LinstrFileInfo();
            info.setId(Integer.parseInt(hitDoc.get("id")));
            info.setProductNameCn(hitDoc.get("productNameCn"));
            info.setTradeName(hitDoc.get("tradeName"));
            info.setComponent(hitDoc.get("component"));
            info.setIndication(hitDoc.get("indication"));
            info.setSpecification(hitDoc.get("specification"));
            info.setUsageDosage(hitDoc.get("usageDosage"));
            info.setBadReaction(hitDoc.get("badReaction"));
            info.setTaboo(hitDoc.get("taboo"));
            info.setAttentionItem(hitDoc.get("attentionItem"));
            info.setWomanMedicine(hitDoc.get("womanMedicine"));
            info.setChildrenMedicine(hitDoc.get("childrenMedicine"));
            info.setAgednessMedicine(hitDoc.get("agednessMedicine"));
            info.setMedicineInteracts(hitDoc.get("medicineInteracts"));
            info.setMedicineBellyful(hitDoc.get("medicineBellyful"));
            info.setPharmacologyPoisons(hitDoc.get("pharmacologyPoisons"));
            info.setMedicineDynamics(hitDoc.get("medicineDynamics"));
            info.setQuality(hitDoc.get("quality"));
            info.setStore(hitDoc.get("store"));
            info.setCasing(hitDoc.get("casing"));
            info.setValidPeriod(hitDoc.get("validPeriod"));
            info.setExecuteStandard(hitDoc.get("executeStandard"));
            info.setApproveDocNo(hitDoc.get("approveDocNo"));
            info.setEiMdMcNo(hitDoc.get("eiMdMcNo"));

            info.setProductNameEn(hitDoc.get("productNameEn"));

            info.setPinyin(hitDoc.get("pinyin"));
            info.setDrugForm(hitDoc.get("drugForm"));

            info.setTaboo(hitDoc.get("taboo"));

        }

        return info;

    }

    @Override
    public Map<String, Object> findLinstrFileInfoWithId(String id)
            throws IOException {

        LinstrFileInfo info = null;
        File lFile = new File(compressFolderPath);
        Directory dir = FSDirectory.open(lFile);
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(directoryReader);
        BooleanQuery booleanQuery = new BooleanQuery();
        Query query = new TermQuery(new Term("id", id + ""));
        booleanQuery.add(query, BooleanClause.Occur.MUST);
        TopDocs tds = searcher.search(booleanQuery, 1);
        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            info = generateLinstrFileInfo(doc);
            return DrugServiceImpl.generateMap(info);
        }
        return null;
    }

    @Override
    public Pager findLinstrFileInfoListForMMAP(String mc, String tymc,
            String pzwh, String jx, int startPage, int maxRows)
            throws IOException, ParseException {
        Pager pager = new Pager(startPage, maxRows);
        List<LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        File lFile = new File(compressFolderPath);
        Directory dir = FSDirectory.open(lFile);
        Analyzer analyzer = new IKAnalyzer();
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        BooleanQuery bQuery = new BooleanQuery();
        QueryParser parser01 = new QueryParser(Version.LUCENE_45,
                "productNameCn", analyzer);

        QueryParser parser02 = new QueryParser(Version.LUCENE_45, "tradeName",
                analyzer);

        QueryParser parser03 = new QueryParser(Version.LUCENE_45, "drugForm",
                analyzer);

        QueryParser parser04 = new QueryParser(Version.LUCENE_45,
                "approveDocNo", analyzer);

        if (!StringUtils.isEmpty(pzwh)) {
            Query query04 = parser04.parse(pzwh);
            bQuery.add(query04, BooleanClause.Occur.SHOULD);
        }

        if (!StringUtils.isEmpty(mc)) {
            Query query01 = parser01.parse(mc);
            bQuery.add(query01, BooleanClause.Occur.SHOULD);
        }

        if (!StringUtils.isEmpty(tymc)) {
            Query query02 = parser02.parse(tymc);
            bQuery.add(query02, BooleanClause.Occur.SHOULD);
        }

        if (!StringUtils.isEmpty(jx)) {
            Query query03 = parser03.parse(jx);
            bQuery.add(query03, BooleanClause.Occur.SHOULD);
        }

        TopScoreDocCollector results = TopScoreDocCollector.create(
                pager.getEndIndex(), false);

        // TopDocs topDocs =
        indexSearcher.search(bQuery, results);
        TopDocs topDocs = results.topDocs(pager.getStartIndex(),
                pager.getEndIndex());
        pager.setTotalRows(topDocs.totalHits);

        ScoreDoc[] hits = topDocs.scoreDocs;

        list = new ArrayList<LinstrFileInfo>();
        for (int n = 0; n < hits.length; n++) {
            Document hitDoc = indexSearcher.doc(hits[n].doc);
            info = generateLinstrFileInfo(hitDoc);
            list.add(info);
        }
        pager.setResults(list);
        return pager;
    }

    // 精确查找
    public List<LinstrFileInfo> searchLinstrFileInfoByTerm(String field,
            String[] names, int num) throws IOException {
        List<LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        File lFile = new File(compressFolderPath);
        Directory dir = FSDirectory.open(lFile);
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(directoryReader);
        BooleanQuery booleanQuery = new BooleanQuery();
        if (!StringUtils.isEmpty(field)) {
            if (names != null && names.length > 0) {
                for (String name : names) {
                    Query query = new TermQuery(new Term(field, name));
                    booleanQuery.add(query, BooleanClause.Occur.SHOULD);
                }
            }
        }
        TopDocs tds = searcher.search(booleanQuery, num);
        list = new ArrayList<LinstrFileInfo>();
        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            info = generateLinstrFileInfo(doc);
            list.add(info);
        }
        return list;
    }

    @Override
    public List<LinstrFileInfo> findLinstrFileInfoWithTerms(String field,
            String[] params) {
        List<LinstrFileInfo> list = null;
        try {
            list = searchLinstrFileInfoByTerm(field, params, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Pager findLinstrFileInfosWithFileds(int startPage, int maxRows,
            String param, String... fields) throws IOException, ParseException {
        Pager pager = new Pager(startPage, maxRows);
        List<LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        File lFile = new File(compressFolderPath);
        Directory dir = FSDirectory.open(lFile);
        Analyzer analyzer = new IKAnalyzer();

        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        BooleanQuery bQuery = new BooleanQuery();
        for (String field : fields) {
            QueryParser parser = new QueryParser(Version.LUCENE_45, field,
                    analyzer);
            Query query = parser.parse(param);
            bQuery.add(query, BooleanClause.Occur.SHOULD);
        }
        TopScoreDocCollector results = TopScoreDocCollector.create(
                pager.getEndIndex(), false);
        indexSearcher.search(bQuery, results);
        TopDocs topDocs = results.topDocs(pager.getStartIndex(),
                pager.getEndIndex());
        pager.setTotalRows(topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        list = new ArrayList<LinstrFileInfo>();
        for (int n = 0; n < hits.length; n++) {
            Document hitDoc = indexSearcher.doc(hits[n].doc);
            info = generateLinstrFileInfo(hitDoc);
            list.add(info);
        }
        pager.setResults(list);
        return pager;
    }

    /**
     * 
     * @作者: s_lei
     * @时间: Apr 30, 2014 11:10:53 AM
     * @param params
     * @param num
     * @return
     * @throws IOException
     * @throws ParseException
     * @备注:
     */
    public List<LinstrFileInfo> searchLinstrFileInfoByParams(
            Map<String, String> params, int num) throws IOException,
            ParseException {
        List<LinstrFileInfo> list = null;
        LinstrFileInfo info = null;
        File lFile = new File(compressFolderPath);
        Directory dir = FSDirectory.open(lFile);
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(directoryReader);
        Analyzer analyzer = new IKAnalyzer();
        BooleanQuery booleanQuery = new BooleanQuery();
        if (params != null) {
            for (Entry<String, String> entry : params.entrySet()) {
                QueryParser parser = new QueryParser(Version.LUCENE_45,
                        entry.getKey(), analyzer);
                Query query = parser.parse(entry.getValue());
                booleanQuery.add(query, BooleanClause.Occur.MUST);
            }
        }
        TopDocs tds = searcher.search(booleanQuery, num);
        list = new ArrayList<LinstrFileInfo>();
        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            info = generateLinstrFileInfo(doc);
            list.add(info);
        }
        return list;
    }

    /**
     * 此处解压luc.zip包的。
     */
    @Override
    public void afterPropertiesSet() throws Exception {
//        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
//
//        InputStream is = defaultResourceLoader.getResource("classpath:luc.zip")
//                .getInputStream();
//        // System.out.println("===================================Constants.SYS_TMP_DIR_PATH:" +
//        // Constants.SYS_TMP_DIR_PATH);
//
//        if (Constants.SYS_TMP_DIR_PATH.endsWith("\\")
//                || Constants.SYS_TMP_DIR_PATH.endsWith("/")) {
//            compressFolderPath = Constants.SYS_TMP_DIR_PATH + "luc" + dateStr;
//
//        } else {
//            compressFolderPath = Constants.SYS_TMP_DIR_PATH + File.separator
//                    + "luc" + dateStr;
//        }
//
//        File compressFolder = new File(compressFolderPath);
//        if (!compressFolder.exists())
//            compressFolder.mkdirs();
//        CompressFileUtils.unzip(is, compressFolderPath);
    }

}
