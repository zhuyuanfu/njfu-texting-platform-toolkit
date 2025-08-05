package cn.edu.njfu.zyf.toolkit.service;

import java.util.List;

import cn.edu.njfu.zyf.toolkit.model.TeachingStaff;

public interface TeachingStaffBirthdayService {
	List<TeachingStaff> listTodaysTeachingStaff();
	String sendTextMessageToBirthdayTeachingStaff();
}
