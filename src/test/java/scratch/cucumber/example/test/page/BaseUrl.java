package scratch.cucumber.example.test.page;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class BaseUrl implements ApplicationContextAware {

    private ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = (ConfigurableApplicationContext) context;
    }

    @Override
    public String toString() {
        return format("http://localhost:%s/spring", context.getEnvironment().getProperty("local.server.port"));
    }
}
