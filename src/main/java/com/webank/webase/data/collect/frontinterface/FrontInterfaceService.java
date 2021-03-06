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
import com.webank.webase.data.collect.block.entity.BlockInfo;
import com.webank.webase.data.collect.front.entity.TotalTransCountInfo;
import com.webank.webase.data.collect.frontinterface.entity.PeerInfo;
import com.webank.webase.data.collect.frontinterface.entity.SyncStatus;
import com.webank.webase.data.collect.monitor.ChainTransInfo;
import com.webank.webase.data.collect.receipt.entity.TransReceipt;
import com.webank.webase.data.collect.transaction.entity.TransactionInfo;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock.Block;
import org.fisco.bcos.web3j.protocol.core.methods.response.NodeVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


@Log4j2
@Service
public class FrontInterfaceService {

    @Autowired
    private FrontRestTools frontRestTools;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * request from specific front.
     */
    private <T> T requestSpecificFront(int groupId, String frontIp, Integer frontPort,
            HttpMethod method, String uri, Object param, Class<T> clazz) {
        log.debug(
                "start requestSpecificFront. groupId:{} frontIp:{} frontPort:{} "
                        + "httpMethod:{} uri:{}",
                groupId, frontIp, frontPort, method.toString(), uri);

        uri = FrontRestTools.uriAddGroupId(groupId, uri);
        String url = String.format(FrontRestTools.FRONT_URL, frontIp, frontPort, uri);
        log.debug("requestSpecificFront. url:{}", url);

        try {
            HttpEntity<?> entity = FrontRestTools.buildHttpEntity(param);// build entity
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            JSONObject error = JSONObject.parseObject(ex.getResponseBodyAsString());
            log.error("http request fail. error:{}", JSON.toJSONString(error));
            if (error.containsKey("code") && error.containsKey("errorMessage")) {
                throw new BaseException(error.getInteger("code"),
                        error.getString("errorMessage"));
            }
            throw new BaseException(ConstantCode.REQUEST_FRONT_FAIL, ex);
        }
    }

    /**
     * get from specific front.
     */
    private <T> T getFromSpecificFront(int groupId, String frontIp, Integer frontPort, String uri,
            Class<T> clazz) {
        log.debug("start getFromSpecificFront. groupId:{} frontIp:{} frontPort:{}  uri:{}", groupId,
                frontIp, frontPort.toString(), uri);
        String url = String.format(FrontRestTools.FRONT_URL, frontIp, frontPort, uri);
        log.debug("getFromSpecificFront. url:{}", url);
        return requestSpecificFront(groupId, frontIp, frontPort, HttpMethod.GET, uri, null, clazz);
    }

    /**
     * get block by number from specific front.
     */
    public BlockInfo getBlockByNumberFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId, BigInteger blockNumber) throws BaseException {
        String uri = String.format(FrontRestTools.URI_BLOCK_BY_NUMBER, blockNumber);
        return getFromSpecificFront(groupId, frontIp, frontPort, uri, BlockInfo.class);
    }

    /**
     * get group list from specific front.
     */
    public List<String> getGroupListFromSpecificFront(String frontIp, Integer frontPort) {
        Integer groupId = Integer.MAX_VALUE;
        return getFromSpecificFront(groupId, frontIp, frontPort, FrontRestTools.URI_GROUP_PLIST,
                List.class);
    }


    /**
     * get groupPeers from specific front.
     */
    public List<String> getGroupPeersFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromSpecificFront(groupId, frontIp, frontPort, FrontRestTools.URI_GROUP_PEERS,
                List.class);
    }

    /**
     * get NodeIDList from specific front.
     */
    public List<String> getNodeIDListFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromSpecificFront(groupId, frontIp, frontPort, FrontRestTools.URI_NODEID_LIST,
                List.class);
    }

    /**
     * get peers from specific front.
     */
    public PeerInfo[] getPeersFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromSpecificFront(groupId, frontIp, frontPort, FrontRestTools.URI_PEERS,
                PeerInfo[].class);
    }

    /**
     * get peers from specific front.
     */
    public SyncStatus getSyncStatusFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromSpecificFront(groupId, frontIp, frontPort, FrontRestTools.URI_CSYNC_STATUS,
                SyncStatus.class);
    }

    /**
     * get peers.
     */
    public PeerInfo[] getPeers(Integer groupId) {
        return frontRestTools.getForEntity(groupId, FrontRestTools.URI_PEERS, PeerInfo[].class);
    }

    /**
     * get contract code.
     */
    public String getContractCode(Integer groupId, String address, BigInteger blockNumber)
            throws BaseException {
        log.debug("start getContractCode groupId:{} address:{} blockNumber:{}", groupId, address,
                blockNumber);
        String uri = String.format(FrontRestTools.URI_CODE, address, blockNumber);
        String contractCode = frontRestTools.getForEntity(groupId, uri, String.class);
        log.debug("end getContractCode. contractCode:{}", contractCode);
        return contractCode;
    }

    /**
     * get transaction receipt.
     */
    public TransReceipt getTransReceipt(Integer groupId, String transHash) throws BaseException {
        log.debug("start getTransReceipt groupId:{} transaction:{}", groupId, transHash);
        String uri = String.format(FrontRestTools.FRONT_TRANS_RECEIPT_BY_HASH_URI, transHash);
        TransReceipt transReceipt = frontRestTools.getForEntity(groupId, uri, TransReceipt.class);
        log.debug("end getTransReceipt");
        return transReceipt;
    }

    /**
     * get transaction by hash.
     */
    public TransactionInfo getTransaction(Integer groupId, String transHash)
            throws BaseException {
        log.debug("start getTransaction groupId:{} transaction:{}", groupId, transHash);
        if (StringUtils.isBlank(transHash)) {
            return null;
        }
        String uri = String.format(FrontRestTools.URI_TRANS_BY_HASH, transHash);
        TransactionInfo transInfo =
                frontRestTools.getForEntity(groupId, uri, TransactionInfo.class);
        log.debug("end getTransaction");
        return transInfo;
    }
    
//    public Block getBlockByNumberNew(Integer groupId, BigInteger blockNumber)
//            throws BaseException {
//        log.debug("start getBlockByNumber groupId:{} blockNumber:{}", groupId, blockNumber);
//        String uri = String.format(FrontRestTools.URI_BLOCK_BY_NUMBER, blockNumber);
//        Block blockInfo = null;
//        try {
//            blockInfo = frontRestTools.getForEntity(groupId, uri, Block.class);
//        } catch (Exception ex) {
//            log.info("fail getBlockByNumber,exception:{}", ex);
//        }
//        log.debug("end getBlockByNumber");
//        return blockInfo;
//    }

    /**
     * get block by number.
     */
    public BlockInfo getBlockByNumber(Integer groupId, BigInteger blockNumber)
            throws BaseException {
        log.debug("start getBlockByNumber groupId:{} blockNumber:{}", groupId, blockNumber);
        String uri = String.format(FrontRestTools.URI_BLOCK_BY_NUMBER, blockNumber);
        BlockInfo blockInfo = null;
        try {
            blockInfo = frontRestTools.getForEntity(groupId, uri, BlockInfo.class);
        } catch (Exception ex) {
            log.info("fail getBlockByNumber,exception:{}", ex);
        }
        log.debug("end getBlockByNumber");
        return blockInfo;
    }


    /**
     * request front for block by hash.
     */
    public BlockInfo getblockByHash(Integer groupId, String blockHash) throws BaseException {
        log.debug("start getblockByHash. groupId:{}  blockHash:{}", groupId, blockHash);
        String uri = String.format(FrontRestTools.URI_BLOCK_BY_HASH, blockHash);
        BlockInfo blockInfo = frontRestTools.getForEntity(groupId, uri, BlockInfo.class);
        log.debug("end getblockByHash. blockInfo:{}", JSON.toJSONString(blockInfo));
        return blockInfo;
    }


    /**
     * getTransFromFrontByHash.
     */
    public ChainTransInfo getTransInfoByHash(Integer groupId, String hash) throws BaseException {
        log.debug("start getTransInfoByHash. groupId:{} hash:{}", groupId, hash);
        TransactionInfo trans = getTransaction(groupId, hash);
        if (Objects.isNull(trans)) {
            return null;
        }
        ChainTransInfo chainTransInfo = new ChainTransInfo(trans.getFrom(), trans.getTo(),
                trans.getInput(), trans.getBlockNumber());
        log.debug("end getTransInfoByHash:{}", JSON.toJSONString(chainTransInfo));
        return chainTransInfo;
    }

    /**
     * getAddressByHash.
     */
    public String getAddressByHash(Integer groupId, String transHash) throws BaseException {
        log.debug("start getAddressByHash. groupId:{} transHash:{}", groupId, transHash);

        TransReceipt transReceipt = getTransReceipt(groupId, transHash);
        String contractAddress = transReceipt.getContractAddress();
        log.debug("end getAddressByHash. contractAddress{}", contractAddress);
        return contractAddress;
    }


    /**
     * get code from front.
     */
    public String getCodeFromFront(Integer groupId, String contractAddress, BigInteger blockNumber)
            throws BaseException {
        log.debug("start getCodeFromFront. groupId:{} contractAddress:{} blockNumber:{}", groupId,
                contractAddress, blockNumber);
        String uri = String.format(FrontRestTools.URI_CODE, contractAddress, blockNumber);
        String code = frontRestTools.getForEntity(groupId, uri, String.class);

        log.debug("end getCodeFromFront:{}", code);
        return code;
    }

    /**
     * get total transaction count
     */
    public TotalTransCountInfo getTotalTransactionCount(Integer groupId) {
        log.debug("start getTotalTransactionCount. groupId:{}", groupId);
        TotalTransCountInfo totalCount = frontRestTools.getForEntity(groupId,
                FrontRestTools.URI_TRANS_TOTAL, TotalTransCountInfo.class);
        log.debug("end getTotalTransactionCount:{}", totalCount);
        return totalCount;
    }

    /**
     * get transaction hash by block number
     */
    public List<TransactionInfo> getTransByBlockNumber(Integer groupId, BigInteger blockNumber) {
        log.debug("start getTransByBlockNumber. groupId:{} blockNumber:{}", groupId, blockNumber);
        BlockInfo blockInfo = getBlockByNumber(groupId, blockNumber);
        if (blockInfo == null) {
            return null;
        }
        List<TransactionInfo> transInBLock = blockInfo.getTransactions();
        log.debug("end getTransByBlockNumber. transInBLock:{}", JSON.toJSONString(transInBLock));
        return transInBLock;
    }

    /**
     * get group peers
     */
    public List<String> getGroupPeers(Integer groupId) {
        log.debug("start getGroupPeers. groupId:{}", groupId);
        List<String> groupPeers =
                frontRestTools.getForEntity(groupId, FrontRestTools.URI_GROUP_PEERS, List.class);
        log.debug("end getGroupPeers. groupPeers:{}", JSON.toJSONString(groupPeers));
        return groupPeers;
    }

    /**
     * get group peers
     */
    public List<String> getObserverList(Integer groupId) {
        log.debug("start getObserverList. groupId:{}", groupId);
        List<String> observers = frontRestTools.getForEntity(groupId,
                FrontRestTools.URI_GET_OBSERVER_LIST, List.class);
        log.info("end getObserverList. observers:{}", JSON.toJSONString(observers));
        return observers;
    }

    /**
     * get observer list from specific front
     */
    public List<String> getObserverListFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromSpecificFront(groupId, frontIp, frontPort,
                FrontRestTools.URI_GET_OBSERVER_LIST, List.class);
    }

    /**
     * get consensusStatus
     */
    public String getConsensusStatus(Integer groupId) {
        log.debug("start getConsensusStatus. groupId:{}", groupId);
        String consensusStatus = frontRestTools.getForEntity(groupId,
                FrontRestTools.URI_CONSENSUS_STATUS, String.class);
        log.debug("end getConsensusStatus. consensusStatus:{}", consensusStatus);
        return consensusStatus;
    }

    /**
     * get syncStatus
     */
    public SyncStatus getSyncStatus(Integer groupId) {
        log.debug("start getSyncStatus. groupId:{}", groupId);
        SyncStatus ststus = frontRestTools.getForEntity(groupId, FrontRestTools.URI_CSYNC_STATUS,
                SyncStatus.class);
        log.debug("end getSyncStatus. ststus:{}", JSON.toJSONString(ststus));
        return ststus;
    }

    /**
     * get latest block number
     */
    public BigInteger getLatestBlockNumber(Integer groupId) {
        log.debug("start getLatestBlockNumber. groupId:{}", groupId);
        BigInteger latestBlockNmber = frontRestTools.getForEntity(groupId,
                FrontRestTools.URI_BLOCK_NUMBER, BigInteger.class);
        log.debug("end getLatestBlockNumber. latestBlockNmber:{}", latestBlockNmber);
        return latestBlockNmber;
    }

    /**
     * get sealerList.
     */
    public List<String> getSealerList(Integer groupId) {
        log.debug("start getSealerList. groupId:{}", groupId);
        List getSealerList = frontRestTools.getForEntity(groupId,
                FrontRestTools.URI_GET_SEALER_LIST, List.class);
        log.debug("end getSealerList. getSealerList:{}", JSON.toJSONString(getSealerList));
        return getSealerList;
    }

    /**
     * get sealer list from specific front
     */
    public List<String> getSealerListFromSpecificFront(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromSpecificFront(groupId, frontIp, frontPort, FrontRestTools.URI_GET_SEALER_LIST,
                List.class);
    }

    /**
     * get config by key
     */
    public String getSystemConfigByKey(Integer groupId, String key) {
        log.debug("start getSystemConfigByKey. groupId:{}", groupId);
        String uri = String.format(FrontRestTools.URI_SYSTEMCONFIG_BY_KEY, key);
        String config = frontRestTools.getForEntity(groupId, uri, String.class);
        log.debug("end getSystemConfigByKey. config:{}", config);
        return config;
    }

    /**
     * get front's encryptType
     */
    public Integer getEncryptTypeFromSpecificFront(String nodeIp, Integer frontPort) {
        log.debug("start getEncryptTypeFromSpecificFront. nodeIp:{},frontPort:{}", nodeIp,
                frontPort);
        Integer groupId = Integer.MAX_VALUE;
        int encryptType = getFromSpecificFront(groupId, nodeIp, frontPort,
                FrontRestTools.URI_ENCRYPT_TYPE, Integer.class);
        log.debug("end getEncryptTypeFromSpecificFront. encryptType:{}", encryptType);
        return encryptType;
    }

    public String getClientVersion(String frontIp, Integer frontPort, Integer groupId) {
        log.debug("start getClientVersion. groupId:{}", groupId);
        NodeVersion.Version clientVersion = getFromSpecificFront(groupId, frontIp, frontPort,
                FrontRestTools.URI_GET_CLIENT_VERSION, NodeVersion.Version.class);
        log.debug("end getClientVersion. consensusStatus:{}", clientVersion);
        return clientVersion.getVersion();
    }

}
