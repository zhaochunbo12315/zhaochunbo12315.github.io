package com.cy.pj.common.exception;

/**
 * 自定义异常:目的是对错误信息进行更加清晰的描述
 *
 * @author qilei
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 4827965566190694838L;

    public ServiceException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ServiceException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ServiceException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }


}
