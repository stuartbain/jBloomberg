/*
 * Copyright (C) 2012 - present by Yann Le Tallec.
 * Please see distribution for license.
 */
package com.assylias.jbloomberg;

import com.bloomberglp.blpapi.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 *
 */
abstract class AbstractRequestBuilder<T extends RequestResult> implements RequestBuilder<T> {

    final static DateTimeFormatter BB_REQUEST_DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE; //'20111203'
    final static DateTimeFormatter BB_REQUEST_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME; //'2011-12-03T10:15:30'

    @Override
    public Request buildRequest(Session session) {
        Service service = session.getService(getServiceType().getUri());
        Request request = service.createRequest(getRequestType().toString());
        buildRequest(request);
        return request;
    }

    static void addCollectionToElement(Request request, Iterable<String> collection, String elementName) {
        Element element = request.getElement(Name.getName(elementName));
        for (String item : collection) {
            element.appendValue(item);
        }
    }

    static void addOverrides(Request request, Map<String, String> overrides) {
        Element overridesElt = request.getElement(Name.getName("overrides"));
        for (Map.Entry<String, String> e : overrides.entrySet()) {
            Element override = overridesElt.appendElement();
            override.setElement( Name.getName("fieldId"), e.getKey());
            override.setElement(Name.getName("value"), e.getValue());
        }
    }

    /**
     *
     * @param request an empty Request that needs to be populated
     */
    protected abstract void buildRequest(Request request);
}
