/**
 * Copyright 2014-2020  the original author or authors.
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
package com.webank.webase.data.collect.scheduler;


import com.webank.webase.data.collect.base.enums.DataStatus;
import com.webank.webase.data.collect.base.properties.ConstantProperties;
import com.webank.webase.data.collect.block.BlockService;
import com.webank.webase.data.collect.group.GroupService;
import com.webank.webase.data.collect.group.entity.TbGroup;
import com.webank.webase.data.collect.monitor.MonitorService;
import com.webank.webase.data.collect.transaction.TransactionService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * delete block/trans/monitorTrans data task
 */
@Log4j2
@Component
@ConditionalOnProperty(name = "constant.isDeleteInfo", havingValue = "true")
public class DeleteInfoTask {

    @Autowired
    private GroupService groupService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ConstantProperties cProperties;
    @Autowired
    private MonitorService monitorService;


//    @Scheduled(cron = "${constant.deleteInfoCron}", initialDelay = 1000)
    public void taskStart() {
       deleteInfoStart();
    }

    /**
     * start.
     */
    public void deleteInfoStart() {
        Instant startTime = Instant.now();
        log.info("start deleteInfoStart. startTime:{}", startTime.toEpochMilli());
        //get group list
        List<TbGroup> groupList = groupService.getGroupList(DataStatus.NORMAL.getValue());
        if (CollectionUtils.isEmpty(groupList)) {
            log.info("DeleteInfoTask jump over .not found any group");
            return;
        }

        groupList.stream().forEach(g -> deleteByGroupId(g.getGroupId()));

        log.info("end deleteInfoStart useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * delete by groupId.
     */
    private void deleteByGroupId(int groupId) {
        //delete block
        deleteBlock(groupId);
        //delete transHash
        deleteTransHash(groupId);
        //delete transaction monitor info
        deleteTransMonitor(groupId);
    }


    /**
     * delete block.
     */
    private void deleteBlock(int groupId) {
        log.info("start deleteBlock. groupId:{}", groupId);
        try {
            Integer removeCount = blockService.remove(groupId, cProperties.getBlockRetainMax());
            log.info("end deleteBlock. groupId:{} removeCount:{}", groupId, removeCount);
        } catch (Exception ex) {
            log.info("fail deleteBlock. groupId:{}", groupId, ex);
        }
    }

    /**
     * delete transHash.
     */
    private void deleteTransHash(int groupId) {
        log.info("start deleteTransHash. groupId:{}", groupId);
        try {
//            TransListParam queryParam = new TransListParam(null, null);
//            Integer count = transHashService.queryCountOfTran(groupId, queryParam);
            Integer count = transactionService.queryCountOfTranByMinus(groupId);
            Integer removeCount = 0;
            if (count > cProperties.getTransRetainMax().intValue()) {
                Integer subTransNum = count - cProperties.getTransRetainMax().intValue();
                removeCount = transactionService.remove(groupId, subTransNum);
            }
            log.info("end deleteTransHash. groupId:{} removeCount:{}", groupId, removeCount);
        } catch (Exception ex) {
            log.info("fail deleteTransHash. groupId:{}", groupId, ex);
        }
    }


    /**
     * delete monitor info.
     */
    private void deleteTransMonitor(int groupId) {
        log.info("start deleteTransMonitor. groupId:{}", groupId);
        try {
            Integer removeCount = monitorService
                .delete(groupId, cProperties.getMonitorInfoRetainMax());
            log.info("end deleteTransMonitor. groupId:{} removeCount:{}", groupId, removeCount);
        } catch (Exception ex) {
            log.info("fail deleteTransMonitor. groupId:{}", groupId, ex);
        }
    }
}
