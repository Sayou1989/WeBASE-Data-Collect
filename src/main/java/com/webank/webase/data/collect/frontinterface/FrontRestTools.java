/**
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webank.webase.data.collect.frontinterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webank.webase.data.collect.base.code.ConstantCode;
import com.webank.webase.data.collect.base.exception.BaseException;
import com.webank.webase.data.collect.base.properties.ConstantProperties;
import com.webank.webase.data.collect.frontgroupmap.entity.FrontGroup;
import com.webank.webase.data.collect.frontgroupmap.entity.FrontGroupMapCache;
import com.webank.webase.data.collect.frontinterface.entity.FailInfo;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * about http request for WeBASE-Front.
 */
@Log4j2
@Service
public class FrontRestTools {

    public static final String FRONT_URL = "http://%1s:%2d/WeBASE-Front/%3s";
    public static final String URI_GROUP_PLIST = "web3/groupList";
    public static final String URI_BLOCK_BY_NUMBER = "web3/blockByNumber/%1d";
    public static final String URI_BLOCK_BY_HASH = "web3/blockByHash/%1s";
    public static final String URI_TRANS_BY_HASH = "web3/transaction/%1s";
    public static final String FRONT_TRANS_RECEIPT_BY_HASH_URI = "web3/transactionReceipt/%1s";
    public static final String URI_TRANS_TOTAL = "web3/transaction-total";
    public static final String URI_GROUP_PEERS = "web3/groupPeers";
    public static final String URI_NODEID_LIST = "web3/nodeIdList";
    public static final String URI_PEERS = "web3/peers";
    public static final String URI_CONSENSUS_STATUS = "web3/consensusStatus";
    public static final String URI_CSYNC_STATUS = "web3/syncStatus";
    public static final String URI_SYSTEMCONFIG_BY_KEY = "web3/systemConfigByKey/%1s";
    public static final String URI_CODE = "web3/code/%1s/%2s";
    public static final String URI_BLOCK_NUMBER = "web3/blockNumber";
    public static final String URI_GET_SEALER_LIST = "web3/sealerList";
    public static final String URI_GET_OBSERVER_LIST = "web3/observerList";
    public static final String URI_GET_CLIENT_VERSION = "web3/clientVersion";
    public static final String URI_ENCRYPT_TYPE = "encrypt";

    // 不需要在url的前面添加groupId的
    private static final List<String> URI_NOT_PREPEND_GROUP_ID = Arrays.asList(URI_ENCRYPT_TYPE);
    private static Map<String, FailInfo> failRequestMap = new HashMap<>();

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConstantProperties cproperties;
    @Autowired
    private FrontGroupMapCache frontGroupMapCache;

    /**
     * get from front for entity.
     */
    public <T> T getForEntity(Integer groupId, String uri, Class<T> clazz) {
        return restTemplateExchange(groupId, uri, HttpMethod.GET, null, clazz);
    }

    /**
     * post from front for entity.
     */
    public <T> T postForEntity(Integer groupId, String uri, Object params, Class<T> clazz) {
        return restTemplateExchange(groupId, uri, HttpMethod.POST, params, clazz);
    }

    /**
     * delete from front for entity.
     */
    public <T> T deleteForEntity(Integer groupId, String uri, Object params, Class<T> clazz) {
        return restTemplateExchange(groupId, uri, HttpMethod.DELETE, params, clazz);
    }

    /**
     * restTemplate exchange.
     */
    @SuppressWarnings("rawtypes")
    private <T> T restTemplateExchange(int groupId, String uri, HttpMethod method, Object param,
            Class<T> clazz) {
        List<FrontGroup> frontList = frontGroupMapCache.getMapListByGroupId(groupId);
        if (frontList == null || frontList.size() == 0) {
            log.error("fail restTemplateExchange. frontList is empty");
            throw new BaseException(ConstantCode.FRONT_LIST_NOT_FOUNT);
        }
        ArrayList<FrontGroup> list = new ArrayList<>(frontList);
        while (list != null && list.size() > 0) {
            String url = buildFrontUrl(list, uri, method);// build url
            try {
                HttpEntity entity = buildHttpEntity(param);// build entity
                ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);
                return response.getBody();
            } catch (ResourceAccessException ex) {
                log.warn("fail restTemplateExchange", ex);
                setFailCount(url, method.toString());
                if (isServiceSleep(url, method.toString())) {
                    throw ex;
                }
                log.info("continue next front", ex);
                continue;
            } catch (HttpStatusCodeException ex) {
                JSONObject error = JSONObject.parseObject(ex.getResponseBodyAsString());
                log.error("http request fail. error:{}", JSON.toJSONString(error));
                if (error.containsKey("code") && error.containsKey("errorMessage")) {
                    throw new BaseException(error.getInteger("code"),
                            error.getString("errorMessage"), ex);
                }
                throw new BaseException(ConstantCode.REQUEST_FRONT_FAIL, ex);
            }
        }
        return null;
    }
    
    /**
     * append groupId to uri.
     */
    public static String uriAddGroupId(Integer groupId, String uri) {
        if (groupId == null || StringUtils.isBlank(uri)) {
            return null;
        }

        final String tempUri = uri.contains("?") ? uri.substring(0, uri.indexOf("?")) : uri;
        long countNotAppend =
                URI_NOT_PREPEND_GROUP_ID.stream().filter(u -> u.contains(tempUri)).count();
        if (countNotAppend > 0) {
            return uri;
        }
        return groupId + "/" + uri;
    }

    /**
     * check url status.
     */
    private boolean isServiceSleep(String url, String methType) {
        // get failInfo
        String key = buildKey(url, methType);
        FailInfo failInfo = failRequestMap.get(key);

        // cehck server status
        if (failInfo == null) {
            return false;
        }
        int failCount = failInfo.getFailCount();
        Long subTime = Duration.between(failInfo.getLatestTime(), Instant.now()).toMillis();
        if (failCount > cproperties.getMaxRequestFail()
                && subTime < cproperties.getSleepWhenHttpMaxFail()) {
            return true;
        } else if (subTime > cproperties.getSleepWhenHttpMaxFail()) {
            // service is sleep
            deleteKeyOfMap(failRequestMap, key);
        }
        return false;

    }

    /**
     * set request fail times.
     */
    private void setFailCount(String url, String methodType) {
        // get failInfo
        String key = buildKey(url, methodType);
        FailInfo failInfo = failRequestMap.get(key);
        if (failInfo == null) {
            failInfo = new FailInfo();
            failInfo.setFailUrl(url);
        }

        // reset failInfo
        failInfo.setLatestTime(Instant.now());
        failInfo.setFailCount(failInfo.getFailCount() + 1);
        failRequestMap.put(key, failInfo);
        log.info("the latest failInfo:{}", JSON.toJSONString(failRequestMap));
    }


    /**
     * build key description: frontIp$frontPort example: 2651654951545$8081.
     */
    private String buildKey(String url, String methodType) {
        return url.hashCode() + "$" + methodType;
    }


    /**
     * delete key of map.
     */
    private static void deleteKeyOfMap(Map<String, FailInfo> map, String rkey) {
        log.info("start deleteKeyOfMap. rkey:{} map:{}", rkey, JSON.toJSONString(map));
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (rkey.equals(key)) {
                iter.remove();
            }
        }
        log.info("end deleteKeyOfMap. rkey:{} map:{}", rkey, JSON.toJSONString(map));
    }


    /**
     * build url of front service.
     */
    private String buildFrontUrl(ArrayList<FrontGroup> list, String uri, HttpMethod httpMethod) {
        Collections.shuffle(list);// random one
        log.debug("====================map list:{}", JSON.toJSONString(list));
        Iterator<FrontGroup> iterator = list.iterator();
        while (iterator.hasNext()) {
            FrontGroup frontGroup = iterator.next();
            log.debug("============frontGroup:{}", JSON.toJSONString(frontGroup));

            uri = uriAddGroupId(frontGroup.getGroupId(), uri);// append groupId to uri
            String url = String
                    .format(FRONT_URL, frontGroup.getFrontIp(), frontGroup.getFrontPort(), uri)
                    .replaceAll(" ", "");
            iterator.remove();

            if (isServiceSleep(url, httpMethod.toString())) {
                log.warn("front url[{}] is sleep,jump over", url);
                continue;
            }
            return url;
        }
        log.info("end buildFrontUrl. url is null");
        return null;
    }

    /**
     * build httpEntity.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static HttpEntity buildHttpEntity(Object param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String paramStr = null;
        if (Objects.nonNull(param)) {
            paramStr = JSON.toJSONString(param);
        }
        HttpEntity requestEntity = new HttpEntity(paramStr, headers);
        return requestEntity;
    }
}
