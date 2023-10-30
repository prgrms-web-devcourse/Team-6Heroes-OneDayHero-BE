package com.sixheroes.onedayheroapi.restdocs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@RestController
public class RestdocsController {

    @GetMapping(value = "/api/docs", produces = MediaType.TEXT_HTML_VALUE)
    public String docs() throws IOException {

        var resources = ResourcePatternUtils
                .getResourcePatternResolver(new DefaultResourceLoader())
                .getResources("classpath*:static/docs/**");

        var docs = "";

        for (Resource resource : resources) {
            docs = getDocsHtml(resource);
        }

        return docs;
    }

    private String getDocsHtml(Resource docsResource) throws IOException {
        var inputStream = docsResource.getInputStream();
        var br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        var builder = new StringBuilder();
        while (true) {
            var line = br.readLine();
            log.debug("docs 읽는 중..");
            if (line == null) break;
            builder.append(line);
        }
        log.debug("완료");

        return builder.toString();
    }
}
