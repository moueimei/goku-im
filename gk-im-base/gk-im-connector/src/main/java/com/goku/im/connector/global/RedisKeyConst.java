package com.goku.im.connector.global;

/**
 * Created by milo on 15/11/26.
 */
public class RedisKeyConst
{
    /***************************** register redis start *****************************/
    /**
     * 用户对应的connector IP(客户端连接connector时保存)
     * 结构: String
     * 示例: set im_user_connector_{$user_id} {$connector_ip}
     */
    public final static String USER_CONNECTOR_KEY_PREFIX = "im_user_connector_";

    /**
     * 用户登录token(登录验证通过后保存)
     * 结构: String
     * 示例: set im_user_token_{$token} user_id
     */
    public final static String USER_TOKEN_KEY_PREFIX = "im_user_token_";

    /***************************** register redis end *****************************/

    /***************************** msg & push queue start *****************************/
    /**
     * 负责存储到数据库的消息队列(负责存储聊天消息)
     * 结构: List
     * 示例: lpush im_db_msg_queue {$message}
     */
    public final static String DATABASE_MESSAGE_QUEUE_KEY = "im_db_msg_queue";

    /**
     * 负责存储到数据库的通知队列(负责存储通知消息)
     * 结构: List
     * 示例: lpush im_db_notify_queue {$notify}
     */
    public final static String DATABASE_NOTIFY_QUEUE_KEY = "im_db_notify_queue";

    /**
     * 推送消息队列(负责存储connector将要推送的消息, 每个connector拥有自己的队列(一个或多个))
     * 结构: List
     * 示例: lpush im_push_msg_queue_{$connector_ip} {$message}
     */
    public final static String CONNECTOR_PUSH_MESSAGE_QUEUE_KEY_PREFIX = "im_push_msg_queue_";

    /**
     * 推送通知队列(负责存储connector将要推送的通知, 每个connector拥有自己的队列(一个或多个))
     * 结构: List
     * 示例: lpush im_push_notify_queue_{$connector_ip} {$notify}
     */
    public final static String CONNECTOR_PUSH_NOTIFY_QUEUE_KEY_PREFIX = "im_push_notify_queue_";

    /**
     * logic接收消息队列(logic集群从该队列中读取待处理的消息,然后负责推送至connector对应的 im_push_msg_queue中)
     * 结构: List
     * 示例: lpush im_receive_message_queue
     */
    public final static String LOGIC_RECEIVE_MESSAGE_QUEUE_KEY = "im_receive_msg_queue";

    /**
     * logic接收通知队列(logic集群从该队列中读取待处理的通知,然后负责推送至connector对应的 im_push_notify_queue中)
     * 结构: List
     * 示例: lpush im_receive_notify_queue
     */
    public final static String LOGIC_RECEIVE_NOTIFY_QUEUE_KEY = "im_receive_notify_queue";

    /***************************** msg & push queue end *****************************/

    /***************************** redis cache start *****************************/

    /**
     * 离线私聊消息列表
     * 结构:  ZSet
     * 示例: zadd im_offline_message_list_{$to_user_id} {$create_time} {$message_id}
     */
    public final static String OFFLINE_PRIVATE_MESSAGE_LIST_KEY_PREFIX = "im_offline_private_msg_list_";

    /**
     * 离线群聊消息列表
     * 结构:  ZSet
     * 示例: zadd im_offline_group_msg_list_{$to_user_id} {$create_time} {$message_id}
     */
    public final static String OFFLINE_GROUP_MESSAGE_LIST_KEY_PREFIX = "im_offline_group_msg_list_";

    /**
     * 离线通知列表
     * 结构:  ZSet
     * 示例: zadd im_offline_notify_list_{$to_user_id} {$create_time} {$notify_id}
     */
    public final static String OFFLINE_NOTIFY_LIST_KEY_PREFIX = "im_offline_act_notify_list_";

    /**
     * 用户信息缓存
     * 结构: String
     * 示例: get im_user_info_{$user_id}
     */
    public final static String USER_INFO_KEY_PREFIX = "im_user_info_";

    /***************************** redis cache end *****************************/

    /**
     * 构建Redis Key
     * @param prefix  前缀
     * @param parameter 参数
     * @return
     */
    public static String makeKey(String prefix, Object parameter)
    {
        return prefix.concat(parameter.toString());
    }
}