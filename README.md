# home

========================velocity 过滤html标签=======================

<!-- Velocity 模版配置  -->
	<bean id="velocityConfigurer" class="org.springframework.web.servlet.view.velocity.VelocityConfigurer">
	    <property name="resourceLoaderPath" value="/WEB-INF/views/" />
	    <property name="velocityProperties">
		    <props>
		        <prop key="input.encoding">UTF-8</prop>
		        <prop key="output.encoding">UTF-8</prop>
		         <prop key="eventhandler.referenceinsertion.class">com.cndw.web.util.WebEscapeHtmlReference</prop>  
		    </props>
	    </property>
	</bean>

test.html
<html>
<body>

    $name

</body>

</html>

jar包
oro-2.0.8.jar

TestController
@Controller
public class TestController {
	@RequestMapping(value="/test",method = RequestMethod.GET)
	public String log(HttpServletRequest request, HttpServletResponse response,Model model){
		model.addAttribute("name", "<html>你号</html>");
		return "test";
	}
}


public class WebEscapeHtmlReference extends EscapeHtmlReference {  
	  
    @Override  
    protected String escape(Object text) {  
        return escapeHtml(text);  
    }  
  
    private static String escapeHtml(Object value) {  
        if (value == null)  
            return null;  
  
        if (value instanceof String) {  
            String result = value.toString();
            result = result.replaceAll("&", "&amp;").replaceAll(">", "&gt;")  
                    .replaceAll("<", "&lt;").replaceAll("\"", "&quot;");  
            return result;  
        } else {  
            return value.toString();  
        }  
    }  
}  

=======================velocity 过滤html标签=======================




======================CSRF攻击=======================
======================CSRF攻击=======================

======================SQL攻击=======================
======================SQL攻击=======================


