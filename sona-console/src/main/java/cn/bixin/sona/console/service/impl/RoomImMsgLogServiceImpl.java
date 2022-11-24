package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import cn.bixin.sona.console.domain.req.RoomImMsgLogQuery;
import cn.bixin.sona.console.repository.RoomImMsgLogRepository;
import cn.bixin.sona.console.service.RoomImMsgLogService;
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
public class RoomImMsgLogServiceImpl implements RoomImMsgLogService {
    @Resource
    private RoomImMsgLogRepository repository;
    @Override
    public RoomImMsgLog save(RoomImMsgLog roomImMsgLog) {
        return repository.save(roomImMsgLog);
    }

    @Override
    public Page<RoomImMsgLog> pageQuery(RoomImMsgLogQuery query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(query.getMessageId())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("messageId", query.getMessageId()));
        }
        if (StringUtils.isNotBlank(query.getRoomId())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("roomId", query.getRoomId()));
        }
        if (StringUtils.isNotBlank(query.getMsgType())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("msgType", query.getMsgType()));
        }
        if (StringUtils.isNotBlank(query.getProductCode())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("productCode", query.getProductCode()));
        }
        if (StringUtils.isNotBlank(query.getPriority())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("priority", query.getPriority()));
        }
        if (StringUtils.isNotBlank(query.getUid())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("uid", query.getUid()));
        }
        if (StringUtils.isNotBlank(query.getContent())) {
            queryBuilder.must(QueryBuilders.fuzzyQuery("content", query.getContent()));
        }
        if (StringUtils.isNotBlank(query.getToUid())) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("toUid", query.getToUid()));
        }
        if (Objects.nonNull(query.getFromTime()) && Objects.nonNull(query.getToTime())) {
            queryBuilder.must(QueryBuilders.rangeQuery("sendTime").from(query.getFromTime()).to(query.getToTime()));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(query.getPageNo(), query.getPageSize()))
                .build();
        return repository.search(searchQuery);
    }
}
