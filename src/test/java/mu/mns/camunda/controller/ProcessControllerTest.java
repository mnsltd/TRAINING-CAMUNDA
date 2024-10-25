package mu.mns.camunda.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProcessController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ProcessControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RuntimeService runtimeService;

    @Test
    void startProcess1_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process 1 started successfully with businessKey")));
    }

    @Test
    void startProcess2_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process MessageEventTaskExample started successfully with businessKey")));
    }

    @Test
    void startProcess3_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process BoundaryEventExample started successfully with businessKey")));
    }

    @Test
    void startProcess4_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process ExternalTaskExample started successfully with businessKey")));
    }

    @Test
    void startProcess5_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process VariablesExample started successfully with businessKey")));
    }

    @Test
    void startProcess6_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/6")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process CallActivityExample started successfully with businessKey")));
    }

    @Test
    void startProcess7_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process TransactionExample started successfully with businessKey")));
    }

    @Test
    void startProcess8_shouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/start-process/8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("camunda process SignalExample started successfully with businessKey")));
    }

    @Test
    void correlateMessage_shouldReturnOkStatus() throws Exception {
        ExecutionQuery executionQuery = mock(ExecutionQuery.class);

        // Ensure RuntimeService.createExecutionQuery() returns the mocked ExecutionQuery
        when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);

        // Mock the processInstanceBusinessKey method to return the mock ExecutionQuery
        when(executionQuery.processInstanceBusinessKey(anyString())).thenReturn(executionQuery);

        // Mock the signalEventSubscriptionName method to return the mock ExecutionQuery
        when(executionQuery.messageEventSubscriptionName(anyString())).thenReturn(executionQuery);

        // Mock unlimitedList() to return an empty list (since you don't need to test the logic)
        when(executionQuery.unlimitedList()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/message/{businessKey}", "businessKey")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void correlateMessageBoundary_shouldReturnOkStatus() throws Exception {
        ExecutionQuery executionQuery = mock(ExecutionQuery.class);

        // Ensure RuntimeService.createExecutionQuery() returns the mocked ExecutionQuery
        when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);

        // Mock the processInstanceBusinessKey method to return the mock ExecutionQuery
        when(executionQuery.processInstanceBusinessKey(anyString())).thenReturn(executionQuery);

        // Mock the signalEventSubscriptionName method to return the mock ExecutionQuery
        when(executionQuery.messageEventSubscriptionName(anyString())).thenReturn(executionQuery);

        // Mock unlimitedList() to return an empty list (since you don't need to test the logic)
        when(executionQuery.unlimitedList()).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/message/boundary/{businessKey}", "businessKey")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void correlateMessageExternal_shouldReturnOkStatus() throws Exception {
        ExecutionQuery executionQuery = mock(ExecutionQuery.class);

        // Ensure RuntimeService.createExecutionQuery() returns the mocked ExecutionQuery
        when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);

        // Mock the processInstanceBusinessKey method to return the mock ExecutionQuery
        when(executionQuery.processInstanceBusinessKey(anyString())).thenReturn(executionQuery);

        // Mock the signalEventSubscriptionName method to return the mock ExecutionQuery
        when(executionQuery.messageEventSubscriptionName(anyString())).thenReturn(executionQuery);

        // Mock unlimitedList() to return an empty list (since you don't need to test the logic)
        when(executionQuery.unlimitedList()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/message/external/{businessKey}", "businessKey")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void correlateMessageNotStarted_shouldReturnOkStatus() throws Exception {

        ExecutionQuery executionQuery = mock(ExecutionQuery.class);

        // Ensure RuntimeService.createExecutionQuery() returns the mocked ExecutionQuery
        when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);

        // Mock the processInstanceBusinessKey method to return the mock ExecutionQuery
        when(executionQuery.processInstanceBusinessKey(anyString())).thenReturn(executionQuery);

        // Mock the signalEventSubscriptionName method to return the mock ExecutionQuery
        when(executionQuery.messageEventSubscriptionName(anyString())).thenReturn(executionQuery);

        // Mock unlimitedList() to return an empty list (since you don't need to test the logic)
        when(executionQuery.unlimitedList()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/message/external/not-started/{businessKey}", "businessKey")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void correlateCancelMessage_shouldReturnOkStatus() throws Exception {
        ExecutionQuery executionQuery = mock(ExecutionQuery.class);

        // Ensure RuntimeService.createExecutionQuery() returns the mocked ExecutionQuery
        when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);

        // Mock the processInstanceBusinessKey method to return the mock ExecutionQuery
        when(executionQuery.processInstanceBusinessKey(anyString())).thenReturn(executionQuery);

        // Mock the signalEventSubscriptionName method to return the mock ExecutionQuery
        when(executionQuery.messageEventSubscriptionName(anyString())).thenReturn(executionQuery);

        // Mock unlimitedList() to return an empty list (since you don't need to test the logic)
        when(executionQuery.unlimitedList()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/message/cancel/{businessKey}", "businessKey")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void correlateCancelSignal_shouldReturnOkStatus() throws Exception {

        // Create a mock ExecutionQuery object
        ExecutionQuery executionQuery = mock(ExecutionQuery.class);

        // Ensure RuntimeService.createExecutionQuery() returns the mocked ExecutionQuery
        when(runtimeService.createExecutionQuery()).thenReturn(executionQuery);

        // Mock the processInstanceBusinessKey method to return the mock ExecutionQuery
        when(executionQuery.processInstanceBusinessKey(anyString())).thenReturn(executionQuery);

        // Mock the signalEventSubscriptionName method to return the mock ExecutionQuery
        when(executionQuery.signalEventSubscriptionName(anyString())).thenReturn(executionQuery);

        // Mock unlimitedList() to return an empty list (since you don't need to test the logic)
        when(executionQuery.unlimitedList()).thenReturn(Collections.emptyList());

        // Perform the test call using MockMvc
        mockMvc.perform(post("/signal/cancel/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

