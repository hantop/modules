package com.fenlibao.p2p.model.entity.umeng;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

public abstract class IOSNotification extends UmengNotification {


	protected static final HashSet<String> APS_KEYS = new HashSet<String>(Arrays.asList(new String[]{
			"alert", "badge", "sound", "content-available"
	}));

	protected static final HashSet<String> MODELDATE_KEYS = new HashSet<String>(Arrays.asList(new String[]{
			"activityUrl"
	}));
	
	@Override
	public boolean setPredefinedKeyValue(String key, Object value) throws Exception {
		if (ROOT_KEYS.contains(key)) {
			// This key should be in the root level
			rootJson.put(key, value);
		} else if (APS_KEYS.contains(key)) {
			// This key should be in the aps level
			JSONObject apsJson = null;
			JSONObject payloadJson = null;
			if (rootJson.containsKey("payload")) {
				payloadJson = rootJson.getJSONObject("payload");
			} else {
				payloadJson = new JSONObject();
				rootJson.put("payload", payloadJson);
			}
			if (payloadJson.containsKey("aps")) {
				apsJson = payloadJson.getJSONObject("aps");
			} else {
				apsJson = new JSONObject();
				payloadJson.put("aps", apsJson);
			}
			apsJson.put(key, value);
		}else if (MODELDATE_KEYS.contains(key)) {
			// This key should be in the aps level
			JSONObject modelJson = null;
			JSONObject payloadJson = null;
			if (rootJson.containsKey("payload")) {
				payloadJson = rootJson.getJSONObject("payload");
			} else {
				payloadJson = new JSONObject();
				rootJson.put("payload", payloadJson);
			}
			if (payloadJson.containsKey("modelData")) {
				modelJson = payloadJson.getJSONObject("modelData");
			} else {
				modelJson = new JSONObject();
				payloadJson.put("modelData", modelJson);
			}
			modelJson.put(key, value);
		}
		else if (POLICY_KEYS.contains(key)) {

			JSONObject policyJson = null;
			if (rootJson.containsKey("policy")) {
				policyJson = rootJson.getJSONObject("policy");
			} else {
				policyJson = new JSONObject();
				rootJson.put("policy", policyJson);
			}
			policyJson.put(key, value);
		}

		else {
			if (key == "payload" || key == "aps" || key == "policy") {
				throw new Exception("You don't need to set value for " + key + " , just set values for the sub keys in it.");
			} else {
				throw new Exception("Unknownd key: " + key);
			}
		}
		
		return true;
	}

	public boolean setCustomizedField(String key, String value) throws Exception {
		//rootJson.put(key, value);
		JSONObject payloadJson = null;
		if (rootJson.containsKey("payload")) {
			payloadJson = rootJson.getJSONObject("payload");
		} else {
			payloadJson = new JSONObject();
			rootJson.put("payload", payloadJson);
		}
		payloadJson.put(key, value);
		return true;
	}

	public void setAlert(String token) throws Exception {
    	setPredefinedKeyValue("alert", token);
    }
	
	public void setBadge(Integer badge) throws Exception {
    	setPredefinedKeyValue("badge", badge);
    }
	
	public void setSound(String sound) throws Exception {
    	setPredefinedKeyValue("sound", sound);
    }

	public void setActivityUrl(String activityUrl) throws Exception {
		setPredefinedKeyValue("activityUrl", activityUrl);
	}
	
	public void setContentAvailable(Integer contentAvailable) throws Exception {
    	setPredefinedKeyValue("content-available", contentAvailable);
    }

	public void setPushType(String pushType) throws Exception {
		setCustomizedField("pushType", pushType);
	}

	public void setModelType(String modelType) throws Exception {
		setCustomizedField("modelType", modelType);
	}
}
