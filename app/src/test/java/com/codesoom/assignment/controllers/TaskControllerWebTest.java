package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TaskService service;

    private final List<Task> tasks = new LinkedList<>();
    private final String TASK_TITLE = "Test Task";
    private final String TASK_TITLE_UPDATED = "Updated Task";
    private final Long TASK_ID = 1L;
    private final int FIRST = 0;
    private final Long TASK_ID_NOT_EXISTING = 10L;
    private final String DEFAULT_PATH = "/tasks";

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
        tasks.add(task);
    }

    @AfterEach
    void clear() {
        tasks.clear();
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {
        @BeforeEach
        void setUp() {
            given(service.getTasks()).willReturn(tasks);
        }

        @Test
        @DisplayName("HTTP Status Code 200 OK 응답한다")
        void it_responds_with_200_ok() throws Exception {
            mockMvc.perform(get(DEFAULT_PATH))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 상세조회한다면")
        class Context_with_default_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                given(service.getTask(TASK_ID)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP Status Code 200 OK 응답한다")
            void it_responds_with_200_ok() throws Exception {
                mockMvc.perform(get(DEFAULT_PATH + "/" + TASK_ID))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 상세조회한다면")
        class Context_without_existing_task {
            @BeforeEach
            void setUp() {
                given(service.getTask(TASK_ID_NOT_EXISTING))
                        .willThrow(new TaskNotFoundException(TASK_ID_NOT_EXISTING));
            }

            @Test
            @DisplayName("HTTP Status Code 404 NOT FOUND 응답한다")
            void it_responds_with_404() throws Exception {
                mockMvc.perform(get(DEFAULT_PATH + "/" + TASK_ID_NOT_EXISTING))
                        .andExpect(status().isNotFound());
            }

        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 수정한다면")
        class Context_with_default_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                final Task newTask = new Task();
                newTask.setTitle(TASK_TITLE_UPDATED);
                task.setTitle(TASK_TITLE_UPDATED);

                given(service.updateTask(TASK_ID, newTask)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP Status Code 200 OK 응답한다")
            void it_responds_with_200_ok() throws Exception {
                Task task = new Task();
                task.setTitle(TASK_TITLE_UPDATED);
                String content = objectMapper.writeValueAsString(task);

                mockMvc.perform(put(DEFAULT_PATH + "/" + TASK_ID)
                                .content(content))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 수정한다면")
        class Context_without_existing_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                given(service.updateTask(TASK_ID, task))
                        .willThrow(new TaskNotFoundException(TASK_ID_NOT_EXISTING));
            }

            @Test
            @DisplayName("HTTP Status Code 404 NOT FOUND 응답한다")
            void it_responds_with_404() throws Exception {
                mockMvc.perform(put(DEFAULT_PATH + "/" + TASK_ID_NOT_EXISTING))
                        .andExpect(status().isNotFound());
            }

        }
    }

    @Nested
    @DisplayName("patch 메소드는")
    class Describe_patch {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 부분 수정한다면")
        class Context_with_default_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                final Task newTask = new Task();
                newTask.setTitle(TASK_TITLE_UPDATED);
                task.setTitle(TASK_TITLE_UPDATED);

                given(service.updateTask(TASK_ID, newTask)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP Status Code 200 OK 응답한다")
            void it_responds_with_200_ok() throws Exception {
                Task task = new Task();
                task.setTitle(TASK_TITLE_UPDATED);
                String content = objectMapper.writeValueAsString(task);

                mockMvc.perform(patch(DEFAULT_PATH + "/" + TASK_ID)
                                .content(content))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 부분 수정한다면")
        class Context_without_existing_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                given(service.updateTask(TASK_ID, task))
                        .willThrow(new TaskNotFoundException(TASK_ID_NOT_EXISTING));
            }

            @Test
            @DisplayName("HTTP Status Code 404 NOT FOUND 응답한다")
            void it_responds_with_404() throws Exception {
                mockMvc.perform(patch(DEFAULT_PATH + "/" + TASK_ID_NOT_EXISTING))
                        .andExpect(status().isNotFound());
            }

        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 기본 생성된 Task를 삭제한다면")
        class Context_with_default_task {
            @BeforeEach
            void setUp() {
                final Task task = tasks.get(FIRST);
                given(service.deleteTask(TASK_ID)).willReturn(task);
            }

            @Test
            @DisplayName("HTTP Status Code 204 NO CONTENT 응답한다")
            void it_responds_with_204() throws Exception {
                mockMvc.perform(delete(DEFAULT_PATH + "/" + TASK_ID))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 삭제한다면")
        class Context_without_existing_task {
            @BeforeEach
            void setUp() {
                given(service.deleteTask(TASK_ID_NOT_EXISTING))
                        .willThrow(new TaskNotFoundException(TASK_ID_NOT_EXISTING));
            }

            @Test
            @DisplayName("HTTP Status Code 404 NOT FOUND 응답한다")
            void it_responds_with_404() throws Exception {
                mockMvc.perform(delete(DEFAULT_PATH + "/" + TASK_ID_NOT_EXISTING))
                        .andExpect(status().isNotFound());
            }

        }
    }
}