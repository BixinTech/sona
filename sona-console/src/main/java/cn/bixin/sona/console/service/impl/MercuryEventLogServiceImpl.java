package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import cn.bixin.sona.console.domain.req.MercuryEventLogQuery;
import cn.bixin.sona.console.repository.MercuryEventLogRepository;
import cn.bixin.sona.console.service.MercuryEventLogService;
import cn.bixin.sona.console.utils.DateUtils;
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
public class MercuryEventLogServiceImpl implements MercuryEventLogService {
    @Resource
    private MercuryEventLogRepository mercuryEventLogRepository;

    @Override
    public void save(MercuryEventLog mercuryEventLog) {
        mercuryEventLogRepository.save(mercuryEventLog);
    }


    @Override
    public Page<MercuryEventLog> pageQuery(MercuryEventLogQuery query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(query.getUid())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("uid", query.getUid()));
        }
        if (StringUtils.isNotBlank(query.getServer())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("server", query.getServer()));
        }
        if (StringUtils.isNotBlank(query.getAddr())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("addr", query.getAddr()));
        }
        if (StringUtils.isNotBlank(query.getType())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("type", query.getType()));
        }
        if (StringUtils.isNotBlank(query.getDevice())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("device", query.getDevice()));
        }
        if (StringUtils.isNotBlank(query.getEvent())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("event", query.getEvent()));
        }
        if (StringUtils.isNotBlank(query.getContent())) {
            queryBuilder.must(QueryBuilders.fuzzyQuery("content", query.getContent()));
        }
        if (StringUtils.isNotBlank(query.getCmd())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("cmd", query.getCmd()));
        }
        if (StringUtils.isNotBlank(query.getHeader())) {
            queryBuilder.must(QueryBuilders.fuzzyQuery("header", query.getHeader()));
        }
        if (Objects.nonNull(query.getFromTime()) && Objects.nonNull(query.getToTime())) {
            queryBuilder.must(QueryBuilders.rangeQuery("sendTime").from(query.getFromTime()).to(query.getToTime()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(query.getPageNo(), query.getPageSize()))
                .build();
        return mercuryEventLogRepository.search(searchQuery);
    }
}
