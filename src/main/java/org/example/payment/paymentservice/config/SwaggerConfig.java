package org.example.payment.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {
    // Bean để cấu hình và tạo Docket cho Swagger
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) // Sử dụng phiên bản Swagger 2
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.example.payment")) // Chọn các API trong gói org.example.payment
                .paths(PathSelectors.regex("/.*")) // Áp dụng cho tất cả các đường dẫn
                .build()
                .apiInfo(apiInfoMetaData())// Thêm thông tin API
                .securitySchemes(Arrays.asList(apiKey()));// Chỉ định sử dụng JWT cho xác thực:
    }

    // Phương thức để tạo thông tin API
    private ApiInfo apiInfoMetaData() {
        return new ApiInfoBuilder()
                .title("NAME OF SERVICE") // Tiêu đề của API
                .description("API Endpoint Decoration") // Mô tả của API
                .contact(new Contact("Dev-Team", "https://www.dev-team.com/", "dev-team@gmail.com")) // Thông tin liên hệ
                .license("Apache 2.0") // Giấy phép
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html") // URL giấy phép
                .version("1.0.0") // Phiên bản
                .build();
    }
    // Tạo một đối tượng ApiKey để xác thực JWT
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }
}