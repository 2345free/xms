package com.xiao.xms.controller;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final SpringTemplateEngine templateEngine;

    @GetMapping("/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException, InterruptedException {
        Map<String, Object> vars = new HashMap<>();
        vars.put("id", UUID.randomUUID().toString().replace("-", ""));
        vars.put("ordId", System.currentTimeMillis());
        vars.put("apiType", "淘宝物流");
        vars.put("providerSystem", "淘宝");
        vars.put("clientSystem", "xms");
        vars.put("trackingNo", "SF12345678");
        // 物流公司中文名称
        vars.put("b5cLogisticsCd", "顺丰物流");
        vars.put("createTime", LocalDateTime.now());
        vars.put("errMsg", "系统内部异常");

        Context context = new Context();
        context.setVariables(vars);
        String html = templateEngine.process("test/elec-err-report", context);

        Pdf pdf = new Pdf();
        pdf.addPageFromString(html);
        IOUtils.copy(new ByteArrayInputStream(pdf.getPDF()), response.getOutputStream());

        response.setContentType("application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "接口错误报告.pdf");
    }
}
