package resource.database;

import com.github.santavaran.super_mario_235.code.DataBase;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.utbot.runtime.utils.java.UtUtils.createInstance;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.utbot.runtime.utils.java.UtUtils.setField;

public final class DataBaseTest {
    @Test
    public void testConnect1() throws Exception {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(java.sql.DriverManager.class);
            (mockedStatic.when(() -> java.sql.DriverManager.getConnection(any(String.class), any(String.class), any(String.class)))).thenReturn(((Connection) null));
            DataBase dataBase = ((DataBase) createInstance("resource.database.DataBase"));
            Connection actual = dataBase.connect();
            assertNull(actual);
        } finally {
            mockedStatic.close();
        }
    }
    @Test
    public void testCreateDB1() throws Exception {
        DataBase dataBase = ((DataBase) createInstance("resource.database.DataBase"));
        setField(dataBase, "resource.database.DataBase", "connection", null);

        dataBase.createDB();
    }
    @Test
    public void testGetAll1() throws Exception {
        DataBase dataBase = ((DataBase) createInstance("resource.database.DataBase"));
        setField(dataBase, "resource.database.DataBase", "connection", null);
        dataBase.getAll();
    }
    @Test
    public void testGetByUID1() throws Exception {
        DataBase dataBase = ((DataBase) createInstance("resource.database.DataBase"));
        setField(dataBase, "resource.database.DataBase", "connection", null);
        dataBase.getByUID(-255L);
    }
    @Test
    public void testSaveData1() throws Exception {
        org.mockito.MockedStatic mockedStatic = null;
        try {
            mockedStatic = mockStatic(LocalDate.class);
            (mockedStatic.when(LocalDate::now)).thenReturn(((LocalDate) null));
            DataBase dataBase = ((DataBase) createInstance("resource.database.DataBase"));
            dataBase.saveData(-255);
        } finally {
            mockedStatic.close();
        }
    }
}
