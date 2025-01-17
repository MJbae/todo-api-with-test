package com.codesoom.assignment.application;


import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스 ")
class TaskServiceTest {
    private TaskService service;
    private final String TASK_TITLE = "Test Task";
    private final String TASK_TITLE_UPDATED = "Updated Task";
    private final Long TASK_ID = 1L;
    private final Long TASK_ID_NOT_EXISTING = 0L;

    @BeforeEach
    void setUp() {
        service = new TaskService();
        service.createTask(new Task(TASK_TITLE));
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {
        @Test
        @DisplayName("List 타입의 Collection으로 값을 반환한다")
        void it_returns_list() {
            final List<Task> actual = service.getTasks();

            assertThat(actual).isInstanceOf(List.class);
        }
    }

    @Nested
    @DisplayName("createTasks 메소드는")
    class Describe_createTasks {
        @Test
        @DisplayName("매개변수로 전달한 값이 반영된 Task를 반환한다")
        void it_returns_task_reflecting_params() {
            final Task task = new Task(TASK_TITLE_UPDATED);

            final Task actual = service.createTask(task);
            assertThat(actual.getTitle()).isEqualTo(TASK_TITLE_UPDATED);
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        abstract class ContextGetting {
            Task withExistingTask() {
                return service.getTask(TASK_ID);
            }

            void withoutExistingTask() {
                service.getTask(TASK_ID_NOT_EXISTING);
            }
        }

        @Nested
        @DisplayName("만약 존재하는 Task를 상세조회한다면")
        class Context_with_existing_task extends ContextGetting {
            @Test
            @DisplayName("매개변수로 전달한 값을 Id로 가지고 있는 Task를 반환한다")
            void it_returns_task_having_id_equal_to_param() {
                assertThat(withExistingTask().getId()).isEqualTo(TASK_ID);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 조회한다면")
        class Context_with_not_existing_task extends ContextGetting {
            @Test
            @DisplayName("예외를 발생시킨다")
            void it_throws_exception() {
                assertThatThrownBy(this::withoutExistingTask)
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        abstract class ContextUpdating {
            final Task task = new Task(TASK_TITLE_UPDATED);

            Task withExistingTask() {
                return service.updateTask(TASK_ID, task);
            }

            void withoutExistingTask() {
                service.updateTask(TASK_ID_NOT_EXISTING, task);
            }
        }

        @Nested
        @DisplayName("만약 존재하는 Task를 수정한다면")
        class Context_with_existing_task extends ContextUpdating {
            @Test
            @DisplayName("매개변수로 전달한 값을 Id로 가지고 있는 Task를 반환한다")
            void it_returns_task_having_id_equal_to_param() {
                assertThat(withExistingTask().getId()).isEqualTo(TASK_ID);
            }

            @Test
            @DisplayName("매개변수로 전달한 값이 반영된 Task를 반환한다")
            void it_returns_task_reflecting_params() {
                assertThat(withExistingTask().getTitle()).isEqualTo(TASK_TITLE_UPDATED);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 수정한다면")
        class Context_with_not_existing_task extends ContextUpdating {
            @Test
            @DisplayName("예외를 발생시킨다")
            void it_throws_exception() {
                assertThatThrownBy(this::withoutExistingTask)
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {
        abstract class ContextDeleting {
            Task withExistingTask() {
                return service.deleteTask(TASK_ID);
            }

            void withoutExistingTask() {
                service.deleteTask(TASK_ID_NOT_EXISTING);
            }
        }

        @Nested
        @DisplayName("만약 존재하는 Task를 삭제한다면")
        class Context_with_existing_task extends ContextDeleting {
            @Test
            @DisplayName("매개변수로 전달한 값을 Id로 가지고 있는 Task를 반환한다")
            void it_returns_task_having_id_equal_to_param() {
                assertThat(withExistingTask().getId()).isEqualTo(TASK_ID);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Task를 삭제한다면")
        class Context_with_not_existing_task extends ContextDeleting {

            @Test
            @DisplayName("예외를 발생시킨다")
            void it_throws_exception() {
                assertThatThrownBy(this::withoutExistingTask)
                        .isInstanceOf(TaskNotFoundException.class);
            }

        }
    }

}
