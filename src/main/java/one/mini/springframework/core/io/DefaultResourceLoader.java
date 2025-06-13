package one.mini.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URI;

public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        else {
            try {
                return new UrlResource(URI.create(location).toURL());
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }
    }
}
