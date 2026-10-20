package com.college.sms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDashboardPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("totalStudents"))
                .andExpect(model().attributeExists("averageMarks"));
    }

    @Test
    void testStudentsListPageLoads() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    void testStudentsSearch() throws Exception {
        mockMvc.perform(get("/students").param("searchType", "name").param("query", "Alice"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("isSearch", true));
    }

    @Test
    void testAddStudentFormLoads() throws Exception {
        mockMvc.perform(get("/students/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-student"))
                .andExpect(model().attributeExists("studentForm"));
    }

    @Test
    void testAddStudentSubmissionAndValidation() throws Exception {
        // Valid submission
        mockMvc.perform(post("/students/add")
                        .param("id", "888")
                        .param("name", "Test Student")
                        .param("age", "22")
                        .param("course", "Robotics")
                        .param("marks", "89.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attributeExists("successMessage"));

        // Duplicate submission should fail gracefully with an error
        mockMvc.perform(post("/students/add")
                        .param("id", "888")
                        .param("name", "Duplicate Student")
                        .param("age", "22")
                        .param("course", "Robotics")
                        .param("marks", "89.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-student"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void testClassReportPageLoads() throws Exception {
        mockMvc.perform(get("/report"))
                .andExpect(status().isOk())
                .andExpect(view().name("report"))
                .andExpect(model().attributeExists("totalStudents"))
                .andExpect(model().attributeExists("averageMarks"))
                .andExpect(model().attributeExists("passedCount"));
    }

    @Test
    void testApiEndpoints() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));

        mockMvc.perform(get("/api/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalStudents", notNullValue()));
    }
}
