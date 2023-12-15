package com.ehrms.deptenq.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *<p>{@link CustomErrorController} class implements {@link ErrorController} th</p> 
 *The return value from this method is not used; the property `server.error.path`
 * must be set to override the default error page path.
 * @return the error path
 * @deprecated since 2.3.0 in favor of setting the property `server.error.path`
 * @author khalid 
*/

@Controller
@ControllerAdvice
public class CustomErrorController implements ErrorController{
	
	/**
	 * @param httpRequest
	 * @param model
	 * @param res
	 * <p> {@link CustomErrorController} return the {@link httpErrorCode} cheak case in </P>
	 * @return errorx.html page 
	 * @since 2021
	 * @see 
	 * @author Avadhesh
	 */
	
//	  @RequestMapping(value = "error", method = RequestMethod.GET)
	@ExceptionHandler(value = Exception.class)
		@GetMapping(value = "error")
	    public String renderErrorPage(HttpServletRequest httpRequest,Model model,HttpServletResponse res) {
	        
//	        ModelAndView errorPage = new ModelAndView("errorPage");
	        String errorMsg = "";
	        int httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	        
	        switch (httpErrorCode) {
	            case 400: {
	                errorMsg = "Http Error Code: 400. Bad Request";
	                break;
	            }
	            case 401: {
	                errorMsg = "Http Error Code: 401. Unauthorized";
	                break;
	            }
	            case 403: {
	    	        return "errorcode/permissiondenied";
//	                break;
	            }
	            case 404: {
//	                errorMsg = "Http Error Code: 404. Resource not found";
//	                break;
	            	return "errorcode/pagenotfound";
	            }
	            case 405: {
//	                errorMsg = "Http Error Code: 500. Internal Server Error";
//	                break;
	            	return "errorcode/500";
	            }
	            default: {
//	            	errorMsg = "Something went wrong";
	            	return "errorcode/500";
	            }
	        }
	        model.addAttribute("errorMsg", errorMsg);
	        return "errorx";
	    }
	  
	  @ExceptionHandler(value = UsernameNotFoundException.class)
	    public String handle(final UsernameNotFoundException exception,Model model) {
		  model.addAttribute("errorMsg", "User not found or Session Timeout");
			return "errorx";
//	        ...//set headers, response attributes and response body
	    }
	  
//	  @ExceptionHandler(value = NestedServletException.class)
//	    public String handle(final NestedServletException exception,Model model) {
////		  model.addAttribute("errorMsg", "User not found or Session Timeout");
////			return "errorx";
////	        ...//set headers, response attributes and response body
//		  return "errorcode/500";
//	    }
	  
//	  @ExceptionHandler(value = IllegalArgumentException.class)
//	    public String handle(final IllegalArgumentException exception,Model model) {
////		  model.addAttribute("errorMsg", "User not found or Session Timeout");
////			return "errorx";
////	        ...//set headers, response attributes and response body
//		  return "errorcode/500";
//	    }
	  
	  @ExceptionHandler(value = SessionAuthenticationException.class)
	    public String handle(final SessionAuthenticationException exception,Model model) {
		  model.addAttribute("errorMsg", exception.getMessage());
			return "errorx";
//	        ...//set headers, response attributes and response body
	    }
//	  
//	  	private int getErrorCode(HttpServletRequest httpRequest) {
//	        return (Integer) httpRequest
//	          .getAttribute("javax.servlet.error.status_code");
//	    }
//
//		@Override
//		public String getErrorPath() {
//			return "/errors";
//		}




	
//	@RequestMapping("/error")
//	  @ResponseBody
//	  public String handleError(HttpServletRequest request) {
//	      Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//	      Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
//	      return String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
//	                      + "<div>Exception Message: <b>%s</b></div><body></html>",
//	              statusCode, exception==null? "N/A": exception.getMessage());
//	  }

//	  @Override
//	  public String getErrorPath() {
//	      return "/error";
//	  }

}
