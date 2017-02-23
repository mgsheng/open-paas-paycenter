package cn.com.open.pay.platform.manager.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 登录过滤
 * 
 * @author geloin
 * @date 2012-4-10 下午2:37:38
 */
public class SessionFilter extends OncePerRequestFilter {
	// 不过滤路径
	private String notFilter;

	private String serverUri;

	public void setNotFilter(String notFilter) {
		this.notFilter = notFilter;
	}

	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String[] notCheckUrl = null;
		if (this.notFilter != null) {
			notCheckUrl = notFilter.split(",");
		}
		// 不过滤的uri

		// 请求的uri
		String uri = request.getRequestURI();
		// 是否过滤
		boolean doFilter = true;
		if (uri.equals(this.serverUri)) {
			doFilter = false;
		} else {
			for (String s : notCheckUrl) {
				if (uri.indexOf(s) != -1) {
					// 如果uri中包含不过滤的uri，则不进行过滤
					doFilter = false;
					break;
				}
			}
		}

		if (doFilter) {
			// 从session中获取登录者实体
			Object obj = request.getSession().getAttribute("user");
			if (null == obj) {
				// 如果session中不存在登录者实体，则弹出框提示重新登录
				// 设置request和response的字符集，防止乱码
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				StringBuilder builder = new StringBuilder();
				if (request.getHeader("x-requested-with") != null) {
					response.setContentType("application/x-javascript;charset=utf-8");
					builder.append("alert('登陆超时！请重新登陆');");
					builder.append("window.top.location.href='");
					builder.append(request.getContextPath());
					builder.append("';");
				} else {
					builder.append("<script type=\"text/javascript\">");
					builder.append("alert('登陆超时！请重新登陆');");
					builder.append("window.top.location.href='");
					builder.append(request.getContextPath());
					builder.append("';");
					builder.append("</script>");
				}
				out.print(builder.toString());
			} else {
				// 如果session中存在登录者实体，则继续
				filterChain.doFilter(request, response);
			}
		} else {
			// 如果session中存在登录者实体，则继续
			filterChain.doFilter(request, response);
		}
	}

}