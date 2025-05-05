package com.lime.server.busApi.error;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "OpenAPI_ServiceResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusAPIErrorResponse {
    @XmlElement(name = "cmmMsgHeader")
    private CmmMsgHeader cmmMsgHeader;

    @Override
    public String toString() {
        return "OpenAPIServiceResponse{" +
                "cmmMsgHeader=" + cmmMsgHeader +
                '}';
    }

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CmmMsgHeader {
        @XmlElement(name = "errMsg")
        private String errMsg;

        @XmlElement(name = "returnAuthMsg")
        private String returnAuthMsg;

        @XmlElement(name = "returnReasonCode")
        private String returnReasonCode;

        @Override
        public String toString() {
            return "CmmMsgHeader{" +
                    "errMsg='" + errMsg + '\'' +
                    ", returnAuthMsg='" + returnAuthMsg + '\'' +
                    ", returnReasonCode='" + returnReasonCode + '\'' +
                    '}';
        }
    }
}

