package com.gchc.ing.question.common;


/**
 * Created by Administrator on 2015-05-11.
 */
public enum EServerAPI {
    // protocol ,domain, port, api_path, method, token 필수여부

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Common API                                                   //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 기본정보
    API_GET(EProtocol.HTTP, Defined.BASE_URL, Defined.BASE_PORT, "", EHttpMethod.GET),
    API_POST(EProtocol.HTTP, Defined.BASE_URL, Defined.BASE_PORT, "", EHttpMethod.POST),
    API_FILE_UPLOAD(EProtocol.HTTP, Defined.BASE_URL, Defined.BASE_PORT, "", EHttpMethod.MULTIPART);
//    API_SETTINGINFO(EProtocol.HTTP, Defined.BASE_URL, Defined.BASE_PORT, "/api/settinginfo", EHttpMethod.GET),
//    API_CARSOTRY(EProtocol.HTTP, Defined.BASE_URL, Defined.BASE_PORT, "/api/carstory", EHttpMethod.POST),



    private EProtocol m_eProtocol;
    private String m_sDomain;
    private String m_sPort;
    private String m_sPath;
    private EHttpMethod m_oMethod;

    private EServerAPI(EProtocol a_eProtocol, String a_strDomain, String a_strPort, String a_strPath, EHttpMethod a_oMethod) {
        m_eProtocol = a_eProtocol;
        m_sDomain = a_strDomain;
        m_sPort = a_strPort;
        m_sPath = a_strPath;
        m_oMethod = a_oMethod;
    }

    public EHttpMethod getMethod() {
        return m_oMethod;
    }

    public EProtocol getProtocol() {
        return m_eProtocol;
    }

    public String getDomain() {
        return m_sDomain;
    }

    public String getPort() {
        return m_sPort;
    }

    public String getPath() {
        return m_sPath;
    }

    public String getFullUrl() {
        return m_sDomain + m_sPort + m_sPath;
    }
}
