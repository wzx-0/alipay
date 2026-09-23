package cn.seehoo.spg.bizcom.gd.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import cn.hutool.core.util.ObjectUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.*;
import cn.seehoo.spg.bizcom.gd.model.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.seehoo.spg.bizcom.utils.SeqUtil;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;


public class GdCheckClient extends GdBaseClient implements GdMapClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdCheckClient.class);
    /**
     * 高德地理编码
     */
    private static final String GD_MAP_CHECK_URL = "/v3/geocode/geo";

    @Autowired
    private RequestLogClient requestLogClient;
    @Autowired
    private RedisOperateService redis;


    @Override
    public List<GdMapCheckInfo> gdMapCheck(String address) {
        if (conf.checkMock()) {
            LOGGER.info(">>>>>>[高德]，地理编码核查，触发mock，直接返回NULL，关键字={}", address);
            return Collections.singletonList(mock());
        }
        if (StrUtil.isEmpty(address)) {
            throw new BusinessException("查询关键字为空");
        }
        String url = "";
        boolean status = false;
        String req = "address=" + address + "&key=" + conf.getKey() + "&output=JSON";
        String rsp = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // header
            conf.setTransCode(GD_MAP_CHECK_URL);
            conf.setScnNo("A01");
            Map<String, String> headers = SeqUtil.gdBuildReqHeader(conf.getSvcNo(),
                    conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
            headers.put("Content-Type", ContentType.JSON.toString());
            // body
            url = conf.getHost() + GD_MAP_CHECK_URL + "?address=" + address + "&key=" + conf.getKey() + "&output=JSON";
            String proxy = conf.getProxy();
            rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
            LOGGER.debug(">>>>>>[高德]，地理编码，信息={}", rsp);
            status = this.checkResult(rsp);
            if (!status) {
                throw new BusinessException(">>>>>>[高德], 高德调用结果失败");
            }
            JSONObject jsonObject = JSON.parseObject(rsp);
            JSONArray geocodes = jsonObject.getJSONArray("geocodes");
            List<GdMapCheckInfo> gdi = new ArrayList<>();
            if (geocodes != null && !geocodes.isEmpty()) {
                for (int i = 0; i < geocodes.size(); i++) {
                    GdMapCheckInfo gdMapCheckInfo = new GdMapCheckInfo();
                    JSONObject item = geocodes.getJSONObject(i);
                    gdMapCheckInfo.setProvince(item.getString("province"));
                    gdMapCheckInfo.setCity(item.getString("city"));
                    gdMapCheckInfo.setDistrict(item.getString("district"));
                    gdMapCheckInfo.setLevel(item.getString("level"));
                    gdi.add(gdMapCheckInfo);
                }
            }
            LOGGER.debug(">>>>>>[高德]，查询到的地理编码信息，信息={}", gdi);
            return gdi;
        } catch (Exception e) {
            LOGGER.error(">>>>>>[高德]，地理编码异常,", e);
            return null;
        } finally {
            try {
                httpClient.close();
                LOGGER.info(">>>>>>>>>>开始记录地理编码接口调用日志");
                RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
                requestLogSaveDto.setSystemName(SystemNameConstant.GD);
                requestLogSaveDto.setInterfaceName("地理编码");
                requestLogSaveDto.setUrl(url);
                requestLogSaveDto.setRequestMsg(req);
                requestLogSaveDto.setResponseMsg(rsp);
                requestLogSaveDto.setStatus(status ? "1" : "0");
                if (StrUtil.isNotBlank(rsp)) {
                    String msg = JSON.parseObject(rsp).getString("Message");
                    requestLogSaveDto.setReason(msg);
                }
                requestLogClient.saveRequestLog(requestLogSaveDto);
            } catch (Exception e) {
                LOGGER.error(">>>>>>>>>>记录地理编码接口调用日志失败,", e);
            }
        }
    }


    public GdMapCheckInfo mock() {
        GdMapCheckInfo gdMapCheckInfo = new GdMapCheckInfo();
        gdMapCheckInfo.setProvince("北京市");
        gdMapCheckInfo.setCity("北京市");
        gdMapCheckInfo.setDistrict("朝阳区");
        gdMapCheckInfo.setLevel("门址");
        return gdMapCheckInfo;
    }

//	public static void main(String[] args) {
//		GdBaseClient gdBaseClient = new GdCheckClient();
//		boolean status = false;
//		try{
//			String rsp = "{\n" +
//					"\t\"status\":\"1\",\n" +
//					"\t\"info\":\"OK\",\n" +
//					"\t\"infocode\":\"10000\",\n" +
//					"\t\"count\":\"1\",\n" +
//					"\t\"geocodes\":[\n" +
//					"\t\t{\n" +
//					"\t\t\t\"formatted_address\":\"北京市北京市丰台区组家庄\",\n" +
//					"\t\t\t\"country\":\"中国\",\n" +
//					"\t\t\t\"province\":\"北京市\",\n" +
//					"\t\t\t\"citycode\":\"010\",\n" +
//					"\t\t\t\"city\":\"北京市\",\n" +
//					"\t\t\t\"district\":[],\n" +
//					"\t\t\t\"township\":[],\n" +
//					"\t\t\t\"level\":\"市\"\n" +
//					"\t\t},\n" +
//					"\t\t{\n" +
//					"\t\t\t\"formatted_address\":\"河北省承德市隆化县\",\n" +
//					"\t\t\t\"country\":\"中国\",\n" +
//					"\t\t\t\"province\":\"河北省\",\n" +
//					"\t\t\t\"citycode\":\"010\",\n" +
//					"\t\t\t\"city\":\"承德市\",\n" +
//					"\t\t\t\"district\":\"隆化县\",\n" +
//					"\t\t\t\"township\":[],\n" +
//					"\t\t\t\"level\":\"住宅区\"\n" +
//					"\t\t}\n" +
//					"\t\t]\n" +
//					"}";
//			status = gdBaseClient.checkResult(rsp);
//			if (!status) {
//				throw new BusinessException(">>>>>>[高德], 高德调用结果失败");
//			}
//			JSONObject jsonObject = JSON.parseObject(rsp);
//			JSONArray geocodes = jsonObject.getJSONArray("geocodes");
//			List<GdMapCheckInfo> gdi = new ArrayList<>();
//			if(geocodes != null&&!geocodes.isEmpty()){
//				for(int i = 0;i<geocodes.size();i++){
//					GdMapCheckInfo gdMapCheckInfo = new GdMapCheckInfo();
//					JSONObject item = geocodes.getJSONObject(i);
//					gdMapCheckInfo.setProvince(item.getString("province"));
//					gdMapCheckInfo.setCity(item.getString("city"));
//					gdMapCheckInfo.setDistrict(item.getString("district"));
//					gdMapCheckInfo.setLevel(item.getString("level"));
//					gdi.add(gdMapCheckInfo);
//				}
//			}
//			System.out.println(JSON.toJSONString(gdi));
//	} catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }