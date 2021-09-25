package com.gchc.ing.question.common;

/**
 * W3C에 정의된 Http Method http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
 * 
 * @author jmkim
 */
public enum EHttpMethod
{
	/** 요청한 URL에 어떠한 메소드 요청이 가능한지 묻는다 */
	OPTIONS, 
	/** URL에 해당하는 정보의 전송 요청을 보낸다. */
	GET,
	/** URL에 해당하는 정보의 전송을 요청하지만, GET과는 다르게 정보의 Meta 정보만을 요청한다. */
	HEAD,
	/** 서버가 처리할 수 있는 자료를 보낸다. GET으로 보낼 수 없는 자료들에 대해 전송할 때 사용한다. */
	POST,
	/** 자료를 전송하여 해당 URL에 자료를 저장한다. */
	PUT,
	/** 해당 URL의 자원, 정보를 삭제한다. */
	DELETE,
	/** 이전까지 요청한 정보들의 목록을 요청한다. */
	TRACE,
	/** 프록시가 사용하고, 연결을 요청한다 */
	CONNECT,
	/** 멀티 파일 전송시 사용 */
	MULTIPART;
}
