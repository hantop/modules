package com.fenlibao.service.pms.da.reward.cashRedPacket.impl;

import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.common.pms.util.loader.Message;
import com.fenlibao.dao.pms.da.finance.replacementRecharge.ReplacementRechargeMapper;
import com.fenlibao.dao.pms.da.reward.cashRedPacket.UserRedpacketsMapper;
import com.fenlibao.dao.pms.da.reward.common.RewardMapper;
import com.fenlibao.model.pms.common.global.SystemType;
import com.fenlibao.model.pms.da.finance.ReplacementRecharge;
import com.fenlibao.model.pms.da.global.FeeCode;
import com.fenlibao.model.pms.da.reward.*;
import com.fenlibao.model.pms.da.reward.form.UserRedpacketsForm;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.service.pms.da.exception.ExcelException;
import com.fenlibao.service.pms.da.exception.GrantCashRedPacketException;
import com.fenlibao.service.pms.da.finance.replacementRecharge.ReplacementRechargeService;
import com.fenlibao.service.pms.da.reward.cashRedPacket.RedPacketService;
import com.fenlibao.service.pms.da.reward.cashRedPacket.UserRedpacketsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserRedpacketsServiceImpl implements UserRedpacketsService {
	private static final Logger LOG = LoggerFactory.getLogger(UserRedpacketsServiceImpl.class);

	@Autowired
	private UserRedpacketsMapper userRedpacketsMapper;

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private RewardMapper rewardMapper;

	@Autowired
	private XWRechargeService xwRechargeService;

	@Autowired
	private ReplacementRechargeMapper replacementRechargeMapper;

	@Autowired
	private ReplacementRechargeService replacementRechargeService;

	@Override
	public List<UserRedpackets> findPager(UserRedpackets userRedpackets, RowBounds bounds) {
		if (userRedpackets.getActivityCode() != null && "".equals(userRedpackets.getActivityCode().trim())) {
			userRedpackets.setActivityCode(null);
		}
		if (userRedpackets.getGrantStatus() != null) {
			if (userRedpackets.getGrantStatus() < 0) {
				userRedpackets.setGrantStatus(null);
			}
		}
		return userRedpacketsMapper.findPager(userRedpackets, bounds);
	}

	@Override
	public List<UserRedpackets> findAll(UserRedpackets userRedpackets) {
		if (userRedpackets.getActivityCode() != null && "".equals(userRedpackets.getActivityCode().trim())) {
			userRedpackets.setActivityCode(null);
		}
		if (userRedpackets.getGrantStatus() != null) {
			if (userRedpackets.getGrantStatus() < 0) {
				userRedpackets.setGrantStatus(null);
			}
		}
		return userRedpacketsMapper.findAll(userRedpackets);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return userRedpacketsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserRedpackets record) {
		return userRedpacketsMapper.insert(record);
	}

	@Override
	public int insertSelective(UserRedpackets record) {
		return userRedpacketsMapper.insertSelective(record);
	}

	@Override
	public UserRedpackets selectByPrimaryKey(Integer id) {
		return userRedpacketsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserRedpackets record) {
		return userRedpacketsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserRedpackets record) {
		return userRedpacketsMapper.updateByPrimaryKey(record);
	}

	private int min = 0;
	private int max = 3000;

	@PostConstruct
	public void init() {
		Config.loadProperties();
		min = Integer.parseInt(Config.get("reward.redmoney.min"));
		max = Integer.parseInt(Config.get("reward.redmoney.max"));
	}

	/**
	 * 获取指定类型的所有历史纪录
	 *
	 * @param userRedpackets
	 * @return
	 */
	@Override
	public List<UserRedpackets> findAllActivityCode(UserRedpackets userRedpackets) {
		return userRedpacketsMapper.findAllActivityCode(userRedpackets);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<String> insertSelective(RewardRecord newRecord, List<String[]> list) throws ExcelException {
		List<String> result = new ArrayList<>();
		Set<UserRedpackets> insertList = new TreeSet<>();
//		Set<String> insertListTemp = new TreeSet<>();
		newRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
		if (rewardMapper.insertSelective(newRecord) > 0) {
			for (int i = 0; i < list.size(); i++) {
				String[] row = list.get(i);
				if (row == null) {
					continue;
				}
				Integer userId = rewardMapper.getUserIdbyPhone(row[0]);
				if (userId == null) {
					result.add("行数" + (i + 2) + ",手机号：" + row[0] + "不存在");
					continue;
				}
				RedPacket redPacket = redPacketService.findRedPacketByCode(row[1], FeeCode.REGISTERRETURNCACH_REDPACKET);
				if (redPacket == null) {
					result.add("行数" + (i + 2) + ",返现券：" + row[1] + "不存在");
					continue;
				}
				UserRedpackets userRedpackets = new UserRedpackets();
				userRedpackets.setUserId(userId);// 用户id
				userRedpackets.setRedpacketId(redPacket.getId());// 红包或返现券id
				userRedpackets.setGrantId(newRecord.getId());// 发放记录id
				userRedpackets.setGrantStatus((byte) 0);// 发放状态（0：未发放，1：已发放，2：发放失败）
				userRedpackets.setStatus((byte) 1);
				insertList.add(userRedpackets);
				//取消重复2016 06 28允许同一个人发同一种返现券   update lee
				/*if (insertList.add(userRedpackets)) {
					if (!insertListTemp.add(userId.toString() + redPacket.getId())) {
						result.add("行数" + (i + 2) + ",手机号：" + row[0] + ",返现券：" + row[1] + "，信息重复");
						continue;
					}
					continue;
				}*/
			}
			if (insertList.size() == 0) {
				result.add("没有可用的数据导入");
				throw new ExcelException("导入的excel有误", result);
			}
			if (result.isEmpty()) {
				if (insertList.size() > 50000) {
					result.add("一次不能导入超过50000条记录");
					throw new ExcelException("导入的excel有误", result);
				}
				userRedpacketsMapper.batchInsert(insertList);
				return result;
			}
		}
		throw new ExcelException("导入的excel有误", result);
	}

	@Override
	public Byte selectGrantedById(Integer id) {
		return rewardMapper.selectGrantedById(id);
	}

	@Override
	public void updateRewardRecord(RewardRecord rewardRecord) {
		rewardMapper.updateRewardRecord(rewardRecord);
	}

	@Override
	public RewardRecord getRewardRecordById(RewardRecord rewardRecord) {
		return rewardMapper.getRewardRecordById(rewardRecord);
	}

	@Override
	public List<UserRedpackets> getRedPacketActivateCode(UserRedpackets item) {
		return userRedpacketsMapper.getRedPacketActivateCode(item);
	}

	@Override
	public Integer getInServiceRedpackets() {
		return userRedpacketsMapper.getInServiceRedpackets();
	}

	@Override
	public List<UserRedpackets> findAllReport(UserRedpacketsForm userRedpackets) {
		if (userRedpackets.getActivityCode() != null && "".equals(userRedpackets.getActivityCode().trim())) {
			userRedpackets.setActivityCode(null);
		}
		if (userRedpackets.getGrantStatus() != null) {
			if (userRedpackets.getGrantStatus() < 0) {
				userRedpackets.setGrantStatus(null);
			}
		}
		return userRedpacketsMapper.findAllReport(userRedpackets);
	}

	@Override
	public List<UserRedpackets> findUserRedpacketsAll(RewardRecord rewardRecord, UserRedpackets userRedpackets) {
		userRedpackets.setGrantId(rewardRecord.getId());
		List<UserRedpackets> userRedpacketsList = userRedpacketsMapper.findAll(userRedpackets);
		return userRedpacketsList;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String grantBackVoucher(RewardRecord rewardRecord, UserRedpackets userRedpackets) throws Exception {
		Byte granted = rewardMapper.selectGrantedById(rewardRecord.getId());
		if (granted != null && granted == 0) {
			Calendar startTime = Calendar.getInstance();
			startTime.set(Calendar.HOUR_OF_DAY, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);
			startTime.set(Calendar.MILLISECOND, 0);
			long nowMiniSecond = startTime.getTimeInMillis();
			rewardRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
			rewardRecord.setGranted((byte) 1);

			userRedpackets.setGrantId(rewardRecord.getId());
			List<UserRedpackets> userRedpacketsList = userRedpacketsMapper.findAll(userRedpackets);
			List<RedPacket> userRedPacketTypeList = new ArrayList<RedPacket>();

			List<T6123> t6123sToInsert = new ArrayList<>();
			List<T1040> t1040sToInsert = new ArrayList<>();
			Message.loadProperties();
			BigDecimal grantSum = new BigDecimal(0);
			for (UserRedpackets item : userRedpacketsList) {
				RedPacket redPacket = redPacketService.findRedPacketByCode(item.getActivityCode(), 1);
				grantSum = grantSum.add(redPacket.getRedMoney());
				userRedPacketTypeList.add(redPacket);
				long endMiniSecond = nowMiniSecond + ((long) redPacket.getEffectDay() * 24 * 3600 * 1000) - 1000;
				item.setValidTime(new Timestamp(endMiniSecond));
				item.setGrantStatus((byte) 1);
				// 发站内信
				T6123 t6123Letter = new T6123();
				t6123Letter.setF02(item.getUserId());
				t6123Letter.setF03("返现红包发放成功");
				t6123Letter.setF04(new Timestamp(System.currentTimeMillis()));
				t6123Letter.setF05("WD");
				rewardMapper.insertT6123(t6123Letter);
				t6123sToInsert.add(t6123Letter);
				// 发短信
				T1040 t1040Message = new T1040();
				t1040Message.setF02(0);
				StringBuilder shortMessage = new StringBuilder(Message.get("short.system.cashBackVoucher"));
				shortMessage.replace(shortMessage.indexOf("{money}"),
						shortMessage.indexOf("{money}") + "{money}".length(), "" + redPacket.getRedMoney());
				shortMessage.replace(shortMessage.indexOf("{validtime}"),
						shortMessage.indexOf("{validtime}") + "{validtime}".length(),
						(new SimpleDateFormat("yyyy-MM-dd")).format(item.getValidTime()));
				t1040Message.setF03(shortMessage.toString());
				t1040Message.setF04(new Timestamp(System.currentTimeMillis()));
				t1040Message.setF05("W");
				rewardMapper.insertT1040(t1040Message);
				t1040sToInsert.add(t1040Message);
			}
			if (!userRedpacketsList.isEmpty()) {
				userRedpacketsMapper.batchUpdateUserRedpacket(userRedpacketsList);
			}

			rewardRecord.setGrantCount(userRedpacketsList.size());
			rewardRecord.setGrantSum(grantSum);
			rewardMapper.updateRewardRecord(rewardRecord);

			// 批量插入T6124和T1041
			List<T6124> t6124sToInsert = new ArrayList<>();
			List<T1041> t1041sToInsert = new ArrayList<>();
			for (int i = 0; i < userRedpacketsList.size(); i++) {
				T6124 t6124Letter = new T6124();
				t6124Letter.setF01(t6123sToInsert.get(i).getF01());
				StringBuilder internalMessage = new StringBuilder(Message.get("internal.system.cashBackVoucher"));
				internalMessage.replace(internalMessage.indexOf("{money}"),
						internalMessage.indexOf("{money}") + "{money}".length(),
						"" + userRedPacketTypeList.get(i).getRedMoney());
				internalMessage.replace(internalMessage.indexOf("{validtime}"),
						internalMessage.indexOf("{validtime}") + "{validtime}".length(),
						(new SimpleDateFormat("yyyy-MM-dd")).format(userRedpacketsList.get(i).getValidTime()));
				t6124Letter.setF02(internalMessage.toString());
				t6124sToInsert.add(t6124Letter);
				T1041 t1041Message = new T1041();
				t1041Message.setF01(t1040sToInsert.get(i).getF01());
				t1041Message.setF02(userRedpacketsList.get(i).getPhone());
				t1041sToInsert.add(t1041Message);
			}
			// 批量插入t6124
			rewardMapper.batchInsertT6124(t6124sToInsert);
			// 批量插入t1041
			rewardMapper.batchInsertT1041(t1041sToInsert);
			return "发放完成";
		} else {
			return "已发放过，不可重复发放";
		}
	}

	//返现卷发放
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RewardRecord grantBackVoucherResult(RewardRecord rewardRecord, List<UserRedpackets> userRedpacketsList) {
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		long nowMiniSecond = startTime.getTimeInMillis();

		// 批量插入T6124和T1041
		List<T6124> t6124sToInsert = new ArrayList<>();
		List<T1041> t1041sToInsert = new ArrayList<>();
		Message.loadProperties();
		BigDecimal grantSum = new BigDecimal(0);
		// 用户ID，用户拥有的返现券数量
		Map<String, Integer> userIdRedpacketCountMap = new HashMap<>();
		// 用户ID，用户拥有的返现券总金额
		Map<Integer, BigDecimal> userIdRedpacketSumMap = new HashMap<>();
		RedPacket redPacket;
		Integer userId;
		String symbol = ":";
		String tempKey;
		for (UserRedpackets item : userRedpacketsList) {
			userId = item.getUserId();
			tempKey = userId.toString().concat(symbol).concat(item.getPhone());
			// TODO: 2016/5/26 Jing 需要将导入返现券相同手机号码只要发送一条短信，查询所有红包编码
			BigDecimal redSum = new BigDecimal(0); // 相同手机号返现券的总金额
			redPacket = redPacketService.findRedPacketByCode(item.getActivityCode(), FeeCode.REGISTERRETURNCACH_REDPACKET);
			redSum = redSum.add(redPacket.getRedMoney());
			long endMiniSecond = nowMiniSecond + ((long) redPacket.getEffectDay() * 24 * 3600 * 1000) - 1000;
			item.setValidTime(new Timestamp(endMiniSecond));
			item.setGrantStatus((byte) 1);
			if (userIdRedpacketCountMap.containsKey(tempKey)) {
				userIdRedpacketCountMap.put(tempKey, userIdRedpacketCountMap.get(tempKey) + 1);//用户拥有返现券数量+1
			} else {
				userIdRedpacketCountMap.put(tempKey, 1);
			}
			if (userIdRedpacketSumMap.containsKey(userId)) {
				userIdRedpacketSumMap.put(userId, userIdRedpacketSumMap.get(userId).add(redSum));//用户拥有返现券总金额
			} else {
				userIdRedpacketSumMap.put(userId, redSum);
			}
			grantSum = grantSum.add(redPacket.getRedMoney());//发送总额
		}
		//发放名单
		String[] grantNameArr = rewardRecord.getGrantName().split("_");
		Set<Map.Entry<String, Integer>> entries = userIdRedpacketCountMap.entrySet();
		String key;
		Integer value;
		String[] arr;
		Integer uId;
		String uPhone;
		for (Map.Entry<String, Integer> entry : entries) {
			key = entry.getKey();
			arr = key.split(symbol);
			uId = Integer.valueOf(arr[0]);
			uPhone = arr[1];
			value = entry.getValue();
			BigDecimal redSum = new BigDecimal(0);
			// 发站内信 标题
			T6123 t6123Letter = new T6123();
			t6123Letter.setF02(uId);
			t6123Letter.setF03("系统消息");
			t6123Letter.setF04(new Timestamp(System.currentTimeMillis()));
			t6123Letter.setF05("WD");
			rewardMapper.insertT6123(t6123Letter);

			// 发站内信 内容
			T6124 t6124Letter = new T6124();
			t6124Letter.setF01(t6123Letter.getF01());
			StringBuilder internalMessage = new StringBuilder(Message.get("internal.system.cashBackVoucher"));
			if (grantNameArr.length > 0) {
				internalMessage.replace(internalMessage.indexOf("{title}"),
						internalMessage.indexOf("{title}") + "{title}".length(),
						"" + grantNameArr[grantNameArr.length - 1]);
			} else {
				internalMessage.replace(internalMessage.indexOf("{title}"),
						internalMessage.indexOf("{title}") + "{title}".length(), "" + "");
			}
			internalMessage.replace(internalMessage.indexOf("{num}"),
					internalMessage.indexOf("{num}") + "{num}".length(), "" + value);
			redSum = userIdRedpacketSumMap.get(uId);
			internalMessage.replace(internalMessage.indexOf("{money}"),
					internalMessage.indexOf("{money}") + "{money}".length(), "" + redSum);
			t6124Letter.setF02(internalMessage.toString());
			t6124sToInsert.add(t6124Letter);

			// 发短信 内容
			T1040 t1040Message = new T1040();
			t1040Message.setF02(0);
			StringBuilder shortMessage = new StringBuilder(Message.get("short.system.cashBackVoucher"));
			if (grantNameArr.length > 0) {
				shortMessage.replace(shortMessage.indexOf("{title}"),
						shortMessage.indexOf("{title}") + "{title}".length(),
						"" + grantNameArr[grantNameArr.length - 1]);
			} else {
				shortMessage.replace(shortMessage.indexOf("{title}"),
						shortMessage.indexOf("{title}") + "{title}".length(), "" + "");
			}
			shortMessage.replace(shortMessage.indexOf("{num}"), shortMessage.indexOf("{num}") + "{num}".length(),
					"" + value);
			shortMessage.replace(shortMessage.indexOf("{money}"),
					shortMessage.indexOf("{money}") + "{money}".length(), "" + redSum);
			t1040Message.setF03(shortMessage.toString());
			t1040Message.setF04(new Timestamp(System.currentTimeMillis()));
			t1040Message.setF05("W");
			rewardMapper.insertT1040(t1040Message);
			// t1040sToInsert.add(t1040Message);

			// 发短信 标题
			T1041 t1041Message = new T1041();
			t1041Message.setF01(t1040Message.getF01());
			t1041Message.setF02(uPhone);
			t1041sToInsert.add(t1041Message);

		}

		//@see 如果所有一次性处理量大的话会导致连接超时
		/*if (!userRedpacketsAll.isEmpty()) {
			userRedpacketsMapper.batchUpdateUserRedpacket(userRedpacketsAll);
		}*/

		//每个用户拥有的返现卷单独处理
		userRedpacketsMapper.batchUpdateUserRedpacket(userRedpacketsList);

		// 批量插入t6124
		rewardMapper.batchInsertT6124(t6124sToInsert);
		// 批量插入t1041
		rewardMapper.batchInsertT1041(t1041sToInsert);

		rewardRecord.setGrantCount(userRedpacketsList.size());
		rewardRecord.setGrantSum(grantSum);
		rewardMapper.cumsumRewardRecord(rewardRecord);
		return rewardRecord;
	}

	//返现卷发放
	@Transactional(rollbackFor = Exception.class)
	public RewardRecord grantBackVoucherResult1(RewardRecord rewardRecord, List<UserRedpackets> userRedpacketsList) {
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		long nowMiniSecond = startTime.getTimeInMillis();

		// 批量插入T6124和T1041
		List<T6124> t6124sToInsert = new ArrayList<>();
		List<T1041> t1041sToInsert = new ArrayList<>();
		Message.loadProperties();
		BigDecimal grantSum = new BigDecimal(0);
		List<UserRedpackets> userRedpacketsAll = new ArrayList<>();
		for (UserRedpackets item : userRedpacketsList) {
			// TODO: 2016/5/26 Jing 需要将导入返现券相同手机号码只要发送一条短信，查询所有红包编码
			BigDecimal redSum = new BigDecimal(0); // 相同手机号返现券的总金额
			List<UserRedpackets> activateCodes = getRedPacketActivateCode(item);//每个用户拥有的返现券
			for (UserRedpackets activateCode : activateCodes) {
				RedPacket redPacket = redPacketService.findRedPacketByCode(activateCode.getActivityCode(), FeeCode.REGISTERRETURNCACH_REDPACKET);
				grantSum = grantSum.add(redPacket.getRedMoney());
				redSum = redSum.add(redPacket.getRedMoney());
				// userRedPacketTypeList.add(redPacket);
				long endMiniSecond = nowMiniSecond + ((long) redPacket.getEffectDay() * 24 * 3600 * 1000) - 1000;
				activateCode.setValidTime(new Timestamp(endMiniSecond));
				activateCode.setGrantStatus((byte) 1);
				userRedpacketsAll.add(activateCode);
			}

			if (activateCodes != null && activateCodes.size() > 0) {
				String[] grantNameArr = activateCodes.get(0).getGrantName().split("_");
				// 发站内信 标题
				T6123 t6123Letter = new T6123();
				t6123Letter.setF02(item.getUserId());
				t6123Letter.setF03("系统消息");
				t6123Letter.setF04(new Timestamp(System.currentTimeMillis()));
				t6123Letter.setF05("WD");
				rewardMapper.insertT6123(t6123Letter);

				// 发站内信 内容
				T6124 t6124Letter = new T6124();
				t6124Letter.setF01(t6123Letter.getF01());
				StringBuilder internalMessage = new StringBuilder(Message.get("internal.system.cashBackVoucher"));
				if (grantNameArr.length > 0) {
					internalMessage.replace(internalMessage.indexOf("{title}"),
							internalMessage.indexOf("{title}") + "{title}".length(),
							"" + grantNameArr[grantNameArr.length - 1]);
				} else {
					internalMessage.replace(internalMessage.indexOf("{title}"),
							internalMessage.indexOf("{title}") + "{title}".length(), "" + "");
				}
				internalMessage.replace(internalMessage.indexOf("{num}"),
						internalMessage.indexOf("{num}") + "{num}".length(), "" + activateCodes.size());
				internalMessage.replace(internalMessage.indexOf("{money}"),
						internalMessage.indexOf("{money}") + "{money}".length(), "" + redSum);
				t6124Letter.setF02(internalMessage.toString());
				t6124sToInsert.add(t6124Letter);

				// 发短信 内容
				T1040 t1040Message = new T1040();
				t1040Message.setF02(0);
				StringBuilder shortMessage = new StringBuilder(Message.get("short.system.cashBackVoucher"));
				if (grantNameArr.length > 0) {
					shortMessage.replace(shortMessage.indexOf("{title}"),
							shortMessage.indexOf("{title}") + "{title}".length(),
							"" + grantNameArr[grantNameArr.length - 1]);
				} else {
					shortMessage.replace(shortMessage.indexOf("{title}"),
							shortMessage.indexOf("{title}") + "{title}".length(), "" + "");
				}
				shortMessage.replace(shortMessage.indexOf("{num}"), shortMessage.indexOf("{num}") + "{num}".length(),
						"" + activateCodes.size());
				shortMessage.replace(shortMessage.indexOf("{money}"),
						shortMessage.indexOf("{money}") + "{money}".length(), "" + redSum);
				t1040Message.setF03(shortMessage.toString());
				t1040Message.setF04(new Timestamp(System.currentTimeMillis()));
				t1040Message.setF05("W");
				rewardMapper.insertT1040(t1040Message);
				// t1040sToInsert.add(t1040Message);

				// 发短信 标题
				T1041 t1041Message = new T1041();
				t1041Message.setF01(t1040Message.getF01());
				t1041Message.setF02(item.getPhone());
				t1041sToInsert.add(t1041Message);

				//每个用户拥有的返现卷单独处理
				if (!activateCodes.isEmpty()) {
					userRedpacketsMapper.batchUpdateUserRedpacket(activateCodes);
				}
			}
		}

		//@see 如果所有一次性处理量大的话会导致连接超时
		/*if (!userRedpacketsAll.isEmpty()) {
			userRedpacketsMapper.batchUpdateUserRedpacket(userRedpacketsAll);
		}*/

		// 批量插入t6124
		rewardMapper.batchInsertT6124(t6124sToInsert);
		// 批量插入t1041
		rewardMapper.batchInsertT1041(t1041sToInsert);

		rewardRecord.setGrantCount(userRedpacketsAll.size());
		rewardRecord.setGrantSum(grantSum);
		rewardMapper.cumsumRewardRecord(rewardRecord);
		return rewardRecord;
	}

	@Override
	public List<UserCashRedPacket> getCashRedPacketRecordsDetail(UserCashRedPacket userCashRedPacket,
			RowBounds bounds) {
		return userRedpacketsMapper.getCashRedPacketRecordsDetail(userCashRedPacket, bounds);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<String> importCashRedPacketRecords(RewardRecord record, List<String[]> detailList) {
		List<String> phoneColumn = new ArrayList<String>();
		List<String> contentErrorList = new ArrayList<String>();
		List<Integer> userIdList = new ArrayList<Integer>();
		List<String> moneyColumn = new ArrayList<String>();
		List<String> sysTypeColumn = new ArrayList<String>(); //系统类型
		// 系统类型必须保持一致
		int rewordRecordSysType = 0;
		List<String> normalType = new ArrayList<>();// 普通版
		List<String> custodyType = new ArrayList<>();// 存管版
		for (int i = 0; i < detailList.size(); i++) {
			//确认系统类型
			if (detailList.get(i) != null) {
				String row[] = detailList.get(i);
				sysTypeColumn.add(row[0]);
			} else {
				sysTypeColumn.add(null);
			}
		}
		if(sysTypeColumn.size() > 0){
			for (String sysType : sysTypeColumn) {
				if(sysType != null){
					sysType = sysType.replaceAll("\\s*", "");
					if(sysType.trim().equals(SystemType.CG.getLabel())){
						custodyType.add(sysType);
					}else if (sysType.trim().equals(SystemType.NORMAL.getLabel())){
						normalType.add(sysType);
					}
				}
			}
			if(normalType.size() > 0 && normalType.size() == detailList.size()){
				rewordRecordSysType = 1;
			}else if (custodyType.size() > 0 && custodyType.size() == detailList.size()){
				rewordRecordSysType = 2;
			} else {
				contentErrorList.add("系统类型必须全为“普通”或者“存管”！");
			}
			if(contentErrorList.size() > 0){
				return contentErrorList;
			}
		}
		for (int i = 0; i < detailList.size(); i++) {
			if (detailList.get(i) != null) {
				String row[] = detailList.get(i);
				phoneColumn.add(row[1]);
				Integer userId = getUserIdByPhoneAndSysType(row[1], rewordRecordSysType);
				userIdList.add(userId);
				moneyColumn.add(row[2]);
			} else {
				phoneColumn.add(null);
				userIdList.add(null);
				moneyColumn.add(null);
			}
		}

		if (detailList.size() < 1) {
			contentErrorList.add("没有可用数据导入！");
		}else if (detailList.size() > 5000) {
			contentErrorList.add("一次不能导入超过5000条记录！");
		}else {
			Pattern pattern = Pattern.compile("^(([1-9]{1}[0-9]*)|([0]{1}))(\\.([0-9]){1,9})?$");
			for (int i = 0; i < detailList.size(); i++) {
				if (detailList.get(i) != null) {
					StringBuilder message = new StringBuilder();
					message.append("第" + (i + 2) + "行：");
					boolean noProblem = true;

					// 重复的
					if (Collections.frequency(phoneColumn, phoneColumn.get(i)) > 1) {
						message.append(" 重复的手机号" + phoneColumn.get(i) + "  ");
						noProblem = false;
					}

					// 用户不存在
					Integer uId = userIdList.get(i);
					if (uId == null) {
						if(rewordRecordSysType == 2){
							message.append(" 用户" + phoneColumn.get(i) + "未开通银行存管  ");
						}else {
							message.append(" 用户" + phoneColumn.get(i) + "不存在  ");
						}
						noProblem = false;
					}
					// 金额是否为数字
					Matcher match = pattern.matcher(moneyColumn.get(i));
					if (!match.matches()) {
						message.append(" " + moneyColumn.get(i) + "非有效金额  ");
						noProblem = false;
					}
					
					/** 要求红包金额在0~100元之间 */
					if (match.matches()) {
						double money = Double.parseDouble(moneyColumn.get(i));
						if (money <= min || money > max) {
							message.append(" 红包金额只能在" + min + "~" + max + "元之间  ");
							noProblem = false;
						}
					}
					
					if (!noProblem) {
						contentErrorList.add(message.toString());
					}
				}
			}
		}

		if (contentErrorList.isEmpty()) {
			record.setSysType(rewordRecordSysType);
			rewardMapper.insertRewardRecord(record);
			List<UserCashRedPacket> insertList = new ArrayList<UserCashRedPacket>();
			// 代充值
			List<ReplacementRecharge> replacementRechargeList = new ArrayList<>();
				for (int i = 0; i < detailList.size(); i++) {
					if (detailList.get(i) != null) {
						String row[] = detailList.get(i);
						Integer userId = userIdList.get(i);
						UserCashRedPacket item = new UserCashRedPacket();
						item.setUserId(userId);
						item.setSubjectId(0);
						item.setPhone(row[1]);
						item.setMoney(BigDecimal.valueOf(Double.parseDouble(row[2])));
						item.setGrantId(record.getId());
						insertList.add(item);
						// 存管版本添加代充值记录
						if (rewordRecordSysType == SystemType.CG.getValue()){
							ReplacementRecharge replacementRecharge = new ReplacementRecharge();
							replacementRecharge.setUserId(userId);
							replacementRecharge.setUserRole("INVESTOR");
							replacementRecharge.setRechargeMoney(BigDecimal.valueOf(Double.parseDouble(row[2])));
							replacementRecharge.setStatus(1);
							replacementRecharge.setRechargeUserName("0");
							replacementRecharge.setAuditUserName("0");
							replacementRecharge.setRewardRecordId(record.getId());
							replacementRechargeList.add(replacementRecharge);
						}
					}
				}
			if (insertList.size() > 5000 || replacementRechargeList.size() > 5000) {
				rewardMapper.deleteRewardRecord(record);
				contentErrorList.add("一次不能导入超过5000条记录！");
			} else {
				userRedpacketsMapper.insertCashRedPacketRecordDetail(insertList);
				//存管版本添加代充值记录
				if (rewordRecordSysType == SystemType.CG.getValue()){
					userRedpacketsMapper.batchInsertReplacementRecharge(replacementRechargeList);
				}
			}
		}
		return contentErrorList;
	}

	private Integer getUserIdByPhoneAndSysType(String phoneNum, int sysType){
		Integer userId = rewardMapper.getUserIdbyPhone(phoneNum);
		if(sysType != 1){
			userId = rewardMapper.getUserXWUserId("INVESTOR" + userId);
		}
		return userId;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String grantCashRedPacket(RewardRecord rewardRecord) throws Exception {
	//	Byte granted = rewardMapper.selectGrantedById(rewardRecord.getId());
		RewardRecord record = rewardMapper.getRewardRecordById(rewardRecord);
		if (record != null
                && record.getGranted() == 0 ) {// 判断状态为未发放，线上出现的多个页面重复点击发放一个名单出现发放多次的BUG修复。
            //短信站内信添加标题
			String grantName = record.getGrantName();
			String[] grantNameArr = null;
			if (!StringUtils.isBlank(grantName)){
				grantNameArr = grantName.split("_");
			}

			// 修改发放记录
			long nowMiniSecond = System.currentTimeMillis();
			Timestamp datetime = new Timestamp(nowMiniSecond);
			rewardRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
			rewardRecord.setGranted((byte) 1);

			// 查出要发放的明细记录
			UserCashRedPacket userCashRedPacketCondition = new UserCashRedPacket();
			userCashRedPacketCondition.setGrantId(rewardRecord.getId());
			RowBounds bounds = new RowBounds();
			List<UserCashRedPacket> userCashRedPacketList = userRedpacketsMapper
					.getCashRedPacketRecordsDetail(userCashRedPacketCondition, bounds);
			// 取出企业帐号(普通版)
			Integer salaryAccountId = rewardMapper.getSalaryAccountId();
			if (salaryAccountId == null) {
				throw new GrantCashRedPacketException("企业账户不存在");
			}
			T6101 salaryAccountCondition = new T6101();
			salaryAccountCondition.setF01(salaryAccountId);
			T6101 salaryAccount = rewardMapper.getT6101ForUpdate(salaryAccountCondition);
			// 更新每个用户资金和流水
			List<T6101> t6101sToUpdate = new ArrayList<>();
			List<T6102> t6102sToInsert = new ArrayList<>();
			List<T6123> t6123sToInsert = new ArrayList<>();
			List<T1040> t1040sToInsert = new ArrayList<>();
			Message.loadProperties();
			BigDecimal grantSum = new BigDecimal(0);
			for (UserCashRedPacket userCashRedPacket : userCashRedPacketList) {
				T6101 userAccountCondition = new T6101();
				userAccountCondition.setF02(userCashRedPacket.getUserId());
				userAccountCondition.setF03("WLZH");
				T6101 userAccount = rewardMapper.getT6101ForUpdate(userAccountCondition);
				if (userAccount == null) {
					throw new GrantCashRedPacketException("用户" + userCashRedPacket.getPhone() + "资金账户不存在");
				}
				userAccount.setF06(userAccount.getF06().add(userCashRedPacket.getMoney()));
				userAccount.setF07(datetime);
				grantSum = grantSum.add(userCashRedPacket.getMoney());
				// 更新t6101用户账户信息
				t6101sToUpdate.add(userAccount);
				// t6102插入个人资金交易记录
				T6102 userTradeRecord = new T6102();
				userTradeRecord.setF02(userAccount.getF01());
				userTradeRecord.setF03(5118);// 现金红包
				userTradeRecord.setF04(salaryAccountId);
				userTradeRecord.setF05(datetime);
				userTradeRecord.setF06(userCashRedPacket.getMoney());
				userTradeRecord.setF08(userAccount.getF06());
				userTradeRecord.setF09("现金红包");
				t6102sToInsert.add(userTradeRecord);
				// t6102插入企业资金交易记录
				T6102 companyTradeRecord = new T6102();
				companyTradeRecord.setF02(salaryAccountId);
				companyTradeRecord.setF03(5118);
				companyTradeRecord.setF04(userAccount.getF01());
				companyTradeRecord.setF05(datetime);
				companyTradeRecord.setF07(userCashRedPacket.getMoney());
				salaryAccount.setF06(salaryAccount.getF06().subtract(userCashRedPacket.getMoney()));
				companyTradeRecord.setF08(salaryAccount.getF06());
				companyTradeRecord.setF09("现金红包");
				t6102sToInsert.add(companyTradeRecord);
				// 更新t6195
				userCashRedPacket.setGrantStatus("CG");
				// 发站内信
				T6123 t6123Letter = new T6123();
				t6123Letter.setF02(userCashRedPacket.getUserId());
				t6123Letter.setF03("现金红包发放成功");
				t6123Letter.setF04(datetime);
				t6123Letter.setF05("WD");
				rewardMapper.insertT6123(t6123Letter);
				t6123sToInsert.add(t6123Letter);
				// 发短信
				T1040 t1040Message = new T1040();
				t1040Message.setF02(0);
				StringBuilder shortMessage = new StringBuilder(Message.get("short.cashRedPacket"));
				if (grantNameArr.length > 0) {
					shortMessage.replace(shortMessage.indexOf("{title}"), shortMessage.indexOf("{title}") + "{title}".length(), "" + grantNameArr[grantNameArr.length - 1]);
				}else{
					shortMessage.replace(shortMessage.indexOf("{title}"), shortMessage.indexOf("{title}") + "{title}".length(), "" + "");
				}
				shortMessage.replace(shortMessage.indexOf("{money}"),
						shortMessage.indexOf("{money}") + "{money}".length(), "" + userCashRedPacket.getMoney());
				shortMessage.replace(shortMessage.indexOf("{balance}"),
						shortMessage.indexOf("{balance}") + "{balance}".length(), "" + userAccount.getF06());
				t1040Message.setF03(shortMessage.toString());
				t1040Message.setF04(datetime);
				t1040Message.setF05("W");
				rewardMapper.insertT1040(t1040Message);
				t1040sToInsert.add(t1040Message);
			}

			rewardRecord.setGrantCount(userCashRedPacketList.size());
			rewardRecord.setGrantSum(grantSum);
			rewardRecord.setInService((byte) 0);
			rewardMapper.updateRewardRecord(rewardRecord);

			salaryAccount.setF07(datetime);
			t6101sToUpdate.add(salaryAccount);
			// 批量修改t6101
			rewardMapper.batchUpdateT6101(t6101sToUpdate);
			// 批量插入t6102
			rewardMapper.batchInsertT6102(t6102sToInsert);
			// 批量插入T6124和T1041
			List<T6124> t6124sToInsert = new ArrayList<>();
			List<T1041> t1041sToInsert = new ArrayList<>();
			for (int i = 0; i < userCashRedPacketList.size(); i++) {
				T6124 t6124Letter = new T6124();
				t6124Letter.setF01(t6123sToInsert.get(i).getF01());
				StringBuilder internalMessage = new StringBuilder(Message.get("internal.cashRedPacket"));
				if (grantNameArr.length > 0) {
					internalMessage.replace(internalMessage.indexOf("{title}"), internalMessage.indexOf("{title}") + "{title}".length(), "" + grantNameArr[grantNameArr.length - 1]);
				}else{
					internalMessage.replace(internalMessage.indexOf("{title}"), internalMessage.indexOf("{title}") + "{title}".length(), "" + "");
				}
				internalMessage.replace(internalMessage.indexOf("{money}"),
						internalMessage.indexOf("{money}") + "{money}".length(),
						"" + userCashRedPacketList.get(i).getMoney());
				internalMessage.replace(internalMessage.indexOf("{balance}"),
						internalMessage.indexOf("{balance}") + "{balance}".length(),
						"" + t6101sToUpdate.get(i).getF06());
				t6124Letter.setF02(internalMessage.toString());
				t6124sToInsert.add(t6124Letter);
				T1041 t1041Message = new T1041();
				t1041Message.setF01(t1040sToInsert.get(i).getF01());
				t1041Message.setF02(userCashRedPacketList.get(i).getPhone());
				t1041sToInsert.add(t1041Message);
			}
			// 批量插入t6124
			rewardMapper.batchInsertT6124(t6124sToInsert);
			// 批量插入t1041
			rewardMapper.batchInsertT1041(t1041sToInsert);
			// 批量更新t6195
			userRedpacketsMapper.batchUpdateCashRedPacketRecordDetail(userCashRedPacketList);
			return "发放完成";
		} else {
			return "已发放过，不可重复发放";
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String cancelBackVoucher(RewardRecord rewardRecord, UserRedpackets userRedpacket) throws Exception {
		rewardRecord.setGranted((byte) 3);
		rewardMapper.updateRewardRecord(rewardRecord);
		userRedpacket.setGrantId(rewardRecord.getId());
		List<UserRedpackets> userRedpacketsList = userRedpacketsMapper.findAll(userRedpacket);
		for (UserRedpackets item : userRedpacketsList) {
			item.setGrantStatus((byte) 3);
		}
		if (!userRedpacketsList.isEmpty()) {
			userRedpacketsMapper.batchUpdateUserRedpacket(userRedpacketsList);
		}
		RewardRecord r = rewardMapper.getRewardRecordById(rewardRecord);
		return r.getGrantName() + "已作废";
	}

	@Override
	public String cancelCashRedPacket(RewardRecord rewardRecord) throws Exception {
		rewardRecord.setGranted((byte) 3);
		rewardMapper.updateRewardRecord(rewardRecord);
		UserCashRedPacket userCashRedPacketCondition = new UserCashRedPacket();
		userCashRedPacketCondition.setGrantId(rewardRecord.getId());
		RowBounds bounds = new RowBounds();
		List<UserCashRedPacket> userCashRedPacketList = userRedpacketsMapper
				.getCashRedPacketRecordsDetail(userCashRedPacketCondition, bounds);
		for (UserCashRedPacket userCashRedPacket : userCashRedPacketList) {
			userCashRedPacket.setGrantStatus("ZF");
		}
		userRedpacketsMapper.batchUpdateCashRedPacketRecordDetail(userCashRedPacketList);
		RewardRecord r = rewardMapper.getRewardRecordById(rewardRecord);
		return r.getGrantName() + "已作废";
	}

	@Override
	public List<BackVoucherGrantStatistics> backVoucherGrantStatistics(Integer grantId) {
		return userRedpacketsMapper.backVoucherGrantStatistics(grantId);
	}

	@Override
	public void doAlternativeRechargeReward(RewardRecord rewardRecord, List<UserCashRedPacket> userCashRedPackets) throws Exception {
		int xwRequestId;
		//交易类型
		BusinessType businessType = new BusinessType();
		businessType.setCode(SysTradeFeeCode.CASH_VOUCHER);// 现金红包类型
		businessType.setName("现金红包");
		businessType.setStatus("QY");
		if(userCashRedPackets.size() > 0){
			String userRole = "INVESTOR";
			// 代充值方式发放红包
			for (UserCashRedPacket userCashRedPacket: userCashRedPackets) {
				// 代充值账户
				BigDecimal xwReplaceRechageAccountBalance = replacementRechargeService.getReplacementRechargeAccountBalance();
				BigDecimal localReplaceRechargeAccountBalance = replacementRechargeMapper.getReplacementRechargeAccountBalance();
				ReplacementRecharge replacementRecharge = new ReplacementRecharge();
				replacementRecharge.setRewardRecordId(rewardRecord.getId());
				replacementRecharge.setUserId(userCashRedPacket.getUserId());
				if(xwReplaceRechageAccountBalance.compareTo(userCashRedPacket.getMoney()) >= 0
						&& localReplaceRechargeAccountBalance.compareTo(userCashRedPacket.getMoney()) >= 0){
					try {
						// 代充值账户余额足够时请求新网代充值
						xwRequestId = xwRechargeService.doAlternativeRecharge(userCashRedPacket.getUserId(),
								userRole + userCashRedPacket.getUserId(), userCashRedPacket.getMoney(), businessType);
						if(xwRequestId > 0){
							replacementRecharge.setXwRequestId(xwRequestId);
							if (replacementRecharge.getRewardRecordId() > 0 && replacementRecharge.getUserId() > 0){
								Integer recordId = replacementRechargeMapper.getReplacementRechargeId(replacementRecharge);
								if(recordId != null){
									replacementRecharge.setId(recordId);
									// 更新代充值列表
									replacementRechargeMapper.updateReplacementRecharge(replacementRecharge);
									LOG.info("代充值记录id: " + recordId + " ,新网请求id: " + xwRequestId );
									/*String xwRequestState = userRedpacketsMapper.getXWRequestState(xwRequestId);
									if (xwRequestState != null){
										// 根据新网请求状态更新转账记录状态
										if(xwRequestState.equals("CG")){
											userCashRedPacket.setGrantStatus("CG");
										}else if (xwRequestState.equals("SB")){
											userCashRedPacket.setGrantStatus("SB");
										}else {
											userCashRedPacket.setGrantStatus("DZZ");
										}
									}else {
										LOG.info("requestId: " + xwRequestId + "新网请求状态不存在");
									}*/
								}
							}
						}
					} catch (Exception e) {
						userCashRedPacket.setGrantStatus("SB");
						if (e instanceof XWTradeException){
							userCashRedPacket.setMsg("新网代充值失败");
						}else {
							userCashRedPacket.setMsg("代充值请求异常");
						}
						// 多次请求新网,不中断
						LOG.error("[(现金红包)代充值操作异常:]" + e.getMessage() + "userId: " + userCashRedPacket.getUserId(), e);
					}
				}else{
					userCashRedPacket.setGrantStatus("DZZ");
					userCashRedPacket.setMsg("代充值余额不足");
				}
			}
		}
	}


	@Override
	public List<UserCashRedPacket> getCustodyCashRedPacket(RewardRecord rewardRecord) throws Exception {
		List<UserCashRedPacket> userCashRedPacketList = new ArrayList<>();
		RewardRecord record = rewardMapper.getRewardRecordById(rewardRecord);
		if (record != null
				&& record.getGranted() == 0 ) {// 判断状态为未发放，线上出现的多个页面重复点击发放一个名单出现发放多次的BUG修复。
			// 查出要发放的明细记录
			UserCashRedPacket userCashRedPacketCondition = new UserCashRedPacket();
			userCashRedPacketCondition.setGrantId(rewardRecord.getId());
			userCashRedPacketCondition.setGrantStatus("DZZ");// 原始状态
			RowBounds bounds = new RowBounds();
			// 只处理待转账状态
			userCashRedPacketList = userRedpacketsMapper
					.getCashRedPacketRecordsDetail(userCashRedPacketCondition, bounds);
			// 使用代充值账户
			String salaryAccountCode = "XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH";
			T6101 salaryAccount = rewardMapper.getCustodyAccount(1, salaryAccountCode);
			if (salaryAccount != null && salaryAccount.getF01() == null) {
				throw new GrantCashRedPacketException("企业账户不存在");
			}
			for (UserCashRedPacket userCashRedPacket : userCashRedPacketList) {
				T6101 userAccountCondition = new T6101();
				userAccountCondition.setF02(userCashRedPacket.getUserId());
				userAccountCondition.setF03("XW_INVESTOR_WLZH");
				T6101 userAccount = rewardMapper.getT6101ForUpdate(userAccountCondition);
				if (userAccount == null) {
					throw new GrantCashRedPacketException("用户" + userCashRedPacket.getPhone() + "资金账户不存在");
				}
			}
		}
		return userCashRedPacketList;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String modifyLocalDetail(RewardRecord rewardRecord, List<UserCashRedPacket> userCashRedPacketList) throws Exception{
		//短信站内信添加标题
		String grantName = rewardRecord.getGrantName();
		String[] grantNameArr = null;
		if (!StringUtils.isBlank(grantName)){
			grantNameArr = grantName.split("_");
		}
		List<UserCashRedPacket> successUserCashRedPacketList = new ArrayList<>();

		// 修改发放记录
		long nowMiniSecond = System.currentTimeMillis();
		Timestamp datetime = new Timestamp(nowMiniSecond);
		rewardRecord.setGrantTime(new Timestamp(System.currentTimeMillis()));
		rewardRecord.setGranted((byte) 1);
		// 用户账户信息
		List<T6101> userAccounts = new ArrayList<>();
		// 用户站内信
		List<T6123> t6123sToInsert = new ArrayList<>();
		// 短信(待发送短信内容)
		List<T1040> t1040sToInsert = new ArrayList<>();
		Message.loadProperties();
		// 总发送金额, 总发送数
		BigDecimal grantSum = new BigDecimal(0);
		int grantCount = 0;
		// 查询新网充值状态
		List<Map<String, Object>> xwRechargeState = rewardMapper.getXwRechargeState(rewardRecord.getId());
		if(xwRechargeState.size() > 0){
			for (UserCashRedPacket userCashRedPacket : userCashRedPacketList) {
				userCashRedPacket.setGrantStatus(getXwRechargeStateByUserId(xwRechargeState, userCashRedPacket.getUserId()));
				LOG.info("userId: " + userCashRedPacket.getUserId() + " ,状态: " + getXwRechargeStateByUserId(xwRechargeState, userCashRedPacket.getUserId()));
			}
		}
		for (UserCashRedPacket userCashRedPacket : userCashRedPacketList) {
			// 成功发送的才计算
			if(userCashRedPacket.getGrantStatus() != null && userCashRedPacket.getGrantStatus().equals("CG")){
				T6101 userAccount = rewardMapper.getCustodyAccount(userCashRedPacket.getUserId(), "XW_INVESTOR_WLZH");
				if(userAccount != null) {
					userAccounts.add(userAccount);
					successUserCashRedPacketList.add(userCashRedPacket);
					grantSum = grantSum.add(userCashRedPacket.getMoney());
					grantCount = grantCount + 1;
					// 发站内信
					T6123 t6123Letter = new T6123();
					t6123Letter.setF02(userCashRedPacket.getUserId());
					t6123Letter.setF03("现金红包发放成功");
					t6123Letter.setF04(datetime);
					t6123Letter.setF05("WD");
					rewardMapper.insertT6123(t6123Letter);
					t6123sToInsert.add(t6123Letter);
					// 发短信
					T1040 t1040Message = new T1040();
					t1040Message.setF02(0);
					StringBuilder shortMessage = new StringBuilder(Message.get("short.cashRedPacket"));
					if (grantNameArr.length > 0) {
						shortMessage.replace(shortMessage.indexOf("{title}"), shortMessage.indexOf("{title}") + "{title}".length(), "" + grantNameArr[grantNameArr.length - 1]);
					} else {
						shortMessage.replace(shortMessage.indexOf("{title}"), shortMessage.indexOf("{title}") + "{title}".length(), "" + "");
					}
					shortMessage.replace(shortMessage.indexOf("{money}"),
							shortMessage.indexOf("{money}") + "{money}".length(), "" + userCashRedPacket.getMoney());
					shortMessage.replace(shortMessage.indexOf("{balance}"),
							shortMessage.indexOf("{balance}") + "{balance}".length(), "" + userAccount.getF06());
					t1040Message.setF03(shortMessage.toString());
					t1040Message.setF04(datetime);
					t1040Message.setF05("W");
					rewardMapper.insertT1040(t1040Message);
					t1040sToInsert.add(t1040Message);
				}
			}
		}
		rewardRecord.setGrantCount(grantCount);
		rewardRecord.setGrantSum(grantSum);
		rewardRecord.setInService((byte) 0);
		rewardMapper.updateRewardRecord(rewardRecord);

		// 批量插入T6124和T1041
		List<T6124> t6124sToInsert = new ArrayList<>();
		List<T1041> t1041sToInsert = new ArrayList<>();
		if (successUserCashRedPacketList.size() > 0) {
			for (int i = 0; i < successUserCashRedPacketList.size(); i++) {
				if(userAccounts.size() > 0){
					T6124 mail = new T6124();
					mail.setF01(t6123sToInsert.get(i).getF01());
					// 站内信模板
					StringBuilder mailDetail = new StringBuilder(Message.get("internal.cashRedPacket"));
					if (grantNameArr.length > 0) {
						mailDetail.replace(mailDetail.indexOf("{title}"), mailDetail.indexOf("{title}") + "{title}".length(), "" + grantNameArr[grantNameArr.length - 1]);
					}else{
						mailDetail.replace(mailDetail.indexOf("{title}"), mailDetail.indexOf("{title}") + "{title}".length(), "" + "");
					}
					mailDetail.replace(mailDetail.indexOf("{money}"), mailDetail.indexOf("{money}") + "{money}".length(),
							"" + successUserCashRedPacketList.get(i).getMoney());
					mailDetail.replace(mailDetail.indexOf("{balance}"), mailDetail.indexOf("{balance}") + "{balance}".length(),
							"" + userAccounts.get(i).getF06());
					mail.setF02(mailDetail.toString());
					t6124sToInsert.add(mail);
					T1041 t1041Message = new T1041();
					t1041Message.setF01(t1040sToInsert.get(i).getF01());
					t1041Message.setF02(successUserCashRedPacketList.get(i).getPhone());
					t1041sToInsert.add(t1041Message);
				}
			}
			// 批量插入t6124
			rewardMapper.batchInsertT6124(t6124sToInsert);
			// 批量插入t1041
			rewardMapper.batchInsertT1041(t1041sToInsert);
		}
		// 批量更新t6195
		userRedpacketsMapper.batchUpdateCashRedPacketRecordDetail(userCashRedPacketList);
		return "发送成功";
	}

	private String getXwRechargeStateByUserId(List<Map<String, Object>> xwRechargeState, Integer userId){
		for (Map<String, Object> temp : xwRechargeState) {
			if(temp != null){
				if (userId.toString().equals(temp.get("userId").toString())) {
					return temp.get("state").toString() == null ? null: temp.get("state").toString();
				}
			}
		}
		return null;
	}

}