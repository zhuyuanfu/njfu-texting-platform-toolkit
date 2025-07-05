package cn.edu.njfu.zyf.toolkit.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.njfu.zyf.toolkit.dao.TeachingStaffDao;
import cn.edu.njfu.zyf.toolkit.model.TeachingStaff;
import cn.edu.njfu.zyf.toolkit.model.TextMessageDelivery;
import cn.edu.njfu.zyf.toolkit.service.TeachingStaffService;
import cn.edu.njfu.zyf.toolkit.service.TextMessageSendingService;

@Service
public class TeachingStaffServiceImpl implements TeachingStaffService {

	@Autowired
	private TeachingStaffDao dao;
	
	@Autowired
	private TextMessageSendingService textMessageSendingService;
	
	@Override
	public List<TeachingStaff> listTodaysTeachingStaff() {
		List<TeachingStaff> result = new ArrayList<>();
		List<TeachingStaff> allTeachingStaffs = dao.listAllTeachingStaffs();
		LocalDate today = LocalDate.now();
		int todayMonth = today.getMonthValue();
		int todayDateOfMonth = today.getDayOfMonth();
		for(TeachingStaff ts: allTeachingStaffs) {
			LocalDate birthday = ts.getBirthday();
			if (birthday != null) {
				int birthdayMonth = birthday.getMonthValue();
				int birthdayDateOfMonth = birthday.getDayOfMonth();
				if(todayMonth == birthdayMonth && todayDateOfMonth == birthdayDateOfMonth && !result.contains(ts)) {
					result.add(ts);
				}
			}
		}
		return result;
	}

	@Override
	public String sendTextMessageToBirthdayTeachingStaff() {
		List<TeachingStaff> todayStaffs = listTodaysTeachingStaff();
		int successCount = 0;
		StringBuilder sb = new StringBuilder();
		for(TeachingStaff ts: todayStaffs) {
			TextMessageDelivery delivery = new TextMessageDelivery();
			delivery.setSendMode(0);
			delivery.setName(ts.getName());
			delivery.setMobile(ts.getMobilePhoneNumber());
			delivery.setMessage("祝你生日快乐~");
			delivery.setSendDateTime(LocalDateTime.now());
			boolean success = textMessageSendingService.sendTextMessage(delivery);
			if (success) {
				sb.append("Birthday message success: " + delivery.toString() + "\n");
				successCount++;
			} else {
				sb.append("Birthday message failed: " + delivery.toString() + "\n");
			}
		}
		return "total: " + todayStaffs.size() + ", success: " + successCount;
	}

}
