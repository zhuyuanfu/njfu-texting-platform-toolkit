package cn.edu.njfu.zyf.toolkit.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.njfu.zyf.toolkit.config.MasPlatformConnectionAliveCheckingConfig;
import cn.edu.njfu.zyf.toolkit.dao.MasPlatformScheduledMessageBoxDao;
import cn.edu.njfu.zyf.toolkit.utils.HttpUtil;

@Repository
public class MasPlatformScheduledMessageBoxDaoImpl implements MasPlatformScheduledMessageBoxDao {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MasPlatformConnectionAliveCheckingConfig masConfig;
	
	@Override
	public List<String> listScheduledMessageIds(int count) {
		List<String> messageIdList = new ArrayList<>();
		try {
			String listUrl = "http://121.248.150.95:6789/sms/listWaitSmsMt.do?act=waitListMt";
			Map<String, String> pageSizeAndOffset = new HashMap<String, String>();
			pageSizeAndOffset.put("pageSize", "" + count);
			pageSizeAndOffset.put("offset", "0");
			Map<String, String> listHeader = new HashMap<>();
			listHeader.put("Content-Type", "application/x-www-form-urlencoded");
	        listHeader.put("Cookie", "JSESSIONID=" + masConfig.getJSESSIONID());
			
			String messageBoxPage = HttpUtil.requestWithMapFormData("GET", listUrl, listHeader, pageSizeAndOffset);
			//logger.info("{}", messageBoxPage);
			String checkBoxCatcherReg = "<input type=\"checkbox\" name=\"id\" value=\"(\\d+)\" />";
			Pattern checkBoxPattern = Pattern.compile(checkBoxCatcherReg);
			Matcher checkBoxMatcher = checkBoxPattern.matcher(messageBoxPage);
			
			while(checkBoxMatcher.find()) {
				messageIdList.add(checkBoxMatcher.group(1));
			}
			logger.info("amount of messages: {}, their ids are: {}", messageIdList.size(), messageIdList);
		} catch (IOException e) {
			logger.error("{}", e);
		} finally {
			
		}
		return messageIdList;
	}

	@Override
	public boolean deleteMessagesById(List<String> messageIdList) {
		try {			
			String deleteUrl = "http://121.248.150.95:6789/sms/listSmsMt.do?act=deleteWaitMt";
			Map<String, String> deleteHeader = new HashMap<>();
			deleteHeader.put("Content-Type", "application/x-www-form-urlencoded");
			deleteHeader.put("Cookie", "JSESSIONID=" + masConfig.getJSESSIONID());
			
			StringBuilder deleteFormDataBuilder = new StringBuilder("id=&display=&fromdate=&todate=&content=&subject=");
			for(int i = 0; i < messageIdList.size(); i++) {
				deleteFormDataBuilder.append("&").append("id=").append(messageIdList.get(i));
			}
			String deleteFormData = deleteFormDataBuilder.toString();
			String deleteResponse = HttpUtil.requestWithStringRequestBody("POST", deleteUrl, deleteHeader, deleteFormData);
			logger.info("{}", deleteResponse);
			boolean deleteSuccess = deleteResponse.contains("WEB短信→定时短信箱");
			return deleteSuccess;
		} catch (IOException e) {
			logger.error("{}", e);
		} 
		return false;
	}
	
}
