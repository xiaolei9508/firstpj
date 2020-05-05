package com.cmft.slas.cmuop.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.service.ExcelService;
import com.cmft.slas.cmuop.vo.ArticlePreview;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public InputStream writeExcel(List<? extends ArticlePreview> preview, Date dateFrom, Date dateTo,
        Map<String, String> entityMap) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFCell cell;
        InputStream is = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        HSSFRow title = sheet.createRow(0);
        cell = title.createCell(0);
        String dateString = "";
        if (dateFrom != null) {
            dateString = df.format(dateFrom);
        } 
        if (dateTo != null) {
            dateString += df.format(dateTo);
        }
        cell.setCellValue(dateString + "招闻天下栏目资讯");
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 8);
        sheet.addMergedRegion(region);
        title = sheet.createRow(1);
        cell = title.createCell(0);
        cell.setCellValue("序号");
        cell = title.createCell(1);
        cell.setCellValue("标题");
        cell = title.createCell(2);
        cell.setCellValue("来源");
        cell = title.createCell(3);
        cell.setCellValue("发布时间");
        cell = title.createCell(4);
        cell.setCellValue("推送时间");
        cell = title.createCell(5);
        cell.setCellValue("浏览量");
        cell = title.createCell(6);
        cell.setCellValue("舆情主体");
        cell = title.createCell(7);
        cell.setCellValue("情感");
        cell = title.createCell(8);
        cell.setCellValue("是否推送");
        int line = 2;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ArticlePreview article : preview) {
            HSSFRow row = sheet.createRow(line);
            cell = row.createCell(0);
            cell.setCellValue(line - 1);
            cell = row.createCell(1);
            cell.setCellValue(article.getTitle());
            cell = row.createCell(2);
            cell.setCellValue(article.getSource());
            cell = row.createCell(3);
            cell.setCellValue(df.format(article.getPubTime()));
            cell = row.createCell(4);
            cell.setCellValue(df.format(article.getUpdateTime()));
            cell = row.createCell(5);
            if (article.getViewCount() == null) {
                cell.setCellValue(0);
            } else {
                cell.setCellValue(article.getViewCount());
            }
            cell = row.createCell(6);
            String message = "";
            if (article.getEntities() != null) {
                for (String entity : article.getEntities()) {
                    if (message.equals("")) {
                        message = entity;
                    } else {
                        message += "\n" + entity;
                    }
                }
            }
            cell = row.createCell(7);
            if (article.getSentiment()>0) {
                cell.setCellValue("正面");
            } else if (article.getSentiment()<0) {
                cell.setCellValue("负面");
            } else {
                cell.setCellValue("中性");
            }
            cell = row.createCell(8);
            if (article.getIfShow()) {
                cell.setCellValue("显示");
            } else {
                cell.setCellValue("不显示");
            }

            line++;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();
            is = new ByteArrayInputStream(barray);
        } catch (IOException e) {
            log.error("输出流失败： " + ExceptionUtils.getStackTrace(e));
        }
        return is;
    }

}
