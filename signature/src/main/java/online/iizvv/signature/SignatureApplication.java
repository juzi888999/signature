package online.iizvv.signature;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author iizvv
 */
@EnableTransactionManagement
@ComponentScan(basePackages = {"online.iizvv.*"})
@MapperScan(basePackages = {"online.iizvv.dao"})
@ServletComponentScan
@SpringBootApplication
public class SignatureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignatureApplication.class, args);
    }

}
