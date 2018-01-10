/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.happylifeplat.transaction.core.service.impl;

import com.happylifeplat.transaction.common.enums.SerializeProtocolEnum;
import com.happylifeplat.transaction.common.enums.CompensationCacheTypeEnum;
import com.happylifeplat.transaction.common.exception.TransactionRuntimeException;
import com.happylifeplat.transaction.common.holder.LogUtil;
import com.happylifeplat.transaction.common.serializer.ObjectSerializer;
import com.happylifeplat.transaction.core.compensation.TxCompensationService;
import com.happylifeplat.transaction.core.helper.SpringBeanUtils;
import com.happylifeplat.transaction.common.config.TxConfig;
import com.happylifeplat.transaction.core.netty.NettyClientService;
import com.happylifeplat.transaction.core.service.InitService;
import com.happylifeplat.transaction.common.holder.ServiceBootstrap;
import com.happylifeplat.transaction.core.spi.TransactionRecoverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * @author xiaoyu
 */
@Component
public class InitServiceImpl implements InitService {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitServiceImpl.class);


    private final NettyClientService nettyClientService;

    private final TxCompensationService txCompensationService;

    @Autowired
    public InitServiceImpl(NettyClientService nettyClientService, TxCompensationService txCompensationService) {
        this.nettyClientService = nettyClientService;
        this.txCompensationService = txCompensationService;
    }

    @Override
    public void initialization(TxConfig txConfig) {
        try {
            loadSpi(txConfig);
            nettyClientService.start(txConfig);
            txCompensationService.start(txConfig);
        } catch (Exception e) {
            throw new TransactionRuntimeException("补偿配置异常：" + e.getMessage());
        }
        LogUtil.info(LOGGER, () -> "分布式补偿事务初始化成功！");

    }

    /**
     * 根据配置文件初始化spi
     *
     * @param txConfig 配置信息
     */
    private void loadSpi(TxConfig txConfig) {

        //spi  serialize
        final SerializeProtocolEnum serializeProtocolEnum =
                SerializeProtocolEnum.acquireSerializeProtocol(txConfig.getSerializer());
        final ServiceLoader<ObjectSerializer> objectSerializers = ServiceBootstrap.loadAll(ObjectSerializer.class);

        final Optional<ObjectSerializer> serializer = StreamSupport.stream(objectSerializers.spliterator(), false)
                .filter(objectSerializer ->
                        Objects.equals(objectSerializer.getScheme(), serializeProtocolEnum.getSerializeProtocol())).findFirst();


        //spi  RecoverRepository support
        final CompensationCacheTypeEnum compensationCacheTypeEnum = CompensationCacheTypeEnum.acquireCompensationCacheType(txConfig.getCompensationCacheType());
        final ServiceLoader<TransactionRecoverRepository> recoverRepositories = ServiceBootstrap.loadAll(TransactionRecoverRepository.class);


        final Optional<TransactionRecoverRepository> repositoryOptional =
                StreamSupport.stream(recoverRepositories.spliterator(), false)
                        .filter(recoverRepository ->
                                Objects.equals(recoverRepository.getScheme(), compensationCacheTypeEnum.getCompensationCacheType()))
                        .findFirst();
        //将compensationCache实现注入到spring容器
        repositoryOptional.ifPresent(repository -> {
            serializer.ifPresent(repository::setSerializer);
            SpringBeanUtils.getInstance().registerBean(TransactionRecoverRepository.class.getName(), repository);
        });


    }


}
