package cn.edu.njfu.zyf.toolkit.dao;

import java.util.List;

public interface MasPlatformScheduledMessageBoxDao {
	public List<String> listScheduledMessageIds(int count);
	public boolean deleteMessagesById(List<String> idList);
}
