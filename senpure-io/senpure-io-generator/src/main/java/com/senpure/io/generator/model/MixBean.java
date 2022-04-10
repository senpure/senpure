package com.senpure.io.generator.model;

import com.senpure.template.sovereignty.TemplateBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MixBean
 *
 * @author senpure
 * @time 2019-09-23 11:47:23
 */
public abstract class MixBean extends TemplateBean {

    private List<Bean> beans = new ArrayList();
    private List<Enum> enums = new ArrayList();
    private List<Message> messages = new ArrayList();
    private List<Event> events = new ArrayList();
    private int namespaceMaxLen = 0;
    private int namespaceAndNameMaxLen = 0;
    private int nameMaxLen = 0;
    private int messageIdMaxLen = 0;
    private int eventIdMaxLen = 0;
    private int idMaxLen = 0;
    private Set<String> namespaces = new HashSet<>();

    public abstract Language getLanguage(Bean bean);

    public void merge(MixBean target) {
        enums.addAll(target.enums);
        beans.addAll(target.beans);
        messages.addAll(target.messages);
        events.addAll(target.events);
    }

    public void compute() {
        compute(enums);
        compute(beans);
        compute(messages);
        computeMessageId(messages);
        computeEventId(events);
        idMaxLen = messageIdMaxLen > eventIdMaxLen ? messageIdMaxLen : eventIdMaxLen;
    }

    private void compute(List<? extends Bean> beans) {
        for (Bean bean : beans) {
            Language language = getLanguage(bean);
            String namespace = language.getNamespace();
            String name = language.getName();
            String namespaceAndName = namespace + "." + name;
            int nameLen = name.length();
            nameMaxLen = nameLen > nameMaxLen ? nameLen : nameMaxLen;
            int namespaceAndNameLen = namespaceAndName.length();
            namespaceAndNameMaxLen = namespaceAndNameLen > namespaceAndNameMaxLen ? namespaceAndNameLen : namespaceAndNameMaxLen;
            if (namespaces.add(language.getNamespace())) {
                int namespaceLen = namespace.length();
                namespaceMaxLen = namespaceLen > namespaceMaxLen ? namespaceLen : namespaceMaxLen;
            }
        }
    }

    private void computeMessageId(List<Message> beans) {
        for (Message bean : beans) {
            int len = (bean.getId() + "").length();
            messageIdMaxLen = len > messageIdMaxLen ? len : messageIdMaxLen;
        }
    }

    private void computeEventId(List<Event> beans) {
        for (Event bean : beans) {
            int len = (bean.getId() + "").length();
            messageIdMaxLen = len > messageIdMaxLen ? len : messageIdMaxLen;
        }
    }

    public int getEventIdMaxLen() {
        return eventIdMaxLen;
    }

    public void setEventIdMaxLen(int eventIdMaxLen) {
        this.eventIdMaxLen = eventIdMaxLen;
    }

    public int getIdMaxLen() {
        return idMaxLen;
    }

    public void setIdMaxLen(int idMaxLen) {
        this.idMaxLen = idMaxLen;
    }

    public int getNamespaceMaxLen() {
        return namespaceMaxLen;
    }

    public void setNamespaceMaxLen(int namespaceMaxLen) {
        this.namespaceMaxLen = namespaceMaxLen;
    }

    public int getNamespaceAndNameMaxLen() {
        return namespaceAndNameMaxLen;
    }

    public void setNamespaceAndNameMaxLen(int namespaceAndNameMaxLen) {
        this.namespaceAndNameMaxLen = namespaceAndNameMaxLen;
    }

    public int getNameMaxLen() {
        return nameMaxLen;
    }

    public void setNameMaxLen(int nameMaxLen) {
        this.nameMaxLen = nameMaxLen;
    }

    public int getMessageIdMaxLen() {
        return messageIdMaxLen;
    }

    public void setMessageIdMaxLen(int messageIdMaxLen) {
        this.messageIdMaxLen = messageIdMaxLen;
    }

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    public List<Enum> getEnums() {
        return enums;
    }

    public void setEnums(List<Enum> enums) {
        this.enums = enums;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Set<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(Set<String> namespaces) {
        this.namespaces = namespaces;
    }
}
