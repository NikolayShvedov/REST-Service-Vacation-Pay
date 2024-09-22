package ru.neoflex.vacation_pay.test;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neoflex.vacation_pay.ResolversCommonConfiguration;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VacationPayApplicationTests extends ResolversCommonConfiguration {

    @Test
    void calculationOfVacationPayForEmployeeWithInvalidSalaryTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalary", "0")
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Average salary must be greater than 0"));
    }

    @Test
    void calculationOfVacationPayForEmployeeWithInvalidVacationDaysTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalary", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", "0")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vacation days must be greater than 0"));
    }

    @Test
    void calculationOfVacationPayForEmployeeWithInvalidDateTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalary", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .param("startVacationDate", "invalid-date")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid date format"));
    }

    @Test
    void calculationOfVacationPayForEmployeeWithoutRequiredSalaryTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required request parameter 'averageSalary' is not present"));
    }

    @Test
    void calculationOfVacationPayForEmployeeUsingSimpleQueryTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalary", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(BigDecimal.valueOf(27168.80).stripTrailingZeros()));
    }

    @Test
    void calculationOfVacationPayForEmployeeUsingQueryWithDateTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalary", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .param("startVacationDate", TEST_START_VACATION_DAY)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(BigDecimal.valueOf(18113.20).stripTrailingZeros()));
    }
}
