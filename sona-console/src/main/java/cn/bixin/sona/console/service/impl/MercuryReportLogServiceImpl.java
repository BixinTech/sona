package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.domain.es.MercuryReportLog;
import cn.bixin.sona.console.domain.req.MercuryReportLogQuery;
import cn.bixin.sona.console.repository.MercuryReportLogRepository;
import cn.bixin.sona.console.service.MercuryReportLogService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class MercuryReportLogServiceImpl implements MercuryReportLogService {
    @Resource
    private MercuryReportLogRepository mercuryReportLogRepository;
    @Override
    public void save(MercuryReportLog mercuryReportLog) {
        mercuryReportLogRepository.save(mercuryReportLog);
    }

    @Override
    public Page<MercuryReportLog> pageQuery(MercuryReportLogQuery query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(query.getCommon())) {
            queryBuilder.must(QueryBuilders.fuzzyQuery("common", query.getCommon()));
        }
        if (StringUtils.isNotBlank(query.getDesc())) {
            queryBuilder.must(QueryBuilders.fuzzyQuery("desc", query.getDesc()));
        }
        if (StringUtils.isNotBlank(query.getDetails())) {
            queryBuilder.must(QueryBuilders.fuzzyQuery("details", query.getDetails()));
        }
        if (StringUtils.isNotBlank(query.getIp())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("ip", query.getIp()));
        }
        if (StringUtils.isNotBlank(query.getModel())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("model", query.getModel()));
        }
        if (StringUtils.isNotBlank(query.getNetwork())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("network", query.getNetwork()));
        }
        if (StringUtils.isNotBlank(query.getOsVer())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("osVer", query.getOsVer()));
        }
        if (StringUtils.isNotBlank(query.getPlatform())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("platform", query.getPlatform()));
        }
        if (StringUtils.isNotBlank(query.getType())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("type", query.getType()));
        }
        if (StringUtils.isNotBlank(query.getUid())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("uid", query.getUid()));
        }
        if (Objects.nonNull(query.getFromTime()) && Objects.nonNull(query.getToTime())) {
            queryBuilder.must(QueryBuilders.rangeQuery("sendTime").from(query.getFromTime()).to(query.getToTime()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(query.getPageNo(), query.getPageSize()))
                .build();
        return mercuryReportLogRepository.search(searchQuery);
    }
}
