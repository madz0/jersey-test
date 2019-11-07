package com.github.madz0.jerseytest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.madz0.jerseytest.model.Transfer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.*;

public class AbstractUnitTest {
    protected EntityManager mockEntityManagerTransaction() {
        EntityTransaction transaction = mock(EntityTransaction.class);
        doNothing().when(transaction).begin();
        doNothing().when(transaction).commit();
        doNothing().when(transaction).rollback();
        EntityManager entityManager = mock(EntityManager.class);
        doReturn(transaction).when(entityManager).getTransaction();
        return entityManager;
    }

    public static class TestUtils {
        public static String createProperTransferJson(Transfer transfer) throws JsonProcessingException {
        /*TODO
        since objectMapper.disable(MapperFeature.USE_ANNOTATIONS) was deprecated
        could not find any other approach so implemented below code
         */
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(transfer);
            String finalJson = json.substring(0, json.length() - 1);
            finalJson = finalJson + ", \"from\":" + objectMapper.writeValueAsString(transfer.getFrom());
            finalJson = finalJson + ", \"to\":" + objectMapper.writeValueAsString(transfer.getTo());
            return finalJson + "}";
        }
    }
}
