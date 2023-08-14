package io.llsfish.ratelimiter.banner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Redick01
 */
@Slf4j
public class RateLimiterBanner implements InitializingBean {

    private static final String LIMITER = " :: Limiter ::   :: User-->LLS :: ";

    @Override
    public void afterPropertiesSet() throws Exception {
        String banner = readBannerFromFile();
        log.info(AnsiOutput.toString(banner, "\n", AnsiColor.GREEN, LIMITER,
                AnsiColor.DEFAULT, AnsiStyle.FAINT));
    }

    private String readBannerFromFile() {
        try {
            InputStream in = getClass().getResourceAsStream("/lmtbanner.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder banner = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                banner.append(line).append("\n");
            }
            return banner.toString();
        } catch (IOException e) {
            log.error("Failed to read the banner file", e);
            return "";
        }
    }
}
