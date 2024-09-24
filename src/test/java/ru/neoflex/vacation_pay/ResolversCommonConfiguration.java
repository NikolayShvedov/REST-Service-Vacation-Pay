package ru.neoflex.vacation_pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VacationPayApplication.class)
@AutoConfigureMockMvc
public class ResolversCommonConfiguration {

    protected final static String VACATION_PAY_API = "/calculacte";

    protected static final BigDecimal TEST_AVERAGE_SALARY = new BigDecimal("30500.00");

    protected static final Integer TEST_VACATION_DAYS = 30;

    protected static final String TEST_START_VACATION_DAY = "2024-02-25";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
