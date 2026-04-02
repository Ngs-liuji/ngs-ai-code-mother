package com.ngsliuji.ngsaicodemother.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ngsliuji.ngsaicodemother.constant.AppConstant;
import com.ngsliuji.ngsaicodemother.constant.UserConstant;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.exception.ThrowUtils;
import com.ngsliuji.ngsaicodemother.mapper.ChatHistoryMapper;
import com.ngsliuji.ngsaicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.ngsliuji.ngsaicodemother.model.entity.App;
import com.ngsliuji.ngsaicodemother.model.entity.ChatHistory;
import com.ngsliuji.ngsaicodemother.model.entity.User;
import com.ngsliuji.ngsaicodemother.model.enums.ChatHistoryMessageTypeEnum;
import com.ngsliuji.ngsaicodemother.service.AppService;
import com.ngsliuji.ngsaicodemother.service.ChatHistoryService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ngs_liuji
 */
@Service
@Slf4j
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    @Resource
    @Lazy
    private AppService appService;

    // 单条消息最大长度限制（字节），留一些余量
    private static final int MAX_MESSAGE_LENGTH = 15000;

    @Override
    public boolean addChatMessage(Long appId, String message, String messageType, Long userId) {
        // 基础校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "消息内容不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(messageType), ErrorCode.PARAMS_ERROR, "消息类型不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户 ID 不能为空");
        // 验证应用是否存在
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 验证消息类型是否有效
        ChatHistoryMessageTypeEnum messageTypeEnum = ChatHistoryMessageTypeEnum.getEnumByValue(messageType);
        ThrowUtils.throwIf(messageTypeEnum == null, ErrorCode.PARAMS_ERROR, "不支持的消息类型");
        
        // 判断是否需要分段存储
        if (message.length() > MAX_MESSAGE_LENGTH) {
            return saveSegmentedMessage(appId, message, messageType, userId);
        } else {
            // 单条存储
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setAppId(appId);
            chatHistory.setMessage(message);
            chatHistory.setMessageType(messageType);
            chatHistory.setUserId(userId);
            chatHistory.setSegmentIndex(0);
            chatHistory.setTotalSegments(1);
            return this.save(chatHistory);
        }
    }

    /**
     * 分段保存消息
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSegmentedMessage(Long appId, String message, String messageType, Long userId) {
        try {
            List<ChatHistory> segments = new ArrayList<>();
            int length = message.length();
            int totalSegments = (int) Math.ceil((double) length / MAX_MESSAGE_LENGTH);
            
            for (int i = 0; i < totalSegments; i++) {
                int start = i * MAX_MESSAGE_LENGTH;
                int end = Math.min(start + MAX_MESSAGE_LENGTH, length);
                String segmentContent = message.substring(start, end);
                
                ChatHistory segment = new ChatHistory();
                segment.setAppId(appId);
                segment.setMessage(segmentContent);
                segment.setMessageType(messageType);
                segment.setUserId(userId);
                segment.setSegmentIndex(i);
                segment.setTotalSegments(totalSegments);
                segments.add(segment);
            }
            
            boolean result = this.saveBatch(segments);
            log.info("成功分段保存 AI 消息到对话历史，appId: {}, 总分段数：{}", appId, totalSegments);
            return result;
        } catch (Exception e) {
            log.error("分段保存 AI 消息失败，appId: {}, error: {}", appId, e.getMessage(), e);
            throw new RuntimeException("分段保存失败", e);
        }
    }

    @Override
    public boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        // 使用 LambdaQueryWrapper 避免硬编码列名，类型更安全
        LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistory::getAppId, appId);
        return this.remove(queryWrapper);
    }

    @Override
    public Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                                      LocalDateTime lastCreateTime,
                                                      User loginUser) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(pageSize <= 0 || pageSize > 50, ErrorCode.PARAMS_ERROR, "页面大小必须在1-50之间");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // 验证权限：只有应用创建者和管理员可以查看
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        boolean isCreator = app.getUserId().equals(loginUser.getId());
        ThrowUtils.throwIf(!isAdmin && !isCreator, ErrorCode.NO_AUTH_ERROR, "无权查看该应用的对话历史");
        // 构建查询条件
        ChatHistoryQueryRequest queryRequest = new ChatHistoryQueryRequest();
        queryRequest.setAppId(appId);
        queryRequest.setLastCreateTime(lastCreateTime);
        QueryWrapper<ChatHistory> queryWrapper = this.getQueryWrapper(queryRequest);
        // 查询数据
        return this.page(Page.of(1, pageSize), queryWrapper);
    }


    @Override
    public int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount) {
        try {
            // 使用 LambdaQueryWrapper 避免硬编码列名，类型更安全
            LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ChatHistory::getAppId, appId)
                    .orderByDesc(ChatHistory::getCreateTime)
                    .last("LIMIT " + maxCount);
            List<ChatHistory> historyList = this.list(queryWrapper);
            if (CollUtil.isEmpty(historyList)) {
                return 0;
            }
            // 反转列表，确保按时间正序（老的在前，新的在后）
            historyList = historyList.reversed();
            // 按时间顺序添加到记忆中
            int loadedCount = 0;
            // 先清理历史缓存，防止重复加载
            chatMemory.clear();
            
            // 合并分段消息
            List<ChatHistory> mergedHistoryList = mergeSegmentedMessages(historyList);
            
            for (ChatHistory history : mergedHistoryList) {
                if (ChatHistoryMessageTypeEnum.USER.getValue().equals(history.getMessageType())) {
                    chatMemory.add(UserMessage.from(history.getMessage()));
                    loadedCount++;
                } else if (ChatHistoryMessageTypeEnum.AI.getValue().equals(history.getMessageType())) {
                    chatMemory.add(AiMessage.from(history.getMessage()));
                    loadedCount++;
                }
            }
            log.info("成功为 appId: {} 加载了 {} 条历史对话", appId, loadedCount);
            return loadedCount;
        } catch (Exception e) {
            log.error("加载历史对话失败，appId: {}, error: {}", appId, e.getMessage(), e);
            // 加载失败不影响系统运行，只是没有历史上下文
            return 0;
        }
    }

    /**
     * 合并分段消息
     * @param historyList 已按 createTime 排序的历史记录
     * @return 合并后的历史记录列表
     */
    private List<ChatHistory> mergeSegmentedMessages(List<ChatHistory> historyList) {
        List<ChatHistory> mergedList = new ArrayList<>();
        int i = 0;
        
        while (i < historyList.size()) {
            ChatHistory current = historyList.get(i);
            
            // 如果是多段消息的第一段，则合并所有分段
            if (current.getTotalSegments() > 1 && current.getSegmentIndex() == 0) {
                StringBuilder mergedMessage = new StringBuilder(current.getMessage());
                
                // 查找并合并后续分段
                int j = i + 1;
                while (j < historyList.size() && 
                       j < i + current.getTotalSegments() &&
                       historyList.get(j).getSegmentIndex() == (j - i)) {
                    mergedMessage.append(historyList.get(j).getMessage());
                    j++;
                }
                
                // 创建合并后的 ChatHistory 对象
                ChatHistory merged = new ChatHistory();
                merged.setId(current.getId());
                merged.setMessage(mergedMessage.toString());
                merged.setMessageType(current.getMessageType());
                merged.setAppId(current.getAppId());
                merged.setUserId(current.getUserId());
                merged.setSegmentIndex(0);
                merged.setTotalSegments(1);
                merged.setCreateTime(current.getCreateTime());
                merged.setUpdateTime(current.getUpdateTime());
                
                mergedList.add(merged);
                i = j; // 跳过已合并的分段
            } else {
                // 单段消息直接添加
                mergedList.add(current);
                i++;
            }
        }
        
        return mergedList;
    }

    /**
     * 获取查询包装类
     *
     */
    @Override
    public QueryWrapper<ChatHistory> getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest) {
        QueryWrapper<ChatHistory> queryWrapper = new QueryWrapper<>();
        if (chatHistoryQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chatHistoryQueryRequest.getId();
        String message = chatHistoryQueryRequest.getMessage();
        String messageType = chatHistoryQueryRequest.getMessageType();
        Long appId = chatHistoryQueryRequest.getAppId();
        Long userId = chatHistoryQueryRequest.getUserId();
        LocalDateTime lastCreateTime = chatHistoryQueryRequest.getLastCreateTime();
        String sortField = chatHistoryQueryRequest.getSortField();
        String sortOrder = chatHistoryQueryRequest.getSortOrder();
        // 拼接查询条件 - 使用驼峰命名，MyBatis-Plus 会自动转换为下划线列名
        queryWrapper
                .eq(id != null, "appId", appId)
                .eq(StrUtil.isNotBlank(messageType), "messageType", messageType)
                .like(StrUtil.isNotBlank(message), "message", message)
                .eq(appId != null, "appId", appId)
                .eq(userId != null, "userId", userId);
        // 游标查询逻辑 - 只使用 createTime 作为游标
        if (lastCreateTime != null) {
            queryWrapper.lt("createTime", lastCreateTime);
        }
        // 排序
        if (StrUtil.isNotBlank(sortField)) {
            queryWrapper.orderBy(true, "ascend".equals(sortOrder), sortField);
        } else {
            // 默认按创建时间降序排列
            queryWrapper.orderByDesc("createTime");
        }
        return queryWrapper;
    }
}






