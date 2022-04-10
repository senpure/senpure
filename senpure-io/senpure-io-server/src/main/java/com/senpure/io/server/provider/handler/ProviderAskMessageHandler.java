package com.senpure.io.server.provider.handler;


import com.senpure.io.protocol.Message;


public interface ProviderAskMessageHandler<T extends Message> extends ProviderMessageHandler<T> {


    Answer ask(T message);

    class Answer {

        private boolean handle;
        private String value;

        public boolean isHandle() {
            return handle;
        }

        public void setHandle(boolean handle) {
            this.handle = handle;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
