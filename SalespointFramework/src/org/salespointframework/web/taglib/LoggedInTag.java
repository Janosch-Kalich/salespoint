package org.salespointframework.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserManager;

/**
 * 
 * @author Lars Kreisz
 * @author Uwe Schmidt
 * @author Paul Henke
 */
@SuppressWarnings("serial")
public class LoggedInTag extends BodyTagSupport
{
	private boolean test = true;

	/**
	 * The test condition that determines whether or not the body content should be processed.
	 * @param test a boolean condition
	 */
	public void setTest(boolean test)
	{
		this.test = test;
	}

	@Override
	public int doStartTag() throws JspException
	{

		@SuppressWarnings("unchecked")
		UserManager<User> usermanager = (UserManager<User>) Shop.INSTANCE.getUserManager();
		
		if(usermanager == null) {
			throw new RuntimeException("Shop.INSTANCE.getUserManager() returned null");
		}
		
		
		//UserManager<? extends User> usermanager = Shop.INSTANCE.getUserManager();
		User user = usermanager.getUserByToken(User.class, pageContext.getSession());

		if (test)
		{
			return user == null ? SKIP_BODY : EVAL_BODY_INCLUDE;
		} else
		{
			return user == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
		}
	}

}
