package cn.edu.njfu.zyf.toolkit.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.njfu.zyf.toolkit.config.MasPlatformConnectionAliveCheckingConfig;
import cn.edu.njfu.zyf.toolkit.model.TextMessageDelivery;
import cn.edu.njfu.zyf.toolkit.service.TextMessageSendingService;
import cn.edu.njfu.zyf.toolkit.utils.HttpUtil;

@Service
public class TextMessageSendingServiceImpl implements TextMessageSendingService {

    private static Logger logger = LoggerFactory.getLogger(TextMessageSendingServiceImpl.class);

    @Autowired
    private MasPlatformConnectionAliveCheckingConfig masConfig;
    
    @Override
    public String resolveFileAndSendHttpRequest(String jSessionId, MultipartFile excelFile) {
        if(jSessionId == null || jSessionId.equals("")) {
            jSessionId = masConfig.getJSESSIONID();
        }
        
        List<TextMessageDelivery> deliveryList = new ArrayList<>();
        InputStream is = null;
        XSSFWorkbook workbook = null;
        try {
            is = excelFile.getInputStream();
            workbook = new XSSFWorkbook(is);
            
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            logger.info("last row num = {}", lastRowNum);
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                String name = row.getCell(0).getStringCellValue();
                String mobile = null;
                if (row.getCell(1).getCellType() == CellType.STRING) {
                    mobile = row.getCell(1).getStringCellValue();
                } else if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                    mobile = String.valueOf((long) row.getCell(1).getNumericCellValue());
                }
                
                LocalDateTime sendDate = row.getCell(2).getLocalDateTimeCellValue();
                LocalDateTime sendDateTime = LocalDateTime.of(sendDate.toLocalDate(), LocalTime.of(16, 0, 0));
                String content = row.getCell(3).getStringCellValue();
                if(name != null && (!name.equals(""))) {
                    TextMessageDelivery tmd = new TextMessageDelivery(name, mobile, sendDateTime, content, 20);
                    logger.info("{}", tmd);
                    deliveryList.add(tmd);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        logger.debug("一共有" + deliveryList.size() + "人即将被配置定时短信");
        
        StringBuilder resultBuilder = new StringBuilder();
        int successCount = 0;
        for(TextMessageDelivery delivery: deliveryList) {
            String result = sendMessage(
                    jSessionId,
                    delivery.getSendMode(),
                    delivery.getMobile(),
                    delivery.getName(),
                    delivery.getMessage(),
                    delivery.getStringSendDateTime()
                    );
            if (result.startsWith("Succe")) {
                successCount++;
            }
            resultBuilder.append(result).append("\n");
        }
        return "Success count = " + successCount + ", details are as follows: \n" +resultBuilder.toString();
    }


    private static String sendMessage(String jSessionId, int sendMode, String destAddrHand, String subject, String content, String sendTime) {
        String url = "http://121.248.150.95:6789/sms/sendSms.do?act=insertSendSM";
        Map<String, String> header = new HashMap<>();
        Map<String, String> formData = new HashMap<>();
        
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Cookie", "JSESSIONID=" + jSessionId);
        
        formData.put("sendMode", String.valueOf(sendMode));
        formData.put("destAddrHand", destAddrHand);
        formData.put("subject", subject);
        formData.put("reqDeliveryReport", "1");
        formData.put("sendMethod", "2");
        formData.put("content", content);
        formData.put("sendTime", sendTime);
        String responseText = null;
        try {
            responseText = HttpUtil.post(url, header, formData);
        } catch (IOException e) {
            logger.error("Error: {}", e);
        }
        String result = null;
        if (responseText != null && responseText.contains("已成功提交")) {
            result = "Succeeded setting delivery to " + subject + destAddrHand + ", sendTime: " + sendTime + ", content: " + content;
            logger.info(result);
        } else {
            result = "Failed setting delivery to " + subject + destAddrHand + ", sendTime: " + sendTime + ", content: " + content;
            logger.error(result);
        }
        return result;
    }
}
