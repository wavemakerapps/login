/*Copyright (c) 2016-2017 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.completeprofileproject.sales.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;

import com.completeprofileproject.sales.Sale;


/**
 * ServiceImpl object for domain model class Sale.
 *
 * @see Sale
 */
@Service("sales.SaleService")
@Validated
public class SaleServiceImpl implements SaleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleServiceImpl.class);


    @Autowired
    @Qualifier("sales.SaleDao")
    private WMGenericDao<Sale, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<Sale, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "salesTransactionManager")
    @Override
    public Sale create(Sale sale) {
        LOGGER.debug("Creating a new Sale with information: {}", sale);

        Sale saleCreated = this.wmGenericDao.create(sale);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(saleCreated);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public Sale getById(Integer saleId) {
        LOGGER.debug("Finding Sale by id: {}", saleId);
        return this.wmGenericDao.findById(saleId);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public Sale findById(Integer saleId) {
        LOGGER.debug("Finding Sale by id: {}", saleId);
        try {
            return this.wmGenericDao.findById(saleId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No Sale found with id: {}", saleId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public List<Sale> findByMultipleIds(List<Integer> saleIds, boolean orderedReturn) {
        LOGGER.debug("Finding Sales by ids: {}", saleIds);

        return this.wmGenericDao.findByMultipleIds(saleIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "salesTransactionManager")
    @Override
    public Sale update(Sale sale) {
        LOGGER.debug("Updating Sale with information: {}", sale);

        this.wmGenericDao.update(sale);
        this.wmGenericDao.refresh(sale);

        return sale;
    }

    @Transactional(value = "salesTransactionManager")
    @Override
    public Sale delete(Integer saleId) {
        LOGGER.debug("Deleting Sale with id: {}", saleId);
        Sale deleted = this.wmGenericDao.findById(saleId);
        if (deleted == null) {
            LOGGER.debug("No Sale found with id: {}", saleId);
            throw new EntityNotFoundException(String.valueOf(saleId));
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "salesTransactionManager")
    @Override
    public void delete(Sale sale) {
        LOGGER.debug("Deleting Sale with {}", sale);
        this.wmGenericDao.delete(sale);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public Page<Sale> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Sales");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public Page<Sale> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Sales");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service sales for table Sale to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service sales for table Sale to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "salesTransactionManager")
    @Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }



}