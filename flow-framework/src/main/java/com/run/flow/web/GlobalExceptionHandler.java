package com.run.flow.web;

import com.run.flow.common.CommonResult;
import com.run.flow.constant.HttpStatus;
import com.run.flow.exception.ServiceException;
import com.run.flow.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 权限校验异常
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public CommonResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
		return CommonResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
	}

	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public CommonResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
															HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
		return CommonResult.error(e.getMessage());
	}

	/**
	 * 业务异常
	 */
	@ExceptionHandler(ServiceException.class)
	public CommonResult handleServiceException(ServiceException e, HttpServletRequest request) {
		log.error(e.getMessage(), e);
		Integer code = e.getCode();
		return StringUtils.isNotNull(code) ? CommonResult.error(code, e.getMessage()) : CommonResult.error(e.getMessage());
	}

	/**
	 * 拦截未知的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	public CommonResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',发生未知异常.", requestURI, e);
		return CommonResult.error(e.getMessage());
	}

	/**
	 * 系统异常
	 */
	@ExceptionHandler(Exception.class)
	public CommonResult handleException(Exception e, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("请求地址'{}',发生系统异常.", requestURI, e);
		return CommonResult.error(e.getMessage());
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(BindException.class)
	public CommonResult handleBindException(BindException e) {
		log.error(e.getMessage(), e);
		String message = e.getAllErrors().get(0).getDefaultMessage();
		return CommonResult.error(message);
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		String message = e.getBindingResult().getFieldError().getDefaultMessage();
		return CommonResult.error(message);
	}

}
