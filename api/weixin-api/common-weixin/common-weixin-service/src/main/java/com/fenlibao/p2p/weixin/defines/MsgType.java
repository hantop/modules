package com.fenlibao.p2p.weixin.defines;

/**
 * 事件消息
 * Created by Administrator on 2015/5/25.
 */
public enum MsgType {


    text {
        @Override
        public String toString() {
            return "text";//文本消息
        }
    },
    image {
        @Override
        public String toString() {
            return "image";//图片消息
        }
    },
    voice {
        @Override
        public String toString() {
            return "voice";//语音消息
        }
    },
    video {
        @Override
        public String toString() {
            return "video";//视频消息
        }
    },
    music {
        @Override
        public String toString() {
            return "music";//音乐消息
        }
    },
    shortvideo {
        @Override
        public String toString() {
            return "shortvideo";//小视频消息
        }
    },
    location {
        @Override
        public String toString() {
            return "location";//地理位置消息
        }
    },
    link {
        @Override
        public String toString() {
            return "link";///图片消息
        }
    },
    event {
        @Override
        public String toString() {
            return "event";//事件类型
        }
    },
    news {
        @Override
        public String toString() {
            return "news";//回复图文消息
        }
    },
    receive {
        @Override
        public String toString() {
            return "接收消息";
        }
    },
    send {
        @Override
        public String toString() {
            return "发送消息";
        }
    },
    transfer_customer_service {
        @Override
        public String toString() {
            return "消息转发到客服";
        }
    }


}
