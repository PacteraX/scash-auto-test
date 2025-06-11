package com.senko.scash.trm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senko.scash.BaseScashTest;
import com.senko.scash.trm.bean.dto.ListResponse;
import com.senko.scash.trm.bean.dto.message.GetListByCodesRequest;
import com.senko.scash.trm.bean.dto.message.GetListByTypeRequest;
import com.senko.scash.trm.bean.dto.message.MessageLanguageRequest;
import com.senko.scash.trm.bean.entity.MessageResource;
import com.senko.scash.trm.controller.MessageController;
import com.senko.scash.utils.CompareUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestPropertySource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ContextConfiguration(classes = {TextResourceManagerApplication.class})
@TestPropertySource(locations = "classpath:application.properties")
public class MessageControllerTest extends BaseScashTest {

    @Autowired
    private MessageController messageController;

    @BeforeClass
    public void setup() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void doGetListByCodesTest() throws JsonProcessingException {
        // excel参数取得
        Properties requestMap = getRequestProps();
        String requestData = requestMap.getProperty("requestData");
        ObjectMapper objectMapper = new ObjectMapper();
        GetListByCodesRequest getListByCodesRequest = objectMapper.readValue(requestData, GetListByCodesRequest.class);

        // 执行Controller
        ListResponse<MessageResource> response = messageController.getListByCodes(getListByCodesRequest);

        // excel想定结果取得
        Properties responseMap = getResponseProps();

        // statusCode判定
        int statusCode = Integer.parseInt(responseMap.getProperty("responseCode"));
        assertEquals(response.getResponseCode(), statusCode);

        // responsePayload结果判定
        String responseData = responseMap.getProperty("responsePayload");
        List<MessageResource> list = objectMapper.readValue(responseData, new TypeReference<List<MessageResource>>() {});
        boolean compareResult = CompareUtil.compareLists(response.getResponsePayload(), list);
        assertTrue(compareResult, "responsePayload結果一致しない。");
    }

    @Test
    public void doGetListByTypeTest() throws JsonProcessingException {
        // excel参数取得
        Properties requestMap = getRequestProps();
        String requestData = requestMap.getProperty("requestData");
        ObjectMapper objectMapper = new ObjectMapper();
        GetListByTypeRequest getListByTypeRequest = objectMapper.readValue(requestData, GetListByTypeRequest.class);

        // 执行Controller
        ListResponse<MessageResource> response = messageController.getListByType(getListByTypeRequest);

        // excel想定结果取得
        Properties responseMap = getResponseProps();

        // statusCode判定
        int statusCode = Integer.parseInt(responseMap.getProperty("responseCode"));
        assertEquals(response.getResponseCode(), statusCode);

        // responsePayload结果判定
        String responseData = responseMap.getProperty("responsePayload");
        ArrayList list = objectMapper.readValue(responseData, new TypeReference<ArrayList>() {});
        boolean compareResult = CompareUtil.compareLists(response.getResponsePayload(), list);
        assertTrue(compareResult, "responsePayload結果一致しない。");
    }

    @Test
    public void doGetListTest() throws JsonProcessingException {
        // excel参数取得
        Properties requestMap = getRequestProps();
        String requestData = requestMap.getProperty("requestData");
        ObjectMapper objectMapper = new ObjectMapper();
        MessageLanguageRequest messageLanguageRequest = objectMapper.readValue(requestData, MessageLanguageRequest.class);

        // 执行Controller
        ListResponse<MessageResource> response = messageController.getList(messageLanguageRequest);

        // excel想定结果取得
        Properties responseMap = getResponseProps();

        // statusCode判定
        int statusCode = Integer.parseInt(responseMap.getProperty("responseCode"));
        assertEquals(response.getResponseCode(), statusCode);

        // responsePayload结果判定
        String responseData = responseMap.getProperty("responsePayload");
        ArrayList list = objectMapper.readValue(responseData, new TypeReference<ArrayList>() {});
        boolean compareResult = CompareUtil.compareLists(response.getResponsePayload(), list);
        assertTrue(compareResult, "responsePayload結果一致しない。");
    }
}
