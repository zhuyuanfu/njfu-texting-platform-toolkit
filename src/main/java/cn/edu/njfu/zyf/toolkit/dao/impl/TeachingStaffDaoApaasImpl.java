package cn.edu.njfu.zyf.toolkit.dao.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.njfu.zyf.toolkit.dao.TeachingStaffDao;
import cn.edu.njfu.zyf.toolkit.model.TeachingStaff;
import cn.edu.njfu.zyf.toolkit.utils.CommonUtil;
import cn.edu.njfu.zyf.toolkit.utils.HttpUtil;

@Repository
public class TeachingStaffDaoApaasImpl implements TeachingStaffDao{

	private static Map<String, String> fieldCodeToFieldNameMap = new HashMap<>();
	
	static {
		fieldCodeToFieldNameMap.put("input_1682304045325", "unchangeableCode");
		fieldCodeToFieldNameMap.put("input_1682304051031", "name");
		fieldCodeToFieldNameMap.put("input_1682315112334", "identityNumber");
		fieldCodeToFieldNameMap.put("input_1682315140569", "mobilePhoneNumber");
		fieldCodeToFieldNameMap.put("date_1682304091304", "birthday");
		
	}
	
	@Override
	public List<TeachingStaff> listAllTeachingStaffs() {
		String url = "https://apaas.njfu.edu.cn/api/public/query/9463e34a8b874c6397c55d48ff4c90be/data";
		Map<String, String> requestHeader = new HashMap<>();
		requestHeader.put("sharekey", "9463e34a8b874c6397c55d48ff4c90be");
		requestHeader.put("content-type", "application/json;charset=UTF-8");
		String requestBody = "{\"query\":\"{\\\"query\\\":{\\\"bool\\\":{\\\"must\\\":[{\\\"range\\\":{\\\"createTime\\\":{\\\"gte\\\":\\\"1970-01-01 00:00:00\\\",\\\"lte\\\":\\\"2099-07-26 11:12:20\\\",\\\"format\\\":\\\"yyyy-MM-dd HH:mm:ss\\\",\\\"time_zone\\\":\\\"Asia/Shanghai\\\"}}}],\\\"must_not\\\":[]}},\\\"_source\\\":[\\\"input_1682304045325\\\",\\\"input_1682304051031\\\",\\\"date_1682304091304\\\",\\\"input_1682315112334\\\",\\\"input_1682315140569\\\"],\\\"from\\\":0,\\\"size\\\":50000,\\\"sort\\\":[{\\\"createTime\\\":{\\\"order\\\":\\\"desc\\\"}}]}\",\"password\":null,\"appId\":\"65\",\"formId\":\"447\",\"formCode\":\"8463e34a8b874c6397c55d48ff4c90be\",\"filterFields\":[\"createTime\"],\"queryFields\":[\"input_1682304045325\",\"input_1682304051031\",\"date_1682304091304\",\"input_1682315112334\",\"input_1682315140569\"]}";
		List<TeachingStaff> result = new ArrayList<>();
		try {
			String responseText = HttpUtil.post(url, requestHeader, requestBody);
			String parsedResponseText = convertFieldCode2FieldName(responseText);
			ObjectMapper om = new ObjectMapper();
	    	Map<String, Object> responseMap = om.readValue(parsedResponseText, new TypeReference<Map<String, Object>>(){});
	    	Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
	    	List<Map<String, String>> datas = (List<Map<String, String>>) dataMap.get("datas");
	    	
	    	for(Map<String, String> datum: datas) {
	    		TeachingStaff ts = new TeachingStaff();
	    		ts.setUnchangeableCode(datum.get("unchangeableCode"));
	    		ts.setName(datum.get("name"));
	    		ts.setIdentityNumber(datum.get("identityNumber"));
	    		ts.setMobilePhoneNumber(datum.get("mobilePhoneNumber"));
	    		String birthdayText = datum.get("birthday");
	    		if (!CommonUtil.isStringEmpty(birthdayText)) {
	    			birthdayText = birthdayText.split("T")[0];
	    			String[] ymdArray = birthdayText.split("-");
	    			LocalDate birthDate = LocalDate.of(
	    					Integer.valueOf(ymdArray[0]), 
	    					Integer.valueOf(ymdArray[1]),
	    					Integer.valueOf(ymdArray[2]));
	    			ts.setBirthday(birthDate);
	    		}
	    		result.add(ts);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String convertFieldCode2FieldName(String inputJson) {
		String result = inputJson;
		Set<Entry<String, String>> fieldCode2FieldNameEntrySet = fieldCodeToFieldNameMap.entrySet();
		for(Entry<String, String> entry: fieldCode2FieldNameEntrySet) {
			String fieldCode = entry.getKey();
			String fieldName = entry.getValue();
			result = result.replace(fieldCode, fieldName);
		}
		return result;
	} 
}
